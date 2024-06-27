package com.app.ehealthai.doctor.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import com.app.ehealthai.doctor.models.requests.SnapData
import com.app.ehealthai.doctor.models.responses.uploadSnapResponse.SnapResponse
import com.app.ehealthai.doctor.models.responses.uploadSnapResponse.SnapResponseData
import com.app.ehealthai.doctor.ui.activities.PatientDetails.PatientAppointment
import com.app.ehealthaidoctor.R
import com.app.ehealthaidoctor.models.responses.UploadPatientFileResponse
import com.app.ehealthaidoctor.network.ApiUtils
import com.app.ehealthaidoctor.utils.SharedPrefs
import com.example.ehealthai.utils.Constants
import com.example.ehealthai.utils.toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException

class SnapUploadService : Service() {
    companion object {
        private const val NOTIFICATION_ID = 1234
        private const val CHANNEL_ID = "snap_upload_channel_id"
        private const val CHANNEL_NAME = "Snap Upload Notification"
        private const val TAG = "SnapUploadService"
        const val ACTION_ADD_FILES = "ACTION_ADD_FILES"
        const val EXTRA_FILE_PATH_1 = "EXTRA_FILE_PATH_1"
        const val EXTRA_FILE_NAME = "EXTRA_FILE_NAME"
        const val EXTRA_PATIENT_ID = "EXTRA_PATIENT_ID"
        const val APPOINTMENT_ID = "APPOINTMENT_ID"
    }

    private val serviceScope = CoroutineScope(Dispatchers.IO)
    private val snapsList = ArrayList<SnapData>()
    private var filesToUpload = 0
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, createNotification())
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == ACTION_ADD_FILES) {
            val filePath = intent.getStringExtra(EXTRA_FILE_PATH_1)
            val patientId = intent.getStringExtra(EXTRA_PATIENT_ID)
            val appointmentId = intent.getStringExtra(APPOINTMENT_ID)

            Log.e(TAG, "onStartCommand: ${filePath}" )

            val userData = SharedPrefs.getUserData(this)
            val file = filePath?.let { File(it) }
            if (userData != null) {
                val userName = userData.username.toString()
                snapsList.add(
                    SnapData(
                        patientId.toString(),
                        Constants.SNAP_DOC_TYPE_TAG,
                        userName,
                        file!!,
                        filePath,
                        Constants.SNAP_FILE_DESCRIPTION_TAG,
                        appointmentId.toString()
                    )
                )
                filesToUpload = snapsList.size

                try {
//                    if (Constants.isInternetConnected(this@SnapUploadService)) {
                        startNextFileUpload()
//                    } else {
//                        toast("No internet connection")
//                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Error uploading files: ${e.message}")
                }

            }else{
                Log.e(TAG, "user name not found")
                stopForeground(STOP_FOREGROUND_REMOVE)
                stopSelf()
            }

        }


        Log.e(TAG, "onStartCommand: ${snapsList.size}")
        return START_STICKY
    }

    fun startNextFileUpload() {
        if (snapsList?.size > 0) {
            snapsList[0].let { snapData ->
                serviceScope.launch(Dispatchers.IO) {
                    try {
                        fileUpload(
                            snapData.patientId,
                            snapData.docType,
                            snapData.userName,
                            snapData.file,
                            snapData.description,
                            snapData.appointmentid
                        )
                    } catch (e: Exception) {
                        Log.e(TAG, "Error uploading files: ${e.message}")
                    }
                }

            }
        } else {
            stopForeground(STOP_FOREGROUND_REMOVE)
            stopSelf()
        }
    }


    private fun fileUpload(
        mPatientId: String,
        docType: String,
        userName: String,
        mFile: File,
        mDescription: String,
        mAppointmentId: String
    ) {

        val patientId = RequestBody.create("pid".toMediaTypeOrNull(), mPatientId)
        val doc_type = RequestBody.create("doc_type".toMediaTypeOrNull(), docType)
        val username = RequestBody.create("username".toMediaTypeOrNull(), userName)
        val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), mFile)
        val body =
            MultipartBody.Part.createFormData("uploadfile", mFile.name.toString(), requestFile)
        val description = RequestBody.create("description".toMediaTypeOrNull(), mDescription)
        val apptId = RequestBody.create("appointmentid".toMediaTypeOrNull(), mAppointmentId)


        ApiUtils.getAPIService(this).uploadDoctorSnap(
            patientId,
            username,
            description,
            apptId,
            body,
            doc_type
        ).enqueue(object : Callback<SnapResponse> {
            override fun onFailure(
                call: Call<SnapResponse>, t: Throwable
            ) {
//                if (filesRetries <= 3) {
                //TODO only for debug purpose
//                toast("Error while uploading snapshot")
                Handler(Looper.getMainLooper()).postDelayed({
                    if (Constants.isInternetConnected(this@SnapUploadService)) {
//                            filesRetries++
                        startNextFileUpload()
                    }
                }, 30000)
//                }

            }

            override fun onResponse(
                call: Call<SnapResponse>,
                response: Response<SnapResponse>
            ) {
                try {
                    if (response.isSuccessful && response.body() != null && response.body()!!.statuscode == 200) {
                        if (response.body()!!.snapResponseData != null) {
                            if (response.body()!!.snapResponseData!!.vid != null) {
//                                filesRetries = 0
                                //TODO only for debug purpose
//                                toast("Snapshot uploaded")
                                if (snapsList?.size > 0) {
                                    snapsList[0].let {
                                        //delete file at index 0
                                        try {
                                            val file = File(it.filePath)
                                            file.delete()
                                        } catch (ignored: Exception) {
                                        }
                                    }
                                    snapsList.removeAt(0)
                                    startNextFileUpload()
                                }
                            }
                        }

                    } else {
                        //TODO only for debug purpose
//                        toast("Error while uploading snapshot")
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    //TODO only for debug purpose
//                    toast("$e Error while uploading snapshot")
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }


    private fun createNotification(): Notification {
        val notificationBuilder = NotificationCompat.Builder(
            this,
            CHANNEL_ID)
            .setContentTitle("Uploading snaps")
            .setSmallIcon(R.drawable.smallappicon)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)

        return notificationBuilder.build()
    }
}
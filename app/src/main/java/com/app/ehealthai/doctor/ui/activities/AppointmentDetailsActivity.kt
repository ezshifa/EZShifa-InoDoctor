package com.app.ehealthaidoctor.ui.activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentUris
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.net.Uri
import android.os.*
import android.provider.DocumentsContract
import androidx.appcompat.app.AppCompatActivity
import android.provider.MediaStore
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.MotionEvent
import com.app.ehealthaidoctor.R
import com.app.ehealthaidoctor.adapters.recyclerViewAdapters.UploadedFilesAppointmentDetailsRecyclerViewAdapter
import kotlinx.android.synthetic.main.activity_appointment_details.*
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.app.ehealthai.network.firebase.FirebaseUtils
import com.app.ehealthaidoctor.models.responses.*
import com.app.ehealthaidoctor.network.ApiUtils

import com.app.ehealthaidoctor.ui.dialogs.RecordAudioDialog
import com.app.ehealthaidoctor.ui.dialogs.ViewImageDialog
import com.app.ehealthaidoctor.utils.SharedPrefs
import com.crashlytics.android.Crashlytics
import com.example.ehealthai.utils.toast
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
//import com.crashlytics.android.Crashlytics
import com.example.ehealthai.utils.showCancelDialog
import com.github.piasy.rxandroidaudio.AudioRecorder
import io.fabric.sdk.android.Fabric
//import io.fabric.sdk.android.Fabric
import kotlinx.android.synthetic.main.activity_appointment_details.appointment_details_back_btn
import kotlinx.android.synthetic.main.activity_appointment_details.doctor_notes_text
import kotlinx.android.synthetic.main.activity_appointment_details.progress_1
import kotlinx.android.synthetic.main.activity_appointment_details.save_btn
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.fragment_addnotes.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.File

class AppointmentDetailsActivity : AppCompatActivity() {


    var doctorId: String? = null
    var appointmentId: String? = null
    var appointmentIdSend: String? = null
    var patientId: String? = null
    var patientName: String? = null
    var doc_notes: String? = null
    var description: String? = null
    var doctorName: String? = null
    var apptstatus: String? = null
    private val REQUESTED_PERMISSIONS = arrayOf(
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA
    )
    var doctorSpeciality: String? = null
    private val PERMISSION_REQ_ID = 22

    //  var mAudioRecorder: MediaRecorder? = null
    var mAudioRecorder: AudioRecorder? = null

    val SPEACH_RECOGNITION_REQUEST_CODE: Int = 1
    var uploadedFilesAppointmentDetailsRecyclerViewAdapter: UploadedFilesAppointmentDetailsRecyclerViewAdapter? =
        null
    val time = "00:00"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fabric.with(this, Crashlytics())
        setContentView(R.layout.activity_appointment_details)


        home.setOnClickListener {
//         val intent = Intent(applicationContext, HomeActivity::class.java)
//          intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
//          startActivity( intent)
            finish()
        }

        doctor_notes_text.setOnTouchListener { view, event ->
            view.parent.requestDisallowInterceptTouchEvent(true)
            if ((event.action and MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {
                view.parent.requestDisallowInterceptTouchEvent(false)
            }
            return@setOnTouchListener false
        }


        init()

//       doctor_notes_text.setScroller(Scroller(this))
//       // doctor_notes_text.maxLines =
//        doctor_notes_text.isVerticalScrollBarEnabled = true
        doctor_notes_text.movementMethod = ScrollingMovementMethod()


        if (checkSelfPermission(REQUESTED_PERMISSIONS[0], PERMISSION_REQ_ID) &&
            checkSelfPermission(REQUESTED_PERMISSIONS[1], PERMISSION_REQ_ID) &&
            checkSelfPermission(REQUESTED_PERMISSIONS[2], PERMISSION_REQ_ID)
        ) {

            initRecordView()
        }

        picButton.setOnClickListener {
            selectImageInAlbum()
        }

        recButton.setOnClickListener {

            if (start.equals("0")) {
                onStartRec()
                start = "1"
                rectext.visibility = View.VISIBLE
                saved_text.visibility = View.INVISIBLE
                recButton.setBackgroundDrawable(getDrawable(R.drawable.stop_record_ic))
            } else {
                onStopRec()
                start = "0"
                rectext.visibility = View.INVISIBLE
                recButton.setBackgroundDrawable(getDrawable(R.drawable.record_audio_ic))
            }
        }

        delButton.setOnClickListener {

            if (mAudioRecorder != null) {
                mAudioRecorder!!.stopRecord()
                RecordAudioDialog.mAudioFile = null
                start = "0"
                rectext.visibility = View.INVISIBLE
                saved_text.visibility = View.INVISIBLE
                recButton.setBackgroundDrawable(getDrawable(R.drawable.record_audio_ic))
            }

        }


    }

    @SuppressLint("Range")
    private fun imagePath(uri: Uri?, selection: String?): String {
        var path: String? = null
//        通过Uri和selection获取路径
        val cursor = contentResolver.query(uri!!, null, selection, null, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
            }
            cursor.close()
        }
        return path!!
    }

    val REQUEST_SELECT_IMAGE_IN_ALBUM = 1

    var selsetion: String? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {

            REQUEST_SELECT_IMAGE_IN_ALBUM ->
                if (resultCode == Activity.RESULT_OK) {
                    if (Build.VERSION.SDK_INT >= 19) {
                        Log.e("hello", "pic")
//                        4.4以上
                        var imagePath: String? = null
                        val uri = data!!.data
                        if (DocumentsContract.isDocumentUri(this, uri)) {
//            document类型的Uri，用document id处理
                            Log.e("hello", uri.toString())
                            val docId = DocumentsContract.getDocumentId(uri)
                            if ("com.android.providers.media.documents" == uri!!.authority) {
                                val id = docId.split(":")[1]
                                selsetion = MediaStore.Images.Media._ID + "=" + id
                                imagePath = imagePath(
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                    selsetion
                                )
                            } else if ("com.android.providers.downloads.documents" == uri!!.authority) {
                                val contentUri = ContentUris.withAppendedId(
                                    Uri.parse("content://downloads/public_downloads"),
                                    java.lang.Long.valueOf(docId)
                                )
                                imagePath = imagePath(contentUri, null)
                            }


                            /*
                                                        var path: String? = null
                            //        通过Uri和selection获取路径
                                                        val cursor = contentResolver.query(uri, null, selsetion, null, null )
                                                        if (cursor != null) {
                                                            if (cursor.moveToFirst()) {
                                                                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
                                                            }
                                                            cursor.close()
                                                        }
                            */

                            val rnds = (10000..1000000).random()
                            var file = File(imagePath)
                            val requestFile =
                                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
                            Log.e("Hello", requestFile.toString())
                            val stringName = "$rnds.png"
                            Log.e("Hello", stringName)
                            val body = MultipartBody.Part.createFormData(
                                "uploadfile",
                                stringName,
                                requestFile
                            )

                            // var f:FileInputStream
                            val apptId = RequestBody.create(
                                "apptid".toMediaTypeOrNull(),
                                appointmentId!!
                            )
                            val patientId = RequestBody.create(
                                "pid".toMediaTypeOrNull(),
                                patientId!!
                            )
                            val doctorId = RequestBody.create(
                                "doctorid".toMediaTypeOrNull(),
                                doctorId!!
                            )
                            val doc_type = RequestBody.create(
                                "doc_type".toMediaTypeOrNull(),
                                "doc_notes"
                            )
                            val description = RequestBody.create(
                                "description".toMediaTypeOrNull(),
                                doctor_notes_text.text.toString()
                            )
                            val userData = SharedPrefs.getUserData(this)
                            val sessionid = RequestBody.create(
                                "sessionid".toMediaTypeOrNull(),
                                SharedPrefs.getString(this, "sessionid")!!
                            )
                            val username = RequestBody.create(
                                "username".toMediaTypeOrNull(),
                                userData!!.username.toString()
                            )
                            val usertype = RequestBody.create(
                                "usertype".toMediaTypeOrNull(),
                                "DOCTOR"
                            )
                            val fdate = RequestBody.create(
                                "followup_date".toMediaTypeOrNull(),
                                followupdate.text.toString()
                            )
                            sendFileUploadRequest(
                                apptId,
                                patientId,
                                doctorId,
                                doc_type,
                                body,
                                description,
                                sessionid,
                                username,
                                usertype,
                                fdate
                            )


                        }// else {
//                        4.4以下
                        //      handleImageBeforeKitkat(data)
                        //    }
                    }
                }
        }

    }

    fun selectImageInAlbum() {

        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        if (intent.resolveActivity(packageManager) != null) {
            startActivityForResult(intent, REQUEST_SELECT_IMAGE_IN_ALBUM)
        }
    }

    var start: String = "0"

    override fun onResume() {
        super.onResume()


    }


    fun onStartRec() {
        //   mAudioRecorder = AudioRecorder.getInstance()
        //saved_text.visibility = View.GONE
        RecordAudioDialog.mAudioFile =
            File(
                Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + File.separator + System.nanoTime() + "_recording.m4a"
            )


        mAudioRecorder!!.prepareRecord(
            MediaRecorder.AudioSource.MIC,
            MediaRecorder.OutputFormat.MPEG_4,
            MediaRecorder.AudioEncoder.AAC,
            RecordAudioDialog.mAudioFile
        )
        mAudioRecorder!!.startRecord()
    }


    fun onStopRec() {

        if (mAudioRecorder != null) {
            saved_text.visibility = View.VISIBLE
            mAudioRecorder!!.stopRecord()
//
        }
    }

    fun initRecordView() {
        record_view.setSoundEnabled(true)
        //  record_view.setLessThanSecondAllowed(false)
        // record_view.setSlideToCancelText("Slide to cancel")

        /*mAudioRecorder = MediaRecorder()

        mAudioRecorder!!.setAudioSource(MediaRecorder.AudioSource.MIC)
        mAudioRecorder!!.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        mAudioRecorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
        mAudioRecorder!!.setOutputFile(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + System.nanoTime() + "_recording")*/



        mAudioRecorder = AudioRecorder.getInstance()


//        record_view.setOnRecordListener(object : OnRecordListener {
//
//            override fun onFinish(recordTime: Long) {
//                if (mAudioRecorder != null) {
//                    saved_text.visibility = View.VISIBLE
//                    mAudioRecorder!!.stopRecord()
////                    if (RecordAudioDialog.mAudioFile != null) {
////                        val requestFile =
////                            RequestBody.create(MediaType.parse("multipart/form-data"), RecordAudioDialog.mAudioFile!!)
////                        val stringName = RecordAudioDialog.mAudioFile.toString().split("/")
////                        val body = MultipartBody.Part.createFormData("uploadfile", stringName[4], requestFile)
////
////                        val patientId = RequestBody.create(MediaType.parse("pid"), patientId!!)
////                        val doctorId = RequestBody.create(MediaType.parse("doctorid"), doctorId!!)
////                        val doc_type = RequestBody.create(MediaType.parse("doc_type"), RecordAudioDialog.mAudioFile!!.extension)
////
////                        sendFileUploadRequest(patientId, doctorId, doc_type, body)
////                    }
//                }
//
//                /*PlayConfig.file(mAudioFile) // play a local file
//                    //.res(getApplicationContext(), R.raw.audio_record_end) // or play a raw resource
//                    .looping(false) // loop or not
//                    .leftVolume(1.0F) // left volume
//                    .rightVolume(1.0F) // right volume
//                    .build()*/
//            }
//
//            override fun onLessThanSecond() {
//                if (mAudioRecorder != null) {
//                    mAudioRecorder!!.stopRecord()
//                    RecordAudioDialog.mAudioFile = null
//                }
//            }
//
//            override fun onCancel() {
//                if (mAudioRecorder != null) {
//                    mAudioRecorder!!.stopRecord()
//                    RecordAudioDialog.mAudioFile = null
//                }
//            }
//
//            override fun onStart() {
//                saved_text.visibility = View.GONE
//                RecordAudioDialog.mAudioFile =
//                    File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + System.nanoTime() + "_recording.m4a")
//
//
//                mAudioRecorder!!.prepareRecord(
//                    MediaRecorder.AudioSource.MIC,
//                    MediaRecorder.OutputFormat.MPEG_4,
//                    MediaRecorder.AudioEncoder.AAC,
//                    RecordAudioDialog.mAudioFile
//                )
//                mAudioRecorder!!.startRecord()
//            }
//
//        })

        //   record_button.setRecordView(record_view)
        // record_button.isListenForRecord = true

//        record_button.setOnRecordClickListener {
//            saved_text.visibility = View.GONE
//        }

    }

    fun init() {


        doctorId = intent.getStringExtra("doctorId")
        appointmentId = intent.getStringExtra("appointmentId")
        appointmentIdSend = intent.getStringExtra("appointmentId")
        patientId = intent.getStringExtra("patientId")
        description = intent.getStringExtra("description")
        patientName = intent.getStringExtra("patientName")
        doc_notes = intent.getStringExtra("doctor_notes")
        apptstatus = intent.getStringExtra("apptstatus")

        if (apptstatus.equals("closed")) {
            set.visibility = View.INVISIBLE
            prescription_title.visibility = View.INVISIBLE
            save_btn.visibility = View.GONE
            save_rec.visibility = View.GONE
            cancel_btn.visibility = View.GONE
            start_call_btn.visibility = View.INVISIBLE
            save_notes.visibility = View.VISIBLE
//            doctor_notes_text.isEnabled= false
//            doctor_notes_text.visibility = View.INVISIBLE
//            doctor_notes_text_box.visibility = View.VISIBLE
            Log.e("appt", "closed")

        } else if (apptstatus!!.contains("cancelled")) {
            set.visibility = View.INVISIBLE
            prescription_title.visibility = View.INVISIBLE
            save_btn.visibility = View.GONE
            save_rec.visibility = View.GONE
            cancel_btn.visibility = View.GONE
            start_call_btn.visibility = View.INVISIBLE
            doctor_notes_text.isEnabled = false
            doctor_notes_text.visibility = View.INVISIBLE
            doctor_notes_text_box.visibility = View.VISIBLE
            save_notes.visibility = View.GONE
            Log.e("appt", "cancelled")
//
        }

        Log.e("appointmrntID", appointmentId!!)
//        if (doc_notes == null) {
//
//        } else {
//            Log.e("doctor Notes", doc_notes!!)
//        }

        appointment_no.text = intent.getStringExtra("patientName")

        doctor_notes_text.setText(doc_notes)// = doc_notes.toString()
        if (doc_notes.equals("") || doc_notes.equals(" ")) {
            doctor_notes_text_box.setText("No Doctor Notes Available")
        } else {
            doctor_notes_text_box.setText(doc_notes)
        }

        val userData = SharedPrefs.getUserData(this@AppointmentDetailsActivity)
        doctorId = userData!!.id.toString()
        doctorName = userData.fname + " " + userData.lname
        doctorSpeciality = userData.specialty

        start_call_btn.setOnClickListener {

            //      if (checkSelfPermission(REQUESTED_PERMISSIONS[0], PERMISSION_REQ_ID) && checkSelfPermission(REQUESTED_PERMISSIONS[1], PERMISSION_REQ_ID)) {


//            val intent = Intent(this, VideoCallActivity::class.java)

            var chatId: String = "chat_" + patientId + "_" + doctorId

            //  notify = true

            FirebaseUtils.sendMessage(
                doctorId!!,
                patientId!!,
                "The doctor has initiated the telemedicine video chat room.",
                "text",
                System.currentTimeMillis().toString(),
                patientName!!,
                doctorName!!,
                doctorId!!,
                doctorSpeciality!!,
                userData.image_name!!,
                "",
                messageSent = {
                    sendNotification(
                        patientId!!, "Message from " + doctorName + ""
                    )
                    // newMessageEditText.setText("")
                }
            )


//            intent.putExtra("roomId", chatId)
//            intent.putExtra("doctorId", doctorId)
//            intent.putExtra("patientId", patientId)
//            intent.putExtra("patientName", patientName)
//            startActivity(intent)


            //       }
        }

        cancel_btn.setOnClickListener {
            showCancelDialog {
                progress_1.visibility = View.VISIBLE
                val userData = SharedPrefs.getUserData(this)
                val username = userData!!.fname + " " + userData.lname
                val usertype = "DOCTOR"
                ApiUtils.getAPIService(this).cancelAppointmentRequest(
                    appointmentId!!,
                    doctorId!!,
                    SharedPrefs.getString(this@AppointmentDetailsActivity, "sessionid")!!,
                    username,
                    usertype
                ).enqueue(object : Callback<CancelAppointmentResponse> {
                    override fun onFailure(call: Call<CancelAppointmentResponse>, t: Throwable) {
                        progress_1.visibility = View.GONE
                        toast("Something went wrong")
                    }

                    override fun onResponse(
                        call: Call<CancelAppointmentResponse>,
                        response: Response<CancelAppointmentResponse>
                    ) {
                        progress_1.visibility = View.GONE
                        toast("Appointment cancelled !")
                        val intent = Intent(
                            this@AppointmentDetailsActivity,
                            AppointmentsActivity::class.java
                        )
                        startActivity(intent)
                        setResult(2)
                        finish()
                    }
                })
            }

        }


        appointment_details_back_btn.setOnClickListener { onBackPressed() }

        // uploaded_files_recycler_view.layoutManager = LinearLayoutManager(this@AppointmentDetailsActivity)

        uploaded_files_btn.setOnClickListener {
            val intent =
                Intent(this@AppointmentDetailsActivity, PatientDocumentsActivity::class.java)
            intent.putExtra("patientId", patientId)
            startActivity(intent)
        }
        //  getPatientFiles(patientId!!)

        if (description != "null" && description != "") {
            description_text.text = description
        } else {
            description_text.text = "No description available"
        }


        speak_btn.setOnClickListener {
            speak_btn.isEnabled = false
            //   startSpeechRecognition()
            // startSpeechRecorder()
            saved_text.visibility = View.GONE
            val newDialog = RecordAudioDialog(returnAudioFile = {
                val stringName = it.split("/")
                prescription_file.text = getString(R.string.recorded_file) + " " + stringName[4]

            })
            newDialog.show(supportFragmentManager, "record Audio")
            supportFragmentManager.executePendingTransactions()
            speak_btn.isEnabled = true
        }

        save_btn.setOnClickListener {
            val userData = SharedPrefs.getUserData(this@AppointmentDetailsActivity)
            val username = userData!!.fname + " " + userData.lname
            val usertype = "DOCTOR"
            ApiUtils.getAPIService(this@AppointmentDetailsActivity).closeAppointmentRequest(
                appointmentIdSend!!,
                SharedPrefs.getString(this@AppointmentDetailsActivity, "sessionid")!!,
                username,
                usertype
            ).enqueue(object : Callback<CloseAppointmentResponse> {
                override fun onFailure(call: Call<CloseAppointmentResponse>, t: Throwable) {
                    progress_1.visibility = View.GONE
                    toast("Something went wrong")
                }

                override fun onResponse(
                    call: Call<CloseAppointmentResponse>,
                    response: Response<CloseAppointmentResponse>
                ) {
                    progress_1.visibility = View.GONE
                    toast("Appointment closed !")

                    val patientId = RequestBody.create("pid".toMediaTypeOrNull(), patientId!!)
                    val doctorId = RequestBody.create("doctorid".toMediaTypeOrNull(), doctorId!!)
                    val doc_type = RequestBody.create("doc_type".toMediaTypeOrNull(), "")
                    val description = RequestBody.create(
                        "description".toMediaTypeOrNull(),
                        doctor_notes_text.text.toString()
                    )
                    val apptId = RequestBody.create("apptid".toMediaTypeOrNull(), appointmentId!!)
                    val sessionid = RequestBody.create(
                        "sessionid".toMediaTypeOrNull(),
                        SharedPrefs.getString(this@AppointmentDetailsActivity, "sessionid")!!
                    )
                    val username = RequestBody.create(
                        "username".toMediaTypeOrNull(),
                        userData!!.username.toString()
                    )
                    val usertype = RequestBody.create("usertype".toMediaTypeOrNull(), "DOCTOR")
                    val fdate = RequestBody.create(
                        "followup_date".toMediaTypeOrNull(),
                        followupdate.text.toString()
                    )
                    sendFileUploadRequest2(
                        apptId,
                        patientId,
                        doctorId,
                        doc_type,
                        description,
                        sessionid,
                        username,
                        usertype,
                        fdate
                    )
                    //  showSuccessDialog {

                    // }

                }
            })

        }

        save_notes.setOnClickListener {
            val patientId = RequestBody.create("pid".toMediaTypeOrNull(), patientId!!)
            val doctorId = RequestBody.create("doctorid".toMediaTypeOrNull(), doctorId!!)
            val doc_type = RequestBody.create("doc_type".toMediaTypeOrNull(), "")
            val description = RequestBody.create(
                "description".toMediaTypeOrNull(),
                doctor_notes_text.text.toString()
            )
            val apptId = RequestBody.create("apptid".toMediaTypeOrNull(), appointmentId!!)
            val sessionid = RequestBody.create(
                "sessionid".toMediaTypeOrNull(),
                SharedPrefs.getString(this, "sessionid")!!
            )
            val username =
                RequestBody.create("username".toMediaTypeOrNull(), userData!!.username.toString())
            val usertype = RequestBody.create("usertype".toMediaTypeOrNull(), "DOCTOR")
            val fdate = RequestBody.create(
                "followup_date".toMediaTypeOrNull(),
                followupdate.text.toString()
            )
            sendFileUploadRequest3(
                apptId,
                patientId,
                doctorId,
                doc_type,
                description,
                sessionid,
                username,
                usertype,
                fdate
            )

        }
        save_rec.setOnClickListener {
            progress_1.visibility = View.VISIBLE
            if (RecordAudioDialog.mAudioFile != null) {
                val requestFile =
                    RequestBody.create(
                        "multipart/form-data".toMediaTypeOrNull(),
                        RecordAudioDialog.mAudioFile!!
                    )
                val stringName = RecordAudioDialog.mAudioFile.toString().split("/")
                val body =
                    MultipartBody.Part.createFormData("uploadfile", stringName[4], requestFile)
                val patientId = RequestBody.create("pid".toMediaTypeOrNull(), patientId!!)
                val doctorId = RequestBody.create("doctorid".toMediaTypeOrNull(), doctorId!!)
                val doc_type = RequestBody.create(
                    "doc_type".toMediaTypeOrNull(),
                    RecordAudioDialog.mAudioFile!!.extension
                )
                val description = RequestBody.create(
                    "description".toMediaTypeOrNull(),
                    doctor_notes_text.text.toString()
                )
                val apptId = RequestBody.create("apptid".toMediaTypeOrNull(), appointmentId!!)
                val sessionid = RequestBody.create(
                    "sessionid".toMediaTypeOrNull(),
                    SharedPrefs.getString(this, "sessionid")!!
                )
                val username = RequestBody.create(
                    "username".toMediaTypeOrNull(),
                    userData!!.username.toString()
                )
                val usertype = RequestBody.create("usertype".toMediaTypeOrNull(), "DOCTOR")
                val fdate = RequestBody.create(
                    "followup_date".toMediaTypeOrNull(),
                    followupdate.text.toString()
                )
                sendFileUploadRequest(
                    apptId,
                    patientId,
                    doctorId,
                    doc_type,
                    body,
                    description,
                    sessionid,
                    username,
                    usertype,
                    fdate
                )

            } else {

                val patientId = RequestBody.create("pid".toMediaTypeOrNull(), patientId!!)
                val doctorId = RequestBody.create("doctorid".toMediaTypeOrNull(), doctorId!!)
                val doc_type = RequestBody.create("doc_type".toMediaTypeOrNull(), "")
                val description = RequestBody.create(
                    "description".toMediaTypeOrNull(),
                    doctor_notes_text.text.toString()
                )
                val apptId = RequestBody.create("apptid".toMediaTypeOrNull(), appointmentId!!)
                val sessionid = RequestBody.create(
                    "sessionid".toMediaTypeOrNull(),
                    SharedPrefs.getString(this, "sessionid")!!
                )
                val username = RequestBody.create(
                    "username".toMediaTypeOrNull(),
                    userData!!.username.toString()
                )
                val usertype = RequestBody.create("usertype".toMediaTypeOrNull(), "DOCTOR")
                val fdate = RequestBody.create(
                    "followup_date".toMediaTypeOrNull(),
                    followupdate.text.toString()
                )
                sendFileUploadRequest1(
                    apptId,
                    patientId,
                    doctorId,
                    doc_type,
                    description,
                    sessionid,
                    username,
                    usertype,
                    fdate
                )

                progress_1.visibility = View.GONE
                toast("No Files Updated")
            }


        }

    }


    fun getPatientFiles(patientId: String) {
        val userData = SharedPrefs.getUserData(this)
        val username = userData!!.fname + " " + userData.lname
        val usertype = "DOCTOR"
        ApiUtils.getAPIService(this@AppointmentDetailsActivity).getPatientDocuments(
            patientId,
            SharedPrefs.getString(this@AppointmentDetailsActivity, "sessionid")!!,
            username,
            usertype
        )
            .enqueue(object : Callback<GetPatientDocumentsResponse> {
                override fun onFailure(call: Call<GetPatientDocumentsResponse>, t: Throwable) {
                    toast("Something went wrong")
                }

                override fun onResponse(
                    call: Call<GetPatientDocumentsResponse>,
                    response: Response<GetPatientDocumentsResponse>
                ) {
                    try {
                        if (response.isSuccessful && response.body() != null && response.body()!!.statuscode == 200 && response.body()!!.data.documents != null) {
                            uploadedFilesAppointmentDetailsRecyclerViewAdapter =
                                UploadedFilesAppointmentDetailsRecyclerViewAdapter(
                                    this@AppointmentDetailsActivity,
                                    response.body()!!.data.documents!!,
                                    openDialog = {
                                        val newDialog = ViewImageDialog(it)
                                        newDialog.show(supportFragmentManager, "open Image")
                                        supportFragmentManager.executePendingTransactions()
                                    })
                            //    uploaded_files_recycler_view.adapter = uploadedFilesAppointmentDetailsRecyclerViewAdapter
                        }
                    } catch (e: Exception) {
                        toast("Error: Try again")
                    }
                }
            })
    }


    fun sendFileUploadRequest(
        appointmentId: RequestBody,
        patientId: RequestBody,
        doctorId: RequestBody,
        doc_type: RequestBody,
        file: MultipartBody.Part,
        description: RequestBody,
        sessionid: RequestBody,
        username: RequestBody,
        usertype: RequestBody,
        fdate: RequestBody
    ) {

        ApiUtils.getAPIService(this@AppointmentDetailsActivity)
            .uploadPatientFile(
                appointmentId,
                patientId,
                doctorId,
                doc_type,
                file,
                description,
                sessionid,
                username,
                usertype,
                fdate
            )
            .enqueue(object : Callback<UploadPatientFileResponse> {
                override fun onFailure(call: Call<UploadPatientFileResponse>, t: Throwable) {
                    progress_1.visibility = View.GONE
                    toast("Something went wrong")
                    Log.e("Hello", t.toString())
                    toast(t.toString())
                }

                override fun onResponse(
                    call: Call<UploadPatientFileResponse>,
                    response: Response<UploadPatientFileResponse>
                ) {
                    progress_1.visibility = View.GONE
                    try {
                        if (response.isSuccessful && response.body() != null && response.body()!!.statuscode == 200) {
                            toast("File Uploaded")
                            //                        showSuccessDialog {
//                            finish()
//                        }
                        } else {
                            progress_1.visibility = View.GONE
                            toast("Please try again with a different file")
                        }
                    } catch (e: Exception) {
                        toast("Error: Try again")
                    }
                }
            })

    }


    fun sendFileUploadRequest1(
        appointmentId: RequestBody,
        patientId: RequestBody,
        doctorId: RequestBody,
        doc_type: RequestBody,

        description: RequestBody,
        sessionid: RequestBody,
        username: RequestBody,
        usertype: RequestBody,
        fdate: RequestBody
    ) {
        ApiUtils.getAPIService(this@AppointmentDetailsActivity)
            .uploadPatientFile1(
                appointmentId,
                patientId,
                doctorId,
                doc_type,
                description,
                sessionid,
                username,
                usertype,
                fdate
            )
            .enqueue(object : Callback<UploadPatientFileResponse> {
                override fun onFailure(call: Call<UploadPatientFileResponse>, t: Throwable) {
                    progress_1.visibility = View.GONE
                    toast("Something went wrong")
                    Log.e("Hello", t.toString())
                    //  toast(t.toString())
                }

                override fun onResponse(
                    call: Call<UploadPatientFileResponse>,
                    response: Response<UploadPatientFileResponse>
                ) {
                    progress_1.visibility = View.GONE
                    try {
                        if (response.isSuccessful && response.body() != null && response.body()!!.statuscode == 200) {

                            /*
                                                    ApiUtils.getAPIService(this@AppointmentDetailsActivity).closeAppointmentRequest(appointmentIdSend!!).enqueue(object : Callback<CloseAppointmentResponse> {
                                                        override fun onFailure(call: Call<CloseAppointmentResponse>, t: Throwable) {
                                                            progress_1.visibility = View.GONE
                                                            toast("Something went wrong")
                                                        }

                                                        override fun onResponse(call: Call<CloseAppointmentResponse>, response: Response<CloseAppointmentResponse>) {
                                                            progress_1.visibility = View.GONE
                                                         //   toast("Appointment closed !")

                                                            showSuccessDialog {
                                                                finish()
                                                            }

                                                        }
                                                    })
                            */
//                        showSuccessDialog {
//                            finish()
//                        }
                        } else {
                            progress_1.visibility = View.GONE
                            //  toast("Please try again with a different file")
                        }
                    } catch (e: Exception) {
                        toast("Error: Try again")
                    }
                }
            })

    }

    fun sendFileUploadRequest3(
        appointmentId: RequestBody,
        patientId: RequestBody,
        doctorId: RequestBody,
        doc_type: RequestBody,
        description: RequestBody,
        sessionid: RequestBody,
        username: RequestBody,
        usertype: RequestBody,
        fdate: RequestBody
    ) {
        ApiUtils.getAPIService(this@AppointmentDetailsActivity)
            .uploadPatientFile1(
                appointmentId,
                patientId,
                doctorId,
                doc_type,
                description,
                sessionid,
                username,
                usertype,
                fdate
            )
            .enqueue(object : Callback<UploadPatientFileResponse> {
                override fun onFailure(call: Call<UploadPatientFileResponse>, t: Throwable) {
                    progress_1.visibility = View.GONE
                    toast("Something went wrong")
                    Log.e("Hello", t.toString())
                    //  toast(t.toString())
                }

                override fun onResponse(
                    call: Call<UploadPatientFileResponse>,
                    response: Response<UploadPatientFileResponse>
                ) {
                    progress_1.visibility = View.GONE
                    try {
                        if (response.isSuccessful && response.body() != null && response.body()!!.statuscode == 200) {
                            toast("Notes Saved")
                            /*
                                                    ApiUtils.getAPIService(this@AppointmentDetailsActivity).closeAppointmentRequest(appointmentIdSend!!).enqueue(object : Callback<CloseAppointmentResponse> {
                                                        override fun onFailure(call: Call<CloseAppointmentResponse>, t: Throwable) {
                                                            progress_1.visibility = View.GONE
                                                            toast("Something went wrong")
                                                        }

                                                        override fun onResponse(call: Call<CloseAppointmentResponse>, response: Response<CloseAppointmentResponse>) {
                                                            progress_1.visibility = View.GONE
                                                         //   toast("Appointment closed !")

                                                            showSuccessDialog {
                                                                finish()
                                                            }

                                                        }
                                                    })
                            */
//                        showSuccessDialog {
//                            finish()
//                        }
                        } else {
                            progress_1.visibility = View.GONE
                            //  toast("Please try again with a different file")
                        }
                    } catch (e: Exception) {
                        toast("Error: Try again")
                    }
                }
            })

    }


    fun sendFileUploadRequest2(
        appointmentId: RequestBody,
        patientId: RequestBody,
        doctorId: RequestBody,
        doc_type: RequestBody,

        description: RequestBody,
        sessionid: RequestBody,
        username: RequestBody,
        usertype: RequestBody,
        fdate: RequestBody
    ) {
        ApiUtils.getAPIService(this@AppointmentDetailsActivity)
            .uploadPatientFile1(
                appointmentId,
                patientId,
                doctorId,
                doc_type,
                description,
                sessionid,
                username,
                usertype,
                fdate
            )
            .enqueue(object : Callback<UploadPatientFileResponse> {
                override fun onFailure(call: Call<UploadPatientFileResponse>, t: Throwable) {
                    progress_1.visibility = View.GONE
                    toast("Something went wrong")
                    Log.e("Hello", t.toString())
                    //  toast(t.toString())
                }

                override fun onResponse(
                    call: Call<UploadPatientFileResponse>,
                    response: Response<UploadPatientFileResponse>
                ) {
                    progress_1.visibility = View.GONE
                    try {
                        if (response.isSuccessful && response.body() != null && response.body()!!.statuscode == 200) {

                            finish()

                            /*
                                                    ApiUtils.getAPIService(this@AppointmentDetailsActivity).closeAppointmentRequest(appointmentIdSend!!).enqueue(object : Callback<CloseAppointmentResponse> {
                                                        override fun onFailure(call: Call<CloseAppointmentResponse>, t: Throwable) {
                                                            progress_1.visibility = View.GONE
                                                            toast("Something went wrong")
                                                        }

                                                        override fun onResponse(call: Call<CloseAppointmentResponse>, response: Response<CloseAppointmentResponse>) {
                                                            progress_1.visibility = View.GONE
                                                         //   toast("Appointment closed !")

                                                            showSuccessDialog {
                                                                finish()
                                                            }

                                                        }
                                                    })
                            */
//                        showSuccessDialog {
//                            finish()
//                        }
                        } else {
                            progress_1.visibility = View.GONE
                            //  toast("Please try again with a different file")
                        }
                    } catch (e: Exception) {
                        toast("Error: Try again")
                    }
                }
            })

    }


    fun checkSelfPermission(permission: String, requestCode: Int): Boolean {
        if (ContextCompat.checkSelfPermission(
                this,
                permission
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                REQUESTED_PERMISSIONS,
                requestCode
            )
            record_button.isListenForRecord = false
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQ_ID -> {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED || grantResults[1] != PackageManager.PERMISSION_GRANTED || grantResults[2] != PackageManager.PERMISSION_GRANTED) {
                    this.toast("Permission must be allowed to record audio")
                    record_button.isListenForRecord = false
                } else {
                    // if permission granted, initialize the engine
                    this.toast("Permissions granted !!")
                    initRecordView()
                }
            }
        }
    }


    fun sendNotification(patientId: String, message: String) {
        val userData = SharedPrefs.getUserData(this)
        val username = userData!!.fname + " " + userData.lname
        val usertype = "DOCTOR"
        ApiUtils.getAPIService(this@AppointmentDetailsActivity).sendNotification(
            patientId,
            message,
            SharedPrefs.getString(this@AppointmentDetailsActivity, "sessionid")!!,
            username,
            usertype
        )
            .enqueue(object : Callback<SendNotificationResponse> {
                override fun onFailure(call: Call<SendNotificationResponse>, t: Throwable) {
                    //Do nothing
                }

                override fun onResponse(
                    call: Call<SendNotificationResponse>,
                    response: Response<SendNotificationResponse>
                ) {
                    //Do nothing
                }
            })
    }


}

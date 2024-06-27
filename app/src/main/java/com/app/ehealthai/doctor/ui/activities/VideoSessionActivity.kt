package com.app.ehealthaidoctor.ui.activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.PixelFormat
import android.graphics.PorterDuff
import android.hardware.display.DisplayManager
import android.hardware.display.VirtualDisplay
import android.media.Image
import android.media.ImageReader
import android.media.ImageReader.OnImageAvailableListener
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.telephony.PhoneStateListener
import android.telephony.SignalStrength
import android.telephony.TelephonyManager
import android.text.Editable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.SurfaceView
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.RecyclerView
import com.app.ehealthai.doctor.adapters.recyclerViewAdapters.IClinicalTestItemToDelete
import com.app.ehealthai.doctor.adapters.recyclerViewAdapters.MedicinelistAdapter
import com.app.ehealthai.doctor.adapters.recyclerViewAdapters.dygTestAdapter
import com.app.ehealthai.doctor.adapters.recyclerViewAdapters.dyglistAdapter
import com.app.ehealthai.doctor.models.responses.AddDiagnosisResponse
import com.app.ehealthai.doctor.models.responses.ClinicalTestsResponse
import com.app.ehealthai.doctor.models.responses.DiagnosisList
import com.app.ehealthai.doctor.models.responses.DoctorNotesResponse
import com.app.ehealthai.doctor.models.responses.ForgotPasswordResponse
import com.app.ehealthai.doctor.models.responses.MedicineList
import com.app.ehealthai.doctor.models.responses.RoomDataResponse
import com.app.ehealthai.doctor.rtm.ChatManager
import com.app.ehealthai.doctor.rtm.status.MessageType
import com.app.ehealthai.doctor.services.MediaProjectionService
import com.app.ehealthai.doctor.services.SnapUploadService
import com.app.ehealthai.doctor.ui.activities.PatientDetails.PatientAppointment
import com.app.ehealthai.doctor.ui.activities.PatientDetails.ui.dashboard.AddNotesFragment
import com.app.ehealthai.doctor.ui.activities.VideoSessionMedicineHistory
import com.app.ehealthai.doctor.ui.activities.VideoSessionPatientReports
import com.app.ehealthai.doctor.ui.activities.VideoSessionVitalsHistory
import com.app.ehealthai.doctor.utils.Global
import com.app.ehealthaidoctor.R
import com.app.ehealthaidoctor.models.responses.CloseAppointmentResponse
import com.app.ehealthaidoctor.models.responses.Profile
import com.app.ehealthaidoctor.models.responses.SendNotificationResponse
import com.app.ehealthaidoctor.models.responses.UploadPatientFileResponse
import com.app.ehealthaidoctor.network.ApiUtils
import com.app.ehealthaidoctor.ui.activities.Chat.VideoSessionChat
import com.app.ehealthaidoctor.utils.SharedPrefs
import com.crashlytics.android.Crashlytics
import com.example.ehealthai.MyApplication
import com.example.ehealthai.MyApplication.Companion.isFirstCapture
import com.example.ehealthai.MyApplication.Companion.isPatientVideoShown
import com.example.ehealthai.MyApplication.Companion.remoteUserVideoEnabled
import com.example.ehealthai.MyApplication.Companion.videoSwappedWithUid
import com.example.ehealthai.MyApplication.Companion.videosSwapped
import com.example.ehealthai.utils.toast
import com.google.android.material.snackbar.Snackbar
import com.permissionx.guolindev.PermissionX
import io.agora.rtc2.ChannelMediaOptions
import io.agora.rtc2.Constants
import io.agora.rtc2.IRtcEngineEventHandler
import io.agora.rtc2.RtcEngine
import io.agora.rtc2.RtcEngineConfig
import io.agora.rtc2.video.SegmentationProperty
import io.agora.rtc2.video.VideoCanvas
import io.agora.rtc2.video.VirtualBackgroundSource
import io.agora.rtm.ErrorInfo
import io.agora.rtm.ResultCallback
import io.agora.rtm.RtmClient
import io.agora.rtm.RtmClientListener
import io.agora.rtm.RtmMessage
import io.agora.rtm.RtmStatusCode
import io.fabric.sdk.android.Fabric
import io.github.hyuwah.draggableviewlib.DraggableListener
import io.github.hyuwah.draggableviewlib.DraggableView
import io.github.hyuwah.draggableviewlib.setupDraggable
import kotlinx.android.synthetic.main.activity_custom_video_session.addnotes
import kotlinx.android.synthetic.main.activity_custom_video_session.clVideoViews
import kotlinx.android.synthetic.main.activity_custom_video_session.cl_custom_video_session
import kotlinx.android.synthetic.main.activity_custom_video_session.count
import kotlinx.android.synthetic.main.activity_custom_video_session.duration
import kotlinx.android.synthetic.main.activity_custom_video_session.endCallBtn
import kotlinx.android.synthetic.main.activity_custom_video_session.fl_remote_view
import kotlinx.android.synthetic.main.activity_custom_video_session.iv_idle_image
import kotlinx.android.synthetic.main.activity_custom_video_session.more
import kotlinx.android.synthetic.main.activity_custom_video_session.moreic
import kotlinx.android.synthetic.main.activity_custom_video_session.muteBtn
import kotlinx.android.synthetic.main.activity_custom_video_session.signalstregnth
import kotlinx.android.synthetic.main.activity_custom_video_session.statusc_text
import kotlinx.android.synthetic.main.activity_custom_video_session.switch_btn
import kotlinx.android.synthetic.main.activity_custom_video_session.tv_internet_status_video_session
import kotlinx.android.synthetic.main.activity_custom_video_session.video_btn
import kotlinx.android.synthetic.main.activity_custom_video_session.video_call_toolbar
import kotlinx.android.synthetic.main.addnotesdialog.view.addctestsbtn
import kotlinx.android.synthetic.main.addnotesdialog.view.adddiagnosisbtn
import kotlinx.android.synthetic.main.addnotesdialog.view.addmedicinebtn
import kotlinx.android.synthetic.main.addnotesdialog.view.btn_cancel
import kotlinx.android.synthetic.main.addnotesdialog.view.btn_okay
import kotlinx.android.synthetic.main.addnotesdialog.view.ctests_drp
import kotlinx.android.synthetic.main.addnotesdialog.view.diagnosis_drp
import kotlinx.android.synthetic.main.addnotesdialog.view.doctor_notes_text
import kotlinx.android.synthetic.main.addnotesdialog.view.dosagemeddrpDialog
import kotlinx.android.synthetic.main.addnotesdialog.view.dygnnosislistt
import kotlinx.android.synthetic.main.addnotesdialog.view.etOtherDiagnosis
import kotlinx.android.synthetic.main.addnotesdialog.view.followupchkbox
import kotlinx.android.synthetic.main.addnotesdialog.view.followupdate
import kotlinx.android.synthetic.main.addnotesdialog.view.listView
import kotlinx.android.synthetic.main.addnotesdialog.view.medicines_drp
import kotlinx.android.synthetic.main.addnotesdialog.view.medicinesdropdowns
import kotlinx.android.synthetic.main.addnotesdialog.view.progress_1
import kotlinx.android.synthetic.main.addnotesdialog.view.quantitymeddrpDialog
import kotlinx.android.synthetic.main.addnotesdialog.view.rv_clinicalTests
import kotlinx.android.synthetic.main.addnotesdialog.view.timeperioddrppDialog
import kotlinx.android.synthetic.main.addnotesdialog.view.unitdrpdwnnDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.nio.ByteBuffer
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.collections.set


class VideoSessionActivity : AppCompatActivity() {
    private val REQUEST_CODE_CAPTURE_SCREEN = 100
    private lateinit var mMediaProjectionManager: MediaProjectionManager
    private var mMediaProjection: MediaProjection? = null
    private var mVirtualDisplay: VirtualDisplay? = null
    private var mImageReader: ImageReader? = null
    private lateinit var startDelayHandler: Handler
    private lateinit var recursiveHandler: Handler
    private var isActivityVisible = false
    var patientNotJoinHandler: Handler? = null
    var patientNotJoinHandler5SecDelay: Handler? = null

    companion object {
        val PATIENT_ID = "patient_id"
        val DOCTOR_ID = "doctor_id"
        val TIME_OUT = 3001L
        var mednamelist: ArrayList<String> = ArrayList()
        var meddosagelist: ArrayList<String> = ArrayList()
        var medquantitylist: ArrayList<String> = ArrayList()
        var medUnitlist: ArrayList<String> = ArrayList()
        var medTimePeriodListlist: ArrayList<String> = ArrayList()
        var diagnosis: ArrayList<String> = ArrayList()
    }

    var diagnosisnameList: ArrayList<String> = java.util.ArrayList()
    var diagnosisidList: ArrayList<String> = java.util.ArrayList()
    var testsnameList: ArrayList<String> = java.util.ArrayList()
    var testsnameIdList: ArrayList<String> = java.util.ArrayList()

    //    lateinit var list: ArrayList<String>
//    var testlist: ArrayList<String>? = null
    var selecteddiagnosis = ""
    var dygadapter: dyglistAdapter? = null
    var dygTestsadapter: dygTestAdapter? = null
    var rvDyg: RecyclerView? = null
    var selectedfollowupdate = ""
    var notesupdated = false
    var callstarttime: String = ""
    var callendtime: String = ""
    var callduration: String = ""
    private var mRtmClient: RtmClient? = null
    private var mChatManager: ChatManager? = null
    private var isRTMLogin = false
    private val mTimeSentLastMsg = HashMap<String, Long>()
    var roomId: String = ""
    var patientId: String = ""
    var patientName: String = ""
    var doctorId: String = ""
    private var mRtmListener: MyRtmClientListener? = null
    var notringing = false
    var minutes: Int = 0
    var seconds: Int = 0
    var medicinestring: String = ""
    var medicinenameList: ArrayList<String> = ArrayList()
    var medicineidList: ArrayList<String> = ArrayList()
    var appointmentId = ""
    var docnotes = ""

    var progress_1: ProgressBar? = null

    // Permissions
    private val PERMISSION_REQ_ID = 22
    private val REQUESTED_PERMISSIONS =
        arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA)

    var otherpatyjoined = false
    var otherpatyleftafterjoin = false

    var mAlertDialog: AlertDialog? = null

    lateinit var localVideoContainerFrame: FrameLayout
    var pleaseSelect: String = "Please select"
    var pleaseSelectId: String = "0"
    lateinit var context: Activity
    private lateinit var mMainHandler: Handler
    private lateinit var myRunnable: Runnable
    private lateinit var localDraggableView: DraggableView<FrameLayout> // can be other type of View or ViewGroup
    private var mSignalStrength = 0
    lateinit var telephonyManager: TelephonyManager
    lateinit var mPhoneStateListener: PhoneStateListener
    lateinit var storageDirectory: File
    lateinit var ivCaptured: ImageView
    lateinit var storageDirectoryAudioData: File
    var isAlreadyCapturing: Boolean = false

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_video_session)



        localVideoContainerFrame = findViewById(R.id.local_video_view_container)
        ivCaptured = findViewById(R.id.ivCaptured)

        context = this@VideoSessionActivity
        Global.isfromVideoSessionActivity = true
        Fabric.with(this, Crashlytics())
//        list = ArrayList()
//        testlist = ArrayList()
        callstarttime = Calendar.getInstance().time.toString()
        registerTelephonyManager()
        customOnCreate()
        if (intent != null) {
            if (intent.hasExtra("appointmentClosedFromAddNotes")) {
                customOnDestroy()
                finish()
                val intent = Intent(this@VideoSessionActivity, HomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
            } else if (intent.hasExtra("appointmentCancelledFromAddNotes")) {
                customOnDestroy()
                finish()
                val intent = Intent(
                    this@VideoSessionActivity, AppointmentsActivity::class.java
                )
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
            } else {
                try {
                    val userData = SharedPrefs.getUserData(this@VideoSessionActivity)
                    val username = userData!!.username
                    val usertype = "DOCTOR"
                    try {
                        val url: String = intent.data.toString()
                        Log.e("Url ", url)
                        val arr = url.split("?").toTypedArray()
                        roomId = arr[1].filter { it.isDigit() }
                        channelName = roomId
                        uid = Integer.parseInt(roomId + "" + "")
                    } catch (e: java.lang.Exception) {
                        val url: String = getIntent().getStringExtra("url")!!
                        Log.e("Url ", url);
                        val arr = url.split("?").toTypedArray()
                        roomId = arr[1].filter { it.isDigit() }
                        channelName = roomId
                        uid = Integer.parseInt(roomId + "" + "")

                    }
                    getRoomData(username, usertype)
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                    toast("Session Expired")
                    finish()
                }
            }
        }

        LocalBroadcastManager.getInstance(this).registerReceiver(
            aLBReceiver, IntentFilter("callEnd")
        )

        //DraggableView
        localDraggableView = DraggableView.Builder(localVideoContainerFrame).build()
        localDraggableView = localVideoContainerFrame.setupDraggable().setAnimated(true)
            .setListener(object : DraggableListener {
                override fun onLongPress(view: View) {
                    if (remoteUserVideoEnabled) {
                        for (e in MyApplication.remoteUser) {
                            if (e.key) {
                                if (remoteSurfaceView != null) {
                                    toggleVideos(
                                        MyApplication.remoteUser.getValue(e.key),
                                        fl_remote_view,
                                        remoteSurfaceView!!
                                    )
                                    break
                                }
                            }
                        }
                    }
                }

                override fun onPositionChanged(view: View) {
                }

            }).build()

    }

    override fun onResume() {
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(aLBReceiver, IntentFilter("callEnd"))
        isActivityVisible = true
        super.onResume()
    }

    private fun getRoomData(username: String?, usertype: String) {
        ApiUtils.getAPIService(this@VideoSessionActivity).get_room_data(
            SharedPrefs.getString(this@VideoSessionActivity, "sessionid")!!,
            username!!,
            usertype,
            roomId
        ).enqueue(object : Callback<RoomDataResponse> {
            override fun onFailure(call: Call<RoomDataResponse>, t: Throwable) {
            }

            override fun onResponse(
                call: Call<RoomDataResponse>, response: Response<RoomDataResponse>
            ) {

                try {
                    patientId = response.body()!!.data.list.patient_id.toString()
                    doctorId = response.body()!!.data.list.doc_id.toString()
                    appointmentId = response.body()!!.data.list.apptid.toString()
                    patientName = response.body()!!.data.list.patient_name.toString()
                    val doctorData = SharedPrefs.getUserData(this@VideoSessionActivity)
                    val sessionid = SharedPrefs.getString(this@VideoSessionActivity, "sessionid")

                    getDoctorAppt(sessionid, username, usertype)

                    video_btn.isSelected = true
                    muteBtn.isSelected = true
                    init()
                    if (!isRTMLogin && doctorId != null && patientId != null) {
                        realTimeMessaging()
                    }
                    sendNotification(
                        patientId,
                        doctorData!!.fname + " " + doctorData.lname + " is calling..." + "," + doctorId
                    )
//                    joinChannel()
                    customJoinChannel()
                } catch (e: java.lang.Exception) {
                    toast("Session Failed")
                    Log.i("Session Failed exception", e.toString())
                    finish()
                }
            }

        })

    }

    private fun getDoctorAppt(sessionid: String?, username: String, usertype: String) {
        ApiUtils.getAPIService(this@VideoSessionActivity).get_doctor_documents_appt(
            sessionid!!, username, usertype, appointmentId
        ).enqueue(object : Callback<DoctorNotesResponse> {
            override fun onFailure(
                call: Call<DoctorNotesResponse>, t: Throwable
            ) {
                //Do nothing
            }

            override fun onResponse(
                call: Call<DoctorNotesResponse>, response: Response<DoctorNotesResponse>
            ) {
                //Do nothing
                try {
                    if (response.body() != null && response.body()?.data != null && response.body()!!.data!!.documents[0] != null && response.body()!!.data!!.documents[0].doctor_notes != null) docnotes =
                        response.body()!!.data!!.documents[0].doctor_notes
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }
        })
    }

    private val aLBReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {

            if (intent.getStringExtra("end").equals("ringing")) {
                //ringing.text = "RINGING"
                statusc_text.text = "RINGING..."
            } else if (intent.getStringExtra("end").equals("call")) {
                //ringing.text = "Call Ended"
                statusc_text.text = "Call Ended"
                val doctorData = SharedPrefs.getUserData(this@VideoSessionActivity)
                toast("Call Ended")

                sendNotification(
                    patientId,
                    doctorData!!.fname + " " + doctorData.lname + " call Ending..." + "," + doctorId
                )
            }

        }
    }

    override fun onDestroy() {
        try {
            if (this::mMainHandler.isInitialized && mMainHandler != null) {
                mMainHandler.removeCallbacks(myRunnable)
            }
            if (this::telephonyManager.isInitialized) if (this::mPhoneStateListener.isInitialized) {
                telephonyManager.listen(mPhoneStateListener, PhoneStateListener.LISTEN_NONE)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        if (mAlertDialog != null) {
            mAlertDialog!!.dismiss()
            mAlertDialog = null
        }
        LocalBroadcastManager.getInstance(this@VideoSessionActivity).unregisterReceiver(aLBReceiver)
        super.onDestroy()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.i("TAG", "onRequestPermissionsResult " + grantResults[0] + " " + requestCode)

        when (requestCode) {
            PERMISSION_REQ_ID -> {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED && grantResults[1] != PackageManager.PERMISSION_GRANTED) {
                    Log.i(
                        "TAG",
                        "Need permissions " + Manifest.permission.RECORD_AUDIO + "/" + Manifest.permission.CAMERA
                    )
                } else {
                    // if permission granted, initialize the engine

                    val doctorData = SharedPrefs.getUserData(this@VideoSessionActivity)

                    sendNotification(
                        patientId,
                        doctorData!!.fname + " " + doctorData.lname + " is calling..." + "," + doctorId
                    )
                    joinChannel()
                    customJoinChannel()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.S) {
            // Request permission to capture the screen
            startDelayHandler = Handler(Looper.getMainLooper())
            recursiveHandler = Handler(Looper.getMainLooper())

            storageDirectory =
                File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "EZShifaDoctorSnaps")

            // Initialize MediaProjectionManager
            mMediaProjectionManager =
                getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager

            startActivityForResult(
                mMediaProjectionManager.createScreenCaptureIntent(),
                REQUEST_CODE_CAPTURE_SCREEN
            )
            // Your code for Android versions greater than 12 (Android S)
            // For example, you can use new features specific to Android versions higher than 12 here.
            // This block will execute for Android 13 (API level 33) and beyond.
        } else {
            PermissionX.init(this).permissions(
                "android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.WRITE_EXTERNAL_STORAGE"
            ).onExplainRequestReason { scope, deniedList ->
                scope.showRequestReasonDialog(
                    deniedList,
                    "files permission is required to capture screenshots please allow it",
                    "OK",
                    "Cancel"
                )
            }.onForwardToSettings { scope, deniedList ->
                scope.showForwardToSettingsDialog(
                    deniedList,
                    "Please allow files permission to capture screenshots",
                    "OK",
                    "Cancel"
                )
            }.request { allGranted, grantedList, deniedList ->
                if (allGranted) {
                    //TODO only for debug
//                    Toast.makeText(this, "All permissions are granted", Toast.LENGTH_LONG).show()

                    // Request permission to capture the screen
                    startDelayHandler = Handler(Looper.getMainLooper())
                    recursiveHandler = Handler(Looper.getMainLooper())

                    storageDirectory =
                        File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "EZShifaDoctorSnaps")

                    // Initialize MediaProjectionManager
                    mMediaProjectionManager =
                        getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager

                    startActivityForResult(
                        mMediaProjectionManager.createScreenCaptureIntent(),
                        REQUEST_CODE_CAPTURE_SCREEN
                    )

                } else {
                    customOnDestroy()
                    finish()
                    //Permission denied
                }
            }
            // Your code for Android versions 12 (Android S) and below
            // For example, you can use backward-compatible code here.
            // This block will execute for Android 12 (API level 31) and below.
        }



    }

    override fun onStop() {
        super.onStop()

        mMediaProjection?.stop()
        mMediaProjection = null

        if (mImageReader != null) {
            mImageReader!!.close()
            mImageReader = null
        }
        if (mVirtualDisplay != null) {
            mVirtualDisplay!!.release()
            mVirtualDisplay = null
        }

        if (isServiceRunningInForeground(this, MediaProjectionService::class.java)) {
            stopService(Intent(this, MediaProjectionService::class.java))
        }
        if (this::startDelayHandler.isInitialized) {
            startDelayHandler.removeCallbacksAndMessages(null)
        }
        if (patientNotJoinHandler != null) {
            patientNotJoinHandler!!.removeCallbacksAndMessages(null)
        }
        if (patientNotJoinHandler5SecDelay != null) {
            patientNotJoinHandler5SecDelay!!.removeCallbacksAndMessages(null)
        }
        if (this::recursiveHandler.isInitialized) {
            recursiveHandler.removeCallbacksAndMessages(null)
        }
        mChatManager?.unregisterListener(mRtmListener)
    }

    fun checkSelfPermission(permission: String, requestCode: Int): Boolean {
        if (ContextCompat.checkSelfPermission(
                this, permission
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(this, REQUESTED_PERMISSIONS, requestCode)
            return false
        }
        return true
    }

    internal fun init() {

        if (checkSelfPermission(REQUESTED_PERMISSIONS[0], PERMISSION_REQ_ID) && checkSelfPermission(
                REQUESTED_PERMISSIONS[1], PERMISSION_REQ_ID
            )
        ) {
            initializeAgoraEngine()
            //  joinChannel()
        }
        val doctorData = SharedPrefs.getUserData(this@VideoSessionActivity)
        initButtonsClick(doctorData)

        val timer = object : CountDownTimer(1800000, 1000) {
            override fun onTick(millisUntilFinished: Long) {

                var minutestodisplay = ""
                var secondstodisplay = ""
                seconds++

                if (seconds.toInt() % 60 == 0) {
                    minutes++
                    seconds = 0
                }
                if (minutes < 10) {
                    minutestodisplay = "0" + minutes.toString()
                } else {
                    minutestodisplay = minutes.toString()
                }
                if (seconds < 10) {
                    secondstodisplay = "0" + seconds.toString()
                } else {
                    secondstodisplay = seconds.toString()
                }
                duration.text = "$minutestodisplay:$secondstodisplay"
                if (minutes == 10 && seconds == 0) {
                    Snackbar.make(
                        cl_custom_video_session, "5 minutes left in your scheduled session", 8000
                    ).setAction("Close") { }.setActionTextColor(Color.RED).show()
                }
            }

            override fun onFinish() {}
        }
        timer.start()
    }

    private fun initializeAgoraEngine() {
        val mainHandler = Handler(Looper.getMainLooper())
        mainHandler.post(object : Runnable {
            override fun run() {
                try {
                    val notifcount = SharedPrefs.getInt(applicationContext, "notifavailable")
                    if (notifcount > 0) {
                        // count.setText("New Message")
                        count.setText(notifcount)
                        count.visibility = View.VISIBLE
                    }
                } catch (_: Exception) {
                }

                try {
                    if (!otherpatyjoined && !otherpatyleftafterjoin) {
                        statusc_text.visibility = View.VISIBLE
                        statusc_text.text = "Waiting for " + patientName + "..."
//                        val container = findViewById<FrameLayout>(R.id.friend_frame_layout)

                    } else if (otherpatyleftafterjoin) {
                        statusc_text.visibility = View.VISIBLE
                        statusc_text.text = patientName + " Left"
//                        val container = findViewById<FrameLayout>(R.id.friend_frame_layout)
                    }

                    if (agoraEngine?.connectionState.toString() == "1") {
                        statusc_text.visibility = View.VISIBLE
                        statusc_text.text = "Disconnected"
                    }
                    if (agoraEngine?.connectionState.toString() == "2") {
                        statusc_text.visibility = View.VISIBLE
                        statusc_text.text = "Connecting..."
                    }
                    if (agoraEngine?.connectionState.toString() == "3") {
                        statusc_text.visibility = View.VISIBLE
                        statusc_text.text = "Connected"

                    }
                    if (agoraEngine?.connectionState.toString() == "4") {
                        statusc_text.visibility = View.VISIBLE
                        statusc_text.text = "Reconnecting..."
                    }
                    if (agoraEngine?.connectionState.toString() == "5") {
                        statusc_text.visibility = View.VISIBLE
                        statusc_text.text = "Call Failed"
                    }

                } catch (e: java.lang.Exception) {
                    Toast.makeText(
                        this@VideoSessionActivity, "connectionState $e", Toast.LENGTH_LONG
                    ).show()
                }

                mainHandler.postDelayed(this, 1000)
            }
        })

        try {
            myRunnable = Runnable {
                try {
                    manageSignals()
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
                mMainHandler.postDelayed(myRunnable, 5000)
            }
            checkInternetFrequently()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun initButtonsClick(doctorData: Profile?) {
        switch_btn.setOnClickListener {
            agoraEngine?.switchCamera()
        }
        endCallBtn.setOnClickListener { v ->
            sendNotification(
                patientId,
                doctorData!!.fname + " " + doctorData.lname + " call Ending..." + "," + doctorId
            )
            val userData = SharedPrefs.getUserData(this@VideoSessionActivity)
            val username = userData!!.username
            val usertype = "DOCTOR"

            if (notesupdated) {
                notesupdated = false
                val dialogBuilder = AlertDialog.Builder(this@VideoSessionActivity)
                dialogBuilder.setMessage("Please close your appointment if you are done with the session.ThankYou")
                    .setCancelable(false)
                    .setPositiveButton("Close Now", DialogInterface.OnClickListener { dialog, id ->
                        try {
                            callendtime = Calendar.getInstance().time.toString()
                            callduration = duration.text.toString()
                            ApiUtils.getAPIService(this@VideoSessionActivity)
                                .callstatuswithextraparams(
                                    SharedPrefs.getString(this@VideoSessionActivity, "sessionid")!!,
                                    username!!,
                                    usertype,
                                    PatientAppointment.appointmentId.toString(),
                                    "Doctor has Ended Video Session",
                                    callstarttime,
                                    callendtime,
                                    callduration
                                ).enqueue(object : Callback<ForgotPasswordResponse> {
                                    override fun onFailure(
                                        call: Call<ForgotPasswordResponse>, t: Throwable
                                    ) {
                                        //Do nothing
                                    }

                                    override fun onResponse(
                                        call: Call<ForgotPasswordResponse>,
                                        response: Response<ForgotPasswordResponse>
                                    ) {
                                        val userData =
                                            SharedPrefs.getUserData(this@VideoSessionActivity)
                                        val username = userData!!.username
                                        val usertype = "DOCTOR"
                                        ApiUtils.getAPIService(this@VideoSessionActivity)
                                            .closeAppointmentRequest(
                                                PatientAppointment.appointmentId!!,
                                                SharedPrefs.getString(
                                                    this@VideoSessionActivity, "sessionid"
                                                )!!,
                                                username!!,
                                                usertype
                                            ).enqueue(object : Callback<CloseAppointmentResponse> {
                                                override fun onFailure(
                                                    call: Call<CloseAppointmentResponse>,
                                                    t: Throwable
                                                ) {
                                                    //   mDialogView. progress_1.visibility = View.GONE
                                                    Toast.makeText(
                                                        this@VideoSessionActivity,
                                                        "Something went wrong, please try again later",
                                                        Toast.LENGTH_LONG
                                                    ).show()
                                                }

                                                override fun onResponse(
                                                    call: Call<CloseAppointmentResponse>,
                                                    response: Response<CloseAppointmentResponse>
                                                ) {
                                                    try {
                                                        PatientAppointment.appointmentClosed = true
                                                        PatientAppointment.clearAppointmentNotes()
                                                        Toast.makeText(
                                                            this@VideoSessionActivity,
                                                            "Appointment closed !",
                                                            Toast.LENGTH_LONG
                                                        ).show()

                                                        AlertDialog.Builder(this@VideoSessionActivity)
                                                            .setTitle(
                                                                this@VideoSessionActivity.resources.getString(
                                                                    R.string.success_dialog_title
                                                                )
                                                            ).setMessage(
                                                                this@VideoSessionActivity.resources.getString(
                                                                    R.string.success_dialog_message_appt
                                                                )
                                                            ).setPositiveButton(
                                                                this@VideoSessionActivity.resources.getString(
                                                                    R.string.success_dialog_confirm
                                                                )
                                                            ) { dialog, _ ->
                                                                //agView?.agkit?.stopPreview()
                                                                //agView?.agkit?.leaveChannel()
                                                                //agView = null
                                                                dialog?.dismiss()
                                                                customOnDestroy()
                                                                finish()
                                                                val intent = Intent(
                                                                    this@VideoSessionActivity,
                                                                    HomeActivity::class.java
                                                                )
                                                                intent.flags =
                                                                    Intent.FLAG_ACTIVITY_CLEAR_TOP
                                                                startActivity(intent)

                                                            }.show()
                                                    } catch (e: Exception) {
                                                        e.printStackTrace()
                                                    }
                                                }
                                            })
                                    }
                                })
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                    })

                    .setNegativeButton(
                        "Close Later"
                    ) { dialog, id ->
                        try {
                            callendtime = Calendar.getInstance().time.toString()
                            callduration = duration.text.toString()
                            ApiUtils.getAPIService(this@VideoSessionActivity)
                                .callstatuswithextraparams(
                                    SharedPrefs.getString(
                                        this@VideoSessionActivity, "sessionid"
                                    )!!,
                                    username!!,
                                    usertype,
                                    PatientAppointment.appointmentId.toString(),
                                    "Doctor has Ended Video Session",
                                    callstarttime,
                                    callendtime,
                                    callduration
                                ).enqueue(object : Callback<ForgotPasswordResponse> {
                                    override fun onFailure(
                                        call: Call<ForgotPasswordResponse>, t: Throwable
                                    ) {
                                        //Do nothing

                                    }

                                    override fun onResponse(
                                        call: Call<ForgotPasswordResponse>,
                                        response: Response<ForgotPasswordResponse>
                                    ) {
                                        customOnDestroy()
//                                            agView?.agkit?.stopPreview()
//                                            agView?.agkit?.leaveChannel()
//                                            agView = null
//                                            Globbal.callStatus = ""
                                        finish()
                                    }
                                })
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        //leaveChannel()
                    }
                // create dialog box
                val alert = dialogBuilder.create()
                // set title for alert dialog box
                alert.setTitle("Session Ended")
                // show alert dialog
                alert.show()
            } else {
                PatientAppointment.appointmentClosed = true
                PatientAppointment.clearAppointmentNotes()
                notesupdated = false
                customOnDestroy()
//                agView?.agkit?.stopPreview()
//                agView?.agkit?.leaveChannel()
//                this.agView = null
//                Globbal.callStatus = ""
                finish()
            }
        }
        muteBtn.setOnClickListener {

            if (muteBtn.isSelected) {
                agoraEngine?.muteLocalAudioStream(true)
                sendStatusMessage(MessageType.MUTE_MIC)
                Handler(Looper.getMainLooper()).postDelayed(
                    { sendStopStatusMsg(MessageType.MUTE_MIC) }, TIME_OUT
                )
                muteBtn.isSelected = false
                muteBtn.setImageResource(R.drawable.nomic)

            } else {
                agoraEngine?.muteLocalAudioStream(false)
                sendStatusMessage(MessageType.MIC_ON)
                Handler(Looper.getMainLooper()).postDelayed(
                    { sendStopStatusMsg(MessageType.MIC_ON) }, TIME_OUT
                )
                muteBtn.isSelected = true
                muteBtn.setImageResource(R.drawable.mic)

            }

        }
        more.setOnClickListener {
            Global.IntentFromHandler = false
            sendStatusMessage(MessageType.OPENING_MSGS)
            Handler(Looper.getMainLooper()).postDelayed({
                sendStopStatusMsg(MessageType.OPENING_MSGS)
            }, TIME_OUT)
            count.visibility = View.GONE
            val intent = Intent(this, VideoSessionChat::class.java)
            intent.putExtra("patientId", patientId)
            intent.putExtra("patientName", patientName)
            intent.putExtra("image_name_p", "")
            startActivity(intent)

        }
        moreic.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("More Options")

            val moreoptions = arrayOf("Vitals History", "Medicine History", "Patient Reports")
            builder.setItems(
                moreoptions
            ) { dialog, which ->
                when (which) {
                    0 -> {
                        val intent = Intent(this, VideoSessionVitalsHistory::class.java)
                        intent.putExtra("patientId", patientId)
                        intent.putExtra("patientName", patientName)
                        intent.putExtra("image_name_p", "")
                        startActivity(intent)
                    }

                    1 -> {
                        val intent = Intent(this, VideoSessionMedicineHistory::class.java)
                        intent.putExtra("patientId", patientId)
                        intent.putExtra("patientName", patientName)
                        intent.putExtra("image_name_p", "")
                        startActivity(intent)
                    }

                    2 -> {
                        val intent = Intent(this, VideoSessionPatientReports::class.java)
                        intent.putExtra("patientId", patientId)
                        intent.putExtra("patientName", patientName)
                        intent.putExtra("image_name_p", "")
                        startActivity(intent)
                    }
                }
            }
            val dialog = builder.create()
            dialog.show()
        }
        addnotes.setOnClickListener {
            showMessage("Opening notes please wait")
            mednamelist.clear()
            val sessionid = SharedPrefs.getString(this@VideoSessionActivity, "sessionid")
            val userData = SharedPrefs.getUserData(this@VideoSessionActivity)
            val username = userData!!.username
            val usertype = "DOCTOR"
            diagnosisnameList = java.util.ArrayList()
            diagnosisidList = java.util.ArrayList()

            val mDialogView = LayoutInflater.from(this).inflate(R.layout.addnotesdialog, null)
            //AlertDialogBuilder
            val mBuilder = AlertDialog.Builder(this).setView(mDialogView).setTitle("Add Notes")
            //show dialog
            mAlertDialog = mBuilder.show()
            mAlertDialog?.setOnDismissListener {
                if (mDialogView != null) {
                    if (mDialogView.etOtherDiagnosis.text != null && mDialogView.etOtherDiagnosis.text.toString() != "") PatientAppointment.otherDiagnosisText =
                        mDialogView.etOtherDiagnosis.text.toString()
                }
                mAlertDialog = null

            }
            mAlertDialog?.setOnCancelListener {
                if (mDialogView != null) {
                    if (mDialogView.etOtherDiagnosis.text != null && mDialogView.etOtherDiagnosis.text.toString() != "") PatientAppointment.otherDiagnosisText =
                        mDialogView.etOtherDiagnosis.text.toString()
                }
                mAlertDialog = null
            }
            try {
                mDialogView.listView.isNestedScrollingEnabled = true
            } catch (e: Exception) {
                e.printStackTrace()
            }

            AddNotesFragment.adapter = MedicinelistAdapter(
                this,
                mednamelist,
                medquantitylist,
                meddosagelist,
                medTimePeriodListlist,
                medUnitlist
            )
            mDialogView.listView.adapter = AddNotesFragment.adapter

//            val layoutManager = LinearLayoutManager(
//                context, LinearLayoutManager.HORIZONTAL, false
//            )
//            mDialogView?.dygnnosislistt?.layoutManager = layoutManager
//
//            mDialogView?.rv_clinicalTests?.layoutManager = layoutManager


            mDialogView.followupchkbox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    PatientAppointment.isFollowUpChecked = isChecked
                    val datePickerListener =
                        DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                            val dateYouChoosed =
                                year.toString() + "-" + (monthOfYear + 1) + "-" + dayOfMonth.toString()
                            mDialogView?.followupdate?.text = dateYouChoosed
                            selectedfollowupdate = dateYouChoosed
                            PatientAppointment.followUpDate = selectedfollowupdate
                        }
                    val c = Calendar.getInstance()
                    val mYear = c[Calendar.YEAR]
                    val mMonth = c[Calendar.MONTH]
                    val mDay = c[Calendar.DAY_OF_MONTH]
                    val dateDialog = DatePickerDialog(this, datePickerListener, mYear, mMonth, mDay)
                    dateDialog.datePicker.minDate = System.currentTimeMillis() - 1000
                    dateDialog.show()

                } else {
                    PatientAppointment.isFollowUpChecked = isChecked
                    mDialogView?.followupdate?.setText("")
                    selectedfollowupdate = ""
                }
            }
            //login button click of custom layout
            ApiUtils.getAPIService(this@VideoSessionActivity)
                .get_doctor_documents_appt(sessionid!!, username!!, usertype, appointmentId)
                .enqueue(object : Callback<DoctorNotesResponse> {
                    override fun onFailure(call: Call<DoctorNotesResponse>, t: Throwable) {
                        //Do nothing
                    }

                    override fun onResponse(
                        call: Call<DoctorNotesResponse>, response: Response<DoctorNotesResponse>
                    ) {
                        //Do nothing
                        try {
                            docnotes = response.body()!!.data!!.documents[0].doctor_notes
                            mDialogView.doctor_notes_text.setText(docnotes)
                        } catch (e: java.lang.Exception) {
                            e.printStackTrace()
                        }
                    }
                })

            mDialogView.diagnosis_drp.isEnabled = false


            ApiUtils.getAPIService(this@VideoSessionActivity).get_diagnosis_list(
                SharedPrefs.getString(this@VideoSessionActivity, "sessionid")!!, username, usertype
            ).enqueue(object : Callback<DiagnosisList> {
                override fun onFailure(call: Call<DiagnosisList>, t: Throwable) {
                    toast("Something went wrong, please try again later")
                }

                override fun onResponse(
                    call: Call<DiagnosisList>, response: Response<DiagnosisList>
                ) {
                    try {
                        if (response.isSuccessful && response.body() != null && response.body()!!.statuscode == 200) {
                            try {

                                diagnosis = ArrayList()
                                val diagnosisAdapter: ArrayAdapter<String>?
//                                medicinenameList.clear()
//                                medicineidList.clear()

                                diagnosis.add(pleaseSelect)
                                diagnosisnameList.add(pleaseSelect)
                                diagnosisidList.add(pleaseSelectId)

                                for (diag in response.body()!!.data!!.data!!) {
                                    diagnosis.add(diag.diagnosis_name!!)
                                    diagnosisnameList.add(diag.diagnosis_name)
                                    diagnosisidList.add(diag.id.toString())
                                }

                                diagnosisAdapter = ArrayAdapter(
                                    this@VideoSessionActivity,
                                    android.R.layout.simple_spinner_item,
                                    diagnosis
                                )
                                diagnosisAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                                mDialogView?.diagnosis_drp?.isEnabled = true
                                mDialogView?.diagnosis_drp?.adapter = diagnosisAdapter
                                mDialogView?.diagnosis_drp?.onItemSelectedListener =
                                    object : AdapterView.OnItemSelectedListener {
                                        override fun onItemSelected(
                                            parent: AdapterView<*>,
                                            view: View,
                                            position: Int,
                                            id: Long
                                        ) {
                                            if (diagnosis[position].equals("Others")) {
                                                mDialogView?.etOtherDiagnosis?.visibility =
                                                    View.VISIBLE
                                                if (PatientAppointment.otherDiagnosisText != null && PatientAppointment.otherDiagnosisText != "") {
                                                    mDialogView?.etOtherDiagnosis?.setText(
                                                        PatientAppointment.otherDiagnosisText
                                                    )
                                                }
                                            } else {
                                                mDialogView?.etOtherDiagnosis?.visibility =
                                                    View.GONE
                                            }
                                        }

                                        override fun onNothingSelected(parent: AdapterView<*>) {
                                            // write code to perform some action
                                        }
                                    }

                                mDialogView.adddiagnosisbtn.isClickable = true
                                mDialogView.adddiagnosisbtn.visibility = View.VISIBLE
                                addDiagnosisButtonFunctionality(mDialogView)

                            } catch (e: java.lang.Exception) {
                                e.printStackTrace()
                            }
                        } else {
                            Toast.makeText(
                                this@VideoSessionActivity,
                                "Error: Diagnosis list failed to load",
                                Toast.LENGTH_LONG
                            ).show()

                        }
                    } catch (e: Exception) {
                        Toast.makeText(
                            this@VideoSessionActivity,
                            "Error: Diagnosis list failed to load",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    mDialogView.adddiagnosisbtn.setOnClickListener {
                        selecteddiagnosis = mDialogView.diagnosis_drp.selectedItem.toString()
                        if (!mDialogView.diagnosis_drp.selectedItem.toString()
                                .equals("") && !mDialogView.diagnosis_drp.selectedItem.toString()
                                .equals(pleaseSelect)
                        ) {
                            if (AddNotesFragment.mDiagsList.size > 0) {
                                if (!AddNotesFragment.mDiagsList.contains(selecteddiagnosis)) {
//                                list.add(selecteddiagnosis)
                                    var index = diagnosisnameList.indexOf(selecteddiagnosis)
                                    Log.i("listasize", AddNotesFragment.mDiagsList?.size.toString())
                                    var selectedid = diagnosisidList[index].toString()
                                    Log.e("SELECTED ID ", selectedid)
                                    if (!mDialogView.diagnosis_drp.selectedItem.toString()
                                            .equals("")
                                    ) {
                                        if (mDialogView.diagnosis_drp.selectedItem.toString()
                                                .equals("Others")
                                        ) {
                                            if (mDialogView.etOtherDiagnosis.text.toString().trim()
                                                    .equals("") || mDialogView.etOtherDiagnosis.text.trim()
                                                    .isEmpty()
                                            ) {
                                                mDialogView.etOtherDiagnosis.error =
                                                    "Diagnosis can't be empty"
                                                return@setOnClickListener
                                            } else {
                                                selecteddiagnosis =
                                                    mDialogView.etOtherDiagnosis.text.toString()
                                                        .trim()
                                                if (diagnosisnameList.size > 0 && !diagnosisnameList.contains(
                                                        selecteddiagnosis
                                                    )
                                                ) {
                                                    if (!containsCaseInsensitive(
                                                            selecteddiagnosis, diagnosisnameList
                                                        )
                                                    ) {
                                                        if (!AddNotesFragment.mDiagsList.contains(
                                                                selecteddiagnosis
                                                            )
                                                        ) {
                                                            if (!containsCaseInsensitive(
                                                                    selecteddiagnosis,
                                                                    AddNotesFragment.mDiagsList
                                                                )
                                                            ) {
                                                                AddNotesFragment.mDiagsList.add(
                                                                    selecteddiagnosis
                                                                )
                                                            } else {
                                                                toast("Diagnosis Already Added")
                                                                return@setOnClickListener
                                                            }
                                                        } else {
                                                            toast("Diagnosis Already Added")
                                                            return@setOnClickListener
                                                        }
                                                    } else {
                                                        toast("Diagnosis is already available in list")
                                                        return@setOnClickListener
                                                    }
                                                } else {
                                                    toast("Diagnosis is already available in list")
                                                    return@setOnClickListener
                                                }

                                            }
                                            selectedid = ""
                                        } else {
                                            selecteddiagnosis =
                                                mDialogView.diagnosis_drp.selectedItem.toString()
                                            AddNotesFragment.mDiagsList.add(selecteddiagnosis)
                                        }

                                        dygadapter = dyglistAdapter(this@VideoSessionActivity,
                                            AddNotesFragment.mDiagsList,
                                            "Diagnosis",
                                            this@VideoSessionActivity,
                                            object : IClinicalTestItemToDelete {
                                                override fun findDiagnosisIdAndDeleteItem(
                                                    diagnosisName: String, itemPosition: Int
                                                ) {
                                                    if (!mDialogView.diagnosis_drp.selectedItem.toString()
                                                            .equals("")
                                                    ) {
                                                        val diagId = findDiagnosisId(diagnosisName)
                                                        if (diagId != "empty") {
                                                            dygadapter?.callingDeleteApi(
                                                                itemPosition,
                                                                diagId,
                                                                "",
                                                                diagnosisName
                                                            )
                                                        } else {
                                                            dygadapter?.callingDeleteApi(
                                                                itemPosition,
                                                                "",
                                                                diagnosisName,
                                                                diagnosisName
                                                            )
                                                        }
                                                    }
                                                }

                                            })
                                        mDialogView.dygnnosislistt.adapter = dygadapter
                                    }
                                    Global.username = username
                                    Global.usertype = usertype
                                    Global.selectedid = selectedid
                                    Global.appointmentIdd = PatientAppointment.appointmentId
                                    Global.patientIdd = PatientAppointment.patientId

                                    //Global.showProgressDialog(this@VideoSessionActivity, "Please wait...")

                                    ApiUtils.getAPIService(this@VideoSessionActivity)
                                        .add_patient_diagnosis_with_Other_Diagnosis(
                                            SharedPrefs.getString(
                                                this@VideoSessionActivity, "sessionid"
                                            )!!,
                                            username,
                                            usertype,
                                            selectedid,
                                            selecteddiagnosis,
                                            PatientAppointment.appointmentId!!,
                                            PatientAppointment.patientId!!
                                        ).enqueue(object : Callback<AddDiagnosisResponse> {
                                            override fun onFailure(
                                                call: Call<AddDiagnosisResponse>, t: Throwable
                                            ) {
                                                // Global.hideProgressDialog()
                                                Toast.makeText(
                                                    this@VideoSessionActivity,
                                                    "Something went wrong, please try again later",
                                                    Toast.LENGTH_LONG
                                                ).show()
                                            }

                                            override fun onResponse(
                                                call: Call<AddDiagnosisResponse>,
                                                response: Response<AddDiagnosisResponse>
                                            ) {
                                                PatientAppointment.isDiagnosisUpdated = true
                                                //   Global.hideProgressDialog()
                                                Toast.makeText(
                                                    this@VideoSessionActivity,
                                                    "Diagnosis Added Successfully !",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        })
                                } else {
                                    toast("Diagnosis Already Added")
                                }

                            } else {
//                            list?.add(selecteddiagnosis)
                                var index = diagnosisnameList.indexOf(selecteddiagnosis)
                                var selectedid = diagnosisidList[index]
                                Log.e("SELECTED ID ", selectedid)
                                if (!mDialogView.diagnosis_drp.selectedItem.toString().equals("")) {
                                    if (mDialogView.diagnosis_drp.selectedItem.toString()
                                            .equals("Others")
                                    ) {
                                        if (mDialogView.etOtherDiagnosis.text.toString().trim()
                                                .equals("") || mDialogView.etOtherDiagnosis.text.trim()
                                                .isEmpty()
                                        ) {
                                            mDialogView.etOtherDiagnosis.error =
                                                "Diagnosis can't be empty"
                                            return@setOnClickListener
                                        } else {
                                            selecteddiagnosis =
                                                mDialogView.etOtherDiagnosis.text.toString().trim()
                                            if (diagnosisnameList.size > 0 && !diagnosisnameList.contains(
                                                    selecteddiagnosis
                                                )
                                            ) {
                                                if (!containsCaseInsensitive(
                                                        selecteddiagnosis, diagnosisnameList
                                                    )
                                                ) {
                                                    if (!AddNotesFragment.mDiagsList.contains(
                                                            selecteddiagnosis
                                                        )
                                                    ) {
                                                        if (!containsCaseInsensitive(
                                                                selecteddiagnosis,
                                                                AddNotesFragment.mDiagsList
                                                            )
                                                        ) {
                                                            AddNotesFragment.mDiagsList.add(
                                                                selecteddiagnosis
                                                            )
                                                        } else {
                                                            toast("Diagnosis Already Added")
                                                            return@setOnClickListener

                                                        }
                                                    } else {
                                                        toast("Diagnosis Already Added")
                                                        return@setOnClickListener
                                                    }
                                                } else {
                                                    toast("Diagnosis is already available in list")
                                                    return@setOnClickListener
                                                }

                                            } else {
                                                toast("Diagnosis is already available in list")
                                                return@setOnClickListener
                                            }
                                        }
                                        selectedid = ""
                                    } else {
                                        selecteddiagnosis =
                                            mDialogView.diagnosis_drp.selectedItem.toString()
                                        AddNotesFragment.mDiagsList.add(selecteddiagnosis)
                                    }

                                    dygadapter = dyglistAdapter(this@VideoSessionActivity,
                                        AddNotesFragment.mDiagsList,
                                        "Diagnosis",
                                        this@VideoSessionActivity,
                                        object : IClinicalTestItemToDelete {
                                            override fun findDiagnosisIdAndDeleteItem(
                                                diagnosisName: String, itemPosition: Int
                                            ) {
                                                if (!mDialogView.diagnosis_drp.selectedItem.toString()
                                                        .equals("")
                                                ) {
                                                    val diagId = findDiagnosisId(diagnosisName)
                                                    if (diagId != "empty") {
                                                        dygadapter?.callingDeleteApi(
                                                            itemPosition, diagId, "", diagnosisName
                                                        )
                                                    } else {
                                                        dygadapter?.callingDeleteApi(
                                                            itemPosition,
                                                            "",
                                                            diagnosisName,
                                                            diagnosisName
                                                        )
                                                    }
                                                }
                                            }
                                        })
                                    mDialogView.dygnnosislistt.adapter = dygadapter

                                }
                                Global.username = username
                                Global.usertype = usertype
                                Global.selectedid = selectedid
                                Global.appointmentIdd = PatientAppointment.appointmentId
                                Global.patientIdd = PatientAppointment.patientId

                                //Global.showProgressDialog(this@VideoSessionActivity, "Please wait...")

                                ApiUtils.getAPIService(this@VideoSessionActivity)
                                    .add_patient_diagnosis_with_Other_Diagnosis(
                                        SharedPrefs.getString(
                                            this@VideoSessionActivity, "sessionid"
                                        )!!,
                                        username,
                                        usertype,
                                        selectedid,
                                        selecteddiagnosis,
                                        PatientAppointment.appointmentId!!,
                                        PatientAppointment.patientId!!
                                    ).enqueue(object : Callback<AddDiagnosisResponse> {
                                        override fun onFailure(
                                            call: Call<AddDiagnosisResponse>, t: Throwable
                                        ) {
                                            // Global.hideProgressDialog()
                                            Toast.makeText(
                                                this@VideoSessionActivity,
                                                "Something went wrong, please try again later",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }

                                        override fun onResponse(
                                            call: Call<AddDiagnosisResponse>,
                                            response: Response<AddDiagnosisResponse>
                                        ) {
                                            //   Global.hideProgressDialog()
                                            PatientAppointment.isDiagnosisUpdated = true
                                            Toast.makeText(
                                                this@VideoSessionActivity,
                                                "Diagnosis Added Successfully !",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    })
                            }
                        } else {
                            toast("Please select diagnosis")
                        }
                    }

                    mDialogView.addctestsbtn.setOnClickListener {
                        try {
                            var selectedtest = mDialogView.ctests_drp.selectedItem.toString()
                            if (!mDialogView.ctests_drp.selectedItem.toString()
                                    .equals("") && !mDialogView.ctests_drp.selectedItem.toString()
                                    .equals(pleaseSelect)
                            ) {
                                if (!PatientAppointment.selectedClinicalTests?.contains(selectedtest)!!) {
                                    PatientAppointment.selectedClinicalTests?.add(selectedtest)

                                    var index = testsnameList.indexOf(selectedtest)
                                    Log.e("INDEX ", "" + index)
                                    if (!mDialogView.ctests_drp.selectedItem.toString()
                                            .equals("")
                                    ) {
                                        selectedtest =
                                            mDialogView?.ctests_drp?.selectedItem.toString()

                                        val textView = TextView(this@VideoSessionActivity)
                                        textView.setPadding(20, 20, 20, 20)
                                        val lp: LinearLayout.LayoutParams =
                                            LinearLayout.LayoutParams(
                                                LinearLayout.LayoutParams.MATCH_PARENT,
                                                LinearLayout.LayoutParams.WRAP_CONTENT
                                            )


                                        lp.setMargins(6, 0, 6, 0)
                                        textView.layoutParams = lp
                                        textView.setBackgroundColor(resources.getColor(R.color.grey_light))
                                        textView.text = selectedtest
                                        dygTestsadapter = dygTestAdapter(
                                            context,
                                            PatientAppointment.selectedClinicalTests,
                                            "Test",
                                            context
                                        )
                                        mDialogView.rv_clinicalTests.adapter = dygTestsadapter

                                    }

                                    //  mDialogView.progress_1.visibility = View.VISIBLE
                                    ApiUtils.getAPIService(this@VideoSessionActivity)
                                        .add_patient_test(
                                            SharedPrefs.getString(
                                                this@VideoSessionActivity, "sessionid"
                                            )!!,
                                            username!!,
                                            usertype,
                                            selectedtest,
                                            PatientAppointment.appointmentId!!
                                        ).enqueue(object : Callback<AddDiagnosisResponse> {
                                            override fun onFailure(
                                                call: Call<AddDiagnosisResponse>, t: Throwable
                                            ) {
                                                mDialogView.progress_1.visibility = View.GONE

                                                Toast.makeText(
                                                    this@VideoSessionActivity,
                                                    "Something went wrong, please try again later",
                                                    Toast.LENGTH_LONG
                                                ).show()
                                            }

                                            override fun onResponse(
                                                call: Call<AddDiagnosisResponse>,
                                                response: Response<AddDiagnosisResponse>
                                            ) {
                                                mDialogView.progress_1.visibility = View.GONE
                                                Toast.makeText(
                                                    this@VideoSessionActivity,
                                                    "Test Added Successfully !",
                                                    Toast.LENGTH_LONG
                                                ).show()
                                            }
                                        })
                                } else {
                                    toast("Test Already Added")
                                }
                            } else {
                                toast("Please select test")
                            }
                        } catch (e: Exception) {
                            Log.i("e", e.toString())
                        }
                    }
                }
            })


            ApiUtils.getAPIService(this@VideoSessionActivity).get_clinical_test(
                SharedPrefs.getString(this@VideoSessionActivity, "sessionid")!!,
                username!!,
                usertype
            ).enqueue(object : Callback<ClinicalTestsResponse> {
                override fun onFailure(call: Call<ClinicalTestsResponse>, t: Throwable) {
                    Toast.makeText(
                        this@VideoSessionActivity,
                        "Something went wrong, please try again later",
                        Toast.LENGTH_LONG
                    ).show()
                }

                override fun onResponse(
                    call: Call<ClinicalTestsResponse>, response: Response<ClinicalTestsResponse>
                ) {
                    try {
                        if (response.isSuccessful && response.body() != null && response.body()!!.statuscode == 200) {
                            try {

                                var tests: ArrayList<String> = ArrayList()
                                var testsAdapter: ArrayAdapter<String>?
                                testsnameList.clear()
                                testsnameIdList.clear()

                                tests.add(pleaseSelect)
                                testsnameList.add(pleaseSelect)

                                for (test in response.body()!!.data!!.ctests) {
                                    tests.add(test.test_name)
                                    testsnameList.add(test.test_name)
                                    testsnameIdList.add(test.id)
                                }

                                testsAdapter = ArrayAdapter(
                                    this@VideoSessionActivity,
                                    android.R.layout.simple_spinner_item,
                                    tests
                                )
                                testsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                                mDialogView.ctests_drp.adapter = testsAdapter

                            } catch (e: java.lang.Exception) {
                                e.printStackTrace()
                            }

                            mDialogView.addctestsbtn.isClickable = true
                            mDialogView.addctestsbtn.visibility = View.VISIBLE
                            addTestsButtonFunctionality(mDialogView)
                        } else {
                            Toast.makeText(
                                this@VideoSessionActivity,
                                "Error: Tests list failed to load",
                                Toast.LENGTH_LONG
                            ).show()


                        }
                    } catch (e: Exception) {
                        Toast.makeText(
                            this@VideoSessionActivity,
                            "Clinical Tests list failed to load",
                            Toast.LENGTH_LONG
                        ).show()

                    }
                }
            })

            ApiUtils.getAPIService(this@VideoSessionActivity).get_medicine_list(
                SharedPrefs.getString(this@VideoSessionActivity, "sessionid")!!,
                username!!,
                usertype
            ).enqueue(object : Callback<MedicineList> {
                override fun onFailure(call: Call<MedicineList>, t: Throwable) {
                    toast("Something went wrong, please try again later")
                }

                @SuppressLint("SetTextI18n")
                override fun onResponse(
                    call: Call<MedicineList>, response: Response<MedicineList>
                ) {
                    try {
                        if (response.isSuccessful && response.body() != null && response.body()!!.statuscode == 200) {
                            mDialogView.addmedicinebtn.visibility = View.VISIBLE
                            mDialogView.addmedicinebtn.setOnClickListener {
                                try {
                                    var medicine = mDialogView.medicines_drp.selectedItem.toString()
                                    if (mednamelist.contains(medicine)) {
                                        Toast.makeText(
                                            this@VideoSessionActivity,
                                            "$medicine is already added",
                                            Toast.LENGTH_LONG
                                        ).show()
                                        return@setOnClickListener
                                    }
                                    var quantity =
                                        mDialogView.quantitymeddrpDialog.selectedItem.toString()
                                    var dosage =
                                        mDialogView.dosagemeddrpDialog.selectedItem.toString()
                                    var unit = mDialogView.unitdrpdwnnDialog.selectedItem.toString()
                                    var timeperiod =
                                        mDialogView.timeperioddrppDialog?.selectedItem.toString()
                                    mDialogView.doctor_notes_text?.setText(mDialogView.doctor_notes_text.text.toString() + "\n" + medicine + " " + quantity + " " + unit + "," + dosage + "," + timeperiod)
                                    mednamelist.add(medicine)
                                    medquantitylist.add(quantity)
                                    meddosagelist.add(dosage)
                                    medUnitlist.add(unit)
                                    medTimePeriodListlist.add(timeperiod)
                                    Log.e(TAG, "addmedicinebtn : ${mednamelist.size}")
//                                    createMedicineString()
//                                    if (medicinestring.equals("")) {
//                                        medicinestring =
//                                            medicineidList.get(medicinenameList.indexOf(medicine)) + "@#@" + appointmentId + "@#@" + dosage + "@#@" + quantity + "@#@" + unit + "@#@" + timeperiod
//                                    } else {
//                                        medicinestring =
//                                            medicinestring + "#@#" + medicineidList.get(
//                                                medicinenameList.indexOf(medicine)
//                                            ) + "@#@" + appointmentId + "@#@" + dosage + "@#@" + quantity + "@#@" + unit + "@#@" + timeperiod
//                                    }
                                    Log.e(TAG, "addmedicinebtn Medicine String $medicinestring")
                                } catch (e: Exception) {
                                    Log.e(TAG, "Medicine String Exception $e")
                                }

                            }
                            var medicines: ArrayList<String> = ArrayList()
                            var medicinesAdapter: ArrayAdapter<String>?
                            medicinenameList.clear()
                            medicineidList.clear()
                            for (medicine in response.body()!!.data!!.list!!) {
                                medicines.add(medicine.Brand_Name)
                                medicinenameList.add(medicine.Brand_Name)
                                medicineidList.add(medicine.Brand_ID.toString())
                            }
                            //  mDialogView.medicines_drp.setTitle("Select Medicine");
                            //   mDialogView.medicines_drp.setPositiveButton("Close");
                            medicinesAdapter = ArrayAdapter(
                                this@VideoSessionActivity,
                                android.R.layout.simple_spinner_item,
                                medicines
                            )
                            medicinesAdapter!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            mDialogView.medicines_drp.adapter = medicinesAdapter
                            mDialogView.addmedicinebtn.isClickable = true
                            setMedicineQuantityUnitDosagePeriodAdapters(mDialogView)
                        } else {
                            mDialogView.addmedicinebtn.visibility = View.GONE
                            mDialogView.medicinesdropdowns.visibility = View.GONE
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()

                        toast("Error: Medicine list failed to load")
                        mDialogView.addmedicinebtn.visibility = View.GONE
                        mDialogView.medicinesdropdowns.visibility = View.GONE
                    }
                }
            })

            //cancel button click of custom layout
            mDialogView.btn_okay.setOnClickListener {
                //dismiss dialog
                if (mDialogView.doctor_notes_text.text.toString().length < 1) {
                    toast("Empty Notes")
                } else if (!PatientAppointment.isDiagnosisUpdated) {
                    toast("Empty Diagnosis")
                } else {
                    createMedicineString()
                    if (com.example.ehealthai.utils.Constants.isInternetConnected(this@VideoSessionActivity)) {
                        showProgressDialog("Adding Prescription please wait")
                        PatientAppointment.doctorNotes =
                            mDialogView.doctor_notes_text?.text.toString()
                        notesupdated = true
                        val patientId = RequestBody.create("pid".toMediaTypeOrNull(), patientId)
                        val doctorId = RequestBody.create("doctorid".toMediaTypeOrNull(), doctorId)
                        val doc_type = RequestBody.create("doc_type".toMediaTypeOrNull(), "")
                        val description = RequestBody.create(
                            "description".toMediaTypeOrNull(),
                            mDialogView.doctor_notes_text.text.toString()
                        )
                        val apptId = RequestBody.create("apptid".toMediaTypeOrNull(), appointmentId)
                        sendFileUploadRequest1(apptId, patientId, doctorId, doc_type, description)
                    } else {
                        toast("No Internet connection")
                    }
                    //    mDialogView. progress_1.visibility = View.GONE
                    sendStatusMessage(MessageType.SAVING_NOTES)
                    Handler(Looper.getMainLooper()).postDelayed(
                        { sendStopStatusMsg(MessageType.SAVING_NOTES) }, TIME_OUT
                    )

                    mDialogView.doctor_notes_text.removeTextChangedListener(doctorAddNoteTextWatcher)
                    mAlertDialog?.dismiss()
                    Global.selectedid.equals(null)
                }

            }
            mDialogView.btn_cancel.setOnClickListener {
                //dismiss dialog
//                testlist?.clear()
//                list.clear()
                mDialogView.doctor_notes_text.removeTextChangedListener(doctorAddNoteTextWatcher)
                mAlertDialog?.dismiss()
            }
            mDialogView.doctor_notes_text.addTextChangedListener(doctorAddNoteTextWatcher)

        }

        video_btn.setOnClickListener {
            if (video_btn.isSelected) {
                if (videosSwapped) {
                    remoteSurfaceView?.visibility = View.GONE
                    iv_idle_image?.visibility = View.VISIBLE
                } else {
                    localSurfaceView?.visibility = View.GONE
                }
                agoraEngine?.enableLocalVideo(false)
                agoraEngine?.stopPreview()

                localVideoContainerFrame?.isEnabled = false
                localVideoContainerFrame?.isFocusable = false
                localVideoContainerFrame?.isClickable = false
//                localSurfaceView?.visibility = View.GONE
                sendStatusMessage(MessageType.TURN_OFF_CAMERA)
                Handler(Looper.getMainLooper()).postDelayed(
                    { sendStopStatusMsg(MessageType.TURN_ON_CAMERA) }, TIME_OUT
                )
                video_btn.isSelected = false
                video_btn.setImageResource(R.drawable.novideo)
                //   removeVideo(R.id.my_frame_layout)

            } else {
                agoraEngine?.enableLocalVideo(true)
                agoraEngine?.startPreview()
                localVideoContainerFrame?.isEnabled = true
                localVideoContainerFrame?.isFocusable = true
                localVideoContainerFrame?.isClickable = true

//                localSurfaceView?.visibility = View.VISIBLE
                if (videosSwapped) {
                    remoteSurfaceView?.visibility = View.VISIBLE
                    iv_idle_image?.visibility = View.GONE
                    localSurfaceView?.setZOrderOnTop(true)
                    localSurfaceView?.bringToFront()
                } else {
                    localSurfaceView?.visibility = View.VISIBLE
                }
                sendStatusMessage(MessageType.TURN_ON_CAMERA)
                Handler(Looper.getMainLooper()).postDelayed(
                    { sendStopStatusMsg(MessageType.TURN_ON_CAMERA) }, TIME_OUT
                )
                video_btn.isSelected = true
                video_btn.setImageResource(R.drawable.videoon)

                //  setupLocalVideo()
            }
        }
    }

    private fun createMedicineString() {
        Log.e(TAG, "createMedicineString Called: ${mednamelist.size}")

        try {
            medicinestring = ""
            mednamelist.forEachIndexed { index, item ->
                Log.e(TAG, "mednamelist[index]:  ${mednamelist[index]}")
                Log.e(TAG, "meddosagelist[index]:  ${meddosagelist[index]}")
                Log.e(TAG, "medquantitylist[index]:  ${medquantitylist[index]}")
                Log.e(TAG, "medUnitlist[index]:  ${medUnitlist[index]}")
                Log.e(TAG, "medTimePeriodListlist[index]:  ${medTimePeriodListlist[index]}")


                val mediciNameIndex = mednamelist[index]
                val dosage = meddosagelist[index]
                val quantity = medquantitylist[index]
                val unit = medUnitlist[index]
                val timePeriod = medTimePeriodListlist[index]

                Log.e(TAG, "medicinenameList Size:  ${medicinenameList.size}")

                Log.e(TAG, "medicinenameList:  ${medicinenameList.indexOf(mediciNameIndex)}")

                Log.e(
                    TAG,
                    "medicineidList:  ${medicineidList.get(medicinenameList.indexOf(mediciNameIndex))}"
                )

                if (medicinestring.equals("")) {
                    medicinestring =
                        medicineidList.get(medicinenameList.indexOf(mediciNameIndex)) + "@#@" + appointmentId + "@#@" + dosage + "@#@" + quantity + "@#@" + unit + "@#@" + timePeriod
                } else {
                    medicinestring = medicinestring + "#@#" + medicineidList.get(
                        medicinenameList.indexOf(mediciNameIndex)
                    ) + "@#@" + appointmentId + "@#@" + dosage + "@#@" + quantity + "@#@" + unit + "@#@" + timePeriod
                }
            }
            Log.e(TAG, "createMedicineString: ${medicinestring}")
        } catch (e: Exception) {
            medicinestring = ""
            Log.e(TAG, "createMedicineString Exception $e: ${medicinestring}")
            toast("Error while adding medicine try again")
        }
    }
//}


    private fun joinChannel() {
//
//        val addnotesButton = addNNotesButton()
//        val moreButton = moreButton()
//        val chatButton = chatbutton()


//        Handler(Looper.getMainLooper()).postDelayed({
//            sendStatusMessage(MessageType.OPENING_MSGS)
//            Handler(Looper.getMainLooper()).postDelayed({
//                sendStopStatusMsg(MessageType.OPENING_MSGS)
//            }, TIME_OUT)
//            count.visibility = View.GONE
//            val intent = Intent(this@VideoSessionActivity, VideoSessionChat::class.java)
//            intent.putExtra("patientId", patientId)
//            intent.putExtra("patientName", patientName)
//            intent.putExtra("image_name_p", "")
//            startActivity(intent)
//            Global.IntentFromHandler = true
//        }, 3000)

//        this.agView?.join(roomId, null, role = Constants.CLIENT_ROLE_BROADCASTER, uid)

//        writeImagePhoneGallery()

        LocalBroadcastManager.getInstance(this).registerReceiver(
            aLBReceiver, IntentFilter("callEnd")
        )

        /* chatButton.setOnClickListener {
             sendStatusMessage(MessageType.OPENING_MSGS)
             Handler(Looper.getMainLooper()).postDelayed({
                 sendStopStatusMsg(MessageType.OPENING_MSGS)
             }, TIME_OUT)
             count.visibility = View.GONE
             val intent = Intent(this, VideoSessionChat::class.java)
             intent.putExtra("patientId", patientId)
             intent.putExtra("patientName", patientName)
             intent.putExtra("image_name_p", "")
             startActivity(intent)
         }*//* moreButton.setOnClickListener {
             val builder = AlertDialog.Builder(this)
             builder.setTitle("More Options")

             val moreoptions = arrayOf("Vitals History", "Medicine History", "Patient Reports")
             builder.setItems(
                 moreoptions
             ) { dialog, which ->
                 when (which) {
                     0 -> {
                         val intent = Intent(this, VideoSessionVitalsHistory::class.java)
                         intent.putExtra("patientId", patientId)
                         intent.putExtra("patientName", patientName)
                         intent.putExtra("image_name_p", "")
                         startActivity(intent)
                     }

                     1 -> {
                         val intent = Intent(this, VideoSessionMedicineHistory::class.java)
                         intent.putExtra("patientId", patientId)
                         intent.putExtra("patientName", patientName)
                         intent.putExtra("image_name_p", "")
                         startActivity(intent)
                     }

                     2 -> {
                         val intent = Intent(this, VideoSessionPatientReports::class.java)
                         intent.putExtra("patientId", patientId)
                         intent.putExtra("patientName", patientName)
                         intent.putExtra("image_name_p", "")
                         startActivity(intent)
                     }
                 }
             }
             val dialog = builder.create()
             dialog.show()
         }*/

        val userData = SharedPrefs.getUserData(this@VideoSessionActivity)
        val username = userData!!.username
        val usertype = "DOCTOR"

        managedoctorStatus(username, usertype)

    }

    private fun checkInternetFrequently() {
        mMainHandler = Handler(Looper.getMainLooper())
        mMainHandler.post(myRunnable)
    }

    private var internetStatusPoorShown = false
    private fun manageSignals() {
        val internetStatus = checkInternet()
        val trimInternetStatus = internetStatus.trim()
        if (trimInternetStatus != "") {
//            toast(trimInternetStatus)
            when (trimInternetStatus) {
                "Good" -> {
                    signalstregnth.setImageResource(0)
                    signalstregnth.setImageResource(R.drawable.goodsignal)
                    internetStatusPoorShown = false
                }

                "Excellent" -> {
                    signalstregnth.setImageResource(0)
                    signalstregnth.setImageResource(R.drawable.goodsignal)
                    internetStatusPoorShown = false
                }

                "Average" -> {
                    signalstregnth.setImageResource(0)
                    signalstregnth.setImageResource(R.drawable.avg)
                    internetStatusPoorShown = false
                }

                "Poor" -> {
                    signalstregnth.setImageResource(0)
                    signalstregnth.setImageResource(R.drawable.poor)
                    if (!internetStatusPoorShown) {
                        val snackBar = Snackbar.make(
                            video_call_toolbar,
                            "You may experience difficulty in your session due to poor internet connection.",
                            15000
                        ).setAction(R.string.gotit) {

                        }.setTextColor(resources.getColor(R.color.white))
                            .setBackgroundTint(resources.getColor(R.color.colorPrimary))
                            .setActionTextColor(resources.getColor(R.color.white))
                        val view = snackBar.view
                        val params = view.layoutParams as FrameLayout.LayoutParams
                        params.topMargin = 150
                        params.gravity = Gravity.CENTER_HORIZONTAL
                        snackBar.show()
                        internetStatusPoorShown = true
                    }

                }

                "Connected" -> {
                }

                "Data Connected" -> {
                }

                "Not Connected" -> {
                }
            }
        }
    }

    private fun writeImagePhoneGallery() {
        //writing image to phone gallery
        val bm = BitmapFactory.decodeResource(resources, R.drawable.logo_splash)/*   val file1 = File(
               Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
               "virtualCallingBackgroundImg.PNG"
           )
           val outStream = FileOutputStream(file1)
           bm.compress(Bitmap.CompressFormat.PNG, 100, outStream)
           outStream.flush()
           outStream.close()*/
//        saveImageToGallery(bm)

    }

    private fun getDoctorDocumentsAppt(
        sessionid: String?, username: String?, usertype: String, mDialogView: View
    ) {

        ApiUtils.getAPIService(this@VideoSessionActivity)
            .get_doctor_documents_appt(sessionid!!, username!!, usertype, appointmentId)
            .enqueue(object : Callback<DoctorNotesResponse> {
                override fun onFailure(call: Call<DoctorNotesResponse>, t: Throwable) {
                    //Do nothing
                }

                override fun onResponse(
                    call: Call<DoctorNotesResponse>, response: Response<DoctorNotesResponse>
                ) {
                    //Do nothing
                    try {
                        docnotes = response.body()!!.data!!.documents[0].doctor_notes
                        mDialogView.doctor_notes_text.setText(docnotes)
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()

                    }
                }
            })
    }


    private fun addpatientDiagnnosisApi(username: String, usertype: String, selectedid: String) {
        ApiUtils.getAPIService(this@VideoSessionActivity)
            .add_patient_diagnosis_with_Other_Diagnosis(
                SharedPrefs.getString(this@VideoSessionActivity, "sessionid")!!,
                username!!,
                usertype,
                selectedid,
                selecteddiagnosis,
                PatientAppointment.appointmentId!!,
                PatientAppointment.patientId!!
            ).enqueue(object : Callback<AddDiagnosisResponse> {
                override fun onFailure(
                    call: Call<AddDiagnosisResponse>, t: Throwable
                ) {

                    //  Global.hideProgressDialog()

                    Toast.makeText(
                        this@VideoSessionActivity,
                        "Something went wrong, please try again later",
                        Toast.LENGTH_LONG
                    ).show()
                }

                override fun onResponse(
                    call: Call<AddDiagnosisResponse>, response: Response<AddDiagnosisResponse>
                ) {
                    //  Global.hideProgressDialog()
                    Toast.makeText(
                        this@VideoSessionActivity,
                        "Diagnosis Added Successfully !",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun managedoctorStatus(username: String?, usertype: String) {
        ApiUtils.getAPIService(this).manage_doctor_status(
            SharedPrefs.getString(this@VideoSessionActivity, "sessionid")!!,
            username!!,
            usertype,
            appointmentId,
            patientId,
            doctorId,
            "Busy"
        ).enqueue(object : Callback<ForgotPasswordResponse> {
            override fun onFailure(call: Call<ForgotPasswordResponse>, t: Throwable) {
                //Do nothing for now
            }

            override fun onResponse(
                call: Call<ForgotPasswordResponse>, response: Response<ForgotPasswordResponse>
            ) {
                try {
                } catch (e: Exception) {
                    e.printStackTrace()
                    toast("Something went wrong, please try again later")
                }
            }
        })
    }

    private fun sendNotification(patientId: String, message: String) {

        val userData = SharedPrefs.getUserData(this@VideoSessionActivity)
        val username = userData!!.username
        val usertype = "DOCTOR"
        ApiUtils.getAPIService(this@VideoSessionActivity).sendNotification(
            patientId,
            message,
            SharedPrefs.getString(this@VideoSessionActivity, "sessionid")!!,
            username!!,
            usertype
        ).enqueue(object : Callback<SendNotificationResponse> {
            override fun onFailure(call: Call<SendNotificationResponse>, t: Throwable) {
                //Do nothing

            }

            override fun onResponse(
                call: Call<SendNotificationResponse>, response: Response<SendNotificationResponse>
            ) {
                Log.i("issues", response.message().toString())
            }
        })
    }

    private fun sendFileUploadRequest1(
        appointmentId: RequestBody,
        patientId: RequestBody,
        doctorId: RequestBody,
        doc_type: RequestBody,
        description: RequestBody
    ) {
        val userData = SharedPrefs.getUserData(this)
        val sessionid = RequestBody.create(
            "sessionid".toMediaTypeOrNull(), SharedPrefs.getString(this, "sessionid")!!
        )
        val username = RequestBody.create(
            "username".toMediaTypeOrNull(), userData!!.username.toString()
        )
        val usertype = RequestBody.create("usertype".toMediaTypeOrNull(), "DOCTOR")
        val fdate = RequestBody.create("followup_date".toMediaTypeOrNull(), selectedfollowupdate)
        ApiUtils.getAPIService(this@VideoSessionActivity).uploadPatientFile1(
            appointmentId,
            patientId,
            doctorId,
            doc_type,
            description,
            sessionid,
            username,
            usertype,
            fdate
        ).enqueue(object : Callback<UploadPatientFileResponse> {
            override fun onFailure(call: Call<UploadPatientFileResponse>, t: Throwable) {
                toast("Something went wrong, please try again later")
                progressDialog?.dismiss()
            }

            override fun onResponse(
                call: Call<UploadPatientFileResponse>, response: Response<UploadPatientFileResponse>
            ) {
                progressDialog?.dismiss()
                try {
                    if (response.isSuccessful && response.body() != null && response.body()!!.statuscode == 200) {
                        if (medicinestring.equals("")) {
                            Log.e(TAG, "onResponse: medicinestring is empty $medicinestring")
                        } else {
                            showProgressDialog("Adding medicine please wait")
                            SendPatientMedicines(medicinestring)
                        }

                    } else {
                        toast("Please try again with a different file")
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    toast("Something went wrong, please try again later")
                }
            }
        })

    }

    private fun SendPatientMedicines(description: String) {
        val userData = SharedPrefs.getUserData(this)
        val sessionid = SharedPrefs.getString(this, "sessionid")!!
        val username = userData!!.username.toString()
        val usertype = "DOCTOR"
        ApiUtils.getAPIService(this@VideoSessionActivity)
            .patient_medicine(sessionid, username, usertype, description)
            .enqueue(object : Callback<ForgotPasswordResponse> {
                override fun onFailure(call: Call<ForgotPasswordResponse>, t: Throwable) {
                    toast("Error while adding medicine, please try again later")
                    progressDialog?.dismiss()
                }

                override fun onResponse(
                    call: Call<ForgotPasswordResponse>, response: Response<ForgotPasswordResponse>
                ) {
                    progressDialog?.dismiss()
                    try {
                        if (response.isSuccessful && response.body() != null && response.body()!!.statuscode == "200") {
                            toast("Medicine added successfully")
                            toast("Notes Updated")
                        } else {
                            //   mDialogView. progress_1.visibility = View.GONE
                            toast("Error while adding medicine, please try again later")
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        toast("Error while adding medicine, please try again later")
                    }
                }
            })
    }

    private fun checkInternet(): String {
        var status: String = ""
        try {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (cm != null && cm.activeNetworkInfo != null) {
                val activeNetwork: NetworkInfo? = cm.activeNetworkInfo!!
                val isConnected: Boolean = activeNetwork!!.isConnectedOrConnecting == true
                val mWifi: NetworkInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI)!!
                val mDataConnection: NetworkInfo =
                    cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)!!

                if (isConnected) {
                    var connecqual = ""
                    if (mWifi.isConnected) {
                        val wifiManager =
                            context.getSystemService(Context.WIFI_SERVICE) as WifiManager
                        val numberOfLevels = 5
                        val wifiInfo: WifiInfo = wifiManager.getConnectionInfo()
                        val level =
                            WifiManager.calculateSignalLevel(wifiInfo.getRssi(), numberOfLevels)
                        if (level == 3) {
                            connecqual = "Average"
                        } else if (level == 4) {
                            connecqual = "Good"
                        } else if (level >= 5) {
                            connecqual = "Excellent"
                        } else {
                            connecqual = "Poor"
                        }
//                        status = "Internet Status: Connected" + "Quality : " + connecqual
                        status = connecqual
                        tv_internet_status_video_session.text = "Quality : $connecqual"
                    } else if (mDataConnection.isConnected) {
                        if (mSignalStrength == 0) {
                            connecqual = "Poor"
                        } else if (mSignalStrength == 2 || mSignalStrength == 3) {
                            connecqual = "Average"
                        } else if (mSignalStrength == 4) {
                            connecqual = "Good"
                        } else if (mSignalStrength >= 5) {
                            connecqual = "Excellent"
                        } else {
                            connecqual = "Poor"
                        }
                        status = connecqual
                        tv_internet_status_video_session.text = "Quality : $connecqual"

                    } else {
                        status = "Not Connected"
                        tv_internet_status_video_session.text = "Wifi Not Connected"
                    }
                } else {
                    status = "Not Connected"
                    tv_internet_status_video_session.text = "Not Connected"
                    Log.e(TAG, "checkInternet: $status")
                }
            } else {
                status = "Not Connected"
                tv_internet_status_video_session.text = "Not Connected"
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        return status
    }

    private fun realTimeMessaging() {
        mChatManager = (application as MyApplication).getChatManager()
        mRtmListener = MyRtmClientListener()
        if (mRtmListener != null) {
            mChatManager?.registerListener(mRtmListener!!)
        }
        mRtmClient = mChatManager?.rtmClient
        doRTMLogin()
    }

    private fun doRTMLogin() {
        Log.e("RTM LOGIN DOC ID ", doctorId)
        mRtmClient?.login(null, doctorId, object : ResultCallback<Void?> {
            override fun onSuccess(responseInfo: Void?) {
                Log.i("", "login success")
                isRTMLogin = true

            }

            override fun onFailure(errorInfo: ErrorInfo) {
                Log.i("", "login failed: " + errorInfo.errorCode)
                isRTMLogin = false

            }
        })
    }

    private fun sendStopStatusMsg(type: MessageType) {
        val time =
            if (mTimeSentLastMsg.containsKey(type.toString())) mTimeSentLastMsg.getValue(type.toString()) else System.currentTimeMillis()
        if (System.currentTimeMillis() - time > 3) {
            sendStatusMessage(MessageType.STOP_STATUS)
        }
    }

    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    private fun findDiagnosisId(name: String): String {
        var selectedid = "empty"
        try {
            if (diagnosisnameList != null && diagnosisnameList.size > 0) {
                val index = diagnosisnameList.indexOf(name)
                selectedid = diagnosisidList[index]
            }
            return selectedid
        } catch (e: Exception) {
            return selectedid
        }
    }

    private fun findTestId(name: String): String {
        var selectedTestId = "empty"
        if (testsnameList != null && testsnameList.size > 0) {
            val index = testsnameList.indexOf(name)
            selectedTestId = testsnameIdList[index]
        }
        return selectedTestId
    }

    private fun sendStatusMessage(messageType: MessageType) {
        val data = ByteArray(1)
        val statusMessage = mRtmClient!!.createMessage()
        statusMessage.setRawMessage(data, messageType.toString())
        mTimeSentLastMsg[messageType.toString()] = System.currentTimeMillis()
        sendPeerMessage(statusMessage)
    }

    private fun sendPeerMessage(message: RtmMessage) {
        if (patientId == null) {
            showToast("Patient not found")
            return
        }
        mRtmClient?.sendMessageToPeer(
            patientId,
            message,
            mChatManager!!.sendMessageOptions,
            object : ResultCallback<Void?> {
                override fun onSuccess(aVoid: Void?) {
                    // do nothing
                }

                override fun onFailure(errorInfo: ErrorInfo) {
                    // refer to RtmStatusCode.PeerMessageState for the message state
                    try {
                        val errorCode = errorInfo.errorCode
                        runOnUiThread {
                            when (errorCode) {
                                RtmStatusCode.PeerMessageError.PEER_MESSAGE_ERR_TIMEOUT, RtmStatusCode.PeerMessageError.PEER_MESSAGE_ERR_FAILURE -> showToast(
                                    getString(R.string.send_msg_failed)
                                )

                                RtmStatusCode.PeerMessageError.PEER_MESSAGE_ERR_PEER_UNREACHABLE -> showToast(
                                    getString(R.string.peer_offline)
                                )

                                RtmStatusCode.PeerMessageError.PEER_MESSAGE_ERR_CACHED_BY_SERVER -> showToast(
                                    getString(R.string.message_cached)
                                )
                            }

                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            })
    }

    private val doctorAddNoteTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun afterTextChanged(editable: Editable?) {
            if (editable == null) return
            val lastMessageChange =
                if (mTimeSentLastMsg.containsKey(MessageType.START_WRITING_NOTES.toString())) {
                    mTimeSentLastMsg[MessageType.START_WRITING_NOTES.toString()]!!
                } else 0L

            if (editable.isNotEmpty() && (lastMessageChange == 0L || System.currentTimeMillis() - lastMessageChange > 3)) {

                sendStatusMessage(MessageType.START_WRITING_NOTES)
                Handler(Looper.getMainLooper()).postDelayed(
                    { sendStopStatusMsg(MessageType.START_WRITING_NOTES) }, TIME_OUT
                )
            }
        }
    }

    private fun showSnackBar(snackBarText: String) {

        val snackBar = Snackbar.make(clVideoViews, snackBarText, Snackbar.LENGTH_LONG)
            .setAction(R.string.gotit) {}.setActionTextColor(resources.getColor(R.color.white))

        if (snackBar != null) {
            val view = snackBar.view
            if (view != null) {
                val params = view.layoutParams as FrameLayout.LayoutParams
                with(params) {
                    topMargin = 150
                    gravity = Gravity.CENTER_HORIZONTAL
                }
                snackBar.show()
            }
        }

    }

    private fun showStatus(state: MessageType?) {
        if (state != null) {
            when (state) {
                MessageType.STATUS_START_TYPING_MSG -> {
                    //statusc_text.text = "Patient typing message..."
                    showSnackBar("Patient is typing chat message...")
                }

                MessageType.ONLINE -> {
                    statusc_text.text = "Online"
                }

                MessageType.OPENING_NOTES_SECTION -> {
                    showSnackBar("Patient has opened notes...")
                }

                MessageType.START_WRITING_NOTES -> {
                }

                MessageType.OPENING_MSGS -> {
                    showSnackBar("Patient has opened chat messages...")
                }

                MessageType.SAVING_NOTES -> {
                }

                MessageType.MIC_ON -> {
                    showSnackBar("Patient has unmuted Mic...")
                }

                MessageType.MUTE_MIC -> {
                    showSnackBar("Patient has muted Mic...")
                }

                MessageType.TURN_OFF_CAMERA -> {
                    showSnackBar("Patient has turned off Camera...")
                }

                MessageType.TURN_ON_CAMERA -> {
                    showSnackBar("Patient has turned on Camera...")
                }

                MessageType.STOP_STATUS -> {
                    if (statusc_text != null) {
                        statusc_text.text = "Online"
                    }
                }

                else -> {}
            }
        }
    }

    private fun parseIncomingMessage(message: RtmMessage, from: String) {
        runOnUiThread {
            if (message.rawMessage.size == 1 && from == patientId) {
                val type = message.text
                showStatus(MessageType.fromValue(type))
            }
        }
    }

    inner class MyRtmClientListener : RtmClientListener {
        override fun onConnectionStateChanged(state: Int, reason: Int) {

        }

        override fun onMessageReceived(message: RtmMessage, peerId: String) {
            parseIncomingMessage(message, peerId)
        }


        override fun onTokenExpired() {}
        override fun onTokenPrivilegeWillExpire() {

        }

        override fun onPeersOnlineStatusChanged(map: Map<String, Int>) {}
    }

    fun containsCaseInsensitive(s: String?, l: List<String>): Boolean {
        for (string in l) {
            if (string.equals(s, ignoreCase = true)) {
                return true
            }
        }
        return false
    }


//Muti Agora Video Call Implementation (Full Rtc Version)

    private lateinit var root: File
    private var assetManager: AssetManager? = null
    private var createdVirtualImageFilePath: String? = null

    // Fill the App ID of your project generated on Agora Console.
    private val appId = "1634986669fd433c903f131af715ba6b"

    // Fill the channel name.
    private var channelName = ""

    // Fill the temp token generated on Agora Console.
    private val token = ""

    // An integer that identifies the local user.
    private var uid = 0
    private var remoteUid = 0 // Stores the uid of the remote user

    private var isJoined = false

    private var agoraEngine: RtcEngine? = null

    //SurfaceView to render local video in a Container.
    private var localSurfaceView: SurfaceView? = null
    private var remoteSurfaceView: SurfaceView? = null

    private val requestedPermissionsReqCode = 22
    private val requestedPermissions = arrayOf(
        Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA
    )

    private fun setVirtualBackground() {
//        val virtualBackgroundSource = VirtualBackgroundSource()
//        virtualBackgroundSource.backgroundSourceType = VirtualBackgroundSource.BACKGROUND_COLOR
//        virtualBackgroundSource.color = 0xFFFFFF
//        // Set processing properties for background
//        val segmentationProperty = SegmentationProperty()
//        segmentationProperty.modelType = SegmentationProperty.SEG_MODEL_AI
//        // Use SEG_MODEL_GREEN if you have a green background
//        segmentationProperty.greenCapacity =
//            0.5f // Accuracy for identifying green colors (range 0-1)
//        // Enable or disable virtual background
//        agoraEngine!!.enableVirtualBackground(
//            true,
//            virtualBackgroundSource, segmentationProperty
//        )

        if (!createdVirtualImageFilePath.isNullOrEmpty()) {
//            val virtualBackgroundSource = VirtualBackgroundSource()
//            virtualBackgroundSource.backgroundSourceType = VirtualBackgroundSource.BACKGROUND_COLOR
//            virtualBackgroundSource.color = 0xFFFFFF
//
            val virtualBackgroundSource = VirtualBackgroundSource()
            virtualBackgroundSource.backgroundSourceType = VirtualBackgroundSource.BACKGROUND_IMG
            virtualBackgroundSource.source = createdVirtualImageFilePath
            // Set processing properties for background
            val segmentationProperty = SegmentationProperty()
            segmentationProperty.modelType = SegmentationProperty.SEG_MODEL_AI
            // Use SEG_MODEL_GREEN if you have a green background
//            segmentationProperty.greenCapacity =
//                0.5f // Accuracy for identifying green colors (range 0-1)
//            // Enable or disable virtual background
            agoraEngine!!.enableVirtualBackground(
                true, virtualBackgroundSource, segmentationProperty
            )
//        }
        }
    }

    private fun customOnCreate() {
        // Open the asset
        if (!checkSelfPermission()) {
            ActivityCompat.requestPermissions(
                this, requestedPermissions, requestedPermissionsReqCode
            )
        }
        //UnComment If Virtual Background is required
        createTemporaryFileForVirtualBackground()
        Log.e(TAG, "customOnCreate: $agoraEngine")
        setupVideoSDKEngine()
        for (e in MyApplication.remoteUser) {
            if (e.key) {
                val remoteUserId: Int = MyApplication.remoteUser.getValue(e.key)
                if (videosSwapped) {
                    videosSwapped = false
                    setupRemoteVideo(remoteUserId)
                } else {
                    setupRemoteVideo(remoteUserId)
                }
                break
            }
        }

    }

    private fun setupVideoSDKEngine() {
        try {
            val config = RtcEngineConfig()
            config.mContext = baseContext
            config.mAppId = appId
            config.mEventHandler = mRtcEventHandler
            agoraEngine = RtcEngine.create(config)
            // By default, the video module is disabled, call enableVideo to enable it.
            agoraEngine?.enableVideo()

        } catch (e: Exception) {
            showMessage(e.toString())
        }
    }


    private val TAG = "VideoSessionActivity"
    private val mRtcEventHandler: IRtcEngineEventHandler = object : IRtcEngineEventHandler() {

        override fun onError(err: Int) {
            super.onError(err)
            Log.e(TAG, "onError: $err")
        }

        override fun onConnectionLost() {
            super.onConnectionLost()
            Log.e(TAG, "on COnnection lost")
        }

        override fun onRejoinChannelSuccess(channel: String?, uid: Int, elapsed: Int) {
            super.onRejoinChannelSuccess(channel, uid, elapsed)
            Log.e(TAG, "onRejoinChannelSuccess $channel, $uid, $elapsed")

        }

        override fun onLeaveChannel(stats: RtcStats?) {
            super.onLeaveChannel(stats)
        }

        override fun onUserEnableLocalVideo(uid: Int, enabled: Boolean) {
            super.onUserEnableLocalVideo(uid, enabled)
            if (!enabled) {
                runOnUiThread {
                    if (videosSwapped) {

                    } else fl_remote_view.removeAllViews()
                }
            } else {
                runOnUiThread {
                    if (videosSwapped) {

                    } else setupRemoteVideo(uid)
                }
            }
        }

        override fun onRemoteVideoStateChanged(uid: Int, state: Int, reason: Int, elapsed: Int) {
            super.onRemoteVideoStateChanged(uid, state, reason, elapsed)


            if (state == Constants.REMOTE_VIDEO_STATE_STOPPED || state == Constants.REMOTE_VIDEO_STATE_FROZEN) {
//                    // The remote user's video has been turned off
//                    // You can hide the remote video view or take appropriate action
                runOnUiThread {
                    remoteUserVideoEnabled = false
//                    if (videosSwapped) {
//                        local_video_view_container.removeView(localSurfaceView!!)
//                        localSurfaceView?.visibility = View.GONE
//                    } else {
//                        remoteViewsAdapter?.remoteUserVideoChanged(uid, true)
//                    }
//                    fl_remote_view.removeAllViews()
//                    val container = findViewById<FrameLayout>(R.id.fl_remote_view)
//                    container.removeView(remoteSurfaceView)

                    if (videosSwapped) {
//                        local_video_view_container.removeView(localSurfaceView!!)
//                        localSurfaceView?.visibility = View.GONE
                        localVideoContainerFrame?.visibility = View.INVISIBLE
                    } else {
                        fl_remote_view?.visibility = View.INVISIBLE
                        iv_idle_image?.visibility = View.VISIBLE
                    }

                }
            } else if (state == Constants.REMOTE_VIDEO_STATE_PLAYING) {
                remoteUserVideoEnabled = true
//                    // The remote user's video has resumed
//                    // You can show the remote video view or take appropriate action
                runOnUiThread {
//                    if (videosSwapped) {
//                        local_video_view_container.addView(localSurfaceView!!)
//                        localSurfaceView?.visibility = View.VISIBLE
//                    } else {
//                        remoteViewsAdapter?.remoteUserVideoChanged(uid, false)
//                    }
//                    val container = findViewById<FrameLayout>(R.id.fl_remote_view)
//                    container.removeView(remoteSurfaceView)
                    if (videosSwapped) {
                        localVideoContainerFrame?.visibility = View.VISIBLE
                    } else {
                        fl_remote_view?.visibility = View.VISIBLE
                        iv_idle_image?.visibility = View.GONE
                    }
//                    setupRemoteVideo(uid)
                }
                Log.e("TAG", "onRemoteVideoStateChanged uid: $uid")
                Log.e("TAG", "onRemoteVideoStateChanged state: $state")
                Log.e("TAG", "onRemoteVideoStateChanged reason: $reason")
                Log.e("TAG", "onRemoteVideoStateChanged elapsed: $elapsed")
            }
        }

        @Deprecated("Deprecated in Java")
        override fun onFirstRemoteVideoDecoded(
            uid: Int, width: Int, height: Int, elapsed: Int
        ) {
            super.onFirstRemoteVideoDecoded(uid, width, height, elapsed)
            runOnUiThread {
                otherpatyjoined = true
                isPatientVideoShown = true
                otherpatyleftafterjoin = false
                val spannableString = SpannableString("$patientName joined")
                spannableString.setSpan(
                    ForegroundColorSpan(resources.getColor(android.R.color.white)),
                    0,
                    spannableString.length,
                    0
                )
                statusc_text.visibility = View.VISIBLE
                statusc_text.text = patientName + " joined"
                Log.e(TAG, "onFirstRemoteVideoDecoded: Remote user joined")
//                if (mediaProjectionPermissionAllowed) {
//                    if (!isServiceRunningInForeground(
//                            this@VideoSessionActivity,
//                            MediaProjectionService::class.java
//                        )
//                    ) {
//                        ContextCompat.startForegroundService(
//                            this@VideoSessionActivity,
//                            Intent(this@VideoSessionActivity, MediaProjectionService::class.java)
//                        )
//                        if (mResultCode != null && mData != null && !isAlreadyCapturing) {
//                            isFirstCapture = true
//                            captureScreenContent(mResultCode!!, mData!!)
//                        }
//                    } else {
//                        if (mResultCode != null && mData != null && !isAlreadyCapturing) {
//                            isFirstCapture = true
//                            captureScreenContent(mResultCode!!, mData!!)
//                        }
//                    }

            }
        }

        // Listen for the remote host joining the channel to get the uid of the host.
        override fun onUserJoined(uid: Int, elapsed: Int) {
            remoteUid = uid
//            showMessage("Remote user joined $uid")
            runOnUiThread {
                statusc_text.visibility = View.VISIBLE
                statusc_text.text = "Connected"
                MyApplication.remoteUser[true] = uid
                setupRemoteVideo(uid)
            }

        }

        override fun onJoinChannelSuccess(channel: String, uid: Int, elapsed: Int) {
            isJoined = true
//                showMessage("Joined Channel $channel")
        }

        override fun onUserOffline(uid: Int, reason: Int) {
            Log.e(TAG, "onUserOffline: $uid $reason")
            runOnUiThread {
                otherpatyjoined = false
                otherpatyleftafterjoin = true
                isPatientVideoShown = false

                isFirstCapture = true
                val spannableString = SpannableString("$patientName Left")
                spannableString.setSpan(
                    ForegroundColorSpan(resources.getColor(android.R.color.white)),
                    0,
                    spannableString.length,
                    0
                )
                if (!isFinishing) endCallBtn.performClick()
//                            Globbal.callStatus = ""

            }

//                showMessage("Remote user offline $uid $reason")
            runOnUiThread {
                remoteSurfaceView?.visibility = View.GONE
            }
        }
    }

    private fun showMessage(message: String?) {
        runOnUiThread {
            Toast.makeText(
                applicationContext, message, Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun createTemporaryFileForVirtualBackground() {
        assetManager = assets
        root = applicationContext.cacheDir
        try {
            // Open the asset
            val inputStream = assets.open("app_logo.png") // Replace with your asset file name

            // Create a temporary file
            val tempFile = File.createTempFile(
                "virtual_background_image", ".png", cacheDir
            ) // Use getCacheDir() for cache or specify another directory

            // Copy the asset to the temporary file
            val outputStream = FileOutputStream(tempFile)
            val buffer = ByteArray(1024)
            var length: Int
            while (inputStream.read(buffer).also { length = it } > 0) {
                outputStream.write(buffer, 0, length)
            }
            outputStream.flush()
            outputStream.close()
            inputStream.close()

            // Now, tempFile.getAbsolutePath() contains the path to the copied asset
            createdVirtualImageFilePath = tempFile.absolutePath
            Log.e(
                TAG, "createTemporaryFileForVirtualBackground: $createdVirtualImageFilePath"
            )

            // Use tempFilePath as needed with the API that expects a file path
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun checkSelfPermission(): Boolean {
        return !(ContextCompat.checkSelfPermission(
            this, requestedPermissions[0]
        ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
            this, requestedPermissions[1]
        ) != PackageManager.PERMISSION_GRANTED)
    }

    fun customJoinChannel() {
        if (checkSelfPermission()) {
            val options = ChannelMediaOptions()

            // For a Video call, set the channel profile as COMMUNICATION.
            options.channelProfile = Constants.CHANNEL_PROFILE_COMMUNICATION
            // Set the client role as BROADCASTER or AUDIENCE according to the scenario.
            options.clientRoleType = Constants.CLIENT_ROLE_BROADCASTER
            // Display LocalSurfaceView.
            customSetupLocalVideo()
            localSurfaceView!!.visibility = View.VISIBLE
            // Start local preview.
            agoraEngine!!.startPreview()
            // Join the channel with a temp token.
            // You need to specify the user ID yourself, and ensure that it is unique in the channel.

            agoraEngine!!.joinChannel(null, channelName, uid, options)
        } else {
            Toast.makeText(
                applicationContext, "Permissions was not granted", Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun customLeaveChannel(view: View?) {
        if (!isJoined) {
            showMessage("Join a channel first")
        } else {
            agoraEngine?.stopPreview()
            agoraEngine?.leaveChannel()
            showMessage("You left the channel")
            if (remoteSurfaceView != null) remoteSurfaceView?.visibility = View.GONE
            // Stop local video rendering.
            if (localSurfaceView != null) localSurfaceView!!.visibility = View.GONE
            isJoined = false
        }
    }

    private fun customSetupLocalVideo() {
//        val container = findViewById<FrameLayout>(R.id.local_video_view_container)
        // Create a SurfaceView object and add it as a child to the FrameLayout.
        localSurfaceView = SurfaceView(baseContext)
        localSurfaceView?.setZOrderMediaOverlay(true)
        //TODO RECHECK
        localVideoContainerFrame.addView(localSurfaceView)
        // Call setupLocalVideo with a VideoCanvas having uid set to 0.
        agoraEngine!!.setupLocalVideo(
            VideoCanvas(
                localSurfaceView, VideoCanvas.RENDER_MODE_HIDDEN, 0
            )
        )
        setVirtualBackground()
    }

    private fun setupRemoteVideo(uid: Int) {
        remoteSurfaceView = SurfaceView(baseContext)
//        remoteSurfaceView?.setZOrderMediaOverlay(true)
        fl_remote_view.addView(remoteSurfaceView)
        agoraEngine!!.setupRemoteVideo(
            VideoCanvas(
                remoteSurfaceView, VideoCanvas.RENDER_MODE_FIT, uid
            )
        )
        // Display RemoteSurfaceView.
        remoteSurfaceView?.visibility = View.VISIBLE
        //TODO RECHECK
        localVideoContainerFrame.bringToFront()

    }


    private fun customOnDestroy() {


        agoraEngine?.stopPreview()
        agoraEngine?.leaveChannel()

        if (localSurfaceView != null) localSurfaceView!!.visibility = View.GONE
        isJoined = false
        // Destroy the engine in a sub-thread to avoid congestion
//        MyApplication.remoteViewsList.clear()
        MyApplication.remoteUser[false] = uid
        MyApplication.remoteUser.clear()
        MyApplication.videoSwappedWithUid = 0
        MyApplication.videosSwapped = false
        MyApplication.remoteUserVideoEnabled = false
        Thread {
            RtcEngine.destroy()
            agoraEngine = null
            if (createdVirtualImageFilePath != null) {
                deleteVirtualCreatedFile(createdVirtualImageFilePath!!)
            }
        }.start()

        isPatientVideoShown = false
        isFirstCapture = true
        if (isServiceRunningInForeground(this, MediaProjectionService::class.java)) {
            stopService(Intent(this, MediaProjectionService::class.java))
        }
    }

    private fun deleteVirtualCreatedFile(filePath: String) {
        try {
            val imageFile = File(filePath)
            if (imageFile.exists()) {
                val deleted = imageFile.delete()
                if (deleted) {
                    createdVirtualImageFilePath = null
                } else {
                    //Failed to delete file
                }
            } else {
                //File doesn't exist
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun showLocalVideoInRemoteView(
        uid: Int, mFrameLayout: FrameLayout, mRemoteSurfaceView: SurfaceView
    ) {
        mFrameLayout.addView(mRemoteSurfaceView)
        agoraEngine!!.setupLocalVideo(
            VideoCanvas(
                mRemoteSurfaceView, VideoCanvas.RENDER_MODE_FIT, uid
            )
        )
        // Display RemoteSurfaceView.
        mRemoteSurfaceView.visibility = View.VISIBLE
    }

    private fun showRemoteVideoInLocalView(
        uid: Int, mLocalFrameLayout: FrameLayout, mLocalSurfaceView: SurfaceView
    ) {
        runOnUiThread {
            //TODO RECHECK
//            val container = findViewById<FrameLayout>(R.id.local_video_view_container)
            // Create a SurfaceView object and add it as a child to the FrameLayout.
//            localSurfaceView = SurfaceView(baseContext)
            mLocalFrameLayout.addView(mLocalSurfaceView)
            // Call setupLocalVideo with a VideoCanvas having uid set to 0.
            agoraEngine!!.setupRemoteVideo(
                VideoCanvas(
                    mLocalSurfaceView, VideoCanvas.RENDER_MODE_HIDDEN, uid
                )
            )
//            localSurfaceView!!.visibility = View.VISIBLE
            mLocalSurfaceView!!.visibility = View.VISIBLE
        }


    }

    private fun swapVideosLocalToRemote(
        localUid: Int,
        remoteUid: Int,
        mRemoteFrameLayout: FrameLayout,
        mRemoteSurfaceView: SurfaceView,
        mLocalFrameLayout: FrameLayout,
        mLocalSurfaceView: SurfaceView,

        ) {
        showLocalVideoInRemoteView(localUid, mRemoteFrameLayout, mRemoteSurfaceView)
        showRemoteVideoInLocalView(remoteUid, mLocalFrameLayout, mLocalSurfaceView)
    }

    private fun swapVideosRemoteToLocal(
        localUid: Int,
        remoteUid: Int,
        mRemoteFrameLayout: FrameLayout,
        mRemoteSurfaceView: SurfaceView
    ) {
        customSetupLocalVideo()
        setupRemoteVideo(remoteUid, mRemoteFrameLayout, mRemoteSurfaceView)
    }

    private fun setupRemoteVideo(
        uid: Int, mRemoteFrameLayout: FrameLayout, mRemoteSurfaceView: SurfaceView
    ) {
        mRemoteFrameLayout.addView(mRemoteSurfaceView)
        agoraEngine!!.setupRemoteVideo(
            VideoCanvas(
                mRemoteSurfaceView, VideoCanvas.RENDER_MODE_FIT, uid
            )
        )
        // Display RemoteSurfaceView.
        mRemoteSurfaceView?.visibility = View.VISIBLE
    }

    //    private var videoSwappedWithUid: Int = 0
//    private var videosSwapped = false
    private fun toggleVideos(
        remoteUserId: Int, mRemoteFrameLayout: FrameLayout, mRemoteSurfaceView: SurfaceView
    ) {

        if (videoSwappedWithUid == 0 || videoSwappedWithUid == remoteUserId) {
            Log.e(TAG, "toggleVideos: If")
            mRemoteFrameLayout.removeAllViews()
            mRemoteSurfaceView.visibility = View.GONE

            val localContainer = findViewById<FrameLayout>(R.id.local_video_view_container)
            // Create a SurfaceView object and add it as a child to the FrameLayout.
            localContainer.removeAllViews()
            localSurfaceView!!.visibility = View.GONE


            if (!videosSwapped) {
                swapVideosLocalToRemote(
                    0,
                    remoteUserId,
                    mRemoteFrameLayout,
                    mRemoteSurfaceView,
                    localContainer,
                    localSurfaceView!!
                )
                videosSwapped = true
            } else {
                swapVideosRemoteToLocal(0, remoteUserId, mRemoteFrameLayout, mRemoteSurfaceView)
                videosSwapped = false
            }
        } else {
            //TODO CHECK CODE USAGE MIGHT BE USELESS
            Log.e(TAG, "toggleVideos: else")
            mRemoteFrameLayout.removeAllViews()
            mRemoteSurfaceView.visibility = View.GONE

            val localContainer = findViewById<FrameLayout>(R.id.local_video_view_container)
            // Create a SurfaceView object and add it as a child to the FrameLayout.
            localContainer.removeAllViews()
            localSurfaceView!!.visibility = View.GONE
            swapVideosRemoteToLocal(0, remoteUserId, mRemoteFrameLayout, mRemoteSurfaceView)
            videosSwapped = false

            mRemoteFrameLayout.removeAllViews()
            mRemoteSurfaceView.visibility = View.GONE
//
            // Create a SurfaceView object and add it as a child to the FrameLayout.
            localContainer.removeAllViews()
            localSurfaceView!!.visibility = View.GONE

            if (!videosSwapped) {
                Log.e(TAG, "toggleVideos: videosSwapped else $videosSwapped")
                swapVideosLocalToRemote(
                    0,
                    remoteUserId,
                    mRemoteFrameLayout,
                    mRemoteSurfaceView,
                    localContainer,
                    localSurfaceView!!
                )
                videosSwapped = true
            } else {
                Log.e(TAG, "toggleVideos: videosSwapped else $videosSwapped")
                swapVideosRemoteToLocal(0, remoteUserId, mRemoteFrameLayout, mRemoteSurfaceView)
                videosSwapped = false
            }
        }
        videoSwappedWithUid = remoteUserId
    }

    private var progressDialog: ProgressDialog? = null

    fun showProgressDialog(message: String?) {
        progressDialog = ProgressDialog(this@VideoSessionActivity)
        progressDialog?.setMessage(message)
        progressDialog?.setCancelable(false)
        progressDialog?.show()
        val progressbar = progressDialog?.findViewById<ProgressBar>(android.R.id.progress)
        progressbar?.indeterminateDrawable?.setColorFilter(
            Color.parseColor("#d32e33"), PorterDuff.Mode.SRC_IN
        )
    }

    fun hideProgressDialog() {
        if (progressDialog != null && progressDialog?.isShowing == true) {
            progressDialog?.hide()
        }
    }

    private fun addDiagnosisButtonFunctionality(mDialogView: View) {
        try {
            if (mDialogView != null) {
                dygadapter = dyglistAdapter(context,
                    AddNotesFragment.mDiagsList,
                    "Diagnosis",
                    context,
                    object : IClinicalTestItemToDelete {
                        override fun findDiagnosisIdAndDeleteItem(
                            diagnosisName: String, itemPosition: Int
                        ) {
                            if (!mDialogView.diagnosis_drp.selectedItem.toString().equals("")) {
                                val diagId = findDiagnosisId(diagnosisName)
                                if (diagId != "empty") {
                                    dygadapter?.callingDeleteApi(
                                        itemPosition, diagId, "", diagnosisName
                                    )
                                } else {
                                    dygadapter?.callingDeleteApi(
                                        itemPosition, "", diagnosisName, diagnosisName
                                    )
                                }
                            }
                        }

                    })
                mDialogView.dygnnosislistt.adapter = dygadapter
                mDialogView.etOtherDiagnosis.setText(PatientAppointment.otherDiagnosisText)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun addTestsButtonFunctionality(mDialogView: View) {
        try {
            if (mDialogView != null) {
                dygTestsadapter = dygTestAdapter(
                    context, PatientAppointment.selectedClinicalTests, "Test", context
                )
                mDialogView.rv_clinicalTests.adapter = dygTestsadapter
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun setMedicineQuantityUnitDosagePeriodAdapters(mDialogView: View) {
        if (mDialogView != null) {
            val qtyArray = resources.getStringArray(R.array.quantitydrop)
            val dosageArray = resources.getStringArray(R.array.dosagedrop)
            val unitArray = resources.getStringArray(R.array.unitdrop)
            val timeArray = resources.getStringArray(R.array.timeperiod)
            val spinnerQtyArrayAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
                context, R.layout.spinner_row, qtyArray
            )
            val spinnerDosageArrayAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
                context, R.layout.spinner_row, dosageArray
            )
            val spinnerUnitMedArrayAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
                context, R.layout.spinner_row, unitArray
            )
            val spinnerTimePeriodArrayAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
                context, R.layout.spinner_row, timeArray
            )
            mDialogView.quantitymeddrpDialog.adapter = spinnerQtyArrayAdapter
            mDialogView.dosagemeddrpDialog.adapter = spinnerDosageArrayAdapter
            mDialogView.unitdrpdwnnDialog.adapter = spinnerUnitMedArrayAdapter
            mDialogView.timeperioddrppDialog.adapter = spinnerTimePeriodArrayAdapter
        }
    }

    private fun registerTelephonyManager() {
        telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        mPhoneStateListener = object : PhoneStateListener() {
            @Deprecated("Deprecated in Java")
            override fun onSignalStrengthsChanged(signalStrength: SignalStrength) {
                super.onSignalStrengthsChanged(signalStrength)
                // Get signal strength values
                mSignalStrength = signalStrength.level
            }
        }

        // Register the PhoneStateListener to receive signal strength updates
        telephonyManager.listen(mPhoneStateListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS)

    }


    private fun saveBitmapToCustomDirectory(bitmap: Bitmap, directory: File) {
        lifecycleScope.launch(Dispatchers.IO) {

            // Create the directory if it doesn't exist
            if (!directory.exists()) {
                directory.mkdirs()
            }

            // Create a file to save the bitmap
            val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val fileName = "IMG_$timestamp.jpg"
            val file = File(directory, fileName)
            try {
                // Write the bitmap to the file
                val outputStream: OutputStream = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                outputStream.close()
                ContextCompat.startForegroundService(
                    this@VideoSessionActivity,
                    Intent(this@VideoSessionActivity, SnapUploadService::class.java)
                        .setAction(SnapUploadService.ACTION_ADD_FILES)
                        .putExtra(
                            SnapUploadService.APPOINTMENT_ID,
                            PatientAppointment.appointmentId
                        )
                        .putExtra(SnapUploadService.EXTRA_PATIENT_ID, PatientAppointment.patientId)
                        .putExtra(SnapUploadService.EXTRA_FILE_PATH_1, file.absolutePath)
                )

                Log.e(TAG, "saveBitmapToCustomDirectory: ${file.absolutePath}")
                // Notify the user that the bitmap has been saved
//                withContext(Dispatchers.Main)
//                {
//                    //TODO only for debug purpose
//                    Toast.makeText(
//                        context,
//                        "Snap saved to " + file.absolutePath,
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
            } catch (e: IOException) {
                e.printStackTrace()
                //TODO only for debug purpose
//                withContext(Dispatchers.Main)
//                {
//                    Toast.makeText(context, "Failed to save snap", Toast.LENGTH_SHORT).show()
//                }
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_CAPTURE_SCREEN) {
            if (resultCode == RESULT_OK && data != null) {
                startServiceIfNotRunning(resultCode, data)
            } else {
                customOnDestroy()
                finish()
            }
        }
    }

    private fun startServiceIfNotRunning(resultCode: Int, data: Intent) {
        if (!isServiceRunningInForeground(this, MediaProjectionService::class.java)) {
            ContextCompat.startForegroundService(
                this, Intent(this, MediaProjectionService::class.java)
            )
        }
        captureScreenContent(resultCode, data)
    }


    @SuppressLint("WrongConstant")
    private fun captureScreenContent(resultCode: Int, data: Intent) {
        if (this::startDelayHandler.isInitialized) {
            startDelayHandler.postDelayed({
                if (isServiceRunningInForeground(this, MediaProjectionService::class.java)) {
                    mMediaProjection = mMediaProjectionManager.getMediaProjection(resultCode, data)
                    if (mMediaProjection != null) {
//                        // Create VirtualDisplay to capture the screen content
                        val metrics = resources.displayMetrics
                        val density = metrics.densityDpi
                        if (mImageReader == null) {
                            mImageReader = ImageReader.newInstance(
                                metrics.widthPixels, metrics.heightPixels, PixelFormat.RGBA_8888, 1
                            )
                        }
                        val virtualDisplayFlags =
                            DisplayManager.VIRTUAL_DISPLAY_FLAG_OWN_CONTENT_ONLY or
                                    DisplayManager.VIRTUAL_DISPLAY_FLAG_PUBLIC
                        if (mVirtualDisplay == null && mImageReader != null) {
                            mVirtualDisplay = mMediaProjection?.createVirtualDisplay(
                                "ScreenCapture",
                                metrics.widthPixels,
                                metrics.heightPixels,
                                density,
                                virtualDisplayFlags,
                                mImageReader!!.surface,
                                null,
                                null
                            )
                        }

                        mImageReader?.setOnImageAvailableListener({ reader ->
                            // Get the latest captured image
                            if (isPatientVideoShown) {
                                startRealCapture(reader)
                            } else {
                                patientNotJoinHandler = Handler(Looper.getMainLooper())
                                patientNotJoinHandler5SecDelay = Handler(Looper.getMainLooper())
                                patientNotJoinHandler!!.postDelayed(object : Runnable {
                                    override fun run() {
                                        if (isPatientVideoShown) {
                                            patientNotJoinHandler5SecDelay!!.postDelayed({
                                                startRealCapture(reader)
                                            }, 5000)
                                        } else {
                                            patientNotJoinHandler?.postDelayed(this, 1000)
                                        }
                                    }
                                }, 1000)
                            }
                        }, null)
                    } else {
                        if (isServiceRunningInForeground(
                                this,
                                MediaProjectionService::class.java
                            )
                        ) {
                            stopService(Intent(this, MediaProjectionService::class.java))
                        }
                        customOnDestroy()
                        finish()
                        //TODO Only for debug
//                        toast("Snap permission denied")
                    }
                } else {
                    //TODO Only for debug
//                    toast("Service not running")
                }
            }, 5000)
        }
    }


    private fun imageToBitmap(image: Image): Bitmap? {
        val planes = image.planes
        val buffer = planes[0].buffer
        val pixelStride = planes[0].pixelStride
        val rowStride = planes[0].rowStride
        val rowPadding = rowStride - pixelStride * image.width
        // Create Bitmap with ARGB_8888 format
        val bitmap = Bitmap.createBitmap(
            image.width + rowPadding / pixelStride, image.height, Bitmap.Config.ARGB_8888
        )
        bitmap.copyPixelsFromBuffer(buffer)

        return bitmap
    }

    private fun isServiceRunningInForeground(context: Context, serviceClass: Class<*>): Boolean {
        val manager = context.getSystemService(ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return service.foreground
            }
        }
        return false
    }


    private fun startRealCapture(reader: ImageReader) {
        if (isFirstCapture) {
            try {
                val image: Image? = reader.acquireLatestImage()
                if (image != null) {
                    // Convert the image to bitmap
                    val bitmap: Bitmap? = imageToBitmap(image)
                    ivCaptured.setImageBitmap(bitmap)

                    // Save the bitmap to storage or do further processing
                    bitmap?.let {
                        saveBitmapToCustomDirectory(
                            it, storageDirectory
                        )
                    }
                    // Release the image
                    isFirstCapture = false
                }
                image?.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            recursiveHandler.postDelayed({
                val image: Image? = reader.acquireLatestImage()
                if (image != null) {
                    // Convert the image to bitmap
                    val bitmap: Bitmap? = imageToBitmap(image)
                    ivCaptured.setImageBitmap(bitmap)
                    // Save the bitmap to storage or do further processing
                    bitmap?.let {
                        saveBitmapToCustomDirectory(
                            it, storageDirectory
                        )
                    }
                    // Release the image
                }
                image?.close()
            }, 60000)
        }


    }


}

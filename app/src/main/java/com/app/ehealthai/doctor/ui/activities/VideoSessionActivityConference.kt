//package com.app.ehealthaidoctor.ui.activities
//
//import android.Manifest
//import android.app.DatePickerDialog
//import android.content.BroadcastReceiver
//import android.content.Context
//import android.content.DialogInterface
//import android.content.Intent
//import android.content.IntentFilter
//import android.content.pm.PackageManager
//import android.content.res.AssetManager
//import android.graphics.BitmapFactory
//import android.graphics.Color
//import android.net.ConnectivityManager
//import android.net.NetworkInfo
//import android.net.wifi.WifiInfo
//import android.net.wifi.WifiManager
//import android.os.Bundle
//import android.os.CountDownTimer
//import android.os.Handler
//import android.os.Looper
//import android.text.Editable
//import android.text.SpannableString
//import android.text.TextWatcher
//import android.text.style.ForegroundColorSpan
//import android.util.Log
//import android.view.Gravity
//import android.view.LayoutInflater
//import android.view.SurfaceView
//import android.view.View
//import android.widget.AdapterView
//import android.widget.ArrayAdapter
//import android.widget.FrameLayout
//import android.widget.LinearLayout
//import android.widget.ProgressBar
//import android.widget.TextView
//import android.widget.Toast
//import androidx.appcompat.app.AlertDialog
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.app.ActivityCompat
//import androidx.core.content.ContextCompat
//import androidx.localbroadcastmanager.content.LocalBroadcastManager
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.app.ehealthai.doctor.adapters.recyclerViewAdapters.IClinicalTestItemToDelete
//import com.app.ehealthai.doctor.adapters.recyclerViewAdapters.MedicinelistAdapter
//import com.app.ehealthaidoctor.adapters.recyclerViewAdapters.RemoteViewsAdapters
//import com.app.ehealthai.doctor.adapters.recyclerViewAdapters.dygTestAdapter
//import com.app.ehealthai.doctor.adapters.recyclerViewAdapters.dyglistAdapter
//import com.app.ehealthai.doctor.models.responses.AddDiagnosisResponse
//import com.app.ehealthai.doctor.models.responses.ClinicalTestsResponse
//import com.app.ehealthai.doctor.models.responses.DiagnosisList
//import com.app.ehealthai.doctor.models.responses.DoctorNotesResponse
//import com.app.ehealthai.doctor.models.responses.ForgotPasswordResponse
//import com.app.ehealthai.doctor.models.responses.MedicineList
//import com.app.ehealthai.doctor.models.responses.RoomDataResponse
//import com.app.ehealthai.doctor.rtm.ChatManager
//import com.app.ehealthai.doctor.rtm.status.MessageType
//import com.app.ehealthai.doctor.ui.activities.PatientDetails.PatientAppointment
//import com.app.ehealthai.doctor.ui.activities.PatientDetails.ui.dashboard.AddNotesFragment
//import com.app.ehealthai.doctor.ui.activities.VideoSessionMedicineHistory
//import com.app.ehealthai.doctor.ui.activities.VideoSessionPatientReports
//import com.app.ehealthai.doctor.ui.activities.VideoSessionVitalsHistory
//import com.app.ehealthai.doctor.utils.Global
//import com.app.ehealthaidoctor.R
//import com.app.ehealthaidoctor.models.responses.CloseAppointmentResponse
//import com.app.ehealthaidoctor.models.responses.Profile
//import com.app.ehealthaidoctor.models.responses.SendNotificationResponse
//import com.app.ehealthaidoctor.models.responses.UploadPatientFileResponse
//import com.app.ehealthaidoctor.network.ApiUtils
//import com.app.ehealthaidoctor.ui.activities.Chat.VideoSessionChat
//import com.app.ehealthaidoctor.utils.SharedPrefs
//import com.crashlytics.android.Crashlytics
//import com.example.ehealthai.MyApplication
//import com.example.ehealthai.utils.toast
//import com.google.android.material.snackbar.Snackbar
//import io.agora.rtc2.ChannelMediaOptions
//import io.agora.rtc2.Constants
//import io.agora.rtc2.IRtcEngineEventHandler
//import io.agora.rtc2.RtcEngine
//import io.agora.rtc2.RtcEngineConfig
//import io.agora.rtc2.video.SegmentationProperty
//import io.agora.rtc2.video.VideoCanvas
//import io.agora.rtc2.video.VirtualBackgroundSource
//import io.agora.rtm.ErrorInfo
//import io.agora.rtm.ResultCallback
//import io.agora.rtm.RtmClient
//import io.agora.rtm.RtmClientListener
//import io.agora.rtm.RtmMessage
//import io.agora.rtm.RtmStatusCode
//import io.fabric.sdk.android.Fabric
//import kotlinx.android.synthetic.main.activity_custom_video_session.cl_custom_video_session
//import kotlinx.android.synthetic.main.activity_custom_video_session.local_video_view_container
//import kotlinx.android.synthetic.main.activity_custom_video_session.rv_remote_views
//import kotlinx.android.synthetic.main.activity_video_session.addnotes
//import kotlinx.android.synthetic.main.activity_video_session.coordinatorLayout
//import kotlinx.android.synthetic.main.activity_video_session.count
//import kotlinx.android.synthetic.main.activity_video_session.duration
//import kotlinx.android.synthetic.main.activity_video_session.endCallBtn
//import kotlinx.android.synthetic.main.activity_video_session.more
//import kotlinx.android.synthetic.main.activity_video_session.moreic
//import kotlinx.android.synthetic.main.activity_video_session.muteBtn
//import kotlinx.android.synthetic.main.activity_video_session.signalstregnth
//import kotlinx.android.synthetic.main.activity_video_session.statusc_text
//import kotlinx.android.synthetic.main.activity_video_session.switch_btn
//import kotlinx.android.synthetic.main.activity_video_session.video_btn
//import kotlinx.android.synthetic.main.addnotesdialog.view.addctestsbtn
//import kotlinx.android.synthetic.main.addnotesdialog.view.adddiagnosisbtn
//import kotlinx.android.synthetic.main.addnotesdialog.view.addmedicinebtn
//import kotlinx.android.synthetic.main.addnotesdialog.view.btn_cancel
//import kotlinx.android.synthetic.main.addnotesdialog.view.btn_okay
//import kotlinx.android.synthetic.main.addnotesdialog.view.ctests_drp
//import kotlinx.android.synthetic.main.addnotesdialog.view.diagnosis_drp
//import kotlinx.android.synthetic.main.addnotesdialog.view.doctor_notes_text
//import kotlinx.android.synthetic.main.addnotesdialog.view.dosagemeddrp
//import kotlinx.android.synthetic.main.addnotesdialog.view.dygnnosislistt
//import kotlinx.android.synthetic.main.addnotesdialog.view.etOtherDiagnosis
//import kotlinx.android.synthetic.main.addnotesdialog.view.followupchkbox
//import kotlinx.android.synthetic.main.addnotesdialog.view.followupdate
//import kotlinx.android.synthetic.main.addnotesdialog.view.listView
//import kotlinx.android.synthetic.main.addnotesdialog.view.medicines_drp
//import kotlinx.android.synthetic.main.addnotesdialog.view.medicinesdropdowns
//import kotlinx.android.synthetic.main.addnotesdialog.view.progress_1
//import kotlinx.android.synthetic.main.addnotesdialog.view.quantitymeddrp
//import kotlinx.android.synthetic.main.addnotesdialog.view.rv_clinicalTests
//import kotlinx.android.synthetic.main.addnotesdialog.view.timeperioddrpp
//import kotlinx.android.synthetic.main.addnotesdialog.view.unitdrpdwnn
//import okhttp3.MediaType.Companion.toMediaTypeOrNull
//import okhttp3.RequestBody
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
//import java.io.File
//import java.io.FileOutputStream
//import java.io.IOException
//import java.util.Calendar
//import kotlin.collections.set
//
//
//class VideoSessionActivityConference : AppCompatActivity() {
//    companion object {
//        val PATIENT_ID = "patient_id"
//        val DOCTOR_ID = "doctor_id"
//        val TIME_OUT = 3001L
//        var mednamelist: ArrayList<String> = ArrayList()
//        var meddosagelist: ArrayList<String> = ArrayList()
//        var medquantitylist: ArrayList<String> = ArrayList()
//        var medUnitlist: ArrayList<String> = ArrayList()
//        var medTimePeriodListlist: ArrayList<String> = ArrayList()
//        var diagnosis: ArrayList<String> = ArrayList()
//    }
//
//    var diagnosisnameList: ArrayList<String> = java.util.ArrayList()
//    var diagnosisidList: ArrayList<String> = java.util.ArrayList()
//    var testsnameList: ArrayList<String> = java.util.ArrayList()
//    var testsnameIdList: ArrayList<String> = java.util.ArrayList()
//
//    lateinit var list: ArrayList<String>
//    var testlist: ArrayList<String>? = null
//    var selecteddiagnosis = ""
//    var dygadapter: dyglistAdapter? = null
//    var dygTestsadapter: dygTestAdapter? = null
//    var rvDyg: RecyclerView? = null
//    var selectedfollowupdate = ""
//    var notesupdated = false
//    var callstarttime: String = ""
//    var callendtime: String = ""
//    var callduration: String = ""
//    private var mRtmClient: RtmClient? = null
//    private var mChatManager: ChatManager? = null
//    private var isRTMLogin = false
//    private val mTimeSentLastMsg = HashMap<String, Long>()
//    var roomId: String = ""
//    var patientId: String = ""
//    var patientName: String = ""
//    var doctorId: String = ""
//    private var mRtmListener: MyRtmClientListener? = null
//    var notringing = false
//    var minutes: Int = 0
//    var seconds: Int = 0
//    var medicinestring: String = ""
//    var medicinenameList: ArrayList<String> = ArrayList()
//    var medicineidList: ArrayList<String> = ArrayList()
//    var appointmentId = ""
//    var docnotes = ""
//
//    var progress_1: ProgressBar? = null
//
//    // Permissions
//    private val PERMISSION_REQ_ID = 22
//    private val REQUESTED_PERMISSIONS =
//        arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA)
//
//    var otherpatyjoined = false
//    var otherpatyleftafterjoin = false
//
//    //    var agView: AgoraVideoViewer? = null
////    var segprop = SegmentationProperty()
//    var mAlertDialog: AlertDialog? = null
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_custom_video_session)
//        Global.isfromVideoSessionActivity = true
////        progress_1 = findViewById(R.id.progress_1)
//        Fabric.with(this, Crashlytics())
////        rvDyg = findViewById(R.id.dygnnosislistt)
//        list = ArrayList()
//        testlist = ArrayList()
//        callstarttime = Calendar.getInstance().time.toString()
//
//        //initializing agora video viewer for virtual bg
//
////        this.agView = AgoraVideoViewer(this, AgoraConnectionData(appId = "1634986669fd433c903f131af715ba6b"))
//
////        val frameLayout: ConstraintLayout.LayoutParams = ConstraintLayout.LayoutParams(
////            ConstraintLayout.LayoutParams.MATCH_PARENT,
////            ConstraintLayout.LayoutParams.MATCH_PARENT,
////        )
////
////        ccforvirtualbbg.addView(agView, frameLayout)
//        customOnCreate()
//        if (intent != null) {
//            if (intent.hasExtra("appointmentClosedFromAddNotes")) {
//                customOnDestroy()
////                agView?.agkit?.stopPreview()
////                agView?.agkit?.leaveChannel()
////                agView = null
//////                Globbal.callStatus = ""
//                finish()
//                val intent = Intent(this@VideoSessionActivityConference, HomeActivity::class.java)
//                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
//                startActivity(intent)
//            } else if (intent.hasExtra("appointmentCancelledFromAddNotes")) {
//                customOnDestroy()
////                agView?.agkit?.stopPreview()
////                agView?.agkit?.leaveChannel()
////                agView = null
////                Globbal.callStatus = ""
//                finish()
//                val intent = Intent(
//                    this@VideoSessionActivityConference,
//                    AppointmentsActivity::class.java
//                )
//                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
//                startActivity(intent)
//            } else {
//                try {
//                    val userData = SharedPrefs.getUserData(this@VideoSessionActivityConference)
//                    val username = userData!!.username
//                    val usertype = "DOCTOR"
//                    try {
//                        val url: String = intent.data.toString()
//                        Log.e("Url ", url)
//                        val arr = url.split("?").toTypedArray()
//                        roomId = arr[1].filter { it.isDigit() }
//                        channelName = roomId
//                        uid = Integer.parseInt(roomId + "" + "")
//                    } catch (e: java.lang.Exception) {
//                        val url: String = getIntent().getStringExtra("url")!!
//                        Log.e("Url ", url);
//                        val arr = url.split("?").toTypedArray()
//                        roomId = arr[1].filter { it.isDigit() }
//                        channelName = roomId
//                        uid = Integer.parseInt(roomId + "" + "")
//
//                    }
//                    getRoomData(username, usertype)
//                } catch (e: java.lang.Exception) {
//                    toast("Session Expired")
//                    finish()
//                }
//            }
//        }
//
//        LocalBroadcastManager.getInstance(this).registerReceiver(
//            aLBReceiver, IntentFilter("callEnd")
//        )
//
//
//    }
//
//    override fun onResume() {
//        LocalBroadcastManager.getInstance(this).registerReceiver(
//            aLBReceiver, IntentFilter("callEnd")
//        )
//        super.onResume()
//    }
//
//    private fun getRoomData(username: String?, usertype: String) {
//        ApiUtils.getAPIService(this@VideoSessionActivityConference).get_room_data(
//            SharedPrefs.getString(this@VideoSessionActivityConference, "sessionid")!!,
//            username!!,
//            usertype,
//            roomId
//        ).enqueue(object : Callback<RoomDataResponse> {
//            override fun onFailure(call: Call<RoomDataResponse>, t: Throwable) {
//            }
//
//            override fun onResponse(
//                call: Call<RoomDataResponse>, response: Response<RoomDataResponse>
//            ) {
//
//                try {
//                    patientId = response.body()!!.data.list.patient_id.toString()
//                    doctorId = response.body()!!.data.list.doc_id.toString()
//                    appointmentId = response.body()!!.data.list.apptid.toString()
//                    patientName = response.body()!!.data.list.patient_name.toString()
//                    val doctorData = SharedPrefs.getUserData(this@VideoSessionActivityConference)
//                    val sessionid = SharedPrefs.getString(this@VideoSessionActivityConference, "sessionid")
//
//                    getDoctorAppt(sessionid, username, usertype)
//
//                    video_btn.isSelected = true
//                    muteBtn.isSelected = true
//                    init()
//                    if (!isRTMLogin && doctorId != null && patientId != null) {
//                        realTimeMessaging()
//                    }
//                    sendNotification(
//                        patientId,
//                        doctorData!!.fname + " " + doctorData.lname + " is calling..." + "," + doctorId
//                    )
////                    joinChannel()
//                    customJoinChannel()
//                } catch (e: java.lang.Exception) {
//                    toast("Session Failed")
//                    Log.i("Session Failed exception", e.toString())
//                    finish()
//                }
//            }
//
//        })
//
//    }
//
//    private fun getDoctorAppt(sessionid: String?, username: String, usertype: String) {
//        ApiUtils.getAPIService(this@VideoSessionActivityConference).get_doctor_documents_appt(
//            sessionid!!, username, usertype, appointmentId
//        ).enqueue(object : Callback<DoctorNotesResponse> {
//            override fun onFailure(
//                call: Call<DoctorNotesResponse>, t: Throwable
//            ) {
//                //Do nothing
//            }
//
//            override fun onResponse(
//                call: Call<DoctorNotesResponse>, response: Response<DoctorNotesResponse>
//            ) {
//                //Do nothing
//                try {
//                    if (response.body() != null && response.body()?.data != null && response.body()!!.data!!.documents[0]!=null && response.body()!!.data!!.documents[0].doctor_notes!=null)
//                        docnotes = response.body()!!.data!!.documents[0].doctor_notes
//                } catch (e: java.lang.Exception) {
//                    e.printStackTrace()
//                }
//            }
//        })
//    }
//
//    private val aLBReceiver = object : BroadcastReceiver() {
//        override fun onReceive(context: Context, intent: Intent) {
//
//            if (intent.getStringExtra("end").equals("ringing")) {
//                //ringing.text = "RINGING"
//                statusc_text.text = "RINGING..."
//            } else if (intent.getStringExtra("end").equals("call")) {
//                //ringing.text = "Call Ended"
//                statusc_text.text = "Call Ended"
//                val doctorData = SharedPrefs.getUserData(this@VideoSessionActivityConference)
//                toast("Call Ended")
//
//                sendNotification(
//                    patientId,
//                    doctorData!!.fname + " " + doctorData.lname + " call Ending..." + "," + doctorId
//                )
//            }
//
//        }
//    }
//
//    override fun onDestroy() {
//        if (mAlertDialog != null) {
//            mAlertDialog!!.dismiss()
//            mAlertDialog = null
//        }
//        LocalBroadcastManager.getInstance(this@VideoSessionActivityConference).unregisterReceiver(aLBReceiver)
//        if (remoteViewsAdapter?.returnListItems() != null) {
////            MyApplication.remoteViewsList.clear()
////            MyApplication.remoteViewsList.addAll(remoteViewsAdapter?.returnListItems()!!)
//            Log.e(
//                TAG,
//                "onDestroy: remoteViewsAdapter?.returnListItems() ${remoteViewsAdapter?.returnListItems()}"
//            )
//            Log.e(
//                TAG,
//                "onDestroy: MyApplication.remoteViewsList ${MyApplication.remoteViewsList.size}"
//            )
//        }
////        remoteViewsAdapter?.removeAllRemoteRemoteView()
//
////        finish()
//        super.onDestroy()
//    }
//
//    override fun onRequestPermissionsResult(
//        requestCode: Int, permissions: Array<String>, grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        Log.i("TAG", "onRequestPermissionsResult " + grantResults[0] + " " + requestCode)
//
//        when (requestCode) {
//            PERMISSION_REQ_ID -> {
//                if (grantResults[0] != PackageManager.PERMISSION_GRANTED && grantResults[1] != PackageManager.PERMISSION_GRANTED) {
//                    Log.i(
//                        "TAG",
//                        "Need permissions " + Manifest.permission.RECORD_AUDIO + "/" + Manifest.permission.CAMERA
//                    )
//                } else {
//                    // if permission granted, initialize the engine
//
//                    val doctorData = SharedPrefs.getUserData(this@VideoSessionActivityConference)
//
//                    sendNotification(
//                        patientId,
//                        doctorData!!.fname + " " + doctorData.lname + " is calling..." + "," + doctorId
//                    )
//                    joinChannel()
//                    customJoinChannel()
//                }
//            }
//        }
//    }
//
//    override fun onStop() {
//        super.onStop()
//        mChatManager?.unregisterListener(mRtmListener)
//    }
//
//    fun checkSelfPermission(permission: String, requestCode: Int): Boolean {
//        if (ContextCompat.checkSelfPermission(
//                this, permission
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//
//            ActivityCompat.requestPermissions(
//                this, REQUESTED_PERMISSIONS, requestCode
//            )
//            return false
//        }
//        return true
//    }
//
//    internal fun init() {
//
//        if (checkSelfPermission(REQUESTED_PERMISSIONS[0], PERMISSION_REQ_ID) && checkSelfPermission(
//                REQUESTED_PERMISSIONS[1], PERMISSION_REQ_ID
//            )
//        ) {
//            initializeAgoraEngine()
//            //  joinChannel()
//        }
//        val doctorData = SharedPrefs.getUserData(this@VideoSessionActivityConference)
//        initButtonsClick(doctorData)
//
//        val timer = object : CountDownTimer(1800000, 1000) {
//            override fun onTick(millisUntilFinished: Long) {
//
//                var minutestodisplay = ""
//                var secondstodisplay = ""
//                seconds++
//
//                if (seconds.toInt() % 60 == 0) {
//                    minutes++
//                    seconds = 0
//                }
//                if (minutes < 10) {
//                    minutestodisplay = "0" + minutes.toString()
//                } else {
//                    minutestodisplay = minutes.toString()
//                }
//                if (seconds < 10) {
//                    secondstodisplay = "0" + seconds.toString()
//                } else {
//                    secondstodisplay = seconds.toString()
//                }
//                duration.text = "$minutestodisplay:$secondstodisplay"
//                if (minutes == 10 && seconds == 0) {
//                    Snackbar.make(
//                        cl_custom_video_session, "5 minutes left in your scheduled session", 8000
//                    ).setAction("Close") { }.setActionTextColor(Color.RED)
//                        .show()
//                }
//            }
//
//            override fun onFinish() {}
//        }
//        timer.start()
//    }
//
//    private fun initializeAgoraEngine() {
//        val mainHandler = Handler(Looper.getMainLooper())
//        mainHandler.post(object : Runnable {
//            override fun run() {
////                if (Globbal.callStatus != null) {
////                    if (Globbal.callStatus.equals("onFirstRemoteVideoDecoded")) {
////                        otherpatyjoined = true
////                        otherpatyleftafterjoin = false
////                        var spannableString: SpannableString =
////                            SpannableString(patientName + " joined")
////                        spannableString.setSpan(
////                            ForegroundColorSpan(getResources().getColor(android.R.color.white)),
////                            0,
////                            spannableString.length,
////                            0
////                        )
////                        statusc_text.visibility = View.VISIBLE
////                        statusc_text.text = patientName + " joined"
////                    }
////                    else if (Globbal.callStatus.equals("onUserJoined")) {
////
////                        statusc_text.visibility = View.VISIBLE
////                        statusc_text.text = "Connected"
////
////                    }
////                    else if (Globbal.callStatus.equals("onUserOffline")) {
////                        runOnUiThread {
////                            otherpatyjoined = false;
////                            otherpatyleftafterjoin = true
////                            var spannableString: SpannableString =
////                                SpannableString(patientName + " Left")
////                            spannableString.setSpan(
////                                ForegroundColorSpan(getResources().getColor(android.R.color.white)),
////                                0,
////                                spannableString.length,
////                                0
////                            )
////                            endCallBtn.performClick()
//////                            Globbal.callStatus = ""
////
////                        }
////                    }
////                    else if (Globbal.callStatus.equals("onFirstRemoteVideoDecoded")) {
////                        //* runOnUiThread { setupRemoteVideo(uid) }*//*
////                        otherpatyjoined = true;
////                        otherpatyleftafterjoin = false;
////                        var spannableString: SpannableString =
////                            SpannableString(patientName + " joined")
////                        spannableString.setSpan(
////                            ForegroundColorSpan(getResources().getColor(android.R.color.white)),
////                            0,
////                            spannableString.length,
////                            0
////                        );
////
////                        statusc_text.visibility = View.VISIBLE
////                        statusc_text.text = patientName + " joined"
////                    }
////
////                }
//                try {
//                    val notifcount = SharedPrefs.getInt(applicationContext, "notifavailable")
//                    if (notifcount > 0) {
//                        // count.setText("New Message")
//                        count.setText(notifcount)
//                        count.visibility = View.VISIBLE
//                    }
//                } catch (e: java.lang.Exception) {
//                    e.printStackTrace()
//                }
//
//                manageSignals()
//
//                try {
//                    if (!otherpatyjoined && !otherpatyleftafterjoin) {
//                        statusc_text.visibility = View.VISIBLE
//                        statusc_text.text = "Waiting for " + patientName + "..."
////                        val container = findViewById<FrameLayout>(R.id.friend_frame_layout)
//
//                    } else if (otherpatyleftafterjoin) {
//                        statusc_text.visibility = View.VISIBLE
//                        statusc_text.text = patientName + " Left"
////                        val container = findViewById<FrameLayout>(R.id.friend_frame_layout)
//                    }
//
//                    if (agoraEngine?.connectionState.toString() == "1") {
//                        statusc_text.visibility = View.VISIBLE
//                        statusc_text.text = "Disconnected"
//                    }
//                    if (agoraEngine?.connectionState.toString() == "2") {
//                        statusc_text.visibility = View.VISIBLE
//                        statusc_text.text = "Connecting..."
//                    }
//                    if (agoraEngine?.connectionState.toString() == "3") {
//                        statusc_text.visibility = View.VISIBLE
//                        statusc_text.text = "Connected"
//
//                    }
//                    if (agoraEngine?.connectionState.toString() == "4") {
//                        statusc_text.visibility = View.VISIBLE
//                        statusc_text.text = "Reconnecting..."
//                    }
//                    if (agoraEngine?.connectionState.toString() == "5") {
//                        statusc_text.visibility = View.VISIBLE
//                        statusc_text.text = "Call Failed"
//                    }
//
//                } catch (e: java.lang.Exception) {
//                    Toast.makeText(
//                        this@VideoSessionActivityConference,
//                        "connectionState $e",
//                        Toast.LENGTH_LONG
//                    ).show()
//                }
//
//                mainHandler.postDelayed(this, 1000)
//            }
//        })
//    }
//
//    private fun initButtonsClick(doctorData: Profile?) {
//        switch_btn.setOnClickListener {
//            agoraEngine?.switchCamera()
//        }
//        endCallBtn.setOnClickListener { v ->
//            sendNotification(
//                patientId,
//                doctorData!!.fname + " " + doctorData.lname + " call Ending..." + "," + doctorId
//            )
//            val userData = SharedPrefs.getUserData(this@VideoSessionActivityConference)
//            val username = userData!!.username
//            val usertype = "DOCTOR"
//
//            if (notesupdated) {
//                notesupdated = false
//                val dialogBuilder = AlertDialog.Builder(this@VideoSessionActivityConference)
//                dialogBuilder.setMessage("Please close your appointment if you are done with the session.ThankYou")
//                    .setCancelable(false)
//                    .setPositiveButton("Close Now", DialogInterface.OnClickListener { dialog, id ->
//                        try {
//                            callendtime = Calendar.getInstance().time.toString()
//                            callduration = duration.text.toString()
//                            ApiUtils.getAPIService(this@VideoSessionActivityConference)
//                                .callstatuswithextraparams(
//                                    SharedPrefs.getString(this@VideoSessionActivityConference, "sessionid")!!,
//                                    username!!,
//                                    usertype,
//                                    PatientAppointment.appointmentId.toString(),
//                                    "Doctor has Ended Video Session",
//                                    callstarttime,
//                                    callendtime,
//                                    callduration
//                                ).enqueue(object : Callback<ForgotPasswordResponse> {
//                                    override fun onFailure(
//                                        call: Call<ForgotPasswordResponse>, t: Throwable
//                                    ) {
//                                        //Do nothing
//                                    }
//
//                                    override fun onResponse(
//                                        call: Call<ForgotPasswordResponse>,
//                                        response: Response<ForgotPasswordResponse>
//                                    ) {
//                                        val userData =
//                                            SharedPrefs.getUserData(this@VideoSessionActivityConference)
//                                        val username = userData!!.username
//                                        val usertype = "DOCTOR"
//                                        ApiUtils.getAPIService(this@VideoSessionActivityConference)
//                                            .closeAppointmentRequest(
//                                                PatientAppointment.appointmentId!!,
//                                                SharedPrefs.getString(
//                                                    this@VideoSessionActivityConference, "sessionid"
//                                                )!!,
//                                                username!!,
//                                                usertype
//                                            ).enqueue(object : Callback<CloseAppointmentResponse> {
//                                                override fun onFailure(
//                                                    call: Call<CloseAppointmentResponse>,
//                                                    t: Throwable
//                                                ) {
//                                                    //   mDialogView. progress_1.visibility = View.GONE
//                                                    Toast.makeText(
//                                                        this@VideoSessionActivityConference,
//                                                        "Something went wrong, please try again later",
//                                                        Toast.LENGTH_LONG
//                                                    ).show()
//                                                }
//
//                                                override fun onResponse(
//                                                    call: Call<CloseAppointmentResponse>,
//                                                    response: Response<CloseAppointmentResponse>
//                                                ) {
//                                                    PatientAppointment.appointmentClosed = true
//                                                    PatientAppointment.clearAppointmentNotes()
//                                                    Toast.makeText(
//                                                        this@VideoSessionActivityConference,
//                                                        "Appointment closed !",
//                                                        Toast.LENGTH_LONG
//                                                    ).show()
//
//                                                    AlertDialog.Builder(this@VideoSessionActivityConference)
//                                                        .setTitle(
//                                                            this@VideoSessionActivityConference.resources.getString(
//                                                                R.string.success_dialog_title
//                                                            )
//                                                        ).setMessage(
//                                                            this@VideoSessionActivityConference.resources.getString(
//                                                                R.string.success_dialog_message_appt
//                                                            )
//                                                        )
//                                                        .setPositiveButton(
//                                                            this@VideoSessionActivityConference.resources.getString(
//                                                                R.string.success_dialog_confirm
//                                                            )
//                                                        ) { dialog, _ ->
//                                                            //agView?.agkit?.stopPreview()
//                                                            //agView?.agkit?.leaveChannel()
//                                                            //agView = null
//                                                            customOnDestroy()
//                                                            finish()
//                                                            val intent = Intent(
//                                                                this@VideoSessionActivityConference,
//                                                                HomeActivity::class.java
//                                                            )
//                                                            intent.flags =
//                                                                Intent.FLAG_ACTIVITY_CLEAR_TOP
//                                                            startActivity(intent)
//                                                            dialog?.dismiss()
//                                                        }.show()
//                                                }
//                                            })
//                                    }
//                                })
//                        } catch (e: Exception) {
//                            e.printStackTrace()
//                        }
//
//                    })
//
//                    .setNegativeButton("Close Later",
//                        DialogInterface.OnClickListener { dialog, id ->
//                            try {
//                                callendtime = Calendar.getInstance().time.toString()
//                                callduration = duration.text.toString()
//                                ApiUtils.getAPIService(this@VideoSessionActivityConference)
//                                    .callstatuswithextraparams(
//                                        SharedPrefs.getString(
//                                            this@VideoSessionActivityConference, "sessionid"
//                                        )!!,
//                                        username!!,
//                                        usertype,
//                                        PatientAppointment.appointmentId.toString(),
//                                        "Doctor has Ended Video Session",
//                                        callstarttime,
//                                        callendtime,
//                                        callduration
//                                    ).enqueue(object : Callback<ForgotPasswordResponse> {
//                                        override fun onFailure(
//                                            call: Call<ForgotPasswordResponse>, t: Throwable
//                                        ) {
//                                            //Do nothing
//
//                                        }
//
//                                        override fun onResponse(
//                                            call: Call<ForgotPasswordResponse>,
//                                            response: Response<ForgotPasswordResponse>
//                                        ) {
//                                            customOnDestroy()
////                                            agView?.agkit?.stopPreview()
////                                            agView?.agkit?.leaveChannel()
////                                            agView = null
////                                            Globbal.callStatus = ""
//                                            finish()
//                                        }
//                                    })
//                            } catch (e: Exception) {
//                                e.printStackTrace()
//                            }
//                            //leaveChannel()
//                        })
//                // create dialog box
//                val alert = dialogBuilder.create()
//                // set title for alert dialog box
//                alert.setTitle("Session Ended")
//                // show alert dialog
//                alert.show()
//            } else {
//                PatientAppointment.appointmentClosed = true
//                PatientAppointment.clearAppointmentNotes()
//                notesupdated = false
//                customOnDestroy()
//
////                agView?.agkit?.stopPreview()
////                agView?.agkit?.leaveChannel()
////                this.agView = null
////                Globbal.callStatus = ""
//                finish()
//            }
//        }
//        muteBtn.setOnClickListener {
//
//            if (muteBtn.isSelected) {
//                agoraEngine?.muteLocalAudioStream(true)
//                sendStatusMessage(MessageType.MUTE_MIC)
//                Handler(Looper.getMainLooper()).postDelayed(
//                    { sendStopStatusMsg(MessageType.MUTE_MIC) }, TIME_OUT
//                )
//                muteBtn.isSelected = false
//                muteBtn.setImageResource(R.drawable.nomic)
//
//            } else {
//                agoraEngine?.muteLocalAudioStream(false)
//                sendStatusMessage(MessageType.MIC_ON)
//                Handler(Looper.getMainLooper()).postDelayed(
//                    { sendStopStatusMsg(MessageType.MIC_ON) }, TIME_OUT
//                )
//                muteBtn.isSelected = true
//                muteBtn.setImageResource(R.drawable.mic)
//
//            }
//
//        }
//        more.setOnClickListener {
//            Global.IntentFromHandler = false
//            sendStatusMessage(MessageType.OPENING_MSGS)
//            Handler(Looper.getMainLooper()).postDelayed({
//                sendStopStatusMsg(MessageType.OPENING_MSGS)
//            }, TIME_OUT)
//            count.visibility = View.GONE
//            val intent = Intent(this, VideoSessionChat::class.java)
//            intent.putExtra("patientId", patientId)
//            intent.putExtra("patientName", patientName)
//            intent.putExtra("image_name_p", "")
//            startActivity(intent)
//
//        }
//        moreic.setOnClickListener {
//            val builder = AlertDialog.Builder(this)
//            builder.setTitle("More Options")
//
//            val moreoptions = arrayOf("Vitals History", "Medicine History", "Patient Reports")
//            builder.setItems(
//                moreoptions
//            ) { dialog, which ->
//                when (which) {
//                    0 -> {
//                        val intent = Intent(this, VideoSessionVitalsHistory::class.java)
//                        intent.putExtra("patientId", patientId)
//                        intent.putExtra("patientName", patientName)
//                        intent.putExtra("image_name_p", "")
//                        startActivity(intent)
//                    }
//
//                    1 -> {
//                        val intent = Intent(this, VideoSessionMedicineHistory::class.java)
//                        intent.putExtra("patientId", patientId)
//                        intent.putExtra("patientName", patientName)
//                        intent.putExtra("image_name_p", "")
//                        startActivity(intent)
//                    }
//
//                    2 -> {
//                        val intent = Intent(this, VideoSessionPatientReports::class.java)
//                        intent.putExtra("patientId", patientId)
//                        intent.putExtra("patientName", patientName)
//                        intent.putExtra("image_name_p", "")
//                        startActivity(intent)
//                    }
//                }
//            }
//            val dialog = builder.create()
//            dialog.show()
//        }
//        addnotes.setOnClickListener {
//            showMessage("Opening notes please wait")
//            mednamelist.clear()
//            val sessionid = SharedPrefs.getString(this@VideoSessionActivityConference, "sessionid")
//            val userData = SharedPrefs.getUserData(this@VideoSessionActivityConference)
//            val username = userData!!.username
//            val usertype = "DOCTOR"
//            diagnosisnameList = java.util.ArrayList()
//            diagnosisidList = java.util.ArrayList()
//
//            val mDialogView = LayoutInflater.from(this).inflate(R.layout.addnotesdialog, null)
//            //AlertDialogBuilder
//            val mBuilder = AlertDialog.Builder(this).setView(mDialogView).setTitle("Add Notes")
//            //show dialog
//            mAlertDialog = mBuilder.show()
//            mAlertDialog?.setOnDismissListener {
//                mAlertDialog = null
//
//            }
//            mAlertDialog?.setOnCancelListener {
//                mAlertDialog = null
//            }
//            try {
//                mDialogView.listView.isNestedScrollingEnabled = true
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//
//            AddNotesFragment.adapter = MedicinelistAdapter(
//                this,
//                mednamelist,
//                medquantitylist,
//                meddosagelist,
//                medTimePeriodListlist,
//                medUnitlist
//            )
//            mDialogView.listView.adapter = AddNotesFragment.adapter
//            mDialogView.followupchkbox.setOnCheckedChangeListener { _, isChecked ->
//                if (isChecked) {
//                    PatientAppointment.isFollowUpChecked = isChecked
//                    val datePickerListener =
//                        DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
//                            val dateYouChoosed =
//                                year.toString() + "-" + (monthOfYear + 1) + "-" + dayOfMonth.toString()
//                            mDialogView?.followupdate?.text = dateYouChoosed
//                            selectedfollowupdate = dateYouChoosed
//                            PatientAppointment.followUpDate = selectedfollowupdate
//                        }
//                    val c = Calendar.getInstance()
//                    val mYear = c[Calendar.YEAR]
//                    val mMonth = c[Calendar.MONTH]
//                    val mDay = c[Calendar.DAY_OF_MONTH]
//                    val dateDialog = DatePickerDialog(this, datePickerListener, mYear, mMonth, mDay)
//                    dateDialog.datePicker.minDate = System.currentTimeMillis() - 1000;
//                    dateDialog.show()
//
//                } else {
//                    PatientAppointment.isFollowUpChecked = isChecked
//                    mDialogView?.followupdate?.setText("")
//                    selectedfollowupdate = ""
//                }
//            }
//            //login button click of custom layout
//            ApiUtils.getAPIService(this@VideoSessionActivityConference)
//                .get_doctor_documents_appt(sessionid!!, username!!, usertype, appointmentId)
//                .enqueue(object : Callback<DoctorNotesResponse> {
//                    override fun onFailure(call: Call<DoctorNotesResponse>, t: Throwable) {
//                        //Do nothing
//                    }
//
//                    override fun onResponse(
//                        call: Call<DoctorNotesResponse>, response: Response<DoctorNotesResponse>
//                    ) {
//                        //Do nothing
//                        try {
//                            docnotes = response.body()!!.data!!.documents[0].doctor_notes
//                            mDialogView.doctor_notes_text.setText(docnotes)
//                        } catch (e: java.lang.Exception) {
//                            e.printStackTrace()
//                        }
//                    }
//                })
//
//            mDialogView.diagnosis_drp.isEnabled = false
//
//
//            ApiUtils.getAPIService(this@VideoSessionActivityConference).get_diagnosis_list(
//                SharedPrefs.getString(this@VideoSessionActivityConference, "sessionid")!!,
//                username,
//                usertype
//            ).enqueue(object : Callback<DiagnosisList> {
//                override fun onFailure(call: Call<DiagnosisList>, t: Throwable) {
//
//                    toast("Something went wrong, please try again later")
//                }
//
//                override fun onResponse(
//                    call: Call<DiagnosisList>, response: Response<DiagnosisList>
//                ) {
//                    try {
//                        if (response.isSuccessful && response.body() != null && response.body()!!.statuscode == 200) {
//                            try {
//
//                                diagnosis = ArrayList()
//                                val diagnosisAdapter: ArrayAdapter<String>?
//                                medicinenameList.clear()
//                                medicineidList.clear()
//
//                                for (diag in response.body()!!.data!!.data!!) {
//                                    diagnosis.add(diag.diagnosis_name!!)
//                                    diagnosisnameList.add(diag.diagnosis_name)
//                                    diagnosisidList.add(diag.id.toString())
//                                }
//
//                                diagnosisAdapter = ArrayAdapter(
//                                    this@VideoSessionActivityConference,
//                                    android.R.layout.simple_spinner_item,
//                                    diagnosis
//                                )
//                                diagnosisAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//
//                                mDialogView?.diagnosis_drp?.isEnabled = true
//                                mDialogView?.diagnosis_drp?.adapter = diagnosisAdapter
//                                mDialogView?.diagnosis_drp?.onItemSelectedListener =
//                                    object : AdapterView.OnItemSelectedListener {
//                                        override fun onItemSelected(
//                                            parent: AdapterView<*>,
//                                            view: View,
//                                            position: Int,
//                                            id: Long
//                                        ) {
//                                            if (diagnosis[position].equals("Others")) {
//                                                mDialogView?.etOtherDiagnosis?.visibility =
//                                                    View.VISIBLE
//                                            } else {
//                                                mDialogView?.etOtherDiagnosis?.visibility =
//                                                    View.GONE
//                                            }
//                                        }
//
//                                        override fun onNothingSelected(parent: AdapterView<*>) {
//                                            // write code to perform some action
//                                        }
//                                    }
//
//                                mDialogView.adddiagnosisbtn.isClickable = true
//                                mDialogView.adddiagnosisbtn.visibility = View.VISIBLE
//
//                            } catch (e: java.lang.Exception) {
//                                e.printStackTrace()
//                            }
//                        } else {
//                            Toast.makeText(
//                                this@VideoSessionActivityConference,
//                                "Error: Diagnosis list failed to load",
//                                Toast.LENGTH_LONG
//                            ).show()
//
//                        }
//                    } catch (e: Exception) {
//                        Toast.makeText(
//                            this@VideoSessionActivityConference,
//                            "Error: Diagnosis list failed to load",
//                            Toast.LENGTH_LONG
//                        ).show()
//                        try {
//
//                        } catch (e: java.lang.Exception) {
//                        }
//
//                    }
//                    mDialogView.adddiagnosisbtn.setOnClickListener {
//                        val layoutManager = LinearLayoutManager(
//                            this@VideoSessionActivityConference, LinearLayoutManager.HORIZONTAL, false
//                        )
//                        mDialogView.dygnnosislistt.layoutManager = layoutManager
//                        selecteddiagnosis = mDialogView.diagnosis_drp.selectedItem.toString()
//                        if (list.size > 0) {
//                            if (!list.contains(selecteddiagnosis)) {
////                                list.add(selecteddiagnosis)
//                                var index = diagnosisnameList.indexOf(selecteddiagnosis)
//                                Log.i("listasize", list?.size.toString())
//                                var selectedid = diagnosisidList[index].toString()
//                                Log.e("SELECTED ID ", selectedid)
//                                if (!mDialogView.diagnosis_drp.selectedItem.toString()
//                                        .equals("")
//                                ) {
//                                    if (mDialogView.diagnosis_drp.selectedItem.toString()
//                                            .equals("Others")
//                                    ) {
//                                        if (mDialogView.etOtherDiagnosis.text.toString().trim()
//                                                .equals("") || mDialogView.etOtherDiagnosis.text.trim()
//                                                .isEmpty()
//                                        ) {
//                                            mDialogView.etOtherDiagnosis.error =
//                                                "Diagnosis can't be empty"
//                                            return@setOnClickListener
//                                        } else {
//                                            selecteddiagnosis =
//                                                mDialogView.etOtherDiagnosis.text.toString().trim()
//                                            if (diagnosisnameList.size > 0 && !diagnosisnameList.contains(
//                                                    selecteddiagnosis
//                                                )
//                                            ) {
//                                                if (!containsCaseInsensitive(
//                                                        selecteddiagnosis,
//                                                        diagnosisnameList
//                                                    )
//                                                ) {
//                                                    if (!list.contains(selecteddiagnosis)) {
//                                                        if (!containsCaseInsensitive(
//                                                                selecteddiagnosis,
//                                                                list
//                                                            )
//                                                        ) {
//                                                            list.add(selecteddiagnosis)
//                                                        } else {
//                                                            toast("Diagnosis Already Added")
//                                                            return@setOnClickListener
//                                                        }
//                                                    } else {
//                                                        toast("Diagnosis Already Added")
//                                                        return@setOnClickListener
//                                                    }
//                                                } else {
//                                                    toast("Diagnosis is already available in list")
//                                                    return@setOnClickListener
//                                                }
//                                            } else {
//                                                toast("Diagnosis is already available in list")
//                                                return@setOnClickListener
//                                            }
//
//                                        }
//                                        selectedid = ""
//                                    } else {
//                                        selecteddiagnosis =
//                                            mDialogView.diagnosis_drp.selectedItem.toString()
//                                        list.add(selecteddiagnosis)
//                                    }
//
//                                    dygadapter = dyglistAdapter(
//                                        this@VideoSessionActivityConference,
//                                        list,
//                                        "Diagnosis",
//                                        this@VideoSessionActivityConference,
//                                        object : IClinicalTestItemToDelete {
//                                            override fun findDiagnosisIdAndDeleteItem(
//                                                diagnosisName: String,
//                                                itemPosition: Int
//                                            ) {
//                                                if (!mDialogView.diagnosis_drp.selectedItem.toString()
//                                                        .equals("")
//                                                ) {
//                                                    val diagId = findDiagnosisId(diagnosisName)
//                                                    if (diagId != "empty") {
//                                                        dygadapter?.callingDeleteApi(
//                                                            itemPosition,
//                                                            diagId, "", diagnosisName
//                                                        )
//                                                    } else {
//                                                        dygadapter?.callingDeleteApi(
//                                                            itemPosition,
//                                                            "", diagnosisName, diagnosisName
//                                                        )
//                                                    }
//                                                }
//                                            }
//
//                                        })
//                                    mDialogView.dygnnosislistt.adapter = dygadapter
//                                }
//                                Global.username = username
//                                Global.usertype = usertype
//                                Global.selectedid = selectedid
//                                Global.appointmentIdd = PatientAppointment.appointmentId
//                                Global.patientIdd = PatientAppointment.patientId
//
//                                //Global.showProgressDialog(this@VideoSessionActivity, "Please wait...")
//
//                                ApiUtils.getAPIService(this@VideoSessionActivityConference)
//                                    .add_patient_diagnosis_with_Other_Diagnosis(
//                                        SharedPrefs.getString(
//                                            this@VideoSessionActivityConference,
//                                            "sessionid"
//                                        )!!,
//                                        username,
//                                        usertype,
//                                        selectedid,
//                                        selecteddiagnosis,
//                                        PatientAppointment.appointmentId!!,
//                                        PatientAppointment.patientId!!
//                                    ).enqueue(object : Callback<AddDiagnosisResponse> {
//                                        override fun onFailure(
//                                            call: Call<AddDiagnosisResponse>, t: Throwable
//                                        ) {
//                                            // Global.hideProgressDialog()
//                                            Toast.makeText(
//                                                this@VideoSessionActivityConference,
//                                                "Something went wrong, please try again later",
//                                                Toast.LENGTH_LONG
//                                            ).show()
//                                        }
//
//                                        override fun onResponse(
//                                            call: Call<AddDiagnosisResponse>,
//                                            response: Response<AddDiagnosisResponse>
//                                        ) {
//                                            PatientAppointment.isDiagnosisUpdated = true
//                                            //   Global.hideProgressDialog()
//                                            Toast.makeText(
//                                                this@VideoSessionActivityConference,
//                                                "Diagnosis Added Successfully !",
//                                                Toast.LENGTH_SHORT
//                                            ).show()
//                                        }
//                                    })
//                            } else {
//                                toast("Diagnosis Already Added")
//                            }
//
//                        } else {
////                            list?.add(selecteddiagnosis)
//                            var index = diagnosisnameList.indexOf(selecteddiagnosis)
//                            Log.i("listasize", list?.size.toString())
//                            var selectedid = diagnosisidList[index]
//                            Log.e("SELECTED ID ", selectedid)
//                            if (!mDialogView.diagnosis_drp.selectedItem.toString().equals("")) {
//                                if (mDialogView.diagnosis_drp.selectedItem.toString()
//                                        .equals("Others")
//                                ) {
//                                    if (mDialogView.etOtherDiagnosis.text.toString().trim()
//                                            .equals("") || mDialogView.etOtherDiagnosis.text.trim()
//                                            .isEmpty()
//                                    ) {
//                                        mDialogView.etOtherDiagnosis.error =
//                                            "Diagnosis can't be empty"
//                                        return@setOnClickListener
//                                    } else {
//                                        selecteddiagnosis =
//                                            mDialogView.etOtherDiagnosis.text.toString().trim()
//                                        if (diagnosisnameList.size > 0 && !diagnosisnameList.contains(
//                                                selecteddiagnosis
//                                            )
//                                        ) {
//                                            if (!containsCaseInsensitive(
//                                                    selecteddiagnosis,
//                                                    diagnosisnameList
//                                                )
//                                            ) {
//                                                if (!list.contains(selecteddiagnosis)) {
//                                                    if (!containsCaseInsensitive(
//                                                            selecteddiagnosis,
//                                                            list
//                                                        )
//                                                    ) {
//                                                        list.add(selecteddiagnosis)
//                                                    } else {
//                                                        toast("Diagnosis Already Added")
//                                                        return@setOnClickListener
//
//                                                    }
//                                                } else {
//                                                    toast("Diagnosis Already Added")
//                                                    return@setOnClickListener
//                                                }
//                                            } else {
//                                                toast("Diagnosis is already available in list")
//                                                return@setOnClickListener
//                                            }
//
//                                        } else {
//                                            toast("Diagnosis is already available in list")
//                                            return@setOnClickListener
//                                        }
//                                    }
//                                    selectedid = ""
//                                } else {
//                                    selecteddiagnosis =
//                                        mDialogView.diagnosis_drp.selectedItem.toString()
//                                    list.add(selecteddiagnosis)
//                                }
//
//                                dygadapter =
//                                    dyglistAdapter(this@VideoSessionActivityConference,
//                                        list,
//                                        "Diagnosis",
//                                        this@VideoSessionActivityConference,
//                                        object : IClinicalTestItemToDelete {
//                                            override fun findDiagnosisIdAndDeleteItem(
//                                                diagnosisName: String,
//                                                itemPosition: Int
//                                            ) {
//                                                if (!mDialogView.diagnosis_drp.selectedItem.toString()
//                                                        .equals("")
//                                                ) {
//                                                    val diagId = findDiagnosisId(diagnosisName)
//                                                    if (diagId != "empty") {
//                                                        dygadapter?.callingDeleteApi(
//                                                            itemPosition,
//                                                            diagId, "", diagnosisName
//                                                        )
//                                                    } else {
//                                                        dygadapter?.callingDeleteApi(
//                                                            itemPosition,
//                                                            "", diagnosisName, diagnosisName
//                                                        )
//                                                    }
//                                                }
//                                            }
//                                        })
//                                mDialogView.dygnnosislistt.adapter = dygadapter
//
//                            }
//                            Global.username = username
//                            Global.usertype = usertype
//                            Global.selectedid = selectedid
//                            Global.appointmentIdd = PatientAppointment.appointmentId
//                            Global.patientIdd = PatientAppointment.patientId
//
//                            //Global.showProgressDialog(this@VideoSessionActivity, "Please wait...")
//
//                            ApiUtils.getAPIService(this@VideoSessionActivityConference)
//                                .add_patient_diagnosis_with_Other_Diagnosis(
//                                    SharedPrefs.getString(this@VideoSessionActivityConference, "sessionid")!!,
//                                    username,
//                                    usertype,
//                                    selectedid,
//                                    selecteddiagnosis,
//                                    PatientAppointment.appointmentId!!,
//                                    PatientAppointment.patientId!!
//                                ).enqueue(object : Callback<AddDiagnosisResponse> {
//                                    override fun onFailure(
//                                        call: Call<AddDiagnosisResponse>, t: Throwable
//                                    ) {
//                                        // Global.hideProgressDialog()
//                                        Toast.makeText(
//                                            this@VideoSessionActivityConference,
//                                            "Something went wrong, please try again later",
//                                            Toast.LENGTH_LONG
//                                        ).show()
//                                    }
//
//                                    override fun onResponse(
//                                        call: Call<AddDiagnosisResponse>,
//                                        response: Response<AddDiagnosisResponse>
//                                    ) {
//                                        //   Global.hideProgressDialog()
//                                        PatientAppointment.isDiagnosisUpdated = true
//                                        Toast.makeText(
//                                            this@VideoSessionActivityConference,
//                                            "Diagnosis Added Successfully !",
//                                            Toast.LENGTH_SHORT
//                                        ).show()
//                                    }
//                                })
//                        }
//                    }
//
//                    mDialogView.addctestsbtn.setOnClickListener {
//                        try {
//                            val layoutManager = LinearLayoutManager(
//                                this@VideoSessionActivityConference,
//                                LinearLayoutManager.HORIZONTAL,
//                                false
//                            )
//                            mDialogView.rv_clinicalTests.layoutManager = layoutManager
//                            var selectedtest = mDialogView.ctests_drp.selectedItem.toString()
//                            if (!testlist?.contains(selectedtest)!!) {
//                                testlist?.add(selectedtest)
//
//                                var index = testsnameList.indexOf(selectedtest)
//                                Log.e("INDEX ", "" + index)
//                                if (!mDialogView.ctests_drp.selectedItem.toString().equals("")) {
//                                    selectedtest = mDialogView?.ctests_drp?.selectedItem.toString()
//
//                                    val textView = TextView(this@VideoSessionActivityConference)
//                                    textView.setPadding(20, 20, 20, 20)
//                                    val lp: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
//                                        LinearLayout.LayoutParams.MATCH_PARENT,
//                                        LinearLayout.LayoutParams.WRAP_CONTENT
//                                    )
//
//
//                                    lp.setMargins(6, 0, 6, 0)
//                                    textView.layoutParams = lp
//                                    textView.setBackgroundColor(resources.getColor(R.color.grey_light))
//                                    textView.text = selectedtest
//                                    dygTestsadapter = dygTestAdapter(
//                                        this@VideoSessionActivityConference,
//                                        testlist!!,
//                                        "Test",
//                                        this@VideoSessionActivityConference
//                                    )
//                                    mDialogView.rv_clinicalTests.adapter = dygTestsadapter
//
//                                }
//
//                                //  mDialogView.progress_1.visibility = View.VISIBLE
//                                ApiUtils.getAPIService(this@VideoSessionActivityConference).add_patient_test(
//                                    SharedPrefs.getString(this@VideoSessionActivityConference, "sessionid")!!,
//                                    username!!,
//                                    usertype,
//                                    selectedtest,
//                                    PatientAppointment.appointmentId!!
//                                ).enqueue(object : Callback<AddDiagnosisResponse> {
//                                    override fun onFailure(
//                                        call: Call<AddDiagnosisResponse>, t: Throwable
//                                    ) {
//                                        mDialogView.progress_1.visibility = View.GONE
//
//                                        Toast.makeText(
//                                            this@VideoSessionActivityConference,
//                                            "Something went wrong, please try again later",
//                                            Toast.LENGTH_LONG
//                                        ).show()
//                                    }
//
//                                    override fun onResponse(
//                                        call: Call<AddDiagnosisResponse>,
//                                        response: Response<AddDiagnosisResponse>
//                                    ) {
//                                        mDialogView.progress_1.visibility = View.GONE
//                                        Toast.makeText(
//                                            this@VideoSessionActivityConference,
//                                            "Test Added Successfully !",
//                                            Toast.LENGTH_LONG
//                                        ).show()
//                                    }
//                                })
//                            } else {
//                                toast("Test Already Added")
//                            }
//                        } catch (e: Exception) {
//                            Log.i("e", e.toString())
//                        }
//                    }
//                }
//            })
//
//
//            ApiUtils.getAPIService(this@VideoSessionActivityConference).get_medicine_list(
//                SharedPrefs.getString(this@VideoSessionActivityConference, "sessionid")!!,
//                username!!,
//                usertype
//            ).enqueue(object : Callback<MedicineList> {
//                override fun onFailure(call: Call<MedicineList>, t: Throwable) {
//                    toast("Something went wrong, please try again later")
//                }
//
//                override fun onResponse(
//                    call: Call<MedicineList>, response: Response<MedicineList>
//                ) {
//                    try {
//                        if (response.isSuccessful && response.body() != null && response.body()!!.statuscode == 200) {
//                            mDialogView.addmedicinebtn.visibility = View.VISIBLE
//                            mDialogView.addmedicinebtn.setOnClickListener {
//                                try {
//                                    var medicine =
//                                        mDialogView.medicines_drp.selectedItem.toString()
//                                    if (mednamelist.contains(medicine)) {
//                                        Toast.makeText(
//                                            this@VideoSessionActivityConference,
//                                            medicine + " is already added",
//                                            Toast.LENGTH_LONG
//                                        ).show()
//                                        return@setOnClickListener
//                                    }
//                                    var quantity =
//                                        mDialogView.quantitymeddrp.selectedItem.toString()
//                                    var dosage = mDialogView.dosagemeddrp.selectedItem.toString()
//                                    var unit = mDialogView.unitdrpdwnn.selectedItem.toString()
//                                    var timeperiod =
//                                        mDialogView.timeperioddrpp?.selectedItem.toString()
//                                    mDialogView.doctor_notes_text?.setText(mDialogView.doctor_notes_text.text.toString() + "\n" + medicine + " " + quantity + " " + unit + "," + dosage + "," + timeperiod)
//                                    mednamelist.add(medicine)
//                                    medquantitylist.add(quantity)
//                                    meddosagelist.add(dosage)
//                                    medUnitlist.add(unit)
//                                    medTimePeriodListlist.add(timeperiod)
//                                    if (medicinestring.equals("")) {
//                                        medicinestring =
//                                            medicineidList.get(medicinenameList.indexOf(medicine)) + "@#@" + appointmentId + "@#@" + dosage + "@#@" + quantity + "@#@" + unit + "@#@" + timeperiod
//                                    } else {
//                                        medicinestring =
//                                            medicinestring + "#@#" + medicineidList.get(
//                                                medicinenameList.indexOf(medicine)
//                                            ) + "@#@" + appointmentId + "@#@" + dosage + "@#@" + quantity + "@#@" + unit + "@#@" + timeperiod
//                                    }
//                                    Log.e("Medicine String ", medicinestring);
//                                } catch (e: Exception) {
//                                    Log.i("e", e.toString())
//                                }
//
//                            }
//                            var medicines: ArrayList<String> = ArrayList()
//                            var medicinesAdapter: ArrayAdapter<String>? = null
//                            medicinenameList.clear()
//                            medicineidList.clear()
//                            for (medicine in response.body()!!.data!!.list!!) {
//                                medicines.add(medicine.Brand_Name)
//                                medicinenameList.add(medicine.Brand_Name)
//                                medicineidList.add(medicine.Brand_ID.toString())
//                            }
//                            //  mDialogView.medicines_drp.setTitle("Select Medicine");
//                            //   mDialogView.medicines_drp.setPositiveButton("Close");
//                            medicinesAdapter = ArrayAdapter(
//                                this@VideoSessionActivityConference,
//                                android.R.layout.simple_spinner_item,
//                                medicines
//                            )
//                            medicinesAdapter!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//                            mDialogView.medicines_drp.adapter = medicinesAdapter
//                            mDialogView.addmedicinebtn.isClickable = true
//                        } else {
//                            mDialogView.addmedicinebtn.visibility = View.GONE
//                            mDialogView.medicinesdropdowns.visibility = View.GONE
//                        }
//                    } catch (e: Exception) {
//                        toast("Error: Medicine list failed to load")
//                        mDialogView.addmedicinebtn.visibility = View.GONE
//                        mDialogView.medicinesdropdowns.visibility = View.GONE
//                    }
//                }
//            })
//
//
//
//            ApiUtils.getAPIService(this@VideoSessionActivityConference).get_clinical_test(
//                SharedPrefs.getString(this@VideoSessionActivityConference, "sessionid")!!,
//                username!!,
//                usertype
//            ).enqueue(object : Callback<ClinicalTestsResponse> {
//                override fun onFailure(call: Call<ClinicalTestsResponse>, t: Throwable) {
//                    Toast.makeText(
//                        this@VideoSessionActivityConference,
//                        "Something went wrong, please try again later",
//                        Toast.LENGTH_LONG
//                    ).show()
//                }
//
//                override fun onResponse(
//                    call: Call<ClinicalTestsResponse>, response: Response<ClinicalTestsResponse>
//                ) {
//                    try {
//                        if (response.isSuccessful && response.body() != null && response.body()!!.statuscode == 200) {
//                            try {
//
//                                var tests: ArrayList<String> = ArrayList()
//                                var testsAdapter: ArrayAdapter<String>?
//                                testsnameList.clear()
//                                testsnameIdList.clear()
//
//
//                                for (test in response.body()!!.data!!.ctests) {
//                                    tests.add(test.test_name)
//                                    testsnameList.add(test.test_name)
//                                    testsnameIdList.add(test.id)
//                                    //testsidList.add(test.id.toString())
//                                    //testsidList.add(test.id.toString())
//                                }
//
//                                testsAdapter = ArrayAdapter(
//                                    this@VideoSessionActivityConference,
//                                    android.R.layout.simple_spinner_item,
//                                    tests
//                                )
//                                testsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//                                mDialogView.ctests_drp.adapter = testsAdapter
//
//                            } catch (e: java.lang.Exception) {
//                            }
//
//                            mDialogView.addctestsbtn.isClickable = true
//                            mDialogView.addctestsbtn.visibility = View.VISIBLE
//                        } else {
//                            Toast.makeText(
//                                this@VideoSessionActivityConference,
//                                "Error: Tests list failed to load",
//                                Toast.LENGTH_LONG
//                            ).show()
//
//
//                        }
//                    } catch (e: Exception) {
//                        Toast.makeText(
//                            this@VideoSessionActivityConference,
//                            "Error: Medicine list failed to load",
//                            Toast.LENGTH_LONG
//                        ).show()
//
//
//                    }
//                }
//            })
//
//            //cancel button click of custom layout
//            mDialogView.btn_okay.setOnClickListener {
//                //dismiss dialog
//                if (mDialogView.doctor_notes_text.text.toString().length < 1) {
//                    toast("Empty Notes")
//                } else if (!PatientAppointment.isDiagnosisUpdated) {
//                    toast("Empty Diagnosis")
//                } else {
////                }
////                    else if (PatientAppointment.addDiagnosisList.size <= 0) {
////                        toast("Enter diagnosis")
////                    }else{
//                    PatientAppointment.doctorNotes = mDialogView.doctor_notes_text?.text.toString()
//                    notesupdated = true
//                    val patientId = RequestBody.create("pid".toMediaTypeOrNull(), patientId)
//                    val doctorId = RequestBody.create("doctorid".toMediaTypeOrNull(), doctorId)
//                    val doc_type = RequestBody.create("doc_type".toMediaTypeOrNull(), "")
//                    val description = RequestBody.create(
//                        "description".toMediaTypeOrNull(),
//                        mDialogView.doctor_notes_text.text.toString()
//                    )
//                    val apptId = RequestBody.create("apptid".toMediaTypeOrNull(), appointmentId)
//                    sendFileUploadRequest1(apptId, patientId, doctorId, doc_type, description)
//
//                    //    mDialogView. progress_1.visibility = View.GONE
//                    toast("Notes Updated")
//                    sendStatusMessage(MessageType.SAVING_NOTES)
//                    Handler(Looper.getMainLooper()).postDelayed(
//                        { sendStopStatusMsg(MessageType.SAVING_NOTES) }, TIME_OUT
//                    )
//
//                    mDialogView.doctor_notes_text.removeTextChangedListener(doctorAddNoteTextWatcher)
//                    mAlertDialog?.dismiss()
//                    Global.selectedid.equals(null)
//                }
//
//            }
//            mDialogView.btn_cancel.setOnClickListener {
//                //dismiss dialog
//                testlist?.clear()
//                list.clear()
//                mDialogView.doctor_notes_text.removeTextChangedListener(doctorAddNoteTextWatcher)
//                mAlertDialog?.dismiss()
//            }
//            mDialogView.doctor_notes_text.addTextChangedListener(doctorAddNoteTextWatcher)
//
//        }
//        video_btn.setOnClickListener {
//            if (video_btn.isSelected) {
//                agoraEngine?.enableLocalVideo(false)
//                agoraEngine?.stopPreview()
//                localSurfaceView?.visibility = View.GONE
//                sendStatusMessage(MessageType.TURN_OFF_CAMERA)
//                Handler(Looper.getMainLooper()).postDelayed(
//                    { sendStopStatusMsg(MessageType.TURN_ON_CAMERA) }, TIME_OUT
//                )
//                video_btn.isSelected = false
//                video_btn.setImageResource(R.drawable.novideo)
//                //   removeVideo(R.id.my_frame_layout)
//
//            } else {
//                agoraEngine?.enableLocalVideo(true)
//                agoraEngine?.startPreview()
//                localSurfaceView?.visibility = View.VISIBLE
//                sendStatusMessage(MessageType.TURN_ON_CAMERA)
//                Handler(Looper.getMainLooper()).postDelayed(
//                    { sendStopStatusMsg(MessageType.TURN_ON_CAMERA) }, TIME_OUT
//                )
//                video_btn.isSelected = true
//                video_btn.setImageResource(R.drawable.videoon)
//
//                //  setupLocalVideo()
//            }
//        }
//    }
//
//
//    private fun joinChannel() {
////
////        val addnotesButton = addNNotesButton()
////        val moreButton = moreButton()
////        val chatButton = chatbutton()
//
//
////        Handler(Looper.getMainLooper()).postDelayed({
////            sendStatusMessage(MessageType.OPENING_MSGS)
////            Handler(Looper.getMainLooper()).postDelayed({
////                sendStopStatusMsg(MessageType.OPENING_MSGS)
////            }, TIME_OUT)
////            count.visibility = View.GONE
////            val intent = Intent(this@VideoSessionActivity, VideoSessionChat::class.java)
////            intent.putExtra("patientId", patientId)
////            intent.putExtra("patientName", patientName)
////            intent.putExtra("image_name_p", "")
////            startActivity(intent)
////            Global.IntentFromHandler = true
////        }, 3000)
//
////        this.agView?.join(roomId, null, role = Constants.CLIENT_ROLE_BROADCASTER, uid)
//
////        writeImagePhoneGallery()
//
//        LocalBroadcastManager.getInstance(this).registerReceiver(
//            aLBReceiver, IntentFilter("callEnd")
//        )
//
//        /* chatButton.setOnClickListener {
//             sendStatusMessage(MessageType.OPENING_MSGS)
//             Handler(Looper.getMainLooper()).postDelayed({
//                 sendStopStatusMsg(MessageType.OPENING_MSGS)
//             }, TIME_OUT)
//             count.visibility = View.GONE
//             val intent = Intent(this, VideoSessionChat::class.java)
//             intent.putExtra("patientId", patientId)
//             intent.putExtra("patientName", patientName)
//             intent.putExtra("image_name_p", "")
//             startActivity(intent)
//         }*/
//        /* moreButton.setOnClickListener {
//             val builder = AlertDialog.Builder(this)
//             builder.setTitle("More Options")
//
//             val moreoptions = arrayOf("Vitals History", "Medicine History", "Patient Reports")
//             builder.setItems(
//                 moreoptions
//             ) { dialog, which ->
//                 when (which) {
//                     0 -> {
//                         val intent = Intent(this, VideoSessionVitalsHistory::class.java)
//                         intent.putExtra("patientId", patientId)
//                         intent.putExtra("patientName", patientName)
//                         intent.putExtra("image_name_p", "")
//                         startActivity(intent)
//                     }
//
//                     1 -> {
//                         val intent = Intent(this, VideoSessionMedicineHistory::class.java)
//                         intent.putExtra("patientId", patientId)
//                         intent.putExtra("patientName", patientName)
//                         intent.putExtra("image_name_p", "")
//                         startActivity(intent)
//                     }
//
//                     2 -> {
//                         val intent = Intent(this, VideoSessionPatientReports::class.java)
//                         intent.putExtra("patientId", patientId)
//                         intent.putExtra("patientName", patientName)
//                         intent.putExtra("image_name_p", "")
//                         startActivity(intent)
//                     }
//                 }
//             }
//             val dialog = builder.create()
//             dialog.show()
//         }*/
//
//        val userData = SharedPrefs.getUserData(this@VideoSessionActivityConference)
//        val username = userData!!.username
//        val usertype = "DOCTOR"
//
//        managedoctorStatus(username, usertype)
//
//    }
//
//    private fun manageSignals() {
//        when (checkInternet()) {
//            "Good" -> {
//                signalstregnth.setImageResource(R.drawable.goodsignal)
//            }
//
//            "Excellent" -> {
//                signalstregnth.setImageResource(R.drawable.goodsignal)
//            }
//
//            "Average" -> {
//                signalstregnth.setImageResource(R.drawable.avg)
//            }
//
//            "Poor" -> {
//                signalstregnth.setImageResource(R.drawable.poor)
//                val builder = AlertDialog.Builder(this@VideoSessionActivityConference)
//
//                // Set the alert dialog title
//                builder.setTitle("Low Internet Connectivity")
//                builder.setIcon(android.R.drawable.ic_dialog_info)
//                // Display a message on alert dialog
//                builder.setMessage("You may experience difficulty in your session due to poor internet connection.")
//
//                // Set a positive button and its click listener on alert dialog
//                builder.setPositiveButton("Ok") { _, _ ->
//                    // Do something when user press the positive button
//
//                }
//
//                // Finally, make the alert dialog using builder
//                val dialog: AlertDialog = builder.create()
//
//                // Display the alert dialog on app interface
//                dialog.show()
//                val snackBar = Snackbar.make(
//                    coordinatorLayout,
//                    "You may experience difficulty in your session due to poor internet connection.",
//                    15000
//                ).setAction(R.string.gotit) {
//
//                }.setTextColor(resources.getColor(R.color.white))
//                    .setBackgroundTint(resources.getColor(R.color.pink_dark_calendar))
//                    .setActionTextColor(resources.getColor(R.color.white))
//                val view = snackBar.view
//                val params = view.layoutParams as FrameLayout.LayoutParams
//                params.topMargin = 150
//                params.gravity = Gravity.CENTER_HORIZONTAL
//                snackBar.show()
//            }
//        }
//    }
//
//    private fun writeImagePhoneGallery() {
//        //writing image to phone gallery
//        val bm = BitmapFactory.decodeResource(resources, R.drawable.logo_splash)/*   val file1 = File(
//               Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
//               "virtualCallingBackgroundImg.PNG"
//           )
//           val outStream = FileOutputStream(file1)
//           bm.compress(Bitmap.CompressFormat.PNG, 100, outStream)
//           outStream.flush()
//           outStream.close()*/
////        saveImageToGallery(bm)
//
//    }
//
//    private fun getDoctorDocumentsAppt(
//        sessionid: String?, username: String?, usertype: String, mDialogView: View
//    ) {
//
//        ApiUtils.getAPIService(this@VideoSessionActivityConference)
//            .get_doctor_documents_appt(sessionid!!, username!!, usertype, appointmentId)
//            .enqueue(object : Callback<DoctorNotesResponse> {
//                override fun onFailure(call: Call<DoctorNotesResponse>, t: Throwable) {
//                    //Do nothing
//                }
//
//                override fun onResponse(
//                    call: Call<DoctorNotesResponse>, response: Response<DoctorNotesResponse>
//                ) {
//                    //Do nothing
//                    try {
//                        docnotes = response.body()!!.data!!.documents[0].doctor_notes
//                        mDialogView.doctor_notes_text.setText(docnotes)
//                    } catch (e: java.lang.Exception) {
//                    }
//                }
//            })
//    }
//
//    private fun getDiagnosisList(
//        username: String,
//        usertype: String,
//        diagnosisnameList: ArrayList<String>,
//        diagnosisidList: ArrayList<String>,
//        mDialogView: View
//    ) {
//        ApiUtils.getAPIService(this@VideoSessionActivityConference).get_diagnosis_list(
//            SharedPrefs.getString(this@VideoSessionActivityConference, "sessionid")!!, username!!, usertype
//        ).enqueue(object : Callback<DiagnosisList> {
//            override fun onFailure(call: Call<DiagnosisList>, t: Throwable) {
//
//                toast("Something went wrong, please try again later")
//            }
//
//            override fun onResponse(
//                call: Call<DiagnosisList>, response: Response<DiagnosisList>
//            ) {
//                try {
//                    if (response.isSuccessful && response.body() != null && response.body()!!.statuscode == 200) {
//                        try {
//
//                            var diagnosis: ArrayList<String> = ArrayList()
//                            var diagnosisAdapter: ArrayAdapter<String>? = null
//                            medicinenameList.clear()
//                            medicineidList.clear()
//
//                            for (diag in response.body()!!.data!!.data!!) {
//                                diagnosis.add(diag.diagnosis_name!!)
//                                diagnosisnameList.add(diag.diagnosis_name!!)
//                                diagnosisidList.add(diag.id.toString())
//                            }
//
//                            diagnosisAdapter = ArrayAdapter(
//                                this@VideoSessionActivityConference,
//                                android.R.layout.simple_spinner_item,
//                                diagnosis
//                            )
//                            diagnosisAdapter!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//                            mDialogView.diagnosis_drp.adapter = diagnosisAdapter
//
//
//                        } catch (e: java.lang.Exception) {
//                        }
//                    } else {
//                        Toast.makeText(
//                            this@VideoSessionActivityConference,
//                            "Error: Diagnosis list failed to load",
//                            Toast.LENGTH_LONG
//                        ).show()
//
//                        try {
//
//                        } catch (e: java.lang.Exception) {
//                        }
//                    }
//                } catch (e: Exception) {
//                    Toast.makeText(
//                        this@VideoSessionActivityConference,
//                        "Error: Diagnosis list failed to load",
//                        Toast.LENGTH_LONG
//                    ).show()
//                    try {
//
//                    } catch (e: java.lang.Exception) {
//                    }
//
//                }
//                mDialogView.adddiagnosisbtn.setOnClickListener {
//                    try {
//                        val layoutManager = LinearLayoutManager(
//                            this@VideoSessionActivityConference, LinearLayoutManager.HORIZONTAL, false
//                        )
//                        mDialogView.dygnnosislistt.layoutManager = layoutManager
//                        selecteddiagnosis = mDialogView.diagnosis_drp.selectedItem.toString()
////                        list?.add(selecteddiagnosis)
//                        var index = diagnosisnameList.indexOf(selecteddiagnosis)
//                        Log.i("listasize", list?.size.toString())
//                        var selectedid = diagnosisidList[index].toString()
//                        Log.e("SELECTED ID ", selectedid)
//                        if (!mDialogView.diagnosis_drp.selectedItem.toString().equals("")) {
//
//                            if (mDialogView.diagnosis_drp.selectedItem.toString()
//                                    .equals("Others")
//                            ) {
//                                if (mDialogView.etOtherDiagnosis.text.toString()
//                                        .trim() == "" || mDialogView.etOtherDiagnosis.text.trim()
//                                        .isEmpty()
//                                ) {
//                                    mDialogView.etOtherDiagnosis.error = "Diagnosis can't be empty"
//                                    return@setOnClickListener
//                                } else {
//                                    selecteddiagnosis =
//                                        mDialogView.etOtherDiagnosis.text.toString().trim()
//                                    if (diagnosisnameList.size > 0 && !diagnosisnameList.contains(
//                                            selecteddiagnosis
//                                        )
//                                    ) {
//                                        if (!containsCaseInsensitive(
//                                                selecteddiagnosis,
//                                                diagnosisnameList
//                                            )
//                                        ) {
//                                            if (!list.contains(selecteddiagnosis)) {
//                                                if (!containsCaseInsensitive(
//                                                        selecteddiagnosis,
//                                                        list
//                                                    )
//                                                ) {
//                                                    list.add(selecteddiagnosis)
//                                                } else {
//                                                    toast("Diagnosis Already Added")
//                                                    return@setOnClickListener
//
//                                                }
//                                            } else {
//                                                toast("Diagnosis Already Added")
//                                                return@setOnClickListener
//                                            }
//                                        } else {
//                                            toast("Diagnosis is already available in list")
//                                            return@setOnClickListener
//                                        }
//                                    } else {
//                                        toast("Diagnosis Already Added")
//                                        return@setOnClickListener
//                                    }
//
//                                }
//                                selectedid = ""
//                            } else {
//                                selecteddiagnosis =
//                                    mDialogView.diagnosis_drp.selectedItem.toString()
//                                list.add(selecteddiagnosis)
//                            }
//
//                            dygadapter = dyglistAdapter(this@VideoSessionActivityConference,
//                                list,
//                                "Diagnosis",
//                                this@VideoSessionActivityConference,
//                                object : IClinicalTestItemToDelete {
//                                    override fun findDiagnosisIdAndDeleteItem(
//                                        diagnosisName: String,
//                                        itemPosition: Int
//                                    ) {
//                                        if (mDialogView.diagnosis_drp.selectedItem.toString() != "") {
//                                            val diagId = findDiagnosisId(diagnosisName)
//                                            if (diagId != "empty") {
//                                                dygadapter?.callingDeleteApi(
//                                                    itemPosition,
//                                                    diagId, "", diagnosisName
//                                                )
//                                            } else {
//                                                dygadapter?.callingDeleteApi(
//                                                    itemPosition,
//                                                    "", diagnosisName, diagnosisName
//                                                )
//                                            }
//                                        }
//                                    }
//
//                                })
//                            mDialogView.dygnnosislistt.adapter = dygadapter
//
//                        }
//                        Global.username = username
//                        Global.usertype = usertype
//                        Global.selectedid = selectedid
//                        Global.appointmentIdd = PatientAppointment.appointmentId
//                        Global.patientIdd = PatientAppointment.patientId
//
//                        //Global.showProgressDialog(this@VideoSessionActivity, "Please wait...")
//
//                        addpatientDiagnnosisApi(username, usertype, selectedid)
//                    } catch (e: Exception) {
//                        Toast.makeText(
//                            this@VideoSessionActivityConference, e.toString(), Toast.LENGTH_LONG
//                        ).show()
//                    }
//                }
//            }
//        })
//    }
//
//    private fun addpatientDiagnnosisApi(username: String, usertype: String, selectedid: String) {
//        ApiUtils.getAPIService(this@VideoSessionActivityConference)
//            .add_patient_diagnosis_with_Other_Diagnosis(
//                SharedPrefs.getString(this@VideoSessionActivityConference, "sessionid")!!,
//                username!!,
//                usertype,
//                selectedid,
//                selecteddiagnosis,
//                PatientAppointment.appointmentId!!,
//                PatientAppointment.patientId!!
//            ).enqueue(object : Callback<AddDiagnosisResponse> {
//                override fun onFailure(
//                    call: Call<AddDiagnosisResponse>, t: Throwable
//                ) {
//
//                    //  Global.hideProgressDialog()
//
//                    Toast.makeText(
//                        this@VideoSessionActivityConference,
//                        "Something went wrong, please try again later",
//                        Toast.LENGTH_LONG
//                    ).show()
//                }
//
//                override fun onResponse(
//                    call: Call<AddDiagnosisResponse>, response: Response<AddDiagnosisResponse>
//                ) {
//                    //  Global.hideProgressDialog()
//                    Toast.makeText(
//                        this@VideoSessionActivityConference,
//                        "Diagnosis Added Successfully !",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//            })
//    }
//
//    private fun managedoctorStatus(username: String?, usertype: String) {
//        ApiUtils.getAPIService(this).manage_doctor_status(
//            SharedPrefs.getString(this@VideoSessionActivityConference, "sessionid")!!,
//            username!!,
//            usertype,
//            appointmentId,
//            patientId,
//            doctorId,
//            "Busy"
//        ).enqueue(object : Callback<ForgotPasswordResponse> {
//            override fun onFailure(call: Call<ForgotPasswordResponse>, t: Throwable) {
//                //Do nothing for now
//            }
//
//            override fun onResponse(
//                call: Call<ForgotPasswordResponse>, response: Response<ForgotPasswordResponse>
//            ) {
//                try {
//                } catch (e: Exception) {
//                    toast("Something went wrong, please try again later")
//                }
//            }
//        })
//    }
//
//    private fun getMedicinslist(username: String, usertype: String, mDialogView: View) {
//        ApiUtils.getAPIService(this@VideoSessionActivityConference).get_medicine_list(
//            SharedPrefs.getString(this@VideoSessionActivityConference, "sessionid")!!, username!!, usertype
//        ).enqueue(object : Callback<MedicineList> {
//            override fun onFailure(call: Call<MedicineList>, t: Throwable) {
//                toast("Something went wrong, please try again later")
//            }
//
//            override fun onResponse(
//                call: Call<MedicineList>, response: Response<MedicineList>
//            ) {
//                try {
//                    if (response.isSuccessful && response.body() != null && response.body()!!.statuscode == 200) {
//
//                        mDialogView.addmedicinebtn.setOnClickListener {
//                            var medicine = mDialogView.medicines_drp.selectedItem.toString()
//                            if (mednamelist.contains(medicine)) {
//                                Toast.makeText(
//                                    this@VideoSessionActivityConference,
//                                    medicine + " is already added",
//                                    Toast.LENGTH_LONG
//                                ).show()
//                                return@setOnClickListener
//                            }
//                            var quantity = mDialogView.quantitymeddrp.selectedItem.toString()
//                            var dosage = mDialogView.dosagemeddrp.selectedItem.toString()
//                            var unit = mDialogView.unitdrpdwnn.selectedItem.toString()
//                            var timeperiod = mDialogView.timeperioddrpp.selectedItem.toString()
//                            mednamelist.add(medicine)
//                            medquantitylist.add(quantity)
//                            meddosagelist.add(dosage)
//                            medUnitlist.add(unit)
//                            medTimePeriodListlist.add(timeperiod)
//
//
//                            mDialogView.doctor_notes_text.setText(mDialogView.doctor_notes_text.text.toString() + "\n" + medicine + " " + quantity + " " + unit + "@#@" + dosage + "@#@" + timeperiod)
//
//                            if (medicinestring.equals("")) {
//                                medicinestring =
//                                    medicineidList.get(medicinenameList.indexOf(medicine)) + "@#@" + appointmentId + "@#@" + dosage + "@#@" + quantity + "@#@" + unit + "@#@" + timeperiod
//                            } else {
//                                medicinestring = medicinestring + "#@#" + medicineidList.get(
//                                    medicinenameList.indexOf(medicine)
//                                ) + "@#@" + appointmentId + "@#@" + dosage + "@#@" + quantity + "@#@" + unit + "@#@" + timeperiod
//                            }
//                            Log.e("Medicine String ", medicinestring)
//                        }
//                        var medicines: ArrayList<String> = ArrayList()
//                        var medicinesAdapter: ArrayAdapter<String>? = null
//                        medicinenameList.clear()
//                        medicineidList.clear()
//                        for (medicine in response.body()!!.data!!.list!!) {
//                            medicines.add(medicine.Brand_Name)
//                            medicinenameList.add(medicine.Brand_Name)
//                            medicineidList.add(medicine.Brand_ID.toString())
//                        }
//                        //   mDialogView.medicines_drp.setTitle("Select Medicine");
//                        //   mDialogView.medicines_drp.setPositiveButton("Close");
//                        medicinesAdapter = ArrayAdapter(
//                            this@VideoSessionActivityConference,
//                            android.R.layout.simple_spinner_item,
//                            medicines
//                        )
//                        medicinesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//                        mDialogView.medicines_drp.adapter = medicinesAdapter
//                        mDialogView.addmedicinebtn.visibility = View.VISIBLE
//                    } else {
//                        mDialogView.addmedicinebtn.visibility = View.GONE
//                        mDialogView.medicinesdropdowns.visibility = View.GONE
//                    }
//                } catch (e: Exception) {
//                    toast("Error: Medicine list failed to load")
//                    mDialogView.addmedicinebtn.visibility = View.GONE
//                    mDialogView.medicinesdropdowns.visibility = View.GONE
//                }
//            }
//        })
//    }
//
//    private fun sendNotification(patientId: String, message: String) {
//
//        val userData = SharedPrefs.getUserData(this@VideoSessionActivityConference)
//        val username = userData!!.username
//        val usertype = "DOCTOR"
//        ApiUtils.getAPIService(this@VideoSessionActivityConference).sendNotification(
//            patientId,
//            message,
//            SharedPrefs.getString(this@VideoSessionActivityConference, "sessionid")!!,
//            username!!,
//            usertype
//        ).enqueue(object : Callback<SendNotificationResponse> {
//            override fun onFailure(call: Call<SendNotificationResponse>, t: Throwable) {
//                //Do nothing
//
//            }
//
//            override fun onResponse(
//                call: Call<SendNotificationResponse>, response: Response<SendNotificationResponse>
//            ) {
//                Log.i("issues", response.message().toString())
//            }
//        })
//    }
//
//    private fun sendFileUploadRequest1(
//        appointmentId: RequestBody,
//        patientId: RequestBody,
//        doctorId: RequestBody,
//        doc_type: RequestBody,
//        description: RequestBody
//    ) {
//        val userData = SharedPrefs.getUserData(this)
//        val sessionid = RequestBody.create(
//            "sessionid".toMediaTypeOrNull(), SharedPrefs.getString(this, "sessionid")!!
//        )
//        val username = RequestBody.create(
//            "username".toMediaTypeOrNull(), userData!!.username.toString()
//        )
//        val usertype = RequestBody.create("usertype".toMediaTypeOrNull(), "DOCTOR")
//        val fdate = RequestBody.create("followup_date".toMediaTypeOrNull(), selectedfollowupdate)
//        ApiUtils.getAPIService(this@VideoSessionActivityConference).uploadPatientFile1(
//            appointmentId,
//            patientId,
//            doctorId,
//            doc_type,
//            description,
//            sessionid,
//            username,
//            usertype,
//            fdate
//        ).enqueue(object : Callback<UploadPatientFileResponse> {
//            override fun onFailure(call: Call<UploadPatientFileResponse>, t: Throwable) {
//                //    mDialogView. progress_1.visibility = View.GONE
//                toast("Fleft went wrong, please try again later")
//                Log.e("Hello", t.toString())
//
//            }
//
//            override fun onResponse(
//                call: Call<UploadPatientFileResponse>,
//                response: Response<UploadPatientFileResponse>
//            ) {
//                //    mDialogView. progress_1.visibility = View.GONE
//                try {
//                    if (response.isSuccessful && response.body() != null && response.body()!!.statuscode == 200) {
//                        if (medicinestring.equals("")) {
//                        } else {
//                            SendPatientMedicines(medicinestring)
//                        }
//
//                    } else {
//                        //    mDialogView. progress_1.visibility = View.GONE
//                        toast("Please try again with a different file")
//                    }
//                } catch (e: Exception) {
//                    toast("Something went wrong, please try again later")
//                }
//            }
//        })
//
//    }
//
//    private fun SendPatientMedicines(description: String) {
//        val userData = SharedPrefs.getUserData(this)
//        val sessionid = SharedPrefs.getString(this, "sessionid")!!
//        val username = userData!!.username.toString()
//        val usertype = "DOCTOR"
//        ApiUtils.getAPIService(this@VideoSessionActivityConference)
//            .patient_medicine(sessionid, username, usertype, description)
//            .enqueue(object : Callback<ForgotPasswordResponse> {
//                override fun onFailure(call: Call<ForgotPasswordResponse>, t: Throwable) {
//                    //   mDialogView. progress_1.visibility = View.GONE
//                    toast("Something went wrong, please try again later")
//                }
//
//                override fun onResponse(
//                    call: Call<ForgotPasswordResponse>, response: Response<ForgotPasswordResponse>
//                ) {
//                    //   mDialogView. progress_1.visibility = View.GONE
//                    try {
//                        if (response.isSuccessful) {
//                        } else {
//                            //   mDialogView. progress_1.visibility = View.GONE
//                            toast("Something went wrong, please try again later")
//                        }
//                    } catch (e: Exception) {
//                        toast("Something went wrong, please try again later")
//                    }
//                }
//            })
//
//    }
//
//    private fun checkInternet(): String {
//        var status: String = ""
//        try {
//            val cm =
//                applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//            val activeNetwork: NetworkInfo? = cm.activeNetworkInfo!!
//            val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true
//            val mWifi: NetworkInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI)!!
//
//            if (isConnected) {
//
//                var connecqual = ""
//                if (mWifi.isConnected) {
//                    val wifiManager =
//                        applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
//                    val numberOfLevels = 5
//                    val wifiInfo: WifiInfo = wifiManager.getConnectionInfo()
//                    val level =
//                        WifiManager.calculateSignalLevel(wifiInfo.getRssi(), numberOfLevels);
//                    Log.e("Level ", "" + level)
//                    if (level == 3) {
//                        connecqual = "Average"
//                    } else if (level == 4) {
//                        connecqual = "Good"
//                    } else if (level >= 5) {
//                        connecqual = "Excellent"
//                    } else {
//                        connecqual = "Poor"
//                    }
//                    status = "Internet Status: Connected\n" + "Quality: " + connecqual
//                } else {
//                    status = "Internet Status: Connected"
//                }
//            } else {
//                status = "Internet Status: Not Connected"
//            }
//        } catch (ex: Exception) {
//            ex.printStackTrace()
//        }
//        return status
//    }
//
//    private fun realTimeMessaging() {
//        mChatManager = (application as MyApplication).getChatManager()
//        mRtmListener = MyRtmClientListener()
//        if (mRtmListener != null) {
//            mChatManager?.registerListener(mRtmListener!!)
//        }
//        mRtmClient = mChatManager?.rtmClient
//        doRTMLogin()
//    }
//
//    private fun doRTMLogin() {
//        Log.e("RTM LOGIN DOC ID ", doctorId)
//        mRtmClient?.login(null, doctorId, object : ResultCallback<Void?> {
//            override fun onSuccess(responseInfo: Void?) {
//                Log.i("", "login success")
//                isRTMLogin = true
//
//            }
//
//            override fun onFailure(errorInfo: ErrorInfo) {
//                Log.i("", "login failed: " + errorInfo.errorCode)
//                isRTMLogin = false
//
//            }
//        })
//    }
//
//    private fun sendStopStatusMsg(type: MessageType) {
//        val time =
//            if (mTimeSentLastMsg.containsKey(type.toString())) mTimeSentLastMsg.getValue(type.toString()) else System.currentTimeMillis()
//        if (System.currentTimeMillis() - time > 3) {
//            sendStatusMessage(MessageType.STOP_STATUS)
//        }
//    }
//
//    private fun showToast(msg: String) {
//        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
//    }
//
//    private fun findDiagnosisId(name: String): String {
//        var selectedid = "empty"
//        try {
//            if (diagnosisnameList != null && diagnosisnameList.size > 0) {
//                val index = diagnosisnameList.indexOf(name)
//                selectedid = diagnosisidList[index]
//            }
//            return selectedid
//        } catch (e: Exception) {
//            return selectedid
//        }
//    }
//
//    private fun findTestId(name: String): String {
//        var selectedTestId = "empty"
//        if (testsnameList != null && testsnameList.size > 0) {
//            val index = testsnameList.indexOf(name)
//            selectedTestId = testsnameIdList[index]
//        }
//        return selectedTestId
//    }
//
//    private fun sendStatusMessage(messageType: MessageType) {
//        val data = ByteArray(1)
//        val statusMessage = mRtmClient!!.createMessage()
//        statusMessage.setRawMessage(data, messageType.toString())
//        mTimeSentLastMsg[messageType.toString()] = System.currentTimeMillis()
//        sendPeerMessage(statusMessage)
//    }
//
//    private fun sendPeerMessage(message: RtmMessage) {
//        if (patientId == null) {
//            showToast("Patient not found")
//            return
//        }
//        mRtmClient?.sendMessageToPeer(patientId,
//            message,
//            mChatManager!!.sendMessageOptions,
//            object : ResultCallback<Void?> {
//                override fun onSuccess(aVoid: Void?) {
//                    // do nothing
//                }
//
//                override fun onFailure(errorInfo: ErrorInfo) {
//                    // refer to RtmStatusCode.PeerMessageState for the message state
//                    val errorCode = errorInfo.errorCode
//                    runOnUiThread {
//                        when (errorCode) {
//                            RtmStatusCode.PeerMessageError.PEER_MESSAGE_ERR_TIMEOUT, RtmStatusCode.PeerMessageError.PEER_MESSAGE_ERR_FAILURE -> showToast(
//                                getString(R.string.send_msg_failed)
//                            )
//
//                            RtmStatusCode.PeerMessageError.PEER_MESSAGE_ERR_PEER_UNREACHABLE -> showToast(
//                                getString(R.string.peer_offline)
//                            )
//
//                            RtmStatusCode.PeerMessageError.PEER_MESSAGE_ERR_CACHED_BY_SERVER -> showToast(
//                                getString(R.string.message_cached)
//                            )
//                        }
//
//                    }
//                }
//            })
//    }
//
//    private val doctorAddNoteTextWatcher = object : TextWatcher {
//        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//        }
//
//        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//
//        }
//
//        override fun afterTextChanged(editable: Editable?) {
//            if (editable == null) return
//            val lastMessageChange =
//                if (mTimeSentLastMsg.containsKey(MessageType.START_WRITING_NOTES.toString())) {
//                    mTimeSentLastMsg[MessageType.START_WRITING_NOTES.toString()]!!
//                } else 0L
//
//            if (editable.isNotEmpty() && (lastMessageChange == 0L || System.currentTimeMillis() - lastMessageChange > 3)) {
//
//                sendStatusMessage(MessageType.START_WRITING_NOTES)
//                Handler(Looper.getMainLooper()).postDelayed(
//                    { sendStopStatusMsg(MessageType.START_WRITING_NOTES) }, TIME_OUT
//                )
//            }
//        }
//    }
//
//    private fun showSnackBar(snackBarText: String) {
//
//        val snackBar = Snackbar.make(cl_custom_video_session, snackBarText, Snackbar.LENGTH_LONG)
//            .setAction(R.string.gotit)
//            {
//            }
//            .setActionTextColor(resources.getColor(R.color.white))
//
//        if (snackBar != null) {
//            val view = snackBar.view
//            if (view != null) {
//                val params = view.layoutParams as FrameLayout.LayoutParams
//                with(params)
//                {
//                    topMargin = 150
//                    gravity = Gravity.CENTER_HORIZONTAL
//                }
//                snackBar.show()
//            }
//        }
//
//    }
//
//    private fun showStatus(state: MessageType?) {
//        if (state != null) {
//            when (state) {
//                MessageType.STATUS_START_TYPING_MSG -> {
//                    //statusc_text.text = "Patient typing message..."
//                    showSnackBar("Patient is typing chat message...")
//                }
//
//                MessageType.ONLINE -> {
//                    statusc_text.text = "Online"
//                }
//
//                MessageType.OPENING_NOTES_SECTION -> {
//                    showSnackBar("Patient has opened notes...")
//                }
//
//                MessageType.START_WRITING_NOTES -> {
//                }
//
//                MessageType.OPENING_MSGS -> {
//                    showSnackBar("Patient has opened chat messages...")
//                }
//
//                MessageType.SAVING_NOTES -> {
//                }
//
//                MessageType.MIC_ON -> {
//                    showSnackBar("Patient has unmuted Mic...")
//                }
//
//                MessageType.MUTE_MIC -> {
//                    showSnackBar("Patient has muted Mic...")
//                }
//
//                MessageType.TURN_OFF_CAMERA -> {
//                    showSnackBar("Patient has turned off Camera...")
//                }
//
//                MessageType.TURN_ON_CAMERA -> {
//                    showSnackBar("Patient has turned on Camera...")
//                }
//
//                MessageType.STOP_STATUS -> {
//                    statusc_text.text = "Online"
//                }
//
//            }
//        }
//    }
//
//    private fun parseIncomingMessage(message: RtmMessage, from: String) {
//        runOnUiThread {
//            if (message.rawMessage.size == 1 && from == patientId) {
//                val type = message.text
//                showStatus(MessageType.fromValue(type))
//            }
//        }
//    }
//
//    inner class MyRtmClientListener : RtmClientListener {
//        override fun onConnectionStateChanged(state: Int, reason: Int) {
//
//        }
//
//        override fun onMessageReceived(message: RtmMessage, peerId: String) {
//            parseIncomingMessage(message, peerId)
//        }
//
//
//        override fun onTokenExpired() {}
//        override fun onTokenPrivilegeWillExpire() {
//
//        }
//
//        override fun onPeersOnlineStatusChanged(map: Map<String, Int>) {}
//    }
//
////    private fun virtualBackgroundIMG(imgSrc: String? = null) {
////        val backgroundSource = VirtualBackgroundSource()
////        backgroundSource.backgroundSourceType = VirtualBackgroundSource.BACKGROUND_IMG
////        backgroundSource.source = imgSrc
////        enableVirtualBackground(backgroundSource)
////    }
//
////    private fun enableVirtualBackground(backgroundSource: VirtualBackgroundSource) {
////        this.agView?.agkit?.enableVirtualBackground(true, backgroundSource, segprop)
////    }
//
////    private fun addNNotesButton(): AgoraButton {
////        val addnotes = AgoraButton(this)
////        addnotes.isVisible = false
////        addnotes.setImageDrawable(resources.getDrawable(R.drawable.notes))
////        this.agView?.agoraSettings?.extraButtons?.add(addnotes)
////        return addnotes
////    }
//
////    private fun chatbutton(): AgoraButton {
////        val chatbtn = AgoraButton(this)
////        chatbtn.isVisible = false
////        chatbtn.setImageDrawable(resources.getDrawable(R.drawable.vschaticon))
////        this.agView?.agoraSettings?.extraButtons?.add(chatbtn)
////        return chatbtn
////    }
//
////    private fun moreButton(): AgoraButton {
////        val morebtn = AgoraButton(this)
////        morebtn.isVisible = false
////        morebtn.setImageDrawable(resources.getDrawable(R.drawable.moredots))
////        this.agView?.agoraSettings?.extraButtons?.add(morebtn)
////        return morebtn
////    }
//
//    private fun virtualBackgroundCOLOR(color: Int) {
////        val backgroundSource = VirtualBackgroundSource()
////        backgroundSource.backgroundSourceType = VirtualBackgroundSource.BACKGROUND_COLOR
////        backgroundSource.color = color
////        enableVirtualBackground(backgroundSource)
//    }
//
////    private fun saveImageToGallery(bitmap: Bitmap) {
////        val savePath = Environment.getExternalStoragePublicDirectory(
////            Environment.DIRECTORY_PICTURES
////        ).toString() + "/MyApp"
////        val dir = File(savePath)
////        if (!dir.exists()) dir.mkdirs()
////        val fileName = "MyImage_" + System.currentTimeMillis() + ".png"
////        val file = File(dir, fileName)
////        try {
////            val stream = FileOutputStream(file)
////            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
////            stream.flush()
////            stream.close()
////        } catch (e: IOException) {
////            e.printStackTrace()
////        }
////        // Add the image to the system gallery
////        val values = ContentValues().apply {
////            put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
////            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
////            put(MediaStore.Images.Media.DATA, file.absolutePath)
////        }
////        val uri = this.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
////        // Get the image path from the gallery
////        val path = getRealPathFromURI(uri!!)
//////        virtualBackgroundIMG(path)
////        // Do something with the path
////        // ...
////    }
////
////    private fun getRealPathFromURI(uri: Uri): String {
////        val projection = arrayOf(MediaStore.Images.Media.DATA)
////        val cursor = this.contentResolver.query(uri, projection, null, null, null)
////        if (cursor != null) {
////            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
////            cursor.moveToFirst()
////            val path = cursor.getString(columnIndex)
////            cursor.close()
////            return path
////        }
////        return uri.path ?: ""
////    }
//
//    fun containsCaseInsensitive(s: String?, l: List<String>): Boolean {
//        for (string in l) {
//            if (string.equals(s, ignoreCase = true)) {
//                return true
//            }
//        }
//        return false
//    }
//
//
//    //Muti Agora Video Call Implementation (Full Rtc Version)
//
//    private var remoteViewsAdapter: RemoteViewsAdapters? = null
//    private lateinit var remoteViewItems: ArrayList<Int>
//    private lateinit var root: File
//    private var assetManager: AssetManager? = null
//    private var createdVirtualImageFilePath: String? = null
//
//    // Fill the App ID of your project generated on Agora Console.
//    private val appId = "1634986669fd433c903f131af715ba6b"
//
//    // Fill the channel name.
//    private var channelName = ""
//
//    // Fill the temp token generated on Agora Console.
//    private val token = ""
//
//    // An integer that identifies the local user.
//    private var uid = 0
//    private var remoteUid = 0 // Stores the uid of the remote user
//
//    private var isJoined = false
//
//    private var agoraEngine: RtcEngine? = null
//
//    //SurfaceView to render local video in a Container.
//    private var localSurfaceView: SurfaceView? = null
//
//    private val requestedPermissionsReqCode = 22
//    private val requestedPermissions = arrayOf(
//        Manifest.permission.RECORD_AUDIO,
//        Manifest.permission.CAMERA
//    )
//
//    private lateinit var localVideoViewContainer: FrameLayout
//
//    private fun setVirtualBackground() {
////        if (!createdVirtualImageFilePath.isNullOrEmpty()) {
////            val virtualBackgroundSource = VirtualBackgroundSource()
////            virtualBackgroundSource.backgroundSourceType = VirtualBackgroundSource.BACKGROUND_COLOR
////            virtualBackgroundSource.color = 0xFFFFFF
////
//////            val virtualBackgroundSource = VirtualBackgroundSource()
//////            virtualBackgroundSource.backgroundSourceType = VirtualBackgroundSource.BACKGROUND_IMG
//////            virtualBackgroundSource.source = createdVirtualImageFilePath
////            // Set processing properties for background
////            val segmentationProperty = SegmentationProperty()
////            segmentationProperty.modelType = SegmentationProperty.SEG_MODEL_AI
////            // Use SEG_MODEL_GREEN if you have a green background
////            segmentationProperty.greenCapacity =
////                0.5f // Accuracy for identifying green colors (range 0-1)
////            // Enable or disable virtual background
////            agoraEngine!!.enableVirtualBackground(
////                true,
////                virtualBackgroundSource, segmentationProperty
////            )
////        }
//        val virtualBackgroundSource = VirtualBackgroundSource()
//        virtualBackgroundSource.backgroundSourceType = VirtualBackgroundSource.BACKGROUND_COLOR
//        virtualBackgroundSource.color = 0xFFFFFF
//
////            val virtualBackgroundSource = VirtualBackgroundSource()
////            virtualBackgroundSource.backgroundSourceType = VirtualBackgroundSource.BACKGROUND_IMG
////            virtualBackgroundSource.source = createdVirtualImageFilePath
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
//    }
//
//    private fun customOnCreate() {
//        // Open the asset
//
//        if (!checkSelfPermission()) {
//            ActivityCompat.requestPermissions(
//                this,
//                requestedPermissions,
//                requestedPermissionsReqCode
//            )
//        }
//        remoteViewItems = ArrayList()
//        //UnComment If Virtual Background is required
////        createTemporaryFileForVirtualBackground()
//        setRemoteVideosAdapter()
//        setupVideoSDKEngine()
////        remoteVideoViewContainer = binding.remoteVideoViewContainer
////
////        localVideoViewContainer = local_video_view_container
//
//
//    }
//
//    private fun setRemoteVideosAdapter() {
//        Log.e(
//            TAG,
//            "setRemoteVideosAdapter remoteViewItems ${remoteViewItems.size}"
//        )
//        Log.e(
//            TAG,
//            "setRemoteVideosAdapter: MyApplication.remoteViewsList ${MyApplication.remoteViewsList.size}"
//        )
////        if (MyApplication.remoteViewsList.size > 0) {
//            remoteViewsAdapter =
//                RemoteViewsAdapters(this@VideoSessionActivityConference, MyApplication.remoteViewsList)
////        }
////        else {
////            remoteViewsAdapter = RemoteViewsAdapters(this@VideoSessionActivity, remoteViewItems)
////        }
////        val layoutManager = GridLayoutManager(this, 2)
////        // at last set adapter to recycler view.
////        rv_remote_views.layoutManager = layoutManager
//        rv_remote_views.adapter = remoteViewsAdapter
//    }
//
//    private fun setupVideoSDKEngine() {
//        try {
//            val config = RtcEngineConfig()
//            config.mContext = baseContext
//            config.mAppId = appId
//            config.mEventHandler = mRtcEventHandler
//            agoraEngine = RtcEngine.create(config)
//            // By default, the video module is disabled, call enableVideo to enable it.
//            agoraEngine?.enableVideo()
//        } catch (e: Exception) {
//            showMessage(e.toString())
//        }
//    }
//
//    fun addRemoteVideoInAgoraEngine(
//        mRemoteSurfaceView: SurfaceView,
//        uid: Int
//    ) {
//        agoraEngine!!.setupRemoteVideo(
//            VideoCanvas(
//                mRemoteSurfaceView,
//                VideoCanvas.RENDER_MODE_FIT,
//                uid
//            )
//        )
//        mRemoteSurfaceView.visibility = View.VISIBLE
//
//    }
//    private val TAG = "VideoSessionActivity"
//    private val mRtcEventHandler: IRtcEngineEventHandler = object : IRtcEngineEventHandler() {
//
//        override fun onError(err: Int) {
//            super.onError(err)
//            Log.e(TAG, "onError: $err", )
//        }
//
//        override fun onConnectionLost() {
//            super.onConnectionLost()
//            Log.e(TAG, "on COnnection lost", )
//        }
//
//        override fun onRejoinChannelSuccess(channel: String?, uid: Int, elapsed: Int) {
//            super.onRejoinChannelSuccess(channel, uid, elapsed)
//            Log.e(TAG, "onRejoinChannelSuccess $channel, $uid, $elapsed", )
//
//        }
//
//        override fun onRemoteVideoStateChanged(uid: Int, state: Int, reason: Int, elapsed: Int) {
//            super.onRemoteVideoStateChanged(uid, state, reason, elapsed)
//
//
//            if (state == Constants.REMOTE_VIDEO_STATE_STOPPED || state == Constants.REMOTE_VIDEO_STATE_FROZEN) {
////                    // The remote user's video has been turned off
////                    // You can hide the remote video view or take appropriate action
//                runOnUiThread {
//                    if (videosSwapped) {
//                        local_video_view_container.removeView(localSurfaceView!!)
//                        localSurfaceView?.visibility = View.GONE
//                    } else {
//                        remoteViewsAdapter?.remoteUserVideoChanged(uid, true)
//                    }
//                }
//            } else if (state == Constants.REMOTE_VIDEO_STATE_PLAYING) {
////                    // The remote user's video has resumed
////                    // You can show the remote video view or take appropriate action
//                runOnUiThread {
//                    if (videosSwapped) {
//                        local_video_view_container.addView(localSurfaceView!!)
//                        localSurfaceView?.visibility = View.VISIBLE
//                    } else {
//                        remoteViewsAdapter?.remoteUserVideoChanged(uid, false)
//                    }
//                }
//                Log.e("TAG", "onRemoteVideoStateChanged uid: $uid")
//                Log.e("TAG", "onRemoteVideoStateChanged state: $state")
//                Log.e("TAG", "onRemoteVideoStateChanged reason: $reason")
//                Log.e("TAG", "onRemoteVideoStateChanged elapsed: $elapsed")
//            }
//        }
//
//            override fun onFirstRemoteVideoDecoded(
//                uid: Int,
//                width: Int,
//                height: Int,
//                elapsed: Int
//            ) {
//                super.onFirstRemoteVideoDecoded(uid, width, height, elapsed)
//                runOnUiThread {
//                    otherpatyjoined = true
//                    otherpatyleftafterjoin = false
//                    val spannableString = SpannableString("$patientName joined")
//                    spannableString.setSpan(
//                        ForegroundColorSpan(resources.getColor(android.R.color.white)),
//                        0,
//                        spannableString.length,
//                        0
//                    )
//                    statusc_text.visibility = View.VISIBLE
//                    statusc_text.text = patientName + " joined"
//                }
//            }
//
//            // Listen for the remote host joining the channel to get the uid of the host.
//            override fun onUserJoined(uid: Int, elapsed: Int) {
//                remoteUid = uid
//                showMessage("Remote user joined $uid")
//                runOnUiThread {
//                    statusc_text.visibility = View.VISIBLE
//                    statusc_text.text = "Connected"
//                    if (remoteViewsAdapter != null) {
//                        remoteViewsAdapter?.addRemoteView(uid)
//                    }
//                }
//
//            }
//
//            override fun onJoinChannelSuccess(channel: String, uid: Int, elapsed: Int) {
//                isJoined = true
////                showMessage("Joined Channel $channel")
//            }
//
//            override fun onUserOffline(uid: Int, reason: Int) {
//
//                runOnUiThread {
//                    otherpatyjoined = false
//                    otherpatyleftafterjoin = true
//                    val spannableString = SpannableString("$patientName Left")
//                    spannableString.setSpan(
//                        ForegroundColorSpan(resources.getColor(android.R.color.white)),
//                        0,
//                        spannableString.length,
//                        0
//                    )
//                    endCallBtn.performClick()
////                            Globbal.callStatus = ""
//
//                }
//
////                showMessage("Remote user offline $uid $reason")
//                runOnUiThread {
//                    remoteViewsAdapter?.remoteRemoteView(uid)
//                }
//            }
//        }
//
//        fun showMessage(message: String?) {
//            runOnUiThread {
//                Toast.makeText(
//                    applicationContext,
//                    message,
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//        }
//
//        private fun createTemporaryFileForVirtualBackground() {
//            assetManager = assets
//            root = applicationContext.cacheDir
//            try {
//                // Open the asset
//                val inputStream =
//                    assets.open("logo_splash.png") // Replace with your asset file name
//
//                // Create a temporary file
//                val tempFile = File.createTempFile(
//                    "virtual_background_image",
//                    ".png",
//                    cacheDir
//                ) // Use getCacheDir() for cache or specify another directory
//
//                // Copy the asset to the temporary file
//                val outputStream = FileOutputStream(tempFile)
//                val buffer = ByteArray(1024)
//                var length: Int
//                while (inputStream.read(buffer).also { length = it } > 0) {
//                    outputStream.write(buffer, 0, length)
//                }
//                outputStream.flush()
//                outputStream.close()
//                inputStream.close()
//
//                // Now, tempFile.getAbsolutePath() contains the path to the copied asset
//                createdVirtualImageFilePath = tempFile.absolutePath
//                Log.e(
//                    "TAG",
//                    "createTemporaryFileForVirtualBackground: $createdVirtualImageFilePath"
//                )
//
//                // Use tempFilePath as needed with the API that expects a file path
//            } catch (e: IOException) {
//                e.printStackTrace()
//            }
//        }
//
//        private fun checkSelfPermission(): Boolean {
//            return !(ContextCompat.checkSelfPermission(
//                this,
//                requestedPermissions[0]
//            ) != PackageManager.PERMISSION_GRANTED ||
//                    ContextCompat.checkSelfPermission(
//                        this,
//                        requestedPermissions[1]
//                    ) != PackageManager.PERMISSION_GRANTED)
//        }
//
//        fun customJoinChannel() {
//            if (checkSelfPermission()) {
//                val options = ChannelMediaOptions()
//
//                // For a Video call, set the channel profile as COMMUNICATION.
//                options.channelProfile = Constants.CHANNEL_PROFILE_COMMUNICATION
//                // Set the client role as BROADCASTER or AUDIENCE according to the scenario.
//                options.clientRoleType = Constants.CLIENT_ROLE_BROADCASTER
//                // Display LocalSurfaceView.
//                customSetupLocalVideo()
//                localSurfaceView!!.visibility = View.VISIBLE
//                // Start local preview.
//                agoraEngine!!.startPreview()
//                // Join the channel with a temp token.
//                // You need to specify the user ID yourself, and ensure that it is unique in the channel.
//                agoraEngine!!.joinChannel(null, channelName, uid, options)
//            } else {
//                Toast.makeText(
//                    applicationContext,
//                    "Permissions was not granted",
//                    Toast.LENGTH_SHORT
//                )
//                    .show()
//            }
//        }
//
//        fun customLeaveChannel(view: View?) {
//            if (!isJoined) {
//                showMessage("Join a channel first")
//            } else {
//                agoraEngine?.stopPreview()
//                agoraEngine?.leaveChannel()
//                showMessage("You left the channel")
//                // Stop local video rendering.
//                if (localSurfaceView != null)
//                    localSurfaceView!!.visibility = View.GONE
//                isJoined = false
//                remoteViewsAdapter?.removeAllRemoteRemoteView()
//            }
//        }
//
//        private fun customSetupLocalVideo() {
//            val container = findViewById<FrameLayout>(R.id.local_video_view_container)
//            // Create a SurfaceView object and add it as a child to the FrameLayout.
//            localSurfaceView = SurfaceView(baseContext)
//            container.addView(localSurfaceView)
//            // Call setupLocalVideo with a VideoCanvas having uid set to 0.
//            agoraEngine!!.setupLocalVideo(
//                VideoCanvas(
//                    localSurfaceView,
//                    VideoCanvas.RENDER_MODE_HIDDEN,
//                    0
//                )
//            )
//            setVirtualBackground()
//        }
//
//        private fun customOnDestroy() {
//            agoraEngine?.stopPreview()
//            agoraEngine?.leaveChannel()
//            if (localSurfaceView != null)
//                localSurfaceView!!.visibility = View.GONE
//            isJoined = false
//            // Destroy the engine in a sub-thread to avoid congestion
//            MyApplication.remoteViewsList.clear()
//            remoteViewsAdapter?.removeAllRemoteRemoteView()
//            Thread {
//                if (createdVirtualImageFilePath != null) {
//                    deleteVirtualCreatedFile(createdVirtualImageFilePath!!)
//                }
//
//                RtcEngine.destroy()
//                agoraEngine = null
//            }.start()
//        }
//
//        private fun deleteVirtualCreatedFile(filePath: String) {
//            try {
//                val imageFile = File(filePath)
//                if (imageFile.exists()) {
//                    val deleted = imageFile.delete()
//                    if (deleted) {
//                        createdVirtualImageFilePath = null
//                    } else {
//                        //Failed to delete file
//                    }
//                } else {
//                    //File doesn't exist
//                }
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
//
//        private fun showLocalVideoInRemoteView(
//            uid: Int,
//            mFrameLayout: FrameLayout,
//            mRemoteSurfaceView: SurfaceView
//        ) {
//            mFrameLayout.addView(mRemoteSurfaceView)
//            agoraEngine!!.setupLocalVideo(
//                VideoCanvas(
//                    mRemoteSurfaceView,
//                    VideoCanvas.RENDER_MODE_FIT,
//                    uid
//                )
//            )
//            // Display RemoteSurfaceView.
//            mRemoteSurfaceView.visibility = View.VISIBLE
//        }
//
//        private fun showRemoteVideoInLocalView(uid: Int) {
//            runOnUiThread {
//                val container = findViewById<FrameLayout>(R.id.local_video_view_container)
//                // Create a SurfaceView object and add it as a child to the FrameLayout.
//                localSurfaceView = SurfaceView(baseContext)
//                container.addView(localSurfaceView)
//                // Call setupLocalVideo with a VideoCanvas having uid set to 0.
//                agoraEngine!!.setupRemoteVideo(
//                    VideoCanvas(
//                        localSurfaceView,
//                        VideoCanvas.RENDER_MODE_HIDDEN,
//                        uid
//                    )
//                )
//                localSurfaceView!!.visibility = View.VISIBLE
//            }
//
//        }
//
//        private fun setupRemoteVideo(
//            uid: Int,
//            mRemoteFrameLayout: FrameLayout,
//            mRemoteSurfaceView: SurfaceView
//        ) {
//            mRemoteFrameLayout.addView(mRemoteSurfaceView)
//            agoraEngine!!.setupRemoteVideo(
//                VideoCanvas(
//                    mRemoteSurfaceView,
//                    VideoCanvas.RENDER_MODE_FIT,
//                    uid
//                )
//            )
//            // Display RemoteSurfaceView.
//            mRemoteSurfaceView?.visibility = View.VISIBLE
//        }
//
//        private fun swapVideosLocalToRemote(
//            localUid: Int,
//            remoteUid: Int,
//            mRemoteFrameLayout: FrameLayout,
//            mRemoteSurfaceView: SurfaceView
//        ) {
//            showLocalVideoInRemoteView(localUid, mRemoteFrameLayout, mRemoteSurfaceView)
//            showRemoteVideoInLocalView(remoteUid)
//        }
//
//        private fun swapVideosRemoteToLocal(
//            localUid: Int,
//            remoteUid: Int,
//            mRemoteFrameLayout: FrameLayout,
//            mRemoteSurfaceView: SurfaceView
//        ) {
//            customSetupLocalVideo()
//            setupRemoteVideo(remoteUid, mRemoteFrameLayout, mRemoteSurfaceView)
//        }
//
//        private var videoSwappedWithUid: Int = 0
//        private var videosSwapped = false
//        fun toggleVideos(
//            remoteUserId: Int,
//            mRemoteFrameLayout: FrameLayout,
//            mRemoteSurfaceView: SurfaceView
//        ) {
//            if (videoSwappedWithUid == 0 || videoSwappedWithUid == remoteUserId) {
//                mRemoteFrameLayout.removeAllViews()
//                mRemoteSurfaceView.visibility = View.GONE
//
//                val localContainer = findViewById<FrameLayout>(R.id.local_video_view_container)
//                // Create a SurfaceView object and add it as a child to the FrameLayout.
//                localContainer.removeAllViews()
//                localSurfaceView!!.visibility = View.GONE
//
//                if (!videosSwapped) {
//                    swapVideosLocalToRemote(0, remoteUserId, mRemoteFrameLayout, mRemoteSurfaceView)
//                    videosSwapped = true
//                } else {
//                    swapVideosRemoteToLocal(0, remoteUserId, mRemoteFrameLayout, mRemoteSurfaceView)
//                    videosSwapped = false
//                }
//            } else {
//                mRemoteFrameLayout.removeAllViews()
//                mRemoteSurfaceView.visibility = View.GONE
//
//                val localContainer = findViewById<FrameLayout>(R.id.local_video_view_container)
//                // Create a SurfaceView object and add it as a child to the FrameLayout.
//                localContainer.removeAllViews()
//                localSurfaceView!!.visibility = View.GONE
//                swapVideosRemoteToLocal(0, remoteUserId, mRemoteFrameLayout, mRemoteSurfaceView)
//                videosSwapped = false
//
//                mRemoteFrameLayout.removeAllViews()
//                mRemoteSurfaceView.visibility = View.GONE
////
//                // Create a SurfaceView object and add it as a child to the FrameLayout.
//                localContainer.removeAllViews()
//                localSurfaceView!!.visibility = View.GONE
//
//                if (!videosSwapped) {
//                    swapVideosLocalToRemote(0, remoteUserId, mRemoteFrameLayout, mRemoteSurfaceView)
//                    videosSwapped = true
//                } else {
//                    swapVideosRemoteToLocal(0, remoteUserId, mRemoteFrameLayout, mRemoteSurfaceView)
//                    videosSwapped = false
//                }
//            }
//            videoSwappedWithUid = remoteUserId
//        }
//
//    }

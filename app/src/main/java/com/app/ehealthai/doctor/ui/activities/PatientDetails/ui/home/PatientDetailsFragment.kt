package com.app.ehealthai.doctor.ui.activities.PatientDetails.ui.home

import android.Manifest
import android.content.Intent
import android.content.res.Resources
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.view.marginStart
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.Orientation
import com.app.ehealthai.doctor.models.responses.ForgotPasswordResponse
import com.app.ehealthai.doctor.models.responses.GetVitalsResponse
import com.app.ehealthai.doctor.models.responses.KioskAccsoriesResp.KioskAccsoriesResp
import com.app.ehealthai.doctor.models.responses.VitalsData
import com.app.ehealthai.doctor.rtm.ChatManager
import com.app.ehealthai.doctor.rtm.status.MessageType
import com.app.ehealthai.doctor.ui.activities.PatientDetails.PatientAppointment
import com.app.ehealthai.doctor.ui.activities.ViewReportActivity
import com.app.ehealthai.doctor.utils.Global
import com.app.ehealthaidoctor.R
import com.app.ehealthaidoctor.models.responses.Document
import com.app.ehealthaidoctor.models.responses.GetPatientDocumentsResponse
import com.app.ehealthaidoctor.network.ApiUtils
import com.app.ehealthaidoctor.ui.activities.HomeActivity
import com.app.ehealthaidoctor.ui.activities.PatientDocumentsActivity
import com.app.ehealthaidoctor.ui.activities.VideoSessionActivity
import com.app.ehealthaidoctor.utils.SharedPrefs
import com.bumptech.glide.Glide
import com.example.ehealthai.MyApplication
import com.example.ehealthai.utils.Constants
import com.example.ehealthai.utils.Constants.Companion.INTENT_KEY_SCHOOL_ID
import com.example.ehealthai.utils.Constants.Companion.INTENT_KEY_STUDENT_ID
import com.example.ehealthai.utils.Constants.Companion.INTENT_KEY_STUDENT_NAME
import com.example.example.StudentScreening
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.installations.Utils
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import io.agora.rtm.ErrorInfo
import io.agora.rtm.ResultCallback
import io.agora.rtm.RtmClient
import io.agora.rtm.RtmClientListener
import io.agora.rtm.RtmMessage
import io.agora.rtm.RtmStatusCode
import kotlinx.android.synthetic.main.fragment_patient_reports.user_docs_tab
import kotlinx.android.synthetic.main.fragment_patientdetails.btn_view_report
import kotlinx.android.synthetic.main.fragment_patientdetails.spKioskAccessories
import kotlinx.android.synthetic.main.fragment_patientdetails.tab_layout
import kotlinx.android.synthetic.main.fragment_patientdetails.tvCheckKioskAccessories
import kotlinx.android.synthetic.main.fragment_patientdetails.view_pager
import kotlinx.android.synthetic.main.resetpassword.logo
import kotlinx.android.synthetic.main.vital_history_single_for_patient_details.pbVitalHistoryForSinglePatientDetails
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalDateTime
import java.util.Calendar
import java.util.Locale


class PatientDetailsFragment : Fragment() {

    var patientId: String? = null
    var isButtonEnabled = true
    private lateinit var handler: Handler
    private lateinit var enableButtonRunnable: Runnable

    companion object {
        var labReportsList: ArrayList<Document> = ArrayList()
        var xRayList: ArrayList<Document> = ArrayList()
        var docNotesList: ArrayList<Document> = ArrayList()
        var miscList: ArrayList<Document> = ArrayList()
    }

    private var mRtmClient: RtmClient? = null
    private var mChatManager: ChatManager? = null
    private var isRTMLogin = false
    private var mRtmListener: MyRtmClientListener? = null
    private val mTimeSentLastMsg = HashMap<String, Long>()

    private lateinit var context: PatientAppointment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = activity as PatientAppointment
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_patientdetails, container, false)
        val patname: TextView = root.findViewById(R.id.paname)
        val gendertxt: TextView = root.findViewById(R.id.second_text_view2)
        val agetxt: TextView = root.findViewById(R.id.second_text_view3)
        val start_call_btn: Button = root.findViewById(R.id.start_call_btn)
        val descriptiontxt: TextView = root.findViewById(R.id.description_text)
        val btnReport: TextView = root.findViewById(R.id.btn_view_report)
        val llViewReport: LinearLayout = root.findViewById(R.id.llViewReport)
        agetxt.setText(PatientAppointment.age)
        if (!PatientAppointment.description.isNullOrEmpty()) {
            descriptiontxt.text = PatientAppointment.description
        } else {
            descriptiontxt.visibility = View.GONE
        }
        gendertxt.setText(PatientAppointment.sex)

        val home_pd: ImageView = root.findViewById(R.id.pd_home)
        val pd_primary_action: ImageView = root.findViewById(R.id.pd_primary_action)


        if (PatientAppointment.appointmentId != null) {
            getKioskAccessories(PatientAppointment.appointmentId)
        }

        if (PatientAppointment.profile_image.equals("")) {
        } else {
            Glide.with(context).load(
                SharedPrefs.getString(
                    context, "filepath"
                ) + PatientAppointment.profile_image
            ).into(pd_primary_action)
        }
        val userData = SharedPrefs.getUserData(context)
        val username = userData!!.username
        val usertype = "DOCTOR"
        try {
            ApiUtils.getAPIService(context).callstatus(
                SharedPrefs.getString(context, "sessionid")!!,
                username!!,
                usertype,
                PatientAppointment.appointmentId.toString(),
                "Doctor has opened appointment"
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


                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
        home_pd.setOnClickListener {
            val intent = Intent(context, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }
        if (!PatientAppointment.registeredFrom.isNullOrEmpty() && PatientAppointment.registeredFrom != "null" && PatientAppointment.registeredFrom == "8") {
            if (!PatientAppointment.studentFatherPhone.isNullOrEmpty() && PatientAppointment.studentFatherPhone != "null") {
                if (PatientAppointment.studentFatherPhone.toString() != "12345") {
                    llViewReport.visibility = View.VISIBLE
                }
            }
        }

        btnReport.setOnClickListener {
            try {
                if (!PatientAppointment.studentCNIC.isNullOrEmpty()) {
                    val (schoolId, studentId) = PatientAppointment.studentCNIC.toString()
                        .split("_")

                    try {
                        if (Constants.isInternetConnected(context)) {
                            Global.showProgressDialog(context, "Opening report please wait")
                            ApiUtils.getAPIServiceEZScreening(context).checkStudentScreened(
                                studentId,
                            ).enqueue(object : Callback<StudentScreening> {
                                override fun onFailure(
                                    call: Call<StudentScreening>, t: Throwable
                                ) {
                                    Global.hideProgressDialog()
                                    //Do nothing
                                    Toast.makeText(
                                        context,
                                        "Something went wrong",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                }

                                override fun onResponse(
                                    call: Call<StudentScreening>,
                                    response: Response<StudentScreening>
                                ) {
                                    if (response.isSuccessful) {
                                        if (response.body() != null) {
                                            if (response.body()!!.stdData?.studentData?.size!! > 0) {
                                                if (response.body()!!.stdData!!.studentData.get(0).screeningStatus == 1) {
                                                    startActivity(
                                                        Intent(
                                                            context,
                                                            ViewReportActivity::class.java
                                                        )
                                                            .putExtra(
                                                                INTENT_KEY_SCHOOL_ID,
                                                                schoolId
                                                            )
                                                            .putExtra(
                                                                INTENT_KEY_STUDENT_ID,
                                                                studentId
                                                            )
                                                            .putExtra(
                                                                INTENT_KEY_STUDENT_NAME,
                                                                PatientAppointment.patientName
                                                            )
                                                    )
                                                } else if (response.body()!!.stdData!!.studentData.get(
                                                        0
                                                    ).screeningStatus == 0
                                                ) {
                                                    Toast.makeText(
                                                        context,
                                                        "Screening not done",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                            } else {
                                                Toast.makeText(
                                                    context,
                                                    "no report available",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }
                                    }
                                    Global.hideProgressDialog()
                                }
                            })
                        } else {
                            Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT)
                                .show()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        start_call_btn.setOnClickListener {
            if (isButtonEnabled) {
                // Disable the button
                start_call_btn.isEnabled = false
                isButtonEnabled = false
                requestPermissions()
                // Re-enable the button after a delay (e.g., 1 second)
                start_call_btn.postDelayed({
                    start_call_btn.isEnabled = true
                    isButtonEnabled = true
                }, 1000) // 1000 milliseconds (1 second)
            }
            // REMOVE EXTRA CODE
////            documents_progress_bar.visibility = View.VISIBLE
//            val userData = SharedPrefs.getUserData(context)
//            val username = userData!!.username
//            val usertype = "DOCTOR"
//
//            ApiUtils.getAPIService(context).callstatus(
//                SharedPrefs.getString(context, "sessionid")!!,
//                username!!,
//                usertype,
//                PatientAppointment.appointmentId.toString(),
//                "Doctor has joined video session"
//            ).enqueue(object : Callback<ForgotPasswordResponse> {
//                override fun onFailure(
//                    call: Call<ForgotPasswordResponse>, t: Throwable
//                ) {
//                    //Do nothing
////                    documents_progress_bar.visibility = View.GONE
//                    val intent = Intent(context, VideoSessionActivity::class.java)
//                    //val intent = Intent(context
//                    //, agkitActivity::class.java)
//                    // val intent = Intent(context
//                    //, VideoSessioActivityOld::class.java)
//                    intent.putExtra(
//                        "url",
//                        "https://ezshifa.com/emr/restapi_admin/index.php/videocall?roomid=" + PatientAppointment.roomid
//                    )
//                    startActivity(intent)
//                }
//
//                override fun onResponse(
//                    call: Call<ForgotPasswordResponse>,
//                    response: Response<ForgotPasswordResponse>
//                ) {
////                    documents_progress_bar.visibility = View.GONE
//                    val intent = Intent(context, VideoSessionActivity::class.java)
//                    //  val intent = Intent(context
//                    // , agkitActivity::class.java)
//                    //  val intent = Intent(context
//                    // , VideoSessioActivityOld::class.java)
//
//                    intent.putExtra(
//                        "url",
//                        "https://ezshifa.com/emr/restapi_admin/index.php/videocall?roomid=" + PatientAppointment.roomid
//                    )
//                    startActivity(intent)
//
//                }
//            })
        }

        Log.e("TAG", "onCreateView: ${PatientAppointment.apptDate}")
        Log.e("TAG", "onCreateView: ${PatientAppointment.apptTime}")
        if (PatientAppointment.apptstatus == "open" || PatientAppointment.apptstatus == "Open") {
            start_call_btn.visibility = View.VISIBLE
            if (!PatientAppointment.apptTime.isNullOrEmpty() && PatientAppointment.apptTime != "null" && !PatientAppointment.apptDate.isNullOrEmpty() && PatientAppointment.apptDate != "null") {
                handler = Handler(Looper.getMainLooper())
                enableButtonRunnable = object : Runnable {
                    override fun run() {

                        val isButtonEnabled = checkDateAndTimeDifference(
                            PatientAppointment.apptDate!!,
                            PatientAppointment.apptTime!!
                        )

                        if (isButtonEnabled) {
                            // Enable the button
                            start_call_btn.visibility = View.VISIBLE
                            // Optionally, you can stop checking at this point
                            handler.removeCallbacks(this)
                        } else {
                            start_call_btn.visibility = View.GONE
                            // Re-schedule the runnable
                            handler.postDelayed(this, 1000) // Check every second
                        }
                    }
                }

                // Start checking for enabling the button
                handler.post(enableButtonRunnable)

            } else if (!PatientAppointment.apptTime.isNullOrEmpty() && PatientAppointment.apptTime != "null") {
                handler = Handler(Looper.getMainLooper())
                enableButtonRunnable = object : Runnable {
                    override fun run() {
                        val currentTime = getCurrentTime()
                        val buttonEnableTime = PatientAppointment.apptTime

                        if (!currentTime.isNullOrEmpty() && !buttonEnableTime.isNullOrEmpty()) {
                            val isButtonEnabled = shouldEnableButton(currentTime, buttonEnableTime)

                            if (isButtonEnabled) {
                                // Enable the button
                                start_call_btn.visibility = View.VISIBLE
                                // Optionally, you can stop checking at this point
                                handler.removeCallbacks(this)
                            } else {
                                start_call_btn.visibility = View.GONE
                                // Re-schedule the runnable
                                handler.postDelayed(this, 1000) // Check every second
                            }
                        }
                    }
                }

                // Start checking for enabling the button
                handler.post(enableButtonRunnable)
            } else if (!PatientAppointment.apptDate.isNullOrEmpty() && PatientAppointment.apptDate != "null") {

            }
        } else {
            start_call_btn.visibility = View.GONE
        }
//        if (PatientAppointment.apptstatus.equals("closed") || PatientAppointment.apptstatus.equals("cancelled_patient")) {
//            start_call_btn.visibility = View.GONE
//        }
        patientId = PatientAppointment.patientId
        patname.text = PatientAppointment.patientName
        // patientId = intent.getStringExtra("patientId")

//        documents_progress_bar.visibility = View.VISIBLE
        if (Constants.isInternetConnected(context)) {
            try {
                ApiUtils.getAPIService(context).getPatientDocuments(
                    patientId!!,
                    SharedPrefs.getString(context, "sessionid")!!,
                    username!!,
                    usertype
                ).enqueue(object : Callback<GetPatientDocumentsResponse> {
                    override fun onFailure(call: Call<GetPatientDocumentsResponse>, t: Throwable) {
//                    documents_progress_bar.visibility = View.GONE

                        Toast.makeText(
                            context,
                            "Something went wrong, please try again later",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    override fun onResponse(
                        call: Call<GetPatientDocumentsResponse>,
                        response: Response<GetPatientDocumentsResponse>
                    ) {
//                    documents_progress_bar.visibility = View.GONE
                        sendStatusMessage(MessageType.VIEWING_PATIENT_DETAILS)
                        Handler(Looper.getMainLooper()).postDelayed(
                            { sendStopStatusMsg(MessageType.VIEWING_PATIENT_DETAILS) },
                            VideoSessionActivity.TIME_OUT
                        )
                        try {
                            if (response.isSuccessful && response.body() != null && response.body()!!.statuscode == 200 && response.body()!!.data.documents != null) {
                                PatientDocumentsActivity.labReportsList = ArrayList()
                                PatientDocumentsActivity.xRayList = ArrayList()
                                PatientDocumentsActivity.docNotesList = ArrayList()
                                PatientDocumentsActivity.miscList = ArrayList()

                                for (i in 0 until response.body()!!.data.documents!!.size) {

                                    if (response.body()!!.data.documents!!.get(i).doc_type.toLowerCase()
                                            .contains("lab_report")
                                    ) {
                                        PatientDocumentsActivity.labReportsList.add(
                                            response.body()!!.data.documents!!.get(
                                                i
                                            )
                                        )
                                    } else if (response.body()!!.data.documents!!.get(i).doc_type.toLowerCase()
                                            .contains("x_ray")
                                    ) {
                                        PatientDocumentsActivity.xRayList.add(
                                            response.body()!!.data.documents!!.get(
                                                i
                                            )
                                        )
                                    } else if (response.body()!!.data.documents!!.get(i).doc_type.toLowerCase()
                                            .contains("doc_notes")
                                    ) {
                                        PatientDocumentsActivity.docNotesList.add(
                                            response.body()!!.data.documents!!.get(
                                                i
                                            )
                                        )
                                    } else {
                                        PatientDocumentsActivity.miscList.add(
                                            response.body()!!.data.documents!!.get(
                                                i
                                            )
                                        )
                                    }
                                }

//                            setupViewpager()
                            }

                        } catch (e: Exception) {
                            Toast.makeText(
                                context,
                                "Something went wrong, please try again later",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                })
            } catch (e: Exception) {
//            documents_progress_bar.visibility = View.GONE
            }
        }

        if (!isRTMLogin && PatientAppointment.doctorId != null && PatientAppointment.patientId != null) {
            realTimeMessaging()
        }



        return root
    }


    fun dpToPx(dp: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            Resources.getSystem().displayMetrics
        ).toInt()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mViewPager: ViewPager2 = view.findViewById(R.id.view_pager)
        val mTabLayout: TabLayout = view.findViewById(R.id.tab_layout)

        try {
            mViewPager.offscreenPageLimit = 2
            val viewPagerAdapter = ViewPagerAdapterPatientDetails(
                this,
                patientId,
                PatientAppointment.appointmentId,
                context
            )
            mViewPager.adapter = viewPagerAdapter

            val tabs = mTabLayout.getChildAt(0) as ViewGroup

            val linearLayout = mTabLayout.getChildAt(0) as LinearLayout
            linearLayout.showDividers = LinearLayout.SHOW_DIVIDER_MIDDLE
            val drawable = GradientDrawable()
            drawable.setColor(this.resources.getColor(R.color.grey))
            drawable.setSize(3, 1)
            linearLayout.dividerPadding = 10
            linearLayout.dividerDrawable = drawable

            // Attach the TabLayout to the ViewPager2
            TabLayoutMediator(mTabLayout, mViewPager) { tab, position ->
                when (position) {
                    0 -> {
                        tab.text = "Vitals"
                    }

                    1 -> {
                        tab.text = "Screening"
                    }

                    2 -> {
                        tab.text = "Rapid Tests"
                    }
                }

            }.attach()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getKioskAccessories(appointmentId: String?) {
        ApiUtils.getAPIService(context).kioskAccesories(
            appointmentId!!
        ).enqueue(object : Callback<KioskAccsoriesResp> {
            override fun onFailure(
                call: Call<KioskAccsoriesResp>, t: Throwable
            ) {
                //Do nothing
            }

            override fun onResponse(
                call: Call<KioskAccsoriesResp>, response: Response<KioskAccsoriesResp>
            ) {
                val tests: ArrayList<String> = ArrayList()
                if (response.body()?.data?.list != null) {
                    spKioskAccessories?.visibility = View.VISIBLE
                    tvCheckKioskAccessories?.visibility = View.VISIBLE
                    for (i in 0 until response.body()?.data?.list!!.size) {
                        tests.add(response.body()?.data?.list!![i].accessoryName)
                    }
                    val testsAdapter: ArrayAdapter<String>?
                    testsAdapter =
                        ArrayAdapter(context, android.R.layout.simple_spinner_item, tests)
                    testsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spKioskAccessories?.adapter = testsAdapter
                } else {
                    spKioskAccessories?.visibility = View.GONE
                    tvCheckKioskAccessories?.visibility = View.GONE
                }

            }
        })

    }


    private fun realTimeMessaging() {
        mChatManager = (activity?.application as MyApplication).getChatManager()
        mRtmListener = MyRtmClientListener()
        if (mRtmListener != null) {
            mChatManager?.registerListener(mRtmListener!!)
        }
        mRtmClient = mChatManager?.rtmClient
        doRTMLogin()
    }


    private fun doRTMLogin() {
        Log.e("RTM LOGIN DOC ID ", PatientAppointment.doctorId!!)
        mRtmClient?.login(null, PatientAppointment.doctorId, object : ResultCallback<Void?> {
            override fun onSuccess(responseInfo: Void?) {
                Log.i("", "login success")
                isRTMLogin = true
                sendStatusMessage(MessageType.VIEWING_PATIENT_DETAILS)
                Handler(Looper.getMainLooper()).postDelayed(
                    { sendStopStatusMsg(MessageType.VIEWING_PATIENT_DETAILS) },
                    VideoSessionActivity.TIME_OUT
                )
            }

            override fun onFailure(errorInfo: ErrorInfo) {
                Log.i("", "login failed: " + errorInfo.errorCode)
                isRTMLogin = false

            }
        })
    }

    override fun onStop() {
        super.onStop()
        mChatManager?.unregisterListener(mRtmListener)
    }

    inner class MyRtmClientListener : RtmClientListener {
        override fun onConnectionStateChanged(state: Int, reason: Int) {

        }

        override fun onMessageReceived(message: RtmMessage, peerId: String) {
            parseIncommingMessage(message, peerId)
        }


        override fun onTokenExpired() {}
        override fun onTokenPrivilegeWillExpire() {

        }

        override fun onPeersOnlineStatusChanged(map: Map<String, Int>) {}
    }

    private fun parseIncommingMessage(message: RtmMessage, from: String) {
        activity?.runOnUiThread {
            if (message.rawMessage.size == 1 && from == PatientAppointment.patientId) {
                val type = message.text

                //showStatus(MessageType.fromValue(type))
            }
        }
    }

    private fun showsnackbar(snackbarText: String) {

        /*val snackbar=  Snackbar.make(cordlayan, snackbarText, Snackbar.LENGTH_LONG)
            .setAction(R.string.gotit) {

            }
            .setActionTextColor(resources.getColor(R.color.white))


        val view = snackbar.view
        val params = view.layoutParams as FrameLayout.LayoutParams
        params.topMargin=150
        params.gravity = Gravity.CENTER_HORIZONTAL
        snackbar.show()*/

    }

    private fun sendStatusMessage(messageType: MessageType) {
        try {
            val data = ByteArray(1)
            val statusMessage = mRtmClient!!.createMessage()
            statusMessage.setRawMessage(data, messageType.toString())
            mTimeSentLastMsg[messageType.toString()] = System.currentTimeMillis()
            sendPeerMessage(statusMessage)
        } catch (e: Exception) {
        }

    }

    private fun sendStopStatusMsg(type: MessageType) {
        val time =
            if (mTimeSentLastMsg.containsKey(type.toString())) mTimeSentLastMsg.getValue(type.toString()) else System.currentTimeMillis()
        if (System.currentTimeMillis() - time > 3) {
            sendStatusMessage(MessageType.STOP_STATUS)
        }
    }

    private fun sendPeerMessage(message: RtmMessage) {
        if (PatientAppointment.patientId == null) {
            //showsnackbar("Patient not found")
            return
        }
        mRtmClient?.sendMessageToPeer(PatientAppointment.patientId,
            message,
            mChatManager!!.sendMessageOptions,
            object : ResultCallback<Void?> {
                override fun onSuccess(aVoid: Void?) {
                    // do nothing
                }

                override fun onFailure(errorInfo: ErrorInfo) {
                    // refer to RtmStatusCode.PeerMessageState for the message state
                    val errorCode = errorInfo.errorCode
                    activity?.runOnUiThread {
                        when (errorCode) {
                            RtmStatusCode.PeerMessageError.PEER_MESSAGE_ERR_TIMEOUT, RtmStatusCode.PeerMessageError.PEER_MESSAGE_ERR_FAILURE -> showsnackbar(
                                context.resources.getString(R.string.send_msg_failed)
                            )

                            RtmStatusCode.PeerMessageError.PEER_MESSAGE_ERR_PEER_UNREACHABLE -> showsnackbar(
                                context.resources.getString(R.string.peer_offline)
                            )

                            RtmStatusCode.PeerMessageError.PEER_MESSAGE_ERR_CACHED_BY_SERVER -> showsnackbar(
                                context.resources.getString(R.string.message_cached)
                            )
                        }

                    }
                }
            })
    }

    private fun requestPermissions() {
        // below line is use to request permission in the current activity.
        // this method is use to handle error in runtime permissions
        Dexter.withContext(context) // below line is use to request the number of permissions which are required in our app.
            .withPermissions(
                Manifest.permission.CAMERA,  // below is the list of permissions
                Manifest.permission.RECORD_AUDIO,
            ) // after adding permissions we are calling an with listener method.
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(multiplePermissionsReport: MultiplePermissionsReport) {
                    // this method is called when all permissions are granted
                    if (multiplePermissionsReport.areAllPermissionsGranted()) {
                        // do you work now
                        // if permission granted, initialize the engine
                        val userData = SharedPrefs.getUserData(context)
                        val username = userData!!.username
                        val usertype = "DOCTOR"

                        ApiUtils.getAPIService(context).callstatus(
                            SharedPrefs.getString(context, "sessionid")!!,
                            username!!,
                            usertype,
                            PatientAppointment.appointmentId.toString(),
                            "Doctor has joined video session"
                        ).enqueue(object : Callback<ForgotPasswordResponse> {
                            override fun onFailure(
                                call: Call<ForgotPasswordResponse>, t: Throwable
                            ) {
                                //Do nothing
//                    documents_progress_bar.visibility = View.GONE
                                val intent = Intent(context, VideoSessionActivity::class.java)
                                //val intent = Intent(context
                                //, agkitActivity::class.java)
                                // val intent = Intent(context
                                //, VideoSessioActivityOld::class.java)
                                intent.putExtra(
                                    "url",
                                    "https://ezshifa.com/emr/restapi_admin/index.php/videocall?roomid=" + PatientAppointment.roomid
                                )
                                startActivity(intent)
                            }

                            override fun onResponse(
                                call: Call<ForgotPasswordResponse>,
                                response: Response<ForgotPasswordResponse>
                            ) {
//                    documents_progress_bar.visibility = View.GONE
                                val intent = Intent(context, VideoSessionActivity::class.java)
                                //  val intent = Intent(context
                                // , agkitActivity::class.java)
                                //  val intent = Intent(context
                                // , VideoSessioActivityOld::class.java)

                                intent.putExtra(
                                    "url",
                                    "https://ezshifa.com/emr/restapi_admin/index.php/videocall?roomid=" + PatientAppointment.roomid
                                )
                                startActivity(intent)

                            }
                        })
                    }
                    // check for permanent denial of any permission
                    if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied) {
                        // permission is denied permanently, we will show user a dialog message.
                        showSettingsDialog()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: MutableList<com.karumi.dexter.listener.PermissionRequest>?,
                    permissionToken: PermissionToken?
                ) {
                    // this method is called when user grants some permission and denies some of them.
                    permissionToken?.continuePermissionRequest()
                }
            }).withErrorListener { error ->
                // we are displaying a toast message for error message.
                Toast.makeText(
                    context,
                    "Error occurred! ",
                    Toast.LENGTH_SHORT
                ).show()
            } // below line is use to run the permissions on same thread and to check the permissions
            .onSameThread().check()
    }

    // below is the shoe setting dialog method which is use to display a dialogue message.
    private fun showSettingsDialog() {
        // we are displaying an alert dialog for permissions
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)

        // below line is the title for our alert dialog.
        builder.setTitle("Need Permissions")

        // below line is our message for our dialog
        builder.setMessage("This app needs Camera and Audio permission to use this feature. You can grant them in app settings.")
        builder.setPositiveButton("GOTO SETTINGS") { dialog, which ->
            // this method is called on click on positive button and on clicking shit button
            // we are redirecting our user from our app to the settings page of our app.
            dialog.cancel()
            // below is the intent from which we are redirecting our user.
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", context.packageName, null)
            intent.data = uri
            startActivityForResult(intent, 101)
        }
        builder.setNegativeButton("Cancel") { dialog, which ->
            // this method is called when user click on negative button.
            dialog.cancel()
        }
        // below line is used to display our dialog
        builder.show()
    }

    private fun getCurrentTime(): String {
        return try {
            val currentTime = Calendar.getInstance().time
            val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
            sdf.format(currentTime)
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    private fun shouldEnableButton(currentTime: String, targetTime: String): Boolean {
        val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        val targetDateTime = sdf.parse(targetTime)
        val currentDateTime = sdf.parse(currentTime)

        // Calculate the difference in milliseconds
        val differenceInMillis = targetDateTime.time - currentDateTime.time

        // Enable the button if the difference is less than or equal to 5 minutes (5 * 60 * 1000 milliseconds)
        return differenceInMillis <= (5 * 60 * 1000)
    }

    override fun onDestroy() {
        super.onDestroy()
        // Remove the callbacks to prevent memory leaks
        if (this::handler.isInitialized) {
            handler.removeCallbacks(enableButtonRunnable)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun checkDateAndTimeDifference(apptDate: String, apptTime: String): Boolean {
        var buttonEnableStatus: Boolean
        try {

            val dateTime = apptDate.plus("T" + apptTime)
            val specificDateTime = LocalDateTime.parse(dateTime)
            val currentDateTime = LocalDateTime.now()

            val difference = Duration.between(currentDateTime, specificDateTime)

            val days = difference.toDays()
            val hours = difference.toHours() % 24
            val minutes = difference.toMinutes() % 60
            // Check if it's the desired day, hour, and the remaining minutes are less than 5
            buttonEnableStatus = days <= 0 && hours.toInt() <= 0 && minutes <= 5
            Log.e("TAG", "Status ${PatientAppointment.apptstatus} Difference from current date and time:\n" + "$days days, $hours hours, $minutes minutes")
            return buttonEnableStatus
        } catch (e: Exception) {
            buttonEnableStatus = false
            e.printStackTrace()
            return buttonEnableStatus
        }

    }

}

package com.app.ehealthai.doctor.ui.activities.PatientDetails.ui

import android.content.Context
import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.app.ehealthai.doctor.rtm.ChatManager
import com.app.ehealthai.doctor.rtm.status.MessageType
import com.app.ehealthai.doctor.ui.activities.PatientDetails.PatientAppointment
import com.app.ehealthaidoctor.R
import com.app.ehealthaidoctor.models.responses.GetPatientDocumentsResponse
import com.app.ehealthaidoctor.network.ApiUtils
import com.app.ehealthaidoctor.ui.activities.HomeActivity
import com.app.ehealthaidoctor.ui.activities.PatientDocumentsActivity
import com.app.ehealthaidoctor.ui.activities.VideoSessionActivity
import com.app.ehealthaidoctor.ui.fragments.documents.LabReportsFragment
import com.app.ehealthaidoctor.ui.fragments.documents.MiscFragment
import com.app.ehealthaidoctor.ui.fragments.documents.XRayFragment
import com.app.ehealthaidoctor.utils.SharedPrefs
import com.example.ehealthai.MyApplication
import io.agora.rtm.ErrorInfo
import io.agora.rtm.ResultCallback
import io.agora.rtm.RtmClient
import io.agora.rtm.RtmClientListener
import io.agora.rtm.RtmMessage
import io.agora.rtm.RtmStatusCode
import kotlinx.android.synthetic.main.fragment_patient_reports.iv_home_patient_reports
import kotlinx.android.synthetic.main.fragment_patient_reports.pb_patient_reports
import kotlinx.android.synthetic.main.fragment_patient_reports.user_docs_tab
import kotlinx.android.synthetic.main.fragment_patient_reports.user_docs_view_pager
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PatientReportsFragment : Fragment() {

    companion object {
        fun newInstance() = PatientReportsFragment()
    }

    var patientId: String? = null
    private var mChatManager: ChatManager? = null
    private var mRtmListener: MyRtmClientListener? = null
    private var isRTMLogin = false
    private lateinit var patientActivityContext: PatientAppointment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        patientActivityContext = activity as PatientAppointment
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_patient_reports, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iv_home_patient_reports.setOnClickListener {
            val intent = Intent(patientActivityContext, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }
        getPatientDocuments()
        if (!isRTMLogin && PatientAppointment.doctorId != null && PatientAppointment.patientId != null) {
            realTimeMessaging()
        }
    }

    private fun getPatientDocuments() {
        val userData = SharedPrefs.getUserData(patientActivityContext)
        val username = userData!!.username
        val usertype = "DOCTOR"
        patientId = PatientAppointment.patientId
        try {
            ApiUtils.getAPIService(patientActivityContext).getPatientDocuments(
                patientId!!,
                SharedPrefs.getString(patientActivityContext, "sessionid")!!,
                username!!,
                usertype
            ).enqueue(object : Callback<GetPatientDocumentsResponse> {
                override fun onFailure(call: Call<GetPatientDocumentsResponse>, t: Throwable) {
                    pb_patient_reports?.visibility = View.GONE

                    Toast.makeText(
                        patientActivityContext,
                        "Something went wrong, please try again later",
                        Toast.LENGTH_LONG
                    ).show()
                }

                override fun onResponse(
                    call: Call<GetPatientDocumentsResponse>,
                    response: Response<GetPatientDocumentsResponse>
                ) {
                    pb_patient_reports?.visibility = View.GONE
                    sendStatusMessage(MessageType.VIEWING_PATIENT_REPORTS)
                    Handler(Looper.getMainLooper()).postDelayed(
                        {
                            sendStopStatusMsg(MessageType.VIEWING_PATIENT_REPORTS)
                        },
                        VideoSessionActivity.TIME_OUT
                    )
                    try {
                        if (response.isSuccessful && response.body() != null && response.body()!!.statuscode == 200 && response.body()!!.data.documents != null) {
                            PatientDocumentsActivity.labReportsList = ArrayList()
                            PatientDocumentsActivity.xRayList = ArrayList()
                            PatientDocumentsActivity.docNotesList = ArrayList()
                            PatientDocumentsActivity.miscList = ArrayList()

                            for (i in 0 until response.body()!!.data.documents!!.size) {

                                if (response.body()!!.data.documents!![i].doc_type.toLowerCase()
                                        .contains("lab_report")
                                ) {
                                    PatientDocumentsActivity.labReportsList.add(
                                        response.body()!!.data.documents!![i]
                                    )
                                } else if (response.body()!!.data.documents!![i].doc_type.toLowerCase()
                                        .contains("x_ray")
                                ) {
                                    PatientDocumentsActivity.xRayList.add(
                                        response.body()!!.data.documents!![i]
                                    )
                                } else if (response.body()!!.data.documents!![i].doc_type.toLowerCase()
                                        .contains("doc_notes")
                                ) {
                                    PatientDocumentsActivity.docNotesList.add(
                                        response.body()!!.data.documents!![i]
                                    )
                                } else {
                                    PatientDocumentsActivity.miscList.add(
                                        response.body()!!.data.documents!![i]
                                    )
                                }
                            }

                            setupViewpager()
                        } else {
                            try {
                                if (response.isSuccessful && response.body() != null && response.body()!!.statuscode == 404) {
                                    val data = response.body()!!.data
                                    if (data != null) {
                                        Toast.makeText(
                                            patientActivityContext,
                                            data.error,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }

                    } catch (e: Exception) {
                        Toast.makeText(
                            patientActivityContext,
                            "Something went wrong, please try again later",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            })
        } catch (e: Exception) {
            pb_patient_reports?.visibility = View.GONE
        }
    }

    private val mTimeSentLastMsg = HashMap<String, Long>()
    private var mRtmClient: RtmClient? = null

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
                                getString(R.string.send_msg_failed)
                            )

                            RtmStatusCode.PeerMessageError.PEER_MESSAGE_ERR_PEER_UNREACHABLE -> showsnackbar(
                                getString(R.string.peer_offline)
                            )

                            RtmStatusCode.PeerMessageError.PEER_MESSAGE_ERR_CACHED_BY_SERVER -> showsnackbar(
                                getString(R.string.message_cached)
                            )
                        }

                    }
                }
            })
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

    private fun setupViewpager() {
        user_docs_tab.setupWithViewPager(user_docs_view_pager)
        val adapter = UserDocumentsFragmentAdapter(requireFragmentManager(), patientActivityContext)
        user_docs_view_pager.adapter = adapter
        val linearLayout = user_docs_tab.getChildAt(0) as LinearLayout
        linearLayout.showDividers = LinearLayout.SHOW_DIVIDER_MIDDLE
        val drawable = GradientDrawable()
        drawable.setColor(this.resources.getColor(R.color.grey))
        drawable.setSize(3, 1)
        linearLayout.dividerPadding = 10
        linearLayout.dividerDrawable = drawable

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

    private fun parseIncommingMessage(message: RtmMessage, from: String) {
        activity?.runOnUiThread {
            if (message.rawMessage.size == 1 && from == PatientAppointment.patientId) {
                val type = message.text

                //showStatus(MessageType.fromValue(type))
            }
        }
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

    private fun doRTMLogin() {
        Log.e("RTM LOGIN DOC ID ", PatientAppointment.doctorId!!)
        mRtmClient?.login(null, PatientAppointment.doctorId, object : ResultCallback<Void?> {
            override fun onSuccess(responseInfo: Void?) {
                Log.i("", "login success")
                isRTMLogin = true
                sendStatusMessage(MessageType.VIEWING_PATIENT_REPORTS)
                Handler(Looper.getMainLooper()).postDelayed(
                    { sendStopStatusMsg(MessageType.VIEWING_PATIENT_REPORTS) },
                    VideoSessionActivity.TIME_OUT
                )
            }

            override fun onFailure(errorInfo: ErrorInfo) {
                Log.i("", "login failed: " + errorInfo.errorCode)
                isRTMLogin = false

            }
        })
    }

    inner class UserDocumentsFragmentAdapter(fm: FragmentManager, val context: Context) :
        FragmentStatePagerAdapter(fm) {
        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> {
                    LabReportsFragment()
                }

                1 -> {
                    XRayFragment()
                }

                2 -> {
                    MiscFragment()
                }

                3 -> {
                    MiscFragment()
                }

                else -> {
                    LabReportsFragment()
                }

            }
        }

        // override fun getCount(): Int = 4
        override fun getCount(): Int = 3

        override fun getPageTitle(position: Int): CharSequence? {
            return when (position) {
                0 -> context.resources.getString(R.string.LAB_REPORTS_VIEWPAGER)
                1 -> context.resources.getString(R.string.X_RAY_VIEWPAGER)
                2 -> context.resources.getString(R.string.MISC_VIEWPAGER)
                3 -> context.resources.getString(R.string.MISC_VIEWPAGER)
                else -> null
            }
        }
    }

    override fun onStop() {
        super.onStop()
        mChatManager?.unregisterListener(mRtmListener)
    }
}
package com.app.ehealthai.doctor.ui.activities

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.ehealthai.adapters.recyclerViewAdapters.VitalsHistoryRecyclerViewAdapter
import com.app.ehealthai.doctor.models.responses.GetVitalsResponse
import com.app.ehealthai.doctor.models.responses.VitalsData
import com.app.ehealthai.doctor.rtm.ChatManager
import com.app.ehealthai.doctor.rtm.status.MessageType
import com.app.ehealthaidoctor.R
import com.app.ehealthaidoctor.network.ApiUtils
import com.app.ehealthaidoctor.utils.SharedPrefs
import com.crashlytics.android.Crashlytics
//import com.crashlytics.android.Crashlytics
import com.example.ehealthai.MyApplication
import com.example.ehealthai.utils.toast
import io.agora.rtm.*
import io.fabric.sdk.android.Fabric
//import io.fabric.sdk.android.Fabric
import kotlinx.android.synthetic.main.vitals_history.*
import retrofit2.Call
import retrofit2.Response

class VideoSessionVitalsHistory : AppCompatActivity(){
    var vitalshistoryRecyclerViewAdapter: VitalsHistoryRecyclerViewAdapter? = null
    var appointmentId: String? = null
    var patientId: String? = null
    private var mRtmClient: RtmClient? = null
    private var mChatManager : ChatManager? = null
    private val mTimeSentLastMsg = HashMap<String,Long>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fabric.with(this, Crashlytics())
        setContentView(R.layout.vitals_history)
        /* appointmentId = intent.getStringExtra("appointmentId")*/
        appointmentId=""
        patientId = intent.getStringExtra("patientId")

        vitals_history_back_btn.setOnClickListener {
            onBackPressed()
        }
        vitalslistview.layoutManager = LinearLayoutManager(this@VideoSessionVitalsHistory)
        progress_load_vs.visibility = View.VISIBLE
        getVitalsHistory()
        mChatManager = (application as MyApplication).getChatManager()
        mRtmClient = mChatManager?.rtmClient

        sendStatusMessage(MessageType.WATCHING_VITAL_HISTORY)
    }
    fun getVitalsHistory() {
        val userData= SharedPrefs.getUserData(this)
        val username=userData!!.username
        val usertype="DOCTOR"
        ApiUtils.getAPIService(this@VideoSessionVitalsHistory).GetVitalsRequest(appointmentId!!,patientId!!, SharedPrefs.getString(this@VideoSessionVitalsHistory, "sessionid")!!,username!!,usertype)
            .enqueue(object : retrofit2.Callback<GetVitalsResponse> {
                override fun onFailure(call: Call<GetVitalsResponse>, t: Throwable) {
                    progress_load_vs.visibility = View.GONE
                    toast("Something went wrong, please try again later")
                }

                override fun onResponse(call: Call<GetVitalsResponse>, response: Response<GetVitalsResponse>) {
                    progress_load_vs.visibility = View.GONE
                    if (response.isSuccessful) {
                        val vitalsList: ArrayList<VitalsData> = ArrayList()
                        vitalsList.addAll(response.body()!!.data!!.vitals!!)
                        if(vitalsList.size==0)
                        {
                            emptytextvs.visibility= View.VISIBLE
                        }
                        else {
                            vitalshistoryRecyclerViewAdapter =
                                VitalsHistoryRecyclerViewAdapter(this@VideoSessionVitalsHistory,
                                    response.body()!!.data!!.vitals!! as ArrayList<VitalsData>,patientId!!)
                            vitalslistview.adapter = vitalshistoryRecyclerViewAdapter
                        }

                    } else {
                        toast("Something went wrong, please try again later")
                    }
                }
            })


    }
    private fun showToast(msg:String){
        Toast.makeText(this,msg, Toast.LENGTH_SHORT).show()
    }
    private fun sendPeerMessage(message: RtmMessage) {
        if(patientId==null){
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
                }
            })
    }
    private fun sendStatusMessage(messageType: MessageType) {

        val data = ByteArray(1)
        val statusMessage = mRtmClient!!.createMessage()
        statusMessage.setRawMessage(data, messageType.toString())
        mTimeSentLastMsg[messageType.toString()] = System.currentTimeMillis()
        sendPeerMessage(statusMessage)

    }

    override fun onStop() {
        super.onStop()
        sendStatusMessage(MessageType.STOP_STATUS)
    }

}
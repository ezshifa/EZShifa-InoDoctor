package com.app.ehealthaidoctor.ui.activities.Chat

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.ehealthai.doctor.utils.Global
import com.app.ehealthai.models.local.Chat
import com.app.ehealthai.network.firebase.FirebaseUtils
import com.app.ehealthaidoctor.R
import com.app.ehealthaidoctor.adapters.recyclerViewAdapters.ChatRecyclerViewAdapter
import com.app.ehealthaidoctor.models.responses.SendNotificationResponse
import com.app.ehealthaidoctor.network.ApiUtils
import com.app.ehealthaidoctor.utils.SharedPrefs
import kotlinx.android.synthetic.main.activity_chat.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class VideoSessionChat : AppCompatActivity() {

    var doctorId: String? = null
    var doctorName: String? = null
    var patientName: String? = null
    var doctorSpeciality: String? = null
    var chatId: String? = null
    var image_name: String? = null
    var image_name_p: String? = null
    var notify = false
    var list: ArrayList<Chat> = ArrayList()

    var chatRecyclerViewAdapter: ChatRecyclerViewAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        if (Global.IntentFromHandler){
            finish()
        }

        val params = window.attributes

        params.height = 1200



        this.getWindow().setAttributes(params)
        init()
    }

    companion object {
        var oldTimeStamp = ""
    }

    fun init() {


        val userData = SharedPrefs.getUserData(this@VideoSessionChat)
        doctorId = userData!!.id.toString()
        doctorName = userData.fname + " " + userData.lname
        doctorSpeciality = userData.specialty

        val patientId = intent.getStringExtra("patientId")
        patientName = intent.getStringExtra("patientName")
        image_name_p = intent.getStringExtra("image_name_p")
        chat_name.text = patientName

        val string = userData.image_name

        /* val index = string!!.indexOf("image/")

         val domain: String? = if (index == -1) null else string!!.substring(index + 6)*/
        image_name=string


        val linearLayoutManager = LinearLayoutManager(this@VideoSessionChat)
        chat_details_recycler_view.layoutManager = linearLayoutManager

        chat_back_btn.setOnClickListener { onBackPressed() }
        videoCallBtn.visibility= View.GONE
        videoCallBtn.setOnClickListener {

            notify = true

            FirebaseUtils.sendMessage(
                    doctorId!!,
                    patientId!!,
                    "The doctor has initiated the telemedicine video chat room.",
                    "text",
                    System.currentTimeMillis().toString(),
                    patientName!!,
                    doctorName!!,
                    doctorSpeciality!!,doctorId!!,image_name!!,image_name_p!!,
                    messageSent = {
                        sendNotification(
                                patientId, "Message from " + doctorName + ""
                        )
                        // newMessageEditText.setText("")
                    }
            )

//            val intent = Intent(this, VideoCallActivity::class.java)
//            intent.putExtra("roomId", chatId)
//            intent.putExtra("doctorId", doctorId)
//            intent.putExtra("patientId", patientId)
//            intent.putExtra("patientName", patientName)
//            startActivity(intent)
        }
        chatId = "chat_" + patientId + "_" + doctorId

        var firstTime = true
        FirebaseUtils.getChatMessages(chatId!!, returnChat = {
            list.clear()
            list.addAll(it)

            chatRecyclerViewAdapter = ChatRecyclerViewAdapter(this, doctorId!!, it)
            chat_details_recycler_view.adapter = chatRecyclerViewAdapter
            chat_details_recycler_view.scrollToPosition(it.size - 1)

            if (it.isNotEmpty() && firstTime) {
                firstTime = false
                if (!it.get(it.lastIndex).senderId.equals(doctorId!!)) {
                    FirebaseUtils.updateLastSeen(doctorId!!, patientId!!, chatId!!)
                } else if (it.isNotEmpty()) {
                    if (!it.get(it.lastIndex).senderId.equals(patientId)) {
                        FirebaseUtils.updateLastSeen(doctorId!!, patientId!!, chatId!!)
                    }
                }
            }
        })

        /*val intent = Intent(this, NotificationService::class.java)
        SharedPrefs.save(this, "patientId", patientId!!)
        SharedPrefs.save(this, "doctorId", doctorId!!)
        SharedPrefs.save(this, "chatId", chatId!!)
        startService(intent)*/

        sendBtn.setOnClickListener {
            notify = true
            if (!newMessageEditText.text.toString().isBlank() && newMessageEditText.text.toString().isNotEmpty()) {
                FirebaseUtils.sendMessage(
                        doctorId!!,
                        patientId!!,
                        newMessageEditText.text.toString(),
                        "text",
                        System.currentTimeMillis().toString(),
                        patientName!!,
                        doctorName!!,
                        doctorSpeciality!!,
                        doctorId!!,
                        image_name!!,
                        image_name_p!!,
                        messageSent = {
                            sendNotification(
                                    patientId!!, "Message from " + doctorName + " \"" + newMessageEditText.text.toString()+"\""
                            )
                            newMessageEditText.setText("")
                        }
                )

                chat_details_recycler_view.scrollToPosition(list.size - 1)


                //      }

                /*  if (notify) {
                      val patient = "p" + patientId
                      sendNotification(doctorName!!, patient, newMessageEditText.text.toString())
                  }*/

            }
        }

    }

    /* fun sendNotification(username: String, patientId: String, message: String) {
         notify = false
          val  doctorId = "d" + doctorId!!

         val data =
             Data(username, patientId, R.drawable.ehealth_ai_logo, username + ": " + message, "New Message", patientId)

         val sender = Sender(data, patientId)

         *//*ApiUtils.getFirebaseAPIService(this@ChatActivity).sendNotification(sender)
            .enqueue(object : retrofit2.Callback<NotificationSentResponse> {
                override fun onFailure(call: Call<NotificationSentResponse>, t: Throwable) {
                    //Do nothing
                }

                override fun onResponse(
                    call: Call<NotificationSentResponse>,
                    response: Response<NotificationSentResponse>
                ) {
                    //Do nothing for now
                }
            })*//*
    }*/


    fun sendNotification(patientId: String, message: String) {
        val userData= SharedPrefs.getUserData(this@VideoSessionChat)
        val username=userData!!.username
        val usertype="DOCTOR"
        ApiUtils.getAPIService(this@VideoSessionChat).sendNotification(patientId, message, SharedPrefs.getString(this@VideoSessionChat, "sessionid")!!,username!!,usertype)
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


package com.app.ehealthaidoctor.services

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.app.ehealthai.models.local.Chat
import com.app.ehealthai.network.firebase.FirebaseUtils
import com.app.ehealthaidoctor.R
import com.app.ehealthaidoctor.ui.activities.Chat.ChatActivity
import com.app.ehealthaidoctor.utils.SharedPrefs

class NotificationService : Service() {

    var patientId: String = ""
    var doctorId: String = ""
    var chatId: String = ""

    override fun onBind(intent: Intent?): IBinder? {

        FirebaseUtils.listenToChat(patientId, doctorId, chatId, fireNotification = {
            val chat = it.getValue(Chat::class.java)
            if (!chat!!.senderId.equals(doctorId) && !ChatActivity.oldTimeStamp.equals(chat.timeStamp))
                fireNotification(chat.message, chat.senderId, chat.timeStamp)
        })

        return null
    }


    fun fireNotification(message: String, sender: String, timeStamp: String) {
        ChatActivity.oldTimeStamp = timeStamp


        val user = sender
        val title = "New Message"

        val intent = Intent(this, ChatActivity::class.java)
        intent.putExtra("doctorId", doctorId)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(this, 1, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE)
        val notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val builder = NotificationCompat.Builder(this)
            .setSmallIcon(R.drawable.ehealth_ai_logo)
            .setContentTitle(title)
            .setContentText(user + ": " + message)
            .setAutoCancel(true)
            .setSound(notificationSound)
            .setContentIntent(pendingIntent)


        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.notify(1, builder.build())


    }

    override fun onStart(intent: Intent?, startId: Int) {
        super.onStart(intent, startId)

        patientId = SharedPrefs.getString(applicationContext, "patientId")!!
        doctorId = SharedPrefs.getString(applicationContext, "doctorId")!!
        chatId = SharedPrefs.getString(applicationContext, "chatId")!!

        FirebaseUtils.listenToChat(patientId, doctorId, chatId, fireNotification = {
            val chat = it.getValue(Chat::class.java)
            if (!chat!!.senderId.equals(patientId) && !ChatActivity.oldTimeStamp.equals(chat.timeStamp))
                fireNotification(chat.message, chat.senderId, chat.timeStamp)
        })
    }


}
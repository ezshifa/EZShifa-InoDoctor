package com.app.ehealthai.doctor.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.app.ehealthaidoctor.R


class MediaProjectionService : Service() {

    companion object {
        private const val NOTIFICATION_ID = 123
        private const val CHANNEL_ID = "media_projection_channel_id"
        private const val CHANNEL_NAME = "Monitoring Call Notification"
    }


    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, createNotification())
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
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
        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Monitoring Call")
            .setSmallIcon(R.drawable.smallappicon)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)

        return notificationBuilder.build()
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        Log.e("TAG", "onTaskRemoved: ")
        super.onTaskRemoved(rootIntent)
        this.stopSelf()
    }
}


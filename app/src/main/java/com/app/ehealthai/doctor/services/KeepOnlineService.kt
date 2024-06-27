package com.app.ehealthai.doctor.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Handler
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.app.ehealthai.doctor.models.responses.ForgotPasswordResponse
import com.app.ehealthaidoctor.R
import com.app.ehealthaidoctor.network.ApiUtils
import com.app.ehealthaidoctor.ui.activities.splashScreen
import com.app.ehealthaidoctor.utils.SharedPrefs
import com.example.ehealthai.utils.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class KeepOnlineService : Service() {
    private val CHANNEL_ID = "ForegroundService Kotlin"
    var handler: Handler = Handler()
    var runnable: Runnable? = null
    var delay = 120000

    companion object {
        fun startService(context: Context, message: String) {
            // Toast.makeText(context, "START SERVICE", Toast.LENGTH_SHORT).show()
            val startIntent = Intent(context, KeepOnlineService::class.java)
            startIntent.putExtra("inputExtra", message)
            ContextCompat.startForegroundService(context, startIntent)
        }

        fun stopService(context: Context) {
            val stopIntent = Intent(context, KeepOnlineService::class.java)
            context.stopService(stopIntent)
        }
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Toast.makeText(this, "onStartCommand", Toast.LENGTH_SHORT).show()
        //do heavy work on a background thread
        val input = intent?.getStringExtra("inputExtra")
        createNotificationChannel()
        val notificationIntent = Intent(this, splashScreen::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("You are Online")
            .setContentText(input)
            .setSmallIcon(R.drawable.notif_logo)
            .setColor(ContextCompat.getColor(this, R.color.colorPrimary))
            .setContentIntent(pendingIntent)
            .build()

        startForeground(121, notification)
        handler.postDelayed(Runnable {
            handler.postDelayed(runnable!!, delay.toLong())
            changeoofstatus()
        }.also {
            runnable = it
        },
            delay.toLong()
        )
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(runnable!!)
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID, "Online Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager!!.createNotificationChannel(serviceChannel)
        }
    }

    private fun changeoofstatus() {
        try {
            val userData = SharedPrefs.getUserData(this)
            if (userData != null) {
                val username = userData.username
                val usertype = "DOCTOR"
                var appversion: String = ""
                val version: String = android.os.Build.VERSION.SDK
                val device: String = android.os.Build.DEVICE
                val model: String = android.os.Build.MODEL
                val prod: String = android.os.Build.PRODUCT
                val devicedetail: String = device + model + prod + device + version
                try {
                    val pInfo = packageManager.getPackageInfo(packageName, 0)
                    appversion = pInfo.versionName
                } catch (e: PackageManager.NameNotFoundException) {
                    e.printStackTrace()
                }
                ApiUtils.getAPIService(this).update_doctor_onoff_status(
                    SharedPrefs.getString(this, "sessionid")!!,
                    username!!,
                    usertype,
                    userData.id!!.toString(),
                    "1",
                    ""
                )
                    .enqueue(object : Callback<ForgotPasswordResponse> {
                        override fun onFailure(call: Call<ForgotPasswordResponse>, t: Throwable) {
                            //Do nothing for now
                        }

                        override fun onResponse(
                            call: Call<ForgotPasswordResponse>,
                            response: Response<ForgotPasswordResponse>
                        ) {
                            try {
                                if (response.isSuccessful) {
                                }
                            } catch (e: Exception) {
                                toast("Error: Try again")
                            }
                        }
                    })
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

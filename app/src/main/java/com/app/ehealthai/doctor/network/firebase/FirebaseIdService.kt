package com.app.ehealthaidoctor.network.firebase

import android.app.ActivityManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.app.ehealthai.doctor.models.responses.ForgotPasswordResponse
import com.app.ehealthai.doctor.services.KeepOnlineService
import com.app.ehealthai.doctor.ui.activities.PatientDetails.PatientAppointment
import com.app.ehealthai.doctor.utils.TinyDB
import com.app.ehealthaidoctor.R
import com.app.ehealthaidoctor.models.responses.GetAppointmentDoctorList
import com.app.ehealthaidoctor.network.ApiUtils
import com.app.ehealthaidoctor.ui.activities.Chat.ChatActivity
import com.app.ehealthaidoctor.ui.activities.HomeActivity
import com.app.ehealthaidoctor.ui.activities.LoginActivity
import com.app.ehealthaidoctor.utils.SharedPrefs
import com.example.ehealthai.utils.Constants
import com.example.ehealthai.utils.toast
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date


class FirebaseIdService : FirebaseMessagingService() {

    override fun onNewToken(token: String?) {
        SharedPrefs.save(applicationContext, "Token", token!!)
        SharedPrefs.save(applicationContext, "TOKEN_REFRESHED", false)
        super.onNewToken(token)
    }

    override fun onMessageReceived(message: RemoteMessage?) {
        super.onMessageReceived(message)
        var iscallnowappt = false
        var intent: Intent? = null;
        try {

            val msgbody = message?.data?.get("body").toString().split(",")
            var apptid = msgbody[1]
            Log.e("Appointment ID ", apptid)
            if (!apptid.equals("")) {
                iscallnowappt = true
                Log.e("Appointment ID ", apptid)
                intent = Intent(this, PatientAppointment::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//                intent.putExtra("FromNotification",true)
//                intent.putExtra("PatientAppointmentStatus", PatientAppointment.apptstatus)
                val userData = SharedPrefs.getUserData(this)
                ApiUtils.getAPIService(this).get_appointment_byapptid(apptid)
                    .enqueue(object : Callback<GetAppointmentDoctorList> {
                        override fun onFailure(call: Call<GetAppointmentDoctorList>, t: Throwable) {
                            //Do nothing for now
                            //progressDialog!!.dismiss()
                        }

                        override fun onResponse(
                            call: Call<GetAppointmentDoctorList>,
                            response: Response<GetAppointmentDoctorList>
                        ) {
                            try {
                                if (response.isSuccessful) {

                                    if (response.body()?.data?.list?.size!! > 0) {
                                        var patientId = response.body()?.data?.list?.get(0)?.id
                                        var age = response.body()?.data?.list?.get(0)?.age
                                        var apptstatus =
                                            response.body()?.data?.list?.get(0)?.apptstatus
                                        var description =
                                            response.body()?.data?.list?.get(0)?.description
                                        var doctor_notes =
                                            response.body()?.data?.list?.get(0)?.doctor_notes
                                        var email = response.body()?.data?.list?.get(0)?.email
                                        var patientName =
                                            response.body()?.data?.list?.get(0)?.fname + " " + response.body()?.data?.list?.get(
                                                0
                                            )?.lname
                                        var profile_image =
                                            response.body()?.data?.list?.get(0)?.profile_image
                                        var roomid = response.body()?.data?.list?.get(0)?.roomid
                                        var sex = response.body()?.data?.list?.get(0)?.sex
                                        var appointmentId = apptid
                                        var DOB = response.body()?.data?.list?.get(0)?.DOB

                                        PatientAppointment.patientId = patientId.toString()
                                        PatientAppointment.patientName = patientName
                                        PatientAppointment.appointmentId = appointmentId
                                        PatientAppointment.description = description
                                        PatientAppointment.doctorId = userData!!.id.toString()
                                        PatientAppointment.apptstatus = apptstatus
                                        PatientAppointment.apptTime =
                                            response.body()?.data?.list?.get(0)?.appttime
                                        PatientAppointment.doc_notes = doctor_notes
                                        PatientAppointment.email = email
                                        PatientAppointment.roomid = roomid!!
                                        PatientAppointment.age = age
                                        PatientAppointment.sex = sex
                                        PatientAppointment.profile_image = profile_image
                                        PatientAppointment.studentFatherPhone =
                                            response.body()?.data?.list?.get(0)?.phone_cell
                                        PatientAppointment.registeredFrom =
                                            response.body()?.data?.list?.get(0)?.registered_from
                                        PatientAppointment.studentCNIC =
                                            response.body()?.data?.list?.get(0)?.cnic
                                        //  val intent = Intent(context, AppointmentDetailsActivity::class.java)
                                        intent!!.putExtra("patientId", patientId)
                                        intent!!.putExtra("appointmentId", appointmentId)
                                        intent!!.putExtra("description", description)
                                        intent!!.putExtra("doctorId", userData.id.toString())
                                        intent!!.putExtra("patientName", patientName)
                                        intent!!.putExtra("degrees", "")
                                        intent!!.putExtra("doctor_notes", doctor_notes)
                                        intent!!.putExtra("apptstatus", apptstatus)
                                        Log.e("online doc notes", doctor_notes!!)
                                        val room = "room" + "1"
                                        intent!!.putExtra("room", room)
                                        intent!!.putExtra("email", email)
                                        intent!!.putExtra("profileimage", profile_image)
                                        intent!!.putExtra("dob", DOB)
                                        intent!!.putExtra("roomid", roomid)
                                        intent!!.putExtra("age", age)
                                        intent!!.putExtra("sex", sex)
                                    }
                                }


                            } catch (e: Exception) {
                                e.printStackTrace()
                                //toast("Error: Try again")
                            }
                        }
                    })

            } else {
                intent = Intent(this, HomeActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            }
        } catch (e: Exception) {
            e.printStackTrace()

        }
        /*val sent = message!!.data.get("sent")
        val user = FirebaseAuth.getInstance().currentUser

      //  if (user != null && sent.equals(user.uid)) {
            fireNotification(message)
      // }*/
        Log.e("recived message doctor ", message?.data?.get("body").toString())
        if (message?.data?.get("body").toString().contains("Message from")) {
            var notifcount: Int = 0
            try {
                notifcount = SharedPrefs.getInt(applicationContext, "notifavailable")
            } catch (e: Exception) {
            }
            notifcount++
            Log.e("notifavailable ", notifcount.toString())
            SharedPrefs.save(applicationContext, "notifavailable", notifcount)
        }
        if ((message?.data?.get("body").toString())?.contains("call Ending...")) {
            val intent = Intent("callEnd")
            intent.putExtra("end", "call")
            Log.e("test", "call end")
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
        } else if ((message?.data?.get("body").toString())?.contains("call Ringing...")) {
            val intent = Intent("callEnd")
            intent.putExtra("end", "ringing")
            Log.e("test", "call ringing")
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
        } else {
            if (!(message?.data?.get("body").toString()).contains("is calling")) {
                try {
                    val commasep = message?.data?.get("body").toString().split(",")
                    var kioskname = commasep[2]
                    val bodystring = message?.data?.get("body").toString().split("@#@")
                    var gooffline = false
                    if (bodystring[0].equals("Offline")) {
                        gooffline = true
                    }
                    var isnormal = false
                    if (bodystring[0].equals("Normal")) {
                        isnormal = true
                    }
                    if (!gooffline) {
                        val tinydb = TinyDB(applicationContext)
                        var currentdate: String = ""
                        try {
                            val sdf = SimpleDateFormat("dd/MM/yyyy hh:mm:ss")
                            currentdate = sdf.format(Date()).toString()

                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                        if (tinydb.getListString("notifications").isEmpty()) {
                            var list: java.util.ArrayList<String> = java.util.ArrayList()
                            list.add(
                                message?.data?.get("title").toString() + "#@#" + message?.data?.get(
                                    "body"
                                )
                                    .toString() + "#@#" + currentdate + "#@#" + bodystring[0].toString() + "#@#" + kioskname
                            )
                            tinydb.putListString("notifications", list);
                        } else {
                            var list: java.util.ArrayList<String> = java.util.ArrayList()
                            for (item in tinydb.getListString("notifications")) {
                                // body of loop
                                list.add(item)
                            }
                            list.add(
                                message?.data?.get("title").toString() + "#@#" + message?.data?.get(
                                    "body"
                                )
                                    .toString() + "#@#" + currentdate + "#@#" + bodystring[0].toString() + "#@#" + kioskname
                            )
                            tinydb.putListString("notifications", list);


                        }
                        var notifcount: Int = 0
                        try {
                            notifcount = SharedPrefs.getInt(applicationContext, "notifcount")
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        notifcount = notifcount + 1
                        SharedPrefs.save(applicationContext, "notifcount", notifcount)
                        Log.e("Notification", "From : " + message?.getFrom());
                        Log.e("Notification", "Data : " + message?.getData().toString());
                    }
                    //val intent = Intent(this, HomeActivity::class.java)
                    if (isnormal) {
                    } else {
                        intent!!.putExtra("notiftitle", message?.data?.get("title").toString())
                        intent!!.putExtra("notifbody", bodystring[1])
                        intent!!.putExtra("notifimage", bodystring[0])
                    }
                    intent!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

                    val pendingIntent = PendingIntent.getActivity(
                        this,
                        9999,
                        intent,
                        PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
                    )
                    val notificationSound =
                        RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                    val vibratePattern = longArrayOf(0, 100, 200, 300)
                    val notificationManager =
                        this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

                    val notificationId = 1
                    val channelId = "channel-01"
                    val channelName = "Channel Name"
                    val importance = NotificationManager.IMPORTANCE_HIGH

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        val mChannel = NotificationChannel(
                            channelId, channelName, importance
                        )
                        mChannel.setLightColor(Color.GRAY)
                        mChannel.enableLights(true)

                        val audioAttributes = AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                            .build()
                        mChannel.setSound(
                            Uri.parse(
                                ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + applicationContext.getPackageName() + "/" + R.raw.docnotifalert
                            ), audioAttributes
                        )
                        notificationManager.createNotificationChannel(mChannel)
                    }

                    val mBuilder = NotificationCompat.Builder(this, channelId)
//                        .setSmallIcon(R.drawable.smallappicon)
                        .setSmallIcon(R.drawable.notif_logo)
                        .setColor(ContextCompat.getColor(this, R.color.colorPrimary))
                        .setBadgeIconType(NotificationCompat.BADGE_ICON_LARGE)
                        .setContentTitle(message?.data?.get("title").toString())
                        .setContentText(bodystring[1])
                        .setCategory(NotificationCompat.CATEGORY_ALARM)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                        .setVibrate(vibratePattern)
                        .setDefaults(Notification.DEFAULT_LIGHTS)
                        .setSound(
                            Uri.parse(
                                ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + applicationContext.packageName + "/" + R.raw.docnotifalert
                            ), AudioManager.STREAM_ALARM
                        )
                        .setAutoCancel(true)

                    val stackBuilder: TaskStackBuilder = TaskStackBuilder.create(this)
                    stackBuilder.addNextIntent(intent)
                    val resultPendingIntent: PendingIntent = stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                    )
                    mBuilder.setContentIntent(resultPendingIntent)

                    if (gooffline) {
                        KeepOnlineService.stopService(this)
                        changeoofstatustooffline(this, "Offline By Customer Support")

                    } else {
                        notificationManager.notify(notificationId, mBuilder.build())
                        playAudio()
                    }
                } catch (e: Exception) {
                    //val intent = Intent(this, HomeActivity::class.java)
                    if (iscallnowappt) {
                        intent = Intent(this, PatientAppointment::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    } else {
                        intent = Intent(this, HomeActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    }

                    val notificationSound =
                        RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

                    val notificationManager =
                        this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

                    val notificationId = 1
                    val channelId = "channel-01"
                    val channelName = "Channel Name"
                    val importance = NotificationManager.IMPORTANCE_HIGH

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        val mChannel = NotificationChannel(
                            channelId, channelName, importance
                        )
                        notificationManager.createNotificationChannel(mChannel)
                    }

                    val mBuilder = NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.doctor_icon)
                        .setBadgeIconType(NotificationCompat.BADGE_ICON_LARGE)
                        .setContentTitle(message?.data?.get("title").toString())
                        .setContentText(message?.data?.get("body").toString())
                        .setAutoCancel(true)

                    val stackBuilder: TaskStackBuilder = TaskStackBuilder.create(this)
                    stackBuilder.addNextIntent(intent)
                    val resultPendingIntent: PendingIntent = stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                    )
                    mBuilder.setContentIntent(resultPendingIntent)

                    //notificationManager.notify(notificationId, mBuilder.build())
                }
            }
        }

    }

    fun fireNotification(message: RemoteMessage) {

        val user = message.data.get("user")
        val icon = message.data.get("icon")
        val body = message.data.get("body")
        val title = message.data.get("title")
        val doctorId = message.data.get("doctorId")

        val notification = message.notification

        val j = Integer.parseInt(user!!.replace("[\\D]", ""))
        val intent = Intent(this, ChatActivity::class.java)
        intent.putExtra("doctorId", doctorId)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(
            this,
            j,
            intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )
        val notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val builder = NotificationCompat.Builder(this)
            // .setSmallIcon(Integer.parseInt(icon))
            .setContentTitle("Hello")
            .setContentText("Hello")
            .setAutoCancel(true)
            .setSound(notificationSound)
            .setContentIntent(pendingIntent)


        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        var i = 0

        if (j > 0) {
            i = j
        }

        notificationManager.notify(i, builder.build())


    }

    private fun playAudio() {
        val audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3"
        val mediaPath = Uri.parse("android.resource://" + packageName + "/" + R.raw.docnotifalert)
        // initializing media player
        var mediaPlayer = MediaPlayer()

        // below line is use to set the audio
        // stream type for our media player.
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)

        // below line is use to set our
        // url to our media player.
        try {
            mediaPlayer.setDataSource(getApplicationContext(), mediaPath)
            // below line is use to prepare
            // and start our media player.
            mediaPlayer.prepare()
            mediaPlayer.start()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    fun changeoofstatustooffline(con: Context, reason: String) {
        val userData = SharedPrefs.getUserData(this)
        val username = userData!!.username
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
            "0",
            reason
        )
            .enqueue(object : Callback<ForgotPasswordResponse> {
                override fun onFailure(call: Call<ForgotPasswordResponse>, t: Throwable) {
                    //Do nothing for now
                    //progressDialog!!.dismiss()
                }

                override fun onResponse(
                    call: Call<ForgotPasswordResponse>,
                    response: Response<ForgotPasswordResponse>
                ) {
                    try {
                        if (response.isSuccessful) {
                            //progressDialog!!.dismiss()
                            //onofftoggle.setText("Go Online")
                            //onofftoggle.setImageResource(R.drawable.iamoffline);
                            //servicerunning=false
                            //oof_image_btn.setBackgroundResource(R.drawable.offlineico)
                            if (isAppOnForeground(con, "com.app.ehealthai.doctor")) {
                                SharedPrefs.removeUserData(con)
                                SharedPrefs.removeKey(con, Constants.USERNAME)
                                SharedPrefs.removeKey(con, Constants.PASSWORD)
                                SharedPrefs.save(con, "TOKEN_REFRESHED", false)
                                val intent = Intent(con, LoginActivity::class.java)
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent)
                            }
                        }
                    } catch (e: Exception) {
                        toast("Error: Try again")
                    }
                }
            })
    }

    private fun isAppOnForeground(context: Context, appPackageName: String): Boolean {
        val activityManager = context.getSystemService(ACTIVITY_SERVICE) as ActivityManager
        val appProcesses = activityManager.runningAppProcesses ?: return false
        for (appProcess in appProcesses) {
            if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.processName == appPackageName) {
                //                Log.e("app",appPackageName);
                return true
            }
        }
        return false
    }
}
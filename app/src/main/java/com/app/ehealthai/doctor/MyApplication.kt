package com.example.ehealthai

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.multidex.MultiDex
import com.app.ehealthai.doctor.rtm.ChatManager
import com.google.firebase.FirebaseApp

class MyApplication : Application() {
    private var mChatManager: ChatManager? = null
    companion object{
        var remoteViewsList:ArrayList<Int> = ArrayList()
        var remoteUser:HashMap<Boolean,Int> = HashMap()
        var isPatientVideoShown=false
        var isFirstCapture=true
        var videoSwappedWithUid: Int = 0
        var videosSwapped = false
        var remoteUserVideoEnabled: Boolean = false

        lateinit var context: Context
    }

    private  val TAG = "MyApplication"
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        context = applicationContext
        mChatManager = ChatManager(this)
        mChatManager?.init()
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
    fun getChatManager(): ChatManager? {
        return mChatManager
    }


}
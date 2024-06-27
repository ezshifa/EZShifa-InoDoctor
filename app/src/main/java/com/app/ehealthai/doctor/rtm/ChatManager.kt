package com.app.ehealthai.doctor.rtm

import android.content.Context
import android.util.Log
import androidx.multidex.BuildConfig

import com.app.ehealthaidoctor.R
import io.agora.rtm.*

class ChatManager(private val mContext: Context) {
    var rtmClient: RtmClient? = null
        private set
    var sendMessageOptions: SendMessageOptions? = null
        private set
    private val mListenerList: MutableList<RtmClientListener> = ArrayList()
    private val mMessagePool = RtmMessagePool()
    fun init() {
        val appID = mContext.getString(R.string.agora_app_id)
        try {
            rtmClient = RtmClient.createInstance(mContext, appID, object : RtmClientListener {
                override fun onConnectionStateChanged(state: Int, reason: Int) {
                    for (listener in mListenerList) {
                        listener.onConnectionStateChanged(state, reason)
                    }
                }
                override fun onMessageReceived(rtmMessage: RtmMessage, peerId: String) {
                    if (mListenerList.isEmpty()) {
                        // If currently there is no callback to handle this
                        // message, this message is unread yet. Here we also
                        // take it as an offline message.
                        mMessagePool.insertOfflineMessage(rtmMessage, peerId)
                    } else {
                        for (listener in mListenerList) {
                            listener.onMessageReceived(rtmMessage, peerId)
                        }
                    }
                }

                override fun onTokenExpired() {}
                override fun onTokenPrivilegeWillExpire() {}

                override fun onPeersOnlineStatusChanged(status: Map<String, Int>) {}
            })
            if (BuildConfig.DEBUG) {
                rtmClient?.setParameters("{\"rtm.log_filter\": 65535}")
            }
        } catch (e: Exception) {
            Log.e(TAG, Log.getStackTraceString(e))
            throw RuntimeException(
                """
    NEED TO check rtm sdk init fatal error
    ${Log.getStackTraceString(e)}
    """.trimIndent()
            )
        }

        // Global option, mainly used to determine whether
        // to support offline messages now.
        sendMessageOptions = SendMessageOptions()
    }

    fun registerListener(listener: RtmClientListener) {
        mListenerList.add(listener)
    }

    fun unregisterListener(listener: RtmClientListener?) {
        mListenerList.remove(listener)
    }

    fun enableOfflineMessage(enabled: Boolean) {
   //     sendMessageOptions!!.enableOfflineMessaging = enabled
    }

    //    get() = sendMessageOptions!!.enableOfflineMessaging

    fun getAllOfflineMessages(peerId: String?): List<RtmMessage>? {
        return mMessagePool.getAllOfflineMessages(peerId)
    }

    fun removeAllOfflineMessages(peerId: String?) {
        mMessagePool.removeAllOfflineMessages(peerId)
    }

    companion object {
        private val TAG = ChatManager::class.java.simpleName

    }
}
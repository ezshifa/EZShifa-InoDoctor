package com.app.ehealthai.network.firebase


import android.util.Log
import com.app.ehealthai.models.local.Chat
import com.app.ehealthai.models.local.User
import com.app.ehealthaidoctor.ui.activities.Chat.ChatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class FirebaseUtils {

    companion object {

        fun sendMessage(
            sender: String,
            receiver: String,
            message: String,
            isTextOrImage: String,
            timeStamp: String,
            patientName: String,
            doctorName: String,
            doctorSpeciality: String,
            doctorId: String,
            image_name: String,
            image_name_p: String,
            messageSent: () -> Unit
        ) {

            //Save in Chats
            val map: HashMap<String, Any> = HashMap()
            map.put("isTextOrImage", isTextOrImage)
            map.put("message", message)
            map.put("senderId", sender)
            map.put("timeStamp", timeStamp)
            map.put("image_name", image_name)



            val chatName = "chat_" + receiver + "_" + sender
            FirebaseDatabase.getInstance().getReference("Chats").child(chatName).push().setValue(map)


            //Save in Users
            map.put("lastSeen", "false")
            map.put("patientName", patientName)
            map.put("doctorName", doctorName)
            map.put("doctorSpeciality", doctorSpeciality)
            map.put("doctorId", doctorId)
            if(!image_name_p.equals("")) {
                map.put("image_name_p", image_name_p)
            }
            //Sender
            val doctor = "d" + sender
            FirebaseDatabase.getInstance().getReference("Users").child(doctor).child("chats").child(chatName)
                .updateChildren(map)


            //Receiver
            val patient = "p" + receiver
            //   map.remove("timeStamp")
            FirebaseDatabase.getInstance().getReference("Users").child(patient).child("chats").child(chatName)
                .updateChildren(map)


            updateLastSeen(sender, receiver, chatName)
            messageSent()
        }


        fun getUserChats(userId: String, returnChats: (list: HashMap<String, User>) -> Unit) {
            var newUserData: Iterable<DataSnapshot>? = null
            val list: HashMap<String, User> = HashMap()
            val user = "d" + userId

            FirebaseDatabase.getInstance().getReference("Users").child(user).child("chats")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {
                        return
                    }

                    override fun onDataChange(data: DataSnapshot) {


                        newUserData = data.children

                        val map: HashMap<String, String> = HashMap()
                        Log.d("chatData", newUserData.toString())

                        newUserData!!.forEach {

                            val chatData = it.value as HashMap<String, String>
                            if (!chatData.get("image_name_p").isNullOrEmpty()) {
                                val user = User(
                                        chatData.get("isTextOrImage")!!,
                                        chatData.get("message")!!,
                                        chatData.get("lastSeen")!!,
                                        chatData.get("senderId")!!,
                                        chatData.get("timeStamp")!!,
                                        chatData.get("patientName")!!,
//                                        chatData.get("doctorName")!!,
                                        chatData.get("doctorSpeciality")!!,
                                        chatData.get("doctorId")!!
                                        , chatData.get("image_name_p")!!

                                )
                                list.put(it.key!!, user)
                            }
                            else{
                                    val user = User(
                                            chatData.get("isTextOrImage")!!,
                                            chatData.get("message")!!,
                                            chatData.get("lastSeen")!!,
                                            chatData.get("senderId")!!,
                                            chatData.get("timeStamp")!!,
                                            chatData.get("patientName")!!,
                                            chatData.get("doctorName")!!,
                                            chatData.get("doctorSpeciality")!!,
                                            chatData.get("doctorId")!!
                                            , ""

                                    )
                                list.put(it.key!!, user)
                        }


                        }
                        returnChats(list)
                    }
                })
        }

        fun getChatMessages(chatId: String, returnChat: (list: ArrayList<Chat>) -> Unit) {
            val chatList: ArrayList<Chat> = ArrayList()

            FirebaseDatabase.getInstance().getReference("Chats").child(chatId)
                .addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        chatList.clear()

                        for (chat in dataSnapshot.children) {
                            chatList.add(chat.getValue(Chat::class.java)!!)
                        }
                        returnChat(chatList)
                        //TODO: Update Last Seen
                    }
                })
        }

        fun updateLastSeen(userId: String, patientId: String, chatId: String) {
            val lastSeenMap: HashMap<String, Any> = HashMap()
            var newUserData: Iterable<DataSnapshot>? = null
            val user = "d" + userId

            FirebaseDatabase.getInstance().getReference("Users").child(user).child("chats").child(chatId)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {
                        return
                    }

                    override fun onDataChange(data: DataSnapshot) {
                        newUserData = data.children
                        val chatData = data.getValue(Chat::class.java)

                        lastSeenMap.put("lastSeen", "true")
                        ChatActivity.oldTimeStamp = System.currentTimeMillis().toString()
                        lastSeenMap.put("timeStamp", ChatActivity.oldTimeStamp)


                        FirebaseDatabase.getInstance().getReference("Users").child(user).child("chats")
                            .child(chatId)
                            .updateChildren(lastSeenMap)


                        if (userId != chatData!!.senderId) {
                            val patient = "p" + patientId
                            lastSeenMap.remove("timeStamp")

                            FirebaseDatabase.getInstance().getReference("Users").child(patient).child("chats")
                                .child(chatId)
                                .updateChildren(lastSeenMap)
                        }


                    }

                })

        }

        fun listenToChat(userId: String, doctorId: String, chatId: String, fireNotification: (data: DataSnapshot) -> Unit) {
            val patient = "p" + userId
            FirebaseDatabase.getInstance().getReference("Users").child(patient).child("chats").child(chatId)
                .addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        fireNotification(p0)
                    }
                })
        }

        fun updateDoctorName(doctorId: String, patientId: String, doctorName: String) {
            val updateNameMap: HashMap<String, Any> = HashMap()
            val user = "d" + doctorId
            val chatId = "chat_" + patientId + "_" + doctorId

            FirebaseDatabase.getInstance().getReference("Users").child(user).child("chats").child(chatId)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {
                        return
                    }

                    override fun onDataChange(data: DataSnapshot) {

                        updateNameMap.put("doctorName", doctorName)

                        FirebaseDatabase.getInstance().getReference("Users").child(user).child("chats")
                            .child(chatId)
                            .updateChildren(updateNameMap)


                        val patient = "p" + patientId

                        FirebaseDatabase.getInstance().getReference("Users").child(patient).child("chats")
                            .child(chatId)
                            .updateChildren(updateNameMap)


                    }

                })
        }
    }
}
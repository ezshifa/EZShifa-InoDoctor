package com.app.ehealthaidoctor.ui.activities.Chat

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.ehealthai.models.local.User
import com.app.ehealthai.network.firebase.FirebaseUtils
import com.app.ehealthaidoctor.R
import com.app.ehealthaidoctor.adapters.recyclerViewAdapters.UserChatsRecyclerViewAdapter
import com.app.ehealthaidoctor.utils.SharedPrefs
import kotlinx.android.synthetic.main.activity_user_chats.*

class UserChatsActivity : AppCompatActivity() {

    var userChatsRecyclerViewAdapter: UserChatsRecyclerViewAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_chats)
    }

    override fun onResume() {
        super.onResume()

        list.clear()
        keyList.clear()
        photoList.clear()
        init()
    }

    val list: ArrayList<User> = ArrayList()
    val keyList: ArrayList<String> = ArrayList()
    val photoList: ArrayList<String> = ArrayList()


    fun init() {
        userChatsRecyclerView.layoutManager = LinearLayoutManager(this@UserChatsActivity)
        user_chats_back_btn.setOnClickListener { onBackPressed() }
        add_chat_btn.setOnClickListener { startActivity(Intent(this, AddNewChatActivity::class.java)) }
        addchat.setOnClickListener { startActivity(Intent(this, AddNewChatActivity::class.java)) }
        val userData = SharedPrefs.getUserData(this)
        var ids=""

        FirebaseUtils.getUserChats(userData!!.id.toString(), returnChats = {
            if (it.isNotEmpty()) {
                addchat.visibility = View.INVISIBLE
//                val list: ArrayList<User> = ArrayList()
//                val keyList: ArrayList<String> = ArrayList()

                it.forEach {

//                    list.add(it.value)
//                    keyList.add(it.key)

                    list.add(it.value)
                    it.value.senderId
                    keyList.add(it.key)

                }
                if (!list.isNullOrEmpty()) {
                    // list.reverse()
                }
                Log.d("userChat", list.toString())
                userChatsRecyclerViewAdapter =
                    UserChatsRecyclerViewAdapter(this@UserChatsActivity, list, keyList, photoList)
                userChatsRecyclerView.adapter = userChatsRecyclerViewAdapter
            }

            else{
                addchat.visibility = View.VISIBLE

            }
        })


    }
}

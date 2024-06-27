package com.app.ehealthaidoctor.adapters.recyclerViewAdapters

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.ehealthai.models.local.User
import com.app.ehealthai.network.firebase.FirebaseUtils
import com.app.ehealthaidoctor.R
import com.app.ehealthaidoctor.ui.activities.Chat.ChatActivity
import com.app.ehealthaidoctor.utils.SharedPrefs
import com.example.ehealthai.utils.convertTimeFormat
import com.squareup.picasso.Picasso
import java.util.*

class UserChatsRecyclerViewAdapter(
    val context: Context,
    val chatsList: List<User>,
    val keyList: List<String>,
    val photoList: List<String>
) :
    RecyclerView.Adapter<UserChatsRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.item_user_chats_recycler_view, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = chatsList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chat = chatsList.get(position)

        val chatIds = keyList.get(position).split("_")

        val cal = Calendar.getInstance()
        cal.timeInMillis = chatsList.get(position).timeStamp.toLong()
        val day = cal.get(Calendar.DAY_OF_WEEK)
        val time = cal.get(Calendar.HOUR_OF_DAY).toString() + ":" + cal.get(Calendar.MINUTE).toString()
        val timeIn12hr = convertTimeFormat(time, null)

        holder.chatTime.text = timeIn12hr
        holder.chatName.text = chat.patientName
        holder.lastMessage.text = chat.lastMessage

        //If the sender is opposite party and message is not seen by him
        if (chat.lastSeen == "false" && chat.senderId.equals(chatIds[1])) {
            holder.newMessageImage.visibility = View.VISIBLE
            holder.lastMessage.typeface = Typeface.DEFAULT_BOLD
        } else {
            holder.newMessageImage.visibility = View.GONE
            holder.lastMessage.typeface = Typeface.DEFAULT
        }
Log.e("profile pic",SharedPrefs.getString(context,"filepath") +chat.image_name_p)
        Picasso.get().load(SharedPrefs.getString(context,"filepath") +chat.image_name_p)
                .placeholder(context.resources.getDrawable(R.drawable.doctor_icon))
                .into(holder.chatImage)

        //holder.specialityImage.setImageResource()

        holder.chatLayout.setOnClickListener {

            holder.newMessageImage.visibility = View.GONE
            holder.lastMessage.typeface = Typeface.DEFAULT

            FirebaseUtils.updateLastSeen(chatIds[2], chatIds[1], keyList.get(position))

            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra("patientId", chatIds[1])
            intent.putExtra("patientName", chat.patientName)
            intent.putExtra("image_name_p", chat.image_name_p)
            intent.putExtra("image_name", chat.image_name_p)
            context.startActivity(intent)
        }

    }


    // stores and recycles views as they are scrolled off screen
    class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val chatImage by lazy { itemView.findViewById<ImageView>(R.id.chatImage) }
        val chatName by lazy { itemView.findViewById<TextView>(R.id.chatName) }
        val lastMessage by lazy { itemView.findViewById<TextView>(R.id.lastMessage) }
        val newMessageImage by lazy { itemView.findViewById<ImageView>(R.id.newMessageImage) }
        val chatLayout by lazy { itemView.findViewById<RelativeLayout>(R.id.chatLayout) }
        val chatTime by lazy { itemView.findViewById<TextView>(R.id.chat_time) }

    }
}
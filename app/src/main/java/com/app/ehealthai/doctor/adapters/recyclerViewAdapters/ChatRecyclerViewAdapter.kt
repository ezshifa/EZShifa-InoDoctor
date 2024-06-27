package com.app.ehealthaidoctor.adapters.recyclerViewAdapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.ehealthai.models.local.Chat
import com.app.ehealthaidoctor.R
import com.example.ehealthai.utils.convertTimeFormat
import java.util.*
import kotlin.collections.ArrayList

class ChatRecyclerViewAdapter(
    val context: Context,
    val userId: String,
    val chatList: ArrayList<Chat>
) :
    RecyclerView.Adapter<ChatRecyclerViewAdapter.ViewHolder>() {

    companion object {
        val MESSAGE_RIGHT = 1
        val MESSAGE_LEFT = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View
        if (viewType == MESSAGE_RIGHT)
            view = LayoutInflater.from(context).inflate(R.layout.item_right_chat_recycler_view, parent, false)
        else
            view = LayoutInflater.from(context).inflate(R.layout.item_left_chat_recycler_view, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = chatList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.chatMessage.text = chatList.get(position).message

        val cal = Calendar.getInstance()
        cal.timeInMillis = chatList.get(position).timeStamp.toLong()
        val day = cal.get(Calendar.DAY_OF_WEEK)
        val time = cal.get(Calendar.HOUR_OF_DAY).toString() + ":" + cal.get(Calendar.MINUTE).toString()
        val timeIn12hr = convertTimeFormat(time, null)

        holder.chatTime.text = timeIn12hr
        var monthvalcal:Int=cal.get(Calendar.MONTH)
        monthvalcal=monthvalcal+1
        if (position - 1 != -1) {
            val prev = Calendar.getInstance()
            prev.timeInMillis = chatList.get(position - 1).timeStamp.toLong()
            val currentDate = cal.get(Calendar.DAY_OF_MONTH).toString() + "-" + cal.get(Calendar.MONTH) + "-" + cal.get(Calendar.YEAR)
            val prevDate = prev.get(Calendar.DAY_OF_MONTH).toString() + "-" + prev.get(Calendar.MONTH) + "-" + prev.get(Calendar.YEAR)

            if (currentDate == prevDate) {
                holder.dateLayout.visibility = View.GONE
            } else {
                holder.dateLayout.visibility = View.VISIBLE
                holder.dateText.text =
                    convertDayNumToDayName(day) + ", " + cal.get(Calendar.DAY_OF_MONTH).toString() + "-" + monthvalcal.toString() + "-" + cal.get(Calendar.YEAR).toString()
            }
        } else {
            holder.dateLayout.visibility = View.VISIBLE
            holder.dateText.text = convertDayNumToDayName(day) + ", " + cal.get(Calendar.DAY_OF_MONTH).toString() + "-" + monthvalcal.toString() + "-" + cal.get(Calendar.YEAR).toString()
        }


    }

    override fun getItemViewType(position: Int): Int {

        if (chatList.get(position).senderId.equals(userId)) {
            return MESSAGE_RIGHT
        } else {
            return MESSAGE_LEFT
        }
    }

    // stores and recycles views as they are scrolled off screen
    class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val chatMessage by lazy { itemView.findViewById<TextView>(R.id.chat_message) }
        val chatTime by lazy { itemView.findViewById<TextView>(R.id.chat_time) }
        val dateText by lazy { itemView.findViewById<TextView>(R.id.date_text) }
        val dateLayout by lazy { itemView.findViewById<LinearLayout>(R.id.date_layout) }
    }

    fun convertDayNumToDayName(day: Int): String{
        var dayName = " "
        when(day){
         1 ->{ dayName = "Sunday"}
         2 ->{ dayName = "Monday"}
         3 ->{ dayName = "Tuesday"}
         4 ->{ dayName = "Wednesday"}
         5 ->{ dayName = "Thursday"}
         6 ->{ dayName = "Friday"}
         7 ->{ dayName = "Saturday"}
        }
        return dayName
    }

}
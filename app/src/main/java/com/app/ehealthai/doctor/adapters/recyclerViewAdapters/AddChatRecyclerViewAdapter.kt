package com.app.ehealthaidoctor.adapters.recyclerViewAdapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.app.ehealthaidoctor.R
import com.app.ehealthaidoctor.models.responses.AppointmentDoctorData
import com.app.ehealthaidoctor.utils.SharedPrefs
import com.bumptech.glide.Glide

class AddChatRecyclerViewAdapter(
    val context: Context,
    val appointmentsList: List<AppointmentDoctorData>,
    var startChat: (patientId: String,patientName: String, image_name: String) -> Unit
) :
    RecyclerView.Adapter<AddChatRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.item_add_chat_recycler_view, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = appointmentsList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {



        Glide.with(context)
                .load(SharedPrefs.getString(context,"filepath")+appointmentsList.get(position).profile_image)
                .error(context.resources.getDrawable(R.drawable.doctor_icon))
                .into(holder.doctorImage)

        holder.doctorName.text = appointmentsList.get(position).fname + " " + appointmentsList.get(position).lname

        holder.doctorCard.setOnClickListener {
            startChat(appointmentsList.get(position).id.toString(),
                    appointmentsList.get(position).fname + " " + appointmentsList.get(position).lname,
                    appointmentsList.get(position).profile_image) }
    }


    // stores and recycles views as they are scrolled off screen
    class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val doctorImage by lazy { itemView.findViewById<ImageView>(R.id.user_image) }
        val doctorName by lazy { itemView.findViewById<TextView>(R.id.username) }
        val doctorCard by lazy { itemView.findViewById<CardView>(R.id.doctor_card) }


    }
}
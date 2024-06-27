package com.app.ehealthai.doctor.adapters

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.app.ehealthai.doctor.models.responses.notifications_info
import com.app.ehealthai.doctor.utils.TinyDB
import com.app.ehealthaidoctor.R
import com.bumptech.glide.Glide

class AppNotificationsRecyclerViewAdapter(
    val context: Context,
    val aiList: ArrayList<notifications_info> = ArrayList()
) :
    RecyclerView.Adapter<AppNotificationsRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.app_notif_item, parent, false)
        return ViewHolder(view)
    }
    override fun getItemCount(): Int {
        return aiList.size
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val Notifs = aiList.get(position)

        holder.company.text = Notifs.title
        holder.rdetails.text= Notifs.body
        holder.ndate.text= Notifs.date
        try
        {
            if(Notifs.imageurl.startsWith("http")) {
                holder.imagenotif.visibility=View.VISIBLE
                Glide.with(context).load(Notifs.imageurl).error(context.resources.getDrawable(R.drawable.ic_notif2)).dontAnimate().into(holder.imagenotif)
            }

        }
        catch (e:Exception)
        {

        }


        holder.imagenotif.setOnClickListener {
/*            val intent = Intent(context, DisplayNotificationDialog::class.java)
            intent.putExtra("notiftitle", Notifs.title)
            intent.putExtra("notifbody", Notifs.body)
            intent.putExtra("notifimage", Notifs.imageurl)
            context.startActivity(intent)*/
        }
        holder.delan.setOnClickListener {

            val builder = AlertDialog.Builder(context)

            // Set the alert dialog title
            builder.setTitle("Delete Notification")

            // Display a message on alert dialog
            builder.setMessage("Are you sure you want to delete this notification ?")

            // Set a positive button and its click listener on alert dialog
            builder.setPositiveButton("Yes"){ _, _ ->
            try {
                Toast.makeText(context, "deleted successfully", Toast.LENGTH_LONG).show()
                val tinydb = TinyDB(context)
                val oldstrings: java.util.ArrayList<String> = tinydb.getListString("notifications")
                oldstrings.removeAt(position)
                aiList.removeAt(position)
                val list = java.util.ArrayList<String>()
                for (ii in oldstrings.indices) {
                    list.add(oldstrings[ii].trim { it <= ' ' })
                }
                tinydb.putListString("notifications", list)
                notifyDataSetChanged()
            } catch (a: Exception) {
            }
            }


            // Display a negative button on alert dialog
            builder.setNegativeButton("No"){ _, _ ->

            }

            // Finally, make the alert dialog using builder
            val dialog: AlertDialog = builder.create()

            // Display the alert dialog on app interface
            dialog.show()
        }

    }


    // stores and recycles views as they are scrolled off screen
    class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val rdetails by lazy { itemView.findViewById<TextView>(R.id.rdetails) }
        val company by lazy { itemView.findViewById<TextView>(R.id.company) }
        val ndate by lazy { itemView.findViewById<TextView>(R.id.ndate) }
        val delan by lazy { itemView.findViewById<ImageView>(R.id.delan) }
        val imagenotif by lazy { itemView.findViewById<ImageView>(R.id.imagenotif) }
val vitalsh_layout by lazy { itemView.findViewById<RelativeLayout>(R.id.vitalsh_layout) }




    }
}
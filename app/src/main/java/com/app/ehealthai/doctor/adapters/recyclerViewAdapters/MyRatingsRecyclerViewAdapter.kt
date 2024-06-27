package com.app.ehealthai.doctor.adapters.recyclerViewAdapters

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.ehealthai.doctor.models.responses.FeedbackData
import com.app.ehealthaidoctor.R

class MyRatingsRecyclerViewAdapter(
        val context: Context,
        val feedbackList: List<FeedbackData>
) :
        RecyclerView.Adapter<MyRatingsRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val view: View =
                LayoutInflater.from(context).inflate(R.layout.item_myratings, parent, false)
        return ViewHolder(view)
    }
    override fun getItemCount(): Int {
        return feedbackList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val myratings = feedbackList.get(position)
if(myratings.rating_number.isNullOrEmpty()){
        holder.rating.text = "Rating : "+"0/5"}
        else{holder.rating.text = "Rating : "+myratings.rating_number+"/5"}
        holder.title.text = myratings.title
        holder.comments.text = myratings.comments
    }
    // stores and recycles views as they are scrolled off screen
    class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val rating by lazy { itemView.findViewById<TextView>(R.id.rating) }
        val title by lazy { itemView.findViewById<TextView>(R.id.rtitle) }
        val comments by lazy { itemView.findViewById<TextView>(R.id.rcomments) }
    }
}
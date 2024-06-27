package com.app.ehealthaidoctor.adapters.recyclerViewAdapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.ehealthaidoctor.R
import com.app.ehealthaidoctor.models.responses.Document


class UploadedFilesAppointmentDetailsRecyclerViewAdapter(
    val context: Context,
    val filesList: List<Document>,
    var openDialog: (imageUrl: String) -> Unit
) :
    RecyclerView.Adapter<UploadedFilesAppointmentDetailsRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.item_uploaded_files_recycler_view, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = filesList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = filesList.get(position)

        val imageUrl = "https://emr.e-healthai.com/patient_docs/" + item.file_name
        // Picasso.get().load(imageUrl).into(holder.FileImage)
        holder.FileName.text = item.file_name
        holder.FileDate.text = item.create_date
        holder.FileType.text = item.doc_type

        holder.FileLayout.setOnClickListener {
            if (item.doc_type.equals("png") || item.doc_type.equals("jpg") || item.doc_type.equals("jpeg")) {
                openDialog(imageUrl)
            }
        }

    }


    // stores and recycles views as they are scrolled off screen
    class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val FileImage by lazy { itemView.findViewById<ImageView>(R.id.file_preview) }
        val FileName by lazy { itemView.findViewById<TextView>(R.id.file_name) }
        val FileDate by lazy { itemView.findViewById<TextView>(R.id.date) }
        val FileType by lazy { itemView.findViewById<TextView>(R.id.type_name) }
        val FileLayout by lazy { itemView.findViewById<RelativeLayout>(R.id.file_layout) }

    }
}

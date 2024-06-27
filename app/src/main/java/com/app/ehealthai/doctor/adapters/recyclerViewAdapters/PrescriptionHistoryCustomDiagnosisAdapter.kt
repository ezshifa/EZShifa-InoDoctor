package com.app.ehealthai.doctor.adapters.recyclerViewAdapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.ehealthaidoctor.R
import com.example.example.PatientCustomDiagnosis
import com.example.example.PatientDiagnosis

class PrescriptionHistoryCustomDiagnosisAdapter(
    val context: Context,
    val diagnosisList: List<PatientCustomDiagnosis>) :
    RecyclerView.Adapter<PrescriptionHistoryCustomDiagnosisAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(context)
                .inflate(R.layout.list_item_custom_diagnosis_history, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = diagnosisList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvDiagnosisName.text = diagnosisList[position].diagnosisName
    }

    class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvDiagnosisName: TextView by lazy { itemView.findViewById(R.id.tv_custom_diagnosis_history_name) }
    }


}
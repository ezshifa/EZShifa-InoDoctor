package com.app.ehealthai.doctor.adapters.recyclerViewAdapters

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.app.ehealthai.doctor.models.responses.RemovePatientDiagnosisResponse
import com.app.ehealthai.doctor.ui.activities.PatientDetails.PatientAppointment
import com.app.ehealthai.doctor.utils.Global
import com.app.ehealthaidoctor.R
import com.app.ehealthaidoctor.network.ApiUtils
import com.example.example.PatientDiagnosis
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PresciptionHistoryDiagnosisAdapter(
    val context: Context,
    val diagnosisList: List<PatientDiagnosis>) :
    RecyclerView.Adapter<PresciptionHistoryDiagnosisAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(context)
                .inflate(R.layout.list_item_diagnosis_history, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = diagnosisList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvDiagnosisName.text = diagnosisList[position].diagnosisName
    }

    class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvDiagnosisName: TextView by lazy { itemView.findViewById(R.id.tv_diagnosis_history_name) }
    }


}
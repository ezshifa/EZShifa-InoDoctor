package com.app.ehealthai.doctor.adapters.recyclerViewAdapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.ehealthaidoctor.R
import com.example.example.PatientTest

class PrescriptionHistoryTestsAdapter(
    val context: Context,
    val testsList: List<PatientTest>
) :
    RecyclerView.Adapter<PrescriptionHistoryTestsAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(context)
                .inflate(R.layout.list_item_tests_history, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = testsList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvDiagnosisName.text = testsList[position].testName
    }


    // stores and recycles views as they are scrolled off screen
    class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvDiagnosisName: TextView by lazy { itemView.findViewById(R.id.tv_test_history_name) }
    }

}
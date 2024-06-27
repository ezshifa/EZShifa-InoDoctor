package com.app.ehealthai.doctor.adapters.recyclerViewAdapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.ehealthaidoctor.R
import com.example.example.Medicine

class PrescriptionHistoryMedicinesAdapter(
    val context: Context,
    val medicinesList: List<Medicine>
) :
    RecyclerView.Adapter<PrescriptionHistoryMedicinesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(context)
                .inflate(R.layout.list_item_medicine_history, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = medicinesList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.tvMedicineName.text = medicinesList[position].brandName
        holder.tvMedicineQuantity.text = medicinesList[position].quantity
        holder.tvMedicineDosage.text = medicinesList[position].dozeTime
    }
    class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvMedicineName: TextView by lazy { itemView.findViewById(R.id.tv_diagnosis_history_medicine_name) }
        val tvMedicineQuantity: TextView by lazy { itemView.findViewById(R.id.tv_diagnosis_history_quantity) }
        val tvMedicineDosage: TextView by lazy { itemView.findViewById(R.id.tv_diagnosis_history_doze_time) }
    }
}
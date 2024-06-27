package com.app.ehealthai.doctor.ui.activities.PatientDetails.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.ehealthai.doctor.models.responses.patientTests.PatientTestsList
import com.app.ehealthai.doctor.ui.activities.PatientDetails.PatientAppointment
import com.app.ehealthaidoctor.R
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class RapidTestsAdapter(
    private val patientTestsList: List<PatientTestsList>,
    private val context: PatientAppointment
) :
    RecyclerView.Adapter<RapidTestsAdapter.MyViewHolder>() {


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTestNameSugar: TextView = itemView.findViewById(R.id.tvTestNameSugar)
        val tvTestValueSugar: TextView = itemView.findViewById(R.id.tvTestSugarValue)
        val tvTestValueHiv: TextView = itemView.findViewById(R.id.tvTestHivValue)
        val tvTestValueTb: TextView = itemView.findViewById(R.id.tvTestTbValue)
        val tvTestValueHepatitis: TextView = itemView.findViewById(R.id.tvTestHepatitisValue)
        val tvDate: TextView = itemView.findViewById(R.id.tvTestDateValue)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_rapid_test, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val patientData = patientTestsList[position]
        if (patientData != null) {
            if (patientData.sugarType != null) {
                holder.tvTestNameSugar.text = "${patientData.sugarType}\nSugar"
            } else {
                holder.tvTestNameSugar.text = "Sugar"
            }
            if (patientData.sugar == "" ||patientData.sugar == null) {
                holder.tvTestValueSugar.text = "n/a"
            } else {
                holder.tvTestValueSugar.text = patientData.sugar
            }
            holder.tvTestValueHiv.text = patientData.hiv
            holder.tvTestValueTb.text = patientData.tb
            holder.tvTestValueHepatitis.text = patientData.hepatitis
            if(patientData.cdate!="" && patientData.cdate!=null)
            {
                val dateRepresentable=formatDateString(patientData.cdate)
                holder.tvDate.text = dateRepresentable
            }
        }
    }

    private fun formatDateString(dateString: String?): String? {
        var formattedDate = ""
        formattedDate = try {
            val inputFormat: DateFormat =
                SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val outputFormat: DateFormat =
                SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
            val date: Date = inputFormat.parse(dateString)
            outputFormat.format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
            dateString!!
        }
        return formattedDate
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return patientTestsList.size
    }
}
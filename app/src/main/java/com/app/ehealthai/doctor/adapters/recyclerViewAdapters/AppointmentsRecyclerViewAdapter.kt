package com.app.ehealthaidoctor.adapters.recyclerViewAdapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.ehealthai.doctor.models.responses.SiteLangResponse
import com.app.ehealthai.doctor.ui.callBacks.HistoryItemClicked
import com.app.ehealthaidoctor.R
import com.app.ehealthaidoctor.models.responses.AppointmentDoctorData
import com.app.ehealthaidoctor.network.ApiUtils
import com.app.ehealthaidoctor.utils.SharedPrefs
import com.example.ehealthai.utils.convertTimeFormat
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class AppointmentsRecyclerViewAdapter(
    val context: Context,
    val appointmentList: ArrayList<AppointmentDoctorData>,
    var appointmentClick: (patientId: String, patientName: String, description: String?, appointmentId: String, doctor_notes: String, apptstatus: String, email: String, profile_image: String, DOB: String?, roomid: String?, age: String?, sex: String?, phone_cell: String?, registered_from: String?,cnic:String?,appTime:String?,appDate:String?) -> Unit,
    private var historyItemClicked: HistoryItemClicked
) :
    RecyclerView.Adapter<AppointmentsRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(context)
                .inflate(R.layout.item_appointments_recycler_view, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = appointmentList.size
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = appointmentList.get(position)

        val startTime = item.appttime

        val splitTime = startTime.split(":")

        val formatter = SimpleDateFormat("HH:mm")
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, splitTime[0].toInt())
        cal.set(Calendar.MINUTE, splitTime[1].toInt())
        cal.add(Calendar.MINUTE, 15)

        val endTime = formatter.format(cal.time)

        holder.appointmentTime.text = convertTimeFormat(startTime, null) + " - " + convertTimeFormat(endTime, null)
        holder.patientName.text = item.fname + " " + item.lname
        var email = "Not Available"
        var pimage = ""
        var dob = "Not Available"
        var age = "Not Available"
        var sex = "Not Available"
        if (item.apptstatus.contains("open")) {
            holder.detailsBtn.setText("Open")
            holder.detailsBtn.setBackgroundColor(Color.parseColor("#00A222"))
        } else if (item.apptstatus.contains("cancelled")) {
            holder.detailsBtn.setText("Cancelled")
            holder.detailsBtn.setBackgroundColor(Color.parseColor("#DF0000"))
        } else {
            holder.detailsBtn.setText("Closed")
            holder.detailsBtn.setBackgroundColor(Color.parseColor("#808080"))
        }
        @Suppress("SENSELESS_COMPARISON")
        if (item.email != null) {
            email = item.email
        }
        @Suppress("SENSELESS_COMPARISON")
        if (item.profile_image != null) {
            pimage = item.profile_image
        }
        @Suppress("SENSELESS_COMPARISON")
        if (item.DOB != null) {
            dob = item.DOB
        }
        @Suppress("SENSELESS_COMPARISON")
        if (item.age != null) {
            age = item.age
        }
        @Suppress("SENSELESS_COMPARISON")
        if (item.sex != null) {
            sex = item.sex
        }
        holder.detailsBtn.setOnClickListener {
            appointmentClick(
                item.id.toString(),
                item.fname + " " + item.lname,
                item.description,
                item.apptid.toString(),
                item.doctor_notes,
                item.apptstatus,
                email,
                pimage,
                dob,
                item.roomid,
                age,
                sex,
                item.phone_cell,
                item.registered_from,
                item.cnic,
                item.appttime,
                item.apptdate
            )
        }
        holder.otherdetailsBtn.setOnClickListener {

            val userData = SharedPrefs.getUserData(context)
            val username = userData!!.username
            val usertype = "DOCTOR"
            ApiUtils.getAPIService(context).get_appt_site_language(
                SharedPrefs.getString(context, "sessionid")!!,
                username!!,
                usertype,
                item.apptid.toString()
            )
                .enqueue(object : Callback<SiteLangResponse> {
                    override fun onFailure(call: Call<SiteLangResponse>, t: Throwable) {
                        // stores and recycles views as they are scrolled off screen
                    }

                    override fun onResponse(
                        call: Call<SiteLangResponse>,
                        response: Response<SiteLangResponse>
                    ) {
                        if (response.isSuccessful && response.body() != null && response.body()!!.data != null && !response.body()!!.data!!.list!!.isNullOrEmpty()) {
                            holder.loclang.text =
                                "Project: " + response.body()!!.data!!.list!!.get(0).project_name +
                                        "\nSite: " + response.body()!!.data!!.list!!.get(0).site_name +
                                        "\nLanguage: " + response.body()!!.data!!.list!!.get(0).language
                        }
                    }
                })
        }
        holder.historyBtn.setOnClickListener {
            if (historyItemClicked != null)
                historyItemClicked.historyItem(item)
        }
    }


    // stores and recycles views as they are scrolled off screen
    class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val appointmentTime by lazy { itemView.findViewById<TextView>(R.id.appointment_time) }
        val patientName by lazy { itemView.findViewById<TextView>(R.id.patient_name) }
        val detailsBtn by lazy { itemView.findViewById<TextView>(R.id.details_btn) }
        val historyBtn by lazy { itemView.findViewById<TextView>(R.id.btn_history) }
        val otherdetailsBtn by lazy { itemView.findViewById<TextView>(R.id.other_details) }
        val loclang by lazy { itemView.findViewById<TextView>(R.id.loclang) }
    }
}

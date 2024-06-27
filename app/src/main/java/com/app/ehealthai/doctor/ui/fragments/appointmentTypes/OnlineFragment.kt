package com.app.ehealthaidoctor.ui.fragments.appointmentTypes

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.ehealthai.doctor.ui.activities.HistoryActivity
import com.app.ehealthai.doctor.ui.activities.PatientDetails.PatientAppointment
import com.app.ehealthai.doctor.ui.callBacks.HistoryItemClicked
import com.app.ehealthaidoctor.R
import com.app.ehealthaidoctor.adapters.recyclerViewAdapters.AppointmentsRecyclerViewAdapter
import com.app.ehealthaidoctor.models.responses.AppointmentDoctorData
import com.app.ehealthaidoctor.ui.activities.AppointmentsActivity
import com.app.ehealthaidoctor.utils.SharedPrefs
import com.example.ehealthai.utils.Constants
import kotlinx.android.synthetic.main.fragment_appointment_type.view.appointment_type_recycler_view
import kotlinx.android.synthetic.main.fragment_appointment_type.view.no_appointments_text


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class OnlineFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    var appointmentsRecyclerViewAdapter: AppointmentsRecyclerViewAdapter? = null
    lateinit var appointmentsActivity: AppointmentsActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        appointmentsActivity = activity as AppointmentsActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_appointment_type, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (AppointmentsActivity.onlineList.isEmpty()) {
            view.no_appointments_text.visibility = View.VISIBLE
        } else {
            view.no_appointments_text.visibility = View.GONE
            view.appointment_type_recycler_view.layoutManager = LinearLayoutManager(context)
            val userData = SharedPrefs.getUserData(context!!)

            appointmentsRecyclerViewAdapter = AppointmentsRecyclerViewAdapter(context!!,
                AppointmentsActivity.onlineList,
                appointmentClick = { patientId, patientName, description, appointmentId, doctor_notes, apptstatus, email, profile_image, DOB, roomid, age, sex, phone_cell, registered_from,cnic,appTime,appDate ->
                    val intent = Intent(context, PatientAppointment::class.java)
                    PatientAppointment.patientId = patientId
                    PatientAppointment.patientName = patientName
                    PatientAppointment.appointmentId = appointmentId
                    PatientAppointment.description = description
                    PatientAppointment.doctorId = userData!!.id.toString()
                    PatientAppointment.apptstatus = apptstatus
                    PatientAppointment.apptTime = appTime
                    PatientAppointment.apptDate = appDate
                    PatientAppointment.doc_notes = doctor_notes
                    PatientAppointment.email = email
                    PatientAppointment.roomid = roomid!!
                    PatientAppointment.age = age
                    PatientAppointment.sex = sex
                    PatientAppointment.profile_image = profile_image
                    //for ezscreening appointment
                    PatientAppointment.studentFatherPhone = phone_cell
                    PatientAppointment.registeredFrom = registered_from
                    PatientAppointment.studentCNIC = cnic
                    //  val intent = Intent(context, AppointmentDetailsActivity::class.java)
                    intent.putExtra("patientId", patientId)
                    intent.putExtra("appointmentId", appointmentId)
                    intent.putExtra("description", description)
                    intent.putExtra("doctorId", userData!!.id.toString())
                    intent.putExtra("patientName", patientName)
                    intent.putExtra("degrees", userData!!.degrees)
                    intent.putExtra("doctor_notes", doctor_notes)
                    intent.putExtra("apptstatus", apptstatus)
                    Log.e("online doc notes", doctor_notes)
                    val room = "room" + "1"
                    intent.putExtra("room", room)
                    intent.putExtra("email", email)
                    intent.putExtra("profileimage", profile_image)
                    intent.putExtra("dob", DOB)
                    intent.putExtra("roomid", roomid)
                    intent.putExtra("age", age)
                    intent.putExtra("sex", sex)
                    startActivityForResult(intent, 0)
                },
                object : HistoryItemClicked {
                    override fun historyItem(item: AppointmentDoctorData) {
                        Log.e("TAG", "historyItem: ${item.apptid}")
                        if (Constants.isInternetConnected(appointmentsActivity)) {
                            startActivity(
                                Intent(appointmentsActivity, HistoryActivity::class.java)
                                    .putExtra(Constants.APPOINTMENTID, item.apptid.toString())
                            )
                        } else {
                            Toast.makeText(
                                appointmentsActivity,
                                "No Internet Connection",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                })

            view.appointment_type_recycler_view.adapter = appointmentsRecyclerViewAdapter
        }
    }


    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) = OnlineFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_PARAM1, param1)
                putString(ARG_PARAM2, param2)
            }
        }
    }
}

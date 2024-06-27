package com.app.ehealthaidoctor.ui.fragments.appointmentTypes

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.ehealthai.doctor.ui.activities.PatientDetails.PatientAppointment
import com.app.ehealthai.doctor.ui.callBacks.HistoryItemClicked
import com.app.ehealthaidoctor.R
import com.app.ehealthaidoctor.adapters.recyclerViewAdapters.AppointmentsRecyclerViewAdapter
import com.app.ehealthaidoctor.models.responses.AppointmentDoctorData
import com.app.ehealthaidoctor.ui.activities.AppointmentsActivity
import com.app.ehealthaidoctor.utils.SharedPrefs
import kotlinx.android.synthetic.main.activity_registernewpatient.cnic
import kotlinx.android.synthetic.main.fragment_appointment_type.view.appointment_type_recycler_view
import kotlinx.android.synthetic.main.fragment_appointment_type.view.no_appointments_text


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class WalkinFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

    var appointmentsRecyclerViewAdapter: AppointmentsRecyclerViewAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_appointment_type, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (AppointmentsActivity.walkinList.isEmpty()) {
            view.no_appointments_text.visibility = View.VISIBLE
        } else {
            view.no_appointments_text.visibility = View.GONE
            view.appointment_type_recycler_view.layoutManager = LinearLayoutManager(context)
            val userData = SharedPrefs.getUserData(context!!)

            appointmentsRecyclerViewAdapter = AppointmentsRecyclerViewAdapter(
                context!!,
                AppointmentsActivity.walkinList,
                appointmentClick = { patientId, patientName, description, appointmentId,doctor_notes,apptstatus,email,profile_image,DOB,roomid,age,sex,phone_cell,registered_from,cnic,appTime:String?,appDate:String? ->
                    val intent = Intent(context, PatientAppointment::class.java)
                    PatientAppointment.patientId=patientId
                    PatientAppointment.patientName=patientName
                    PatientAppointment.appointmentId=appointmentId
                    PatientAppointment.description=description
                    PatientAppointment.doctorId=userData!!.id.toString()
                    PatientAppointment.apptstatus=apptstatus
                    PatientAppointment.apptTime=appTime
                    PatientAppointment.doc_notes=doctor_notes
                    PatientAppointment.email=email
                    PatientAppointment.roomid=roomid!!
                    PatientAppointment.age=age
                    PatientAppointment.sex=sex
                    PatientAppointment.profile_image=profile_image
                    PatientAppointment.studentFatherPhone = phone_cell
                    PatientAppointment.registeredFrom = registered_from
                    PatientAppointment.studentCNIC = cnic

                    //val intent = Intent(context, AppointmentDetailsActivity::class.java)
                    intent.putExtra("patientId", patientId)
                    intent.putExtra("appointmentId", appointmentId)
                    intent.putExtra("description", description)
                    intent.putExtra("doctorId", userData!!.id.toString())
                    intent.putExtra("patientName", patientName)
                    intent.putExtra("doctor_notes", doctor_notes)
                    intent.putExtra("apptstatus", apptstatus)
                    val room = "room" + "1"
                    intent.putExtra("room", room)
                    intent.putExtra("email", email)
                    intent.putExtra("profileimage", profile_image)
                    intent.putExtra("dob", DOB)
                    intent.putExtra("roomid", roomid)
                    intent.putExtra("age", age)
                    intent.putExtra("sex", sex)
                    startActivityForResult(intent,0)
                },
                object : HistoryItemClicked {
                    override fun historyItem(item: AppointmentDoctorData) {
                        Log.e("TAG", "historyItem: $item", )

                    }
                })

            view.appointment_type_recycler_view.adapter = appointmentsRecyclerViewAdapter
        }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(" must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(docExtension: String)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            WalkinFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }

            }
    }
}

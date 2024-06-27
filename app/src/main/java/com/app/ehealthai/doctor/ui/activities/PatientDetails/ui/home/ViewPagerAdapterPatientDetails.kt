package com.app.ehealthai.doctor.ui.activities.PatientDetails.ui.home

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.app.ehealthai.doctor.ui.activities.PatientDetails.PatientAppointment

class ViewPagerAdapterPatientDetails(
    fragment: Fragment,
    patientId: String?,
    appointmentId: String?,
    context: PatientAppointment
) : FragmentStateAdapter(fragment) {

    private var mPatientId: String? = null
    private var mAppointmentId: String? = null
    private var mContext: PatientAppointment

    init {
        mPatientId = patientId
        mAppointmentId = appointmentId
        mContext = context
    }

    override fun getItemCount(): Int {
        // Return the number of tabs
        return 3
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun createFragment(position: Int): Fragment {
        // Return your fragment based on the position
        return when (position) {
            0 -> PatientVitalsFragment(mPatientId, mAppointmentId,mContext)
            1 -> PatientScreeningFragment(mPatientId, mAppointmentId, mContext)
            2 -> PatientTestsFragment(mPatientId, mAppointmentId, mContext)
            else -> {
                PatientVitalsFragment(mPatientId, mAppointmentId,mContext)
            }
        }
    }
}
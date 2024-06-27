package com.app.ehealthai.doctor.ui.activities.PatientDetails.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.app.ehealthai.doctor.models.responses.patientTests.PatientTests
import com.app.ehealthai.doctor.models.responses.patientTests.PatientTestsList
import com.app.ehealthai.doctor.ui.activities.PatientDetails.PatientAppointment
import com.app.ehealthaidoctor.R
import com.app.ehealthaidoctor.network.ApiUtils
import com.example.ehealthai.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PatientTestsFragment(pId: String?, apptId: String?, context: PatientAppointment) :
    Fragment() {
    private lateinit var pbLoadingTests: ProgressBar
    private lateinit var rvTests: RecyclerView
    private lateinit var tvTestsNotAvailable: TextView

    private var mContext: PatientAppointment
    private var patientId: String? = null
    private var mAppointmentId: String? = null

    init {
        patientId = pId
        mContext = context
        mAppointmentId = apptId
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_patient_test, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pbLoadingTests = view.findViewById(R.id.pbTestsPatientDetails)
        rvTests = view.findViewById(R.id.rvRapidTests)
        tvTestsNotAvailable = view.findViewById(R.id.tvTestsNotAvailablePatientDetails)

        try {
            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                if (!patientId.isNullOrEmpty() && !mAppointmentId.isNullOrEmpty()) {
                    if (Constants.isInternetConnected(mContext)) {
                        pbLoadingTests.visibility = View.VISIBLE
                        getPatientTests(patientId!!, mAppointmentId!!)
                    } else {
                        withContext(Dispatchers.Main)
                        {
                            if (isVisible) {
                                pbLoadingTests.visibility = View.GONE
                                tvTestsNotAvailable.visibility = View.VISIBLE
                                Toast.makeText(
                                    mContext,
                                    "No internet connection",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }

                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


    private fun getPatientTests(patientId: String, appointmentId: String) {
        ApiUtils.getAPIService(mContext).getPatientTests(
            patientId, appointmentId
        ).enqueue(object : Callback<PatientTests> {
            override fun onFailure(call: Call<PatientTests>, t: Throwable) {
                if (isVisible) {
                    pbLoadingTests.visibility = View.GONE
                    tvTestsNotAvailable.visibility = View.VISIBLE
                    //Do nothing
                    Toast.makeText(mContext, "Unable to get patient test", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onResponse(
                call: Call<PatientTests>, response: Response<PatientTests>
            ) {
                try {
                    if (isVisible) {
                        pbLoadingTests.visibility = View.GONE
                        if (response != null) {
                            if (response.body() != null && response.body()!!.statuscode != null && response.body()!!.statuscode == 200) {
                                if (response.body()!!.patientTestsData != null) {
                                    if (response.body()!!.patientTestsData!!.patientTestslist != null) {
                                        if (response.body()!!.patientTestsData!!.patientTestslist.size > 0) {
                                            setTestsAdapter(response.body()!!.patientTestsData!!.patientTestslist)
                                        } else {
                                            tvTestsNotAvailable.visibility = View.VISIBLE
                                        }
                                    } else {
                                        tvTestsNotAvailable.visibility = View.VISIBLE
                                    }
                                }

                            }
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()

                }
            }
        })
    }

    fun setTestsAdapter(patientTestsList: List<PatientTestsList>) {
        val adapter = RapidTestsAdapter(patientTestsList, mContext)
        rvTests.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}
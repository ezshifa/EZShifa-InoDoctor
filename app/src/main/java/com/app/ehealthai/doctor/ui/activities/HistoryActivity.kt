package com.app.ehealthai.doctor.ui.activities

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.ehealthai.doctor.adapters.recyclerViewAdapters.PresciptionHistoryDiagnosisAdapter
import com.app.ehealthai.doctor.adapters.recyclerViewAdapters.PrescriptionHistoryCustomDiagnosisAdapter
import com.app.ehealthai.doctor.adapters.recyclerViewAdapters.PrescriptionHistoryMedicinesAdapter
import com.app.ehealthai.doctor.adapters.recyclerViewAdapters.PrescriptionHistoryTestsAdapter
import com.app.ehealthaidoctor.R
import com.app.ehealthaidoctor.network.ApiUtils
import com.example.ehealthai.utils.Constants
import com.example.ehealthai.utils.toast
import com.example.example.Medicine
import com.example.example.PatientCustomDiagnosis
import com.example.example.PatientDiagnosis
import com.example.example.PatientTest
import com.example.example.PrescriptionHistory
import kotlinx.android.synthetic.main.activity_history.iv_back_btn
import kotlinx.android.synthetic.main.activity_history.pbDiagnosis
import kotlinx.android.synthetic.main.activity_history.pbMedicine
import kotlinx.android.synthetic.main.activity_history.pbTests
import kotlinx.android.synthetic.main.activity_history.rvDiagnosis
import kotlinx.android.synthetic.main.activity_history.rvDiagnosisCustom
import kotlinx.android.synthetic.main.activity_history.rvMedicines
import kotlinx.android.synthetic.main.activity_history.rvTests
import kotlinx.android.synthetic.main.activity_history.tv_diagnosis_name_not_available_history
import kotlinx.android.synthetic.main.activity_history.tv_medicine_name_not_available_history
import kotlinx.android.synthetic.main.activity_history.tv_test_name_not_available_history
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryActivity : AppCompatActivity() {
    private lateinit var adapterTests: PrescriptionHistoryTestsAdapter
    private lateinit var adapterDiagnosis: PresciptionHistoryDiagnosisAdapter
    private lateinit var adapterCustomDiagnosis: PrescriptionHistoryCustomDiagnosisAdapter
    private lateinit var adapterMedicines: PrescriptionHistoryMedicinesAdapter
    lateinit var context: Context
    private var appointmentId: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        context = this@HistoryActivity
        if (intent != null) {
            appointmentId = intent.getStringExtra(Constants.APPOINTMENTID)!!
        }
        if (appointmentId != "") {
            if (Constants.isInternetConnected(this@HistoryActivity)) {
                getTestsAndDiagnosisHistory(appointmentId)
            } else {
                toast("No internet connection")
            }
        } else
            Toast.makeText(context, "Appointment id not available", Toast.LENGTH_SHORT).show()

        iv_back_btn.setOnClickListener {
            finish()
        }
    }

    fun setTestsAdapter(patientTests: ArrayList<PatientTest>) {
        pbTests.visibility = View.GONE
        adapterTests = PrescriptionHistoryTestsAdapter(context, patientTests)
        rvTests.adapter = adapterTests
    }

    fun setDiagnosisAdapter(patientDiagnosis: ArrayList<PatientDiagnosis>) {
        pbDiagnosis.visibility = View.GONE
        adapterDiagnosis = PresciptionHistoryDiagnosisAdapter(context, patientDiagnosis)
        rvDiagnosis.adapter = adapterDiagnosis
    }
    fun setCustomDiagnosisAdapter(otherDiagnosis: ArrayList<PatientCustomDiagnosis>) {
        pbDiagnosis.visibility = View.GONE
        adapterCustomDiagnosis = PrescriptionHistoryCustomDiagnosisAdapter(context, otherDiagnosis)
        rvDiagnosisCustom.adapter = adapterCustomDiagnosis
    }
    fun setMedicinesAdapter(medicines: ArrayList<Medicine>) {
        pbMedicine.visibility = View.GONE
        adapterMedicines = PrescriptionHistoryMedicinesAdapter(context, medicines)
        rvMedicines.adapter = adapterMedicines
    }

    private fun getTestsAndDiagnosisHistory(apptId: String) {
        ApiUtils.getAPIService(context)
            .getDiagnosisAndTests(apptId)
            .enqueue(object : Callback<PrescriptionHistory> {
                override fun onFailure(call: Call<PrescriptionHistory>, t: Throwable) {
                    toast("Unable to load prescriptions")
                    pbDiagnosis.visibility = View.GONE
                    pbTests.visibility = View.GONE
                    pbMedicine.visibility = View.GONE
                }

                override fun onResponse(
                    call: Call<PrescriptionHistory>,
                    response: Response<PrescriptionHistory>
                ) {
                    try {
                        if (response.isSuccessful && response.body() != null && response.body()!!.statuscode == 200) {
                            response.body()!!.data?.list?.patientDiagnosis?.let {
                                if (it.size == 0) {
                                    rvDiagnosis.visibility=View.GONE
                                    pbDiagnosis.visibility = View.GONE
                                } else {
                                    tv_diagnosis_name_not_available_history.visibility = View.GONE
                                    setDiagnosisAdapter(it)
                                }
                            }
                            response.body()!!.data?.list?.patientCustomDiagnosis?.let {
                                if (it.size == 0) {
                                    rvDiagnosisCustom.visibility=View.GONE
//                                    pbDiagnosis.visibility = View.GONE
                                } else {
                                    tv_diagnosis_name_not_available_history.visibility = View.GONE
                                    setCustomDiagnosisAdapter(it)
                                }
                            }
                            response.body()!!.data?.list?.patientTest?.let {
                                if (it.size == 0) {
                                    pbTests.visibility = View.GONE
                                } else {
                                    tv_test_name_not_available_history.visibility = View.GONE
                                    setTestsAdapter(it)
                                }
                            }
                            response.body()!!.data?.list?.medicine?.let {
                                if (it.size == 0) {
                                    pbMedicine.visibility = View.GONE
                                } else {
                                    tv_medicine_name_not_available_history.visibility = View.GONE
                                    setMedicinesAdapter(it)
                                }
                            }
                        } else if (response.isSuccessful && response.body() != null && response.body()!!.statuscode == 404) {
                            response.body()?.data?.message?.let {
                                toast(it)
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            })
    }

}
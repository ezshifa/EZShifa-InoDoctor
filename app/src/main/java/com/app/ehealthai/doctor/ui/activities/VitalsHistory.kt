package com.app.ehealthai.doctor.ui.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.ehealthai.adapters.recyclerViewAdapters.VitalsHistoryRecyclerViewAdapter
import com.app.ehealthai.doctor.models.responses.GetVitalsResponse
import com.app.ehealthai.doctor.models.responses.VitalsData
import com.app.ehealthaidoctor.R
import com.app.ehealthaidoctor.network.ApiUtils
import com.app.ehealthaidoctor.utils.SharedPrefs
import com.crashlytics.android.Crashlytics
import com.example.ehealthai.utils.toast
import io.fabric.sdk.android.Fabric
import kotlinx.android.synthetic.main.vitals_history.emptytextvs
import kotlinx.android.synthetic.main.vitals_history.progress_load_vs
import kotlinx.android.synthetic.main.vitals_history.vitals_history_back_btn
import kotlinx.android.synthetic.main.vitals_history.vitalslistview
import retrofit2.Call
import retrofit2.Response

class VitalsHistory : AppCompatActivity() {
    var vitalshistoryRecyclerViewAdapter: VitalsHistoryRecyclerViewAdapter? = null
    var appointmentId: String? = null
    var patientId: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fabric.with(this, Crashlytics())
        setContentView(R.layout.vitals_history)
        /* appointmentId = intent.getStringExtra("appointmentId")*/
        appointmentId = ""
        patientId = intent.getStringExtra("patientId")

        vitals_history_back_btn.setOnClickListener {
            onBackPressed()
        }
        vitalslistview.layoutManager = LinearLayoutManager(this@VitalsHistory)
        progress_load_vs.visibility = View.VISIBLE
        getVitalsHistory()
    }

    private fun getVitalsHistory() {
        val userData = SharedPrefs.getUserData(this)
        val username = userData!!.username
        val usertype = "DOCTOR"
        ApiUtils.getAPIService(this@VitalsHistory).GetVitalsRequest(
            appointmentId!!,
            patientId!!,
            SharedPrefs.getString(this@VitalsHistory, "sessionid")!!,
            username!!,
            usertype
        )
            .enqueue(object : retrofit2.Callback<GetVitalsResponse> {
                override fun onFailure(call: Call<GetVitalsResponse>, t: Throwable) {
                    progress_load_vs.visibility = View.GONE
                    toast("Something went wrong, please try again later")
                }

                override fun onResponse(
                    call: Call<GetVitalsResponse>,
                    response: Response<GetVitalsResponse>
                ) {
                    progress_load_vs.visibility = View.GONE
                    if (response.isSuccessful) {
                        val vitalsList: ArrayList<VitalsData> = ArrayList()
                        vitalsList.addAll(response.body()!!.data!!.vitals!!)
                        if (vitalsList.size == 0) {
                            emptytextvs.visibility = View.VISIBLE
                        } else {
                            vitalshistoryRecyclerViewAdapter =
                                VitalsHistoryRecyclerViewAdapter(
                                    this@VitalsHistory,
                                    response.body()!!.data!!.vitals!! as ArrayList<VitalsData>,
                                    patientId!!
                                )
                            vitalslistview.adapter = vitalshistoryRecyclerViewAdapter
                        }

                    } else {
                        toast("Something went wrong, please try again later")
                    }
                }
            })
    }
}
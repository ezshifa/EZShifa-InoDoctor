package com.app.ehealthai.doctor.ui.activities.PatientDetails.ui.home

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.app.ehealthai.doctor.models.responses.GetVitalsResponse
import com.app.ehealthai.doctor.models.responses.VitalsData
import com.app.ehealthai.doctor.ui.activities.PatientDetails.PatientAppointment
import com.app.ehealthaidoctor.R
import com.app.ehealthaidoctor.network.ApiUtils
import com.app.ehealthaidoctor.utils.SharedPrefs
import com.example.ehealthai.utils.Constants
import kotlinx.android.synthetic.main.vital_history_single_for_patient_details.pbVitalHistoryForSinglePatientDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PatientVitalsFragment(pId: String?, apptId: String?,context: PatientAppointment) : Fragment() {

    private lateinit var tvTemperatureVital: TextView
    private lateinit var tvBloodPressureVital: TextView
    private lateinit var tvPulseVital: TextView
    private lateinit var tvBreathingVital: TextView
    private lateinit var tvOxygenVital: TextView
    private lateinit var tvSugarVital: TextView
    private lateinit var tvHeightVital: TextView
    private lateinit var tvWeightVital: TextView
    private lateinit var pbVitalHistoryForSinglePatientDetails: ProgressBar
    private lateinit var llVitals: LinearLayout
    private lateinit var tvVitalsNotAvailable: TextView
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
        return inflater.inflate(R.layout.fragment_patient_vitals, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvTemperatureVital =
            view.findViewById(R.id.tvTemperatureVitalHistoryForSinglePatientDetails)
        tvBloodPressureVital =
            view.findViewById(R.id.tvBloodPressureVitalHistoryForSinglePatientDetails)
        tvPulseVital = view.findViewById(R.id.tvPulseVitalHistoryForSinglePatientDetails)
        tvBreathingVital = view.findViewById(R.id.tvBreathVitalHistoryForSinglePatientDetails)
        tvOxygenVital = view.findViewById(R.id.tvOxygenVitalHistoryForSinglePatientDetails)
        tvSugarVital = view.findViewById(R.id.tvSugarVitalHistoryForSinglePatientDetails)
        tvHeightVital = view.findViewById(R.id.tvHeightVitalHistoryForSinglePatientDetails)
        tvWeightVital = view.findViewById(R.id.tvWeightVitalHistoryForSinglePatientDetails)
        pbVitalHistoryForSinglePatientDetails =
            view.findViewById(R.id.pbVitalHistoryForSinglePatientDetails)
        tvVitalsNotAvailable = view.findViewById(R.id.tvVitalsNotAvailablePatientDetails)
        llVitals = view.findViewById(R.id.llVitals)

        try {
            if (patientId != null) {
                viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                    if (Constants.isInternetConnected(mContext)) {
                        getVitalsHistory()
                    } else {
                        withContext(Dispatchers.Main)
                        {
                            if (isVisible) {
                                pbVitalHistoryForSinglePatientDetails.visibility = View.GONE
                                tvVitalsNotAvailable.visibility = View.VISIBLE
                                llVitals.visibility = View.GONE
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


    private fun getVitalsHistory() {
        try {
            pbVitalHistoryForSinglePatientDetails.visibility = View.VISIBLE
            tvVitalsNotAvailable.visibility = View.GONE
            val userData = SharedPrefs.getUserData(mContext)
            val username = userData!!.username
            val usertype = "DOCTOR"
            ApiUtils.getAPIService(mContext).GetVitalsSingleRequest(
                mAppointmentId.toString(),
                patientId!!,
                SharedPrefs.getString(mContext, "sessionid")!!,
                username!!,
                usertype
            ).enqueue(object : Callback<GetVitalsResponse> {
                override fun onFailure(call: Call<GetVitalsResponse>, t: Throwable) {
                    if (isVisible) {
                        pbVitalHistoryForSinglePatientDetails.visibility = View.GONE
                        tvVitalsNotAvailable.visibility = View.VISIBLE
                        llVitals.visibility = View.GONE
                        Toast.makeText(
                            mContext,
                            "Unable to get vitals",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onResponse(
                    call: Call<GetVitalsResponse>, response: Response<GetVitalsResponse>
                ) {
                    if (isVisible) {
                        pbVitalHistoryForSinglePatientDetails.visibility = View.GONE
                        llVitals.visibility = View.VISIBLE
                        if (response.isSuccessful) {
                            val vitalsList: ArrayList<VitalsData> = ArrayList()
                            vitalsList.addAll(response.body()!!.data!!.vitals!!)
                            if (vitalsList.size == 0) {
                                tvVitalsNotAvailable.visibility = View.VISIBLE
                                llVitals.visibility = View.GONE
                            } else {
                                tvVitalsNotAvailable.visibility = View.GONE
                                with(vitalsList[0]) {
                                    try {
                                        val notAvailable = "n/a"
                                        if (temprature_c.equals(null) || temprature_c == "") {
                                            if (!temprature_f.equals(null) && temprature_f != "") {
                                                tvTemperatureVital.text = temprature_f + "°F"
                                            } else {
                                                tvTemperatureVital.text = "$notAvailable °F"
                                            }
                                        } else if (temprature_f.equals(null) || temprature_f == "") {
                                            if (!temprature_c.equals(null) && temprature_c != "") {
                                                tvTemperatureVital.text = temprature_c + "°C"
                                            } else {
                                                tvTemperatureVital.text = "$notAvailable °C"
                                            }
                                        }

                                        if (!bps.equals(null) && bps != "" && !bpd.equals(null) && bpd != "") {
                                            tvBloodPressureVital.text = bps + "/" + bpd + " mmHG"
                                        } else {
                                            if (!bps.equals(null) && bps != "") {
                                                if (!bpd.equals(null) && bpd != "") {
                                                    tvBloodPressureVital.text =
                                                        bps + "/" + bpd + " mmHG"
                                                } else {
                                                    tvBloodPressureVital.text =
                                                        bps + "/" + notAvailable + " mmHG"
                                                }
                                            } else {
                                                if (!bpd.equals(null) && bpd != "") {
                                                    tvBloodPressureVital.text =
                                                        notAvailable + "/" + bpd + " mmHG"
                                                } else {
                                                    tvBloodPressureVital.text =
                                                        notAvailable + "/" + notAvailable + " mmHG"
                                                }
                                            }
                                        }

                                        if (!sugar.equals(null) && sugar != ("") && !sugar_type.equals(
                                                null
                                            ) && sugar_type != ("")
                                        ) {
                                            tvSugarVital.text =
                                                sugar + " mg/dl" + "\n" + sugar_type + ""
                                        } else {
                                            if (!sugar.equals(null) && sugar != ("")) {
                                                if (!sugar_type.equals(null) && sugar_type != ("")) {
                                                    tvSugarVital.text =
                                                        sugar + " mg/dl" + "\n" + sugar_type + ""
                                                } else {
                                                    tvSugarVital.text =
                                                        sugar + " mg/dl" + "\n" + notAvailable + ""
                                                }
                                            } else {
                                                if (!sugar_type.equals(null) && sugar_type != ("")) {
                                                    tvSugarVital.text =
                                                        notAvailable + " mg/dl" + "\n" + sugar_type + ""
                                                } else {
                                                    tvSugarVital.text =
                                                        notAvailable + " mg/dl" + "\n" + notAvailable + ""
                                                }
                                            }
                                        }

                                        if (height != null) {
                                            tvHeightVital.text = height
                                        }

                                        if (weight != null && !weight.equals(null) && weight == "") {
                                            tvWeightVital.text = weight + " kg"
                                        } else {
                                            tvWeightVital.text = "$notAvailable kg"
                                        }

                                        if (height_cm.equals(null) || height_cm == "") {
                                            if (!height_ft.equals(null) && height_ft != "" && !height_inch.equals(
                                                    null
                                                ) && height_inch != ""
                                            ) {
                                                tvHeightVital.text =
                                                    height_ft + " " + " ft " + height_inch + " inch"
                                            } else {
                                                if (!height_ft.equals(null) && height_ft != "") {
                                                    if (!height_inch.equals(null) && height_inch != "") {
                                                        tvHeightVital.text =
                                                            height_ft + " " + " ft " + height_inch + " inch"
                                                    } else {
                                                        tvHeightVital.text =
                                                            height_ft + " " + " ft " + "$notAvailable" + " inch"
                                                    }
                                                } else {
                                                    if (!height_inch.equals(null) && height_inch != "") {
                                                        tvHeightVital.text =
                                                            notAvailable + " " + " ft " + height_inch + " inch"
                                                    } else {
                                                        tvHeightVital.text =
                                                            notAvailable + " " + " ft " + "$notAvailable" + " inch"
                                                    }
                                                }
                                            }

                                        } else if (height_inch.equals(null) || height_inch == "") {
                                            if (!height_cm.equals(null) && height_cm != "") {
                                                tvHeightVital.text = height_cm + " cm"
                                            } else {
                                                tvHeightVital.text = notAvailable + " cm"
                                            }
                                        }

                                        if (weight_kg.equals(null) || weight_kg == "") {
                                            if (!weight_lb.equals(null) && weight_lb != "") {
                                                tvWeightVital.text = weight_lb + " lb"
                                            } else {
                                                tvWeightVital.text = notAvailable + " lb"
                                            }
                                        } else if (weight_lb.equals(null) || weight_lb == "") {
                                            if (!weight_kg.equals(null) && weight_kg != "") {
                                                tvWeightVital.text = weight_kg + " Kg"
                                            } else {
                                                tvWeightVital.text = notAvailable + " Kg"
                                            }
                                        }

                                        if (breathing2.equals(null) || breathing2 == "") {
                                            if (breathing == "" || breathing.equals(null))
                                                tvBreathingVital.text = "n/a bpm"
                                            else
                                                tvBreathingVital.text = breathing + " bpm"
                                        } else {
                                            if (breathing == "" || breathing.equals(null))
                                                tvBreathingVital.text = notAvailable + " bpm"
                                            else
                                                tvBreathingVital.text =
                                                    breathing + " to " + breathing2 + " bpm"
                                        }

                                        if (!oxygen2.equals(null) && oxygen2 != "") {
                                            if (oxygen != null && !oxygen.equals(null) && oxygen != "")
                                                tvOxygenVital.text = oxygen + " SPO2"
                                            else
                                                tvOxygenVital.text = notAvailable + " SPO2"
                                        } else {
                                            if (oxygen != null && !oxygen.equals(null) && oxygen != "")
                                                tvOxygenVital.text = oxygen + " SPO2"
                                            else
                                                if (oxygen2 != null && !oxygen2.equals(null) && oxygen2 != "")
                                                    tvOxygenVital.text = oxygen2 + " SPO2"
                                                else
                                                    tvOxygenVital.text = notAvailable + " SPO2"

                                        }
                                        if (pulse2.equals(null) || pulse2 == "") {
                                            if (!pulse.equals(null) && pulse != "")
                                                tvPulseVital.text = pulse + " bpm"
                                            else
                                                tvPulseVital.text = notAvailable + " bpm"
                                        } else {
                                            if (!pulse.equals(null) && pulse != "")
                                                tvPulseVital.text =
                                                    notAvailable + " to " + pulse2 + " bpm"
                                            else
                                                tvPulseVital.text = pulse + " to " + pulse2 + " bpm"
                                        }
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }
                                }
                            }

                        } else {
                            Toast.makeText(
                                context,
                                "Something went wrong, please try again later",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}
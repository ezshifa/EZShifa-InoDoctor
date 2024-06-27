package com.app.ehealthai.doctor.ui.activities.PatientDetails.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.app.ehealthai.doctor.ui.activities.PatientDetails.PatientAppointment
import com.app.ehealthaidoctor.R
import com.app.ehealthaidoctor.network.ApiUtils
import com.example.ehealthai.utils.Constants
import com.example.example.PatientScreeningResponse
import kotlinx.android.synthetic.main.fragment_patient_screening.tvLe4000
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PatientScreeningFragment(pId: String?, apptId: String?, context: PatientAppointment) :
    Fragment() {

    private var mContext: PatientAppointment
    private var patientId: String? = null
    private var mAppointmentId: String? = null

    private lateinit var tvLeftEar: TextView
    private lateinit var tvLeftEar250: TextView
    private lateinit var tvLeftEar500: TextView
    private lateinit var tvLeftEar1000: TextView
    private lateinit var tvLeftEar2000: TextView
    private lateinit var tvLeftEar3000: TextView
    private lateinit var tvLeftEar4000: TextView
    private lateinit var tvLeftEar6000: TextView
    private lateinit var tvLeftEar8000: TextView
    private lateinit var hsvHorizontalLeftEar: HorizontalScrollView

    private lateinit var tvRightEar: TextView
    private lateinit var tvRightEar250: TextView
    private lateinit var tvRightEar500: TextView
    private lateinit var tvRightEar1000: TextView
    private lateinit var tvRightEar2000: TextView
    private lateinit var tvRightEar3000: TextView
    private lateinit var tvRightEar4000: TextView
    private lateinit var tvRightEar6000: TextView
    private lateinit var tvRightEar8000: TextView
    private lateinit var hsvHorizontalRightEar: HorizontalScrollView


    private lateinit var tvLeftEye: TextView
    private lateinit var tvRightEye: TextView
    private lateinit var tvColorBlindness: TextView
    private lateinit var tvScreeningDataNotAvailable: TextView
    private lateinit var pbLoadingScreeningData: ProgressBar
    private lateinit var llScreeningValues: LinearLayoutCompat

    private lateinit var tvEarResult: TextView
    private lateinit var tvEyeResult: TextView
    private lateinit var tvColorVisionResult: TextView
    private val soundUnit="dB"
    init {
        patientId = pId
        mContext = context
        mAppointmentId = apptId
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_patient_screening, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvLeftEar = view.findViewById(R.id.tvLeftEarValue)
        tvLeftEar250 = view.findViewById(R.id.tvLe250)
        tvLeftEar500 = view.findViewById(R.id.tvLe500)
        tvLeftEar1000 = view.findViewById(R.id.tvLe1000)
        tvLeftEar2000 = view.findViewById(R.id.tvLe2000)
        tvLeftEar3000 = view.findViewById(R.id.tvLe3000)
        tvLeftEar4000 = view.findViewById(R.id.tvLe4000)
        tvLeftEar6000 = view.findViewById(R.id.tvLe6000)
        tvLeftEar8000 = view.findViewById(R.id.tvLe8000)
        hsvHorizontalLeftEar = view.findViewById(R.id.hsvLeftEar)

        tvRightEar = view.findViewById(R.id.tvRightEarValue)
        tvRightEar250 = view.findViewById(R.id.tvRe250)
        tvRightEar500 = view.findViewById(R.id.tvRe500)
        tvRightEar1000 = view.findViewById(R.id.tvRe1000)
        tvRightEar2000 = view.findViewById(R.id.tvRe2000)
        tvRightEar3000 = view.findViewById(R.id.tvRe3000)
        tvRightEar4000 = view.findViewById(R.id.tvRe4000)
        tvRightEar6000 = view.findViewById(R.id.tvRe6000)
        tvRightEar8000 = view.findViewById(R.id.tvRe8000)
        hsvHorizontalRightEar = view.findViewById(R.id.hsvRightEar)

        tvLeftEye = view.findViewById(R.id.tvLeftEyeValue)
        tvRightEye = view.findViewById(R.id.tvRightEyeValue)
        tvColorBlindness = view.findViewById(R.id.tvColorBlindnessValue)
        tvScreeningDataNotAvailable = view.findViewById(R.id.tvScreeningNotAvailablePatientDetails)
        pbLoadingScreeningData = view.findViewById(R.id.pbScreeningPatientDetails)
        llScreeningValues = view.findViewById(R.id.llScreeningValuesPatientDetails)

        tvEarResult = view.findViewById(R.id.tvEarResultValue)
        tvEyeResult = view.findViewById(R.id.tvEyeResultValue)
        tvColorVisionResult = view.findViewById(R.id.tvColorVisionResultValue)


        try {
            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                if (!patientId.isNullOrEmpty() && !mAppointmentId.isNullOrEmpty()) {
                    if (Constants.isInternetConnected(mContext)) {
                        pbLoadingScreeningData.visibility = View.VISIBLE
                        getPatientScreeningData(patientId!!, mAppointmentId!!)
                    }
                    else {
                        withContext(Dispatchers.Main)
                        {
                            if (isVisible) {
                                pbLoadingScreeningData.visibility = View.GONE
                                tvScreeningDataNotAvailable.visibility = View.VISIBLE
                                llScreeningValues.visibility = View.GONE
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
    private fun getPatientScreeningData(patientId: String, appointmentId: String) {
        ApiUtils.getAPIService(mContext).getPatientScreeningData(
            patientId, appointmentId
        ).enqueue(object : Callback<PatientScreeningResponse> {
            override fun onFailure(call: Call<PatientScreeningResponse>, t: Throwable) {
                if (isVisible) {
                    pbLoadingScreeningData.visibility = View.GONE
                    tvScreeningDataNotAvailable.visibility = View.VISIBLE
                    llScreeningValues.visibility = View.GONE
                        Toast.makeText(mContext, "Unable to get screening data", Toast.LENGTH_SHORT)
                            .show()
                }
                //Do nothing
            }

            override fun onResponse(
                call: Call<PatientScreeningResponse>, response: Response<PatientScreeningResponse>
            ) {
                try {
                    if (isVisible) {
                        pbLoadingScreeningData.visibility = View.GONE
                        if (response != null) {
                            if (response.body() != null && response.body()!!.statuscode != null && response.body()!!.statuscode == 200) {

                                if (response.body()!!.patientScreeningResponseData != null) {
                                    if (response.body()!!.patientScreeningResponseData!!.patientScreeningResponseDataList != null) {
                                        if (response.body()!!.patientScreeningResponseData!!.patientScreeningResponseDataList.size > 0) {
                                            llScreeningValues.visibility = View.VISIBLE
                                            tvLeftEye.setText(
                                                response.body()!!.patientScreeningResponseData?.patientScreeningResponseDataList?.get(
                                                    0
                                                )?.leftEyeTest.toString()
                                            )
                                            tvRightEye.setText(
                                                response.body()!!.patientScreeningResponseData?.patientScreeningResponseDataList?.get(
                                                    0
                                                )?.rightEyeTest.toString()
                                            )
                                            if (response.body()!!.patientScreeningResponseData?.patientScreeningResponseDataList?.get(
                                                    0
                                                )?.hearingTestLeft.toString() == ""
                                            ) {
                                                tvLeftEar.setText(
                                                    "Prefect"
                                                )
                                            } else {
                                                tvLeftEar.setText(
                                                    "Checkup recommended"
                                                )
                                            }

                                            if (response.body()!!.patientScreeningResponseData?.patientScreeningResponseDataList?.get(
                                                    0
                                                )?.hearingTestRight.toString() == ""
                                            ) {
                                                tvRightEar.setText(
                                                    "Prefect"
                                                )
                                            } else {
                                                tvRightEar.setText(
                                                    "Checkup recommended"
                                                )
                                            }
                                            tvColorBlindness.setText(
                                                response.body()!!.patientScreeningResponseData?.patientScreeningResponseDataList?.get(
                                                    0
                                                )?.colorBlind.toString()
                                            )


                                            if(response.body()!!.patientScreeningResponseData?.patientScreeningResponseDataList?.get(0)?.hearingData!=null)
                                            {
                                                if(response.body()!!.patientScreeningResponseData?.patientScreeningResponseDataList?.get(0)?.hearingData!=null
                                                    && response.body()!!.patientScreeningResponseData?.patientScreeningResponseDataList?.get(0)?.hearingData?.leftEar!=null)
                                                {
                                                    with(response.body()!!.patientScreeningResponseData?.patientScreeningResponseDataList?.get(0)?.hearingData?.leftEar)
                                                    {
                                                        tvLeftEar250.text = "250Hz/".plus(this?.F250Hz).plus(soundUnit)
                                                        tvLeftEar500.text = "500Hz/".plus(this?.F500Hz).plus(soundUnit)
                                                        tvLeftEar1000.text = "1000Hz/".plus(this?.F1000Hz).plus(soundUnit)
                                                        tvLeftEar2000.text = "2000Hz/".plus(this?.F2000Hz).plus(soundUnit)
                                                        tvLeftEar3000.text = "3000Hz/".plus(this?.F3000Hz).plus(soundUnit)
                                                        tvLeftEar4000.text = "4000Hz/".plus(this?.F4000Hz).plus(soundUnit)
                                                        tvLeftEar6000.text = "6000Hz/".plus(this?.F6000Hz).plus(soundUnit)
                                                        tvLeftEar8000.text = "8000Hz/".plus(this?.F8000Hz).plus(soundUnit)
                                                    }
                                                }else
                                                {
                                                    //hide left ear data
                                                    hsvHorizontalLeftEar.visibility=View.GONE
                                                }

                                                if(response.body()!!.patientScreeningResponseData?.patientScreeningResponseDataList?.get(0)?.hearingData!=null
                                                    && response.body()!!.patientScreeningResponseData?.patientScreeningResponseDataList?.get(0)?.hearingData?.rightEar!=null)
                                                {

                                                    with(response.body()!!.patientScreeningResponseData?.patientScreeningResponseDataList?.get(0)?.hearingData?.rightEar)
                                                    {

                                                        tvRightEar250.text = "250Hz/".plus(this?.F250Hz).plus(soundUnit)
                                                        tvRightEar500.text = "500Hz/".plus(this?.F500Hz).plus(soundUnit)
                                                        tvRightEar1000.text = "1000Hz/".plus(this?.F1000Hz).plus(soundUnit)
                                                        tvRightEar2000.text = "2000Hz/".plus(this?.F2000Hz).plus(soundUnit)
                                                        tvRightEar3000.text = "3000Hz/".plus(this?.F3000Hz).plus(soundUnit)
                                                        tvRightEar4000.text = "4000Hz/".plus(this?.F4000Hz).plus(soundUnit)
                                                        tvRightEar6000.text = "6000Hz/".plus(this?.F6000Hz).plus(soundUnit)
                                                        tvRightEar8000.text = "8000Hz/".plus(this?.F8000Hz).plus(soundUnit)
                                                    }
                                                }else{
                                                    //hide right ear data
                                                    hsvHorizontalRightEar.visibility=View.GONE

                                                }


                                            }else
                                            {
                                                hsvHorizontalLeftEar.visibility=View.GONE
                                                hsvHorizontalRightEar.visibility=View.GONE

                                                //hide right ear and left ear data
                                            }

                                            val mResponse = response.body()!!.patientScreeningResponseData?.patientScreeningResponseDataList!!.get(0)
                                            setScreeningResults(
                                                mResponse.leftEyeTest!!.toString(),
                                                mResponse.rightEyeTest!!.toString(),
                                                mResponse.hearingTestLeft.toString(),
                                                mResponse.hearingTestRight.toString(),
                                                mResponse.colorBlind.toString()
                                            )
                                        } else {
                                            tvScreeningDataNotAvailable.visibility = View.VISIBLE
                                            llScreeningValues.visibility = View.GONE
                                        }
                                    } else {
                                        tvScreeningDataNotAvailable.visibility = View.VISIBLE
                                        llScreeningValues.visibility = View.GONE
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

    fun setScreeningResults(
        leftEyeValue: String,
        RightEyeValue: String,
        leftEarValue: String,
        RighEarValue: String,
        ColorVisionValue: String
    ) {
        try {
            val eyeValuesArray = arrayOf("20/30", "20/25", "20/20")

            if (eyeValuesArray.contains(leftEyeValue) && eyeValuesArray.contains(RightEyeValue)) {
                tvEyeResult.text = "EyeSight is Normal"
            } else {
                tvEyeResult.text = "Eye Check-up is Recommended"

            }
            if (leftEarValue.toLowerCase() == "yes" || RighEarValue.toLowerCase() == "yes") {
                tvEarResult.text = "Hearing test is Recommended"
            } else {
                tvEarResult.text = "Hearing is Normal"
            }
            if (ColorVisionValue.toLowerCase() == "yes") {
                tvColorVisionResult.text = "Color vision Deficiency"
            } else {
                tvColorVisionResult.text = "Color vision is Normal"
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }


    }

}
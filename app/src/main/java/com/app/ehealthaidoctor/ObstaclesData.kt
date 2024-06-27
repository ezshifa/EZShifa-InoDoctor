package com.app.ehealthaidoctor

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.ehealthai.doctor.adapters.ChildDetailsAdapter
import com.app.ehealthai.doctor.models.responses.getOstacleDataREsponse.Child
import com.app.ehealthai.doctor.models.responses.getOstacleDataREsponse.GetOstacleDataREsponse
import com.app.ehealthai.doctor.ui.activities.PatientDetails.PatientAppointment
import com.app.ehealthaidoctor.network.ApiInterface
import com.app.ehealthaidoctor.ui.activities.HomeActivity
import com.app.ehealthaidoctor.utils.SharedPrefs
import kotlinx.android.synthetic.main.fragment_obstacles_data.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Collections
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit


class ObstaclesData : Fragment() {
    var childListDataAfterApi: ArrayList<Child> = ArrayList()
    var adapter: ChildDetailsAdapter? = null
    lateinit var context: PatientAppointment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = activity as PatientAppointment
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_obstacles_data, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userData = SharedPrefs.getUserData(context)
//        val patientId = userData!!.id
        val patientId = PatientAppointment.patientId
//        var patientId = 123
        Log.i("patientId", patientId.toString())
        if (patientId != null) {
            callingGetChildDataApi(patientId)
        }
        av_home.setOnClickListener {
            val intent = Intent(context, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }
    }

    private fun callingGetChildDataApi(patientId: String) {

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        var client: OkHttpClient? = OkHttpClient()
        client = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.MINUTES)
            .addInterceptor(interceptor)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://ezshifa.com/emr/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client) // Set HttpClient to be used by Retrofit
            .build()
        val apiInterface: ApiInterface = retrofit.create(ApiInterface::class.java)
        val call1: Call<GetOstacleDataREsponse> =
            apiInterface.getPatientChildListResponse(patientId)

        try {
            call1.enqueue(object : Callback<GetOstacleDataREsponse?> {
                override fun onResponse(
                    call: Call<GetOstacleDataREsponse?>,
                    response: Response<GetOstacleDataREsponse?>
                ) {


                    if (response.code().equals("502")) {
                        Toast.makeText(
                            context,
                            "Server Down",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    if (response != null && response.body() != null && response.body()?.data != null
                        && response.body()?.data?.obsData != null && response.code() != 404) {


                        if (response.body()?.data?.obsData?.size!! > 0) {


                            if (response.body()?.data?.obsData?.get(0)?.cousionMarriage != null) {

                                try {
                                    tv_cousin_marriage.text =
                                        response.body()?.data?.obsData?.get(0)?.cousionMarriage.toString()
                                } catch (ex: Exception) {
                                }

                            } else {
                                tv_cousin_marriage.text = "Not Provided"
                            }

                            if (response.body()?.data?.obsData?.get(0)?.marriagePeriod != null) {

                                try {
                                    tvMarriedFor.text =
                                        response.body()?.data?.obsData?.get(0)?.marriagePeriod.toString()
                                } catch (ex: Exception) {
                                }

                            } else {
                                tvMarriedFor.text = "Not Provided"
                            }



                            if (response.body()?.data?.obsData?.get(0)?.husbandProfession != null) {

                                try {
                                    tvHusbandProfession.text =
                                        response.body()?.data?.obsData?.get(0)?.husbandProfession.toString()
                                } catch (ex: Exception) {
                                }

                            } else {
                                tvHusbandProfession.text = "Not Provided"
                            }

                            if (response.body()?.data?.obsData?.get(0)?.abortion != null) {

                                try {
                                    tvAbortion.text =
                                        response.body()?.data?.obsData?.get(0)?.abortion.toString()
                                } catch (ex: Exception) {
                                }

                            } else {
                                tvAbortion.text = "Not Provided"
                            }
                            if (response.body()?.data?.obsData?.get(0)?.abortionReason != null) {

                                try {
                                    tvAbortionReason.text =
                                        response.body()?.data?.obsData?.get(0)?.abortionReason.toString()
                                } catch (ex: Exception) {
                                }

                            } else {
                                tvAbortionReason.text = "Not Provided"
                            }

                            if (!response.body()?.data?.obsData?.get(0)?.kidsAlive.equals("")) {

                            }
                            if (response.body()?.data?.obsData?.get(0)?.lastManstrualDate != null) {

                                try {
                                    lastmenstrualdate.setText(response.body()?.data?.obsData?.get(0)?.lastManstrualDate)
                                    findDateDifference(response.body()?.data?.obsData?.get(0)?.lastManstrualDate.toString())
                                } catch (ex: Exception) {
                                }
                            } else {
                                lastmenstrualdate.setText("Not Provided")
                            }

                            /*        if (response.body()?.data?.obsData?.get(0)?.birthAnomilies != null) {

                                        try {
                                            tv_birthAnomilies.setText(
                                                response.body()?.data?.obsData?.get(
                                                    0
                                                )?.birthAnomilies
                                            )
                                        } catch (ex: Exception) {
                                        }

                                    } else {
                                        tv_birthAnomilies.setText("Not Provided")
                                    }*/

                            if (response.body()?.data?.obsData?.get(0)?.birthStatus != null) {

                                try {
                                    tv_birth_status.setText(response.body()?.data?.obsData?.get(0)?.birthStatus.toString())

                                } catch (ex: Exception) {
                                }
                            } else {
                                tv_birth_status.setText("Not Provided")
                            }


                            if (response.body()?.data?.obsData?.get(0)?.pregnancyProblem != null) {
                                try {
                                    tv_complications.setText(response.body()?.data?.obsData?.get(0)?.pregnancyProblem.toString())

                                } catch (ex: Exception) {
                                }
                            } else {
                                tv_complications.setText("Not Provided")
                            }


                            if (response.body()?.data?.obsData?.get(0)?.motherStat != null) {

                                try {
                                    tv_preMorbids.setText(response.body()?.data?.obsData?.get(0)?.motherStat.toString())

                                } catch (ex: Exception) {
                                }
                            } else {
                                tv_preMorbids.setText("Not Provided")
                            }

                            if (response.body()?.data?.obsData?.get(0)?.husbandBloodGroup != null) {
                                try {
                                    tv_husbandBloodGroup.setText(
                                        response.body()?.data?.obsData?.get(
                                            0
                                        )?.husbandBloodGroup.toString()
                                    )
                                } catch (ex: Exception) {
                                }


                            } else {
                                tv_husbandBloodGroup.setText("Not Provided")
                            }

                            if (response.body()?.data?.obsData?.get(0)?.patient_bloodgroup != null) {
                                try {
                                    tv_Patient_blood_group.setText(
                                        response.body()?.data?.obsData?.get(
                                            0
                                        )?.patient_bloodgroup.toString()
                                    )
                                } catch (ex: Exception) {
                                }


                            } else {
                                tv_Patient_blood_group.setText("Not Provided")
                            }

                            if (response.body()?.data?.obsData?.get(0)?.patientNature != null) {

                                try {
                                    tv_patient_profession.setText(
                                        response.body()?.data?.obsData?.get(
                                            0
                                        )?.patientNature.toString()
                                    )
                                } catch (ex: Exception) {
                                }

                            } else {
                                tv_patient_profession.setText("Not Provided")
                            }


                        }


                        if (response.body()?.data?.childlist?.size!! > 0) {
                            for (i in 0 until response.body()?.data?.childlist?.size!!) {
                                childListDataAfterApi.add(
                                    response.body()?.data?.childlist?.get(
                                        i
                                    )!!
                                )
                            }
                            try {
                                rv_childData.setHasFixedSize(true)
                                childListDataAfterApi.reverse()
                                val mLayoutManager = LinearLayoutManager(context)
                                mLayoutManager.reverseLayout = true
                                mLayoutManager.stackFromEnd = true
                                rv_childData.layoutManager = LinearLayoutManager(
                                    context,
                                    LinearLayoutManager.HORIZONTAL,
                                    false
                                )
                                adapter =
                                    ChildDetailsAdapter(
                                        context,
                                        childListDataAfterApi
                                    )
                                rv_childData.adapter = adapter
                            } catch (ex: Exception) {
                            }

                        } else {
                            try {
                                if (headinng_childs != null)
                                    headinng_childs.visibility = View.GONE
                                if (rv_childData != null)
                                    rv_childData.visibility = View.GONE
                            } catch (e: Exception) {

                            }
                        }

                    } else {
                        Log.i("getDetailsResp", response.message().toString())
                    }

                }

                override fun onFailure(call: Call<GetOstacleDataREsponse?>, t: Throwable) {

                    Log.i("failure", "onFailure called $t")
                    Toast.makeText(
                        context,
                        "Something went wrong please try again later ",
                        Toast.LENGTH_SHORT
                    ).show()
                    call.cancel()
                }
            })
        } catch (ex: Exception) {
            Log.i("exveeee", ex.toString())

        }
    }

    fun findDateDifference(dateString: String) {
//        val dateString = "2023-09-15" // Replace with your date string
        if (!dateString.isNullOrEmpty()) {
            try {
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
                var date: Date? = null


                date = dateFormat.parse(dateString)


                val currentDate: Calendar = Calendar.getInstance()
                val providedDate: Calendar = Calendar.getInstance()
                providedDate.time = date

                val timeDifferenceMillis: Long =
                    currentDate.getTimeInMillis() - providedDate.getTimeInMillis()

                // Calculate the difference in days

                val daysDifference = TimeUnit.MILLISECONDS.toDays(timeDifferenceMillis)
                if (daysDifference < 90) {
                    tv_currenttrimestered.text = "1st Trimester"
                } else if (daysDifference > 90 && daysDifference < 180) {
                    tv_currenttrimestered.text = "2st Trimester"

                } else if (daysDifference > 180) {
                    tv_currenttrimestered.text = "3rd Trimester"

                }
                Log.e("TAG", "findDateDifference: $daysDifference")
            } catch (e: Exception) {
                e.printStackTrace()
                // Handle parsing error here
            }
        }
    }

}
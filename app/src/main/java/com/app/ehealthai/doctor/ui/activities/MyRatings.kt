package com.app.ehealthai.doctor.ui.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.ehealthai.doctor.adapters.recyclerViewAdapters.MyRatingsRecyclerViewAdapter
import com.app.ehealthai.doctor.models.responses.FeedbackData
import com.app.ehealthai.doctor.models.responses.GetDoctorFeedbackResponse
import com.app.ehealthai.doctor.models.responses.GetRatingResponse
import com.app.ehealthaidoctor.R
import com.app.ehealthaidoctor.network.ApiUtils
import com.app.ehealthaidoctor.utils.SharedPrefs
import com.crashlytics.android.Crashlytics
//import com.crashlytics.android.Crashlytics
import com.example.ehealthai.utils.toast
import io.fabric.sdk.android.Fabric
//import io.fabric.sdk.android.Fabric
import kotlinx.android.synthetic.main.myratings.*
import retrofit2.Call
import retrofit2.Response

class MyRatings: AppCompatActivity()
{
    var mrRecyclerViewAdapter: MyRatingsRecyclerViewAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fabric.with(this, Crashlytics())
        setContentView(R.layout.myratings)
        my_ratings_back_btn.setOnClickListener {
            onBackPressed()
        }
        mrlistview.layoutManager = LinearLayoutManager(this@MyRatings)
        progress_load_mr.visibility = View.VISIBLE
        getAvgRating()
        getFeedbacks()
    }
    fun getFeedbacks() {
        val userData = SharedPrefs.getUserData(this)

        val username=userData!!.username
        val usertype="DOCTOR"
        ApiUtils.getAPIService(this@MyRatings).GetDoctorfeedback(userData!!.id.toString(),SharedPrefs.getString(this@MyRatings, "sessionid")!!,username!!,usertype)
                .enqueue(object : retrofit2.Callback<GetDoctorFeedbackResponse> {
                    override fun onFailure(call: Call<GetDoctorFeedbackResponse>, t: Throwable) {
                        progress_load_mr.visibility = View.GONE
                        toast("Something went wrong, please try again later")
                    }

                    override fun onResponse(call: Call<GetDoctorFeedbackResponse>, response: Response<GetDoctorFeedbackResponse>) {
                        progress_load_mr.visibility =View.GONE

                        if (response.body()!!.statuscode.equals(404)) {
                            toast(response.body()!!.data!!.error)
                        }
                        else if (response.isSuccessful) {
                            val vitalsList: ArrayList<FeedbackData> = ArrayList()
                            vitalsList.addAll(response.body()!!.data!!.rating!!)
                            tf.text="Reviews : "+vitalsList.size.toString()
                            if(vitalsList.size==0)
                            {
                                emptytextmr.visibility=View.VISIBLE
                            }
                            else {
                                mrRecyclerViewAdapter =
                                        MyRatingsRecyclerViewAdapter(this@MyRatings, response.body()!!.data!!.rating!!)
                                mrlistview.adapter = mrRecyclerViewAdapter
                            }

                        } else {
                            toast("Something went wrong, please try again later")
                        }
                    }
                })


    }
    fun getAvgRating() {
        val userData = SharedPrefs.getUserData(this)
        val username=userData!!.username
        val usertype="DOCTOR"
        ApiUtils.getAPIService(this@MyRatings).GetRating(userData!!.id.toString(),SharedPrefs.getString(this@MyRatings, "sessionid")!!,username!!,usertype)
                .enqueue(object : retrofit2.Callback<GetRatingResponse> {
                    override fun onFailure(call: Call<GetRatingResponse>, t: Throwable) {
                        progress_load_mr.visibility = View.GONE
                        toast("Something went wrong, please try again later")
                    }

                    override fun onResponse(call: Call<GetRatingResponse>, response: Response<GetRatingResponse>) {
                        progress_load_mr.visibility =View.GONE
                        if (response.isSuccessful) {
                            if(response.body()!!.data.rating.isNullOrEmpty())
                            {
                                oratinga.text = "Avg Rating : " + "0/5"
                            }
                            else {
                                oratinga.text = "Avg Rating : " + response.body()!!.data.rating + "/5"
                            }

                        } else {
                            toast("Something went wrong, please try again later")
                        }
                    }
                })


    }
}
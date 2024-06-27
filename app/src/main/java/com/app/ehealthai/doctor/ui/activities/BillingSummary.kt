package com.app.ehealthaidoctor.ui.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.ehealthai.adapters.recyclerViewAdapters.BillingSummaryRecyclerViewAdapter
import com.app.ehealthai.models.responses.PatientBillingResponse
import com.app.ehealthai.models.responses.summary
import com.app.ehealthaidoctor.R
import com.app.ehealthaidoctor.network.ApiUtils
import com.app.ehealthaidoctor.utils.SharedPrefs
import com.crashlytics.android.Crashlytics
import com.example.ehealthai.utils.toast
import io.fabric.sdk.android.Fabric
import kotlinx.android.synthetic.main.activity_billing.bs_back_btn
import kotlinx.android.synthetic.main.activity_billing.bs_progress_bar
import kotlinx.android.synthetic.main.activity_billing.bsmblistview
import kotlinx.android.synthetic.main.activity_billing.cvtext
import kotlinx.android.synthetic.main.activity_billing.emptytextca
import kotlinx.android.synthetic.main.activity_billing.mtext
import kotlinx.android.synthetic.main.activity_billing.tdtext
import retrofit2.Call
import retrofit2.Response
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date


class BillingSummary : AppCompatActivity()
{
    var billingsummaryRecyclerViewAdapter: BillingSummaryRecyclerViewAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fabric.with(this, Crashlytics())
        setContentView(R.layout.activity_billing)
        bs_back_btn.setOnClickListener { onBackPressed() }
        bsmblistview.layoutManager = LinearLayoutManager(this@BillingSummary)
        getBillingSummary()
    }
    fun getBillingSummary()
    {
       bs_progress_bar.visibility = View.VISIBLE
        val userData = SharedPrefs.getUserData(this)
        val sessionid = SharedPrefs.getString(this, "sessionid")

        val username=userData!!.username
        val usertype="DOCTOR"
        ApiUtils.getAPIService(this@BillingSummary).BillingSummary(sessionid!!,username!!,usertype)
            .enqueue(object : retrofit2.Callback<PatientBillingResponse> {
                override fun onFailure(call: Call<PatientBillingResponse>, t: Throwable) {
                    bs_progress_bar.visibility = View.GONE
                    toast("Something went wrong, please try again later")
                }

                override fun onResponse(call: Call<PatientBillingResponse>, response: Response<PatientBillingResponse>) {
                    bs_progress_bar.visibility = View.GONE
                    if (response.isSuccessful) {
                        val aiList: ArrayList<summary> = ArrayList()

                        aiList.addAll(response.body()!!.data!!.summary!!)
                        if(aiList.size==0)
                        {
                            emptytextca.visibility= View.VISIBLE
                        }
                        else {
                            var csvisits:Int=0
                            var tdrevenue:Int=0
                            var cmrevenue:Int=0
                            for(bs in response.body()!!.data!!.summary!!)
                            {
                                if(bs.status.equals("cancelled")||bs.status.equals("cancel"))
                                {
                                    csvisits++
                                }
                                tdrevenue=tdrevenue+bs.amount
                                val dtStart =bs.transcation_completed_on
                                val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                                try {
                                    val date: Date = format.parse(dtStart)
                                    val cal1: Calendar = Calendar.getInstance()
                                    val cal2: Calendar = Calendar.getInstance()
                                    cal1.setTime(date)
                                    cal2.setTime(Date())


                                    if (cal1.get(Calendar.MONTH) === cal2.get(Calendar.MONTH))
                                    { // the date falls in current month
                                        cmrevenue=cmrevenue+bs.amount
                                    }
                                } catch (e: ParseException) {

                                    toast("Something went wrong, please try again later")
                                }


                            }
                            mtext.text=""+cmrevenue.toString()+" Rs"
                            tdtext.text=""+tdrevenue.toString()+" Rs"
                            cvtext.text=""+csvisits.toString()
                            billingsummaryRecyclerViewAdapter = BillingSummaryRecyclerViewAdapter(this@BillingSummary, response.body()!!.data!!.summary!!)
                            bsmblistview.adapter = billingsummaryRecyclerViewAdapter
                        }

                    } else {
                        toast("Something went wrong, please try again later")
                    }
                }
            })

    }
}
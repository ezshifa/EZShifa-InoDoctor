package com.app.ehealthai.doctor.adapters.recyclerViewAdapters

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.app.ehealthai.doctor.models.responses.RemovePatientDiagnosisResponse
import com.app.ehealthai.doctor.ui.activities.PatientDetails.PatientAppointment
import com.app.ehealthai.doctor.utils.Global
import com.app.ehealthaidoctor.R
import com.app.ehealthaidoctor.network.ApiUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class dygTestAdapter(val context: Context, val chatsList: List<String>,
                     val toastLabelDiagnosisOrTest: String, val activity: Activity) :
        RecyclerView.Adapter<dygTestAdapter.ViewHolder>() {

    var dataList: ArrayList<String> = chatsList as ArrayList<String>

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.dyg_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.tv_giagnosisname.text = dataList[position]
        holder.iv_delete.setOnClickListener {
            callingDeleteTestApi(position,dataList[position],PatientAppointment.appointmentId.toString())
        }

    }

    //Remove test from Patient
    private fun callingDeleteTestApi(position: Int, testName: String, appointmentId: String) {

        Global.showProgressDialog(context as Activity?, "Please wait")
        //progress_1.visibility = View.VISIBLE
        ApiUtils.getAPIService(context).remove_patient_test(appointmentId, testName).enqueue(object : Callback<RemovePatientDiagnosisResponse> {
            override fun onFailure(call: Call<RemovePatientDiagnosisResponse>, t: Throwable) {
                //   progress_1.visibility = View.GONE

                Global.hideProgressDialog()
                Toast.makeText(context, "Something went wrong, please try again later", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                    call: Call<RemovePatientDiagnosisResponse>,
                    response: Response<RemovePatientDiagnosisResponse>
            ) {
                Global.hideProgressDialog()
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        response.body().let {
                            if (it?.statuscode == 200) {
                                deleteItem(position)
                                // progress_1.visibility = View.GONE
                                Toast.makeText(
                                        context,
                                        "$toastLabelDiagnosisOrTest deleted Successfully !",
                                        Toast.LENGTH_SHORT
                                ).show()
                            } else if (it?.statuscode == 404) {
                                val message = it.data?.message
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        })
    }


    // stores and recycles views as they are scrolled off screen
    class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tv_giagnosisname by lazy { itemView.findViewById<TextView>(R.id.tv_giagnosisname) }
        val iv_delete by lazy { itemView.findViewById<ImageView>(R.id.iv_delete) }
    }

    private fun deleteItem(position: Int) {
        dataList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, dataList.size)
    }
}
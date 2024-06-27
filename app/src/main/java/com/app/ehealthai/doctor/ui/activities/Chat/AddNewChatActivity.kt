package com.app.ehealthaidoctor.ui.activities.Chat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager

import com.app.ehealthaidoctor.R
import com.app.ehealthaidoctor.adapters.recyclerViewAdapters.AddChatRecyclerViewAdapter
import com.app.ehealthaidoctor.models.responses.AppointmentDoctorData
import com.app.ehealthaidoctor.models.responses.GetAppointmentDoctorList
import com.app.ehealthaidoctor.network.ApiUtils
import com.app.ehealthaidoctor.utils.SharedPrefs
import com.example.ehealthai.utils.toast
import kotlinx.android.synthetic.main.activity_add_new_chat.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddNewChatActivity : AppCompatActivity() {


    var addChatRecyclerViewAdapter: AddChatRecyclerViewAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_chat)

        init()
    }


    fun init() {
        new_chat_progress_bar.visibility = View.VISIBLE

        add_chat_back_btn.setOnClickListener { onBackPressed() }
        add_chat_recycler_view.layoutManager = LinearLayoutManager(this)

        val userData = SharedPrefs.getUserData(this)
        val username=userData!!.username
        val usertype="DOCTOR"
        ApiUtils.getAPIService(this).getAppointmentDoctors(userData!!.id.toString(),SharedPrefs.getString(this@AddNewChatActivity, "sessionid")!!,username!!,usertype)
            .enqueue(object : Callback<GetAppointmentDoctorList> {
                override fun onFailure(call: Call<GetAppointmentDoctorList>, t: Throwable) {
                    new_chat_progress_bar.visibility = View.GONE
                    toast("Something went wrong, please try again later")
                }

                override fun onResponse(
                    call: Call<GetAppointmentDoctorList>,
                    response: Response<GetAppointmentDoctorList>
                ) {
                    new_chat_progress_bar.visibility = View.GONE
                    try{
                        if (response.body()!!.statuscode.equals(404)) {
                            toast(response.body()!!.data!!.error)
                        }
                        else if (response.isSuccessful && response.body() != null && response.body()!!.data != null && !response.body()!!.data!!.list!!.isNullOrEmpty()) {

                        val appointmentList: ArrayList<AppointmentDoctorData> = ArrayList()
                        val idList: ArrayList<String> = ArrayList()

                        for (i in 0 until response.body()!!.data!!.list!!.size) {
                            if (!idList.contains(response.body()!!.data!!.list!!.get(i).id.toString())) {
                                appointmentList.add(response.body()!!.data!!.list!!.get(i))
                                idList.add(response.body()!!.data!!.list!!.get(i).id.toString())
                            }
                        }

                        addChatRecyclerViewAdapter = AddChatRecyclerViewAdapter(
                            this@AddNewChatActivity,
                            appointmentList,
                            startChat = { patientId, patientName, image_name ->
                                val intent = Intent(this@AddNewChatActivity, ChatActivity::class.java)
                                intent.putExtra("doctorId", userData.id.toString())
                                intent.putExtra("patientId", patientId)
                                intent.putExtra("patientName", patientName)
                                intent.putExtra("image_name", image_name)
                                intent.putExtra("image_name_p", image_name)
                               // intent.putExtra("image_name", userData.image_name)
                                startActivity(intent)
                                finish()
                            })
                        add_chat_recycler_view.adapter = addChatRecyclerViewAdapter

                    } else {
                        toast("Sorry! No doctors found")
                    }

                }catch(e:Exception){
                    toast("Something went wrong, please try again later")
                }
                }
            })

    }
}

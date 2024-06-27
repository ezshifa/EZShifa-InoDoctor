package com.app.ehealthaidoctor.ui.activities

import android.app.DatePickerDialog

import android.os.Bundle

import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity

import com.app.ehealthai.doctor.models.responses.MakeAppointmentResponse
import com.app.ehealthai.doctor.models.responses.RegisterNewPatientResponse
import com.app.ehealthaidoctor.R
import com.app.ehealthaidoctor.network.ApiUtils
import com.app.ehealthaidoctor.utils.SharedPrefs
import com.example.ehealthai.utils.toast
import kotlinx.android.synthetic.main.activity_registernewpatient.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class RegisterNewPatient: AppCompatActivity() {
    var cnictext:String=""
    var patientid:String=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registernewpatient)
        registerbtn.setOnClickListener {
            if( name.text.toString().equals(""))
            {
                name.error = "This field cannot be empty"
            }
            if(lname.text.toString().equals(""))
            {
                lname.error = "This field cannot be empty"
            }
            else if( cnic.text.toString().equals(""))
            {
                cnic.error = "This field cannot be empty"
            }
            else if( phone.text.toString().equals(""))
            {
                phone.error = "This field cannot be empty"
            }
            else if( age.text.toString().equals(""))
            {
                age.error = "This field cannot be empty"
            }
            else if( blood.selectedItem.toString().equals(""))
            {
                toast("This field cannot be empty")
            }
            else if( adress.text.toString().equals(""))
            {
                adress.error = "This field cannot be empty"
            }
            else if( date.text.toString().equals(""))
            {
                date.error = "This field cannot be empty"
            }

            else {
                try {

                }catch (e:Exception){}
                cnictext=cnic.text.toString()
                registerpatient(cnic.text.toString(),name.text.toString(),lname.text.toString(),"",phone.text.toString(),blood.selectedItem.toString(),age.text.toString(),adress.text.toString(),date.text.toString(),"ezshifapanahgah@gmail.com")
            }

        }
        date.setOnClickListener {
            opendatepickerdialog()
        }
        rnp_back_btn.setOnClickListener {
            finish()
        }
    }
    fun registerpatient(cnic: String, fname: String,lname: String, sex: String, phone: String, bloodgroup: String, age: String, address: String, dob: String, email: String) {
        rnp_progress.visibility= View.VISIBLE

        ApiUtils.getAPIService(this).cnic_registration(cnic,fname,lname,sex,phone,bloodgroup,age,address,dob,email,"")
                .enqueue(object : Callback<RegisterNewPatientResponse> {
                    override fun onFailure(call: Call<RegisterNewPatientResponse>, t: Throwable) {
                        rnp_progress.visibility= View.GONE
                        toast("Something Went Wrong"+t.toString())
                        Log.e("TAG",call.toString()+t.toString())
                    }

                    override fun onResponse(call: Call<RegisterNewPatientResponse>, response: Response<RegisterNewPatientResponse>) {
                        rnp_progress.visibility= View.GONE
                        try {
                            if (response.isSuccessful && response.body() != null && response.body()!!.success.equals(true)) {

                                patientid=response.body()!!.data!!.pid.toString()
                                toast("Patient Registered with ID: "+patientid)
                                submitAppointment()

                            } else if (response.errorBody() != null) {
                                val jObjError = JSONObject(response.errorBody()!!.string())
                                toast(jObjError.getString("message"))
                            }
                        }catch (e : Exception){
                            toast("Error: Try again"+e.toString())
                        }
                    }
                })


    }
    fun opendatepickerdialog()
    {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val dpd = DatePickerDialog(this@RegisterNewPatient, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            val month: Int = monthOfYear + 1
            // Display Selected date in textbox
            date.setText("" + year + "-" + month + "-" + dayOfMonth)

        }, year, month, day)

        dpd.show()
    }
    fun submitAppointment(


    ) {
        val userData = SharedPrefs.getUserData(this)
        val sessionid =  SharedPrefs.getString(this, "sessionid")
        val username= SharedPrefs.getString(this, "cnic")
        val usertype=  "PATIENT"
        rnp_progress.visibility = View.VISIBLE
        ApiUtils.getAPIService(this@RegisterNewPatient).makeAppointment(userData!!.id.toString(), patientid,sessionid!!,username!!,usertype)
                .enqueue(object : Callback<MakeAppointmentResponse> {
                    override fun onFailure(call: Call<MakeAppointmentResponse>, t: Throwable) {
                        rnp_progress.visibility = View.INVISIBLE
                        toast("Something went wrong")
                    }

                    override fun onResponse(
                            call: Call<MakeAppointmentResponse>,
                            response: Response<MakeAppointmentResponse>
                    ) {
                        rnp_progress.visibility = View.INVISIBLE
                        try{                    if (response.isSuccessful && response.body() != null && response.body()!!.statuscode == 200) {
                            rnp_progress.visibility = View.INVISIBLE
                            //toast(response.body()!!.data.message)

                            if (response.body()!!.data.message.equals("Appointment has been set"))
                            {
                                toast("Appointment has been set")
                            }
                            else{
                                finish()
                                toast(response.body()!!.data.message.toString())
                            }
                        } else {
                            rnp_progress.visibility = View.INVISIBLE
                            val jObjError = JSONObject(response.errorBody()!!.string())
                            val dataObject = jObjError.getJSONObject("data")
                            toast(dataObject.getString("error"))
                        }
                        }catch(e:Exception){
                            rnp_progress.visibility = View.INVISIBLE
                            toast(e.toString())
                        }

                    }
                })


    }
}
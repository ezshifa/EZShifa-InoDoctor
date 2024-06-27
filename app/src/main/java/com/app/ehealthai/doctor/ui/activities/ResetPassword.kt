package com.app.ehealthai.doctor.ui.activities

//import com.crashlytics.android.Crashlytics
//import io.fabric.sdk.android.Fabric
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cc.cloudist.acplibrary.ACProgressConstant
import cc.cloudist.acplibrary.ACProgressFlower
import com.app.ehealthai.doctor.models.responses.ForgotPasswordResponse
import com.app.ehealthaidoctor.R
import com.app.ehealthaidoctor.network.ApiUtils
import com.app.ehealthaidoctor.ui.activities.LoginActivity
import com.crashlytics.android.Crashlytics
import com.example.ehealthai.utils.toast
import io.fabric.sdk.android.Fabric
import kotlinx.android.synthetic.main.resetpassword.ed1
import kotlinx.android.synthetic.main.resetpassword.ed2
import kotlinx.android.synthetic.main.resetpassword.resetpass
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ResetPassword : AppCompatActivity()

{
    var username=""
    var ForgotPasswordDialog: ACProgressFlower? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fabric.with(this, Crashlytics())
        setContentView(R.layout.resetpassword)
        ForgotPasswordDialog = ACProgressFlower.Builder(this)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .fadeColor(Color.DKGRAY).build()
        username=intent.getStringExtra("email")!!
        resetpass.setOnClickListener {
           if(ed1.text.toString().equals(""))
           {
             ed1.setError("Password can't be empty")
           }
            else if(ed2.text.toString().equals(""))
           {
               ed2.setError("Password can't be empty")
           }
           else if(!ed1.text.toString().equals(ed2.text.toString()))
           {
               toast("New and confirm passwords does not mached")
           }
            else
           {
               changePassword(username,ed1.text.toString(),ed2.text.toString())
           }
        }
    }
    fun changePassword( email: String, np: String,cp:String) {
        ForgotPasswordDialog!!.show()
        ApiUtils.getAPIService(this).ResetDocPassword( email,np,cp)
                .enqueue(object : Callback<ForgotPasswordResponse> {
                    override fun onFailure(call: Call<ForgotPasswordResponse>, t: Throwable) {
                        ForgotPasswordDialog!!.dismiss()
                        toast("Something went wrong, please try again later")
                    }

                    override fun onResponse(call: Call<ForgotPasswordResponse>, response: Response<ForgotPasswordResponse>) {
                        ForgotPasswordDialog!!.dismiss()
                        try {
                            if (response.isSuccessful && response.body() != null && response.body()!!.statuscode!!.equals("200")) {

                                toast("Password has been changed Successfully")
                                val intent = Intent(this@ResetPassword, LoginActivity::class.java)

                                startActivity(intent)

                            } else if (response.errorBody() != null) {
                                toast("Error")
                            }
                            else if (response.body()!!.statuscode!!.equals("404"))
                            {
                                toast("Doctor not found")
                            }
                        }catch (e : Exception){
                            toast("Something went wrong, please try again later")


                        }
                    }
                })


    }
}
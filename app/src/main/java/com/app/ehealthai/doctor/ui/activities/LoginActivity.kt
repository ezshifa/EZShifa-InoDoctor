package com.app.ehealthaidoctor.ui.activities

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.LinkProperties
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.telephony.CellLocation
import android.telephony.PhoneStateListener
import android.telephony.ServiceState
import android.telephony.SignalStrength
import android.telephony.TelephonyCallback
import android.telephony.TelephonyManager
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import cc.cloudist.acplibrary.ACProgressConstant
import cc.cloudist.acplibrary.ACProgressFlower
import com.app.ehealthai.doctor.models.responses.ChangePasswordResponse
import com.app.ehealthai.doctor.ui.activities.CpVerification
import com.app.ehealthai.doctor.utils.Global
import com.app.ehealthaidoctor.R
import com.app.ehealthaidoctor.models.responses.DoctorLoginResponse
import com.app.ehealthaidoctor.models.responses.Profile
import com.app.ehealthaidoctor.network.ApiUtils
import com.app.ehealthaidoctor.utils.SharedPrefs
import com.crashlytics.android.Crashlytics
import com.example.ehealthai.utils.Constants
import com.example.ehealthai.utils.toast
import io.fabric.sdk.android.Fabric
import kotlinx.android.synthetic.main.activity_login.back
import kotlinx.android.synthetic.main.activity_login.background
import kotlinx.android.synthetic.main.activity_login.btnForgetPassword
import kotlinx.android.synthetic.main.activity_login.btnSignIn
import kotlinx.android.synthetic.main.activity_login.btnrememberme
import kotlinx.android.synthetic.main.activity_login.callrep
import kotlinx.android.synthetic.main.activity_login.password_edit
import kotlinx.android.synthetic.main.activity_login.sendcode
import kotlinx.android.synthetic.main.activity_login.signUpConfirmPassword1
import kotlinx.android.synthetic.main.activity_login.username_edit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity : AppCompatActivity() {

    var signInDialog: ACProgressFlower? = null
    var ForgotPasswordDialog: ACProgressFlower? = null
    var showpass = false

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fabric.with(this, Crashlytics())
        setContentView(R.layout.activity_login)

        init()
        btnForgetPassword.setOnClickListener {

            background.visibility = View.VISIBLE

        }
        back.setOnClickListener {

            background.visibility = View.INVISIBLE

        }
        callrep.setOnClickListener {

            val phone = "03005018328"
            val callIntent = Intent(Intent.ACTION_DIAL)
            callIntent.data = Uri.parse("tel:$phone")
            startActivity(callIntent)

        }
        var username = SharedPrefs.getString(LoginActivity@ this, "usernamelogin")
        var usernamepass = SharedPrefs.getString(LoginActivity@ this, "usernamepass")
        if (username.equals("") && usernamepass.equals("")) {
            btnrememberme.setChecked(false)
        } else {
            username_edit.setText(username)
            password_edit.setText(usernamepass)
            btnrememberme.setChecked(true)
        }
        btnrememberme.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {

                SharedPrefs.save(
                    LoginActivity@ this, "usernamelogin", username_edit.getText().toString()
                )
                SharedPrefs.save(
                    LoginActivity@ this, "usernamepass", password_edit.getText().toString()
                )
            } else {
                SharedPrefs.save(LoginActivity@ this, "usernamelogin", "")
                SharedPrefs.save(LoginActivity@ this, "usernamepass", "")

            }
        }
        password_edit.setOnTouchListener(OnTouchListener { _, event ->
            val DRAWABLE_LEFT = 0
            val DRAWABLE_TOP = 1
            val DRAWABLE_RIGHT = 2
            val DRAWABLE_BOTTOM = 3
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= password_edit.getRight() - password_edit.compoundDrawables[DRAWABLE_RIGHT].bounds.width()) {
                    // your action here
                    if (showpass) {
                        password_edit.transformationMethod =
                            HideReturnsTransformationMethod.getInstance()
                        password_edit.setCompoundDrawablesWithIntrinsicBounds(
                            0,
                            0,
                            R.drawable.hidepass,
                            0
                        )
                        showpass = false
                    } else {
                        password_edit.transformationMethod =
                            PasswordTransformationMethod.getInstance()
                        password_edit.setCompoundDrawablesWithIntrinsicBounds(
                            0,
                            0,
                            R.drawable.showpass,
                            0
                        )
                        showpass = true
                    }
                    return@OnTouchListener true
                }
            }
            false
        })

        sendcode.setOnClickListener {
            when {
                signUpConfirmPassword1.text.toString().equals("") -> {
                    signUpConfirmPassword1.error = "This field cannot be empty"
                }

                else -> {
                    background.visibility = View.INVISIBLE
                    try {
                        forgotPassword(
                            signUpConfirmPassword1.text.toString(),
                            signUpConfirmPassword1.text.toString(),
                            "doctor"
                        )
                    } catch (e: Exception) {
                        toast("Something went wrong, please try again later")
                    }
                }
            }
        }


    }

    fun init() {
        signInDialog = ACProgressFlower.Builder(this).direction(ACProgressConstant.DIRECT_CLOCKWISE)
            .themeColor(Color.WHITE).fadeColor(Color.DKGRAY).build()
        ForgotPasswordDialog =
            ACProgressFlower.Builder(this).direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE).fadeColor(Color.DKGRAY).build()
        if (SharedPrefs.getString(this, Constants.PASSWORD) != null && SharedPrefs.getString(
                this, Constants.PASSWORD
            ) != ""
        ) {
            // signIn(SharedPrefs.getString(this, Constants.USERNAME)!!, SharedPrefs.getString(this, Constants.PASSWORD)!!)
            try {
                val userData = SharedPrefs.getUserData(this)
                val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                intent.putExtra("name", userData!!.fname + " " + userData.lname)
                startActivity(intent)
                finish()
            } catch (e: Exception) {
            }
        } else {
            btnSignIn.setOnClickListener {
                when {
                    username_edit.text.toString() == null -> {
                        username_edit.error = "This field cannot be empty"
                    }

                    password_edit.text.toString() == null -> {
                        password_edit.error = "This field cannot be empty"
                    }

                    else -> {
                        signIn(username_edit.text.toString(), password_edit.text.toString())
                    }
                }
            }
        }
    }


    fun signIn(username: String, password: String) {
        signInDialog!!.show()
        Global.docame = username
        SharedPrefs.save(this@LoginActivity, Constants.USERNAME, username)
        ApiUtils.getAPIService(this).loginCall(username, password, "DOCTOR")
            .enqueue(object : Callback<DoctorLoginResponse> {
                override fun onFailure(call: Call<DoctorLoginResponse>, t: Throwable) {
                    signInDialog!!.dismiss()
                    toast("Something went wrong, please try again later")
                }

                override fun onResponse(
                    call: Call<DoctorLoginResponse>, response: Response<DoctorLoginResponse>
                ) {
                    signInDialog!!.dismiss()
                    try {
                        if (response.body() != null && response.body()!!.statuscode == 200) {
                            SharedPrefs.save(this@LoginActivity, Constants.USERNAME, username)
                            SharedPrefs.save(this@LoginActivity, Constants.PASSWORD, password)
                            SharedPrefs.save(
                                this@LoginActivity, "filepath", response.body()!!.data.filepath
                            )
                            SharedPrefs.save(
                                this@LoginActivity, "sessionid", response.body()!!.data.sessionid
                            )

                            val userData: Profile = response.body()!!.data.profile!!.get(0)
                            if (response.body()!!.data.profile!!.size > 1) {
                                if (response.body()!!.data.profile!!.get(1).image_name != null) userData.image_name =
                                    response.body()!!.data.profile!!.get(0).image_name
                                if (!response.body()!!.data.profile!!.get(0).experience.isNullOrEmpty()) userData.experience =
                                    response.body()!!.data.profile!!.get(0).experience
                                if (!response.body()!!.data.profile!!.get(0).degrees.isNullOrEmpty()) userData.degrees =
                                    response.body()!!.data.profile!!.get(0).degrees
                                if (!response.body()!!.data.profile!!.get(0).country.isNullOrEmpty()) userData.country =
                                    response.body()!!.data.profile!!.get(0).country

                            }

                            //toast(response.body()!!.data.profile!!.get(0).image_name!!)
                            SharedPrefs.setUserData(this@LoginActivity, userData)
                            val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                            intent.putExtra("name", userData.fname + " " + userData.lname)
                            startActivity(intent)
                            finish()
                        } else toast("Invalid Credentials")
                    } catch (e: Exception) {
                        toast("Login Failed ")
                        SharedPrefs.removeUserData(this@LoginActivity)
                        SharedPrefs.removeKey(this@LoginActivity, Constants.USERNAME)
                        SharedPrefs.removeKey(this@LoginActivity, Constants.PASSWORD)
                        SharedPrefs.save(this@LoginActivity, "TOKEN_REFRESHED", false)
                    }

                }
            })
    }
    ////////////////////////////// forgot password

    fun forgotPassword(email: String, username: String, type: String) {
        ForgotPasswordDialog!!.show()
        ApiUtils.getAPIService(this).SendVerificationCode(email)
            .enqueue(object : Callback<ChangePasswordResponse> {
                override fun onFailure(call: Call<ChangePasswordResponse>, t: Throwable) {
                    ForgotPasswordDialog!!.dismiss()
                    toast("Something went wrong, please try again later")
                }

                override fun onResponse(
                    call: Call<ChangePasswordResponse>, response: Response<ChangePasswordResponse>
                ) {
                    ForgotPasswordDialog!!.dismiss()
                    try {
                        if (response.isSuccessful && response.body() != null && response.body()!!.statuscode!!.equals(
                                "200"
                            )
                        ) {

                            toast("Verification code has been sent")
                            val intent = Intent(this@LoginActivity, CpVerification::class.java)
                            intent.putExtra("verificationcode", response.body()!!.data.pcode)
                            intent.putExtra("username", email)
                            startActivity(intent)

                        } else if (response.errorBody() != null) {
                            toast("Error")
                        } else if (response.body()!!.statuscode!!.equals("404")) {
                            toast("Doctor not found")
                        }
                    } catch (e: Exception) {
                        toast("Something went wrong, please try again later")


                    }
                }
            })


    }
}

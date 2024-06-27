//package com.egrb.ehealthaidoctor.ui.activities
//
//import android.content.Context
//import android.content.Intent
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import android.text.Editable
//import android.text.TextWatcher
//import android.view.View
//import android.view.inputmethod.InputMethodManager
//import com.example.ehealthai.R
//import com.example.ehealthai.network.ApiUtils
//import com.example.ehealthai.utils.toast
//import kotlinx.android.synthetic.main.activity_email_verification.*
//import org.json.JSONObject
//import retrofit2.Call
//import retrofit2.Response
//
//class EmailVerificationActivity : AppCompatActivity() {
//    var username: String? = null
//    var email: String? = null
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_email_verification)
//
//        init()
//    }
//
//    fun init() {
//        username = intent.getStringExtra("username")
//        email = intent.getStringExtra("email")
//
//        num_1.requestFocus()
//        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//        imm.showSoftInput(
//            num_1, InputMethodManager.SHOW_IMPLICIT
//        )
//        num_1.addTextChangedListener(object : TextWatcher {
//            override fun afterTextChanged(s: Editable?) {
//                imm.showSoftInput(num_2, InputMethodManager.SHOW_IMPLICIT)
//                if (!num_1.text.isEmpty() && !num_2.text.isEmpty() && !num_3.text.isEmpty() && !num_4.text.isEmpty()) {
//                    verify_code_btn.visibility = View.VISIBLE
//                } else {
//                    verify_code_btn.visibility = View.GONE
//                }
//
//            }
//
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//
//                if (!s.isNullOrEmpty()) {
//                    //  num_1.setText(s.toString())
//
//                    num_2.requestFocus()
//                }
//                if (!num_1.text.isEmpty() && !num_2.text.isEmpty() && !num_3.text.isEmpty() && !num_4.text.isEmpty()) {
//                    verify_code_btn.visibility = View.VISIBLE
//                } else {
//                    verify_code_btn.visibility = View.GONE
//                }
//
//
//            }
//
//        })
//        num_2.addTextChangedListener(object : TextWatcher {
//            override fun afterTextChanged(s: Editable?) {
//                if (num_2.text.toString().isEmpty()) {
//
//                    num_1.requestFocus()
//                }
//
//                imm.showSoftInput(num_3, InputMethodManager.SHOW_IMPLICIT)
//                if (!num_1.text.isEmpty() && !num_2.text.isEmpty() && !num_3.text.isEmpty() && !num_4.text.isEmpty()) {
//                    verify_code_btn.visibility = View.VISIBLE
//                } else {
//                    verify_code_btn.visibility = View.GONE
//                }
//            }
//
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                if (!s.isNullOrEmpty()) {
//                    //  num_2.setText(s.toString())
//                    num_3.requestFocus()
//                }
//                if (!num_1.text.isEmpty() && !num_2.text.isEmpty() && !num_3.text.isEmpty() && !num_4.text.isEmpty()) {
//                    verify_code_btn.visibility = View.VISIBLE
//                } else {
//                    verify_code_btn.visibility = View.GONE
//                }
//            }
//
//        })
//        num_3.addTextChangedListener(object : TextWatcher {
//            override fun afterTextChanged(s: Editable?) {
//                if (num_3.text.toString().isEmpty()) {
//                    num_2.requestFocus()
//                }
//                imm.showSoftInput(num_4, InputMethodManager.SHOW_IMPLICIT)
//                if (!num_1.text.isEmpty() && !num_2.text.isEmpty() && !num_3.text.isEmpty() && !num_4.text.isEmpty()) {
//                    verify_code_btn.visibility = View.VISIBLE
//                } else {
//                    verify_code_btn.visibility = View.GONE
//                }
//            }
//
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                if (!s.isNullOrEmpty()) {
//                    // num_3.setText(s.toString())
//                    num_4.requestFocus()
//                }
//                if (!num_1.text.isEmpty() && !num_2.text.isEmpty() && !num_3.text.isEmpty() && !num_4.text.isEmpty()) {
//                    verify_code_btn.visibility = View.VISIBLE
//                } else {
//                    verify_code_btn.visibility = View.GONE
//                }
//            }
//
//        })
//        num_4.addTextChangedListener(object : TextWatcher {
//            override fun afterTextChanged(s: Editable?) {
//                if (num_4.text.toString().isEmpty()) {
//                    num_3.requestFocus()
//                }
//
//
//                if (!num_1.text.isEmpty() && !num_2.text.isEmpty() && !num_3.text.isEmpty() && !num_4.text.isEmpty()) {
//                    verify_code_btn.visibility = View.VISIBLE
//                } else {
//                    verify_code_btn.visibility = View.GONE
//                }
//            }
//
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                if (!s.isNullOrEmpty()) {
//                    // num_4.setText(s.toString())
//                }
//                if (!num_1.text.isEmpty() && !num_2.text.isEmpty() && !num_3.text.isEmpty() && !num_4.text.isEmpty()) {
//                    verify_code_btn.visibility = View.VISIBLE
//                } else {
//                    verify_code_btn.visibility = View.GONE
//                }
//            }
//
//        })
//
//        if (num_1.text.isEmpty() || num_2.text.isEmpty() || num_3.text.isEmpty() || num_4.text.isEmpty()) {
//            verify_code_btn.visibility = View.GONE
//        } else {
//            verify_code_btn.visibility = View.VISIBLE
//        }
//
//        verify_code_btn.setOnClickListener {
//            val code = num_1.text.toString() + num_2.text.toString() + num_3.text.toString() + num_4.text.toString()
//            sendVerificationCode(code)
//        }
//
//
//        resend_btn.setOnClickListener {
//            resendVerifyRequest(username!!, email!!)
//        }
//    }
//
//    private fun sendVerificationCode(code: String) {
//        ApiUtils.getAPIService(this).verifyAccount(username!!, code)
//            .enqueue(object : retrofit2.Callback<VerifyEmailResponse> {
//                override fun onFailure(call: Call<VerifyEmailResponse>, t: Throwable) {
//                    toast("Something went wrong")
//                }
//
//                override fun onResponse(call: Call<VerifyEmailResponse>, response: Response<VerifyEmailResponse>) {
//                    if (response.isSuccessful && response.body() != null) {
//                        toast(response.message())
//                        startActivity(Intent(this@EmailVerificationActivity, LoginActivity::class.java))
//                        finish()
//                    }else{
//                        val jObjError = JSONObject(response.errorBody()!!.string())
//                        toast(jObjError.getString("message"))
//                    }
//                }
//            })
//
//    }
//
//
//    private fun resendVerifyRequest(username: String, email: String) {
//        ApiUtils.getAPIService(this@EmailVerificationActivity).resendVerificationCode(username, email)
//            .enqueue(object : retrofit2.Callback<ResendVerificationCodeResponse> {
//                override fun onFailure(call: Call<ResendVerificationCodeResponse>, t: Throwable) {
//                    toast("Something went wrong")
//                }
//
//                override fun onResponse(
//                    call: Call<ResendVerificationCodeResponse>,
//                    response: Response<ResendVerificationCodeResponse>
//                ) {
//                    if (response.isSuccessful && response.body() != null) {
//                        if (response.body()!!.success) {
//                            toast(response.body()!!.message)
//                        } else{
//                            val jObjError = JSONObject(response.errorBody()!!.string())
//                            toast(jObjError.getString("message"))
//                        }
//
//                    }
//                }
//            })
//
//    }
//}

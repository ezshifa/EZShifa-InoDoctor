package com.app.ehealthai.doctor.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.app.ehealthaidoctor.R
import com.crashlytics.android.Crashlytics
import com.example.ehealthai.utils.toast
import io.fabric.sdk.android.Fabric
import kotlinx.android.synthetic.main.cpverification.num_1
import kotlinx.android.synthetic.main.cpverification.num_2
import kotlinx.android.synthetic.main.cpverification.num_3
import kotlinx.android.synthetic.main.cpverification.num_4
import kotlinx.android.synthetic.main.cpverification.num_5
import kotlinx.android.synthetic.main.cpverification.num_6
import kotlinx.android.synthetic.main.cpverification.verify_code_btn

class CpVerification : AppCompatActivity()

{
    var verificationcode:String=""
    var email:String="";
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fabric.with(this, Crashlytics())
        setContentView(R.layout.cpverification)
        verificationcode=intent.getStringExtra("verificationcode")!!
        email=intent.getStringExtra("username")!!
        num_1.requestFocus()
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(
                num_1, InputMethodManager.SHOW_IMPLICIT
        )
        num_1.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                imm.showSoftInput(num_2, InputMethodManager.SHOW_IMPLICIT)
                if (!num_1.text.isEmpty() && !num_2.text.isEmpty() && !num_3.text.isEmpty() && !num_4.text.isEmpty()&& !num_5.text.isEmpty()&& !num_6.text.isEmpty()&& !num_5.text.isEmpty()&& !num_6.text.isEmpty()) {
                    verify_code_btn.visibility = View.VISIBLE
                } else {
                    verify_code_btn.visibility = View.GONE
                }

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                if (!s.isNullOrEmpty()) {
                    //  num_1.setText(s.toString())

                    num_2.requestFocus()
                }
                if (!num_1.text.isEmpty() && !num_2.text.isEmpty() && !num_3.text.isEmpty() && !num_4.text.isEmpty()&& !num_5.text.isEmpty()&& !num_6.text.isEmpty()) {
                    verify_code_btn.visibility = View.VISIBLE
                } else {
                    verify_code_btn.visibility = View.GONE
                }


            }

        })
        num_2.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (num_2.text.toString().isEmpty()) {

                    num_1.requestFocus()
                }

                imm.showSoftInput(num_3, InputMethodManager.SHOW_IMPLICIT)
                if (!num_1.text.isEmpty() && !num_2.text.isEmpty() && !num_3.text.isEmpty() && !num_4.text.isEmpty()&& !num_5.text.isEmpty()&& !num_6.text.isEmpty()) {
                    verify_code_btn.visibility = View.VISIBLE
                } else {
                    verify_code_btn.visibility = View.GONE
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty()) {
                    //  num_2.setText(s.toString())
                    num_3.requestFocus()
                }
                if (!num_1.text.isEmpty() && !num_2.text.isEmpty() && !num_3.text.isEmpty() && !num_4.text.isEmpty()&& !num_5.text.isEmpty()&& !num_6.text.isEmpty()) {
                    verify_code_btn.visibility = View.VISIBLE
                } else {
                    verify_code_btn.visibility = View.GONE
                }
            }

        })
        num_3.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (num_3.text.toString().isEmpty()) {
                    num_2.requestFocus()
                }
                imm.showSoftInput(num_4, InputMethodManager.SHOW_IMPLICIT)
                if (!num_1.text.isEmpty() && !num_2.text.isEmpty() && !num_3.text.isEmpty() && !num_4.text.isEmpty()&& !num_5.text.isEmpty()&& !num_6.text.isEmpty()) {
                    verify_code_btn.visibility = View.VISIBLE
                } else {
                    verify_code_btn.visibility = View.GONE
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty()) {
                    // num_3.setText(s.toString())
                    num_4.requestFocus()
                }
                if (!num_1.text.isEmpty() && !num_2.text.isEmpty() && !num_3.text.isEmpty() && !num_4.text.isEmpty()&& !num_5.text.isEmpty()&& !num_6.text.isEmpty()) {
                    verify_code_btn.visibility = View.VISIBLE
                } else {
                    verify_code_btn.visibility = View.GONE
                }
            }

        })
        num_4.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (num_4.text.toString().isEmpty()) {
                    num_3.requestFocus()
                }

                imm.showSoftInput(num_5, InputMethodManager.SHOW_IMPLICIT)
                if (!num_1.text.isEmpty() && !num_2.text.isEmpty() && !num_3.text.isEmpty() && !num_4.text.isEmpty()&& !num_5.text.isEmpty()&& !num_6.text.isEmpty()) {
                    verify_code_btn.visibility = View.VISIBLE
                } else {
                    verify_code_btn.visibility = View.GONE
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty()) {
                    // num_4.setText(s.toString())
                    num_5.requestFocus()
                }
                if (!num_1.text.isEmpty() && !num_2.text.isEmpty() && !num_3.text.isEmpty() && !num_4.text.isEmpty()&& !num_5.text.isEmpty()&& !num_6.text.isEmpty()) {
                    verify_code_btn.visibility = View.VISIBLE
                } else {
                    verify_code_btn.visibility = View.GONE
                }
            }

        })

        num_5.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (num_5.text.toString().isEmpty()) {
                    num_4.requestFocus()
                }

                imm.showSoftInput(num_6, InputMethodManager.SHOW_IMPLICIT)
                if (!num_1.text.isEmpty() && !num_2.text.isEmpty() && !num_3.text.isEmpty() && !num_4.text.isEmpty()&& !num_5.text.isEmpty()&& !num_6.text.isEmpty()) {
                    verify_code_btn.visibility = View.VISIBLE
                } else {
                    verify_code_btn.visibility = View.GONE
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty()) {
                    // num_4.setText(s.toString())
                    num_6.requestFocus()
                }
                if (!num_1.text.isEmpty() && !num_2.text.isEmpty() && !num_3.text.isEmpty() && !num_4.text.isEmpty()&& !num_5.text.isEmpty()&& !num_6.text.isEmpty()) {
                    verify_code_btn.visibility = View.VISIBLE
                } else {
                    verify_code_btn.visibility = View.GONE
                }
            }

        })

        num_6.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (num_6.text.toString().isEmpty()) {
                    num_5.requestFocus()
                }
                Log.e("value","added for 6")

                if (!num_1.text.isEmpty() && !num_2.text.isEmpty() && !num_3.text.isEmpty() && !num_4.text.isEmpty() && !num_5.text.isEmpty() && !num_6.text.isEmpty()) {
                    verify_code_btn.visibility = View.VISIBLE




                } else {
                    verify_code_btn.visibility = View.GONE
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty()) {
                    // num_4.setText(s.toString())
                }
                if (!num_1.text.isEmpty() && !num_2.text.isEmpty() && !num_3.text.isEmpty() && !num_4.text.isEmpty()&& !num_5.text.isEmpty()&& !num_6.text.isEmpty()) {
                    verify_code_btn.visibility = View.VISIBLE
                } else {
                    verify_code_btn.visibility = View.GONE
                }
            }

        })

        if (num_1.text.isEmpty() || num_2.text.isEmpty() || num_3.text.isEmpty() || num_4.text.isEmpty()|| !num_5.text.isEmpty()|| !num_6.text.isEmpty()) {
            verify_code_btn.visibility = View.GONE
        } else {
            verify_code_btn.visibility = View.VISIBLE
        }

        verify_code_btn.setOnClickListener {
            val code = num_1.text.toString() + num_2.text.toString() + num_3.text.toString() + num_4.text.toString()+ num_5.text.toString()+ num_6.text.toString()
            if(code.equals(verificationcode))
            {
                val intent = Intent(this@CpVerification, ResetPassword::class.java)
                intent.putExtra("email",email)
                startActivity(intent)
            }
            else{toast("Invalid Code")}
        }
    }

}
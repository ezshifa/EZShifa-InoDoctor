package com.app.ehealthai.doctor.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.app.ehealthaidoctor.R
import com.app.ehealthaidoctor.models.responses.AddVitalsResponse
import com.app.ehealthaidoctor.network.ApiUtils
import com.app.ehealthaidoctor.utils.SharedPrefs
//import com.crashlytics.android.Crashlytics
import com.example.ehealthai.utils.toast
//import io.fabric.sdk.android.Fabric
import kotlinx.android.synthetic.main.activity_vitals_confirm.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VitalsConfirmation : AppCompatActivity()
{
    var height_cm: String? = null
    var height_inch: String? = null
    var pulseval: String? = null
    var pulse2val: String? = null
    var oxygen2val: String? = null
    var oxygenval: String? = null
    var temparature_f: String? = null
    var breathing2val: String? = null
    var breathingval: String? = null
    var sender_id: String? = null
    var patientId: String? = null
    var appointmentId: String? = null
    var weightunit: String? = null
    var weight_lb: String? = null
    var weight_kg: String? = null
    var heightfval: String? = null
    var heightunit: String? = null
    var temparature_c: String? = null
    var tempunit: String? = null
    var sugar_type: String? = null
    var sugarval: String? = null
    var bp1val: String? = null
    var bp2val: String? = null
    var temparature_ad:String?=null
    var heightad:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        Fabric.with(this, Crashlytics())
        setContentView(R.layout.activity_vitals_confirm)
        appointmentId = intent.getStringExtra("appointmentId")
        patientId = intent.getStringExtra("patientId")
        sender_id = intent.getStringExtra("sender_id")
        breathingval = intent.getStringExtra("breathingval")
        breathing2val = intent.getStringExtra("breathing2val")
        oxygenval = intent.getStringExtra("oxygenval")
        oxygen2val = intent.getStringExtra("oxygen2val")
        pulseval = intent.getStringExtra("pulseval")
        pulse2val = intent.getStringExtra("pulse2val")
        height_cm = intent.getStringExtra("height_cm")
        height_inch = intent.getStringExtra("height_inch")
        heightunit = intent.getStringExtra("heightunit")
        heightfval = intent.getStringExtra("heightfval")
        weight_kg = intent.getStringExtra("weight_kg")
        weight_lb = intent.getStringExtra("weight_lb")
        weightunit = intent.getStringExtra("weightunit")
        temparature_f = intent.getStringExtra("temparature_f")
        temparature_c = intent.getStringExtra("temparature_c")
        tempunit = intent.getStringExtra("tempunit")
        sugar_type = intent.getStringExtra("sugar_type")
        sugarval = intent.getStringExtra("sugarval")
        bp1val = intent.getStringExtra("bp1val")
        bp2val = intent.getStringExtra("bp2val")
        temparature_ad= intent.getStringExtra("temparature_ad")
        heightad= intent.getStringExtra("height_ad")


        if (temparature_c.equals("")) {
            temptxt.text = temparature_f +temparature_ad+ tempunit
        } else if (temparature_f.equals("")) {
            temptxt.text = temparature_c +temparature_ad+ tempunit
        }

        bptxt.text = bp1val + "/" + bp2val+" mmHG"
        sugartxt.text = sugarval+" mg/dl"+"\n"+sugar_type+""
        if (height_cm.equals("")) {
            heighttxt.text = height_inch +heightad.toString()+ heightunit
            //height_inch=height_inch +heightad
        } else if (height_inch.equals("")) {
            heighttxt.text = height_cm +heightad.toString()+ heightunit
            //height_cm=height_cm +heightad.toString()
        }
        if (weight_kg.equals("")) {
            weighttxt.text = weight_lb + weightunit
        } else if (weight_lb.equals("")) {
            weighttxt.text = weight_kg + weightunit
        }

        breathtxt.text = breathingval + " to " + breathing2val+" ppm"
        oxygentxt.text = oxygenval + "%s\n" + oxygen2val + "%p"
        pulsetxt.text = pulseval + " to " + pulse2val+" ppm"
        if(temparature_c.equals(""))
        {
            temparature_c=""
            temparature_f=temparature_f +temparature_ad
        }
        else if(temparature_f.equals(""))
        {
            temparature_f=""
            temparature_c=temparature_c +temparature_ad
        }

        addvitalsbtn.setOnClickListener {
            //Call Add Vitals Webservice
            val userData=SharedPrefs.getUserData(this@VitalsConfirmation)
            val username=userData!!.username
            val usertype="DOCTOR"
            progress_vc.visibility = View.VISIBLE
            ApiUtils.getAPIService(this).AddVitalsRequest(appointmentId!!, bp1val.toString(), bp2val.toString(), sugarval.toString(), sugar_type!!, temparature_c!!, temparature_f!!, weight_kg!!, weight_lb!!, heightfval.toString(), height_cm!!, height_inch!!, pulseval.toString(), pulse2val.toString(), oxygenval.toString(), oxygen2val.toString(), breathingval.toString(), breathing2val.toString(), sender_id!!, patientId!!, SharedPrefs.getString(this@VitalsConfirmation, "sessionid")!!,username!!,usertype).enqueue(object : Callback<AddVitalsResponse> {
                override fun onFailure(call: Call<AddVitalsResponse>, t: Throwable) {
                    progress_vc.visibility = View.GONE
                    toast("Something went wrong, please try again later")
                }

                override fun onResponse(call: Call<AddVitalsResponse>, response: Response<AddVitalsResponse>) {
                    progress_vc.visibility = View.GONE
                    toast("Vitals Added Successfully !")

                    val intent = Intent(this@VitalsConfirmation, VitalsHistory::class.java)

                    intent.putExtra("appointmentId", appointmentId)
                    intent.putExtra("patientId", patientId)

                    startActivity(intent)
                    finish()
                }
            })
        }
        backbtn.setOnClickListener {
           finish()
        }
    }
}
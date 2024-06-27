package com.app.ehealthai.doctor.ui.activities.PatientDetails.ui.notifications

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.app.ehealthai.doctor.ui.activities.PatientDetails.PatientAppointment
import com.app.ehealthai.doctor.ui.activities.VitalsConfirmation
import com.app.ehealthai.doctor.ui.activities.VitalsHistory
import com.app.ehealthaidoctor.R
import com.app.ehealthaidoctor.ui.activities.HomeActivity
import com.app.ehealthaidoctor.utils.SharedPrefs

class AddVitalsFragment : Fragment() {

    var morevitals = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_addvitals, container, false)
        val more_layout: LinearLayout = root.findViewById(R.id.more_layout)
        val breathingsh: RelativeLayout = root.findViewById(R.id.breathingsh)
        val oxsh: RelativeLayout = root.findViewById(R.id.oxsh)
        val prsh: RelativeLayout = root.findViewById(R.id.prsh)
        val arrowupdown: ImageView = root.findViewById(R.id.arrowupdown)
        val bpinfo: ImageView = root.findViewById(R.id.bpinfo)
        val slinfo: ImageView = root.findViewById(R.id.slinfo)
        val tempinfo: ImageView = root.findViewById(R.id.tempinfo)
        val oxinfo: ImageView = root.findViewById(R.id.oxinfo)
        val brinfo: ImageView = root.findViewById(R.id.brinfo)
        val prinfo: ImageView = root.findViewById(R.id.prinfo)
        val bp1: EditText = root.findViewById(R.id.bp1)
        val bp2: EditText = root.findViewById(R.id.bp2)
        val sugar: EditText = root.findViewById(R.id.sugar)
        val heightit: EditText = root.findViewById(R.id.heightit)
        val heightaad: EditText = root.findViewById(R.id.heightaad)
        val weight: EditText = root.findViewById(R.id.weight)
        val temparature: EditText = root.findViewById(R.id.temparature)
        val fromp: EditText = root.findViewById(R.id.fromp)
        val top: EditText = root.findViewById(R.id.top)
        val fromb: EditText = root.findViewById(R.id.fromb)
        val tob: EditText = root.findViewById(R.id.tob)
        val fromo: EditText = root.findViewById(R.id.fromo)
        val temparatureaad: EditText = root.findViewById(R.id.temparatureaad)
        val too: EditText = root.findViewById(R.id.too)
        val tempsp: Spinner = root.findViewById(R.id.tempsp)
        val heightsp: Spinner = root.findViewById(R.id.heightsp)
        val weightsp: Spinner = root.findViewById(R.id.weightsp)
        val sugarsp: Spinner = root.findViewById(R.id.sugarsp)
        val bmirs: TextView = root.findViewById(R.id.bmirs)
        val bmivalue: TextView = root.findViewById(R.id.bmivalue)
        val addvitalsbtn: Button = root.findViewById(R.id.addvitalsbtn)
        val vitalhistorybtn: Button = root.findViewById(R.id.vitalhistorybtn)
        val home_av: ImageView = root.findViewById(R.id.av_home)
        val viewmore: TextView = root.findViewById(R.id.viewmore)
        home_av.setOnClickListener {
            val intent = Intent(requireContext(), HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }
        more_layout.setOnClickListener {
            if (!morevitals) {
                breathingsh.visibility = View.VISIBLE
                oxsh.visibility = View.VISIBLE
                prsh.visibility = View.VISIBLE
                morevitals = true
                arrowupdown.setImageResource(R.drawable.uparrow)
                viewmore.text = "Hide More Vitals"
            } else if (morevitals) {
                breathingsh.visibility = View.GONE
                oxsh.visibility = View.GONE
                prsh.visibility = View.GONE
                morevitals = false
                arrowupdown.setImageResource(R.drawable.dropdown_icon)
                viewmore.text = "Show More Vitals"
            }
            bp1.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    var tempunit = tempsp.selectedItem.toString()
                    var temparature_c = ""
                    var temparature_f = ""
                    if (tempunit.equals("°F")) {
                        temparature_f = temparature.text.toString()
                        temparature_c = ""
                    } else if (tempunit.equals("°C")) {
                        temparature_c = temparature.text.toString()
                        temparature_f = ""
                    }
                    if (temparature.text.toString().equals("")) {
                        temparature.error = "Temparatue value can't be empty"
                    } else if (!temparature_c.equals("") && temparature_c.toString().toInt() > 42) {
                        temparature.error = "Maximum Temparature value is 42C"

                    } else if (!temparature_f.equals("") && temparature_f.toString()
                            .toInt() > 108
                    ) {
                        temparature.error = "Maximum Temparature value is 108F"
                    }
                }
            }
            bp2.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    if (bp1.text.toString().equals("")) {
                        bp1.error = "Blood Pressure value can't be empty"
                    } else if (bp1.text.toString().toInt() < 70) {
                        bp1.error = "Blood Pressure value cant be less than 70"
                    } else if (bp1.text.toString().toInt() > 300) {
                        bp1.error = "Blood Pressure value cant be greater than 300"
                    }
                }
            }
            sugar.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {

                    if (bp2.text.toString().equals("")) {
                        bp2.error = "Blood Pressure value can't be empty"
                    } else if (bp2.text.toString().toInt() < 40) {
                        bp2.error = "Blood Pressure value cant be less than 40"
                    } else if (bp2.text.toString().toInt() > 180) {
                        bp2.error = "Blood Pressure value cant be greater than 180"
                    }
                }
            }
            temparature.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {

                    /* if(sugar.text.toString().equals(""))
                     {
                         sugar.error="Sugar value can't be empty"
                     }
                     else if(sugar.text.toString().toInt()<1)
                     {
                         sugar.error="Minimum Sugar value is 1"
                     }
                     else if(sugar.text.toString().toInt()>650)
                     {
                         sugar.error="Maximum Sugar value is 650"
                     }*/
                }
            }
            fromp.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    var tempunit = tempsp.selectedItem.toString()
                    var temparature_c = ""
                    var temparature_f = ""
                    if (tempunit.equals("°F")) {
                        temparature_f = temparature.text.toString()
                        temparature_c = ""
                    } else if (tempunit.equals("°C")) {
                        temparature_c = temparature.text.toString()
                        temparature_f = ""
                    }
                    if (temparature.text.toString().equals("")) {
                        temparature.error = "Temparatue value can't be empty"
                    } else if (!temparature_c.equals("") && temparature_c.toString().toInt() > 42) {
                        temparature.error = "Maximum Temparature value is 42C"

                    } else if (!temparature_f.equals("") && temparature_f.toString()
                            .toInt() > 108
                    ) {
                        temparature.error = "Maximum Temparature value is 108F"
                    }
                }
            }
            top.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    if (fromp.text.toString().equals("")) {
                        fromp.error = "Pulse value can't be empty"
                    } else if (fromp.text.toString().toInt() < 40) {
                        fromp.error = "Minimum pulse value is 40"
                    } else if (fromp.text.toString().toInt() > 250) {
                        fromp.error = "Maximum pulse value is 250"
                    }
                }
            }
            fromb.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    if (top.text.toString().equals("")) {
                        top.error = "Pulse value can't be empty"
                    } else if (top.text.toString().toInt() < 40) {
                        top.error = "Minimum pulse value is 40"
                    } else if (top.text.toString().toInt() > 250) {
                        top.error = "Maximum pulse value is 250"
                    }
                }
            }
            tob.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    if (fromb.text.toString().equals("")) {
                        fromb.error = "Breathing value can't be empty"
                    } else if (fromb.text.toString().toInt() < 5) {
                        fromb.error = "Minimum Breathing value is 5"
                    } else if (fromb.text.toString().toInt() > 50) {
                        fromb.error = "Maximum Breathing value is 50"
                    }
                }
            }
            fromo.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    if (tob.text.toString().equals("")) {
                        tob.error = "Breathing value can't be empty"
                    } else if (tob.text.toString().toInt() < 5) {
                        tob.error = "Minimum Breathing value is 5"
                    } else if (tob.text.toString().toInt() > 50) {
                        tob.error = "Maximum Breathing value is 50"
                    }
                }
            }
            weight.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    //var heightfval=heightf.text

                    //var heightival=heightit.text
                    var heightunit = heightsp.selectedItem.toString()
                    var height_cm = ""
                    var height_inch = ""
                    if (heightunit.equals("cm")) {
                        height_cm = heightit.text.toString()
                        height_inch = ""
                    } else if (heightunit.equals("ft")) {
                        height_inch = heightit.text.toString()
                        height_cm = ""
                    }
                    if (!height_cm.toString().equals("") && weightsp.selectedItem.toString()
                            .equals("Lb")
                    ) {
                        weight.error = "Please enter weight in Kg"
                    } else if (!height_inch.toString()
                            .equals("") && weightsp.selectedItem.toString().equals("Kg")
                    ) {
                        weight.error = "Please enter weight in Lb"
                    }
                }
            }
            weight.addTextChangedListener(object : TextWatcher {

                override fun afterTextChanged(s: Editable) {}

                override fun beforeTextChanged(
                    s: CharSequence, start: Int,
                    count: Int, after: Int
                ) {
                }

                override fun onTextChanged(
                    s: CharSequence, start: Int,
                    before: Int, count: Int
                ) {
                    try {

                        var heightfval = "" /*heightf.text*/

                        var heightival = heightit.text
                        var heightunit = heightsp.selectedItem.toString()
                        var height_cm = ""
                        var height_inch = ""
                        if (heightunit.equals("cm")) {
                            height_cm = heightit.text.toString()
                            height_inch = ""
                        } else if (heightunit.equals("ft")) {
                            height_inch = heightit.text.toString()
                            height_cm = ""
                        }

                        if (height_cm.equals("")) {

                            if (height_inch.toInt() > 10) {
                                heightit.error = "Height max value is 10"
                            } else {
                                val sqr = height_inch.toDouble() * height_inch.toDouble()
                                val bmival = ((703 * s.toString().toInt()) / sqr)
                                bmivalue.text = "BMI Value : " + bmival

                                if (bmival < 18.5) {
                                    bmirs.text = " (" + "Underweight)"
                                    bmirs.setTextColor(Color.parseColor("#FFFF00"))
                                } else if (bmival >= 18.5 && bmival <= 24.9) {
                                    bmirs.text = " (" + "Healthy Weight)"
                                    bmirs.setTextColor(Color.parseColor("#00ff00"))

                                } else if (bmival >= 25 && bmival <= 29.9) {
                                    bmirs.text = " (" + "Overweight)"
                                    bmirs.setTextColor(Color.parseColor("#FFA500"))
                                } else if (bmival >= 30) {
                                    bmirs.text = " (" + "Obese)"
                                    bmirs.setTextColor(Color.parseColor("#ff0000"))
                                }
                            }
                        }
                        if (height_inch.equals("")) {
                            if (height_cm.toInt() > 400) {
                                heightit.error = "Height max value is 400"
                            } else {
                                val cmtom = height_cm.toDouble() / 100
                                val sqr = cmtom * cmtom
                                val bmival = (s.toString().toInt() / sqr)
                                bmivalue.text = "BMI Value : " + bmival
                                if (bmival < 18.5) {
                                    bmirs.text = " (" + "Underweight)"
                                    bmirs.setTextColor(Color.parseColor("#FFFF00"))
                                } else if (bmival >= 18.5 && bmival <= 24.9) {
                                    bmirs.text = " (" + "Healthy Weight)"
                                    bmirs.setTextColor(Color.parseColor("#00ff00"))

                                } else if (bmival >= 25 && bmival <= 29.9) {
                                    bmirs.text = " (" + "Overweight)"
                                    bmirs.setTextColor(Color.parseColor("#FFA500"))
                                } else if (bmival >= 30) {
                                    bmirs.text = " (" + "Obese)"
                                    bmirs.setTextColor(Color.parseColor("#ff0000"))
                                }
                            }
                        }

                    } catch (ex: Exception) {
                    }
                }

            })
            heightsp.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (heightsp.selectedItem.toString().equals("cm")) {
                        weightsp.setSelection(0)
                    } else {
                        weightsp.setSelection(1)
                    }
                }

            }

            addvitalsbtn.setOnClickListener {
                //Call Add Vitals Webservice

                var bp1val = bp1.text
                var bp2val = bp2.text
                var sugarval = sugar.text
                var sugar_type = sugarsp.selectedItem.toString()
                var temparatureval = temparature.text
                var tempunit = tempsp.selectedItem.toString()
                var temparature_c = ""
                var temparature_f = ""
                if (tempunit.equals("°F")) {
                    temparature_f = temparature.text.toString()
                    temparature_c = ""
                } else if (tempunit.equals("°C")) {
                    temparature_c = temparature.text.toString()
                    temparature_f = ""
                }
                var weightval = weight.text
                var weightunit = weightsp.selectedItem.toString()
                var weight_kg = ""
                var weight_lb = ""
                if (weightunit.equals("Kg")) {
                    weight_kg = weight.text.toString()
                    weight_lb = ""
                } else if (weightunit.equals("Lb")) {
                    weight_lb = weight.text.toString()
                    weight_kg = ""
                }
                var heightfval = ""/*heightf.text*/

                var heightival = heightit.text
                var heightunit = heightsp.selectedItem.toString()
                var height_cm = ""
                var height_inch = ""
                if (heightunit.equals("cm")) {
                    height_cm = heightit.text.toString()
                    height_inch = ""
                } else if (heightunit.equals("ft")) {
                    height_inch = heightit.text.toString()
                    height_cm = ""
                }
                var pulseval = fromp.text
                var pulse2val = top.text
                var oxygenval = fromo.text
                var oxygen2val = too.text
                var breathingval = fromb.text
                var breathing2val = tob.text
                val userData = SharedPrefs.getUserData(requireContext())
                var sender_id = userData!!.title + " " + userData!!.fname + " " + userData.lname

                //Checks

                /* if(bp1val.toString().equals("")||bp2val.toString().equals(""))
                 {
                     toast("Invalid Blood Pressure values")
                 }
                 else if(bp1val.toString().toInt()<70||bp2val.toString().toInt()<40)
                 {
                     toast("Blood Pressure value cant be less than 70/40")
                 }
                 else if(bp1val.toString().toInt()>300||bp2val.toString().toInt()>180)
                 {
                     toast("Blood Pressure value cant be greater than 300/180")
                 }

                 else if(sugarval.toString().equals(""))
                 {
                     toast("Invalid Sugar value")
                 }

                 else if(sugarval.toString().toInt()<1)
                 {
                     toast("Minimum Sugar value is 1")
                 }
                 else if(sugarval.toString().toInt()>650)
                 {
                     toast("Maximum Sugar value is 650")
                 }
                 else if(pulseval.toString().equals("")||pulse2val.toString().equals(""))
                 {
                     toast("Invalid Pulse values")
                 }
                 else if(pulseval.toString().toInt()<40||pulse2val.toString().toInt()<40)
                 {
                     toast("Minimum pulse value is 40")
                 }
                 else if(pulseval.toString().toInt()>250||pulse2val.toString().toInt()>250)
                 {
                     toast("Maximum pulse value is 250")
                 }
                 else if(breathingval.toString().equals("")||breathing2val.toString().equals(""))
                 {
                     toast("Invalid Breathing values")
                 }
                 else if(breathingval.toString().toInt()<5||breathing2val.toString().toInt()<5)
                 {
                     toast("Minimum Breathing value is 5")
                 }
                 else if(breathingval.toString().toInt()>50||breathingval.toString().toInt()>50)
                 {
                     toast("Maximum Breathing value is 50")
                 }

                 else if(temparatureval.toString().equals(""))
                 {
                     toast("Invalid Temparatue value")
                 }
                 else if(!temparature_c.equals("")&&temparature_c.toString().toInt()>42)
                 {
                     toast("Maximum Temparature value is 42C")

                 }
                 else if(!temparature_f.equals("")&&temparature_f.toString().toInt()>108)
                 {
                     toast("Maximum Temparature value is 108F")
                 }
                 else if(!height_cm.toString().equals("")&&weightsp.selectedItem.toString().equals("Lb"))
                 {
                     toast("Please enter weight in Kg")
                 }
                 else if(!height_inch.toString().equals("")&&weightsp.selectedItem.toString().equals("Kg"))
                 {
                     toast("Please enter weight in Lb")
                 }
                 else if(height_inch.equals("")&&height_cm.toInt()>400)
                         {
                             toast("Height max value is 400")
                         }
                 else if(height_cm.equals("")&&height_inch.toInt()>10)
                 {
                     toast("Height max value is 10")
                 }
                 else {*/
                var tempad: String = ""
                var heightaaad: String = ""
                if (!temparatureaad.text.toString().equals("")) {
                    tempad = "." + temparatureaad.text.toString()
                }
                if (!heightaad.text.toString().equals("")) {
                    heightaaad = "." + heightaad.text.toString()
                }

                val intent = Intent(requireContext(), VitalsConfirmation::class.java)
                intent.putExtra("appointmentId", PatientAppointment.appointmentId)
                intent.putExtra("patientId", PatientAppointment.patientId)
                intent.putExtra("sender_id", sender_id)
                intent.putExtra("breathingval", breathingval.toString())
                intent.putExtra("breathing2val", breathing2val.toString())
                intent.putExtra("oxygenval", oxygenval.toString())
                intent.putExtra("oxygen2val", oxygen2val.toString())
                intent.putExtra("pulseval", pulseval.toString())
                intent.putExtra("pulse2val", pulse2val.toString())
                intent.putExtra("height_cm", height_cm.toString())
                intent.putExtra("height_inch", height_inch.toString())
                intent.putExtra("heightunit", heightunit.toString())
                intent.putExtra("height_ad", heightaaad.toString())
                intent.putExtra("heightfval", heightfval.toString())
                intent.putExtra("weight_kg", weight_kg.toString())
                intent.putExtra("weight_lb", weight_lb.toString())
                intent.putExtra("weightunit", weightunit.toString())
                intent.putExtra("temparature_f", temparature_f.toString())
                intent.putExtra("temparature_c", temparature_c.toString())
                intent.putExtra("temparature_ad", tempad.toString())
                intent.putExtra("tempunit", tempunit.toString())
                intent.putExtra("sugar_type", sugar_type.toString())
                intent.putExtra("sugarval", sugarval.toString())
                intent.putExtra("bp1val", bp1val.toString())
                intent.putExtra("bp2val", bp2val.toString())
                startActivity(intent)


            }

            vitalhistorybtn.setOnClickListener {
                val intent = Intent(requireContext(), VitalsHistory::class.java)
                intent.putExtra("appointmentId", PatientAppointment.appointmentId)
                intent.putExtra("patientId", PatientAppointment.patientId)
                startActivity(intent)
            }
            bpinfo.setOnClickListener {

                val builder = AlertDialog.Builder(requireContext())
                //set title for alert dialog
                builder.setTitle("Blood Pressure")
                //set message for alert dialog
                builder.setMessage(R.string.bpMessage)
                builder.setIcon(android.R.drawable.ic_dialog_alert)

                //performing positive action
                builder.setPositiveButton("Close") { dialogInterface, which ->

                }
                //performing cancel action

                // Create the AlertDialog
                val alertDialog: AlertDialog = builder.create()
                // Set other dialog properties
                alertDialog.setCancelable(false)
                alertDialog.show()
            }
            slinfo.setOnClickListener {

                val builder = AlertDialog.Builder(requireContext())
                //set title for alert dialog
                builder.setTitle("Sugar Level")
                //set message for alert dialog
                builder.setMessage(R.string.slMessage)
                builder.setIcon(android.R.drawable.ic_dialog_alert)

                //performing positive action
                builder.setPositiveButton("Close") { dialogInterface, which ->

                }
                //performing cancel action

                // Create the AlertDialog
                val alertDialog: AlertDialog = builder.create()
                // Set other dialog properties
                alertDialog.setCancelable(false)
                alertDialog.show()
            }
            tempinfo.setOnClickListener {

                val builder = AlertDialog.Builder(requireContext())
                //set title for alert dialog
                builder.setTitle("Temparature")
                //set message for alert dialog
                builder.setMessage(R.string.tempMessage)
                builder.setIcon(android.R.drawable.ic_dialog_alert)

                //performing positive action
                builder.setPositiveButton("Close") { dialogInterface, which ->

                }
                //performing cancel action

                // Create the AlertDialog
                val alertDialog: AlertDialog = builder.create()
                // Set other dialog properties
                alertDialog.setCancelable(false)
                alertDialog.show()
            }
            oxinfo.setOnClickListener {
                val builder = AlertDialog.Builder(requireContext())
                //set title for alert dialog
                builder.setTitle("Oxygen")
                //set message for alert dialog
                builder.setMessage(R.string.oxMessage)
                builder.setIcon(android.R.drawable.ic_dialog_alert)

                //performing positive action
                builder.setPositiveButton("Close") { dialogInterface, which ->

                }
                //performing cancel action

                // Create the AlertDialog
                val alertDialog: AlertDialog = builder.create()
                // Set other dialog properties
                alertDialog.setCancelable(false)
                alertDialog.show()

            }
            prinfo.setOnClickListener {
                val builder = AlertDialog.Builder(requireContext())
                //set title for alert dialog
                builder.setTitle("Pulse Rate")
                //set message for alert dialog
                builder.setMessage(R.string.prMessage)
                builder.setIcon(android.R.drawable.ic_dialog_alert)

                //performing positive action
                builder.setPositiveButton("Close") { dialogInterface, which ->

                }
                //performing cancel action

                // Create the AlertDialog
                val alertDialog: AlertDialog = builder.create()
                // Set other dialog properties
                alertDialog.setCancelable(false)
                alertDialog.show()

            }
            brinfo.setOnClickListener {

                val builder = AlertDialog.Builder(requireContext())
                //set title for alert dialog
                builder.setTitle("Breathing Rate")
                //set message for alert dialog
                builder.setMessage(R.string.brMessage)
                builder.setIcon(android.R.drawable.ic_dialog_alert)

                //performing positive action
                builder.setPositiveButton("Close") { dialogInterface, which ->

                }
                //performing cancel action

                // Create the AlertDialog
                val alertDialog: AlertDialog = builder.create()
                // Set other dialog properties
                alertDialog.setCancelable(false)
                alertDialog.show()
            }
        }
        more_layout.performClick()
        return root
    }
}

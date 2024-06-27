package com.app.ehealthai.adapters.recyclerViewAdapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.app.ehealthai.doctor.models.responses.VitalsData
import com.app.ehealthaidoctor.R
import com.app.ehealthaidoctor.models.responses.CancelAppointmentResponse
import com.app.ehealthaidoctor.network.ApiUtils
import com.app.ehealthaidoctor.utils.SharedPrefs
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class VitalsHistoryRecyclerViewAdapter(
        val context: Context,
        val vitalsList: ArrayList<VitalsData>,
        val patientId: String
) :
        RecyclerView.Adapter<VitalsHistoryRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val view: View =
                LayoutInflater.from(context).inflate(R.layout.item_vitalshistory_recycler_view, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return vitalsList.size
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val vitalshistory = vitalsList[position]
        with(vitalshistory) {
            try {
                holder.bmitxt.text = "BMI: " + bmi
                holder.date.text = cdate
                holder.name.text= "By "+sender_id
                val notAvailable = "n/a"
                if (temprature_c.equals(null) || temprature_c == "") {
                    if (!temprature_f.equals(null) && temprature_f != "") {
                        holder.temptxt.text = temprature_f + "°F"
                    } else {
                        holder.temptxt.text = "$notAvailable °F"
                    }
                } else if (temprature_f.equals(null) || temprature_f == "") {
                    if (!temprature_c.equals(null) && temprature_c != "") {
                        holder.temptxt.text = temprature_c + "°C"
                    } else {
                        holder.temptxt.text = "$notAvailable °C"
                    }

                }

                if (!bps.equals(null) && bps != "" && !bpd.equals(null) && bpd != "") {
                    holder.bptxt.text = bps + "/" + bpd + " mmHG"
                } else {
                    if (!bps.equals(null) && bps != "") {
                        if (!bpd.equals(null) && bpd != "") {
                            holder.bptxt.text =
                                    bps + "/" + bpd + " mmHG"
                        } else {
                            holder.bptxt.text =
                                    bps + "/" + notAvailable + " mmHG"
                        }
                    } else {
                        if (!bpd.equals(null) && bpd != "") {
                            holder.bptxt.text =
                                    notAvailable + "/" + bpd + " mmHG"
                        } else {
                            holder.bptxt.text =
                                    notAvailable + "/" + notAvailable + " mmHG"
                        }
                    }
                }

                if (!sugar.equals(null) && sugar != ("") && !sugar_type.equals(
                                null
                        ) && sugar_type != ("")
                ) {
                    holder.sugartxt.text =
                            sugar + " mg/dl" + "\n" + sugar_type + ""
                } else {
                    if (!sugar.equals(null) && sugar != ("")) {
                        if (!sugar_type.equals(null) && sugar_type != ("")) {
                            holder.sugartxt.text =
                                    sugar + " mg/dl" + "\n" + sugar_type + ""
                        } else {
                            holder.sugartxt.text =
                                    sugar + " mg/dl" + "\n" + notAvailable + ""
                        }
                    } else {
                        if (!sugar_type.equals(null) && sugar_type != ("")) {
                            holder.sugartxt.text =
                                    notAvailable + " mg/dl" + "\n" + sugar_type + ""
                        } else {
                            holder.sugartxt.text =
                                    notAvailable + " mg/dl" + "\n" + notAvailable + ""
                        }
                    }
                }

                holder.heighttxt.text = height

                if (weight != null && !weight.equals(null) && weight == "") {
                    holder.weighttxt.text = weight + " kg"
                } else {
                    holder.weighttxt.text = "$notAvailable kg"
                }

                if (height_cm.equals(null) || height_cm == "") {
//                                    if (!weight_kg.equals(null)) {
//                                        tvHeightVital.text = height_ft + " cm"
                    if (!height_ft.equals(null) && height_ft != "" && !height_inch.equals(
                                    null
                            ) && height_inch != ""
                    ) {
                        holder.heighttxt.text = height_ft + " " + " ft " + height_inch + " inch"
                    } else {
                        if (!height_ft.equals(null) && height_ft != "") {
                            if (!height_inch.equals(null) && height_inch != "") {
                                holder.heighttxt.text = height_ft + " " + " ft " + height_inch + " inch"
                            } else {
                                holder.heighttxt.text = height_ft + " " + " ft " + "$notAvailable" + " inch"
                            }
                        } else {
                            if (!height_inch.equals(null) && height_inch != "") {
                                holder.heighttxt.text = notAvailable + " " + " ft " + height_inch + " inch"
                            } else {
                                holder.heighttxt.text = notAvailable + " " + " ft " + "$notAvailable" + " inch"
                            }
                        }
                    }

                } else if (height_inch.equals(null) || height_inch == "") {
                    if (!height_cm.equals(null) && height_cm != "") {
                        holder.heighttxt.text = height_cm + " cm"
                    } else {
                        holder.heighttxt.text = notAvailable + " cm"
                    }
                }

                if (weight_kg.equals(null) || weight_kg == "") {
                    if (!weight_lb.equals(null) && weight_lb != "") {
                        holder.weighttxt.text = weight_lb + " lb"
                    } else {
                        holder.weighttxt.text = notAvailable + " lb"
                    }
                } else if (weight_lb.equals(null) || weight_lb == "") {
                    if (!weight_kg.equals(null) && weight_kg != "") {
                        holder.weighttxt.text = weight_kg + " Kg"
                    } else {
                        holder.weighttxt.text = notAvailable + " Kg"
                    }
                }

                if (breathing2.equals(null) || breathing2 == "") {
                    if (breathing == "" || breathing.equals(null))
                        holder.breathtxt.text = "n/a bpm"
                    else
                        holder.breathtxt.text = breathing + " bpm"
                } else {
                    if (breathing == "" || breathing.equals(null))
                        holder.breathtxt.text = notAvailable + " bpm"
                    else
                        holder.breathtxt.text =
                                breathing + " to " + breathing2 + " bpm"
                }

                if (!oxygen2.equals(null) && oxygen2 != "") {
                    if (!oxygen.equals(null) && oxygen != "")
                        holder.oxygentxt.text = oxygen + " SPO2"
                    else
                        holder.oxygentxt.text = notAvailable + " SPO2"
                } else {
                    if (!oxygen.equals(null) && oxygen != "")
                        holder.oxygentxt.text = oxygen + " SPO2"
                    else
                        if (!oxygen2.equals(null) && oxygen2 != "")
                            holder.oxygentxt.text = oxygen2 + " SPO2"
                        else
                            holder.oxygentxt.text = notAvailable + " SPO2"

                }
                if (pulse2.equals(null) || pulse2 == "") {
                    if (!pulse.equals(null) && pulse != "")
                        holder.pulsetxt.text = pulse + " bpm"
                    else
                        holder.pulsetxt.text = notAvailable + " bpm"
                } else {
                    if (!pulse.equals(null) && pulse != "")
                        holder.pulsetxt.text = notAvailable + " to " + pulse2 + " bpm"
                    else
                        holder.pulsetxt.text = pulse + " to " + pulse2 + " bpm"
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
//        holder.date.text = vitalshistory.cdate
//        holder.name.text= "By "+vitalshistory.sender_id
//        if (vitalshistory.temprature_c.equals(null)) {
//            holder.temptxt.text = vitalshistory.temprature_f + "°F"
//        } else if (vitalshistory.temprature_f.equals(null)) {
//            holder.temptxt.text = vitalshistory.temprature_c + "°C"
//        }
//
//        holder.bptxt.text = vitalshistory.bps + "/" + vitalshistory.bpd + " mmHG"
//        holder.sugartxt.text = vitalshistory.sugar + " mg/dl" + "\n" + vitalshistory.sugar_type + ""
//        holder.heighttxt.text = vitalshistory.height
//        holder.weighttxt.text = vitalshistory.weight + " kg"
//        if (vitalshistory.height_cm.equals(null)) {
//            if (!vitalshistory.weight_kg.equals(null)) {
//                holder.heighttxt.text = vitalshistory.height_ft + " cm"
//            } else {
//                holder.heighttxt.text = vitalshistory.height_inch + " ft"
//            }
//        } else if (vitalshistory.height_inch.equals(null)) {
//            holder.heighttxt.text = vitalshistory.height_cm + " cm"
//        }
//        if (vitalshistory.weight_kg.equals(null)) {
//            holder.weighttxt.text = vitalshistory.weight_lb + " Lb"
//        } else if (vitalshistory.weight_lb.equals(null)) {
//            holder.weighttxt.text = vitalshistory.weight_kg + " Kg"
//        }
//        if (vitalshistory.breathing2.equals(null)) {
//            holder.breathtxt.text = vitalshistory.breathing + " ppm"
//        } else {
//            holder.breathtxt.text =
//                vitalshistory.breathing + " to " + vitalshistory.breathing2 + " ppm"
//        }
//        /* if(vitalshistory.oxygen2.equals(null))
//         {
//             holder.oxygentxt.text= vitalshistory.oxygen+" SPO2"
//         }
//         else {
//             holder.oxygentxt.text = vitalshistory.oxygen + " %s\n" + vitalshistory.oxygen2 + " %p"
//         }*/
//        if (vitalshistory.pulse2.equals(null)) {
//            holder.pulsetxt.text = vitalshistory.pulse + " bpm"
//        } else {
//            holder.pulsetxt.text = vitalshistory.pulse + " to " + vitalshistory.pulse2 + " bpm"
//        }
//        holder.bmitxt.text = "BMI: " + vitalshistory.bmi

            holder.detailsBtn.setOnClickListener {

                if (holder.detailsBtn.text.equals("View Details")) {
                    holder.weightt_layout.visibility = View.VISIBLE
                    holder.weightt_layout2.visibility = View.VISIBLE
                    holder.weightt_layout3.visibility = View.VISIBLE
                    holder.weightt_layout4.visibility = View.VISIBLE
                    holder.detailsBtn.text = "Hide Details"
                } else if (holder.detailsBtn.text.equals("Hide Details")) {
                    holder.weightt_layout.visibility = View.GONE
                    holder.weightt_layout2.visibility = View.GONE
                    holder.weightt_layout3.visibility = View.GONE
                    holder.weightt_layout4.visibility = View.GONE
                    holder.detailsBtn.text = "View Details"
                }
            }

            holder.delh.setOnClickListener {

                val builder = AlertDialog.Builder(context)

                // Set the alert dialog title
                builder.setTitle("Delete Vital")

                // Display a message on alert dialog
                builder.setMessage("Are you sure you want to delete this vital?")

                // Set a positive button and its click listener on alert dialog
                builder.setPositiveButton("Yes") { dialog, which ->
                    holder.progress_vhr.visibility = View.VISIBLE
                    var vitalid = vitalshistory.id
                    val userData = SharedPrefs.getUserData(context)
                    val username = userData!!.fname + " " + userData.lname
                    val usertype = "DOCTOR"
                    ApiUtils.getAPIService(context).delete_vitals(vitalid.toString(), SharedPrefs.getString(context, "sessionid")!!, username, usertype).enqueue(object : Callback<CancelAppointmentResponse> {
                        override fun onFailure(call: Call<CancelAppointmentResponse>, t: Throwable) {
                            holder.progress_vhr.visibility = View.GONE
                            Toast.makeText(context, t.toString(), Toast.LENGTH_LONG).show()
                        }

                        override fun onResponse(call: Call<CancelAppointmentResponse>, response: Response<CancelAppointmentResponse>) {
                            holder.progress_vhr.visibility = View.GONE
                            Toast.makeText(context, "Vital Deleted Successfully !", Toast.LENGTH_LONG)
                                    .show()
                            // (context as Activity).finish()
                            // vitalsList.removeAt(position)
                            deleteItem(position)
                            // notifyDataSetChanged()

                            /*  val intent = Intent(context, VitalsHistory::class.java)

                          intent.putExtra("appointmentId", "")
                          intent.putExtra("patientId", patientId)

                          context.startActivity(intent)*/

                        }
                    })
                }


                // Display a negative button on alert dialog
                builder.setNegativeButton("No") { dialog, which ->

                }

                // Finally, make the alert dialog using builder
                val dialog: AlertDialog = builder.create()

                // Display the alert dialog on app interface
                dialog.show()
            }

        }
    }

    private fun deleteItem(position: Int) {


        vitalsList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, vitalsList.size)
    }


    // stores and recycles views as they are scrolled off screen
    class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val date by lazy { itemView.findViewById<TextView>(R.id.datee) }
        val name by lazy { itemView.findViewById<TextView>(R.id.name) }
        val temptxt by lazy { itemView.findViewById<TextView>(R.id.temptxt) }
        val bptxt by lazy { itemView.findViewById<TextView>(R.id.bptxt) }
        val sugartxt by lazy { itemView.findViewById<TextView>(R.id.sugartxt) }
        val heighttxt by lazy { itemView.findViewById<TextView>(R.id.heighttxt) }
        val weighttxt by lazy { itemView.findViewById<TextView>(R.id.weighttxt) }
        val breathtxt by lazy { itemView.findViewById<TextView>(R.id.breathtxt) }
        val oxygentxt by lazy { itemView.findViewById<TextView>(R.id.oxygentxt) }
        val pulsetxt by lazy { itemView.findViewById<TextView>(R.id.pulsetxt) }
        val detailsBtn by lazy { itemView.findViewById<TextView>(R.id.viewdetails) }
        val delh by lazy { itemView.findViewById<ImageView>(R.id.delh) }
        val progress_vhr by lazy { itemView.findViewById<ProgressBar>(R.id.progress_vhr) }
        val weightt_layout by lazy { itemView.findViewById<LinearLayout>(R.id.weightt_layout) }
        val weightt_layout2 by lazy { itemView.findViewById<LinearLayout>(R.id.weightt_layout2) }
        val weightt_layout3 by lazy { itemView.findViewById<LinearLayout>(R.id.weightt_layout3) }
        val weightt_layout4 by lazy { itemView.findViewById<LinearLayout>(R.id.weightt_layout4) }
        val bmitxt by lazy { itemView.findViewById<TextView>(R.id.bmival) }

    }
}
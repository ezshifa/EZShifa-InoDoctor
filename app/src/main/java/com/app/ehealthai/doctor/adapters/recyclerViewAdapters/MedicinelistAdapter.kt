package com.app.ehealthai.doctor.adapters.recyclerViewAdapters

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.app.ehealthai.doctor.ui.activities.PatientDetails.PatientAppointment
import com.app.ehealthai.doctor.ui.activities.PatientDetails.ui.dashboard.AddNotesFragment
import com.app.ehealthai.doctor.utils.Global
import com.app.ehealthaidoctor.R
import com.app.ehealthaidoctor.models.responses.CancelAppointmentResponse
import com.app.ehealthaidoctor.network.ApiUtils
import com.app.ehealthaidoctor.ui.activities.AppointmentsActivity
import com.app.ehealthaidoctor.ui.activities.HomeActivity
import com.app.ehealthaidoctor.ui.activities.VideoSessionActivity
import com.app.ehealthaidoctor.utils.SharedPrefs
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MedicinelistAdapter(
    private val context: Activity,
    private val name: ArrayList<String>,
    private val quant: ArrayList<String>,
    private val dosage: ArrayList<String>,
    private val medTimePeriodListlist: ArrayList<String>,
    private val medUnitlist: ArrayList<String>
) : ArrayAdapter<String>(context, R.layout.medicine_list, name) {


    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.medicine_list, null, true)

        val mname = rowView.findViewById(R.id.medname) as TextView
        val delmed = rowView.findViewById(R.id.delmed) as ImageView
        val mquantity = rowView.findViewById(R.id.medquantity) as TextView
        val mdosage = rowView.findViewById(R.id.meddosage) as TextView
        val medunit = rowView.findViewById(R.id.medunit) as TextView
        val medtimeperiod = rowView.findViewById(R.id.medtimeperiod) as TextView

        try {
            mname.text = name[position]
            mquantity.text = quant[position]
            mdosage.text = dosage[position]
            medunit.text = medTimePeriodListlist[position]
            medtimeperiod.text = medUnitlist[position]
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        delmed.setOnClickListener {
            AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.delete_am_dialog_title))
                .setMessage(context.resources.getString(R.string.delete_am_dialog_message))
                .setPositiveButton(
                    context.getString(R.string.cancel_confirm_dialog_confirm),
                    object : DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface?, which: Int) {

                            if (Global.isfromVideoSessionActivity) {
                                VideoSessionActivity.mednamelist.removeAt(position)
                                VideoSessionActivity.meddosagelist.removeAt(position)
                                VideoSessionActivity.medquantitylist.removeAt(position)
                                VideoSessionActivity.medUnitlist.removeAt(position)
                                VideoSessionActivity.medTimePeriodListlist.removeAt(position)
                                AddNotesFragment.adapter!!.notifyDataSetChanged()
                            } else {
                                PatientAppointment.mednamelist?.removeAt(position)
                                PatientAppointment.meddosagelist?.removeAt(position)
                                PatientAppointment.medquantitylist?.removeAt(position)
                                PatientAppointment.medUnitlist?.removeAt(position)
                                PatientAppointment.medTimePeriodListlist?.removeAt(position)
                                AddNotesFragment.adapter!!.notifyDataSetChanged()
                            }

                        }
                    })
                .setNegativeButton(
                    context.resources.getString(R.string.cancel_confirm_dialog_dismiss),
                    object : DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface?, which: Int) {
                            dialog?.dismiss()
                        }
                    })
                .show()
        }
        return rowView
    }
}
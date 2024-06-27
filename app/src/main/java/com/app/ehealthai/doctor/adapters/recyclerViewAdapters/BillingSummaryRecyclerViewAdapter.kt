package com.app.ehealthai.adapters.recyclerViewAdapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.ehealthai.models.responses.summary
import com.app.ehealthaidoctor.R

class BillingSummaryRecyclerViewAdapter (
    val context: Context,
    val BillingList: List<summary>
) :
    RecyclerView.Adapter<BillingSummaryRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.item_bs_recycler_view, parent, false)
        return ViewHolder(view)
    }
    override fun getItemCount(): Int {
        return BillingList.size
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val Billing = BillingList.get(position)

        holder.refid.text = "Ref Id : #"+Billing.appointmentid.toString()
        holder.status.text= "Status: "+Billing.status
        holder.dname.text= Billing.patient_fname+" "+Billing.patient_lname
        holder.amount.text= "Rs."+Billing.amount
        holder.aptype.text= "Type: "+Billing.pc_appt_type
        holder.amtype.text= Billing.paymentname
        holder.date.text= "Date: "+Billing.transcation_completed_on




    }


    // stores and recycles views as they are scrolled off screen
    class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val refid by lazy { itemView.findViewById<TextView>(R.id.refid) }
        val status by lazy { itemView.findViewById<TextView>(R.id.status) }
        val dname by lazy { itemView.findViewById<TextView>(R.id.dname) }
        val amount by lazy { itemView.findViewById<TextView>(R.id.amount) }
        val aptype by lazy { itemView.findViewById<TextView>(R.id.aptype) }
        val amtype by lazy { itemView.findViewById<TextView>(R.id.amtype) }
        val date by lazy { itemView.findViewById<TextView>(R.id.date) }




    }
}
package com.app.ehealthai.doctor.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.ehealthai.doctor.models.responses.getOstacleDataREsponse.Child
import com.app.ehealthaidoctor.R

class ChildDetailsAdapter(
    var context: Context,
    onlineList: ArrayList<Child>?
) :
    RecyclerView.Adapter<ChildDetailsAdapter.ViewHolder>() {

    var dataList: ArrayList<Child> = onlineList!!

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_child_details, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        if (dataList[position]?.childno != null) {

            holder.tv_kidNo.setText(dataList[position]?.childno.toString())

        } else {
            holder.tv_kidNo.setText("Not Provided")
        }


        if (dataList[position]?.age != null) {
            holder.tv_age.setText(dataList[position]?.age.toString())
        } else {
            holder.tv_age.setText("Not Provided")
        }

        if (dataList[position]?.gender != null) {
            holder.tv_gender.setText(dataList[position]?.gender.toString())
        } else {
            holder.tv_gender.setText("Not Provided")
        }



        if (dataList[position]?.liveStatus != null) {
            if (dataList[position]?.liveStatus.equals("if Dead select the reason")) {

                holder.tv_lifeStatus.setText("")
            } else {
                holder.tv_lifeStatus.setText(dataList[position]?.liveStatus.toString())
            }

        } else {
            holder.tv_lifeStatus.setText("Not Provided")
        }

        if (dataList[position]?.deathReason != null) {
            holder.tv_deathReason.setText(dataList[position]?.deathReason.toString())
        } else {
            holder.tv_deathReason.setText("Not Provided")
        }


        if (dataList[position]?.deliveryType != null) {

            holder.tv_delieverMode.setText(dataList[position]?.deliveryType.toString())

        } else {
            holder.tv_delieverMode.setText("Not Provided")
        }

    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tv_kidNo: TextView
        var tv_lifeStatus: TextView
        var tv_age: TextView
        var tv_gender: TextView
        var tv_delieverMode: TextView
        var tv_deathReason: TextView


        init {

            tv_kidNo = itemView.findViewById(R.id.tv_kidNo)
            tv_lifeStatus = itemView.findViewById(R.id.tv_lifeStatus)
            tv_age = itemView.findViewById(R.id.tv_age)
            tv_gender = itemView.findViewById(R.id.tv_gender)
            tv_delieverMode = itemView.findViewById(R.id.tv_delieverMode)
            tv_deathReason = itemView.findViewById(R.id.tv_deathReason)


        }
    }


}

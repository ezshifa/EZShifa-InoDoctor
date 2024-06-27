package com.app.ehealthaidoctor.adapters.recyclerViewAdapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.app.ehealthai.doctor.models.responses.LogoutResponse.LogoutResonse
import com.app.ehealthaidoctor.R
import com.app.ehealthaidoctor.network.ApiUtils
import com.app.ehealthaidoctor.ui.activities.AppointmentsActivity
import com.app.ehealthaidoctor.ui.activities.BillingSummary
import com.app.ehealthaidoctor.ui.activities.Chat.UserChatsActivity
import com.app.ehealthaidoctor.ui.activities.HomeActivity
import com.app.ehealthaidoctor.ui.activities.LoginActivity
import com.app.ehealthaidoctor.ui.activities.profile.ProfileActivity
import com.app.ehealthaidoctor.utils.SharedPrefs
import com.example.ehealthai.utils.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeNavigationRecyclerViewAdapter(
    val context: Context,
    val homeActivity: HomeActivity
) :
    RecyclerView.Adapter<HomeNavigationRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(context)
                .inflate(R.layout.recycler_view_home_navigation, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = 1

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.logOutLayout.setOnClickListener {
//            if (homeActivity.docStatus == "1") {
            if (!homeActivity.servicerunning)
                homeActivity.performLogOutWithoutAskingReason()
            else
                homeActivity.showBreakSelectionDialog(true)
//            } else {
//                Toast.makeText(homeActivity, "You are not online", Toast.LENGTH_SHORT).show()
//            }
            /*  SharedPrefs.removeUserData(context)
              SharedPrefs.removeKey(context, Constants.USERNAME)
              SharedPrefs.removeKey(context, Constants.PASSWORD)
              SharedPrefs.save(context, "TOKEN_REFRESHED", false)
              context.startActivity(Intent(context, LoginActivity::class.java))
              (context as Activity).finish()*/
        }

        holder.videoCall.setOnClickListener {
            context.startActivity(Intent(context, UserChatsActivity::class.java))
        }
        holder.profileBtn.setOnClickListener {
            context.startActivity(Intent(context, ProfileActivity::class.java))
        }
        holder.Settingsbtn.setOnClickListener {
            //  context.toast("Under development")
            context.startActivity(Intent(context, AppointmentsActivity::class.java))
        }
        holder.billinglayout.setOnClickListener {
            //  context.toast("Under development")
            context.startActivity(Intent(context, BillingSummary::class.java))
        }
        holder.referdoctor.setOnClickListener {
            //  context.toast("Under development")
            val myIntent = Intent(Intent.ACTION_SEND)

            myIntent.setType("text/plain");

            myIntent.putExtra(Intent.EXTRA_SUBJECT, "Share App");
            myIntent.putExtra(
                Intent.EXTRA_TEXT,
                "Check out EZShifa doctor app, I use it from anywhere in the world to stay in touch with my patients.Get it free at https://play.google.com/store/apps/details?id=com.app.ehealthai.doctor"
            );
            context.startActivity(Intent.createChooser(myIntent, "Share using"));
        }
        holder.referpatient.setOnClickListener {
            //  context.toast("Under development")
            val myIntent = Intent(Intent.ACTION_SEND)

            myIntent.setType("text/plain");

            myIntent.putExtra(Intent.EXTRA_SUBJECT, "Share App");
            myIntent.putExtra(
                Intent.EXTRA_TEXT,
                "Check out EZShifa patient app, I use it for my online health consultation with doctors from all over the world. Get it free at https://play.google.com/store/apps/details?id=com.app.ehealthai.patient"
            );
            context.startActivity(Intent.createChooser(myIntent, "Share using"));
        }
        holder.registerpatientlayout.setOnClickListener {
            //context.startActivity(Intent(context, RegisterNewPatient::class.java))
        }

    }


    // stores and recycles views as they are scrolled off screen
    class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val logOutLayout by lazy { itemView.findViewById<RelativeLayout>(R.id.logout_layout) }
        val videoCall by lazy { itemView.findViewById<RelativeLayout>(R.id.layout_4) }
        val profileBtn by lazy { itemView.findViewById<RelativeLayout>(R.id.layout_1) }
        val Settingsbtn by lazy { itemView.findViewById<RelativeLayout>(R.id.layout_3) }
        val referdoctor by lazy { itemView.findViewById<RelativeLayout>(R.id.referd_layout) }
        val referpatient by lazy { itemView.findViewById<RelativeLayout>(R.id.referp_layout) }
        val billinglayout by lazy { itemView.findViewById<RelativeLayout>(R.id.layout_billing) }
        val registerpatientlayout by lazy { itemView.findViewById<RelativeLayout>(R.id.registerpatientlayout) }

    }

    private fun calligLogOutApi(username: String) {
        homeActivity.showProgressDialog("Logging out...")
        ApiUtils.getAPIService(context).Logout(username).enqueue(object : Callback<LogoutResonse> {
            override fun onFailure(call: Call<LogoutResonse>, t: Throwable) {
                Toast.makeText(
                    context,
                    "Something went wrong, please try again later",
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun onResponse(call: Call<LogoutResonse>, response: Response<LogoutResonse>) {
                homeActivity.hideProgressDialog()
                SharedPrefs.removeUserData(context)
                SharedPrefs.removeKey(context, Constants.USERNAME)
                SharedPrefs.removeKey(context, Constants.PASSWORD)
                SharedPrefs.save(context, "TOKEN_REFRESHED", false)
                homeActivity.startActivity(Intent(homeActivity, LoginActivity::class.java))
                homeActivity.finish()
            }
        })
    }
}

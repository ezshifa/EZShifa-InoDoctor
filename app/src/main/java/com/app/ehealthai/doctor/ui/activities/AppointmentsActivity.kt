package com.app.ehealthaidoctor.ui.activities

import android.content.Context
import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.app.ehealthaidoctor.R
import com.app.ehealthaidoctor.models.EventDecorator
import com.app.ehealthaidoctor.models.responses.AppointmentDoctorData
import com.app.ehealthaidoctor.models.responses.GetAppointmentDoctorList
import com.app.ehealthaidoctor.network.ApiUtils
import com.app.ehealthaidoctor.ui.fragments.appointmentTypes.OnlineFragment
import com.app.ehealthaidoctor.ui.fragments.appointmentTypes.WalkinFragment
import com.app.ehealthaidoctor.utils.SharedPrefs
import com.crashlytics.android.Crashlytics
import com.example.ehealthai.utils.Constants
import com.example.ehealthai.utils.toast
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import io.fabric.sdk.android.Fabric
import kotlinx.android.synthetic.main.activity_appointments.appointments_back_btn
import kotlinx.android.synthetic.main.activity_appointments.appt_type_tab_layout
import kotlinx.android.synthetic.main.activity_appointments.appt_type_view_pager
import kotlinx.android.synthetic.main.activity_appointments.booking_progress_bar
import kotlinx.android.synthetic.main.activity_appointments.calender_view
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat

class AppointmentsActivity : AppCompatActivity(), WalkinFragment.OnFragmentInteractionListener {

    override fun onFragmentInteraction(docExtension: String) {

    }

    val calendarDays: ArrayList<CalendarDay> = ArrayList()

    companion object {
        val walkinList: ArrayList<AppointmentDoctorData> = ArrayList()
        val onlineList: ArrayList<AppointmentDoctorData> = ArrayList()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fabric.with(this, Crashlytics())
        setContentView(R.layout.activity_appointments)
        init()
    }


    fun init() {
        appointments_back_btn.setOnClickListener { onBackPressed() }
        calender_view.selectedDate = CalendarDay.today()

        booking_progress_bar.visibility = View.VISIBLE
        calender_view.isEnabled = false

        val userData = SharedPrefs.getUserData(this)
        val username = userData!!.username
        val usertype = "DOCTOR"
        ApiUtils.getAPIService(this).getAppointmentDoctors(
            userData!!.id.toString(),
            SharedPrefs.getString(this@AppointmentsActivity, "sessionid")!!,
            username!!,
            usertype
        )
            .enqueue(object : Callback<GetAppointmentDoctorList> {
                override fun onFailure(call: Call<GetAppointmentDoctorList>, t: Throwable) {

                    calender_view.isEnabled = true
                    booking_progress_bar.visibility = View.GONE
                    toast("Something went wrong, please try again later")
                }

                override fun onResponse(
                    call: Call<GetAppointmentDoctorList>,
                    response: Response<GetAppointmentDoctorList>
                ) {
                    try {
                        walkinList.clear()
                        onlineList.clear()

                        calender_view.isEnabled = true
                        booking_progress_bar.visibility = View.GONE

                        if (response.body()!!.statuscode.equals(404)) {
                            toast(response.body()!!.data!!.error)
                            SharedPrefs.removeUserData(this@AppointmentsActivity)
                            SharedPrefs.removeKey(this@AppointmentsActivity, Constants.USERNAME)
                            SharedPrefs.removeKey(this@AppointmentsActivity, Constants.PASSWORD)
                            SharedPrefs.save(this@AppointmentsActivity, "TOKEN_REFRESHED", false)
                            val intent =
                                Intent(this@AppointmentsActivity, LoginActivity::class.java)
                            startActivity(intent)
                            finishAffinity()
                        }
                        else if (response.isSuccessful && response.body() != null && response.body()!!.data != null && !response.body()!!.data!!.list!!.isNullOrEmpty()) {

                            val appointmentList: ArrayList<AppointmentDoctorData> = ArrayList()
                            appointmentList.addAll(response.body()!!.data!!.list!!)

                            for (i in 0 until response.body()!!.data!!.list!!.size) {
                                val date = response.body()!!.data!!.list!!.get(i).apptdate.split("-")
                                val year = date[0].toInt()
                                val month = date[1].toInt() - 1
                                val day = date[2].toInt()

                                val calendarDay: CalendarDay = CalendarDay.from(year, month, day)
                                calendarDays.add(calendarDay)
                            }

                            val decorator = EventDecorator(
                                resources.getColor(R.color.colorPrimary),
                                calendarDays
                            )
                            calender_view.addDecorator(decorator)
                            calender_view.selectedDate = CalendarDay.today()
                            val selectedDay = CalendarDay.today()
                            val selectedList: ArrayList<AppointmentDoctorData> = ArrayList()

                            if (calendarDays.contains(selectedDay)) {
                                selectedList.clear()
                                for (i in 0 until appointmentList.size) {
                                    val formatter = SimpleDateFormat("yyyy-MM-dd")
                                    val selectedDate = formatter.format(selectedDay.date)

                                    val bookingDate = appointmentList.get(i).apptdate
                                    if (selectedDate.equals(bookingDate)) {
                                        selectedList.add(appointmentList.get(i))
                                        Log.e("Docotor Notes", appointmentList.get(i).doctor_notes)
                                    }
                                }
                            }

                            onlineList.clear()
                            walkinList.clear()

                            if (selectedList.isNotEmpty()) {
                                for (i in 0 until selectedList.size) {
                                    if (selectedList.get(i).pc_appt_type.equals("online")) {
                                        onlineList.add(selectedList.get(i))
                                    } else {
                                        walkinList.add(selectedList.get(i))
                                    }
                                }
                            }

                            calender_view.setOnDateChangedListener(object : OnDateSelectedListener {
                                override fun onDateSelected(
                                    p0: MaterialCalendarView,
                                    selectedDay: CalendarDay,
                                    p2: Boolean
                                ) {

                                    selectedList.clear()
                                    onlineList.clear()
                                    walkinList.clear()

                                    if (calendarDays.contains(selectedDay)) {


//                                    ///////////////////////////////
//
//                                    ApiUtils.getAPIService(this@AppointmentsActivity).getAppointmentList("", "","").enqueue(object : Callback<DoctorAppointmentListResponse> {
//                                        override fun onFailure(call: Call<DoctorAppointmentListResponse>, t: Throwable) {
//                                          //  signInDialog!!.dismiss()
//                                            toast("Something went wrong")
//                                        }
//
//                                        override fun onResponse(call: Call<DoctorAppointmentListResponse>, response: Response<DoctorAppointmentListResponse>) {
//
//                                            try{  if (response.body() != null && response.body()!!.statuscode == 200) {
//
//                                                val userData: Profile = response.body()!!.data.profile!!.get(0)
//                                                if (response.body()!!.data.profile!!.size > 1) {
//                                                    if (response.body()!!.data.profile!!.get(1).image_name != null)
//                                                        Log.e("Profile pic:",response.body()!!.data.profile!!.get(1).image_name)
//                                                    userData.image_name = "http://www.emr.e-healthai.com/doc_profile_image/" + response.body()!!.data.profile!!.get(1).image_name
//                                                    if (!response.body()!!.data.profile!!.get(1).experience.isNullOrEmpty())
//                                                        userData.experience = response.body()!!.data.profile!!.get(1).experience
//                                                    if (!response.body()!!.data.profile!!.get(1).degrees.isNullOrEmpty())
//                                                        userData.degrees = response.body()!!.data.profile!!.get(1).degrees
//                                                }
//
//                                                SharedPrefs.setUserData(this@AppointmentsActivity, userData)
//                                                val intent = Intent(this@AppointmentsActivity, HomeActivity::class.java)
//                                                intent.putExtra("name", userData.fname + " " + userData.lname)
//                                                startActivity(intent)
//                                                finish()
//                                            } else
//                                                toast("Invalid login!")
//                                            }catch(e:Exception){
//                                                toast("Error: Try again")
//                                            }
//
//                                        }
//                                    })
//
//
//
//                                    ///////////////////////////////////////
                                        for (i in 0 until appointmentList.size) {
                                            val formatter = SimpleDateFormat("yyyy-MM-dd")
                                            val selectedDate = formatter.format(selectedDay.date)
                                            val bookingDate = appointmentList.get(i).apptdate
                                            if (selectedDate.equals(bookingDate)) {
                                                selectedList.add(appointmentList.get(i))
                                            }
                                        }
                                    }
                                    if (selectedList.isNotEmpty()) {
                                        for (i in 0 until selectedList.size) {
                                            if (selectedList.get(i).pc_appt_type.equals("online")) {
                                                onlineList.add(selectedList.get(i))
                                            } else {
                                                walkinList.add(selectedList.get(i))
                                            }
                                        }
                                    }
                                    setupViewPager()
                                }
                            })
                        } else {
                            toast("No Appointments Found")
                        }
                        setupViewPager()

                    } catch (e: Exception) {
                        toast("Something went wrong, please try again later")
                    }
                }


            })

    }

    fun setupViewPager() {
        appt_type_tab_layout.setupWithViewPager(appt_type_view_pager)
        val adapter = AppointmentTypeFragmentAdapter(supportFragmentManager, this)
        appt_type_view_pager.adapter = adapter

        val linearLayout = appt_type_tab_layout.getChildAt(0) as LinearLayout
        linearLayout.showDividers = LinearLayout.SHOW_DIVIDER_MIDDLE
        val drawable = GradientDrawable()
        drawable.setColor(this.resources.getColor(R.color.grey))
        drawable.setSize(3, 1)
        linearLayout.dividerPadding = 10
        linearLayout.dividerDrawable = drawable
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == 2) {
            finish()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    inner class AppointmentTypeFragmentAdapter(fm: FragmentManager, val context: Context) :
        FragmentStatePagerAdapter(fm) {
        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> {
                    WalkinFragment()
                }

                1 -> {
                    OnlineFragment()
                }

                else -> {
                    WalkinFragment()
                }

            }
        }

        override fun getCount(): Int = 2

        override fun getPageTitle(position: Int): CharSequence? {
            return when (position) {
                0 -> context.resources.getString(R.string.walkin_VIEWPAGER)
                1 -> context.resources.getString(R.string.online_VIEWPAGER)
                else -> context.resources.getString(R.string.walkin_VIEWPAGER)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        init()
    }
}

package com.app.ehealthai.doctor.ui.activities

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.app.ehealthaidoctor.R
import com.app.ehealthaidoctor.models.responses.Document
import com.app.ehealthaidoctor.models.responses.GetPatientDocumentsResponse
import com.app.ehealthaidoctor.network.ApiUtils
import com.app.ehealthaidoctor.ui.fragments.documents.LabReportsFragment
import com.app.ehealthaidoctor.ui.fragments.documents.MiscFragment
import com.app.ehealthaidoctor.ui.fragments.documents.XRayFragment
import com.app.ehealthaidoctor.utils.SharedPrefs
import com.example.ehealthai.utils.toast
import kotlinx.android.synthetic.main.activity_patient_documents.documents_progress_bar
import kotlinx.android.synthetic.main.activity_patient_documents.user_docs_back_btn
import kotlinx.android.synthetic.main.activity_patient_documents.user_docs_tab
import kotlinx.android.synthetic.main.activity_patient_documents.user_docs_view_pager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VideoSessionPatientReports : AppCompatActivity(),
    LabReportsFragment.OnFragmentInteractionListener {
    override fun onFragmentInteraction(docExtension: String) {

    }

    var patientId: String? = null

    companion object {
        var labReportsList: ArrayList<Document> = ArrayList()
        var xRayList: ArrayList<Document> = ArrayList()
        var docNotesList: ArrayList<Document> = ArrayList()
        var miscList: ArrayList<Document> = ArrayList()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patient_documents)


        init()

    }

    private fun setupViewpager() {
        user_docs_tab.setupWithViewPager(user_docs_view_pager)
        val adapter = UserDocumentsFragmentAdapter(supportFragmentManager, this)
        user_docs_view_pager.adapter = adapter


        val linearLayout = user_docs_tab.getChildAt(0) as LinearLayout
        linearLayout.showDividers = LinearLayout.SHOW_DIVIDER_MIDDLE
        val drawable = GradientDrawable()
        drawable.setColor(this.resources.getColor(R.color.grey))
        drawable.setSize(3, 1)
        linearLayout.dividerPadding = 10
        linearLayout.dividerDrawable = drawable

    }


    fun init() {
        user_docs_back_btn.setOnClickListener {
            onBackPressed()
        }
        patientId = intent.getStringExtra("patientId")
        val userData = SharedPrefs.getUserData(this@VideoSessionPatientReports)
        val username = userData!!.username
        val usertype = "DOCTOR"
        documents_progress_bar.visibility = View.VISIBLE
        ApiUtils.getAPIService(this).getPatientDocuments(
            patientId!!,
            SharedPrefs.getString(this@VideoSessionPatientReports, "sessionid")!!,
            username!!,
            usertype
        ).enqueue(object :
            Callback<GetPatientDocumentsResponse> {
            override fun onFailure(call: Call<GetPatientDocumentsResponse>, t: Throwable) {
                documents_progress_bar.visibility = View.GONE
                toast("Something went wrong, please try again later")
            }

            override fun onResponse(
                call: Call<GetPatientDocumentsResponse>,
                response: Response<GetPatientDocumentsResponse>
            ) {
                documents_progress_bar.visibility = View.GONE
                try {
                    if (response.isSuccessful && response.body() != null && response.body()!!.statuscode == 200 && response.body()!!.data.documents != null) {
                        labReportsList = ArrayList()
                        xRayList = ArrayList()
                        docNotesList = ArrayList()
                        miscList = ArrayList()

                        for (i in 0 until response.body()!!.data.documents!!.size) {

                            if (response.body()!!.data.documents!!.get(i).doc_type.toLowerCase()
                                    .contains("lab_report")
                            ) {
                                labReportsList.add(response.body()!!.data.documents!!.get(i))
                            } else if (response.body()!!.data.documents!!.get(i).doc_type.toLowerCase()
                                    .contains("x_ray")
                            ) {
                                xRayList.add(response.body()!!.data.documents!!.get(i))
                            } else if (response.body()!!.data.documents!!.get(i).doc_type.toLowerCase()
                                    .contains("doc_notes")
                            ) {
                                docNotesList.add(response.body()!!.data.documents!!.get(i))
                            } else {
                                miscList.add(response.body()!!.data.documents!!.get(i))
                            }
                        }

                        setupViewpager()
                    }

                } catch (e: Exception) {
                    toast("Something went wrong, please try again later")
                }
            }
        })
        user_docs_tab.setTabTextColors(R.color.black, R.color.blue)
        user_docs_tab.setBackgroundColor(resources.getColor(R.color.white))
    }


    inner class UserDocumentsFragmentAdapter(fm: FragmentManager, val context: Context) :
        FragmentStatePagerAdapter(fm) {
        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> {
                    LabReportsFragment()
                }

                1 -> {
                    XRayFragment()
                }

                2 -> {
                    MiscFragment()
                }

                3 -> {
                    MiscFragment()
                }

                else -> {
                    LabReportsFragment()
                }

            }
        }

        // override fun getCount(): Int = 4
        override fun getCount(): Int = 3

        override fun getPageTitle(position: Int): CharSequence? {
            return when (position) {
                0 -> context.resources.getString(R.string.LAB_REPORTS_VIEWPAGER)
                1 -> context.resources.getString(R.string.X_RAY_VIEWPAGER)
                2 -> context.resources.getString(R.string.MISC_VIEWPAGER)
                3 -> context.resources.getString(R.string.MISC_VIEWPAGER)
                else -> null
            }
        }
    }
}
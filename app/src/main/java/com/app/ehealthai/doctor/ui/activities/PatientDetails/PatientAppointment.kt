package com.app.ehealthai.doctor.ui.activities.PatientDetails

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.app.ehealthai.doctor.models.responses.ForgotPasswordResponse
import com.app.ehealthai.doctor.ui.activities.PatientDetails.ui.dashboard.AddNotesFragment
import com.app.ehealthaidoctor.R
import com.app.ehealthaidoctor.network.ApiUtils
import com.app.ehealthaidoctor.ui.fragments.documents.LabReportsFragment
import com.app.ehealthaidoctor.utils.SharedPrefs
import com.example.ehealthai.utils.toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PatientAppointment : AppCompatActivity(), LabReportsFragment.OnFragmentInteractionListener {
    override fun onFragmentInteraction(docExtension: String) {
    }

    companion object {
        var doctorId: String? = null
        var appointmentId: String? = null
        var appointmentIdSend: String? = null
        var patientId: String? = null
        var patientName: String? = null
        var doc_notes: String? = null
        var description: String? = null
        var doctorName: String? = null
        var apptstatus: String? = null
        var apptTime: String? = null
        var apptDate: String? = null
        var email: String? = null
        var roomid: String = ""
        var age: String? = null
        var sex: String? = ""
        var profile_image: String? = ""
        var studentFatherPhone: String? = ""
        var registeredFrom: String? = ""
        var studentCNIC: String? = ""

        var doctorNotes: String? = ""

        var isFollowUpChecked: Boolean = false
        var isApiCalled: Boolean = false
        var isDiagnosisUpdated: Boolean = false
        var followUpDate: String? = ""

        var isEdited: Boolean = false
        var followUpCheckedFromExistingNotes: Boolean = false

        var addClinicalTestItemSelected: String? = ""
        var addDiagnosisItemSelected: String? = ""
        var otherDiagnosisText: String? = ""
        var addMedicine: String? = ""
        var medQty: String? = ""
        var medUnit: String? = ""
        var medFrequency: String? = ""
        var medPeriod: String? = ""
        var medDescription: String? = ""
        var medDescriptionDetails: String? = ""

        var addClinicTestsList: ArrayList<String> = ArrayList()
        var addDiagnosisList: ArrayList<String> = ArrayList()

        var selectedClinicalTests: ArrayList<String> = ArrayList()
        var selectedDiagnosis: ArrayList<String> = ArrayList()
        var mednamelist: ArrayList<String> = ArrayList()
        var medquantitylist: ArrayList<String> = ArrayList()
        var meddosagelist: ArrayList<String> = ArrayList()
        var medUnitlist: ArrayList<String> = ArrayList()
        var medTimePeriodListlist: ArrayList<String> = ArrayList()

        fun clearAppointmentNotes() {
            doctorNotes = ""
            doctorNotes = null
            isEdited = false
            isFollowUpChecked = false
            isApiCalled = false
            isDiagnosisUpdated = false
            followUpCheckedFromExistingNotes = false
            followUpDate = ""
            followUpDate = null
            addClinicalTestItemSelected = ""
            addClinicalTestItemSelected = null
            addDiagnosisItemSelected = ""
            addDiagnosisItemSelected = null
            otherDiagnosisText = ""
            otherDiagnosisText = null
            addMedicine = ""
            addMedicine = null
            medQty = ""
            medQty = null
            medUnit = ""
            medUnit = null
            medFrequency = ""
            medFrequency = null
            medPeriod = ""
            medPeriod = null
            medDescription = ""
            medDescription = null
            medDescriptionDetails = ""
            medDescriptionDetails = null
            addClinicTestsList.clear()
            addDiagnosisList.clear()
            selectedClinicalTests.clear()
            selectedDiagnosis.clear()
            mednamelist.clear()
            medquantitylist.clear()
            meddosagelist.clear()
            medUnitlist.clear()
            medTimePeriodListlist.clear()
            AddNotesFragment.mDiagsList.clear()

        }

        public var appointmentClosed: Boolean = false

    }

    private val REQUESTED_PERMISSIONS = arrayOf(
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA
    )

    var doctorSpeciality: String? = null
    private val PERMISSION_REQ_ID = 22
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patient_appointment)
        clearAppointmentNotes()
        appointmentClosed = false
        mednamelist.clear()
        medUnitlist.clear()
        mednamelist.clear()
        meddosagelist.clear()
        medquantitylist.clear()
        try {
            if (intent.hasExtra("roomid")) {
                doctorId = intent.getStringExtra("doctorId")
                appointmentId = intent.getStringExtra("appointmentId")
                appointmentIdSend = intent.getStringExtra("appointmentId")
                patientId = intent.getStringExtra("patientId")
                description = intent.getStringExtra("description")
                Log.e("DESCRIPTION ", "IS  " + description)
                patientName = intent.getStringExtra("patientName")
                doc_notes = intent.getStringExtra("doctor_notes")
                apptstatus = intent.getStringExtra("apptstatus")
                email = intent.getStringExtra("email")
                age = intent.getStringExtra("age")
                sex = intent.getStringExtra("sex")
                roomid = intent.getStringExtra("roomid")!!
                profile_image = intent.getStringExtra("profileimage")
            }
//            else if (intent.hasExtra("FromNotification")) {
//                apptstatus = intent.getStringExtra("PatientAppointmentStatus")
//            }
            if (apptstatus.equals("closed") || apptstatus.equals("cancelled_patient")) {
            } else {
                val userData = SharedPrefs.getUserData(this@PatientAppointment)
                val username = userData!!.username
                val usertype = "DOCTOR"
                ApiUtils.getAPIService(this@PatientAppointment).manage_doctor_status(
                    SharedPrefs.getString(
                        this@PatientAppointment,
                        "sessionid"
                    )!!, username!!, usertype, appointmentId!!, patientId!!, doctorId!!, "Busy"
                )
                    .enqueue(object : Callback<ForgotPasswordResponse> {
                        override fun onFailure(call: Call<ForgotPasswordResponse>, t: Throwable) {
                            //Do nothing for now
                        }

                        override fun onResponse(
                            call: Call<ForgotPasswordResponse>,
                            response: Response<ForgotPasswordResponse>
                        ) {
                            try {
                                if (response.isSuccessful) {
                                    //toast("Your status is busy now")
                                }


                            } catch (e: Exception) {
                                toast("Something went wrong, please try again later")
                                finish()
                            }
                        }
                    })


            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_patient_reports, R.id.navigation_patient_obstaclesData
            )
        )
        // setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
//
//        if (checkSelfPermission(REQUESTED_PERMISSIONS[0], PERMISSION_REQ_ID) &&
//            checkSelfPermission(REQUESTED_PERMISSIONS[1], PERMISSION_REQ_ID) &&
//            checkSelfPermission(REQUESTED_PERMISSIONS[2], PERMISSION_REQ_ID)
//        ) {
//
//
//        }
    }

    override fun onDestroy() {
        super.onDestroy()
        clearAppointmentNotes()
    }


    fun checkSelfPermission(permission: String, requestCode: Int): Boolean {
        if (ContextCompat.checkSelfPermission(
                this,
                permission
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                REQUESTED_PERMISSIONS,
                requestCode
            )

            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQ_ID -> {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED || grantResults[1] != PackageManager.PERMISSION_GRANTED || grantResults[2] != PackageManager.PERMISSION_GRANTED) {
                    this.toast("Permission must be allowed to record audio")

                } else {
                    // if permission granted, initialize the engine
                    this.toast("Permissions granted !!")

                }
            }
        }
    }

}

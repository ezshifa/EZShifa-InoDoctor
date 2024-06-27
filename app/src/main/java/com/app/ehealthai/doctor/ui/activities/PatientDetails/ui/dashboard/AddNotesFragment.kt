package com.app.ehealthai.doctor.ui.activities.PatientDetails.ui.dashboard

import android.app.DatePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.telephony.PhoneStateListener
import android.telephony.SignalStrength
import android.telephony.TelephonyManager
import android.text.Editable
import android.text.TextWatcher
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.ehealthai.doctor.adapters.recyclerViewAdapters.IClinicalTestItemToDelete
import com.app.ehealthai.doctor.adapters.recyclerViewAdapters.MedicinelistAdapter
import com.app.ehealthai.doctor.adapters.recyclerViewAdapters.dygTestAdapter
import com.app.ehealthai.doctor.adapters.recyclerViewAdapters.dyglistAdapter
import com.app.ehealthai.doctor.models.MedicalAdapterModel
import com.app.ehealthai.doctor.models.responses.AddDiagnosisResponse
import com.app.ehealthai.doctor.models.responses.ClinicalTestsResponse
import com.app.ehealthai.doctor.models.responses.DiagnosisList
import com.app.ehealthai.doctor.models.responses.DoctorNotesResponse
import com.app.ehealthai.doctor.models.responses.ForgotPasswordResponse
import com.app.ehealthai.doctor.models.responses.GetFollowupdateResponse
import com.app.ehealthai.doctor.models.responses.KioskServicesList
import com.app.ehealthai.doctor.models.responses.MedicineList
import com.app.ehealthai.doctor.rtm.ChatManager
import com.app.ehealthai.doctor.rtm.status.MessageType
import com.app.ehealthai.doctor.ui.activities.PatientDetails.PatientAppointment
import com.app.ehealthai.doctor.utils.Global
import com.app.ehealthaidoctor.R
import com.app.ehealthaidoctor.models.responses.CancelAppointmentResponse
import com.app.ehealthaidoctor.models.responses.CloseAppointmentResponse
import com.app.ehealthaidoctor.models.responses.UploadPatientFileResponse
import com.app.ehealthaidoctor.network.ApiUtils
import com.app.ehealthaidoctor.ui.activities.HomeActivity
import com.app.ehealthaidoctor.ui.activities.VideoSessionActivity
import com.app.ehealthaidoctor.ui.dialogs.RecordAudioDialog
import com.app.ehealthaidoctor.utils.SharedPrefs
import com.example.ehealthai.MyApplication
import com.google.android.material.snackbar.Snackbar
import io.agora.rtm.ErrorInfo
import io.agora.rtm.ResultCallback
import io.agora.rtm.RtmClient
import io.agora.rtm.RtmClientListener
import io.agora.rtm.RtmMessage
import io.agora.rtm.RtmStatusCode
import kotlinx.android.synthetic.main.activity_video_session.statusc_text
import kotlinx.android.synthetic.main.fragment_addnotes.cordlayan
import kotlinx.android.synthetic.main.fragment_addnotes.ctests_drp
import kotlinx.android.synthetic.main.fragment_addnotes.diagnosis_drp
import kotlinx.android.synthetic.main.fragment_addnotes.dosagemeddrp
import kotlinx.android.synthetic.main.fragment_addnotes.dygnnosislist
import kotlinx.android.synthetic.main.fragment_addnotes.ivSignalStrength
import kotlinx.android.synthetic.main.fragment_addnotes.listView
import kotlinx.android.synthetic.main.fragment_addnotes.medicines_drp
import kotlinx.android.synthetic.main.fragment_addnotes.otherdiagnosis
import kotlinx.android.synthetic.main.fragment_addnotes.othermeded
import kotlinx.android.synthetic.main.fragment_addnotes.quantitymeddrp
import kotlinx.android.synthetic.main.fragment_addnotes.rvSelectedTests
import kotlinx.android.synthetic.main.fragment_addnotes.timeperioddrpp
import kotlinx.android.synthetic.main.fragment_addnotes.tv_internet_status
import kotlinx.android.synthetic.main.fragment_addnotes.unitdrpdwnn
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar
import kotlin.collections.set


class AddNotesFragment : Fragment() {
    companion object {
        var adapter: MedicinelistAdapter? = null
        var mDiagsList: ArrayList<String> = ArrayList()
    }

    var homeButtonClicked = false
    var selectedTestsList: ArrayList<String>? = null
    var dygadapter: dyglistAdapter? = null
    private var mRtmClient: RtmClient? = null
    private var mChatManager: ChatManager? = null
    private var isRTMLogin = false
    private var mRtmListener: MyRtmClientListener? = null
    private val mTimeSentLastMsg = HashMap<String, Long>()
    var doctor_notes_text: EditText? = null
    var followupbtn: CheckBox? = null
    var followupdate: TextView? = null
    var autoloadft = false
    lateinit var medicalAdapterModel: MedicalAdapterModel
    private val TAG = "AddNotesFragment"
    lateinit var testsnameList: ArrayList<String>
    var medicinestring: String = ""
    lateinit var medicinenameList: ArrayList<String>
    lateinit var medicineidList: ArrayList<String>
    lateinit var diagnosisnameList: ArrayList<String>
    lateinit var diagnosisidList: ArrayList<String>
    lateinit var dateDialog: DatePickerDialog
    lateinit var horizontaltests: LinearLayout
    lateinit var horizontallldiagnosis: LinearLayout
    lateinit var patientAppointmentActivity: PatientAppointment
    lateinit var username: String
    val usertype = "DOCTOR"
    var dygTestsadapter: dygTestAdapter? = null
    var pleaseSelect: String = "Please select"
    var pleaseSelectId: String = "0"
    lateinit var mainHandler: Handler
    lateinit var myRunnable: Runnable
    lateinit var telephonyManager: TelephonyManager
    lateinit var mPhoneStateListener: PhoneStateListener
    var mSignalStrength = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        patientAppointmentActivity = activity as PatientAppointment
    }

    override fun onResume() {
        super.onResume()
        try {
            Global.isfromVideoSessionActivity = false
            val userData = SharedPrefs.getUserData(patientAppointmentActivity)
            val username = userData!!.username
            val usertype = "DOCTOR"
            if (!PatientAppointment.isEdited) {
                ApiUtils.getAPIService(patientAppointmentActivity).get_doctor_documents_appt(
                    SharedPrefs.getString(patientAppointmentActivity, "sessionid")!!,
                    username!!,
                    usertype,
                    PatientAppointment.appointmentId!!
                ).enqueue(object : Callback<DoctorNotesResponse> {
                    override fun onFailure(call: Call<DoctorNotesResponse>, t: Throwable) {
                        //Do nothing
                    }

                    override fun onResponse(
                        call: Call<DoctorNotesResponse>, response: Response<DoctorNotesResponse>
                    ) {
                        //Do nothing
                        try {
                            var docnotes = response.body()!!.data!!.documents[0].doctor_notes
                            if (!docnotes.isNullOrEmpty()) {
                                doctor_notes_text!!.setText(docnotes)
                                doctor_notes_text?.requestFocus()
                                if (doctor_notes_text!!.text.length != null)
                                    doctor_notes_text?.setSelection(doctor_notes_text!!.text.length)
                            } else {
                                doctor_notes_text!!.setText(PatientAppointment.doctorNotes)
                                doctor_notes_text?.requestFocus()
                                if (doctor_notes_text!!.text.length != null)
                                    doctor_notes_text?.setSelection(doctor_notes_text!!.text.length)
                            }
                        } catch (e: java.lang.Exception) {
                        }
                    }
                })
            } else {
                doctor_notes_text?.setText(PatientAppointment.doctorNotes)
                doctor_notes_text?.requestFocus()
                if (doctor_notes_text!!.text.length != null)
                    doctor_notes_text?.setSelection(doctor_notes_text!!.text.length)
            }
            if (!PatientAppointment.isApiCalled) {
                ApiUtils.getAPIService(patientAppointmentActivity)
                    .get_followup_date(PatientAppointment.appointmentId!!)
                    .enqueue(object : Callback<GetFollowupdateResponse> {
                        override fun onFailure(call: Call<GetFollowupdateResponse>, t: Throwable) {
                            //Do nothing
                        }

                        override fun onResponse(
                            call: Call<GetFollowupdateResponse>,
                            response: Response<GetFollowupdateResponse>
                        ) {
                            PatientAppointment.isApiCalled = true
                            //Do nothing
                            try {
                                var followupdatetxt = response.body()!!.data!!.followup_date
                                if (!followupdatetxt.equals("")) {
                                    autoloadft = true
                                    followupdate!!.text = followupdatetxt
                                    followupbtn!!.isChecked = true
                                    PatientAppointment.isFollowUpChecked = true
                                    PatientAppointment.followUpDate = followupdatetxt
                                }
                            } catch (e: java.lang.Exception) {
                            }
                        }
                    })
            } else {
                if (PatientAppointment.isFollowUpChecked) {
                    followupbtn?.isChecked = true
                    followupdate!!.text = PatientAppointment.followUpDate
                } else {
                    followupbtn?.isChecked = false
                    followupdate!!.text = ""
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_addnotes, container, false)
        return root
    }


    fun setMedicineQuantityUnitDoasagePeriodAdapters() {
        try {
            val qtyArray = resources.getStringArray(R.array.quantitydrop)
            val dosageArray = resources.getStringArray(R.array.dosagedrop)
            val unitArray = resources.getStringArray(R.array.unitdrop)
            val timeArray = resources.getStringArray(R.array.timeperiod)
            val spinnerQtyArrayAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
                patientAppointmentActivity, R.layout.spinner_row, qtyArray
            )
            val spinnerDosageArrayAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
                patientAppointmentActivity, R.layout.spinner_row, dosageArray
            )
            val spinnerUnitMedArrayAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
                patientAppointmentActivity, R.layout.spinner_row, unitArray
            )
            val spinnerTimePeriodArrayAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
                patientAppointmentActivity, R.layout.spinner_row, timeArray
            )
            quantitymeddrp.setAdapter(spinnerQtyArrayAdapter)
            dosagemeddrp.setAdapter(spinnerDosageArrayAdapter)
            unitdrpdwnn.setAdapter(spinnerUnitMedArrayAdapter)
            timeperioddrpp.setAdapter(spinnerTimePeriodArrayAdapter)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (isAdded) {
            val showserviceslist: TextView = view.findViewById(R.id.showserviceslist)
            doctor_notes_text = view.findViewById(R.id.doctor_notes_text)
            val close_btn: TextView = view.findViewById(R.id.close_btn)
            val save_btn: Button = view.findViewById(R.id.save_btn)
            val cancel_appt_btn: Button = view.findViewById(R.id.cancel_appt_btn)
            horizontallldiagnosis = view.findViewById(R.id.selecteddiagnosis)
            val adddiagnosisbtn: Button = view.findViewById(R.id.adddiagnosisbtn)
            val addctestsbtn: Button = view.findViewById(R.id.addctestsbtn)
            val progress_1: ProgressBar = view.findViewById(R.id.progress_1)
            var listview: RecyclerView = view.findViewById(R.id.dygnnosislist)
            val addmedicinebtn: Button = view.findViewById(R.id.addmedicinebtn)
            val medicines_drp: Spinner = view.findViewById(R.id.medicines_drp)
            val quantitymeddrp: Spinner = view.findViewById(R.id.quantitymeddrp)
            val dosagemeddrp: Spinner = view.findViewById(R.id.dosagemeddrp)
            val listView: ListView = view.findViewById(R.id.listView)
            val diagnosis_drp: Spinner = view.findViewById(R.id.diagnosis_drp)
            val ctests_drp: Spinner = view.findViewById(R.id.ctests_drp)
            val othermeded: EditText = view.findViewById(R.id.othermeded)
            val home_an: ImageView = view.findViewById(R.id.home_an)



            registerTelephonyManager()

            Global.isfromVideoSessionActivity = false
            var testsstring: String = ""
            testsnameList = ArrayList()
            medicinenameList = ArrayList()
            medicineidList = ArrayList()
            diagnosisnameList = ArrayList()
            diagnosisidList = ArrayList()


            val userData = SharedPrefs.getUserData(patientAppointmentActivity)
//            username = SharedPrefs.toString()
            if (userData != null) {
                if (userData.username != null)
                    username = userData.username!!
            } else {
                username = ""

            }

//            horizontaltests = view.findViewById(R.id.selectedtests)
            if (PatientAppointment.apptstatus.equals("closed")
                || PatientAppointment.apptstatus.equals("cancelled_patient")
            ) {
                save_btn.visibility = View.GONE
                close_btn.visibility = View.GONE
                cancel_appt_btn.visibility = View.GONE
            }

            val layoutManager = LinearLayoutManager(
                patientAppointmentActivity,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            listview.layoutManager = layoutManager
            listView.isNestedScrollingEnabled = true

            showserviceslist.setOnClickListener {
                progress_1.visibility = View.VISIBLE
                ApiUtils.getAPIService(patientAppointmentActivity).get_kiosk_services(
                    SharedPrefs.getString(patientAppointmentActivity, "sessionid")!!,
                    username.toString(),
                    usertype
                ).enqueue(object : Callback<KioskServicesList> {
                    override fun onFailure(call: Call<KioskServicesList>, t: Throwable) {

                        progress_1.visibility = View.GONE
                        Toast.makeText(
                            patientAppointmentActivity,
                            "Something went wrong, please try again later",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    override fun onResponse(
                        call: Call<KioskServicesList>, response: Response<KioskServicesList>
                    ) {
                        progress_1.visibility = View.GONE

                        var kioskservices: ArrayList<String> = ArrayList()
                        for (kser in response.body()!!.data!!.data!!) {
                            kioskservices.add(kser.kiosk_service!!)
                        }
                        // setup the alert builder
                        val builder = AlertDialog.Builder(patientAppointmentActivity)
                        builder.setTitle("Kiosk Services")
                        val karr: Array<String> =
                            kioskservices.toArray(arrayOfNulls<String>(kioskservices.size))
                        builder.setItems(karr) { dialog, which ->
                            when (which) {

                            }
                        }

                        val dialog = builder.create()
                        dialog.show()

                    }
                })
            }

            addctestsbtn.setOnClickListener {

                var selectedtest = ctests_drp.selectedItem.toString()
                var index = testsnameList.indexOf(selectedtest)
                Log.e("INDEX ", "" + index)
                if (!ctests_drp.selectedItem.toString()
                        .equals("") && !ctests_drp.selectedItem.toString().equals(pleaseSelect)
                ) {
                    selectedtest = ctests_drp.selectedItem.toString()
                    if (!PatientAppointment.selectedClinicalTests.contains(selectedtest)) {
                        PatientAppointment.selectedClinicalTests.add(selectedtest)
                        val layoutManager = LinearLayoutManager(
                            patientAppointmentActivity,
                            LinearLayoutManager.HORIZONTAL,
                            false
                        )
                        rvSelectedTests.layoutManager = layoutManager
                        dygTestsadapter = dygTestAdapter(
                            patientAppointmentActivity,
                            PatientAppointment.selectedClinicalTests,
                            "Test",
                            patientAppointmentActivity
                        )
                        rvSelectedTests.adapter = dygTestsadapter

                        progress_1.visibility = View.VISIBLE

                        ApiUtils.getAPIService(patientAppointmentActivity).add_patient_test(
                            SharedPrefs.getString(patientAppointmentActivity, "sessionid")!!,
                            username!!.toString(),
                            usertype,
                            selectedtest,
                            PatientAppointment.appointmentId!!
                        ).enqueue(object : Callback<AddDiagnosisResponse> {
                            override fun onFailure(call: Call<AddDiagnosisResponse>, t: Throwable) {
                                progress_1.visibility = View.GONE

                                Toast.makeText(
                                    patientAppointmentActivity,
                                    "Something went wrong, please try again later",
                                    Toast.LENGTH_LONG
                                ).show()
                            }

                            override fun onResponse(
                                call: Call<AddDiagnosisResponse>,
                                response: Response<AddDiagnosisResponse>
                            ) {
                                PatientAppointment.addClinicTestsList.add(selectedtest)
                                progress_1.visibility = View.GONE
                                Toast.makeText(
                                    patientAppointmentActivity,
                                    "Test Added Successfully !",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        })
                    } else {
                        toast("Test Already Added")
                    }
                } else {
                    toast("Please select test")
                }
            }

            adddiagnosisbtn.setOnClickListener {
                try {
                    var selecteddiagnosis = diagnosis_drp.selectedItem.toString()
                    if (!diagnosis_drp.selectedItem.toString().equals("")
                        && !diagnosis_drp.selectedItem.toString().equals(pleaseSelect)
                    ) {
                        if (mDiagsList.size > 0) {
                            if (!mDiagsList.contains(selecteddiagnosis)) {
//                            list.add(selecteddiagnosis)
                                var index = diagnosisnameList.indexOf(selecteddiagnosis)
                                var selectedid = diagnosisidList[index].toString()
                                Log.e("SELECTED ID ", selectedid)
                                if (!diagnosis_drp.selectedItem.toString().equals("")) {
                                    if (diagnosis_drp.selectedItem.toString().equals("Others")) {
                                        if (otherdiagnosis.text.toString().trim().equals("") ||
                                            otherdiagnosis.text.toString().trim().isNullOrEmpty()
                                        ) {
                                            otherdiagnosis.error = "Diagnosis can't be empty"
                                            return@setOnClickListener
                                        } else {
                                            selecteddiagnosis =
                                                otherdiagnosis.text.toString().trim()
                                            if (diagnosisnameList.size > 0 && !diagnosisnameList.contains(
                                                    selecteddiagnosis
                                                )
                                            ) {
                                                if (!containsCaseInsensitive(
                                                        selecteddiagnosis,
                                                        diagnosisnameList
                                                    )
                                                ) {
                                                    if (!mDiagsList.contains(selecteddiagnosis)) {
                                                        if (!containsCaseInsensitive(
                                                                selecteddiagnosis,
                                                                mDiagsList
                                                            )
                                                        ) {
                                                            mDiagsList.add(selecteddiagnosis)
                                                        } else {
                                                            toast("Diagnosis Already Added")
                                                            return@setOnClickListener

                                                        }
                                                    } else {
                                                        toast("Diagnosis Already Added")
                                                        return@setOnClickListener
                                                    }
                                                } else {
                                                    toast("Diagnosis is already available in list")
                                                    return@setOnClickListener
                                                }
                                            } else {
                                                toast("Diagnosis is already available in list")
                                                return@setOnClickListener
                                            }
                                        }
                                        selectedid = ""
                                    } else {
                                        selecteddiagnosis = diagnosis_drp.selectedItem.toString()
                                        mDiagsList.add(selecteddiagnosis)
                                    }
                                    dygadapter = dyglistAdapter(
                                        patientAppointmentActivity,
                                        mDiagsList,
                                        "Diagnosis",
                                        patientAppointmentActivity,
                                        object : IClinicalTestItemToDelete {
                                            override fun findDiagnosisIdAndDeleteItem(
                                                diagnosisName: String,
                                                itemPosition: Int
                                            ) {
                                                if (!diagnosis_drp.selectedItem.toString()
                                                        .equals("")
                                                ) {
                                                    val diagId = findDiagnosisId(diagnosisName)
                                                    if (diagId != "empty") {
                                                        dygadapter?.callingDeleteApi(
                                                            itemPosition,
                                                            diagId, "", diagnosisName
                                                        )
                                                    } else {
                                                        dygadapter?.callingDeleteApi(
                                                            itemPosition,
                                                            "", diagnosisName, diagnosisName
                                                        )
                                                    }
                                                }
                                            }

                                        })
                                    dygnnosislist.adapter = dygadapter
                                }
                                Global.username = username.toString()
                                Global.usertype = usertype
                                Global.selectedid = selectedid
                                Global.appointmentIdd = PatientAppointment.appointmentId
                                Global.patientIdd = PatientAppointment.patientId
                                progress_1.visibility = View.VISIBLE
                                ApiUtils.getAPIService(patientAppointmentActivity)
                                    .add_patient_diagnosis_with_Other_Diagnosis(
                                        SharedPrefs.getString(
                                            patientAppointmentActivity,
                                            "sessionid"
                                        )!!, username.toString(),
                                        usertype,
                                        selectedid,
                                        selecteddiagnosis,
                                        PatientAppointment.appointmentId!!,
                                        PatientAppointment.patientId!!
                                    ).enqueue(object : Callback<AddDiagnosisResponse> {
                                        override fun onFailure(
                                            call: Call<AddDiagnosisResponse>,
                                            t: Throwable
                                        ) {
                                            progress_1.visibility = View.GONE

                                            Toast.makeText(
                                                patientAppointmentActivity,
                                                "Something went wrong, please try again later",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }

                                        override fun onResponse(
                                            call: Call<AddDiagnosisResponse>,
                                            response: Response<AddDiagnosisResponse>
                                        ) {
                                            PatientAppointment.addDiagnosisList.add(
                                                selecteddiagnosis
                                            )
                                            PatientAppointment.isDiagnosisUpdated = true

                                            progress_1.visibility = View.GONE
                                            Toast.makeText(
                                                patientAppointmentActivity,
                                                "Diagnosis Added Successfully !",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    })
                            } else {
                                toast("Diagnosis Already Added")
                            }
                        } else {
//                        list.add(selecteddiagnosis)
                            var index = diagnosisnameList.indexOf(selecteddiagnosis)
                            Log.i("listasize", mDiagsList.size.toString())
                            var selectedid = diagnosisidList[index].toString()
                            Log.e("SELECTED ID ", selectedid)
                            if (!diagnosis_drp.selectedItem.toString().equals("")) {

                                if (diagnosis_drp.selectedItem.toString().equals("Others")) {
                                    if (otherdiagnosis.text.toString().trim()
                                            .equals("") || otherdiagnosis.text.toString().trim()
                                            .isNullOrEmpty()
                                    ) {
                                        otherdiagnosis.error = "Diagnosis can't be empty"
                                        return@setOnClickListener
                                    } else {
                                        selecteddiagnosis = otherdiagnosis.text.toString().trim()
                                        if (diagnosisnameList.size > 0 && !diagnosisnameList.contains(
                                                selecteddiagnosis
                                            )
                                        ) {
                                            if (!containsCaseInsensitive(
                                                    selecteddiagnosis,
                                                    diagnosisnameList
                                                )
                                            ) {
                                                if (!mDiagsList.contains(selecteddiagnosis)) {
                                                    if (!containsCaseInsensitive(
                                                            selecteddiagnosis,
                                                            mDiagsList
                                                        )
                                                    ) {
                                                        mDiagsList.add(selecteddiagnosis)

                                                    } else {
                                                        toast("Diagnosis Already Added")
                                                        return@setOnClickListener

                                                    }
                                                } else {
                                                    toast("Diagnosis Already Added")
                                                    return@setOnClickListener
                                                }
                                            } else {
                                                toast("Diagnosis is already available in list")
                                                return@setOnClickListener
                                            }

                                        } else {
                                            toast("Diagnosis is already available in list")
                                            return@setOnClickListener
                                        }

                                    }
                                    selectedid = ""
                                } else {
                                    selecteddiagnosis = diagnosis_drp.selectedItem.toString()
                                    mDiagsList.add(selecteddiagnosis)
                                }
                                dygadapter = dyglistAdapter(
                                    patientAppointmentActivity,
                                    mDiagsList,
                                    "Diagnosis",
                                    patientAppointmentActivity,
                                    object : IClinicalTestItemToDelete {
                                        override fun findDiagnosisIdAndDeleteItem(
                                            diagnosisName: String,
                                            itemPosition: Int
                                        ) {
                                            if (!diagnosis_drp.selectedItem.toString().equals("")) {
                                                val diagId = findDiagnosisId(diagnosisName)
                                                if (diagId != "empty") {
                                                    dygadapter?.callingDeleteApi(
                                                        itemPosition,
                                                        diagId, "", diagnosisName
                                                    )
                                                } else {
                                                    dygadapter?.callingDeleteApi(
                                                        itemPosition,
                                                        "", diagnosisName, diagnosisName
                                                    )
                                                }
                                            }
                                        }

                                    })
                                dygnnosislist.adapter = dygadapter
                            }
                            Global.username = username.toString()
                            Global.usertype = usertype
                            Global.selectedid = selectedid
                            Global.appointmentIdd = PatientAppointment.appointmentId
                            Global.patientIdd = PatientAppointment.patientId
                            progress_1.visibility = View.VISIBLE
                            ApiUtils.getAPIService(patientAppointmentActivity)
                                .add_patient_diagnosis_with_Other_Diagnosis(
                                    SharedPrefs.getString(
                                        patientAppointmentActivity,
                                        "sessionid"
                                    )!!,
                                    username.toString(),
                                    usertype,
                                    selectedid,
                                    selecteddiagnosis,
                                    PatientAppointment.appointmentId!!,
                                    PatientAppointment.patientId!!
                                ).enqueue(object : Callback<AddDiagnosisResponse> {
                                    override fun onFailure(
                                        call: Call<AddDiagnosisResponse>,
                                        t: Throwable
                                    ) {
                                        progress_1.visibility = View.GONE

                                        Toast.makeText(
                                            patientAppointmentActivity,
                                            "Something went wrong, please try again later",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }

                                    override fun onResponse(
                                        call: Call<AddDiagnosisResponse>,
                                        response: Response<AddDiagnosisResponse>
                                    ) {
                                        PatientAppointment.addDiagnosisList.add(selecteddiagnosis)
                                        PatientAppointment.isDiagnosisUpdated = true
                                        progress_1.visibility = View.GONE
                                        Toast.makeText(
                                            patientAppointmentActivity,
                                            "Diagnosis Added Successfully !",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                })
                        }
                    } else {
                        toast("Please select diagnosis")
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            // initialize an array adapter
            adapter = MedicinelistAdapter(
                requireActivity(),
                PatientAppointment.mednamelist,
                PatientAppointment.medquantitylist,
                PatientAppointment.meddosagelist,
                PatientAppointment.medTimePeriodListlist,
                PatientAppointment.medUnitlist
            )
            listView.adapter = adapter

            doctor_notes_text!!.addTextChangedListener(doctorAddNoteTextWatcher)
            doctor_notes_text!!.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun afterTextChanged(p0: Editable?) {
                    PatientAppointment.isEdited = true
                    PatientAppointment.doctorNotes = doctor_notes_text?.text.toString()
                }

            })
            doctor_notes_text!!.movementMethod = ScrollingMovementMethod()
//            doctor_notes_text!!.setText(PatientAppointment.doc_notes)// = doc_notes.toString()
            doctor_notes_text!!.setOnTouchListener(View.OnTouchListener { view, event ->
                if (view.id == R.id.doctor_notes_text) {
                    view.parent.requestDisallowInterceptTouchEvent(true)
                    when (event.action and MotionEvent.ACTION_MASK) {
                        MotionEvent.ACTION_UP -> view.parent.requestDisallowInterceptTouchEvent(
                            false
                        )
                    }
                }
                false
            })
            home_an.setOnClickListener {
                homeButtonClicked = true
                PatientAppointment.clearAppointmentNotes()
                val intent = Intent(patientAppointmentActivity, HomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
            }
            ApiUtils.getAPIService(patientAppointmentActivity).get_clinical_test(
                SharedPrefs.getString(patientAppointmentActivity, "sessionid")!!,
                username!!.toString(),
                usertype
            ).enqueue(object : Callback<ClinicalTestsResponse> {
                override fun onFailure(call: Call<ClinicalTestsResponse>, t: Throwable) {
                    Toast.makeText(
                        patientAppointmentActivity,
                        "Something went wrong, please try again later",
                        Toast.LENGTH_LONG
                    ).show()
                }

                override fun onResponse(
                    call: Call<ClinicalTestsResponse>, response: Response<ClinicalTestsResponse>
                ) {
                    try {
                        if (response.isSuccessful && response.body() != null && response.body()!!.statuscode == 200) {
                            try {

                                var tests: ArrayList<String> = ArrayList()
                                var testsAdapter: ArrayAdapter<String>?
                                testsnameList.clear()

                                tests.add(pleaseSelect)
                                testsnameList.add(pleaseSelect)

                                for (test in response.body()!!.data!!.ctests) {
                                    tests.add(test.test_name)
                                    testsnameList.add(test.test_name)
                                    //testsidList.add(test.id.toString())
                                }

                                testsAdapter = ArrayAdapter(
                                    patientAppointmentActivity,
                                    android.R.layout.simple_spinner_item,
                                    tests
                                )
                                testsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                                ctests_drp.adapter = testsAdapter
                                addctestsbtn.visibility = View.VISIBLE
                                ctests_drp.onItemSelectedListener =
                                    object : AdapterView.OnItemSelectedListener {
                                        override fun onItemSelected(
                                            parent: AdapterView<*>,
                                            view: View,
                                            position: Int,
                                            id: Long
                                        ) {

                                        }

                                        override fun onNothingSelected(parent: AdapterView<*>) {
                                            // write code to perform some action
                                        }
                                    }
                            } catch (e: java.lang.Exception) {
                            }
                        } else {
                            Toast.makeText(
                                patientAppointmentActivity,
                                "Error: Tests list failed to load",
                                Toast.LENGTH_LONG
                            ).show()


                        }
                    } catch (e: Exception) {
                        Toast.makeText(
                            patientAppointmentActivity,
                            "Clinical Tests list failed to load",
                            Toast.LENGTH_LONG
                        ).show()


                    }
                }
            })
            ApiUtils.getAPIService(patientAppointmentActivity).get_medicine_list(
                SharedPrefs.getString(patientAppointmentActivity, "sessionid")!!,
                username.toString(),
                usertype
            ).enqueue(object : Callback<MedicineList> {
                override fun onFailure(call: Call<MedicineList>, t: Throwable) {
                    Toast.makeText(
                        patientAppointmentActivity,
                        "Something went wrong, please try again later",
                        Toast.LENGTH_LONG
                    ).show()
                }

                override fun onResponse(
                    call: Call<MedicineList>, response: Response<MedicineList>
                ) {
                    try {
                        if (response.isSuccessful && response.body() != null && response.body()!!.statuscode == 200) {
                            try {
                                addmedicinebtn.visibility = View.VISIBLE
                                addmedicinebtn.setOnClickListener {
                                    var medicine = medicines_drp.selectedItem.toString()
                                    if (PatientAppointment.mednamelist.contains(medicine)) {
                                        Toast.makeText(
                                            context,
                                            medicine + " is already added",
                                            Toast.LENGTH_LONG
                                        ).show()
                                        return@setOnClickListener
                                    }
                                    var quantity = quantitymeddrp.selectedItem.toString()
                                    var dosage = dosagemeddrp.selectedItem.toString()
                                    var unit = unitdrpdwnn.selectedItem.toString()
                                    var timeperiod = timeperioddrpp.selectedItem.toString()
                                    if (medicine.equals("Other Medicine")) {
                                        if (othermeded.text.toString().equals("")) {
                                            othermeded.error = "Medicine can't be empty"
                                        } else {
                                            medicine = othermeded.text.toString()
                                            PatientAppointment.mednamelist.add(medicine)
                                            PatientAppointment.medquantitylist.add(quantity)
                                            PatientAppointment.meddosagelist?.add(dosage)
                                            PatientAppointment.medUnitlist?.add(unit)
                                            PatientAppointment.medTimePeriodListlist?.add(timeperiod)
                                            adapter!!.notifyDataSetChanged()
                                            doctor_notes_text!!.setText(doctor_notes_text!!.text.toString() + "\n" + medicine + " " + quantity + " " + unit + "," + dosage + "," + timeperiod)
                                            Log.e("Medicine String ", medicinestring)
                                        }
                                    } else {
                                        PatientAppointment.mednamelist?.add(medicine)
                                        PatientAppointment.medquantitylist?.add(quantity)
                                        PatientAppointment.meddosagelist?.add(dosage)
                                        PatientAppointment.medUnitlist?.add(unit)
                                        PatientAppointment.medTimePeriodListlist?.add(timeperiod)
                                        adapter!!.notifyDataSetChanged()
                                        doctor_notes_text!!.setText(doctor_notes_text!!.text.toString() + "\n" + medicine + " " + quantity + " " + unit + "," + dosage + "," + timeperiod)
                                        Log.e("Medicine String ", medicinestring)
                                    }
                                }
                                var medicines: ArrayList<String> = ArrayList()
                                var medicinesAdapter: ArrayAdapter<String>? = null
                                medicinenameList.clear()
                                medicineidList.clear()
                                medicines.add("Other Medicine")
                                medicinenameList.add("Other Medicine")
                                medicineidList.add("0")
                                for (medicine in response.body()!!.data!!.list!!) {
                                    medicines.add(medicine.Brand_Name)
                                    medicinenameList.add(medicine.Brand_Name)
                                    medicineidList.add(medicine.Brand_ID.toString())
                                }

                                medicinesAdapter = ArrayAdapter(
                                    patientAppointmentActivity,
                                    android.R.layout.simple_spinner_item,
                                    medicines
                                )
                                medicinesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                                medicines_drp.adapter = medicinesAdapter
                                addmedicinebtn.visibility = View.VISIBLE
                                medicines_drp.onItemSelectedListener =
                                    object : AdapterView.OnItemSelectedListener {
                                        override fun onItemSelected(
                                            parent: AdapterView<*>,
                                            view: View,
                                            position: Int,
                                            id: Long
                                        ) {
                                            if (medicines[position].equals("Other Medicine")) {
                                                othermeded.visibility = View.VISIBLE
                                            } else {
                                                othermeded.visibility = View.GONE
                                            }
                                        }

                                        override fun onNothingSelected(parent: AdapterView<*>) {
                                            // write code to perform some action
                                        }
                                    }
                            } catch (e: java.lang.Exception) {
                            }
                            setMedicineQuantityUnitDoasagePeriodAdapters()
                            setAppointmentDetails()
                        } else {
                            Toast.makeText(
                                patientAppointmentActivity,
                                "Error: Medicine list failed to load",
                                Toast.LENGTH_LONG
                            ).show()

                            try {
                                addmedicinebtn.visibility = View.GONE
                                othermeded.visibility = View.GONE
                            } catch (e: java.lang.Exception) {
                            }
                        }
                    } catch (e: Exception) {
                        Toast.makeText(
                            patientAppointmentActivity,
                            "Error: Medicine list failed to load",
                            Toast.LENGTH_LONG
                        ).show()
                        try {
                            addmedicinebtn.visibility = View.GONE
                            othermeded.visibility = View.GONE
                        } catch (e: java.lang.Exception) {
                        }

                    }
                }
            })

            ApiUtils.getAPIService(patientAppointmentActivity).get_diagnosis_list(
                SharedPrefs.getString(patientAppointmentActivity, "sessionid")!!,
                username.toString(),
                usertype
            ).enqueue(object : Callback<DiagnosisList> {
                override fun onFailure(call: Call<DiagnosisList>, t: Throwable) {
                    Toast.makeText(
                        patientAppointmentActivity,
                        "Something went wrong",
                        Toast.LENGTH_LONG
                    )
                        .show()
                }

                override fun onResponse(
                    call: Call<DiagnosisList>, response: Response<DiagnosisList>
                ) {
                    try {
                        if (response.isSuccessful && response.body() != null && response.body()!!.statuscode == 200) {
                            try {

                                var diagnosis: ArrayList<String> = ArrayList()
                                var diagnosisAdapter: ArrayAdapter<String>? = null
//                                medicinenameList.clear()
//                                medicineidList.clear()


                                diagnosis.add(pleaseSelect)
                                diagnosisnameList.add(pleaseSelect)
                                diagnosisidList.add(pleaseSelectId)

                                for (diag in response.body()!!.data!!.data!!) {
                                    diagnosis.add(diag.diagnosis_name!!)
                                    diagnosisnameList.add(diag.diagnosis_name)
                                    diagnosisidList.add(diag.id.toString())
                                }

                                diagnosisAdapter = ArrayAdapter(
                                    patientAppointmentActivity,
                                    android.R.layout.simple_spinner_item,
                                    diagnosis
                                )
                                diagnosisAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                                diagnosis_drp.adapter = diagnosisAdapter
                                adddiagnosisbtn.visibility = View.VISIBLE
                                diagnosis_drp.onItemSelectedListener =
                                    object : AdapterView.OnItemSelectedListener {
                                        override fun onItemSelected(
                                            parent: AdapterView<*>,
                                            view: View,
                                            position: Int,
                                            id: Long
                                        ) {
                                            if (diagnosis[position].equals("Others")) {
                                                otherdiagnosis.visibility = View.VISIBLE
                                            } else {
                                                otherdiagnosis.visibility = View.GONE
                                            }
                                        }

                                        override fun onNothingSelected(parent: AdapterView<*>) {
                                            // write code to perform some action
                                        }
                                    }
                            } catch (e: java.lang.Exception) {
                            }
                        } else {
                            Toast.makeText(
                                patientAppointmentActivity,
                                "Error: Diagnosis list failed to load",
                                Toast.LENGTH_LONG
                            ).show()

                            try {

                            } catch (e: java.lang.Exception) {
                            }
                        }
                    } catch (e: Exception) {
                        Toast.makeText(
                            patientAppointmentActivity,
                            "Error: Diagnosis list failed to load",
                            Toast.LENGTH_LONG
                        ).show()

                    }
                }
            })

            addmedicinebtn.setOnClickListener {
                try {
                    var medicine = medicines_drp.selectedItem.toString()
                    var quantity = quantitymeddrp.selectedItem.toString()
                    var dosage = dosagemeddrp.selectedItem.toString()
                    var unit = unitdrpdwnn.selectedItem.toString()
                    var timeperiod = timeperioddrpp.selectedItem.toString()
                    if (medicine.equals("Other Medicine")) {
                        if (othermeded.text.toString().equals("")) {
                            othermeded.error = "Medicine can't be empty"
                        } else {
                            medicine = othermeded.text.toString()
                            PatientAppointment.mednamelist?.add(medicine)
                            PatientAppointment.medquantitylist?.add(quantity)
                            PatientAppointment.meddosagelist?.add(dosage)
                            PatientAppointment.medUnitlist?.add(unit)
                            PatientAppointment.medTimePeriodListlist?.add(timeperiod)
                            adapter!!.notifyDataSetChanged()
                            doctor_notes_text!!.setText(doctor_notes_text!!.text.toString() + "\n" + medicine + " " + quantity + " " + unit + "," + dosage + "," + timeperiod)
                            Log.e("Medicine String ", medicinestring)
                        }
                    } else {
                        PatientAppointment.mednamelist?.add(medicine)
                        PatientAppointment.medquantitylist?.add(quantity)
                        PatientAppointment.meddosagelist?.add(dosage)
                        PatientAppointment.medUnitlist?.add(unit)
                        PatientAppointment.medTimePeriodListlist?.add(timeperiod)
                        adapter!!.notifyDataSetChanged()
                        doctor_notes_text!!.setText(doctor_notes_text!!.text.toString() + "\n" + medicine + " " + quantity + " " + unit + "," + dosage + "," + timeperiod)
                        adapter!!.notifyDataSetChanged()
                        Log.e("Medicine String ", medicinestring)
                    }
                } catch (e: Exception) {
                }
            }
            cancel_appt_btn.setOnClickListener {

                AlertDialog.Builder(patientAppointmentActivity)
                    .setTitle(this.resources.getString(R.string.cancel_confirm_dialog_title))
                    .setMessage(this.resources.getString(R.string.cancel_confirm_dialog_message))
                    .setPositiveButton(this.resources.getString(R.string.cancel_confirm_dialog_confirm),
                        object : DialogInterface.OnClickListener {
                            override fun onClick(dialog: DialogInterface?, which: Int) {
                                try {
                                    val userData =
                                        SharedPrefs.getUserData(patientAppointmentActivity)
                                    val username = userData!!.username
                                    val usertype = "DOCTOR"
                                    progress_1.visibility = View.VISIBLE
                                    ApiUtils.getAPIService(patientAppointmentActivity)
                                        .cancelAppointmentRequest(
                                            PatientAppointment.appointmentId!!,
                                            PatientAppointment.doctorId!!,
                                            SharedPrefs.getString(
                                                patientAppointmentActivity,
                                                "sessionid"
                                            )!!,
                                            username!!,
                                            usertype
                                        ).enqueue(object : Callback<CancelAppointmentResponse> {
                                            override fun onFailure(
                                                call: Call<CancelAppointmentResponse>, t: Throwable
                                            ) {
                                                progress_1.visibility = View.GONE
                                                Toast.makeText(
                                                    patientAppointmentActivity,
                                                    "Something went wrong, please try again later",
                                                    Toast.LENGTH_LONG
                                                ).show()
                                            }

                                            override fun onResponse(
                                                call: Call<CancelAppointmentResponse>,
                                                response: Response<CancelAppointmentResponse>
                                            ) {
                                                progress_1.visibility = View.GONE
                                                Toast.makeText(
                                                    patientAppointmentActivity,
                                                    "Appointment cancelled !",
                                                    Toast.LENGTH_LONG
                                                ).show()

                                                requireActivity().finish()
                                                startActivity(
                                                    Intent(
                                                        activity,
                                                        VideoSessionActivity::class.java
                                                    ).putExtra(
                                                        "appointmentCancelledFromAddNotes",
                                                        true
                                                    )
                                                )
                                            }
                                        })
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        })
                    .setNegativeButton(this.resources.getString(R.string.cancel_confirm_dialog_dismiss),
                        object : DialogInterface.OnClickListener {
                            override fun onClick(dialog: DialogInterface?, which: Int) {
                                dialog?.dismiss()
                            }
                        }).show()
            }
            save_btn.setOnClickListener {
                medicinestring = ""
                try {
                    for (count in PatientAppointment.mednamelist.indices) {
                        if (medicinenameList.contains(PatientAppointment.mednamelist!![count])) {
                            var medpos =
                                medicinenameList.indexOf(PatientAppointment.mednamelist!![count])
                            var brandid = medicineidList.get(medpos)
                            if (medicinestring.equals("")) {
                                medicinestring =
                                    brandid + "@#@" + PatientAppointment.appointmentId + "@#@" + PatientAppointment.meddosagelist!![count] + "@#@" + PatientAppointment.medquantitylist!![count] + "@#@" + PatientAppointment.medUnitlist!![count] + "@#@" + PatientAppointment.medTimePeriodListlist!![count]
                            } else {
                                medicinestring =
                                    medicinestring + "#@#" + brandid + "@#@" + PatientAppointment.appointmentId + "@#@" + PatientAppointment.meddosagelist!![count] + "@#@" + PatientAppointment.medquantitylist!![count] + "@#@" + PatientAppointment.medUnitlist!![count] + "@#@" + PatientAppointment.medTimePeriodListlist!![count]
                            }
                        } else {
                            //TODO RECHECK
//                            val brandid = "0"
//                            if (medicinestring.equals("")) {
//                                medicinestring =
//                                    brandid + "@#@" + PatientAppointment.appointmentId + "@#@" + PatientAppointment.meddosagelist!![count] + "@#@" + PatientAppointment.medquantitylist!![count] + "@#@" + PatientAppointment.medUnitlist!![count] + "@#@" + PatientAppointment.medTimePeriodListlist!![count]
//                            } else {
//                                medicinestring =
//                                    medicinestring + "#@#" + brandid + "@#@" + PatientAppointment.appointmentId + "@#@" + PatientAppointment.meddosagelist!![count] + "@#@" + PatientAppointment.medquantitylist!![count] + "@#@" + PatientAppointment.medUnitlist!![count] + "@#@" + PatientAppointment.medTimePeriodListlist!![count]
//                            }
                        }

                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    medicinestring = ""
                }


                Log.e("Medicine String ", medicinestring)
                var medicine = medicinestring.toString()

                progress_1.visibility = View.VISIBLE
                if (RecordAudioDialog.mAudioFile != null) {
                    val requestFile = RequestBody.create(
                        "multipart/form-data".toMediaTypeOrNull(),
                        RecordAudioDialog.mAudioFile!!
                    )
                    val stringName = RecordAudioDialog.mAudioFile.toString().split("/")
                    val body =
                        MultipartBody.Part.createFormData(
                            "uploadfile",
                            stringName[4],
                            requestFile
                        )
                    val patientId = RequestBody.create(
                        "pid".toMediaTypeOrNull(), PatientAppointment.patientId!!
                    )
                    val doctorId = RequestBody.create(
                        "doctorid".toMediaTypeOrNull(), PatientAppointment.doctorId!!
                    )
                    val doc_type = RequestBody.create(
                        "doc_type".toMediaTypeOrNull(), RecordAudioDialog.mAudioFile!!.extension
                    )
                    val description = RequestBody.create(
                        "description".toMediaTypeOrNull(), doctor_notes_text!!.text.toString()
                    )
                    val apptId = RequestBody.create(
                        "apptid".toMediaTypeOrNull(), PatientAppointment.appointmentId!!
                    )
                    val userData = SharedPrefs.getUserData(patientAppointmentActivity)
                    val sessionid = RequestBody.create(
                        "sessionid".toMediaTypeOrNull(),
                        SharedPrefs.getString(patientAppointmentActivity, "sessionid")!!
                    )
                    val username = RequestBody.create(
                        "username".toMediaTypeOrNull(), userData!!.username.toString()
                    )
                    val usertype = RequestBody.create("usertype".toMediaTypeOrNull(), "DOCTOR")
                    val fdate = RequestBody.create(
                        "followup_date".toMediaTypeOrNull(), followupdate!!.text.toString()
                    )
                    ApiUtils.getAPIService(patientAppointmentActivity).uploadPatientFile(
                        apptId,
                        patientId,
                        doctorId,
                        doc_type,
                        body,
                        description,
                        sessionid,
                        username,
                        usertype,
                        fdate
                    ).enqueue(object : Callback<UploadPatientFileResponse> {
                        override fun onFailure(
                            call: Call<UploadPatientFileResponse>, t: Throwable
                        ) {
                            progress_1.visibility = View.GONE

                            Toast.makeText(
                                patientAppointmentActivity,
                                "Something went wrong, please try again later",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                        override fun onResponse(
                            call: Call<UploadPatientFileResponse>,
                            response: Response<UploadPatientFileResponse>
                        ) {
                            //progress_1.visibility = View.GONE
                            try {
                                if (response.isSuccessful && response.body() != null && response.body()!!.statuscode == 200) {
                                    if (medicinestring.equals("")) {
                                    } else {
                                        val userData =
                                            SharedPrefs.getUserData(patientAppointmentActivity)
                                        val sessionid = SharedPrefs.getString(
                                            patientAppointmentActivity, "sessionid"
                                        )!!
                                        val username = userData!!.username.toString()
                                        val usertype = "DOCTOR"
                                        ApiUtils.getAPIService(patientAppointmentActivity)
                                            .patient_medicine(
                                                sessionid, username, usertype, medicinestring
                                            )
                                            .enqueue(object : Callback<ForgotPasswordResponse> {
                                                override fun onFailure(
                                                    call: Call<ForgotPasswordResponse>,
                                                    t: Throwable
                                                ) {
                                                    progress_1.visibility = View.GONE
                                                    Toast.makeText(
                                                        patientAppointmentActivity,
                                                        "Something went wrong, please try again later",
                                                        Toast.LENGTH_LONG
                                                    ).show()
                                                }

                                                override fun onResponse(
                                                    call: Call<ForgotPasswordResponse>,
                                                    response: Response<ForgotPasswordResponse>
                                                ) {
                                                    progress_1.visibility = View.GONE
                                                    try {
                                                        if (response.isSuccessful) {

                                                        } else {
                                                            progress_1.visibility = View.GONE

                                                            Toast.makeText(
                                                                patientAppointmentActivity,
                                                                "Something went wrong, please try again later",
                                                                Toast.LENGTH_LONG
                                                            ).show()

                                                        }
                                                    } catch (e: Exception) {
                                                        Toast.makeText(
                                                            patientAppointmentActivity,
                                                            "Something went wrong, please try again later",
                                                            Toast.LENGTH_LONG
                                                        ).show()

                                                    }
                                                }
                                            })
                                    }
                                } else {
                                    progress_1.visibility = View.GONE

                                    Toast.makeText(
                                        patientAppointmentActivity,
                                        "Please try again with a different file",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            } catch (e: Exception) {
                                Toast.makeText(
                                    patientAppointmentActivity,
                                    "Error: Try again",
                                    Toast.LENGTH_LONG
                                ).show()

                            }
                        }
                    })

                } else {

                    val patientId = RequestBody.create(
                        "pid".toMediaTypeOrNull(), PatientAppointment.patientId!!
                    )
                    val doctorId = RequestBody.create(
                        "doctorid".toMediaTypeOrNull(), PatientAppointment.doctorId!!
                    )
                    val doc_type = RequestBody.create("doc_type".toMediaTypeOrNull(), "")

                    val description = RequestBody.create(
                        "description".toMediaTypeOrNull(), doctor_notes_text!!.text.toString()
                    )
                    val apptId = RequestBody.create(
                        "apptid".toMediaTypeOrNull(), PatientAppointment.appointmentId!!
                    )
                    val userData = SharedPrefs.getUserData(patientAppointmentActivity)
                    val sessionid = RequestBody.create(
                        "sessionid".toMediaTypeOrNull(),
                        SharedPrefs.getString(patientAppointmentActivity, "sessionid")!!
                    )
                    val username = RequestBody.create(
                        "username".toMediaTypeOrNull(), userData!!.username.toString()
                    )
                    val usertype = RequestBody.create("usertype".toMediaTypeOrNull(), "DOCTOR")
                    val fdate = RequestBody.create(
                        "followup_date".toMediaTypeOrNull(), followupdate!!.text.toString()
                    )
                    ApiUtils.getAPIService(patientAppointmentActivity).uploadPatientFile1(
                        apptId,
                        patientId,
                        doctorId,
                        doc_type,
                        description,
                        sessionid,
                        username,
                        usertype,
                        fdate
                    ).enqueue(object : Callback<UploadPatientFileResponse> {
                        override fun onFailure(
                            call: Call<UploadPatientFileResponse>, t: Throwable
                        ) {
                            toast("Patient file not uploaded please try again later")
                            progress_1.visibility = View.GONE
//                            Toast.makeText(
//                                patientAppointmentActivity,
//                                "Something went wrong, please try again later",
//                                Toast.LENGTH_LONG
//                            ).show()
                        }

                        override fun onResponse(
                            call: Call<UploadPatientFileResponse>,
                            response: Response<UploadPatientFileResponse>
                        ) {
                            progress_1.visibility = View.GONE
                            try {
                                if (response.isSuccessful && response.body() != null && response.body()!!.statuscode == 200) {
                                    if (medicinestring.equals("")) {
                                    } else {
                                        val userData =
                                            SharedPrefs.getUserData(patientAppointmentActivity)
                                        val sessionid = SharedPrefs.getString(
                                            patientAppointmentActivity,
                                            "sessionid"
                                        )!!
                                        val username = userData!!.username.toString()
                                        val usertype = "DOCTOR"
                                        ApiUtils.getAPIService(patientAppointmentActivity)
                                            .patient_medicine(
                                                sessionid,
                                                username,
                                                usertype,
                                                medicinestring
                                            )
                                            .enqueue(object : Callback<ForgotPasswordResponse> {
                                                override fun onFailure(
                                                    call: Call<ForgotPasswordResponse>,
                                                    t: Throwable
                                                ) {
                                                    progress_1.visibility = View.GONE
                                                    toast("Error while adding medicine, please try again later")
//                                                    Toast.makeText(
//                                                        patientAppointmentActivity,
//                                                        "Something went wrong, please try again later",
//                                                        Toast.LENGTH_LONG
//                                                    ).show()
                                                }

                                                override fun onResponse(
                                                    call: Call<ForgotPasswordResponse>,
                                                    response: Response<ForgotPasswordResponse>
                                                ) {
                                                    progress_1.visibility = View.GONE
                                                    try {
                                                        if (response.isSuccessful && response.body() != null && response.body()!!.statuscode == "200") {
                                                            toast("Medicine added successfully")
                                                            toast("Notes Updated")
                                                        } else {
                                                            progress_1.visibility = View.GONE
                                                            toast("Error while adding medicine, please try again later")

//                                                            Toast.makeText(
//                                                                patientAppointmentActivity,
//                                                                "Something went wrong, please try again later",
//                                                                Toast.LENGTH_LONG
//                                                            ).show()

                                                        }
                                                    } catch (e: Exception) {
                                                        toast("Error while adding medicine, please try again later")
//                                                        Toast.makeText(
//                                                            patientAppointmentActivity,
//                                                            "Something went wrong, please try again later",
//                                                            Toast.LENGTH_LONG
//                                                        ).show()
                                                    }
                                                }
                                            })
                                    }
                                } else {
                                    progress_1.visibility = View.GONE
                                    Toast.makeText(
                                        patientAppointmentActivity,
                                        "Please try again with a different file",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            } catch (e: Exception) {
                                Toast.makeText(
                                    patientAppointmentActivity,
                                    "Error: Try again",
                                    Toast.LENGTH_LONG
                                ).show()

                            }
                        }
                    })

                    progress_1.visibility = View.GONE
//                    Toast.makeText(patientAppointmentActivity, "Notes Updated", Toast.LENGTH_LONG).show()
                    sendStatusMessage(MessageType.SAVING_NOTES)
                    Handler(Looper.getMainLooper()).postDelayed(
                        { sendStopStatusMsg(MessageType.SAVING_NOTES) },
                        VideoSessionActivity.TIME_OUT
                    )

                    doctor_notes_text!!.removeTextChangedListener(doctorAddNoteTextWatcher)
                }
            }
            close_btn.setOnClickListener {
                if (doctor_notes_text?.text.toString().isNullOrEmpty()) {
                    toast("Enter Doctor Notes")
                } else if (PatientAppointment.addDiagnosisList.size <= 0 && !PatientAppointment.isDiagnosisUpdated) {
                    toast("Enter diagnosis")
                } else {
                    AlertDialog.Builder(patientAppointmentActivity)
                        .setTitle(patientAppointmentActivity.resources.getString(R.string.close_confirm_dialog_title))
                        .setMessage(this.resources.getString(R.string.close_confirm_dialog_message))
                        .setPositiveButton(this.resources.getString(R.string.cancel_confirm_dialog_confirm),
                            object : DialogInterface.OnClickListener {
                                override fun onClick(dialog: DialogInterface?, which: Int) {
                                    save_btn.performClick()
                                    val userData =
                                        SharedPrefs.getUserData(patientAppointmentActivity)
                                    val username = userData!!.username
                                    val usertype = "DOCTOR"
                                    ApiUtils.getAPIService(patientAppointmentActivity)
                                        .closeAppointmentRequest(
                                            PatientAppointment.appointmentId!!,
                                            SharedPrefs.getString(
                                                patientAppointmentActivity,
                                                "sessionid"
                                            )!!,
                                            username!!,
                                            usertype
                                        ).enqueue(object : Callback<CloseAppointmentResponse> {
                                            override fun onFailure(
                                                call: Call<CloseAppointmentResponse>,
                                                t: Throwable
                                            ) {
                                                progress_1.visibility = View.GONE
                                                Toast.makeText(
                                                    patientAppointmentActivity,
                                                    "Something went wrong, please try again later",
                                                    Toast.LENGTH_LONG
                                                ).show()
                                            }

                                            override fun onResponse(
                                                call: Call<CloseAppointmentResponse>,
                                                response: Response<CloseAppointmentResponse>
                                            ) {
                                                PatientAppointment.appointmentClosed = true
                                                PatientAppointment.clearAppointmentNotes()
                                                progress_1.visibility = View.GONE
                                                Toast.makeText(
                                                    patientAppointmentActivity,
                                                    "Appointment closed !",
                                                    Toast.LENGTH_LONG
                                                ).show()
                                                cancel_appt_btn.visibility = View.GONE
                                                AlertDialog.Builder(patientAppointmentActivity)
                                                    .setTitle(
                                                        patientAppointmentActivity.resources.getString(
                                                            R.string.success_dialog_title
                                                        )
                                                    )
                                                    .setMessage(
                                                        patientAppointmentActivity.resources.getString(
                                                            R.string.success_dialog_message_appt
                                                        )
                                                    )
                                                    .setPositiveButton(patientAppointmentActivity.resources.getString(
                                                        R.string.success_dialog_confirm
                                                    ),
                                                        object : DialogInterface.OnClickListener {
                                                            override fun onClick(
                                                                dialog: DialogInterface?,
                                                                which: Int
                                                            ) {
                                                                dialog?.dismiss()
                                                                ApiUtils.getAPIService(
                                                                    patientAppointmentActivity
                                                                ).manage_doctor_status(
                                                                    SharedPrefs.getString(
                                                                        patientAppointmentActivity,
                                                                        "sessionid"
                                                                    )!!,
                                                                    username,
                                                                    usertype,
                                                                    PatientAppointment.appointmentId!!,
                                                                    PatientAppointment.patientId!!,
                                                                    PatientAppointment.doctorId!!,
                                                                    "Available"
                                                                ).enqueue(object :
                                                                    Callback<ForgotPasswordResponse> {
                                                                    override fun onFailure(
                                                                        call: Call<ForgotPasswordResponse>,
                                                                        t: Throwable
                                                                    ) {
                                                                        //Do nothing for now
                                                                    }

                                                                    override fun onResponse(
                                                                        call: Call<ForgotPasswordResponse>,
                                                                        response: Response<ForgotPasswordResponse>
                                                                    ) {
                                                                        try {
                                                                            if (response.isSuccessful) {
                                                                                startActivity(
                                                                                    Intent(
                                                                                        activity,
                                                                                        VideoSessionActivity::class.java
                                                                                    ).putExtra(
                                                                                        "appointmentClosedFromAddNotes",
                                                                                        true
                                                                                    )
                                                                                )
                                                                            }


                                                                        } catch (e: Exception) {
                                                                            toast("Something went wrong, please try again later")
                                                                        }
                                                                    }
                                                                })
                                                            }
                                                        }).show()
                                            }
                                        })
                                }
                            })
                        .setNegativeButton(this.resources.getString(R.string.cancel_confirm_dialog_dismiss),
                            object : DialogInterface.OnClickListener {
                                override fun onClick(dialog: DialogInterface?, which: Int) {
                                    dialog?.dismiss()
                                }
                            }).show()
                }

            }
            if (!isRTMLogin && PatientAppointment.doctorId != null && PatientAppointment.patientId != null) {
                realTimeMessaging()
            }
            followupbtn = view.findViewById(R.id.followupchkbox)
            followupdate = view.findViewById(R.id.followupdate)
            val datePickerListener =
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    val dateYouChoosed =
                        year.toString() + "-" + (monthOfYear + 1) + "-" + dayOfMonth.toString()
                    followupdate!!.text = dateYouChoosed
                    PatientAppointment.followUpDate = dateYouChoosed
                }
            val c = Calendar.getInstance()
            val mYear = c[Calendar.YEAR]
            val mMonth = c[Calendar.MONTH]
            val mDay = c[Calendar.DAY_OF_MONTH]
            dateDialog = DatePickerDialog(
                patientAppointmentActivity,
                datePickerListener,
                mYear,
                mMonth,
                mDay
            )
            dateDialog.datePicker.minDate = System.currentTimeMillis() - 1000
            followupbtn!!.setOnCheckedChangeListener { _, isChecked ->
                PatientAppointment.isFollowUpChecked = isChecked
                if (isChecked) {
                    if (!autoloadft) {
                        autoloadft = false
                        dateDialog.show()
                    }
                } else {
                    followupdate!!.text = ""
                    autoloadft = false
                }
            }
            try {
                myRunnable = Runnable {
                    try {
                        manageSignals()
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }
                    mainHandler.postDelayed(myRunnable, 5000)
                }
                checkInternetFrequently()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun setAppointmentDetails() {

        try {
            val medicineQty =
                patientAppointmentActivity.resources.getStringArray(R.array.quantitydrop)
            val medicineUnit = patientAppointmentActivity.resources.getStringArray(R.array.unitdrop)
            val medicineFrequency =
                patientAppointmentActivity.resources.getStringArray(R.array.dosagedrop)
            val medicinePeriod =
                patientAppointmentActivity.resources.getStringArray(R.array.timeperiod)

//            for ((index, value) in testsnameList.withIndex()) {
//                if (value == PatientAppointment.addClinicalTestItemSelected) {
//                    ctests_drp?.setSelection(index, true)
//                }
//            }
//            for ((index, value) in diagnosisnameList.withIndex()) {
//                if (value == PatientAppointment.addDiagnosisItemSelected) {
//                    diagnosis_drp?.setSelection(index, true)
//                }
//            }
//
            for ((index, value) in medicinenameList.withIndex()) {
                if (value == PatientAppointment.addMedicine) {
                    medicines_drp?.setSelection(index, true)
                }
            }
            for ((index, value) in medicineQty.withIndex()) {
                if (value == PatientAppointment.medQty) {
                    quantitymeddrp.setSelection(index, true)
                }
            }
            for ((index, value) in medicineUnit.withIndex()) {
                if (value == PatientAppointment.medUnit) {
                    unitdrpdwnn.setSelection(index, true)
                }
            }
            for ((index, value) in medicineFrequency.withIndex()) {
                if (value == PatientAppointment.medFrequency) {
                    dosagemeddrp.setSelection(index, true)
                }
            }
            for ((index, value) in medicinePeriod.withIndex()) {
                if (value == PatientAppointment.medPeriod) {
                    timeperioddrpp.setSelection(index, true)
                }
            }
            othermeded.setText(PatientAppointment.medDescription)
            if (this::dateDialog.isInitialized && dateDialog.isShowing) {
                dateDialog.dismiss()
            }
            addTestsButtonFunctionality()
            addDiagnosisButtonFunctionality()
            addMedicineButtonFunctionality()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun addTestsButtonFunctionality() {
        rvSelectedTests.layoutManager = LinearLayoutManager(
            patientAppointmentActivity,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        dygTestsadapter = dygTestAdapter(
            patientAppointmentActivity,
            PatientAppointment.selectedClinicalTests,
            "Test",
            patientAppointmentActivity
        )
        rvSelectedTests.adapter = dygTestsadapter
    }

    private fun addDiagnosisButtonFunctionality() {

//        val addDiagnosisList = PatientAppointment.addDiagnosisList
//        for (d in addDiagnosisList) {
//            if (!d.equals("")) {
//                mDiagsList.add(d)
//            }
//        }
        dygadapter = dyglistAdapter(
            patientAppointmentActivity,
            mDiagsList,
            "Diagnosis",
            patientAppointmentActivity,
            object : IClinicalTestItemToDelete {
                override fun findDiagnosisIdAndDeleteItem(
                    diagnosisName: String,
                    itemPosition: Int
                ) {
                    var selecteddiagnosis = diagnosis_drp.selectedItem.toString()

                    if (!diagnosis_drp.selectedItem.toString().equals("")) {
                        val diagId = findDiagnosisId(diagnosisName)
                        if (diagId != "empty") {
                            dygadapter?.callingDeleteApi(
                                itemPosition,
                                diagId, "", diagnosisName
                            )
                        } else {
                            dygadapter?.callingDeleteApi(
                                itemPosition,
                                "", diagnosisName, diagnosisName
                            )
                        }
                    }

                }

            })
        dygnnosislist.adapter = dygadapter
        otherdiagnosis.setText(PatientAppointment.otherDiagnosisText)
    }

    private fun addMedicineButtonFunctionality() {
        adapter = MedicinelistAdapter(
            requireActivity(),
            PatientAppointment.mednamelist,
            PatientAppointment.medquantitylist,
            PatientAppointment.meddosagelist,
            PatientAppointment.medTimePeriodListlist,
            PatientAppointment.medUnitlist
        )
        listView.adapter = adapter
    }

    private fun saveAppointmentNotes() {
        PatientAppointment.addClinicalTestItemSelected = ctests_drp?.selectedItem.toString()
        PatientAppointment.addDiagnosisItemSelected = diagnosis_drp?.selectedItem.toString()
        PatientAppointment.otherDiagnosisText = otherdiagnosis.text.toString()
        PatientAppointment.addMedicine = medicines_drp?.selectedItem.toString()
        PatientAppointment.medQty = quantitymeddrp?.selectedItem.toString()
        PatientAppointment.medUnit = unitdrpdwnn?.selectedItem.toString()
        PatientAppointment.medFrequency = dosagemeddrp?.selectedItem.toString()
        PatientAppointment.medPeriod = timeperioddrpp?.selectedItem.toString()
        PatientAppointment.medDescription = othermeded?.text.toString()
    }


    private val doctorAddNoteTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun afterTextChanged(editable: Editable?) {
            if (editable == null) return
            val lastMessageChange =
                if (mTimeSentLastMsg.containsKey(MessageType.START_WRITING_NOTES.toString())) {
                    mTimeSentLastMsg[MessageType.START_WRITING_NOTES.toString()]!!
                } else 0L

            if (editable.isNotEmpty() && (lastMessageChange == 0L || System.currentTimeMillis() - lastMessageChange > 3)) {

                sendStatusMessage(MessageType.START_WRITING_NOTES)
                Handler(Looper.getMainLooper()).postDelayed(
                    { sendStopStatusMsg(MessageType.START_WRITING_NOTES) },
                    VideoSessionActivity.TIME_OUT
                )
            }
        }
    }

    private fun realTimeMessaging() {
        mChatManager = (activity?.application as MyApplication).getChatManager()
        mRtmListener = MyRtmClientListener()
        if (mRtmListener != null) {
            mChatManager?.registerListener(mRtmListener!!)
        }
        mRtmClient = mChatManager?.rtmClient
        doRTMLogin()
    }


    private fun doRTMLogin() {
        Log.e("RTM LOGIN DOC ID ", PatientAppointment.doctorId!!)
        mRtmClient?.login(null, PatientAppointment.doctorId, object : ResultCallback<Void?> {
            override fun onSuccess(responseInfo: Void?) {
                Log.i("", "login success")
                isRTMLogin = true

            }

            override fun onFailure(errorInfo: ErrorInfo) {
                Log.i("", "login failed: " + errorInfo.errorCode)
                isRTMLogin = false

            }
        })
    }

    override fun onStop() {
        super.onStop()
        try {
            mainHandler.removeCallbacks(myRunnable)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (!homeButtonClicked && !PatientAppointment.appointmentClosed) {
            saveAppointmentNotes()
        }
        mChatManager?.unregisterListener(mRtmListener)
    }

    private fun findDiagnosisId(name: String): String {
        var selectedid = "empty"
        try {
            if (diagnosisnameList != null && diagnosisnameList.size > 0) {
                val index = diagnosisnameList.indexOf(name)
                selectedid = diagnosisidList[index]
            }
            return selectedid
        } catch (e: Exception) {
            return selectedid
        }
    }

    private fun sendStatusMessage(messageType: MessageType) {
        try {
            val data = ByteArray(1)
            val statusMessage = mRtmClient!!.createMessage()
            statusMessage.setRawMessage(data, messageType.toString())
            mTimeSentLastMsg[messageType.toString()] = System.currentTimeMillis()
            sendPeerMessage(statusMessage)
        } catch (e: Exception) {
        }

    }

    private fun sendStopStatusMsg(type: MessageType) {

        val time =
            if (mTimeSentLastMsg.containsKey(type.toString())) mTimeSentLastMsg.getValue(type.toString()) else System.currentTimeMillis()

        if (System.currentTimeMillis() - time > 3) {
            sendStatusMessage(MessageType.STOP_STATUS)
        }
    }

    private fun sendPeerMessage(message: RtmMessage) {
        if (PatientAppointment.patientId == null) {
            showsnackbar("Patient not found")
            return
        }
        mRtmClient?.sendMessageToPeer(PatientAppointment.patientId,
            message,
            mChatManager!!.sendMessageOptions,
            object : ResultCallback<Void?> {
                override fun onSuccess(aVoid: Void?) {
                    // do nothing
                }

                override fun onFailure(errorInfo: ErrorInfo) {
                    try {
                        val errorCode = errorInfo.errorCode
                        activity?.runOnUiThread {
                            when (errorCode) {
                                RtmStatusCode.PeerMessageError.PEER_MESSAGE_ERR_TIMEOUT, RtmStatusCode.PeerMessageError.PEER_MESSAGE_ERR_FAILURE -> showsnackbar(
                                    getString(R.string.send_msg_failed)
                                )

                                RtmStatusCode.PeerMessageError.PEER_MESSAGE_ERR_PEER_UNREACHABLE -> showsnackbar(
                                    getString(R.string.peer_offline)
                                )

                                RtmStatusCode.PeerMessageError.PEER_MESSAGE_ERR_CACHED_BY_SERVER -> showsnackbar(
                                    getString(R.string.message_cached)
                                )
                            }

                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            })
    }

    private fun showsnackbar(snackbarText: String) {

        /*val snackbar=  Snackbar.make(cordlayan, snackbarText, Snackbar.LENGTH_LONG)
            .setAction(R.string.gotit) {

            }
            .setActionTextColor(resources.getColor(R.color.white))


        val view = snackbar.view
        val params = view.layoutParams as FrameLayout.LayoutParams
        params.topMargin=150
        params.gravity = Gravity.CENTER_HORIZONTAL
        snackbar.show()*/

    }

    private fun showStatus(state: MessageType?) {
        if (state != null) {
            when (state) {
                MessageType.STATUS_START_TYPING_MSG -> {
                    //statusc_text.text = "Patient typing message..."
                    showsnackbar("Patient is typing chat message...")
                }

                MessageType.ONLINE -> {
                    if (statusc_text != null)
                        statusc_text.text = "Online"
                }

                MessageType.OPENING_NOTES_SECTION -> {
                    //statusc_text.text = "Patient Opening Notes..."
                    showsnackbar("Patient has opened notes...")
                }

                MessageType.START_WRITING_NOTES -> {
                    //statusc_text.text = "Patient Writing Notes..."
                    //showsnackbar("Patient Opening Notes...")
                }

                MessageType.OPENING_MSGS -> {
                    //statusc_text.text = "Patient has opened chat messages..."
                    showsnackbar("Patient has opened chat messages...")
                }

                MessageType.SAVING_NOTES -> {
                    //statusc_text.text = "Patient Saving Notes..."
                    //showsnackbar("Patient Opening Notes...")
                }

                MessageType.MIC_ON -> {
                    //statusc_text.text = "Patient Turned On Mic"
                    showsnackbar("Patient has unmuted Mic...")
                }

                MessageType.MUTE_MIC -> {
                    // statusc_text.text = "Patient Muted Mic"
                    showsnackbar("Patient has muted Mic...")
                }

                MessageType.TURN_OFF_CAMERA -> {
                    //statusc_text.text = "Patient Turn Off Camera"
                    showsnackbar("Patient has turned off Camera...")
                }

                MessageType.TURN_ON_CAMERA -> {
                    //statusc_text.text = "Patient Turn On Camera"

                    showsnackbar("Patient has turned on Camera...")
                }

                MessageType.STOP_STATUS -> {
                    if (statusc_text != null)
                        statusc_text.text = "Online"
                }

                else -> {}
            }
        }
    }

    private fun parseIncommingMessage(message: RtmMessage, from: String) {
        activity?.runOnUiThread {
            if (message.rawMessage.size == 1 && from == PatientAppointment.patientId) {
                val type = message.text
                showStatus(MessageType.fromValue(type))
            }
        }
    }

    inner class MyRtmClientListener : RtmClientListener {
        override fun onConnectionStateChanged(state: Int, reason: Int) {

        }

        override fun onMessageReceived(message: RtmMessage, peerId: String) {
            parseIncommingMessage(message, peerId)
        }


        override fun onTokenExpired() {}
        override fun onTokenPrivilegeWillExpire() {

        }

        override fun onPeersOnlineStatusChanged(map: Map<String, Int>) {}
    }

    fun toast(message: String) {
        Toast.makeText(patientAppointmentActivity, message, Toast.LENGTH_SHORT).show()
    }

    fun containsCaseInsensitive(s: String?, l: List<String>): Boolean {
        for (string in l) {
            if (string.equals(s, ignoreCase = true)) {
                return true
            }
        }
        return false
    }


    private fun checkInternetFrequently() {
        mainHandler = Handler(Looper.getMainLooper())
        mainHandler.post(myRunnable)
    }

    var internetStatusPoorShown = false
    private fun manageSignals() {
        val internetStatus = checkInternet()
        val trimInternetStatus = internetStatus.trim()
        if (trimInternetStatus != "") {
//            toast(trimInternetStatus)
            when (trimInternetStatus) {
                "Good" -> {
                    ivSignalStrength.setImageResource(0)
                    ivSignalStrength.setImageResource(R.drawable.goodsignal)
                    internetStatusPoorShown = false
                }

                "Excellent" -> {
                    ivSignalStrength.setImageResource(0)
                    ivSignalStrength.setImageResource(R.drawable.goodsignal)
                    internetStatusPoorShown = false
                }

                "Average" -> {
                    ivSignalStrength.setImageResource(0)
                    ivSignalStrength.setImageResource(R.drawable.avg)
                    internetStatusPoorShown = false
                }

                "Poor" -> {
                    ivSignalStrength.setImageResource(0)
                    ivSignalStrength.setImageResource(R.drawable.poor)
                    if (!internetStatusPoorShown) {
                        val snackBar = Snackbar.make(
                            cordlayan,
                            "You may experience difficulty in your session due to poor internet connection.",
                            15000
                        ).setAction(R.string.gotit) {

                        }.setTextColor(resources.getColor(R.color.white))
                            .setBackgroundTint(resources.getColor(R.color.colorPrimary))
                            .setActionTextColor(resources.getColor(R.color.white))
                        val view = snackBar.view
                        val params = view.layoutParams as FrameLayout.LayoutParams
                        params.topMargin = 150
                        params.gravity = Gravity.CENTER_HORIZONTAL
                        snackBar.show()
                        internetStatusPoorShown = true
                    }

                }

                "Connected" -> {
                }
            }
        }
    }

    private fun checkInternet(): String {
        var status: String = ""
        try {
            val cm =
                patientAppointmentActivity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            if (cm != null && cm.activeNetworkInfo != null) {
                val activeNetwork: NetworkInfo? = cm.activeNetworkInfo!!
                val isConnected: Boolean = activeNetwork!!.isConnectedOrConnecting == true
                val mWifi: NetworkInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI)!!
                val mDataConnection: NetworkInfo =
                    cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)!!
                if (isConnected) {
                    var connecqual = ""
                    if (mWifi.isConnected) {
                        val wifiManager =
                            patientAppointmentActivity.getSystemService(Context.WIFI_SERVICE) as WifiManager
                        val numberOfLevels = 5
                        val wifiInfo: WifiInfo = wifiManager.getConnectionInfo()
                        val level =
                            WifiManager.calculateSignalLevel(wifiInfo.getRssi(), numberOfLevels);
//                        Log.e("Internet Connectivity Level ", "" + level)
                        if (level == 3) {
                            connecqual = "Average"
                        } else if (level == 4) {
                            connecqual = "Good"
                        } else if (level >= 5) {
                            connecqual = "Excellent"
                        } else {
                            connecqual = "Poor"
                        }
//                        status = "Internet Status: Connected" + "Quality : " + connecqual
                        status = connecqual
                        tv_internet_status.text = "Quality : $connecqual"
                    } else if (mDataConnection.isConnected) {
                        if (mSignalStrength == 0) {
                            connecqual = "Poor"
                        } else if (mSignalStrength == 2 || mSignalStrength == 3) {
                            connecqual = "Average"
                        } else if (mSignalStrength == 4) {
                            connecqual = "Good"
                        } else if (mSignalStrength >= 5) {
                            connecqual = "Excellent"
                        } else {
                            connecqual = "Poor"
                        }
                        status = connecqual
                        tv_internet_status.text = "Quality : $connecqual"

                    } else {
                        status = "Wifi Not Connected"
                        tv_internet_status.text = "Wifi Not Connected"
                    }
                } else {
                    status = "Not Connected"
                    tv_internet_status.text = "Not Connected"
                    Log.e(TAG, "checkInternet: $status")
                }
            } else {
                status = "Not Connected"
                tv_internet_status.text = "Not Connected"
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        return status
    }

    fun registerTelephonyManager() {
        telephonyManager =
            patientAppointmentActivity.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        mPhoneStateListener = object : PhoneStateListener() {
            @Deprecated("Deprecated in Java")
            override fun onSignalStrengthsChanged(signalStrength: SignalStrength) {
                super.onSignalStrengthsChanged(signalStrength)
                // Get signal strength values
                mSignalStrength = signalStrength.level
                Log.e("TAG", "onSignalStrengthsChanged: $mSignalStrength")
            }
        }

        // Register the PhoneStateListener to receive signal strength updates
        telephonyManager.listen(mPhoneStateListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS)

    }


    override fun onDestroyView() {
        super.onDestroyView()
        if (mainHandler != null) {
            mainHandler.removeCallbacks(myRunnable)
        }
        if (this::telephonyManager.isInitialized)
            if (this::mPhoneStateListener.isInitialized) {
                telephonyManager.listen(mPhoneStateListener, PhoneStateListener.LISTEN_NONE)
            }

    }
}

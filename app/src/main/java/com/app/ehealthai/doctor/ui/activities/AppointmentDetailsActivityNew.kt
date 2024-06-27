package com.app.ehealthaidoctor.ui.activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentUris
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.media.MediaRecorder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.app.ehealthai.doctor.models.responses.DoctorNotesResponse
import com.app.ehealthai.doctor.models.responses.ForgotPasswordResponse
import com.app.ehealthai.doctor.models.responses.MedicineList
import com.app.ehealthai.doctor.ui.activities.VitalsConfirmation
import com.app.ehealthai.doctor.ui.activities.VitalsHistory
import com.app.ehealthai.doctor.utils.SpeechAPI
import com.app.ehealthai.doctor.utils.VoiceRecorder
import com.app.ehealthaidoctor.R
import com.app.ehealthaidoctor.adapters.recyclerViewAdapters.UploadedFilesAppointmentDetailsRecyclerViewAdapter
import com.app.ehealthaidoctor.models.responses.CancelAppointmentResponse
import com.app.ehealthaidoctor.models.responses.CloseAppointmentResponse
import com.app.ehealthaidoctor.models.responses.GetPatientDocumentsResponse
import com.app.ehealthaidoctor.models.responses.SendNotificationResponse
import com.app.ehealthaidoctor.models.responses.UploadPatientFileResponse
import com.app.ehealthaidoctor.network.ApiUtils
import com.app.ehealthaidoctor.ui.dialogs.RecordAudioDialog
import com.app.ehealthaidoctor.ui.dialogs.ViewImageDialog
import com.app.ehealthaidoctor.utils.SharedPrefs
import com.bumptech.glide.Glide
import com.example.ehealthai.utils.showCancelDialog
import com.example.ehealthai.utils.showCloseDialog
import com.example.ehealthai.utils.showSuccessDialog
import com.example.ehealthai.utils.toast
import com.github.piasy.rxandroidaudio.AudioRecorder
import kotlinx.android.synthetic.main.activity_appointment_details_new.addmedicinebtn
import kotlinx.android.synthetic.main.activity_appointment_details_new.addvitalsbtn
import kotlinx.android.synthetic.main.activity_appointment_details_new.appointment_details_back_btn
import kotlinx.android.synthetic.main.activity_appointment_details_new.appointment_no
import kotlinx.android.synthetic.main.activity_appointment_details_new.arrowupdown
import kotlinx.android.synthetic.main.activity_appointment_details_new.bmirs
import kotlinx.android.synthetic.main.activity_appointment_details_new.bmivalue
import kotlinx.android.synthetic.main.activity_appointment_details_new.bp1
import kotlinx.android.synthetic.main.activity_appointment_details_new.bp2
import kotlinx.android.synthetic.main.activity_appointment_details_new.bpinfo
import kotlinx.android.synthetic.main.activity_appointment_details_new.breathingsh
import kotlinx.android.synthetic.main.activity_appointment_details_new.brinfo
import kotlinx.android.synthetic.main.activity_appointment_details_new.cancel_appt_btn
import kotlinx.android.synthetic.main.activity_appointment_details_new.cancel_appt_btn_d
import kotlinx.android.synthetic.main.activity_appointment_details_new.cancel_appt_btn_r
import kotlinx.android.synthetic.main.activity_appointment_details_new.close_btn
import kotlinx.android.synthetic.main.activity_appointment_details_new.close_btn_d
import kotlinx.android.synthetic.main.activity_appointment_details_new.close_btn_r
import kotlinx.android.synthetic.main.activity_appointment_details_new.delButton
import kotlinx.android.synthetic.main.activity_appointment_details_new.description_text
import kotlinx.android.synthetic.main.activity_appointment_details_new.docnotes_btn
import kotlinx.android.synthetic.main.activity_appointment_details_new.doctor_notes_layout
import kotlinx.android.synthetic.main.activity_appointment_details_new.doctor_notes_text
import kotlinx.android.synthetic.main.activity_appointment_details_new.doctor_notes_title
import kotlinx.android.synthetic.main.activity_appointment_details_new.dosagemeddrp
import kotlinx.android.synthetic.main.activity_appointment_details_new.fromb
import kotlinx.android.synthetic.main.activity_appointment_details_new.fromo
import kotlinx.android.synthetic.main.activity_appointment_details_new.fromp
import kotlinx.android.synthetic.main.activity_appointment_details_new.heightaad
import kotlinx.android.synthetic.main.activity_appointment_details_new.heightit
import kotlinx.android.synthetic.main.activity_appointment_details_new.heightsp
import kotlinx.android.synthetic.main.activity_appointment_details_new.home
import kotlinx.android.synthetic.main.activity_appointment_details_new.medicines_drp
import kotlinx.android.synthetic.main.activity_appointment_details_new.medicinesdropdowns
import kotlinx.android.synthetic.main.activity_appointment_details_new.more_layout
import kotlinx.android.synthetic.main.activity_appointment_details_new.oxinfo
import kotlinx.android.synthetic.main.activity_appointment_details_new.oxsh
import kotlinx.android.synthetic.main.activity_appointment_details_new.picButton
import kotlinx.android.synthetic.main.activity_appointment_details_new.prescription_file
import kotlinx.android.synthetic.main.activity_appointment_details_new.prescription_title
import kotlinx.android.synthetic.main.activity_appointment_details_new.primary_action
import kotlinx.android.synthetic.main.activity_appointment_details_new.prinfo
import kotlinx.android.synthetic.main.activity_appointment_details_new.progress_1
import kotlinx.android.synthetic.main.activity_appointment_details_new.prsh
import kotlinx.android.synthetic.main.activity_appointment_details_new.quantitymeddrp
import kotlinx.android.synthetic.main.activity_appointment_details_new.recButton
import kotlinx.android.synthetic.main.activity_appointment_details_new.record_button
import kotlinx.android.synthetic.main.activity_appointment_details_new.record_view
import kotlinx.android.synthetic.main.activity_appointment_details_new.rectext
import kotlinx.android.synthetic.main.activity_appointment_details_new.save_btn
import kotlinx.android.synthetic.main.activity_appointment_details_new.save_btn_d
import kotlinx.android.synthetic.main.activity_appointment_details_new.save_btn_r
import kotlinx.android.synthetic.main.activity_appointment_details_new.saved_text
import kotlinx.android.synthetic.main.activity_appointment_details_new.second_text_view2
import kotlinx.android.synthetic.main.activity_appointment_details_new.second_text_view3
import kotlinx.android.synthetic.main.activity_appointment_details_new.set
import kotlinx.android.synthetic.main.activity_appointment_details_new.slinfo
import kotlinx.android.synthetic.main.activity_appointment_details_new.speak_btn
import kotlinx.android.synthetic.main.activity_appointment_details_new.start_call_btn
import kotlinx.android.synthetic.main.activity_appointment_details_new.sugar
import kotlinx.android.synthetic.main.activity_appointment_details_new.sugarsp
import kotlinx.android.synthetic.main.activity_appointment_details_new.temparature
import kotlinx.android.synthetic.main.activity_appointment_details_new.temparatureaad
import kotlinx.android.synthetic.main.activity_appointment_details_new.tempinfo
import kotlinx.android.synthetic.main.activity_appointment_details_new.tempsp
import kotlinx.android.synthetic.main.activity_appointment_details_new.tob
import kotlinx.android.synthetic.main.activity_appointment_details_new.too
import kotlinx.android.synthetic.main.activity_appointment_details_new.top
import kotlinx.android.synthetic.main.activity_appointment_details_new.uploaded_files_btn
import kotlinx.android.synthetic.main.activity_appointment_details_new.uploaded_files_btn_d
import kotlinx.android.synthetic.main.activity_appointment_details_new.uploaded_files_btn_r
import kotlinx.android.synthetic.main.activity_appointment_details_new.uploaded_files_title
import kotlinx.android.synthetic.main.activity_appointment_details_new.uploaded_files_title_d
import kotlinx.android.synthetic.main.activity_appointment_details_new.uploaded_files_title_r
import kotlinx.android.synthetic.main.activity_appointment_details_new.vitalhistorybtn
import kotlinx.android.synthetic.main.activity_appointment_details_new.vitals_btn
import kotlinx.android.synthetic.main.activity_appointment_details_new.vitals_layout
import kotlinx.android.synthetic.main.activity_appointment_details_new.voicenotes_btn
import kotlinx.android.synthetic.main.activity_appointment_details_new.weight
import kotlinx.android.synthetic.main.activity_appointment_details_new.weightsp
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class AppointmentDetailsActivityNew : AppCompatActivity() {


    var doctorId: String? = null
    var appointmentId: String? = null
    var appointmentIdSend: String? = null
    var patientId: String? = null
    var patientName: String? = null
    var doc_notes: String? = null
    var description: String? = null
    var doctorName: String? = null
    var apptstatus: String? = null
    var email: String? = null
    var roomid: String = ""
    var profile_image: String? = null
    private val REQUESTED_PERMISSIONS = arrayOf(
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA
    )
    var doctorSpeciality: String? = null
    private val PERMISSION_REQ_ID = 22

    //  var mAudioRecorder: MediaRecorder? = null
    var mAudioRecorder: AudioRecorder? = null

    val SPEACH_RECOGNITION_REQUEST_CODE: Int = 1
    var uploadedFilesAppointmentDetailsRecyclerViewAdapter: UploadedFilesAppointmentDetailsRecyclerViewAdapter? =
        null
    val time = "00:00"
    var morevitals = false
    var medicinestring: String = ""
    var medicinenameList: ArrayList<String> = ArrayList()
    var medicineidList: ArrayList<String> = ArrayList()
    private var speechAPI: SpeechAPI? = null
    private var mVoiceRecorder: VoiceRecorder? = null
    private var stringList: List<String>? = null
    private val RECORD_REQUEST_CODE = 101
    var speakmode = false
    private val mVoiceCallback: VoiceRecorder.Callback = object : VoiceRecorder.Callback() {
        override fun onVoiceStart() {
            speechAPI?.startRecognizing(mVoiceRecorder!!.sampleRate)
        }

        override fun onVoice(data: ByteArray, size: Int) {
            speechAPI?.recognize(data, size)
        }

        override fun onVoiceEnd() {
            speechAPI?.finishRecognizing()
        }
    }
    private var adapter: ArrayAdapter<*>? = null

    /*   private val mSpeechServiceListener = SpeechAPI.Listener { text, isFinal ->
           if (isFinal) {
               mVoiceRecorder!!.dismiss()
           }
          *//* if (textMessage != null && !TextUtils.isEmpty(text)) {*//*
            runOnUiThread {
                if (isFinal) {
                    //textMessage.setText(null)
                    //stringList.add(0, text)
                    if(speakmode) {
                       var extext= doctor_notes_text.text.toString()
                    if(extext.equals(""))
                    {
                        doctor_notes_text.setText(text)
                    }
                        else {
                        doctor_notes_text.setText(extext + " " + text)
                    }
                    }
                    //adapter!!.notifyDataSetChanged()
                } else {
                    //textMessage.setText(text)
                }
            }
       // }
    }*/
    override fun onStart() {
        super.onStart()
        /* if (isGrantedPermission(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
             startVoiceRecorder()
         } else {
             makeRequest(Manifest.permission.RECORD_AUDIO)
         }
         speechAPI!!.addListener(mSpeechServiceListener)*/
    }

    override fun onStop() {
        //stopVoiceRecorder()
        // Stop Cloud Speech API
        //speechAPI!!.removeListener(mSpeechServiceListener)
        // speechAPI!!.destroy()
        // speechAPI = null
        super.onStop()
    }

    private fun isGrantedPermission(permission: String): Int {
        return ContextCompat.checkSelfPermission(this, permission)
    }

    private fun makeRequest(permission: String) {
        ActivityCompat.requestPermissions(this, arrayOf(permission), RECORD_REQUEST_CODE)
    }

    private fun startVoiceRecorder() {
        if (mVoiceRecorder != null) {
            mVoiceRecorder!!.stop()
        }
        mVoiceRecorder = VoiceRecorder(mVoiceCallback)
        mVoiceRecorder!!.start()
    }

    private fun stopVoiceRecorder() {
        if (mVoiceRecorder != null) {
            mVoiceRecorder!!.stop()
            mVoiceRecorder = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        Fabric.with(this, Crashlytics())
        setContentView(R.layout.activity_appointment_details_new)
        speechAPI = SpeechAPI(this)
        stringList = ArrayList()
        adapter = ArrayAdapter<Any?>(
            this,
            android.R.layout.simple_list_item_1, stringList!!
        )
        more_layout.setOnClickListener {
            if (!morevitals) {
                breathingsh.visibility = View.VISIBLE
                oxsh.visibility = View.VISIBLE
                prsh.visibility = View.VISIBLE
                morevitals = true
                arrowupdown.setImageResource(R.drawable.uparrow)
            } else if (morevitals) {
                breathingsh.visibility = View.GONE
                oxsh.visibility = View.GONE
                prsh.visibility = View.GONE
                morevitals = false
                arrowupdown.setImageResource(R.drawable.dropdown_icon)
            }
        }
        bpinfo.setOnClickListener {

            val builder = AlertDialog.Builder(this)
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

            val builder = AlertDialog.Builder(this)
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

            val builder = AlertDialog.Builder(this)
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
            val builder = AlertDialog.Builder(this)
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
            val builder = AlertDialog.Builder(this)
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

            val builder = AlertDialog.Builder(this)
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
        /* speakbtn.setOnClickListener {
            if(!speakmode) {
                speakmode = true
                speakbtn.setText("Listening ...")
                speakbtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.mic, 0, 0, 0);
            }
             else
            {
                speakmode = false
                speakbtn.setText("Speak")
                speakbtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.nomic, 0, 0, 0);
            }
         }*/
        home.setOnClickListener {
            val intent = Intent(applicationContext, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }


        init()
        sugarsp?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

            }

        }
//       doctor_notes_text.setScroller(Scroller(this))
//       // doctor_notes_text.maxLines =
//        doctor_notes_text.isVerticalScrollBarEnabled = true
        doctor_notes_text.movementMethod = ScrollingMovementMethod()


        if (checkSelfPermission(REQUESTED_PERMISSIONS[0], PERMISSION_REQ_ID) &&
            checkSelfPermission(REQUESTED_PERMISSIONS[1], PERMISSION_REQ_ID) &&
            checkSelfPermission(REQUESTED_PERMISSIONS[2], PERMISSION_REQ_ID)
        ) {

            initRecordView()
        }

        picButton.setOnClickListener {
            selectImageInAlbum()
        }

        recButton.setOnClickListener {

            if (start.equals("0")) {
                onStartRec()
                start = "1"
                rectext.visibility = View.VISIBLE
                saved_text.visibility = View.INVISIBLE
                recButton.setBackgroundDrawable(getDrawable(R.drawable.stop_record_ic))
            } else {
                onStopRec()
                start = "0"
                rectext.visibility = View.INVISIBLE
                recButton.setBackgroundDrawable(getDrawable(R.drawable.record_audio_ic))
            }
        }

        delButton.setOnClickListener {

            if (mAudioRecorder != null) {
                mAudioRecorder!!.stopRecord()
                RecordAudioDialog.mAudioFile = null
                start = "0"
                rectext.visibility = View.INVISIBLE
                saved_text.visibility = View.INVISIBLE
                recButton.setBackgroundDrawable(getDrawable(R.drawable.record_audio_ic))
            }

        }


    }

    @SuppressLint("Range")
    private fun imagePath(uri: Uri?, selection: String?): String {
        var path: String? = null
//        通过Uri和selection获取路径
        val cursor = contentResolver.query(uri!!, null, selection, null, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
            }
            cursor.close()
        }
        return path!!
    }

    val REQUEST_SELECT_IMAGE_IN_ALBUM = 1

    var selsetion: String? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {

            REQUEST_SELECT_IMAGE_IN_ALBUM ->
                if (resultCode == Activity.RESULT_OK) {
                    if (Build.VERSION.SDK_INT >= 19) {
                        Log.e("hello", "pic")
//                        4.4以上
                        var imagePath: String? = null
                        val uri = data!!.data
                        if (DocumentsContract.isDocumentUri(this, uri)) {
//            document类型的Uri，用document id处理
                            Log.e("hello", uri.toString())
                            val docId = DocumentsContract.getDocumentId(uri)
                            if ("com.android.providers.media.documents" == uri!!.authority) {
                                val id = docId.split(":")[1]
                                selsetion = MediaStore.Images.Media._ID + "=" + id
                                imagePath = imagePath(
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                    selsetion
                                )
                            } else if ("com.android.providers.downloads.documents" == uri!!.authority) {
                                val contentUri = ContentUris.withAppendedId(
                                    Uri.parse("content://downloads/public_downloads"),
                                    java.lang.Long.valueOf(docId)
                                )
                                imagePath = imagePath(contentUri, null)
                            }

                            /*
                                                        var path: String? = null
                            //        通过Uri和selection获取路径
                                                        val cursor = contentResolver.query(uri, null, selsetion, null, null )
                                                        if (cursor != null) {
                                                            if (cursor.moveToFirst()) {
                                                                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
                                                            }
                                                            cursor.close()
                                                        }
                            */

                            val rnds = (10000..1000000).random()
                            var file = File(imagePath)
                            val requestFile =
                                RequestBody.create(
                                    "multipart/form-data".toMediaTypeOrNull(),
                                    file
                                )
                            Log.e("Hello", requestFile.toString())
                            val stringName = "$rnds.png"
                            Log.e("Hello", stringName)
                            val body = MultipartBody.Part.createFormData(
                                "uploadfile",
                                stringName,
                                requestFile
                            )

                            // var f:FileInputStream
                            val apptId = RequestBody.create(
                                "apptid".toMediaTypeOrNull(),
                                appointmentId!!
                            )
                            val patientId = RequestBody.create(
                                "pid".toMediaTypeOrNull(),
                                patientId!!
                            )
                            val doctorId = RequestBody.create(
                                "doctorid".toMediaTypeOrNull(),
                                doctorId!!
                            )
                            val doc_type = RequestBody.create(
                                "doc_type".toMediaTypeOrNull(),
                                "doc_notes"
                            )
                            val description = RequestBody.create(
                                "description".toMediaTypeOrNull(),
                                doctor_notes_text.text.toString()
                            )
                            val fdate = RequestBody.create("followup_date".toMediaTypeOrNull(), "")
                            sendFileUploadRequest(
                                apptId,
                                patientId,
                                doctorId,
                                doc_type,
                                body,
                                description,
                                fdate
                            )


                        }// else {
//                        4.4以下
                        //      handleImageBeforeKitkat(data)
                        //    }
                    }
                }
        }

    }

    fun selectImageInAlbum() {

        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        if (intent.resolveActivity(packageManager) != null) {
            startActivityForResult(intent, REQUEST_SELECT_IMAGE_IN_ALBUM)
        }
    }

    var start: String = "0"


    fun onStartRec() {
        //   mAudioRecorder = AudioRecorder.getInstance()
        //saved_text.visibility = View.GONE
        RecordAudioDialog.mAudioFile =
            File(
                Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + File.separator + System.nanoTime() + "_recording.m4a"
            )


        mAudioRecorder!!.prepareRecord(
            MediaRecorder.AudioSource.MIC,
            MediaRecorder.OutputFormat.MPEG_4,
            MediaRecorder.AudioEncoder.AAC,
            RecordAudioDialog.mAudioFile
        )
        mAudioRecorder!!.startRecord()
    }


    fun onStopRec() {

        if (mAudioRecorder != null) {
            saved_text.visibility = View.VISIBLE
            mAudioRecorder!!.stopRecord()
//
        }
    }

    fun initRecordView() {
        record_view.setSoundEnabled(true)
        //  record_view.setLessThanSecondAllowed(false)
        // record_view.setSlideToCancelText("Slide to cancel")

        /*mAudioRecorder = MediaRecorder()

        mAudioRecorder!!.setAudioSource(MediaRecorder.AudioSource.MIC)
        mAudioRecorder!!.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        mAudioRecorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
        mAudioRecorder!!.setOutputFile(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + System.nanoTime() + "_recording")*/


        mAudioRecorder = AudioRecorder.getInstance()


//        record_view.setOnRecordListener(object : OnRecordListener {
//
//            override fun onFinish(recordTime: Long) {
//                if (mAudioRecorder != null) {
//                    saved_text.visibility = View.VISIBLE
//                    mAudioRecorder!!.stopRecord()
////                    if (RecordAudioDialog.mAudioFile != null) {
////                        val requestFile =
////                            RequestBody.create(MediaType.parse("multipart/form-data"), RecordAudioDialog.mAudioFile!!)
////                        val stringName = RecordAudioDialog.mAudioFile.toString().split("/")
////                        val body = MultipartBody.Part.createFormData("uploadfile", stringName[4], requestFile)
////
////                        val patientId = RequestBody.create(MediaType.parse("pid"), patientId!!)
////                        val doctorId = RequestBody.create(MediaType.parse("doctorid"), doctorId!!)
////                        val doc_type = RequestBody.create(MediaType.parse("doc_type"), RecordAudioDialog.mAudioFile!!.extension)
////
////                        sendFileUploadRequest(patientId, doctorId, doc_type, body)
////                    }
//                }
//
//                /*PlayConfig.file(mAudioFile) // play a local file
//                    //.res(getApplicationContext(), R.raw.audio_record_end) // or play a raw resource
//                    .looping(false) // loop or not
//                    .leftVolume(1.0F) // left volume
//                    .rightVolume(1.0F) // right volume
//                    .build()*/
//            }
//
//            override fun onLessThanSecond() {
//                if (mAudioRecorder != null) {
//                    mAudioRecorder!!.stopRecord()
//                    RecordAudioDialog.mAudioFile = null
//                }
//            }
//
//            override fun onCancel() {
//                if (mAudioRecorder != null) {
//                    mAudioRecorder!!.stopRecord()
//                    RecordAudioDialog.mAudioFile = null
//                }
//            }
//
//            override fun onStart() {
//                saved_text.visibility = View.GONE
//                RecordAudioDialog.mAudioFile =
//                    File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + System.nanoTime() + "_recording.m4a")
//
//
//                mAudioRecorder!!.prepareRecord(
//                    MediaRecorder.AudioSource.MIC,
//                    MediaRecorder.OutputFormat.MPEG_4,
//                    MediaRecorder.AudioEncoder.AAC,
//                    RecordAudioDialog.mAudioFile
//                )
//                mAudioRecorder!!.startRecord()
//            }
//
//        })

        //   record_button.setRecordView(record_view)
        // record_button.isListenForRecord = true

//        record_button.setOnRecordClickListener {
//            saved_text.visibility = View.GONE
//        }

    }

    fun init() {

        doctorId = intent.getStringExtra("doctorId")
        appointmentId = intent.getStringExtra("appointmentId")
        appointmentIdSend = intent.getStringExtra("appointmentId")
        patientId = intent.getStringExtra("patientId")
        description = intent.getStringExtra("description")
        patientName = intent.getStringExtra("patientName")
        doc_notes = intent.getStringExtra("doctor_notes")
        apptstatus = intent.getStringExtra("apptstatus")
        email = intent.getStringExtra("email")
        profile_image = intent.getStringExtra("profileimage")
        roomid = intent.getStringExtra("roomid")!!
        Log.e("Room ID ", roomid!!)
        if (apptstatus.equals("closed") || apptstatus.equals("cancelled_patient")) {
            set.visibility = View.GONE
            prescription_title.visibility = View.GONE
            save_btn.visibility = View.GONE
            close_btn.visibility = View.GONE
            save_btn_d.visibility = View.GONE
            close_btn_d.visibility = View.GONE

            save_btn_r.visibility = View.GONE
            close_btn_r.visibility = View.GONE
            cancel_appt_btn.visibility = View.GONE
            cancel_appt_btn_d.visibility = View.GONE
            cancel_appt_btn_r.visibility = View.GONE
            //cancel_btn.visibility = View.INVISIBLE
            start_call_btn.visibility = View.INVISIBLE
        }

        Log.e("appointmrntID", appointmentId!!)
        if (doc_notes == null) {

        } else {
            Log.e("doctor Notes", doc_notes!!)
        }

        appointment_no.text = intent.getStringExtra("patientName")
        second_text_view2.text = email
        //toast(profile_image!!)
        Log.e(
            "IMAGE ",
            SharedPrefs.getString(this@AppointmentDetailsActivityNew, "filepath") + profile_image
        )
        if (profile_image.equals("")) {
        } else {
            Glide.with(this).load(
                SharedPrefs.getString(
                    this@AppointmentDetailsActivityNew,
                    "filepath"
                ) + profile_image
            ).into(primary_action)
        }
        if (intent.getStringExtra("dob").equals("")) {
        } else {
            if (getAge(intent.getStringExtra("dob")!!).toString().equals("-1")) {
                second_text_view3.text = "Not Available"
            } else {
                second_text_view3.text = getAge(intent.getStringExtra("dob")!!).toString()
            }
        }
        doctor_notes_text.setText(doc_notes)// = doc_notes.toString()
        doctor_notes_text.setOnTouchListener(View.OnTouchListener { view, event ->
            if (view.id == R.id.doctor_notes_text) {
                view.parent.requestDisallowInterceptTouchEvent(true)
                when (event.action and MotionEvent.ACTION_MASK) {
                    MotionEvent.ACTION_UP -> view.parent.requestDisallowInterceptTouchEvent(false)
                }
            }
            false
        })
        val userData = SharedPrefs.getUserData(this@AppointmentDetailsActivityNew)
        doctorId = userData!!.id.toString()
        doctorName = userData.fname + " " + userData.lname
        doctorSpeciality = userData.specialty



        start_call_btn.setOnClickListener {
            val intent = Intent(this, VideoSessionActivity::class.java)
            intent.putExtra(
                "url",
                "https://ezshifa.com/emr/restapi_admin/index.php/videocall?roomid=" + roomid
            )
            startActivity(intent)
        }

        /* start_call_btn.setOnClickListener {
                  val intent = Intent(this, VideoCallActivity::class.java)
                  var chatId: String = "chat_" + patientId + "_" + doctorId
              FirebaseUtils.sendMessage(
                  doctorId!!, patientId!!,
                  "The doctor has initiated the telemedicine video chat room.",
                  "text",
                  System.currentTimeMillis().toString(),
                  patientName!!, doctorName!!,doctorId!!, doctorSpeciality!!, userData.image_name!!,
                      userData.image_name!!,
                      messageSent = {
                      sendNotification(
                          patientId!!, "Message from " + doctorName + ""
                      )
                  }
              )
                  intent.putExtra("roomId", chatId)
                  intent.putExtra("doctorId", doctorId)
                  intent.putExtra("patientId", patientId)
                  intent.putExtra("patientName", patientName)
                  startActivity(intent)
          }*/

        /*  cancel_btn.setOnClickListener {
              showCancelDialog {
                  val userData=SharedPrefs.getUserData(this)
                  val username=userData!!.username
                  val usertype="DOCTOR"
                  progress_1.visibility = View.VISIBLE
                  ApiUtils.getAPIService(this).cancelAppointmentRequest(appointmentId!!, doctorId!!,SharedPrefs.getString(this@AppointmentDetailsActivityNew, "sessionid")!!,username!!,usertype).enqueue(object : Callback<CancelAppointmentResponse> {
                      override fun onFailure(call: Call<CancelAppointmentResponse>, t: Throwable) {
                          progress_1.visibility = View.GONE
                          toast("Something went wrong")
                      }

                      override fun onResponse(call: Call<CancelAppointmentResponse>, response: Response<CancelAppointmentResponse>) {
                          progress_1.visibility = View.GONE
                          toast("Appointment cancelled !")
                          val intent = Intent(this@AppointmentDetailsActivityNew, AppointmentsActivity::class.java)
                          startActivity(intent)
                          setResult(2)
                          finish()
                      }
                  })
              }

          }*/


        appointment_details_back_btn.setOnClickListener { onBackPressed() }

        // uploaded_files_recycler_view.layoutManager = LinearLayoutManager(this@AppointmentDetailsActivityNew)

        uploaded_files_btn.setOnClickListener {
            val intent =
                Intent(this@AppointmentDetailsActivityNew, PatientDocumentsActivity::class.java)
            intent.putExtra("patientId", patientId)
            startActivity(intent)
        }
        uploaded_files_btn_d.setOnClickListener {
            val intent =
                Intent(this@AppointmentDetailsActivityNew, PatientDocumentsActivity::class.java)
            intent.putExtra("patientId", patientId)
            startActivity(intent)
        }
        uploaded_files_btn_r.setOnClickListener {
            val intent =
                Intent(this@AppointmentDetailsActivityNew, PatientDocumentsActivity::class.java)
            intent.putExtra("patientId", patientId)
            startActivity(intent)
        }

        cancel_appt_btn.setOnClickListener {
            showCancelDialog {
                val userData = SharedPrefs.getUserData(this)
                val username = userData!!.username
                val usertype = "DOCTOR"
                progress_1.visibility = View.VISIBLE
                ApiUtils.getAPIService(this).cancelAppointmentRequest(
                    appointmentId!!,
                    doctorId!!,
                    SharedPrefs.getString(this@AppointmentDetailsActivityNew, "sessionid")!!,
                    username!!,
                    usertype
                ).enqueue(object : Callback<CancelAppointmentResponse> {
                    override fun onFailure(call: Call<CancelAppointmentResponse>, t: Throwable) {
                        progress_1.visibility = View.GONE
                        toast("Something went wrong")
                    }

                    override fun onResponse(
                        call: Call<CancelAppointmentResponse>,
                        response: Response<CancelAppointmentResponse>
                    ) {
                        progress_1.visibility = View.GONE
                        toast("Appointment cancelled !")
                        val intent = Intent(
                            this@AppointmentDetailsActivityNew,
                            AppointmentsActivity::class.java
                        )
                        startActivity(intent)
                        setResult(2)
                        finish()
                    }
                })
            }
        }
        cancel_appt_btn_d.setOnClickListener {
            showCancelDialog {
                val userData = SharedPrefs.getUserData(this)
                val username = userData!!.username
                val usertype = "DOCTOR"
                progress_1.visibility = View.VISIBLE
                ApiUtils.getAPIService(this).cancelAppointmentRequest(
                    appointmentId!!,
                    doctorId!!,
                    SharedPrefs.getString(this@AppointmentDetailsActivityNew, "sessionid")!!,
                    username!!,
                    usertype
                ).enqueue(object : Callback<CancelAppointmentResponse> {
                    override fun onFailure(call: Call<CancelAppointmentResponse>, t: Throwable) {
                        progress_1.visibility = View.GONE
                        toast("Something went wrong")
                    }

                    override fun onResponse(
                        call: Call<CancelAppointmentResponse>,
                        response: Response<CancelAppointmentResponse>
                    ) {
                        progress_1.visibility = View.GONE
                        toast("Appointment cancelled !")
                        val intent = Intent(
                            this@AppointmentDetailsActivityNew,
                            AppointmentsActivity::class.java
                        )
                        startActivity(intent)
                        setResult(2)
                        finish()
                    }
                })
            }
        }
        cancel_appt_btn_r.setOnClickListener {
            showCancelDialog {
                val userData = SharedPrefs.getUserData(this)
                val username = userData!!.username
                val usertype = "DOCTOR"
                progress_1.visibility = View.VISIBLE
                ApiUtils.getAPIService(this).cancelAppointmentRequest(
                    appointmentId!!,
                    doctorId!!,
                    SharedPrefs.getString(this@AppointmentDetailsActivityNew, "sessionid")!!,
                    username!!,
                    usertype
                ).enqueue(object : Callback<CancelAppointmentResponse> {
                    override fun onFailure(call: Call<CancelAppointmentResponse>, t: Throwable) {
                        progress_1.visibility = View.GONE
                        toast("Something went wrong")
                    }

                    override fun onResponse(
                        call: Call<CancelAppointmentResponse>,
                        response: Response<CancelAppointmentResponse>
                    ) {
                        progress_1.visibility = View.GONE
                        toast("Appointment cancelled !")
                        val intent = Intent(
                            this@AppointmentDetailsActivityNew,
                            AppointmentsActivity::class.java
                        )
                        startActivity(intent)
                        setResult(2)
                        finish()
                    }
                })
            }
        }

        //  getPatientFiles(patientId!!)

        if (description != "null" && description != "") {
            description_text.text = "Description: " + description
        } else {
            description_text.text = "No description available"
        }


        speak_btn.setOnClickListener {
            speak_btn.isEnabled = false
            //   startSpeechRecognition()
            // startSpeechRecorder()
            saved_text.visibility = View.GONE
            val newDialog = RecordAudioDialog(returnAudioFile = {
                val stringName = it.split("/")
                prescription_file.text = getString(R.string.recorded_file) + " " + stringName[4]

            })
            newDialog.show(supportFragmentManager, "record Audio")
            supportFragmentManager.executePendingTransactions()
            speak_btn.isEnabled = true
        }

        close_btn.setOnClickListener {
            showCloseDialog {
                val userData = SharedPrefs.getUserData(this@AppointmentDetailsActivityNew)
                val username = userData!!.username
                val usertype = "DOCTOR"
                ApiUtils.getAPIService(this@AppointmentDetailsActivityNew).closeAppointmentRequest(
                    appointmentIdSend!!,
                    SharedPrefs.getString(this@AppointmentDetailsActivityNew, "sessionid")!!,
                    username!!,
                    usertype
                ).enqueue(object : Callback<CloseAppointmentResponse> {
                    override fun onFailure(call: Call<CloseAppointmentResponse>, t: Throwable) {
                        progress_1.visibility = View.GONE
                        toast("Something went wrong")
                    }

                    override fun onResponse(
                        call: Call<CloseAppointmentResponse>,
                        response: Response<CloseAppointmentResponse>
                    ) {
                        progress_1.visibility = View.GONE
                        toast("Appointment closed !")
                        // cancel_btn.visibility = View.INVISIBLE
                        cancel_appt_btn.visibility = View.GONE
                        cancel_appt_btn_d.visibility = View.GONE
                        cancel_appt_btn_r.visibility = View.GONE
                        start_call_btn.visibility = View.INVISIBLE
                        showSuccessDialog {
                            finish()
                        }

                    }
                })
            }
        }

        close_btn_d.setOnClickListener {
            showCloseDialog {
                val userData = SharedPrefs.getUserData(this@AppointmentDetailsActivityNew)
                val username = userData!!.username
                val usertype = "DOCTOR"
                ApiUtils.getAPIService(this@AppointmentDetailsActivityNew).closeAppointmentRequest(
                    appointmentIdSend!!,
                    SharedPrefs.getString(this@AppointmentDetailsActivityNew, "sessionid")!!,
                    username!!,
                    usertype
                ).enqueue(object : Callback<CloseAppointmentResponse> {
                    override fun onFailure(call: Call<CloseAppointmentResponse>, t: Throwable) {
                        progress_1.visibility = View.GONE
                        toast("Something went wrong")
                    }

                    override fun onResponse(
                        call: Call<CloseAppointmentResponse>,
                        response: Response<CloseAppointmentResponse>
                    ) {
                        progress_1.visibility = View.GONE
                        toast("Appointment closed !")
                        // cancel_btn.visibility = View.INVISIBLE
                        cancel_appt_btn.visibility = View.GONE
                        cancel_appt_btn_d.visibility = View.GONE
                        cancel_appt_btn_r.visibility = View.GONE
                        start_call_btn.visibility = View.INVISIBLE
                        showSuccessDialog {
                            finish()
                        }

                    }
                })
            }
        }

        close_btn_r.setOnClickListener {
            showCloseDialog {
                val userData = SharedPrefs.getUserData(this@AppointmentDetailsActivityNew)
                val username = userData!!.username
                val usertype = "DOCTOR"
                ApiUtils.getAPIService(this@AppointmentDetailsActivityNew).closeAppointmentRequest(
                    appointmentIdSend!!,
                    SharedPrefs.getString(this@AppointmentDetailsActivityNew, "sessionid")!!,
                    username!!,
                    usertype
                ).enqueue(object : Callback<CloseAppointmentResponse> {
                    override fun onFailure(call: Call<CloseAppointmentResponse>, t: Throwable) {
                        progress_1.visibility = View.GONE
                        toast("Something went wrong")
                    }

                    override fun onResponse(
                        call: Call<CloseAppointmentResponse>,
                        response: Response<CloseAppointmentResponse>
                    ) {
                        progress_1.visibility = View.GONE
                        toast("Appointment closed !")
                        // cancel_btn.visibility = View.INVISIBLE
                        cancel_appt_btn.visibility = View.GONE
                        cancel_appt_btn_d.visibility = View.GONE
                        cancel_appt_btn_r.visibility = View.GONE
                        start_call_btn.visibility = View.INVISIBLE
                        showSuccessDialog {
                            finish()
                        }

                    }
                })
            }
        }
        save_btn_d.setOnClickListener {
            progress_1.visibility = View.VISIBLE
            if (RecordAudioDialog.mAudioFile != null) {
                val requestFile =
                    RequestBody.create(
                        "multipart/form-data".toMediaTypeOrNull(),
                        RecordAudioDialog.mAudioFile!!
                    )
                val stringName = RecordAudioDialog.mAudioFile.toString().split("/")
                val body =
                    MultipartBody.Part.createFormData("uploadfile", stringName[4], requestFile)
                val patientId = RequestBody.create("pid".toMediaTypeOrNull(), patientId!!)
                val doctorId = RequestBody.create("doctorid".toMediaTypeOrNull(), doctorId!!)
                val doc_type = RequestBody.create(
                    "doc_type".toMediaTypeOrNull(),
                    RecordAudioDialog.mAudioFile!!.extension
                )
                val description = RequestBody.create(
                    "description".toMediaTypeOrNull(),
                    doctor_notes_text.text.toString()
                )
                val apptId = RequestBody.create("apptid".toMediaTypeOrNull(), appointmentId!!)
                val fdate = RequestBody.create("followup_date".toMediaTypeOrNull(), "")
                sendFileUploadRequest(
                    apptId,
                    patientId,
                    doctorId,
                    doc_type,
                    body,
                    description,
                    fdate
                )

            } else {

                val patientId = RequestBody.create("pid".toMediaTypeOrNull(), patientId!!)
                val doctorId = RequestBody.create("doctorid".toMediaTypeOrNull(), doctorId!!)
                val doc_type = RequestBody.create("doc_type".toMediaTypeOrNull(), "")
                val description = RequestBody.create(
                    "description".toMediaTypeOrNull(),
                    doctor_notes_text.text.toString()
                )
                val apptId = RequestBody.create("apptid".toMediaTypeOrNull(), appointmentId!!)
                val fdate = RequestBody.create("followup_date".toMediaTypeOrNull(), "")
                sendFileUploadRequest1(apptId, patientId, doctorId, doc_type, description, fdate)

                progress_1.visibility = View.GONE
                toast("Notes Updated")
            }


        }
        save_btn_r.setOnClickListener {
            progress_1.visibility = View.VISIBLE
            if (RecordAudioDialog.mAudioFile != null) {
                val requestFile =
                    RequestBody.create(
                        "multipart/form-data".toMediaTypeOrNull(),
                        RecordAudioDialog.mAudioFile!!
                    )
                val stringName = RecordAudioDialog.mAudioFile.toString().split("/")
                val body =
                    MultipartBody.Part.createFormData("uploadfile", stringName[4], requestFile)
                val patientId = RequestBody.create("pid".toMediaTypeOrNull(), patientId!!)
                val doctorId = RequestBody.create("doctorid".toMediaTypeOrNull(), doctorId!!)
                val doc_type = RequestBody.create(
                    "doc_type".toMediaTypeOrNull(),
                    RecordAudioDialog.mAudioFile!!.extension
                )
                val description = RequestBody.create(
                    "description".toMediaTypeOrNull(),
                    doctor_notes_text.text.toString()
                )
                val apptId = RequestBody.create("apptid".toMediaTypeOrNull(), appointmentId!!)
                val fdate = RequestBody.create("followup_date".toMediaTypeOrNull(), "")
                sendFileUploadRequest(
                    apptId,
                    patientId,
                    doctorId,
                    doc_type,
                    body,
                    description,
                    fdate
                )

            } else {

                val patientId = RequestBody.create("pid".toMediaTypeOrNull(), patientId!!)
                val doctorId = RequestBody.create("doctorid".toMediaTypeOrNull(), doctorId!!)
                val doc_type = RequestBody.create("doc_type".toMediaTypeOrNull(), "")
                val description = RequestBody.create(
                    "description".toMediaTypeOrNull(),
                    doctor_notes_text.text.toString()
                )
                val apptId = RequestBody.create("apptid".toMediaTypeOrNull(), appointmentId!!)
                val fdate = RequestBody.create("followup_date".toMediaTypeOrNull(), "")
                sendFileUploadRequest1(apptId, patientId, doctorId, doc_type, description, fdate)

                progress_1.visibility = View.GONE
                toast("Notes Updated")
            }


        }
        save_btn.setOnClickListener {
            progress_1.visibility = View.VISIBLE
            if (RecordAudioDialog.mAudioFile != null) {
                val requestFile =
                    RequestBody.create(
                        "multipart/form-data".toMediaTypeOrNull(),
                        RecordAudioDialog.mAudioFile!!
                    )
                val stringName = RecordAudioDialog.mAudioFile.toString().split("/")
                val body =
                    MultipartBody.Part.createFormData("uploadfile", stringName[4], requestFile)
                val patientId = RequestBody.create("pid".toMediaTypeOrNull(), patientId!!)
                val doctorId = RequestBody.create("doctorid".toMediaTypeOrNull(), doctorId!!)
                val doc_type = RequestBody.create(
                    "doc_type".toMediaTypeOrNull(),
                    RecordAudioDialog.mAudioFile!!.extension
                )
                val description = RequestBody.create(
                    "description".toMediaTypeOrNull(),
                    doctor_notes_text.text.toString()
                )
                val apptId = RequestBody.create("apptid".toMediaTypeOrNull(), appointmentId!!)
                val fdate = RequestBody.create("followup_date".toMediaTypeOrNull(), "")
                sendFileUploadRequest(
                    apptId,
                    patientId,
                    doctorId,
                    doc_type,
                    body,
                    description,
                    fdate
                )

            } else {

                val patientId = RequestBody.create("pid".toMediaTypeOrNull(), patientId!!)
                val doctorId = RequestBody.create("doctorid".toMediaTypeOrNull(), doctorId!!)
                val doc_type = RequestBody.create("doc_type".toMediaTypeOrNull(), "")
                val description = RequestBody.create(
                    "description".toMediaTypeOrNull(),
                    doctor_notes_text.text.toString()
                )
                val apptId = RequestBody.create("apptid".toMediaTypeOrNull(), appointmentId!!)
                val fdate = RequestBody.create("followup_date".toMediaTypeOrNull(), "")
                sendFileUploadRequest1(apptId, patientId, doctorId, doc_type, description, fdate)

                progress_1.visibility = View.GONE
                toast("Notes Updated")
            }


        }
        vitals_btn.setBackgroundResource(R.drawable.vitalinactive)
        voicenotes_btn.setBackgroundResource(R.drawable.vitalinactive)
        docnotes_btn.setBackgroundResource(R.drawable.docnoteactive)
        doctor_notes_title.visibility = View.VISIBLE
        doctor_notes_layout.visibility = View.VISIBLE
        prescription_title.visibility = View.GONE
        speak_btn.visibility = View.GONE
        record_view.visibility = View.GONE
        set.visibility = View.GONE
        vitals_layout.visibility = View.INVISIBLE
        save_btn.visibility = View.GONE
        close_btn.visibility = View.GONE
        uploaded_files_title.visibility = View.GONE
        uploaded_files_btn.visibility = View.GONE
        cancel_appt_btn.visibility = View.GONE
        docnotes_btn.setOnClickListener {
            vitals_btn.setBackgroundResource(R.drawable.vitalinactive)
            voicenotes_btn.setBackgroundResource(R.drawable.vitalinactive)
            docnotes_btn.setBackgroundResource(R.drawable.docnoteactive)
            doctor_notes_title.visibility = View.VISIBLE
            doctor_notes_layout.visibility = View.VISIBLE
            prescription_title.visibility = View.GONE
            speak_btn.visibility = View.GONE
            record_view.visibility = View.GONE
            set.visibility = View.GONE
            vitals_layout.visibility = View.INVISIBLE
            save_btn.visibility = View.GONE
            close_btn.visibility = View.GONE
            uploaded_files_title.visibility = View.GONE
            uploaded_files_btn.visibility = View.GONE
            cancel_appt_btn.visibility = View.GONE
            save_btn_d.visibility = View.VISIBLE
            close_btn_d.visibility = View.VISIBLE
            // uploaded_files_title_d.visibility=View.VISIBLE
            uploaded_files_btn_d.visibility = View.VISIBLE
            cancel_appt_btn_d.visibility = View.VISIBLE
            save_btn_r.visibility = View.GONE
            close_btn_r.visibility = View.GONE
            uploaded_files_title_r.visibility = View.GONE
            uploaded_files_btn_r.visibility = View.GONE
            cancel_appt_btn_r.visibility = View.GONE
            if (apptstatus.equals("closed") || apptstatus.equals("cancelled_patient")) {
                save_btn_d.visibility = View.GONE
                close_btn_d.visibility = View.GONE
                // cancel_btn.visibility = View.INVISIBLE
                cancel_appt_btn.visibility = View.GONE
                cancel_appt_btn_d.visibility = View.GONE
                cancel_appt_btn_r.visibility = View.GONE
                start_call_btn.visibility = View.INVISIBLE
            }

        }
        vitals_btn.setOnClickListener {
            vitals_btn.setBackgroundResource(R.drawable.vitalactive)
            voicenotes_btn.setBackgroundResource(R.drawable.vitalinactive)
            docnotes_btn.setBackgroundResource(R.drawable.docnoteinactive)
            doctor_notes_title.visibility = View.GONE
            doctor_notes_layout.visibility = View.GONE
            prescription_title.visibility = View.GONE
            speak_btn.visibility = View.GONE
            record_view.visibility = View.GONE
            set.visibility = View.GONE
            vitals_layout.visibility = View.VISIBLE
            save_btn.visibility = View.VISIBLE
            close_btn.visibility = View.VISIBLE
            //uploaded_files_title.visibility=View.VISIBLE
            uploaded_files_btn.visibility = View.VISIBLE
            cancel_appt_btn.visibility = View.VISIBLE
            save_btn_d.visibility = View.GONE
            close_btn_d.visibility = View.GONE
            uploaded_files_title_d.visibility = View.GONE
            uploaded_files_btn_d.visibility = View.GONE
            cancel_appt_btn_d.visibility = View.GONE
            save_btn_r.visibility = View.GONE
            close_btn_r.visibility = View.GONE
            uploaded_files_title_r.visibility = View.GONE
            uploaded_files_btn_r.visibility = View.GONE
            cancel_appt_btn_r.visibility = View.GONE
            if (apptstatus.equals("closed") || apptstatus.equals("cancelled_patient")) {
                save_btn.visibility = View.GONE
                close_btn.visibility = View.GONE
                //cancel_btn.visibility = View.INVISIBLE
                cancel_appt_btn.visibility = View.GONE
                cancel_appt_btn_d.visibility = View.GONE
                cancel_appt_btn_r.visibility = View.GONE
                start_call_btn.visibility = View.INVISIBLE
            }
        }
        voicenotes_btn.setOnClickListener {
            vitals_btn.setBackgroundResource(R.drawable.vitalinactive)
            voicenotes_btn.setBackgroundResource(R.drawable.vitalactive)
            docnotes_btn.setBackgroundResource(R.drawable.docnoteinactive)
            doctor_notes_title.visibility = View.GONE
            doctor_notes_layout.visibility = View.GONE
            prescription_title.visibility = View.VISIBLE
            speak_btn.visibility = View.VISIBLE
            record_view.visibility = View.VISIBLE
            set.visibility = View.VISIBLE
            vitals_layout.visibility = View.INVISIBLE
            save_btn_d.visibility = View.GONE
            close_btn_d.visibility = View.GONE
            uploaded_files_title_d.visibility = View.GONE
            uploaded_files_btn_d.visibility = View.GONE
            cancel_appt_btn_d.visibility = View.GONE
            save_btn.visibility = View.GONE
            close_btn.visibility = View.GONE
            uploaded_files_title.visibility = View.GONE
            uploaded_files_btn.visibility = View.GONE
            cancel_appt_btn.visibility = View.GONE
            save_btn_r.visibility = View.VISIBLE
            close_btn_r.visibility = View.VISIBLE
            //uploaded_files_title_r.visibility=View.VISIBLE
            uploaded_files_btn_r.visibility = View.VISIBLE
            cancel_appt_btn_r.visibility = View.VISIBLE
            if (apptstatus.equals("closed") || apptstatus.equals("cancelled_patient")) {
                save_btn_r.visibility = View.GONE
                close_btn_r.visibility = View.GONE
                //cancel_btn.visibility = View.INVISIBLE
                cancel_appt_btn.visibility = View.GONE
                cancel_appt_btn_d.visibility = View.GONE
                cancel_appt_btn_r.visibility = View.GONE
                start_call_btn.visibility = View.INVISIBLE
            }
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

                } else if (!temparature_f.equals("") && temparature_f.toString().toInt() > 108) {
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

                } else if (!temparature_f.equals("") && temparature_f.toString().toInt() > 108) {
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
                } else if (!height_inch.toString().equals("") && weightsp.selectedItem.toString()
                        .equals("Kg")
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
            val userData = SharedPrefs.getUserData(this@AppointmentDetailsActivityNew)
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

            val intent = Intent(this@AppointmentDetailsActivityNew, VitalsConfirmation::class.java)
            intent.putExtra("appointmentId", appointmentId)
            intent.putExtra("patientId", patientId)
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

            /* progress_1.visibility = View.VISIBLE
             ApiUtils.getAPIService(this).AddVitalsRequest(appointmentId!!, bp1val.toString(), bp2val.toString(), sugarval.toString(), sugar_type, temparature_c, temparature_f, weight_kg, weight_lb, heightfval.toString(), height_cm, height_inch, pulseval.toString(), pulse2val.toString(), oxygenval.toString(), oxygen2val.toString(), breathingval.toString(), breathing2val.toString(), sender_id, patientId!!).enqueue(object : Callback<AddVitalsResponse> {
                 override fun onFailure(call: Call<AddVitalsResponse>, t: Throwable) {
                     progress_1.visibility = View.GONE
                     toast(t.toString())
                 }

                 override fun onResponse(call: Call<AddVitalsResponse>, response: Response<AddVitalsResponse>) {
                     progress_1.visibility = View.GONE
                     toast("Vitals Added Successfully !")
                     val intent = Intent(this@AppointmentDetailsActivityNew, VitalsHistory::class.java)

                     intent.putExtra("appointmentId", appointmentId)
                     intent.putExtra("patientId", patientId)

                     startActivity(intent)
                 }
             })*/
            /* }*/
        }

        vitalhistorybtn.setOnClickListener {
            val intent = Intent(this, VitalsHistory::class.java)

            intent.putExtra("appointmentId", appointmentId)
            intent.putExtra("patientId", patientId)

            startActivity(intent)


        }


        val username = userData!!.username
        val usertype = "DOCTOR"
        ApiUtils.getAPIService(this@AppointmentDetailsActivityNew).get_medicine_list(
            SharedPrefs.getString(
                this@AppointmentDetailsActivityNew,
                "sessionid"
            )!!, username!!, usertype
        )
            .enqueue(object : Callback<MedicineList> {
                override fun onFailure(call: Call<MedicineList>, t: Throwable) {
                    toast("Something went wrong")
                }

                override fun onResponse(
                    call: Call<MedicineList>,
                    response: Response<MedicineList>
                ) {
                    try {
                        if (response.isSuccessful && response.body() != null && response.body()!!.statuscode == 200) {
                            addmedicinebtn.visibility = View.VISIBLE
                            addmedicinebtn.setOnClickListener {
                                var medicine = medicines_drp.selectedItem.toString()
                                var quantity = quantitymeddrp.selectedItem.toString()
                                var dosage = dosagemeddrp.selectedItem.toString()
                                doctor_notes_text.setText(doctor_notes_text.text.toString() + "\n" + medicine + " " + quantity + " " + dosage + ",")

                                if (medicinestring.equals("")) {
                                    medicinestring =
                                        medicineidList.get(medicinenameList.indexOf(medicine)) + "@#@" + appointmentId + "@#@" + dosage + "@#@" + quantity
                                } else {
                                    medicinestring = medicinestring + "#@#" + medicineidList.get(
                                        medicinenameList.indexOf(medicine)
                                    ) + "@#@" + appointmentId + "@#@" + dosage + "@#@" + quantity
                                }
                                Log.e("Medicine String ", medicinestring);
                            }
                            var medicines: ArrayList<String> = ArrayList()
                            var medicinesAdapter: ArrayAdapter<String>? = null
                            medicinenameList.clear()
                            medicineidList.clear()
                            for (medicine in response.body()!!.data!!.list!!) {
                                medicines.add(medicine.Brand_Name)
                                medicinenameList.add(medicine.Brand_Name)
                                medicineidList.add(medicine.Brand_ID.toString())
                            }
                            //    medicines_drp.setTitle("Select Medicine");
                            //     medicines_drp.setPositiveButton("Close");
                            medicinesAdapter = ArrayAdapter(
                                this@AppointmentDetailsActivityNew,
                                android.R.layout.simple_spinner_item,
                                medicines
                            )
                            medicinesAdapter!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            medicines_drp.adapter = medicinesAdapter
                        } else {
                            addmedicinebtn.visibility = View.GONE
                            medicinesdropdowns.visibility = View.GONE
                        }
                    } catch (e: Exception) {
                        toast("Error: Medicine list failed to load")
                        addmedicinebtn.visibility = View.GONE
                        medicinesdropdowns.visibility = View.GONE
                    }
                }
            })
    }


    fun getPatientFiles(patientId: String) {
        val userData = SharedPrefs.getUserData(this)
        val username = userData!!.username
        val usertype = "DOCTOR"
        ApiUtils.getAPIService(this@AppointmentDetailsActivityNew).getPatientDocuments(
            patientId,
            SharedPrefs.getString(this@AppointmentDetailsActivityNew, "sessionid")!!,
            username!!,
            usertype
        )
            .enqueue(object : Callback<GetPatientDocumentsResponse> {
                override fun onFailure(call: Call<GetPatientDocumentsResponse>, t: Throwable) {
                    toast("Something went wrong")
                }

                override fun onResponse(
                    call: Call<GetPatientDocumentsResponse>,
                    response: Response<GetPatientDocumentsResponse>
                ) {
                    try {
                        if (response.isSuccessful && response.body() != null && response.body()!!.statuscode == 200 && response.body()!!.data.documents != null) {
                            uploadedFilesAppointmentDetailsRecyclerViewAdapter =
                                UploadedFilesAppointmentDetailsRecyclerViewAdapter(
                                    this@AppointmentDetailsActivityNew,
                                    response.body()!!.data.documents!!,
                                    openDialog = {
                                        val newDialog = ViewImageDialog(it)
                                        newDialog.show(supportFragmentManager, "open Image")
                                        supportFragmentManager.executePendingTransactions()
                                    })
                            //    uploaded_files_recycler_view.adapter = uploadedFilesAppointmentDetailsRecyclerViewAdapter
                        }
                    } catch (e: Exception) {
                        toast("Error: Try again")
                    }
                }
            })
    }


    fun sendFileUploadRequest(
        appointmentId: RequestBody,
        patientId: RequestBody,
        doctorId: RequestBody,
        doc_type: RequestBody,
        file: MultipartBody.Part,
        description: RequestBody,
        fdate: RequestBody
    ) {
        val userData = SharedPrefs.getUserData(this)
        val sessionid = RequestBody.create(
            "sessionid".toMediaTypeOrNull(),
            SharedPrefs.getString(this, "sessionid")!!
        )
        val username = RequestBody.create(
            "username".toMediaTypeOrNull(),
            userData!!.username.toString()
        )
        val usertype = RequestBody.create("usertype".toMediaTypeOrNull(), "DOCTOR")
        ApiUtils.getAPIService(this@AppointmentDetailsActivityNew)
            .uploadPatientFile(
                appointmentId,
                patientId,
                doctorId,
                doc_type,
                file,
                description,
                sessionid,
                username,
                usertype,
                fdate
            )
            .enqueue(object : Callback<UploadPatientFileResponse> {
                override fun onFailure(call: Call<UploadPatientFileResponse>, t: Throwable) {
                    progress_1.visibility = View.GONE
                    toast("Something went wrong")
                    Log.e("Hello", t.toString())
                    toast(t.toString())
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
                                SendPatientMedicines(medicinestring);
                            }
                            /*   val userData=SharedPrefs.getUserData(this@AppointmentDetailsActivityNew)
                               val username=userData!!.username
                               val usertype="DOCTOR"
                               ApiUtils.getAPIService(this@AppointmentDetailsActivityNew).closeAppointmentRequest(appointmentIdSend!!,SharedPrefs.getString(this@AppointmentDetailsActivityNew, "sessionid")!!,username!!,usertype).enqueue(object : Callback<CloseAppointmentResponse> {
                                   override fun onFailure(call: Call<CloseAppointmentResponse>, t: Throwable) {
                                       progress_1.visibility = View.GONE
                                       toast("Something went wrong")
                                   }

                                   override fun onResponse(call: Call<CloseAppointmentResponse>, response: Response<CloseAppointmentResponse>) {
                                       progress_1.visibility = View.GONE
                                       toast("Appointment closed !")
                                      // cancel_btn.visibility = View.INVISIBLE
                                       cancel_appt_btn.visibility =  View.GONE
                                       cancel_appt_btn_d.visibility =  View.GONE
                                       cancel_appt_btn_r.visibility=View.GONE
                                       start_call_btn.visibility =  View.INVISIBLE
                                       showSuccessDialog {
                                                                      finish()
                                  }

                                   }
                               })*/
//                        showSuccessDialog {
//                            finish()
//                        }
                        } else {
                            progress_1.visibility = View.GONE
                            toast("Please try again with a different file")
                        }
                    } catch (e: Exception) {
                        toast("Error: Try again")
                    }
                }
            })

    }


    fun SendPatientMedicines(
        description: String
    ) {
        val userData = SharedPrefs.getUserData(this)
        val sessionid = SharedPrefs.getString(this, "sessionid")!!
        val username = userData!!.username.toString()
        val usertype = "DOCTOR"
        ApiUtils.getAPIService(this@AppointmentDetailsActivityNew)
            .patient_medicine(sessionid, username, usertype, description)
            .enqueue(object : Callback<ForgotPasswordResponse> {
                override fun onFailure(call: Call<ForgotPasswordResponse>, t: Throwable) {
                    progress_1.visibility = View.GONE
                    toast("Something went wrong")
                    Log.e("Hello", t.toString())
                    toast(t.toString())
                }

                override fun onResponse(
                    call: Call<ForgotPasswordResponse>,
                    response: Response<ForgotPasswordResponse>
                ) {
                    progress_1.visibility = View.GONE
                    try {
                        if (response.isSuccessful) {

                            /*val userData=SharedPrefs.getUserData(this@AppointmentDetailsActivityNew)
                            val username=userData!!.username
                            val usertype="DOCTOR"
                            ApiUtils.getAPIService(this@AppointmentDetailsActivityNew).closeAppointmentRequest(appointmentIdSend!!,SharedPrefs.getString(this@AppointmentDetailsActivityNew, "sessionid")!!,username!!,usertype).enqueue(object : Callback<CloseAppointmentResponse> {
                                override fun onFailure(call: Call<CloseAppointmentResponse>, t: Throwable) {
                                    progress_1.visibility = View.GONE
                                    toast("Something went wrong")
                                }

                                override fun onResponse(call: Call<CloseAppointmentResponse>, response: Response<CloseAppointmentResponse>) {
                                    progress_1.visibility = View.GONE
                                    toast("Appointment closed !")
                                   // cancel_btn.visibility = View.INVISIBLE
                                    cancel_appt_btn.visibility =  View.GONE
                                    cancel_appt_btn_d.visibility =  View.GONE
                                    cancel_appt_btn_r.visibility=View.GONE
                                    start_call_btn.visibility =  View.INVISIBLE
                                    showSuccessDialog {
                                        finish()
                                    }

                                }
                            })*/

//                        showSuccessDialog {
//                            finish()
//                        }
                        } else {
                            progress_1.visibility = View.GONE
                            toast("Please try again ")
                        }
                    } catch (e: Exception) {
                        toast("Error: Try again")
                    }
                }
            })

    }

    fun sendFileUploadRequest1(
        appointmentId: RequestBody,
        patientId: RequestBody,
        doctorId: RequestBody,
        doc_type: RequestBody,

        description: RequestBody,
        fdate: RequestBody
    ) {
        val userData = SharedPrefs.getUserData(this)
        val sessionid = RequestBody.create(
            "sessionid".toMediaTypeOrNull(),
            SharedPrefs.getString(this, "sessionid")!!
        )
        val username = RequestBody.create(
            "username".toMediaTypeOrNull(),
            userData!!.username.toString()
        )
        val usertype = RequestBody.create("usertype".toMediaTypeOrNull(), "DOCTOR")
        ApiUtils.getAPIService(this@AppointmentDetailsActivityNew)
            .uploadPatientFile1(
                appointmentId,
                patientId,
                doctorId,
                doc_type,
                description,
                sessionid,
                username,
                usertype,
                fdate
            )
            .enqueue(object : Callback<UploadPatientFileResponse> {
                override fun onFailure(call: Call<UploadPatientFileResponse>, t: Throwable) {
                    progress_1.visibility = View.GONE
                    toast("Something went wrong")
                    Log.e("Hello", t.toString())
                    toast(t.toString())
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
                                SendPatientMedicines(medicinestring);
                            }
                            /*val userData=SharedPrefs.getUserData(this@AppointmentDetailsActivityNew)
                            val username=userData!!.username
                            val usertype="DOCTOR"
                            ApiUtils.getAPIService(this@AppointmentDetailsActivityNew).closeAppointmentRequest(appointmentIdSend!!,SharedPrefs.getString(this@AppointmentDetailsActivityNew, "sessionid")!!,username!!,usertype).enqueue(object : Callback<CloseAppointmentResponse> {
                                override fun onFailure(call: Call<CloseAppointmentResponse>, t: Throwable) {
                                    progress_1.visibility = View.GONE
                                    toast("Something went wrong")
                                }

                                override fun onResponse(call: Call<CloseAppointmentResponse>, response: Response<CloseAppointmentResponse>) {
                                    progress_1.visibility = View.GONE
                                    toast("Appointment closed !")
                                   // cancel_btn.visibility = View.INVISIBLE
                                    cancel_appt_btn.visibility =  View.GONE
                                    cancel_appt_btn_d.visibility =  View.GONE
                                    cancel_appt_btn_r.visibility=View.GONE
                                    start_call_btn.visibility =  View.INVISIBLE
                                    showSuccessDialog {
                                        finish()
                                    }

                                }
                            })*/

//                        showSuccessDialog {
//                            finish()
//                        }
                        } else {
                            progress_1.visibility = View.GONE
                            toast("Please try again with a different file")
                        }
                    } catch (e: Exception) {
                        toast("Error: Try again")
                    }
                }
            })

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
            record_button.isListenForRecord = false
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
                    record_button.isListenForRecord = false
                } else {
                    // if permission granted, initialize the engine
                    this.toast("Permissions granted !!")
                    initRecordView()
                    startVoiceRecorder()
                }
            }
        }
    }


    fun sendNotification(patientId: String, message: String) {
        val userData = SharedPrefs.getUserData(this)
        val username = userData!!.username
        val usertype = "DOCTOR"
        ApiUtils.getAPIService(this@AppointmentDetailsActivityNew).sendNotification(
            patientId,
            message,
            SharedPrefs.getString(this@AppointmentDetailsActivityNew, "sessionid")!!,
            username!!,
            usertype
        )
            .enqueue(object : Callback<SendNotificationResponse> {
                override fun onFailure(call: Call<SendNotificationResponse>, t: Throwable) {
                    //Do nothing
                }

                override fun onResponse(
                    call: Call<SendNotificationResponse>,
                    response: Response<SendNotificationResponse>
                ) {
                    //Do nothing
                }
            })
    }

    fun getAge(dobString: String): Int {
        if (dobString.equals("2000-03-16")) {
            return -1;
        }
        var date: Date? = null
        var sdf: SimpleDateFormat = SimpleDateFormat("yyyy-mm-dd")
        try {
            date = sdf.parse(dobString);
        } catch (e: ParseException) {
            e.printStackTrace();
        }


        if (date == null) {
            return 0
        }
        var dob: Calendar = Calendar.getInstance();
        var today: Calendar = Calendar.getInstance();

        dob.setTime(date);

        var year: Int = dob.get(Calendar.YEAR);
        var month: Int = dob.get(Calendar.MONTH);
        var day: Int = dob.get(Calendar.DAY_OF_MONTH);

        dob.set(year, month + 1, day);

        var age: Int = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }



        return age;
    }

    override fun onResume() {
        super.onResume()
        if (appointmentId.equals("")) {
        } else {
            val userData = SharedPrefs.getUserData(this)
            val sessionid = SharedPrefs.getString(this, "sessionid")
            val username = userData!!.username
            val usertype = "DOCTOR"
            ApiUtils.getAPIService(this@AppointmentDetailsActivityNew)
                .get_doctor_documents_appt(sessionid!!, username!!, usertype, appointmentId!!)
                .enqueue(object :
                    Callback<DoctorNotesResponse> {
                    override fun onFailure(call: Call<DoctorNotesResponse>, t: Throwable) {
                        //Do nothing
                    }

                    override fun onResponse(
                        call: Call<DoctorNotesResponse>,
                        response: Response<DoctorNotesResponse>
                    ) {
                        //Do nothing

                        try {
                            doctor_notes_text.setText(response.body()!!.data!!.documents[0].doctor_notes)
                        } catch (e: java.lang.Exception) {
                        }


                    }
                })
        }
    }
}

package com.app.ehealthaidoctor.ui.dialogs

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.app.ehealthaidoctor.R
import com.example.ehealthai.utils.toast
import com.github.piasy.rxandroidaudio.AudioRecorder
import kotlinx.android.synthetic.main.dialog_record_audio.view.*
import java.io.File

class RecordAudioDialog(
    var returnAudioFile: (fileName: String) -> Unit
) : DialogFragment() {
    var mAudioRecorder: AudioRecorder? = null

    private val PERMISSION_REQ_ID = 22

    companion object{
        var mAudioFile: File? = null
    }

    private val REQUESTED_PERMISSIONS = arrayOf(
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window!!.setLayout(width, height)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_record_audio, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.record_audio_text.text = "Press play to record audio"
        view.play_btn.setOnClickListener {
            view.record_audio_text.text = "Recording audio..."
            mAudioRecorder = AudioRecorder.getInstance()
            mAudioFile =
                File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + System.nanoTime() + "_recording.m4a")

            if (checkSelfPermission(REQUESTED_PERMISSIONS[0], 1234) && checkSelfPermission(
                    REQUESTED_PERMISSIONS[1],
                    1234
                ) && checkSelfPermission(REQUESTED_PERMISSIONS[2], 1234)
            ) {
                mAudioRecorder!!.prepareRecord(
                    MediaRecorder.AudioSource.MIC,
                    MediaRecorder.OutputFormat.MPEG_4,
                    MediaRecorder.AudioEncoder.AAC,
                    mAudioFile
                )
                mAudioRecorder!!.startRecord()

                it.visibility = View.GONE
                view.stop_btn.visibility = View.VISIBLE
            }
        }


        view.stop_btn.setOnClickListener {
            view.record_audio_text.text = "Press play to record audio"
            mAudioRecorder!!.stopRecord()

            /*PlayConfig.file(mAudioFile) // play a local file
                //.res(getApplicationContext(), R.raw.audio_record_end) // or play a raw resource
                .looping(false) // loop or not
                .leftVolume(1.0F) // left volume
                .rightVolume(1.0F) // right volume
                .build()*/
            returnAudioFile(mAudioFile.toString())
            dialog!!.dismiss()
        }
    }

    fun checkSelfPermission(permission: String, requestCode: Int): Boolean {
        if (ContextCompat.checkSelfPermission(context!!, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                activity!!,
                REQUESTED_PERMISSIONS,
                requestCode
            )
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        Log.i("TAG", "onRequestPermissionsResult " + grantResults[0] + " " + requestCode)

        when (requestCode) {
            PERMISSION_REQ_ID -> {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED || grantResults[1] != PackageManager.PERMISSION_GRANTED || grantResults[2] != PackageManager.PERMISSION_GRANTED) {
                    context!!.toast("Permission Needed")
                }
                // if permission granted, initialize the engine
                context!!.toast("Permission granted !!")
            }
        }
    }


}
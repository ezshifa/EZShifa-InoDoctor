package com.app.ehealthai.doctor.ui.activities

import android.app.ProgressDialog
import android.media.MediaPlayer.OnPreparedListener
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.MediaController
import android.widget.TextView
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import com.app.ehealthaidoctor.R

class v : AppCompatActivity() {
    var videoView: VideoView? = null
    var tv_filename: TextView? = null
    var iv_close: ImageView? = null
    var mMedia: MediaController? = null
    var progressDialog: ProgressDialog? = null
    var iv_back: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_v)
        progressDialog = ProgressDialog.show(this, "", "Loading...", true);
        videoView = findViewById(R.id.videoView)
        tv_filename = findViewById(R.id.tv_filename)
        iv_close = findViewById(R.id.iv_close)
        iv_back = findViewById(R.id.iv_back)
        //Global.showProgressDialog(this, "Loading Video...")

        iv_close?.setOnClickListener {
            finish()
        }
        iv_back?.setOnClickListener {
            finish()
        }
        try {
            var globaluri=""
            if(intent!=null)
            {
                globaluri=intent.getStringExtra("videoUrl")!!
            }
            tv_filename?.text = globaluri
            val link = Uri.parse(globaluri)
            mMedia = MediaController(this)
            mMedia?.setMediaPlayer(videoView)
            mMedia?.setAnchorView(videoView)
            videoView?.setMediaController(mMedia)
            videoView?.setVideoURI(Uri.parse(link.toString()))
            mMedia?.requestFocus()

            videoView?.setOnPreparedListener(OnPreparedListener {
                progressDialog?.dismiss()
                videoView?.start()
            })

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (progressDialog != null) {
            if (progressDialog?.isShowing == true) {
                progressDialog?.hide()
                progressDialog?.dismiss()
            }
            progressDialog = null
        }
    }
}
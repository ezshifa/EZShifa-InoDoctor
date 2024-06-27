package com.app.ehealthaidoctor.adapters.recyclerViewAdapters


import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Handler
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.app.ehealthai.doctor.ui.activities.v
import com.app.ehealthaidoctor.R
import com.app.ehealthaidoctor.models.responses.Document
import com.app.ehealthaidoctor.utils.SharedPrefs
import com.squareup.picasso.Picasso


class UserDocumentsRecyclerViewAdapter(
    val context: Context,
    val filesList: List<Document>,
    var openDialog: (imageUrl: String) -> Unit
) :
    RecyclerView.Adapter<UserDocumentsRecyclerViewAdapter.ViewHolder>() {

    var fileExtension = ""

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(context)
                .inflate(R.layout.item_user_documents_recycler_view, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = filesList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = filesList.get(position)

        val imageUrl = SharedPrefs.getString(context, "filepath") + item.file_name

        if (item.file_name.contains(".")) {
            val fileArray = item.file_name.split(".")
            val fileExtension = fileArray.get(1)

            holder.FileLayout.setOnClickListener {
                var totalTime: Int = 0
                var mp: MediaPlayer
                try {
                    if (fileExtension.equals("png") || fileExtension.equals("jpg") || fileExtension.equals(
                            "jpeg"
                        )
                    ) {
                        openDialog(imageUrl)
                    } else if (fileExtension.equals("pdf") || fileExtension.equals("xls") || fileExtension.equals(
                            "xlsx"
                        ) || fileExtension.equals("doc") || fileExtension.equals("docx")
                    ) {
                        try {
                            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(imageUrl))
                            context.startActivity(browserIntent)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
//                        context.startActivity(Intent(context,WebViewActivity::class.java).putExtra("Url",imageUrl))
                    } else if (fileExtension.equals("m4a") || fileExtension.equals("amr") || fileExtension.equals(
                            "mp3"
                        ) || fileExtension.equals("3gp") || fileExtension.equals("ogg") || fileExtension.equals(
                            "aac"
                        )
                    ) {
                        val dialog = Dialog(context)
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                        dialog.setCancelable(false)
                        dialog.setContentView(R.layout.audioplayer_custom_layout)
                        val AudioLayout = dialog.findViewById(R.id.player) as RelativeLayout
                        val volumeBar = dialog.findViewById(R.id.volumeBar) as SeekBar
                        val positionBar = dialog.findViewById(R.id.positionBar) as SeekBar
                        val playBtn = dialog.findViewById(R.id.playBtn) as Button
                        val elapsedTimeLabel =
                            dialog.findViewById(R.id.elapsedTimeLabel) as TextView
                        val remainingTimeLabel =
                            dialog.findViewById(R.id.remainingTimeLabel) as TextView
                        val cross = dialog.findViewById(R.id.cross) as ImageView

                        dialog.show()

                        @SuppressLint("HandlerLeak")
                        var handler = object : Handler() {
                            override fun handleMessage(msg: Message) {
                                var currentPosition = msg.what

                                // Update positionBar
                                positionBar.progress = currentPosition

                                // Update Labels
                                var elapsedTime = createTimeLabel(currentPosition)
                                elapsedTimeLabel.text = elapsedTime

                                var remainingTime = createTimeLabel(totalTime - currentPosition)
                                remainingTimeLabel.text = "$remainingTime"
                            }
                        }

                        mp = MediaPlayer.create(context, Uri.parse(imageUrl))
                        mp.isLooping = true
                        mp.setVolume(0.5f, 0.5f)
                        totalTime = mp.duration

                        // Thread
                        Thread(Runnable {
                            while (mp != null) {
                                try {
                                    var msg = Message()
                                    msg.what = mp.currentPosition
                                    handler.sendMessage(msg)
                                    Thread.sleep(1000)
                                } catch (e: InterruptedException) {
                                }
                            }
                        }).start()

                        volumeBar.setOnSeekBarChangeListener(
                            object : SeekBar.OnSeekBarChangeListener {
                                override fun onProgressChanged(
                                    seekbar: SeekBar?,
                                    progress: Int,
                                    fromUser: Boolean
                                ) {
                                    if (fromUser) {
                                        var volumeNum = progress / 100.0f
                                        mp.setVolume(volumeNum, volumeNum)
                                    }
                                }

                                override fun onStartTrackingTouch(p0: SeekBar?) {
                                }

                                override fun onStopTrackingTouch(p0: SeekBar?) {
                                }
                            }
                        )


                        // Position Bar
                        positionBar.max = totalTime
                        positionBar.setOnSeekBarChangeListener(
                            object : SeekBar.OnSeekBarChangeListener {
                                override fun onProgressChanged(
                                    seekBar: SeekBar?,
                                    progress: Int,
                                    fromUser: Boolean
                                ) {
                                    if (fromUser) {
                                        mp.seekTo(progress)
                                    }
                                }

                                override fun onStartTrackingTouch(p0: SeekBar?) {
                                }

                                override fun onStopTrackingTouch(p0: SeekBar?) {
                                }
                            }
                        )
                        playBtn.setOnClickListener(object : View.OnClickListener {
                            override fun onClick(v: View) {
                                if (mp.isPlaying) {
                                    // Stop
                                    mp.pause()
                                    playBtn.setBackgroundResource(R.drawable.play)

                                } else {
                                    // Start
                                    mp.start()
                                    playBtn.setBackgroundResource(R.drawable.stop)
                                }
                            }
                        })


                        cross.setOnClickListener {
                            try {
                                mp.stop()
                                playBtn.setBackgroundResource(R.drawable.play)
                            } catch (e: Exception) {
                            }
                            dialog.dismiss()
                        }


                    } else if (fileExtension.equals("mp4")) {

//                        Globbal.videoUrl = imageUrl
                        //   Globbal.videoname = item.file
                        context.startActivity(Intent(context, v::class.java).putExtra("videoUrl",imageUrl))

                    }
                } catch (e: Exception) {
                    Toast.makeText(context, "Error : File cannot be opened", Toast.LENGTH_LONG)
                        .show()
                }
            }
        }

        if (item.file_name.contains("mp4")) {

        }
        // Picasso.get().load(imageUrl).into(holder.FileImage)
        if (item.file_name.contains(".")) {
            val fileArray = item.file_name.split(".")
            fileExtension = fileArray.get(1)
            if (fileExtension.equals("pdf")) {
                holder.FileImage.setImageResource(R.drawable.pdficonn)
            } else if (fileExtension.equals("mp4")) {
                holder.FileImage.setImageResource(R.drawable.videoplayer)
            } else {
                Picasso.get().load(imageUrl).error(context.resources.getDrawable(R.drawable.audio))
                    .into(holder.FileImage)
            }
        }


        holder.FileName.text = item.filetitle
        holder.FileDate.text = item.create_date
        holder.FileType.text = item.doc_type


    }


    // stores and recycles views as they are scrolled off screen
    class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val FileImage by lazy { itemView.findViewById<ImageView>(R.id.file_preview) }
        val FileName by lazy { itemView.findViewById<TextView>(R.id.file_name) }
        val FileDate by lazy { itemView.findViewById<TextView>(R.id.date) }
        val FileType by lazy { itemView.findViewById<TextView>(R.id.type_name) }
        val FileLayout by lazy { itemView.findViewById<RelativeLayout>(R.id.file_layout) }
        /*  val AudioLayout by lazy { itemView.findViewById<RelativeLayout>(R.id.player) }
          val volumeBar by lazy { itemView.findViewById<SeekBar>(R.id.volumeBar) }
          val positionBar by lazy { itemView.findViewById<SeekBar>(R.id.positionBar) }
          val playBtn   by lazy { itemView.findViewById<Button>(R.id.playBtn) }
          val elapsedTimeLabel   by lazy { itemView.findViewById<TextView>(R.id.elapsedTimeLabel) }
          val remainingTimeLabel by lazy { itemView.findViewById<TextView>(R.id.remainingTimeLabel) }
          val cross by lazy { itemView.findViewById<ImageView>(R.id.cross) }*/
    }

    fun createTimeLabel(time: Int): String {
        var timeLabel = ""
        var min = time / 1000 / 60
        var sec = time / 1000 % 60

        timeLabel = "$min:"
        if (sec < 10) timeLabel += "0"
        timeLabel += sec

        return timeLabel
    }
}

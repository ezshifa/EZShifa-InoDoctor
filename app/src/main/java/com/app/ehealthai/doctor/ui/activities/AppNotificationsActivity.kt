package com.app.ehealthai.doctor.ui.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.ehealthai.doctor.adapters.AppNotificationsRecyclerViewAdapter
import com.app.ehealthai.doctor.models.responses.notifications_info
import com.app.ehealthai.doctor.utils.TinyDB
import com.app.ehealthaidoctor.R
import com.app.ehealthaidoctor.utils.SharedPrefs
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric
import kotlinx.android.synthetic.main.activity_appnotifications.*
import java.util.*
import kotlin.collections.ArrayList

class AppNotificationsActivity : AppCompatActivity() {
    var appNotificationsRecyclerViewAdapter: AppNotificationsRecyclerViewAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fabric.with(this, Crashlytics())
        setContentView(R.layout.activity_appnotifications)
        SharedPrefs.save(applicationContext, "notifcount", 0)
        nf_back_btn.setOnClickListener { onBackPressed() }
        camblistview.layoutManager = LinearLayoutManager(this@AppNotificationsActivity)
        val tinydb = TinyDB(this);



        if (tinydb.getListString("notifications").isEmpty()) {
            emptytextca.visibility = View.VISIBLE
        } else {
            val aiList: ArrayList<notifications_info> = ArrayList()
            var notifcount: Int = 0
            for (item in tinydb.getListString("notifications")) {

                try {
                    if (notifcount == 50) {
                    } else {
                        val splitnotif = item.split("#@#")
                        // if(splitnotif[0].toString().equals("ping doc"))
                        // {
                        //    continue
                        //}
                        val splitinner = splitnotif[1].toString().split("@#@")
                        Log.e("URL ", "URL " + splitnotif[3].toString())
                        aiList.add(
                            notifications_info(
                                splitnotif[0].toString(),
                                splitinner[1].toString(),
                                splitnotif[2].toString(),
                                splitnotif[3].toString()
                            )
                        )
                    }
                    notifcount++
                } catch (e: Exception) {
                }
            }
            Collections.reverse(aiList);
            appNotificationsRecyclerViewAdapter =
                AppNotificationsRecyclerViewAdapter(this@AppNotificationsActivity, aiList)
            camblistview.adapter = appNotificationsRecyclerViewAdapter
        }


    }
}
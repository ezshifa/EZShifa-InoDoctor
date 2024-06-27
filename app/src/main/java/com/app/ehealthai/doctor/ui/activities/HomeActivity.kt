package com.app.ehealthaidoctor.ui.activities

import android.Manifest
import android.app.Activity
import android.app.ActivityManager
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.ehealthai.doctor.models.responses.ForgotPasswordResponse
import com.app.ehealthai.doctor.models.responses.GetDocOnlineResponse
import com.app.ehealthai.doctor.models.responses.LogoutResponse.LogoutResonse
import com.app.ehealthai.doctor.services.KeepOnlineService
import com.app.ehealthai.doctor.ui.activities.AppNotificationsActivity
import com.app.ehealthai.doctor.ui.activities.MyRatings
import com.app.ehealthaidoctor.R
import com.app.ehealthaidoctor.adapters.recyclerViewAdapters.HomeNavigationRecyclerViewAdapter
import com.app.ehealthaidoctor.models.responses.GetUpdateProfileResponse
import com.app.ehealthaidoctor.models.responses.Profile
import com.app.ehealthaidoctor.models.responses.SendFirebaseTokenResponse
import com.app.ehealthaidoctor.network.ApiUtils
import com.app.ehealthaidoctor.ui.activities.Chat.UserChatsActivity
import com.app.ehealthaidoctor.ui.activities.profile.ProfileActivity
import com.app.ehealthaidoctor.utils.SharedPrefs
import com.bumptech.glide.Glide
import com.crashlytics.android.Crashlytics
import com.example.ehealthai.utils.Constants
import com.example.ehealthai.utils.toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import io.fabric.sdk.android.Fabric
import kotlinx.android.synthetic.main.activity_home.avatar
import kotlinx.android.synthetic.main.activity_home.change_image_btn
import kotlinx.android.synthetic.main.activity_home.contact_us_btn
import kotlinx.android.synthetic.main.activity_home.count
import kotlinx.android.synthetic.main.activity_home.drawer_layout
import kotlinx.android.synthetic.main.activity_home.menu_btn
import kotlinx.android.synthetic.main.activity_home.messagesContainer
import kotlinx.android.synthetic.main.activity_home.myAppointmentsContainer
import kotlinx.android.synthetic.main.activity_home.myrating
import kotlinx.android.synthetic.main.activity_home.navigation_menu
import kotlinx.android.synthetic.main.activity_home.notif_btn
import kotlinx.android.synthetic.main.activity_home.onofftoggle
import kotlinx.android.synthetic.main.activity_home.oof_image_btn
import kotlinx.android.synthetic.main.activity_home.telemedicine
import kotlinx.android.synthetic.main.activity_home.username
import kotlinx.android.synthetic.main.activity_home.version_text
import kotlinx.android.synthetic.main.dialog_doctor_status.view.btnConfirm
import kotlinx.android.synthetic.main.dialog_doctor_status.view.ivClose
import kotlinx.android.synthetic.main.dialog_doctor_status.view.rgBreaks
import kotlinx.android.synthetic.main.nav_header.name
import kotlinx.android.synthetic.main.nav_header.user_avatar
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date


class HomeActivity : AppCompatActivity() {
    var userData: Profile? = null
    var pic: Int = 0
    var PERMISSION_REQUEST_CAMERA = 0
    var CAMERA_REQUEST = 1
    var url: String = ""
    var homeNavigationRecyclerViewAdapter: HomeNavigationRecyclerViewAdapter? = null
    var servicerunning = false
    var progressDialog: ProgressDialog? = null
    var autoservicenotrunning = false

    var offlineReasonAppError = "App Error"
    var offlineReasonSessionTimeOutError = "Session Time Out"
    var docStatus = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fabric.with(this, Crashlytics())
        setContentView(R.layout.activity_home)

        try {
            val pInfo: PackageInfo =
                this.getPackageManager().getPackageInfo(this.getPackageName(), 0)
            val version = pInfo.versionName
            version_text.setText("Ino Doctor v$version")
        } catch (e: PackageManager.NameNotFoundException) {
            version_text.setText("Ino Doctor  v2.9.7")
            e.printStackTrace()
        }



        userData = SharedPrefs.getUserData(this@HomeActivity)
        Log.e("User data pic", userData!!.image_name!!)
        try {
            progressDialog = ProgressDialog(this@HomeActivity)
            if (isMyServiceRunning(KeepOnlineService::class.java)) {
                //onofftoggle.setText("Go Offline")
                onofftoggle.setImageResource(R.drawable.iamonline)
                servicerunning = true
                oof_image_btn.setBackgroundResource(R.drawable.onlineico)
            } else {
                ApiUtils.getAPIService(this@HomeActivity)
                    .get_doctor_online_status(userData!!.id!!.toString())
                    .enqueue(object : Callback<GetDocOnlineResponse> {
                        override fun onFailure(call: Call<GetDocOnlineResponse>, t: Throwable) {
                            //Do nothing for now
                        }

                        override fun onResponse(
                            call: Call<GetDocOnlineResponse>,
                            response: Response<GetDocOnlineResponse>
                        ) {
                            try {
                                if (response.isSuccessful) {
                                    val status = response.body()!!.data!!.onlinestatus
                                    docStatus = status
                                    if (status == "1") {
                                        if (!servicerunning) {
                                            progressDialog!!.setMessage("Going Online")
                                            progressDialog!!.show()
                                            changeoofstatus()
                                        } else {
                                            //replace if needed
                                            changeoofstatustooffline("Status 1 but foreground Service not running")
                                        }
                                    }else{
                                        oof_image_btn.setBackgroundResource(R.drawable.offlineico)
                                    }
                                }

                            } catch (e: Exception) {
                                //toast("Something went wrong, please try again later")
                            }
                        }
                    })
                //onofftoggle.setText("Go Online")
                onofftoggle.setImageResource(R.drawable.iamoffline)
                servicerunning = false
                oof_image_btn.setBackgroundResource(R.drawable.offlineico)
            }
            /*var notiftitle = intent.getStringExtra("notiftitle")
            var notifbody = intent.getStringExtra("notifbody")
            var notifimage = intent.getStringExtra("notifimage")
            if(!notiftitle.equals("")){
                val intent = Intent(this, DisplayNotificationDialog::class.java)
                intent.putExtra("notiftitle", notiftitle)
                intent.putExtra("notifbody", notifbody)
                intent.putExtra("notifimage", notifimage)
                startActivity(intent)}*/
        } catch (e: java.lang.Exception) {
        }
        //SharedPrefs.save(this@HomeActivity, "sessionid", "688787")
        Log.e("SESSION ID ", SharedPrefs.getString(this@HomeActivity, "sessionid")!!)
        var sessionid = SharedPrefs.getString(this, "sessionid")
        if (sessionid.equals("")) {
            SharedPrefs.removeUserData(this@HomeActivity)
            SharedPrefs.removeKey(this@HomeActivity, Constants.USERNAME)
            SharedPrefs.removeKey(this@HomeActivity, Constants.PASSWORD)
            SharedPrefs.save(this@HomeActivity, "TOKEN_REFRESHED", false)
            onofftoggle.setImageResource(R.drawable.iamoffline);
            servicerunning = false
            oof_image_btn.setBackgroundResource(R.drawable.offlineico)
            KeepOnlineService.stopService(this)
            changeoofstatustooffline(offlineReasonSessionTimeOutError)
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            toast("Session Timed Out.Please Re-Login")
            finishAffinity()
        }
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
            val builder = AlertDialog.Builder(this)

            // Set the alert dialog title
            builder.setTitle("Old Android Version")
            builder.setIcon(android.R.drawable.ic_dialog_info)
            // Display a message on alert dialog
            builder.setMessage("app has detected lower android version installed in your device, please upgrade your android version for better performance.")

            // Set a positive button and its click listener on alert dialog
            builder.setPositiveButton("Ok") { dialog, which ->
                // Do something when user press the positive button

            }

            // Finally, make the alert dialog using builder
            val dialog: AlertDialog = builder.create()

            // Display the alert dialog on app interface
            dialog.show()

        }

        //url = resources.getString(R.string.profilepicd)

        pic = 0
        init()
        onofftoggle.setOnClickListener {
            if (!servicerunning) {
                progressDialog!!.setMessage("Going Online")
                progressDialog!!.show()
                changeoofstatus()
            } else {
                showBreakSelectionDialog(false)
            }
        }
        notif_btn.setOnClickListener {
            val intent = Intent(this, AppNotificationsActivity::class.java)
            startActivity(intent)
        }
        val userData = SharedPrefs.getUserData(this@HomeActivity)
        val username = userData!!.username
        val usertype = "DOCTOR"

        ApiUtils.getAPIService(this@HomeActivity).manage_doctor_status(
            SharedPrefs.getString(this@HomeActivity, "sessionid")!!,
            username!!,
            usertype,
            "0",
            "0",
            userData.id!!.toString(),
            "Available"
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
                        }
                        if (response.body()!!.statuscode!!.equals("404")) {
                            toast(response.body()!!.data!!.error)
                            SharedPrefs.removeUserData(this@HomeActivity)
                            SharedPrefs.removeKey(this@HomeActivity, Constants.USERNAME)
                            SharedPrefs.removeKey(this@HomeActivity, Constants.PASSWORD)
                            SharedPrefs.save(this@HomeActivity, "TOKEN_REFRESHED", false)
                            onofftoggle.setImageResource(R.drawable.iamoffline);
                            servicerunning = false
                            oof_image_btn.setBackgroundResource(R.drawable.offlineico)
                            KeepOnlineService.stopService(this@HomeActivity)
                            changeoofstatustooffline(offlineReasonAppError)
                            val intent = Intent(this@HomeActivity, LoginActivity::class.java)
                            startActivity(intent)
                            finishAffinity()
                        }

                    } catch (e: Exception) {
                        toast("Something went wrong, please try again later." + " Please Relogin")
                        onofftoggle.setImageResource(R.drawable.iamoffline);
                        servicerunning = false
                        oof_image_btn.setBackgroundResource(R.drawable.offlineico)
                        KeepOnlineService.stopService(this@HomeActivity)
                        changeoofstatustooffline(offlineReasonAppError)
                        SharedPrefs.removeUserData(this@HomeActivity)
                        SharedPrefs.removeKey(this@HomeActivity, Constants.USERNAME)
                        SharedPrefs.removeKey(this@HomeActivity, Constants.PASSWORD)
                        SharedPrefs.save(this@HomeActivity, "TOKEN_REFRESHED", false)

                        val intent = Intent(this@HomeActivity, LoginActivity::class.java)
                        startActivity(intent)
                        finishAffinity()
                    }
                }
            })


        var appversion: String = ""
        val version: String = android.os.Build.VERSION.SDK
        val device: String = android.os.Build.DEVICE
        val model: String = android.os.Build.MODEL
        val prod: String = android.os.Build.PRODUCT
        val devicedetail: String = device + model + prod + device + version
        try {
            val pInfo = packageManager.getPackageInfo(packageName, 0)
            appversion = pInfo.versionName

        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        ApiUtils.getAPIService(this@HomeActivity).update_doctor_status(
            SharedPrefs.getString(this@HomeActivity, "sessionid")!!,
            username,
            usertype,
            userData.id!!.toString(),
            "Online",
            appversion,
            devicedetail
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
                        }
                    } catch (e: Exception) {
                        //toast("Something went wrong, please try again later")
                    }
                }
            })


        //toast(PatientAppointment.roomid)
        /*if(intent.hasExtra("roomid")) {
            if (intent.getStringExtra("roomid").equals("")) {
toast("From App")
            } else {
                toast("From Notif")
            }

        }*/
    }

    public fun showBreakSelectionDialog(isLogOut: Boolean) {
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_doctor_status, null)
        val mBuilder = androidx.appcompat.app.AlertDialog.Builder(this)
            .setView(mDialogView)
        val mAlertDialog = mBuilder.show()
        mAlertDialog.setCancelable(false)
        var resonOfOffline = ""
        mDialogView.rgBreaks.setOnCheckedChangeListener { group, checkedId ->
            val radioButton = group.findViewById(checkedId) as RadioButton
            resonOfOffline = radioButton.text.toString()
        }
        mDialogView.btnConfirm.setOnClickListener {
            if (resonOfOffline != "") {
                if (isLogOut) {
                    val username = SharedPrefs.getString(this@HomeActivity, Constants.USERNAME)
                    if (username != null) {
                        offStatusToggleButton()
                        changeoofstatustooffline(resonOfOffline)
                        callingLogOutApi(username)
                    }
                } else {
                    offStatusToggleButton()
                    changeoofstatustooffline(resonOfOffline)
                }
                mAlertDialog.dismiss()
            } else {
                toast("Please select status")
            }
        }
        mDialogView.ivClose.setOnClickListener {
            mAlertDialog.dismiss()
        }
    }

    fun performLogOutWithoutAskingReason() {
        val username = SharedPrefs.getString(this@HomeActivity, Constants.USERNAME)
        if (username != null) {
            callingLogOutApi(username)
        }
    }

    private fun callingLogOutApi(username: String) {
        showProgressDialog("Logging out...")
        ApiUtils.getAPIService(this).Logout(username).enqueue(object : Callback<LogoutResonse> {
            override fun onFailure(call: Call<LogoutResonse>, t: Throwable) {
                Toast.makeText(
                    this@HomeActivity,
                    "Something went wrong, please try again later",
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun onResponse(call: Call<LogoutResonse>, response: Response<LogoutResonse>) {
                hideProgressDialog()
                SharedPrefs.removeUserData(this@HomeActivity)
                SharedPrefs.removeKey(this@HomeActivity, Constants.USERNAME)
                SharedPrefs.removeKey(this@HomeActivity, Constants.PASSWORD)
                SharedPrefs.save(this@HomeActivity, "TOKEN_REFRESHED", false)
                startActivity(Intent(this@HomeActivity, LoginActivity::class.java))
                finish()
            }
        })
    }

    private fun offStatusToggleButton() {
        progressDialog?.setMessage("Going Offline")
        progressDialog?.show()
        servicerunning = false
        oof_image_btn.setBackgroundResource(R.drawable.offlineico)
        KeepOnlineService.stopService(this)
    }

    override fun onResume() {
        super.onResume()
        try {
            var notifcount: Int = 0
            notifcount = SharedPrefs.getInt(this, "notifcount")
            Log.e("Notif Count ", "" + notifcount)
            if (notifcount != 0) {
                count.visibility = View.VISIBLE
                count.text = "" + notifcount
            } else {
                count.visibility = View.GONE
            }
        } catch (e: java.lang.Exception) {
        }
        val userData = SharedPrefs.getUserData(this@HomeActivity)

        username.text = userData!!.title + " " + userData!!.fname + " " + userData.lname
        name.text = userData!!.fname + " " + userData!!.lname
        if (userData.image_name != null || userData.image_name != "") {
            //Glide.with(this).load(userData.profile_picture).into(profileImage)

            if (pic == 0) {

                if (userData.image_name!!.startsWith("http")) {
                    Glide.with(this).load(userData.image_name).dontAnimate().into(avatar)
                    Glide.with(this).load(userData.image_name).into(user_avatar)
                } else {
                    Glide.with(this).load(
                        SharedPrefs.getString(
                            this@HomeActivity,
                            "filepath"
                        ) + userData!!.image_name
                    ).dontAnimate().into(avatar)
                    Glide.with(this).load(
                        SharedPrefs.getString(
                            this@HomeActivity,
                            "filepath"
                        ) + userData!!.image_name
                    ).into(user_avatar)
                }
            } else {

                if (userData.image_name!!.startsWith("http")) {
                    Glide.with(this).load(userData.image_name).dontAnimate().into(avatar)
                    Glide.with(this).load(userData.image_name).into(user_avatar)
                } else {
                    Glide.with(this).load(
                        SharedPrefs.getString(
                            this@HomeActivity,
                            "filepath"
                        ) + userData.image_name
                    ).dontAnimate().into(avatar)
                    Glide.with(this).load(
                        SharedPrefs.getString(
                            this@HomeActivity,
                            "filepath"
                        ) + userData.image_name
                    ).into(user_avatar)
                }
            }
            pic = 1
        }
//        if (ProfileActivity.bitmapImage != null) {
//          //  avatar.setImageBitmap(userData.image_name)
//            //user_avatar!!.setImageBitmap(ProfileActivity.bitmapImage)
//        }
    }

    fun init() {

        val userData = SharedPrefs.getUserData(this@HomeActivity)

        if (userData!!.image_name != null) {

            //val url : String = userData!!.image_name!!
            Log.e("profile ic is", url)


            //Picasso.get().load(userData!!.image_name).into(avatar)

        }
        //Check Token
        if (!SharedPrefs.getBoolean(applicationContext, "TOKEN_REFRESHED", false)) {

            FirebaseInstanceId.getInstance().instanceId
                .addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.w("HOME", "getInstanceId failed", task.exception)
                        return@OnCompleteListener
                    }

                    // Get new Instance ID token
                    val token = task.result?.token

                    // Log and toast
                    val msg = getString(R.string.msg_token_fmt, token)
                    Log.d("HOME", msg)
                    SharedPrefs.save(this, "Token", token!!)
                    sendTokenToServer(userData!!.id.toString(), token)
                })
        }


        menu_btn.setOnClickListener {
            drawer_layout.openDrawer(GravityCompat.START)
        }

        myAppointmentsContainer.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    AppointmentsActivity::class.java
                )
            )
        }
        messagesContainer.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    UserChatsActivity::class.java
                )
            )
        }
        telemedicine.setOnClickListener { startActivity(Intent(this, ProfileActivity::class.java)) }
        contact_us_btn.setOnClickListener {
            val url = "https://ezshifa.com/elements/contact-forms/"
            val i = Intent(Intent.ACTION_VIEW)
            i.setData(Uri.parse(url))
            startActivity(i)
        }

        myrating.setOnClickListener {
            //toast("Coming Soon")
            startActivity(Intent(this, MyRatings::class.java))
        }

        navigation_menu.layoutManager = LinearLayoutManager(this)
        homeNavigationRecyclerViewAdapter =
            HomeNavigationRecyclerViewAdapter(this, this@HomeActivity)
        navigation_menu.adapter = homeNavigationRecyclerViewAdapter


        user_avatar.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
        change_image_btn.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CAMERA),
                    PERMISSION_REQUEST_CAMERA
                )
            } else {
                val cameraIntent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(cameraIntent, CAMERA_REQUEST)
            }
        }

        Glide.with(this)
            .load(SharedPrefs.getString(this@HomeActivity, "filepath") + userData!!.image_name)
            .into(user_avatar)
        Glide.with(this)
            .load(SharedPrefs.getString(this@HomeActivity, "filepath") + userData!!.image_name)
            .dontAnimate().into(avatar)
    }


    private fun sendTokenToServer(userId: String, token: String) {
        val version: String = android.os.Build.VERSION.SDK
        val device: String = android.os.Build.DEVICE
        val model: String = android.os.Build.MODEL
        val prod: String = android.os.Build.PRODUCT
        val devicedetail: String = device + model + prod + device + version
        val deviceinfo: String = "android"
        val userData = SharedPrefs.getUserData(this@HomeActivity)
        val username = userData!!.username
        val usertype = "DOCTOR"
        var appversion: String = ""
        var currentDate: String = "";
        try {
            val pInfo = packageManager.getPackageInfo(packageName, 0)
            appversion = pInfo.versionName
            val sdf = SimpleDateFormat("yyyy/MM/dd hh:mm:ss")
            currentDate = sdf.format(Date())
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        ApiUtils.getAPIService(this).sendFirebaseToken(
            userId,
            deviceinfo,
            devicedetail,
            token,
            appversion,
            currentDate,
            SharedPrefs.getString(this@HomeActivity, "sessionid")!!,
            username!!,
            usertype
        )
            .enqueue(object : Callback<SendFirebaseTokenResponse> {
                override fun onFailure(call: Call<SendFirebaseTokenResponse>, t: Throwable) {
                    //Do nothing for now
                }

                override fun onResponse(
                    call: Call<SendFirebaseTokenResponse>,
                    response: Response<SendFirebaseTokenResponse>
                ) {
                    try {
                        if (response.isSuccessful && response.body()!!.data.message.equals("Successful!"))
                            SharedPrefs.save(applicationContext, "TOKEN_REFRESHED", true)

                    } catch (e: Exception) {
                        toast("Something went wrong, please try again later")
                    }
                }
            })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                var cameraIntent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode === CAMERA_REQUEST && resultCode === Activity.RESULT_OK) {
            val photo = data!!.extras!!.get("data") as Bitmap
            ProfileActivity.bitmapImage = photo
            updateDoctorProfile()

        }
    }


    fun updateDoctorProfile() {
        var image: MultipartBody.Part? = null

        if (ProfileActivity.bitmapImage != null) {
            val f = File(this.getCacheDir(), System.currentTimeMillis().toString() + "_image")

            f.createNewFile();

            //Convert bitmap to byte array
            val bitmap = ProfileActivity.bitmapImage
            val bos = ByteArrayOutputStream();
            bitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, bos)
            val bitmapdata = bos.toByteArray();

            //write the bytes in file
            val fos = FileOutputStream(f)
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
            val reqFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), f)
            image = MultipartBody.Part.createFormData("profile_image", f.name + ".jpg", reqFile)

        }

        val userData = SharedPrefs.getUserData(this)
        val doctorId = RequestBody.create("doctorid".toMediaTypeOrNull(), userData!!.id.toString())
        val fname = RequestBody.create("fname".toMediaTypeOrNull(), userData.fname.toString())
        val lname = RequestBody.create("lname".toMediaTypeOrNull(), userData.lname.toString())
        val specialty = RequestBody.create(
            "specialty".toMediaTypeOrNull(),
            userData.specialty.toString()
        )
        val facility = RequestBody.create(
            "facility".toMediaTypeOrNull(),
            userData.facility.toString()
        )
        val title = RequestBody.create("title".toMediaTypeOrNull(), userData.title.toString())
        val country = RequestBody.create("country".toMediaTypeOrNull(), userData.city.toString())
        val city = RequestBody.create("city".toMediaTypeOrNull(), userData.city.toString())
        val phone = RequestBody.create("phone".toMediaTypeOrNull(), userData.phone.toString())
        val experienceText = RequestBody.create(
            "experience".toMediaTypeOrNull(),
            userData.experience.toString()
        )
        val degreesText = RequestBody.create(
            "degrees".toMediaTypeOrNull(),
            userData.degrees.toString()
        )

        val sessionid = RequestBody.create(
            "sessionid".toMediaTypeOrNull(),
            SharedPrefs.getString(this, "sessionid")!!
        )
        val username = RequestBody.create(
            "username".toMediaTypeOrNull(),
            userData!!.username.toString()
        )
        val usertype = RequestBody.create("usertype".toMediaTypeOrNull(), "DOCTOR")

        ApiUtils.getAPIService(this@HomeActivity)
            .updateProfile(
                doctorId,
                fname,
                lname, /*specialty,*/
                facility,
                title,
                country,
                city,
                phone,
                image,
                experienceText,
                degreesText,
                sessionid,
                username,
                usertype
            )
            .enqueue(object : Callback<GetUpdateProfileResponse> {
                override fun onFailure(call: Call<GetUpdateProfileResponse>, t: Throwable) {
                    toast("Something went wrong. Couldn't update profile Image.")
                }

                override fun onResponse(
                    call: Call<GetUpdateProfileResponse>,
                    response: Response<GetUpdateProfileResponse>
                ) {
                    try {
                        if (response.isSuccessful && response.body() != null) {
                            toast("Profile Image Updated Successfully !")
                            avatar.setImageBitmap(ProfileActivity.bitmapImage)
                            user_avatar!!.setImageBitmap(ProfileActivity.bitmapImage)
                            // userData!!.image_name=
                        } else {
                            toast("Something went wrong. Couldn't update profile image.")
                        }

                    } catch (e: Exception) {
                        toast("Something went wrong, please try again later")
                    }
                }
            }
            )


    }

    fun showProgressDialog(message: String) {
        progressDialog?.setMessage(message)
        progressDialog?.setCancelable(false)
        progressDialog?.show()
    }

    fun hideProgressDialog() {
        if (progressDialog != null && progressDialog?.isShowing == true) {
            progressDialog?.hide()
            progressDialog?.dismiss()
        }
    }

    private fun changeoofstatus() {
        val userData = SharedPrefs.getUserData(this@HomeActivity)
        val username = userData!!.username
        val usertype = "DOCTOR"
        var appversion: String = ""
        val version: String = android.os.Build.VERSION.SDK
        val device: String = android.os.Build.DEVICE
        val model: String = android.os.Build.MODEL
        val prod: String = android.os.Build.PRODUCT
        val devicedetail: String = device + model + prod + device + version
        try {
            val pInfo = packageManager.getPackageInfo(packageName, 0)
            appversion = pInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        ApiUtils.getAPIService(this@HomeActivity).update_doctor_onoff_status(
            SharedPrefs.getString(this@HomeActivity, "sessionid")!!,
            username!!,
            usertype,
            userData.id!!.toString(),
            "1",
            ""
        )
            .enqueue(object : Callback<ForgotPasswordResponse> {
                override fun onFailure(call: Call<ForgotPasswordResponse>, t: Throwable) {
                    //Do nothing for now
                    progressDialog?.dismiss()
                }

                override fun onResponse(
                    call: Call<ForgotPasswordResponse>,
                    response: Response<ForgotPasswordResponse>
                ) {
                    progressDialog?.dismiss()
                    try {
                        if (response.isSuccessful) {
                            //onofftoggle.setText("Go Offline")
                            onofftoggle.setImageResource(R.drawable.iamonline)
                            servicerunning = true
                            oof_image_btn.setBackgroundResource(R.drawable.onlineico)
                            KeepOnlineService.startService(this@HomeActivity, "You are Online...")
                        }


                    } catch (e: Exception) {
                        toast("Error: Try again")
                    }
                }
            })
    }

    private fun changeoofstatustooffline(reason: String) {
        val userData = SharedPrefs.getUserData(this@HomeActivity)
        if (userData != null) {
            val username = userData.username
            val usertype = "DOCTOR"
            var appversion: String = ""
            val version: String = android.os.Build.VERSION.SDK
            val device: String = android.os.Build.DEVICE
            val model: String = android.os.Build.MODEL
            val prod: String = android.os.Build.PRODUCT
            val devicedetail: String = device + model + prod + device + version
            try {
                val pInfo = packageManager.getPackageInfo(packageName, 0)
                appversion = pInfo.versionName
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }
            ApiUtils.getAPIService(this@HomeActivity).update_doctor_onoff_status(
                SharedPrefs.getString(this@HomeActivity, "sessionid")!!,
                username!!,
                usertype,
                userData.id!!.toString(),
                "0",
                reason
            )
                .enqueue(object : Callback<ForgotPasswordResponse> {
                    override fun onFailure(call: Call<ForgotPasswordResponse>, t: Throwable) {
                        //Do nothing for now
                        progressDialog!!.dismiss()
                    }

                    override fun onResponse(
                        call: Call<ForgotPasswordResponse>,
                        response: Response<ForgotPasswordResponse>
                    ) {
                        try {
                            if (response.isSuccessful) {
                                progressDialog!!.dismiss()
                                //onofftoggle.setText("Go Online")
                                onofftoggle.setImageResource(R.drawable.iamoffline)
                                servicerunning = false
                                oof_image_btn.setBackgroundResource(R.drawable.offlineico)
                            }


                        } catch (e: Exception) {
                            toast("Error: Try again")
                        }
                    }
                })
        }

    }

    fun <T> Context.isMyServiceRunning(service: Class<T>): Boolean {
        return (getSystemService(ACTIVITY_SERVICE) as ActivityManager)
            .getRunningServices(Integer.MAX_VALUE)
            .any { it -> it.service.className == service.name }
    }
}

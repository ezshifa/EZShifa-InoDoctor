package com.app.ehealthaidoctor.ui.activities.profile

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.app.ehealthaidoctor.R
import com.app.ehealthaidoctor.models.responses.DoctorLoginResponse
import com.app.ehealthaidoctor.models.responses.GetUpdateProfileResponse
import com.app.ehealthaidoctor.models.responses.Profile
import com.app.ehealthaidoctor.network.ApiUtils
import com.app.ehealthaidoctor.ui.activities.HomeActivity
import com.app.ehealthaidoctor.utils.SharedPrefs
import com.bumptech.glide.Glide
import com.example.ehealthai.utils.Constants
import com.example.ehealthai.utils.toast
import com.vansuita.pickimage.bean.PickResult
import com.vansuita.pickimage.bundle.PickSetup
import com.vansuita.pickimage.dialog.PickImageDialog
import com.vansuita.pickimage.enums.EPickType
import com.vansuita.pickimage.listeners.IPickCancel
import com.vansuita.pickimage.listeners.IPickResult
import kotlinx.android.synthetic.main.activity_profile.*
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class ProfileActivity : AppCompatActivity() {

    var PERMISSION_REQUEST_CAMERA = 0
    var CAMERA_REQUEST = 1
    var userId: String? = null

    companion object {
        var bitmapImage: Bitmap? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

    }


    override fun onResume() {
        super.onResume()

        getDoctorProfile1()
        profile_scroll.visibility = View.GONE
        profile_progress.visibility = View.VISIBLE
    }

    fun init() {
        val userData = SharedPrefs.getUserData(this@ProfileActivity)
        if (!userData!!.phone.isNullOrEmpty()) {

            phone_num.setText("+"+userData.phone.toString())
        } else {

            phone_num.setText("Not available")
        }
        if (!userData!!.city.isNullOrEmpty()) {
            city.setText(userData.city.toString()+", "+userData.country)
        } else {
            city.setText("Not available")
        }
        if (!userData.experience.isNullOrEmpty()) {
            speciality_experience.setText(userData.experience)
        } else {
            speciality_experience.setText("Not available")
        }
        if (!userData.degrees.isNullOrEmpty()) {
            val degrees = userData.degrees

                certifications.setText(degrees)
        } else {
            certifications.setText("No certifications")
        }

        if (!userData.image_name.isNullOrEmpty()) {
            if(userData!!.image_name!!.startsWith("http"))
            {
                Glide.with(this).load(userData!!.image_name).dontAnimate().into(doctor_image)
                Glide.with(this).load( userData!!.image_name).into(doctor_image)
            }
            else {
                Glide.with(this).load(SharedPrefs.getString(this@ProfileActivity, "filepath") + userData!!.image_name).dontAnimate().into(doctor_image)
                Glide.with(this).load(SharedPrefs.getString(this@ProfileActivity, "filepath") + userData!!.image_name).into(doctor_image)
            }
           // Glide.with(this).load(userData.image_name).into(doctor_image)
        }

        profile_back_btn.setOnClickListener { onBackPressed() }


        edit_profile_btn.setOnClickListener {
            startActivity(Intent(this, EditProfileActivity::class.java))
        }

        change_image_btn.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), PERMISSION_REQUEST_CAMERA)
            } else {
                /*val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(cameraIntent, CAMERA_REQUEST)*/
                PickImageDialog.build( PickSetup().setTitle("Choose One")
                        .setCameraButtonText("Take Photo")
                        .setGalleryButtonText("Pick from gallery")
                        .setIconGravity(Gravity.LEFT)
                        .setButtonOrientation(LinearLayout.HORIZONTAL)
                        .setSystemDialog(false))
                        .setOnPickResult(object : IPickResult {
                            override fun  onPickResult(r: PickResult) {

                                //profile_image.setImageBitmap(photo)
                                try {
                                    bitmapImage = r.getBitmap()
                                    updateDoctorProfile()
                                }
                                catch (e: java.lang.Exception){
                                    toast("Something went wrong, please try again later")
                                }
                            }
                        })
                        .setOnPickCancel(object : IPickCancel {
                            override fun onCancelClick() {

                            }
                        }).show(getSupportFragmentManager());
            }
        }

        profile_scroll.visibility = View.VISIBLE
        profile_progress.visibility = View.GONE

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                /*var cameraIntent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(cameraIntent, CAMERA_REQUEST);*/
                PickImageDialog.build( PickSetup().setTitle("Choose One")
                        .setCameraButtonText("Take Photo")
                        .setGalleryButtonText("Pick from gallery")
                        .setIconGravity(Gravity.LEFT)
                        .setButtonOrientation(LinearLayout.HORIZONTAL)
                        .setSystemDialog(false))
                        .setOnPickResult(object : IPickResult {
                            override fun  onPickResult(r: PickResult) {

                                //profile_image.setImageBitmap(photo)
                                try {
                                    bitmapImage = r.getBitmap()
                                    updateDoctorProfile()
                                }
                                catch (e: java.lang.Exception){
                                    toast("Something went wrong, please try again later")
                                }
                            }
                        })
                        .setOnPickCancel(object : IPickCancel {
                            override fun onCancelClick() {

                            }
                        }).show(getSupportFragmentManager());
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode === CAMERA_REQUEST && resultCode === Activity.RESULT_OK) {
            val photo = data!!.getExtras()!!.get("data") as Bitmap
            bitmapImage = photo
            updateDoctorProfile()
        }
    }

    fun getDoctorProfile1() {
        val userData = SharedPrefs.getUserData(this@ProfileActivity)
        var titledr =""
        if(userData!!.title!!.equals("Select Title") || userData!!.title!!.isNullOrEmpty()){

        }
        else{
            titledr=userData!!.title!!
        }
        doctor_name.setText(titledr+" "+userData!!.fname + " " + userData.lname)
        doctor_desc.setText(userData.specialty)
        //facility.text = userData.facility
        if(!userData.city.equals("")){
        city.setText(userData.city)
        }
        userId = userData.id.toString()

        val username=userData!!.username
        val usertype="DOCTOR"
        Log.e("USERID ",userId!!)
        Log.e("SESSIONID  ",SharedPrefs.getString(this@ProfileActivity, "sessionid")!!)
        ApiUtils.getAPIService(this).getDoctorProfile(userId!!,SharedPrefs.getString(this@ProfileActivity, "sessionid")!!,username!!,usertype).enqueue(object : Callback<DoctorLoginResponse> {
            override fun onFailure(call: Call<DoctorLoginResponse>, t: Throwable) {

                init()
            }

            override fun onResponse(call: Call<DoctorLoginResponse>, response: Response<DoctorLoginResponse>) {
             try{
                 if (response.body()!!.statuscode.equals(404)) {
                     toast(response.body()!!.data.error!!)
                     edit_profile_btn.visibility=View.INVISIBLE
                 }
                 else if (response.body() != null && response.body()!!.statuscode == 200) {
                     edit_profile_btn.visibility=View.VISIBLE
                     val userData2 = SharedPrefs.getUserData(this@ProfileActivity)
                    if (response.body()!!.data.profile!!.size > 0) {
                        if (response.body()!!.data.profile!!.get(0).image_name != null)
                            Log.e("",response.body()!!.data.profile!!.get(0).image_name!!)
                            userData2!!.image_name = SharedPrefs.getString(this@ProfileActivity, "filepath")+  response.body()!!.data.profile!!.get(0).image_name
                            userData!!.image_name = SharedPrefs.getString(this@ProfileActivity, "filepath")+  response.body()!!.data.profile!!.get(0).image_name
                        if (!response.body()!!.data.profile!!.get(0).country.isNullOrEmpty())
                            userData.country = response.body()!!.data.profile!!.get(0).country

                    }
                     if(response.body()!!.data.sessionid.isNullOrEmpty()||response.body()!!.data.sessionid.equals(0))
                     {
                         userData2!!.fname = userData2!!.fname
                         userData2.lname = userData2!!.lname
                         // userData.specialty = speciality
                         userData2.facility = userData2!!.facility
                         userData2.title = userData2!!.title
                         userData2.city = userData2!!.city
                         userData2.country = userData2!!.country
                         userData2.phone = userData2!!.phone
                         userData2.degrees = userData2!!.degrees
                         userData2.experience = userData2!!.experience
                         toast(userData2!!.image_name!!)

                         SharedPrefs.setUserData(this@ProfileActivity, userData2!!)
                     }
                     else {
                         SharedPrefs.setUserData(this@ProfileActivity, userData)

                     }
                }
                init()

            }catch(e:Exception){
                toast("Something went wrong, please try again later")
            }
            }
        })
    }


    fun updateDoctorProfile() {

        profile_scroll.visibility = View.GONE
        profile_progress.visibility = View.VISIBLE

        var image: MultipartBody.Part? = null

        if (bitmapImage != null) {
            val f = File(this.getCacheDir(), System.currentTimeMillis().toString() + "_image")

            f.createNewFile();

            //Convert bitmap to byte array
            val bitmap = bitmapImage
            val bos = ByteArrayOutputStream();
            bitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, bos)
            val bitmapdata = bos.toByteArray();

            //write the bytes in file
            val fos = FileOutputStream(f)
            fos.write(bitmapdata)
            fos.flush()
            fos.close()

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
            SharedPrefs.getString(this@ProfileActivity, "sessionid")!!
        )
        val username=RequestBody.create(
            "username".toMediaTypeOrNull(),
            userData!!.username.toString()
        )
        val usertype=RequestBody.create("usertype".toMediaTypeOrNull(), "DOCTOR")

        ApiUtils.getAPIService(this@ProfileActivity)
            .updateProfile(doctorId, fname, lname, /*specialty,*/ facility, title, country, city, phone, image, experienceText, degreesText,sessionid,username!!,usertype)
            .enqueue(object : Callback<GetUpdateProfileResponse> {
                override fun onFailure(call: Call<GetUpdateProfileResponse>, t: Throwable) {
                    toast("Something went wrong. Couldn't update profile Image.")
                }

                override fun onResponse(call: Call<GetUpdateProfileResponse>, response: Response<GetUpdateProfileResponse>) {
              try{      if (response.isSuccessful && response.body() != null) {
                        toast("Profile Image Updated Successfully !")
                        getDoctorProfile1()
                    } else {
                        toast("Something went wrong. Couldn't update profile image.")
                    }

                }catch(e:Exception){
                    toast("Something went wrong, please try again later")
                }
                }
            }
            )


    }

}

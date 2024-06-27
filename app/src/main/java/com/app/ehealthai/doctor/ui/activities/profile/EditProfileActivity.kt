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
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.app.ehealthai.models.local.User
import com.app.ehealthai.network.firebase.FirebaseUtils
import com.app.ehealthaidoctor.R
import com.app.ehealthaidoctor.models.responses.*
import com.app.ehealthaidoctor.network.ApiUtils
import com.app.ehealthaidoctor.ui.dialogs.RecordAudioDialog
import com.app.ehealthaidoctor.utils.SharedPrefs
import com.bumptech.glide.Glide
import com.example.ehealthai.utils.toast
import com.vansuita.pickimage.bean.PickResult
import com.vansuita.pickimage.bundle.PickSetup
import com.vansuita.pickimage.dialog.PickImageDialog
import com.vansuita.pickimage.listeners.IPickCancel
import com.vansuita.pickimage.listeners.IPickResult
import kotlinx.android.synthetic.main.activity_edit_profile.*
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
import kotlin.math.ln

class EditProfileActivity : AppCompatActivity() {

    var countriesList: ArrayList<String> = ArrayList()
    var countryCityMap: Map<String, List<CityData>> = HashMap()
    var citiesList: ArrayList<String> = ArrayList()
    var cityMap: HashMap<String, String> = HashMap()
    var dataAdapter: ArrayAdapter<String>? = null
    var cityDataAdapter: ArrayAdapter<String>? = null

    var facilityMap: HashMap<String, String> = HashMap()
    var facilityList: ArrayList<String> = ArrayList()
    var facilityAdapter: ArrayAdapter<String>? = null

    var userData: Profile? = null
    var specialityMap: HashMap<String, String> = HashMap()
    var specialityList: ArrayList<String> = ArrayList()
    var specialityAdapter: ArrayAdapter<String>? = null

    var PERMISSION_REQUEST_CAMERA = 0
    var CAMERA_REQUEST = 1

    companion object {
        var updatedUserImage: String? = null
        var imageFile: File? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        init()
    }

    fun init() {
        initSpinners()


        userData = SharedPrefs.getUserData(this)

        edit_fName_edit.setText(userData!!.fname)
        edit_lName_edit.setText(userData!!.lname)

        if (userData!!.phone != null || userData!!.phone != "") {
            edit_phone_edit.setText(userData!!.phone)
        }
        if (!userData!!.experience.isNullOrEmpty() || !userData!!.experience!!.startsWith(" ")) {
            experience_edit.setText(userData!!.experience)
        }
        if (!userData!!.degrees.isNullOrEmpty()  || !userData!!.degrees!!.startsWith(" ")) {
            degree_edit.setText(userData!!.degrees!!)
        }

        if (!userData!!.image_name.isNullOrEmpty() ) {
           // Glide.with(this).load(userData!!.image_name).into(edit_profile_profile_image)
            if(userData!!.image_name!!.startsWith("http"))
            {
                Glide.with(this).load(userData!!.image_name).dontAnimate().into(edit_profile_profile_image)
                Glide.with(this).load( userData!!.image_name).into(edit_profile_profile_image)
            }
            else {
                Glide.with(this).load(SharedPrefs.getString(this@EditProfileActivity, "filepath") + userData!!.image_name).dontAnimate().into(edit_profile_profile_image)
                Glide.with(this).load(SharedPrefs.getString(this@EditProfileActivity, "filepath") + userData!!.image_name).into(edit_profile_profile_image)
            }

        }

        if(userData!!.title.equals("Mr"))
        {
            edit_title_edit.setSelection(1)
        }
        else if(userData!!.title.equals("Mrs"))
        {
            edit_title_edit.setSelection(2)
        }
        else if(userData!!.title.equals("Miss"))
        {
            edit_title_edit.setSelection(3)
        }
        else if(userData!!.title.equals("Ms"))
        {
            edit_title_edit.setSelection(4)
        }
        else if(userData!!.title.equals("Dr"))
        {
            edit_title_edit.setSelection(5)
        }
        else if(userData!!.title.equals("Prof"))
        {
            edit_title_edit.setSelection(6)
        }
        else if(userData!!.title.equals("Sir"))
        {
            edit_title_edit.setSelection(7)
        }
        else
        {
            edit_title_edit.setSelection(0)
        }

        edit_profile_profile_image.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.CAMERA
                    ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), PERMISSION_REQUEST_CAMERA)
            } else {
                /*  val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
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
                                    ProfileActivity.bitmapImage = r.getBitmap()
                                    edit_profile_profile_image.setImageBitmap(ProfileActivity.bitmapImage)
                                    updateDoctorProfileImage()
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


        edit_profile_change_image_btn.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), PERMISSION_REQUEST_CAMERA)
            } else {
              /*  val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
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
                                    ProfileActivity.bitmapImage = r.getBitmap()
                                    edit_profile_profile_image.setImageBitmap(ProfileActivity.bitmapImage)
                                    updateDoctorProfileImage()
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

        edit_profile_back_btn.setOnClickListener { onBackPressed() }
        countriesList.add("Select a country")
        citiesList.add("Select a city")
        getCities()
        //getSpecilities()
       // getFacilities()


        save_profile_btn.setOnClickListener {

            when {
                edit_fName_edit.text.toString().isEmpty() -> {
                    edit_fName_edit.error = "Please enter your first name"
                }
                edit_lName_edit.text.toString().isEmpty() -> {
                    edit_lName_edit.error = "Please enter your last name"
                }
                edit_phone_edit.text.toString().isEmpty() -> {
                    edit_phone_edit.error = "Please enter your phone name"
                }
                experience_edit.text.toString().isEmpty() -> {
                    experience_edit.error = "Please enter your experience details"
                }
                degree_edit.text.toString().isEmpty() -> {
                    degree_edit.error = "Please enter your degrees"
                }
                     edit_title_edit.selectedItem.toString().equals("Select Title") -> {
                         toast("Please Select Title")
                     }

                degree_edit.text.toString().trim().isEmpty() -> {
                    degree_edit.error = "Please enter your degrees"
                }
               experience_edit.text.toString().trim().isEmpty() -> {
                   experience_edit.error = "Please enter your experience details"

               }
//experience_edit.text.toString().startsWith(" ") &&
//        !experience_edit.text.toString().matches(" ^[a-zA-Z]*$".toRegex())  ->{
//
//    experience_edit.error = "Please enter your experience details"
//                }

                city_spinner.selectedItem.toString().toLowerCase().equals("select a city") -> {
                    toast("Please Select City")
                }
                /*facility_spinner.selectedItem.toString().toLowerCase().equals("select a facility") -> {
                    toast("Please Select Facility")
                }*/
               /* speciality_spinner.selectedItem.toString().toLowerCase().equals("select a speciality") -> {
                    toast("Please Select Speciality")
                }*/

                else -> {

                    Log.e("print res", experience_edit.text.toString().trim())

                    var selectedcity=""
                    var selectedcountry=""
                    if (countryCityMap.containsKey(country.selectedItem.toString())) {
                        val cityDataList = countryCityMap.get(country.selectedItem.toString())
                        for (i in 0 until cityDataList!!.size) {

if(cityDataList.get(i).title.equals(city_spinner.selectedItem.toString()))
{
    selectedcity=cityDataList.get(i).option_id!!
    selectedcountry=cityDataList.get(i).country_code!!
    //toast(selectedcountry)
}

                    }}

                    var selectedspeciality=""
                   //toast(specialityMap.getValue(speciality_spinner.selectedItem.toString()))
                        progressBar.visibility = View.VISIBLE
                        // cityMap(city_spinner.selectedItem.toString()
                        val title = edit_title_edit.selectedItem.toString()
                        updateDoctorProfile(
                                edit_fName_edit.text.toString(),
                                edit_lName_edit.text.toString(),
                                edit_phone_edit.text.toString(),
                                experience_edit.text.trim().toString(),
                                degree_edit.text.toString(),
                                selectedcountry,
                                selectedcity,
                                "",
                                "",
                                title
                        )

                }
            }


        }
    }
    fun updateDoctorProfileImage() {



        var image: MultipartBody.Part? = null
        var imagename: String = ""
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
            imagename=f.name

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
        val username=RequestBody.create(
            "username".toMediaTypeOrNull(),
            userData!!.username.toString()
        )
        val usertype=RequestBody.create("usertype".toMediaTypeOrNull(), "DOCTOR")

        ApiUtils.getAPIService(this)
                .updateProfile(doctorId, fname, lname, /*specialty,*/ facility, title, country, city, phone, image, experienceText, degreesText,sessionid,username!!,usertype)
                .enqueue(object : Callback<GetUpdateProfileResponse> {
                    override fun onFailure(call: Call<GetUpdateProfileResponse>, t: Throwable) {
                        toast("Something went wrong. Couldn't update profile Image.")
                    }

                    override fun onResponse(call: Call<GetUpdateProfileResponse>, response: Response<GetUpdateProfileResponse>) {
                        try{      if (response.isSuccessful && response.body() != null) {
                            toast("Profile Image Updated Successfully !")
                            //response.body().data.message()

                            userData.image_name = imagename + ".jpg"
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

    fun updateDoctorProfile(
        firstName: String,
        lastName: String,
        phoneNumber: String,
        experience: String,
        degrees: String,
        countryName: String,
        cityName: String,
        facilityName: String,
        speciality: String,
        title: String
    ) {
        var image: MultipartBody.Part? = null

        if (ProfileActivity.bitmapImage != null) {
            val f = File(this.getCacheDir(), System.currentTimeMillis().toString() + "_image")

            f.createNewFile()

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
            image = MultipartBody.Part.createFormData("profile_image", f.name+".jpg", reqFile)

        }
        val doctorId = RequestBody.create("doctorid".toMediaTypeOrNull(), userData!!.id.toString())
        val fname = RequestBody.create("fname".toMediaTypeOrNull(), firstName)
        val lname = RequestBody.create("lname".toMediaTypeOrNull(), lastName)
        //val specialty = RequestBody.create(MediaType.parse("specialty"), speciality)
        val facility = RequestBody.create("facility".toMediaTypeOrNull(), facilityName)
        val title1 = RequestBody.create("title".toMediaTypeOrNull(), title)
        val country = RequestBody.create("country".toMediaTypeOrNull(), countryName)
        val city = RequestBody.create("city".toMediaTypeOrNull(), cityName)
        val phone = RequestBody.create("phone".toMediaTypeOrNull(), phoneNumber)
        val experienceText = RequestBody.create("experience".toMediaTypeOrNull(), experience)
        val degreesText = RequestBody.create("degrees".toMediaTypeOrNull(), degrees)
       // val usertitle=RequestBody.create(MediaType.parse("text/plain"), title.toString())
        val userData=SharedPrefs.getUserData(this@EditProfileActivity)
        val sessionid = RequestBody.create(
            "sessionid".toMediaTypeOrNull(),
            SharedPrefs.getString(this@EditProfileActivity, "sessionid")!!
        )
        val username=RequestBody.create(
            "username".toMediaTypeOrNull(),
            userData!!.username.toString()
        )
        val usertype=RequestBody.create("usertype".toMediaTypeOrNull(), "DOCTOR")
        ApiUtils.getAPIService(this@EditProfileActivity)
            .updateProfile(doctorId, fname, lname, /*specialty,*/ facility, title1, country, city, phone, image, experienceText, degreesText,sessionid!!,username!!,usertype)
            .enqueue(object : Callback<GetUpdateProfileResponse> {
                override fun onFailure(call: Call<GetUpdateProfileResponse>, t: Throwable) {
                    toast("Something went wrong. Couldn't update profile.")
                    Log.e("ERRORR ",t.toString())
                }

                override fun onResponse(call: Call<GetUpdateProfileResponse>, response: Response<GetUpdateProfileResponse>) {
                 try{   if (response.isSuccessful && response.body() != null) {
                        toast("Profile Updated Successfully !")
                        progressBar.visibility = View.INVISIBLE
                        val userData = SharedPrefs.getUserData(this@EditProfileActivity)
                        userData!!.fname = firstName
                        userData.lname = lastName
                       // userData.specialty = speciality
                        userData.facility = facilityName
                        userData.title = title
                        userData.city = city_spinner.selectedItem.toString()
                        userData.phone = phoneNumber
                        userData.degrees = degrees
                        userData.experience = experience

                        SharedPrefs.setUserData(this@EditProfileActivity, userData)
                        finish()
                    } else {
                        progressBar.visibility = View.INVISIBLE
                        toast("Something went wrong. Couldn't update profile.")
                    }
                }
                catch(e: Exception)
                {
                    progressBar.visibility=View.INVISIBLE
                    toast("Something went wrong, please try again later")
                }
                }
            }
            )


    }

    fun initSpinners() {
       // countriesList.add("Select a country")
        citiesList.add("Select a city")
        cityMap.put("city", "")


        dataAdapter = ArrayAdapter<String>(this@EditProfileActivity, android.R.layout.simple_spinner_item, countriesList)
        dataAdapter!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        country.adapter = dataAdapter

        cityDataAdapter = ArrayAdapter(this@EditProfileActivity, android.R.layout.simple_spinner_item, citiesList)
        cityDataAdapter!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        city_spinner.adapter = cityDataAdapter
    }

    fun getCities() {
        val userData=SharedPrefs.getUserData(this@EditProfileActivity)
        val username=userData!!.username
        val usertype="DOCTOR"
        ApiUtils.getAPIService(this@EditProfileActivity).getCities(SharedPrefs.getString(this@EditProfileActivity, "sessionid")!!,username!!,usertype).enqueue(object : Callback<GetCitiesResponse> {
            override fun onFailure(call: Call<GetCitiesResponse>, t: Throwable) {
                toast("Something went wrong, please try again later")
            }

            override fun onResponse(call: Call<GetCitiesResponse>, response: Response<GetCitiesResponse>) {
           try{
               if (response.isSuccessful && response.body() != null && !response.body()!!.data.isNullOrEmpty()) {
                    countriesList.clear()

                    countriesList.add("Select a country")
                    countriesList.addAll(response.body()!!.data.keys)
                    countryCityMap = response.body()!!.data


                    dataAdapter = ArrayAdapter(this@EditProfileActivity, android.R.layout.simple_spinner_item, countriesList)
                    dataAdapter!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    country.adapter = dataAdapter

                    country?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onNothingSelected(parent: AdapterView<*>?) {

                        }

                        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                            city_spinner.isEnabled = true

                            if (countryCityMap.containsKey(country.selectedItem.toString())) {
                                val cityDataList = countryCityMap.get(country.selectedItem.toString())
                                citiesList.clear()
                                cityMap.clear()
                                for (i in 0 until cityDataList!!.size) {
                                    citiesList.add(cityDataList.get(i).title)
                                    cityMap.put(cityDataList.get(i).title, cityDataList.get(i).option_id)

                                }

                                cityDataAdapter = ArrayAdapter(this@EditProfileActivity, android.R.layout.simple_spinner_item, citiesList)
                                cityDataAdapter!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                                city_spinner.adapter = cityDataAdapter
                            } else {
                                citiesList.clear()
                                cityMap.clear()
                                citiesList.add("Select a city")
                                cityMap.put("city","")

                                cityDataAdapter = ArrayAdapter(this@EditProfileActivity, android.R.layout.simple_spinner_item, citiesList)
                                cityDataAdapter!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                                city_spinner.adapter = cityDataAdapter
                            }
                        }

                    }


                } else {
                    toast("Something went wrong, please try again later")
                }
            }catch(e:Exception){
                toast("Something went wrong, please try again later")
            }
            }
        })

    }

    fun getSpecilities() {
        val userData=SharedPrefs.getUserData(this@EditProfileActivity)
        val username=userData!!.username
        val usertype="DOCTOR"
        ApiUtils.getAPIService(this@EditProfileActivity).getSpecialities(SharedPrefs.getString(this@EditProfileActivity, "sessionid")!!,username!!,usertype).enqueue(object : Callback<GetSpecialityResponse> {
            override fun onFailure(call: Call<GetSpecialityResponse>, t: Throwable) {
                toast("Error Something went wrong. Couldn't get facilities list.")
            }

            override fun onResponse(call: Call<GetSpecialityResponse>, response: Response<GetSpecialityResponse>) {
             try{   if (response.isSuccessful && response.body() != null && !response.body()!!.data.isNullOrEmpty()) {
                    specialityList.clear()
                    specialityMap.clear()

                    specialityList.add("Select a Speciality")
                    for (facility in response.body()!!.data) {
                        specialityList.add(facility.title)
                        specialityMap.put(facility.title,facility.option_id)
                    }


                    specialityAdapter = ArrayAdapter(this@EditProfileActivity, android.R.layout.simple_spinner_item, specialityList)
                    specialityAdapter!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    speciality_spinner.adapter = specialityAdapter


                    for (i in 0 until specialityList.size) {
                        if (specialityList.get(i).equals(userData!!.specialty)) {
                            speciality_spinner.setSelection(i)
                        }
                    }

                } else {
                    toast("Something went wrong. Couldn't get countries list.")
                }

            }catch(e:Exception){
                toast("Something went wrong, please try again later")
            }
            }
        })

    }

   /* fun getFacilities() {
        ApiUtils.getAPIService(this@EditProfileActivity).getFacilities().enqueue(object : Callback<GetFacilitiesResponse> {
            override fun onFailure(call: Call<GetFacilitiesResponse>, t: Throwable) {
                toast("Something went wrong. Couldn't get countries list.")
            }

            override fun onResponse(call: Call<GetFacilitiesResponse>, response: Response<GetFacilitiesResponse>) {
             try{   if (response.isSuccessful && response.body() != null && !response.body()!!.data.isNullOrEmpty()) {
                    facilityList.clear()
                    facilityMap.clear()

                    facilityList.add("Select a facility")
                    for (facility in response.body()!!.data) {
                        facilityList.add(facility.name)
                        facilityMap.put(facility.id.toString(), facility.name)
                    }

                    facilityAdapter = ArrayAdapter(this@EditProfileActivity, android.R.layout.simple_spinner_item, facilityList)
                    facilityAdapter!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    facility_spinner.adapter = facilityAdapter


                    for (i in 0 until facilityList.size) {
                        if (facilityList.get(i).equals(userData!!.facility)) {
                            facility_spinner.setSelection(i)
                        }
                    }
                } else {
                    toast("Something went wrong. Couldn't get countries list.")
                }
            }catch(e:Exception){
                toast("Error: Try again")
            }
            }
        })
    }*/

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                /*val cameraIntent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
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
                                    ProfileActivity.bitmapImage = r.getBitmap()
                                    edit_profile_profile_image.setImageBitmap(ProfileActivity.bitmapImage)
                                    updateDoctorProfileImage()
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
            ProfileActivity.bitmapImage = photo
            edit_profile_profile_image.setImageBitmap(photo)
        }
    }

}

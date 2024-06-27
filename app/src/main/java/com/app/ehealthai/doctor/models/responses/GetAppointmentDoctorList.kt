package com.app.ehealthaidoctor.models.responses

data class GetAppointmentDoctorList(
    val data: AppointmentDoctorListData?,
    val statuscode: Int
)

data class AppointmentDoctorListData(
    val list: List<AppointmentDoctorData>?,
    val message: String,
    val error:String
)

data class AppointmentDoctorData(
    val DOB: String?,
    val apptdate: String,
    val apptid: Int,
    val appttime: String,
    val description: String,
    val fname: String,
    val cnic:String,
    val id: Int,
    val lname: String,
    val pc_duration: Int,
    val pc_appt_type: String,
    val doctor_notes:  String,
    val roomid:String,
    val apptstatus:  String,
    val profile_image: String,
    val email:String,
    val age:String,
    val phone_cell:String,
    val registered_from:String,
    val sex:String

)
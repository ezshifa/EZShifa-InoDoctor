package com.example.ehealthai.models.responses

data class GetDoctorsResponse(
    val data: List<DoctorData>?,
    val message: String,
    val success: Boolean
)

data class DoctorData(
    val city: String,
    val description: String,
    val facility_name: String,
    val fname: String,
    val lname: String,
    val physician_type: String,
    val speciality: String,
    val profile_image: String?
)
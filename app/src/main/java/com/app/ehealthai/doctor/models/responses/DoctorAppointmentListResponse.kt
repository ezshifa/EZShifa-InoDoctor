package com.app.ehealthaidoctor.models.responses

data class DoctorAppointmentListResponse(
    val data: DoctorAppointmentListResponseData,
    val statuscode: Int
)

data class DoctorAppointmentListResponseData(
    val list: List<AppointmentListData>,
    val message: String
)

data class AppointmentListData(
    val DOB: String,
    val apptdate: String,
    val appttime: String,
    val city: String,
    val county: String,
    val description: String?,
    val fname: String,
    val id: Int,
    val lname: String,
    val phone_cell: String,
    val phone_contact: String,
    val sex: String
)
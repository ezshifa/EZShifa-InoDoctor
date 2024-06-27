package com.app.ehealthai.doctor.models.responses

data class MakeAppointmentResponse(
        val data: AppointmentData,
        val statuscode: Int
)

data class AppointmentData(
        val message: String,
        val appointment_id:String

)

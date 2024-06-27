package com.app.ehealthaidoctor.models.responses

data class CancelAppointmentResponse(
    val data: CancelAppointmentData,
    val statuscode: Int
)

data class CancelAppointmentData(
    val message: String
)
package com.app.ehealthaidoctor.models.responses

data class CloseAppointmentResponse(
    val data: CloseAppointmentData,
    val statuscode: Int
)

data class CloseAppointmentData(
    val message: String
)
package com.app.ehealthai.doctor.models.responses

data class DoctorStatusResponse(
    val data: Doctorstatus?,
    val statuscode: Int
)

data class Doctorstatus(
    val docstatus: String

)

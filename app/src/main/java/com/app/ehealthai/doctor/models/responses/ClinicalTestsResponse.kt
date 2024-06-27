package com.app.ehealthai.doctor.models.responses

data class ClinicalTestsResponse(
    val data: cldat?,
    val statuscode: Int
)

data class cldat(
    val ctests: List<ctdata>

)

data class ctdata(
    val test_name: String,
    val test_type: String,
    val id: String



)
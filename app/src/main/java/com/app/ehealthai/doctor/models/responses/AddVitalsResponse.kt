package com.app.ehealthaidoctor.models.responses

data class AddVitalsResponse(
    val data: AddVitalsData,
    val statuscode: Int
)

data class AddVitalsData(
    val message: String
)
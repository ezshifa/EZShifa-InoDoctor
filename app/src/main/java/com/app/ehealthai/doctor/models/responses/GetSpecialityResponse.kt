package com.app.ehealthaidoctor.models.responses

data class GetSpecialityResponse(
    val data: List<SpecialityData>,
    val message: String,
    val success: Boolean
)

data class SpecialityData(
    val option_id: String,
    val title: String
)
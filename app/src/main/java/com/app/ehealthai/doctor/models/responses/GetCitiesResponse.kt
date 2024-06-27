package com.app.ehealthaidoctor.models.responses

data class GetCitiesResponse(
    val data: Map<String,List<CityData>>,
    val message: String,
    val success: Boolean
)

data class CityData(
    val country: String,
    val option_id: String,
    val title: String,
    val country_code: String
)
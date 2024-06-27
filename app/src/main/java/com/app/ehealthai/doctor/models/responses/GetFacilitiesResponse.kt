package com.app.ehealthaidoctor.models.responses

data class GetFacilitiesResponse(
    val data: List<Facility>,
    val message: String,
    val success: Boolean
)

data class Facility(
    val city: String,
    val country_code: String,
    val id: Int,
    val name: String,
    val phone: String,
    val postal_code: String,
    val street: String
)
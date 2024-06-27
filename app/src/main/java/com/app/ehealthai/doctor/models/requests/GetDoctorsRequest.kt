package com.example.ehealthai.models.requests


data class GetDoctorsRequest(
    val cities: List<String>,
    val facilities: List<String>?,
    val specialities: List<String>
)
package com.app.ehealthai.doctor.models.responses

class LastapptPatientMedResponse (
    val data: vdData?
    )

data class vdData(
    val list: List<meddata>?
)


data class meddata(
    val Medicine: String?,
    val Qty: String?,
    val Usage: String?
)

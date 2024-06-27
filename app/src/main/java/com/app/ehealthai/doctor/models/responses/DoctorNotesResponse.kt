package com.app.ehealthai.doctor.models.responses

data class DoctorNotesResponse(
        val data: dn?,
        val statuscode: Int
)

data class dn(
        val documents: List<dnotes>

)

data class dnotes(
        val doctor_notes: String



)
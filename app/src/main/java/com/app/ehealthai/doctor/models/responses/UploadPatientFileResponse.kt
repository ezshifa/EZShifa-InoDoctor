package com.app.ehealthaidoctor.models.responses

data class UploadPatientFileResponse(
    val data: UploadPatientData,
    val statuscode: Int
)

data class UploadPatientData(
    val message: String
)
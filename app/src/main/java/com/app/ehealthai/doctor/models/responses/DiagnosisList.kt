package com.app.ehealthai.doctor.models.responses

data class DiagnosisList (
    val data: DiagnosisData?,
    val statuscode:Int
)

data class DiagnosisData(
    val data: List<diagdata>?
)


data class diagdata(
    val id: Int?,
    val diagnosis_name: String?,
    val diagnosis_description: String?
)
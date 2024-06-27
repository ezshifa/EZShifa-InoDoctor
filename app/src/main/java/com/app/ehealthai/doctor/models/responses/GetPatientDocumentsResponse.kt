package com.app.ehealthaidoctor.models.responses

data class GetPatientDocumentsResponse(
    val data: PatientDocumentsData,
    val statuscode: Int
)

data class PatientDocumentsData(
    val documents: List<Document>?,
    val error:String?
)

data class Document(
    val create_date: String,
    val doc_type: String,
    val facility: String,
    val file_name: String,
    val file_path: String,
    val filetitle: String,
    val id: Int,
    val pid: Int
)
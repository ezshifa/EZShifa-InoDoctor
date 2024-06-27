package com.app.ehealthai.doctor.models.responses


data class RoomDataResponse(
        val data: datalist,
        val statuscode: Int
)
data class datalist(
        val list: list,
        val message: String
)
data class list(
        val doc_name: String,
        val doc_id: Int,
        val patient_id: Int,
        val patient_name: String,
        val apptid: Int

)
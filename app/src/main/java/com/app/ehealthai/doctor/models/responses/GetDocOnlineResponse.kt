package com.app.ehealthai.doctor.models.responses

data class GetDocOnlineResponse(
    val data: DocOnlinedata?,
    val statuscode: Int
)

data class DocOnlinedata(
    val onlinestatus: String

)
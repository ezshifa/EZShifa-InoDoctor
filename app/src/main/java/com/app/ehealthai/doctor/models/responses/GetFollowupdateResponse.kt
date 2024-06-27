package com.app.ehealthai.doctor.models.responses

data class GetFollowupdateResponse(
    val data: Followupdata?,
    val statuscode: Int
)

data class Followupdata(
    val followup_date: String

)
package com.app.ehealthai.doctor.models.responses

data class SiteLangResponse(
    val data: dat?,
    val statuscode: Int
)

data class dat(
    val list: List<sldata>

)

data class sldata(
    val macaddress: String,
    val site_name: String,
    val pc_pid: String,
    val fname: String,
    val lname: String,
    val language: String,
    val projectid: String,
    val project_name: String,
)
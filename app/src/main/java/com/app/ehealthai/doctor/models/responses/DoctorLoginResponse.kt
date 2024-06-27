package com.app.ehealthaidoctor.models.responses

data class DoctorLoginResponse(
    val data: LoginData,
    val statuscode: Int
)

data class LoginData(
    val message: String?,
    val profile: List<Profile>?,
    val sessionid: String,
    val filepath:String,
    val error: String

)

data class Profile(
    var city: String? = " ",
    var facility: String?= " ",
    var facility_id: Int?= 0,
    var fname: String?= " ",
    var id: Int?= 0,
    var lname: String?= " ",
    var phone: String?= " ",
    var specialty: String?= " ",
    var title: String?= " ",
    var username: String?= " ",
    var image_name: String?= " ",
    var experience: String?= " ",
    var degrees: String?= " ",
    var country: String?= " "
)
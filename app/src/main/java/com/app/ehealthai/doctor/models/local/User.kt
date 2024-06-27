package com.app.ehealthai.models.local

data class User(
    val isTextOrImage: String = "",
    val lastMessage: String = "",
    val lastSeen: String = "false",
    val senderId: String = "1",
    val timeStamp: String = "1",
    val patientName: String = "",
    val doctorName: String = "",
    val doctorSpeciality: String = "",
    val doctorId: String = "",
    val image_name_p: String = ""
)
package com.app.ehealthaidoctor.models.local.Notification

data class Data(
    val user: String = "",
    val patientId: String = "",
    val icon: Int = 1,
    val body: String ="",
    val title: String = "",
    val sent: String = ""
)
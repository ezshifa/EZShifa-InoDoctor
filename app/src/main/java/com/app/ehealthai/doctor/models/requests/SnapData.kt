package com.app.ehealthai.doctor.models.requests

import com.google.gson.annotations.SerializedName
import java.io.File

data class SnapData(
    @SerializedName("pid")
    val patientId: String,
    @SerializedName("doc_type")
    val docType: String,
    @SerializedName("username")
    val userName: String,
    @SerializedName("file")
    var file:File,
    @SerializedName("filePath")
    var filePath:String,
    @SerializedName("description")
    val description: String,
    @SerializedName("appointmentid")
    val appointmentid: String,



    )

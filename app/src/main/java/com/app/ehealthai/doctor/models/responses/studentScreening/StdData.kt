package com.app.ehealthai.doctor.models.responses.studentScreening

import com.example.example.StudentData
import com.google.gson.annotations.SerializedName

data class StdData(
    @SerializedName("message" ) var message : String? = null,
    @SerializedName("list"    ) var studentData:ArrayList<StudentData>   = arrayListOf<StudentData>()

)

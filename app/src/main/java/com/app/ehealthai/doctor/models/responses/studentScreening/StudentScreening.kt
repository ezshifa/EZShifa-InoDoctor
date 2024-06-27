package com.example.example

import com.app.ehealthai.doctor.models.responses.studentScreening.StdData
import com.google.gson.annotations.SerializedName


data class StudentScreening (

  @SerializedName("statuscode" ) var statuscode : Int?    = null,
  @SerializedName("message"    ) var message    : String? = null,
  @SerializedName("data"       ) var stdData       : StdData?   = StdData()

)
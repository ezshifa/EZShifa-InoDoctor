package com.example.example

import com.google.gson.annotations.SerializedName


data class PatientScreeningResponse (

  @SerializedName("statuscode" ) var statuscode : Int?  = null,
  @SerializedName("data"       ) var patientScreeningResponseData       : patientScreeningResponseData? = patientScreeningResponseData()

)
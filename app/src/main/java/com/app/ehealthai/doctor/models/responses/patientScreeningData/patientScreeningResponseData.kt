package com.example.example

import com.google.gson.annotations.SerializedName


data class patientScreeningResponseData (

  @SerializedName("message" ) var message : String?         = null,
  @SerializedName("list"    ) var patientScreeningResponseDataList    : ArrayList<patientScreeningResponseDataList> = arrayListOf()

)
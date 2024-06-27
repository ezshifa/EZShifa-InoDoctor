package com.example.example

import com.google.gson.annotations.SerializedName


data class PrescriptionHistory (

  @SerializedName("statuscode" ) var statuscode : Int?  = null,
  @SerializedName("data"       ) var data       : Data? = Data()

)
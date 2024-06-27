package com.example.example

import com.google.gson.annotations.SerializedName


data class patientScreeningResponseLeftEar (

  @SerializedName("1000Hz" ) var F1000Hz : String? = null,
  @SerializedName("2000Hz" ) var F2000Hz : String? = null,
  @SerializedName("250Hz"  ) var F250Hz  : String? = null,
  @SerializedName("3000Hz" ) var F3000Hz : String? = null,
  @SerializedName("4000Hz" ) var F4000Hz : String? = null,
  @SerializedName("500Hz"  ) var F500Hz  : String? = null,
  @SerializedName("6000Hz" ) var F6000Hz : String? = null,
  @SerializedName("8000Hz" ) var F8000Hz : String? = null

)
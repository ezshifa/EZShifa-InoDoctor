package com.app.ehealthai.doctor.models.responses

import com.google.gson.annotations.SerializedName


data class Data (
  @SerializedName("message" ) var message : String? = null
)
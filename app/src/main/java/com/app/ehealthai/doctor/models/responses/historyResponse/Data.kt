package com.example.example

import com.google.gson.annotations.SerializedName


data class Data (

  @SerializedName("message" ) var message : String? = null,
  @SerializedName("list"    ) var list    : List?   = List()

)
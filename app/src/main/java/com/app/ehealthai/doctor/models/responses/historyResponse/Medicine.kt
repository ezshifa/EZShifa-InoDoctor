package com.example.example

import com.google.gson.annotations.SerializedName


data class Medicine (

  @SerializedName("brand_name" ) var brandName : String? = null,
  @SerializedName("quantity"   ) var quantity  : String? = null,
  @SerializedName("doze_time"  ) var dozeTime  : String? = null

)
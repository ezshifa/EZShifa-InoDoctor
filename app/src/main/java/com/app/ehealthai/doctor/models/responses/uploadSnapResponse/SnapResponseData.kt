package com.app.ehealthai.doctor.models.responses.uploadSnapResponse

import com.google.gson.annotations.SerializedName

data class SnapResponseData(
    @SerializedName("message" ) var message : String? = null,
    @SerializedName("vid"     ) var vid     : Int?    = null
)

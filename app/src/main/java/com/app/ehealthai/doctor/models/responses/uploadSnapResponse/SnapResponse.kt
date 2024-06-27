package com.app.ehealthai.doctor.models.responses.uploadSnapResponse

import com.google.gson.annotations.SerializedName

data class SnapResponse(
    @SerializedName("statuscode" ) var statuscode : Int?  = null,
    @SerializedName("data"       ) var snapResponseData       : SnapResponseData? = SnapResponseData()
)
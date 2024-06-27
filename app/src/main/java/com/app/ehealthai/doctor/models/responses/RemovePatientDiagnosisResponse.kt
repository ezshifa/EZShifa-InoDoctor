package com.app.ehealthai.doctor.models.responses

import com.google.gson.annotations.SerializedName

data class RemovePatientDiagnosisResponse(
        @SerializedName("statuscode") var statuscode : Int?  = null,
        @SerializedName("data") var data : Data? = Data()
)

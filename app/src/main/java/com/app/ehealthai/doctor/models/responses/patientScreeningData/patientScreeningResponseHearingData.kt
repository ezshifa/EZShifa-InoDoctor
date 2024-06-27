package com.example.example

import com.google.gson.annotations.SerializedName


data class patientScreeningResponseHearingData (

    @SerializedName("leftEar"  ) var leftEar  : patientScreeningResponseLeftEar?  = patientScreeningResponseLeftEar(),
    @SerializedName("rightEar" ) var rightEar : patientScreeningResponsDataRightEar? = patientScreeningResponsDataRightEar()

)
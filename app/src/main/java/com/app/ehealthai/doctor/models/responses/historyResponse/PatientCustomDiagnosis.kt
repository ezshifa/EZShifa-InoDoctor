package com.example.example

import com.google.gson.annotations.SerializedName


data class PatientCustomDiagnosis (

  @SerializedName("diagnosis" ) var diagnosisName : String? = null

)
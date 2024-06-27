package com.example.example

import com.google.gson.annotations.SerializedName


data class PatientDiagnosis (

  @SerializedName("diagnosis_name" ) var diagnosisName : String? = null

)
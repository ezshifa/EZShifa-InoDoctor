package com.app.ehealthai.doctor.models.responses.patientTests
import com.google.gson.annotations.SerializedName


data class PatientTestsData(

  @SerializedName("message" ) var message : String?         = null,
  @SerializedName("list"    ) var patientTestslist    : ArrayList<PatientTestsList> = arrayListOf()

)
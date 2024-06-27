package com.app.ehealthai.doctor.models.responses.patientTests

import com.google.gson.annotations.SerializedName


data class PatientTests(

  @SerializedName("statuscode" ) var statuscode : Int?  = null,
  @SerializedName("data"       ) var patientTestsData       : PatientTestsData? = PatientTestsData()

)
package com.example.example

import com.google.gson.annotations.SerializedName


data class PatientTest (

  @SerializedName("test_name" ) var testName  : String? = null,
  @SerializedName("doctorid"  ) var doctorid  : String? = null,
  @SerializedName("patientid" ) var patientid : String? = null,
  @SerializedName("apptid"    ) var apptid    : Int?    = null,
  @SerializedName("appt_date" ) var apptDate  : String? = null

)
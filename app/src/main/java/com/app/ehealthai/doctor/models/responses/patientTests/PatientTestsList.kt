package com.app.ehealthai.doctor.models.responses.patientTests

import com.google.gson.annotations.SerializedName


data class PatientTestsList (

  @SerializedName("id"         ) var id         : Int?    = null,
  @SerializedName("patientid"  ) var patientid  : Int?    = null,
  @SerializedName("macaddress" ) var macaddress : String? = null,
  @SerializedName("tb"         ) var tb         : String? = null,
  @SerializedName("hiv"        ) var hiv        : String? = null,
  @SerializedName("hepatitis"  ) var hepatitis  : String? = null,
  @SerializedName("sugar"      ) var sugar      : String? = null,
  @SerializedName("cdate"      ) var cdate      : String? = null,
  @SerializedName("sugar_type" ) var sugarType  : String? = null

)
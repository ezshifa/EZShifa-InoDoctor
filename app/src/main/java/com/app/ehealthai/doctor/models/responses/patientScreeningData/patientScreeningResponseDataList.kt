package com.example.example

import com.google.gson.annotations.SerializedName


data class patientScreeningResponseDataList (

  @SerializedName("id"                     ) var id                  : Int?         = null,
  @SerializedName("patient_id"             ) var patientId           : Int?         = null,
  @SerializedName("left_eye_test"          ) var leftEyeTest         : String?      = null,
  @SerializedName("right_eye_test"         ) var rightEyeTest        : String?      = null,
  @SerializedName("color_blind"            ) var colorBlind          : String?      = null,
  @SerializedName("hearing_test_left"      ) var hearingTestLeft     : String?      = null,
  @SerializedName("hearing_test_right"     ) var hearingTestRight    : String?      = null,
  @SerializedName("hearing_test_both_ears" ) var hearingTestBothEars : String?      = null,
  @SerializedName("create_date"            ) var createDate          : String?      = null,
  @SerializedName("comments"               ) var comments            : String?      = null,
  @SerializedName("hearing_data"           ) var hearingData         : patientScreeningResponseHearingData? = patientScreeningResponseHearingData()

)
package com.example.example

import com.google.gson.annotations.SerializedName


data class StudentData (

  @SerializedName("id"                          ) var id                        : Int?    = null,
  @SerializedName("school_id"                   ) var schoolId                  : Int?    = null,
  @SerializedName("registration_no"             ) var registrationNo            : String? = null,
  @SerializedName("first_name"                  ) var firstName                 : String? = null,
  @SerializedName("last_name"                   ) var lastName                  : String? = null,
  @SerializedName("gender"                      ) var gender                    : String? = null,
  @SerializedName("father_name"                 ) var fatherName                : String? = null,
  @SerializedName("father_email"                ) var fatherEmail               : String? = null,
  @SerializedName("dob"                         ) var dob                       : String? = null,
  @SerializedName("address"                     ) var address                   : String? = null,
  @SerializedName("province"                    ) var province                  : String? = null,
  @SerializedName("city"                        ) var city                      : String? = null,
  @SerializedName("country"                     ) var country                   : String? = null,
  @SerializedName("father_mobile"               ) var fatherMobile              : String? = null,
  @SerializedName("emergency_numbers"           ) var emergencyNumbers          : String? = null,
  @SerializedName("nationality"                 ) var nationality               : String? = null,
  @SerializedName("religion"                    ) var religion                  : String? = null,
  @SerializedName("admission_date"              ) var admissionDate             : String? = null,
  @SerializedName("profile_picture"             ) var profilePicture            : String? = null,
  @SerializedName("class"                       ) var studentClass                     : String? = null,
  @SerializedName("section"                     ) var section                   : String? = null,
  @SerializedName("create_date"                 ) var createDate                : String? = null,
  @SerializedName("status"                      ) var status                    : String? = null,
  @SerializedName("parent_consent"              ) var parentConsent             : String? = null,
  @SerializedName("at_birth_vaccination"        ) var atBirthVaccination        : String? = null,
  @SerializedName("sixth_weeks_vaccination"     ) var sixthWeeksVaccination     : String? = null,
  @SerializedName("tenth_weeks_vaccination"     ) var tenthWeeksVaccination     : String? = null,
  @SerializedName("fourteenth_week_vaccination" ) var fourteenthWeekVaccination : String? = null,
  @SerializedName("nineth_month_vaccination"    ) var ninethMonthVaccination    : String? = null,
  @SerializedName("fifteenth_month_vaccination" ) var fifteenthMonthVaccination : String? = null,
  @SerializedName("DTP_Booster"                 ) var DTPBooster                : String? = null,
  @SerializedName("Chikenpox"                   ) var Chikenpox                 : String? = null,
  @SerializedName("Covid_1"                     ) var Covid1                    : String? = null,
  @SerializedName("Covid_2"                     ) var Covid2                    : String? = null,
  @SerializedName("influenza"                   ) var influenza                 : String? = null,
  @SerializedName("Meningococcal"               ) var Meningococcal             : String? = null,
  @SerializedName("Tdapp"                       ) var Tdapp                     : String? = null,
  @SerializedName("HPV"                         ) var HPV                       : String? = null,
  @SerializedName("Rabies"                      ) var Rabies                    : String? = null,
  @SerializedName("Allergies"                   ) var Allergies                 : String? = null,
  @SerializedName("screening_status"            ) var screeningStatus           : Int?    = null,
  @SerializedName("last_screened_date"          ) var lastScreenedDate          : String? = null

)
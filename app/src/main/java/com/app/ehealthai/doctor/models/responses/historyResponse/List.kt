package com.example.example

import com.google.gson.annotations.SerializedName


data class List(

    @SerializedName("patient_test") var patientTest: ArrayList<PatientTest> = arrayListOf(),
    @SerializedName("patient_diagnosis") var patientDiagnosis: ArrayList<PatientDiagnosis> = arrayListOf(),
    @SerializedName("daignosis_others") var patientCustomDiagnosis: ArrayList<PatientCustomDiagnosis> = arrayListOf(),
    @SerializedName("medicine") var medicine: ArrayList<Medicine> = arrayListOf()

)
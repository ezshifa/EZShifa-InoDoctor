package com.app.ehealthai.doctor.models.responses

data class MedicineList(
        val data: MedicineListData?,
        val statuscode: Int
)

data class MedicineListData(
        val list: List<MedicineData>?,
        val message: String
)

data class MedicineData(
        val Category_ID: Int,
        val Category_Name: String,
        val Generic_ID: Int,
        val Generic_Name: String,
        val Brand_ID: Int,
        val Brand_Name: String,
        val Symptoms: String

)
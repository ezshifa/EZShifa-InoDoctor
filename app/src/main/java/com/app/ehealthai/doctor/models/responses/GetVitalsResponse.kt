package com.app.ehealthai.doctor.models.responses

data class GetVitalsResponse(
        val data: VitalsListData?,
        val statuscode: Int
)
data class VitalsListData(
        val vitals: List<VitalsData>?,
        val message: String
)
data class VitalsData(
        val id: Int?,
        val cdate: String?,
        val bps: String,
        val bpd: String,
        val sugar: String,
        val temprature: String,
        val weight: String,
        val height: String,
        val pulse: String,
        val breathing: String,
        val oxygen: String,
        val pulse2:  String,
        val breathing2:  String,
        val oxygen2:String,
        val sender_id:String,
        val temprature_c:String,
        val temprature_f:String,
        val sugar_type:String,
        val weight_kg:String,
        val weight_lb:String,
        val height_ft:String,
        val height_inch:String,
        val height_cm:String,
        val bmi:String

)
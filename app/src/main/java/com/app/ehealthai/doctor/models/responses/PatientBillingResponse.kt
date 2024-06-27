package com.app.ehealthai.models.responses

data class PatientBillingResponse(
    val data: summaryData?,
    val statuscode: Int
)
data class summaryData(
    val summary: List<summary>?,
    val message: String
)
data class summary(
    val id: Int?,
    val paymentname: String?,
    val amount: Int,
    val transcation_completed_on: String,
    val status: String,
    val patient_fname: String,
    val patient_lname: String,
    val appointmentid: Int?,
    val pc_appt_type: String,
    val appointmentdate: String

)
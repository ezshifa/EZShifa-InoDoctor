package com.app.ehealthai.doctor.models.responses

data class ChangePasswordResponse(

        val statuscode: String?,
        val data: VerifyData
)
data class VerifyData(
        val message: String,
        val pcode: String
)
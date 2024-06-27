package com.app.ehealthai.doctor.models.responses

data class GetRatingResponse(
        val data: GetRatingData,
        val statuscode: Int
)

data class GetRatingData(
        val message: String,
        val rating: String
)
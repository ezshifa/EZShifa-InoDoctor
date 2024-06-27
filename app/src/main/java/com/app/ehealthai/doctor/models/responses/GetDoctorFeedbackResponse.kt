package com.app.ehealthai.doctor.models.responses

data class GetDoctorFeedbackResponse(
        val data: FeedbackListData?,
        val statuscode: Int
)
data class FeedbackListData(
        val rating: List<FeedbackData>?,
        val message: String,
        val error: String
)
data class FeedbackData(
        val id: Int?,
        val comments: String?,
        val rating_number: String,
        val title:String

)
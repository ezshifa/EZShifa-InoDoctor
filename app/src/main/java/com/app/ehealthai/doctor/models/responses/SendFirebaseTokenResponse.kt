package com.app.ehealthaidoctor.models.responses


data class SendFirebaseTokenResponse(
    val data: FirebaseTokenData,
    val statuscode: Int
)

data class FirebaseTokenData(
    val message: String
)
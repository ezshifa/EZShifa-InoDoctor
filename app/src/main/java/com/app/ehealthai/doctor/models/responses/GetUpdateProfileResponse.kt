package com.app.ehealthaidoctor.models.responses

import android.service.autofill.UserData

data class GetUpdateProfileResponse(
    val data: UpdateProfileData,
    val statuscode: Int

)

data class UpdateProfileData(
    val message: String
)
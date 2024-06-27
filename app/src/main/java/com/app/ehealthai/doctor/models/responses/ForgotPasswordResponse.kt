package com.app.ehealthai.doctor.models.responses

import com.app.ehealthaidoctor.models.responses.AppointmentDoctorData
import com.app.ehealthaidoctor.models.responses.AppointmentDoctorListData

data class ForgotPasswordResponse(

    val statuscode: String?,
    val data: errsuccresp?
)

data class errsuccresp(
    val error:String
)

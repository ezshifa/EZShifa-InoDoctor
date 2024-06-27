package com.app.ehealthai.doctor.models.responses

data class KioskServicesList (
    val data: KSData?,
    val statuscode:Int
)

data class KSData(
    val data: List<kioskservicesdata>?
)


data class kioskservicesdata(
    val kiosk_service: String?
)
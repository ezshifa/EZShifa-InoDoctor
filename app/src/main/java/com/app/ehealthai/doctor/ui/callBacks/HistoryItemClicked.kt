package com.app.ehealthai.doctor.ui.callBacks

import com.app.ehealthaidoctor.models.responses.AppointmentDoctorData

interface HistoryItemClicked {
    fun historyItem(item: AppointmentDoctorData)
}
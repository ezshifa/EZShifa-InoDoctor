package com.app.ehealthai.doctor.adapters.recyclerViewAdapters

interface IClinicalTestItemToDelete {
    fun findDiagnosisIdAndDeleteItem(diagnosisName: String,itemPosition:Int)
}
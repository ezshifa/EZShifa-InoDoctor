package com.example.ehealthai.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

class Constants{
    companion object {
        //TODO RECHECK
//        const val ENVIRONMENT_NAME = "dev."
//        const val ENVIRONMENT_NAME = "uat"
        const val ENVIRONMENT_NAME = ""

//            val BASE_URL = "https://ezshifa.com/emr/"
        val BASE_URL="https://${ENVIRONMENT_NAME}ezshifa.com/emr/"
        val BASE_URL_EZSCREENING="https://${ENVIRONMENT_NAME}ezshifa.com/emr/restapi/EZScreening/v3/"
        val BASE_URL_REPORT = "https://ezscreening-report.ezshifa.com?"
        //TODO RECHECK ENVIROMENT BEFORE RELEASE
//        val BASE_URL_REPORT = "https://devezscreening-report.ezshifa.com?"

        //val BASE_URL = "https://e-healthai.com/ezshifa/"
        //val BASE_URL = "https://uat.ezshifa.com/emr/"
        val FIREBASE_BASE_URL = "https://fcm.googleapis.com/"


        val underDevelopment: String = "Under Development"

        val USERNAME: String = "USERNAME"
        val PASSWORD: String = "Under PASSWORD"
        val APPOINTMENTID: String = ""
        val INTENT_KEY_SCHOOL_ID: String = "SchoolId"
        val INTENT_KEY_STUDENT_ID: String = "StudentId"
        val INTENT_KEY_STUDENT_NAME: String = "StudentName"
        val SNAP_FILE_DESCRIPTION_TAG: String = "SnapShot"
        val SNAP_DOC_TYPE_TAG: String = "CallSnaps"
        public fun isInternetConnected(context: Context): Boolean {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val network = connectivityManager.activeNetwork ?: return false
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        }
    }

}
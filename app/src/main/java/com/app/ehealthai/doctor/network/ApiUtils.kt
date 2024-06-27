package com.app.ehealthaidoctor.network

import android.content.Context
import com.example.ehealthai.network.ApiClient
import com.example.ehealthai.network.ApiClientScreening
import com.example.ehealthai.utils.Constants

class ApiUtils {
    companion object {
        fun getAPIService(context: Context): ApiInterface {
            return ApiClient.getClient(context, Constants.BASE_URL).create(ApiInterface::class.java)
        }

        fun getAPIServiceEZScreening(context: Context): ApiInterface {
            return ApiClientScreening.getClientEzScreeningClient(context, Constants.BASE_URL_EZSCREENING).create(ApiInterface::class.java)
        }


        fun getFirebaseAPIService(context: Context): ApiInterface {
            return ApiClient.getFirebaseClient(context, Constants.FIREBASE_BASE_URL).create(ApiInterface::class.java)
        }
    }
}
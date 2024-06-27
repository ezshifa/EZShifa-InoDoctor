package com.example.ehealthai.network

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

public class ApiClient {

    companion object {
        private var retrofit: Retrofit? = null

        fun getClient(context: Context, baseUrl: String): Retrofit {
            if (retrofit == null) {
                val logging = HttpLoggingInterceptor()
                // set your desired log level
                logging.level = HttpLoggingInterceptor.Level.BODY

                val okHttpClient = OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .addInterceptor(logging)
                    .build()


                retrofit = Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(okHttpClient)
                    .build()
            }
            return retrofit!!
        }



        fun getFirebaseClient(context: Context, firebaseUrl: String): Retrofit {
            if (retrofit == null) {
                val logging = HttpLoggingInterceptor()
                // set your desired log level
                logging.level = HttpLoggingInterceptor.Level.BODY

                val okHttpClient = OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .addInterceptor(logging)
                    .build()


                retrofit = Retrofit.Builder()
                    .baseUrl(firebaseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(okHttpClient)
                    .build()
            }
            return retrofit!!
        }
    }


}
package com.pinelabs.plural_sdk_android_testapp.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstanceV3 {

    private const val BASE_URL_EXTERNAL ="https://pluraluat.v2.pinepg.in/api/"

    fun getInstance(): Retrofit {
        val client = OkHttpClient()
        val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val clientBuilder: OkHttpClient.Builder =
            client.newBuilder().addInterceptor(interceptor as HttpLoggingInterceptor)

        return Retrofit.Builder().baseUrl(BASE_URL_EXTERNAL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(clientBuilder.build())
            .build()
    }

}
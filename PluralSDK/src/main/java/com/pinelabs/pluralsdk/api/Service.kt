package com.pinelabs.pluralsdk.api

import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Query

interface Service {

    @POST("fetch/data")
    fun fetchData(@Query("token", encoded = true) token:String): Call<FetchResponse>

}
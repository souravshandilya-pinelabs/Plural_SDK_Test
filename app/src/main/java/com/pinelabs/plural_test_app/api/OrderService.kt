package com.pinelabs.plural_sdk_android_testapp.api

import com.pinelabs.plural_sdk_android_testapp.api.model.OrderRequest
import com.pinelabs.plural_sdk_android_testapp.api.model.OrderResponse
import com.pinelabs.plural_sdk_android_testapp.api.model.TokenRequest
import com.pinelabs.plural_sdk_android_testapp.api.model.TokenResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface OrderService {
    @POST("auth/v1/token")
    fun generateToken(@Body request: TokenRequest): Call<TokenResponse>

    @POST("checkout/v1/orders")
    fun createOrder(@Header("Authorization") authHeader:String, @Body request: OrderRequest): Call<OrderResponse>

}
package com.pinelabs.plural_test_app

import android.app.Activity
import android.app.ProgressDialog
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pinelabs.plural_sdk_android_testapp.api.OrderService
import com.pinelabs.plural_sdk_android_testapp.api.RetrofitInstanceV3
import com.pinelabs.plural_sdk_android_testapp.api.model.MockData
import com.pinelabs.plural_sdk_android_testapp.api.model.OrderFailure
import com.pinelabs.plural_sdk_android_testapp.api.model.OrderResponse
import com.pinelabs.plural_sdk_android_testapp.api.model.TokenFailure
import com.pinelabs.plural_sdk_android_testapp.api.model.TokenResponse
import com.pinelabs.pluralsdk.PluralSDKManager
import com.pinelabs.pluralsdk.api.Service
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.UUID

class TestAppMainActivity : Activity() {

    private lateinit var apiInterface: OrderService

    private lateinit var progressDialog: ProgressDialog

    private lateinit var token: String

    private lateinit var btn_startPayment : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_test_app)

        btn_startPayment = findViewById(R.id.btn_start_sdk)

        btn_startPayment.setOnClickListener {
            generateToken()
            //PluralSDKManager().startPayment(this@TestAppMainActivity, "")
        }

    }

    private fun getApiInterface() {
        apiInterface = RetrofitInstanceV3.getInstance().create(OrderService::class.java)
    }

    private fun generateToken(){
        getApiInterface()

        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Loading....")
        progressDialog.show()

        val call = apiInterface.generateToken(MockData.tokenRequest)
        call.enqueue(object : Callback<TokenResponse> {
            override fun onResponse(call: Call<TokenResponse>, response: Response<TokenResponse>) {
                if (response.isSuccessful && response.body()!=null){
                    println("Token response ${response.body()!!.access_token}")
                    getOrderData(response.body()!!.access_token)
                } else {
                    progressDialog.dismiss()
                    try {
                        if (response.errorBody()!=null) {
                            val type = object : TypeToken<TokenFailure>() {}.type
                            var errorResponse: TokenFailure? =
                                Gson().fromJson(response.errorBody()!!.charStream(), type)
                            println("Error response -> ${errorResponse!!.message}")
                            Toast.makeText(
                                this@TestAppMainActivity,
                                errorResponse.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(this@TestAppMainActivity, "FAILURE - > ${response.code()}", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e:Exception) {
                        Toast.makeText(this@TestAppMainActivity, "Exception occured while generating token", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
                progressDialog.dismiss()
                t.printStackTrace()
            }

        })

    }

    private fun getOrderData(accessToken: String){

        var orderRequest = MockData.orderUAT
        orderRequest.merchant_order_reference = "ORD"+ UUID.randomUUID().toString()

        val call = apiInterface.createOrder( "Bearer $accessToken", orderRequest)
        call.enqueue(object : Callback<OrderResponse> {
            override fun onResponse(call: Call<OrderResponse>, response: Response<OrderResponse>) {
                progressDialog.dismiss()

                if (response.isSuccessful && response.body()!=null){

                    token = response.body()!!.token

                    PluralSDKManager().startPayment(this@TestAppMainActivity, token)

                } else {
                    progressDialog.dismiss()
                    try {
                        if (response.errorBody()!=null) {
                            val type = object : TypeToken<OrderFailure>() {}.type
                            var errorResponse: OrderFailure? =
                                Gson().fromJson(response.errorBody()!!.charStream(), type)
                            println("Error response -> ${errorResponse!!.code}")
                            Toast.makeText(
                                this@TestAppMainActivity,
                                errorResponse.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(this@TestAppMainActivity, "FAILURE - > ${response.code()}", Toast.LENGTH_SHORT).show()
                        }
                    }catch (e:Exception) {
                        Toast.makeText(this@TestAppMainActivity, "Exception occured while creating order", Toast.LENGTH_SHORT).show()
                    }

                }
            }

            override fun onFailure(call: Call<OrderResponse>, t: Throwable) {
                progressDialog.dismiss()
                t.printStackTrace()
            }
        })

    }

}
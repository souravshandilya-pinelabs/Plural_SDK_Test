package com.pinelabs.pluralsdk

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import com.pinelabs.pluralsdk.api.FetchResponse
import com.pinelabs.pluralsdk.api.RetrofitInstance
import com.pinelabs.pluralsdk.api.Service
import com.pinelabs.pluralsdk.util.Constant.TOKEN
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LandingActivity : Activity() {

    private lateinit var token : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.landing)

        token = intent.getStringExtra(TOKEN).toString()

        getOrderData(token)
    }

    private fun getOrderData(accessToken: String){

        var apiInterface: Service =  RetrofitInstance.getInstance().create(Service::class.java)
        val call = apiInterface.fetchData( accessToken)
        call.enqueue(object : Callback<FetchResponse> {
            override fun onResponse(call: Call<FetchResponse>, response: Response<FetchResponse>) {

                if (response.isSuccessful && response.body()!=null){
                    Toast.makeText(this@LandingActivity, "Success", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@LandingActivity, "Error", Toast.LENGTH_SHORT).show()

                }
            }

            override fun onFailure(call: Call<FetchResponse>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(this@LandingActivity, "Failure", Toast.LENGTH_SHORT).show()
            }
        })

    }

}
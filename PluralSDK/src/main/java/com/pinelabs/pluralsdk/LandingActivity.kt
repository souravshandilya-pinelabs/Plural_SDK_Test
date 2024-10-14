package com.pinelabs.pluralsdk

import android.app.Activity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.google.android.material.imageview.ShapeableImageView
import com.pinelabs.pluralsdk.adapter.DividerItemDecorator
import com.pinelabs.pluralsdk.adapter.PaymentOptionsAdapter
import com.pinelabs.pluralsdk.api.FetchResponse
import com.pinelabs.pluralsdk.api.RecyclerViewPaymentOptionData
import com.pinelabs.pluralsdk.api.RetrofitInstance
import com.pinelabs.pluralsdk.api.Service
import com.pinelabs.pluralsdk.util.Constant.TOKEN
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LandingActivity : Activity(), PaymentOptionsAdapter.OnItemClickListener {

    private lateinit var token : String

    private lateinit var imgMerchantimage: ShapeableImageView
    private lateinit var txtMerchantname: TextView
    private lateinit var txtTransactionamount: TextView
    private lateinit var recyclerPaymentOptions: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.landing)

        token = intent.getStringExtra(TOKEN).toString()

        getViews()
        getFetchData(token)
    }

    private fun getViews() {
        imgMerchantimage = findViewById(R.id.img_pic)
        txtMerchantname = findViewById(R.id.txt_merchant_name)
        txtTransactionamount = findViewById(R.id.txt_amount)
        recyclerPaymentOptions = findViewById(R.id.recycler_payment_options)

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val myRecyclerViewAdapter = PaymentOptionsAdapter(populatePaymentData(), this@LandingActivity)
        recyclerPaymentOptions.adapter = myRecyclerViewAdapter
        recyclerPaymentOptions.layoutManager = layoutManager
        val dividerItemDecoration: ItemDecoration = DividerItemDecorator(ContextCompat.getDrawable(this, R.drawable.divider)!!)
        recyclerPaymentOptions.addItemDecoration(dividerItemDecoration)
        myRecyclerViewAdapter.notifyDataSetChanged()
    }

    private fun getFetchData(accessToken: String){

        var apiInterface: Service =  RetrofitInstance.getInstance().create(Service::class.java)
        val call = apiInterface.fetchData( accessToken)
        call.enqueue(object : Callback<FetchResponse> {
            override fun onResponse(call: Call<FetchResponse>, response: Response<FetchResponse>) {

                if (response.isSuccessful && response.body()!=null){
                    Toast.makeText(this@LandingActivity, "Success", Toast.LENGTH_SHORT).show()
                    val response = response.body()
                    setViews(response!!.merchantInfo.merchantName, response!!.paymentData.originalTxnAmount.amount)
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

    private fun setViews(merchantName:String, transactionAmount: String) {
        txtMerchantname.text = merchantName
        txtTransactionamount.text = getString(R.string.rs)+" "+transactionAmount
    }

    private fun populatePaymentData():List<RecyclerViewPaymentOptionData>{
        var recyclerViewData = listOf<RecyclerViewPaymentOptionData>()
        recyclerViewData = recyclerViewData + RecyclerViewPaymentOptionData(R.drawable.card, "Cards")
        recyclerViewData = recyclerViewData + RecyclerViewPaymentOptionData(R.drawable.upi, "UPI")
        recyclerViewData = recyclerViewData + RecyclerViewPaymentOptionData(R.drawable.net_banking, "Netbanking")
        return recyclerViewData
    }

    override fun onItemClick(item: RecyclerViewPaymentOptionData?) {
        Toast.makeText(this, item!!.payment_option, Toast.LENGTH_SHORT).show()
    }

}
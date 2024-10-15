package com.pinelabs.pluralsdk

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pinelabs.pluralsdk.api.FetchResponse
import com.pinelabs.pluralsdk.api.PaymentMode
import com.pinelabs.pluralsdk.api.RecyclerViewPaymentOptionData
import com.pinelabs.pluralsdk.api.RetrofitInstance
import com.pinelabs.pluralsdk.api.Service
import com.pinelabs.pluralsdk.fragment.PaymentListFragment
import com.pinelabs.pluralsdk.util.Constant.TOKEN
import com.pinelabs.pluralsdk.util.PaymentModes
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.lifecycle.ViewModelProvider


class LandingActivity : FragmentActivity(){

    private lateinit var token : String

    private lateinit var imgMerchantimage: ImageView
    private lateinit var txtMerchantname: TextView
    private lateinit var txtTransactionamount: TextView

    private lateinit var viewModel: PaymentModesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.landing_fragment)

        viewModel = ViewModelProvider(this).get(PaymentModesViewModel::class.java)

        token = intent.getStringExtra(TOKEN).toString()

        getViews()
        getFetchData(token)
    }

    private fun getViews() {
        imgMerchantimage = findViewById(R.id.img_pic)
        txtMerchantname = findViewById(R.id.txt_merchant_name)
        txtTransactionamount = findViewById(R.id.txt_amount)

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.details_fragment, PaymentListFragment())
        transaction.commit()
    }

    private fun getFetchData(accessToken: String){

        var apiInterface: Service =  RetrofitInstance.getInstance().create(Service::class.java)
        val call = apiInterface.fetchData( accessToken)
        call.enqueue(object : Callback<FetchResponse> {
            override fun onResponse(call: Call<FetchResponse>, response: Response<FetchResponse>) {
                if (response.isSuccessful && response.body()!=null){
                    Toast.makeText(this@LandingActivity, "Success", Toast.LENGTH_SHORT).show()
                    val response = response.body()
                    setViews(response!!)
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

    private fun setViews(fetchResponse: FetchResponse) {
        if (fetchResponse.merchantBrandingData!=null) {
        val imageBytes = Base64.decode(fetchResponse.merchantBrandingData.logo.imageContent, Base64.DEFAULT)
        val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        imgMerchantimage.setImageBitmap(decodedImage)
        }

        txtMerchantname.text = fetchResponse.merchantInfo.merchantName
        txtTransactionamount.text = getString(R.string.rs)+fetchResponse.paymentData.originalTxnAmount.amount

        val mappedPaymentModes = mapPaymentModes(fetchResponse.paymentModes)
        viewModel.populate(mappedPaymentModes)
    }

    private fun mapPaymentModes(paymentModes:List<PaymentMode>): List<RecyclerViewPaymentOptionData> {
        val paymentModeMap = mutableListOf<RecyclerViewPaymentOptionData>()
        paymentModes.forEach { pm ->
            var paymentModeData = RecyclerViewPaymentOptionData()
            when(pm.paymentModeId) {
                PaymentModes.CREDIT_DEBIT.toString() -> paymentModeData = RecyclerViewPaymentOptionData(PaymentModes.CREDIT_DEBIT.paymentModeImage, PaymentModes.CREDIT_DEBIT.paymentModeName)
                PaymentModes.NET_BANKING.toString() -> paymentModeData = RecyclerViewPaymentOptionData(PaymentModes.NET_BANKING.paymentModeImage, PaymentModes.NET_BANKING.paymentModeName)
                PaymentModes.UPI.toString() -> paymentModeData = RecyclerViewPaymentOptionData(PaymentModes.UPI.paymentModeImage, PaymentModes.UPI.paymentModeName)
            }
            if (!paymentModeData.payment_option.isEmpty())
            paymentModeMap.add(paymentModeData)
        }
        return paymentModeMap
    }
}

class PaymentModesViewModel : ViewModel() {
    private val mutableSelectedItem = MutableLiveData<List<RecyclerViewPaymentOptionData>>()

    val selectedItem: LiveData<List<RecyclerViewPaymentOptionData>> get() = mutableSelectedItem

    fun populate(item: List<RecyclerViewPaymentOptionData>) {
        mutableSelectedItem.value = item
        println("Item size ${item.size}")
    }
}
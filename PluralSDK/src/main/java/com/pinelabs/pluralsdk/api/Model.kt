package com.pinelabs.pluralsdk.api

data class FetchResponse(val merchantInfo:MerchantInfo, val paymentData:PaymentData, val paymentModes:List<PaymentMode>, val merchantBrandingData:MerchantBranding)

data class FetchFailure(val status:String, val type:String, val message:String, val traceId:String)

data class MerchantInfo(val merchantId:Int, val merchantName:String)

data class OrignalTransactionAmount(val amount:String, val currency:String)

data class PaymentData(val originalTxnAmount:OrignalTransactionAmount)

data class PaymentMode(val paymentModeId:String)

data class MerchantBranding(val logo:Logo, val brandTheme:BrandTheme)

data class Logo(val imageSize:String, val imageContent:String)

data class BrandTheme(val color:String)

data class RecyclerViewPaymentOptionData(
    val payment_image: Int,
    val payment_option: String
)

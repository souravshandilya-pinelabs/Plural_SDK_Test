package com.pinelabs.pluralsdk.api

data class FetchResponse(val merchantInfo:MerchantInfo, val paymentData:PaymentData)

data class FetchFailure(val status:String, val type:String, val message:String, val traceId:String)

data class MerchantInfo(val merchantId:Int, val merchantName:String)

data class OrignalTransactionAmount(val amount:String, val currency:String)

data class PaymentData(val originalTxnAmount:OrignalTransactionAmount)

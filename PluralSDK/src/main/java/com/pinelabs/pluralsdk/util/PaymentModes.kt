package com.pinelabs.pluralsdk.util

import com.pinelabs.pluralsdk.R
import com.pinelabs.pluralsdk.util.Constant.CREDIT_DEBIT_LABEL
import com.pinelabs.pluralsdk.util.Constant.NET_BANKING_LABEL
import com.pinelabs.pluralsdk.util.Constant.UPI_LABEL
import com.pinelabs.pluralsdk.util.Constant.WALLET_LABEL

enum class PaymentModes(val paymentModeImage: Int, val paymentModeName: String) {
    CREDIT_DEBIT(R.drawable.card, CREDIT_DEBIT_LABEL),
    NET_BANKING(R.drawable.net_banking, NET_BANKING_LABEL),
    UPI(R.drawable.upi, UPI_LABEL),
    WALLET(R.drawable.upi, WALLET_LABEL)
}
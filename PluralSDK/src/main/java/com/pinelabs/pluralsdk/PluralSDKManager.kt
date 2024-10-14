package com.pinelabs.pluralsdk

import android.content.Context
import android.content.Intent
import com.pinelabs.pluralsdk.util.Constant.TOKEN

class PluralSDKManager {
    fun startPayment(context: Context, token: String) {
        val intent = Intent(context, SplashActivity::class.java)
        intent.putExtra(TOKEN, token)
        context.startActivity(intent)
    }
}
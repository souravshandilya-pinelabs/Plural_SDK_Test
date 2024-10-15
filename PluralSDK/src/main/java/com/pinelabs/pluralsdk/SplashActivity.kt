package com.pinelabs.pluralsdk

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.pinelabs.pluralsdk.util.Constant.TOKEN
import java.util.Timer
import java.util.TimerTask


class SplashActivity : Activity() {

    private lateinit var token : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash)

        token = intent.getStringExtra(TOKEN).toString()

        Timer().schedule(object : TimerTask() {
            override fun run() {
                val i = Intent(applicationContext, LandingActivity::class.java)
                i.putExtra(TOKEN, token)
                startActivity(i)
                finish()
            }
        }, 2400)

    }
}
package com.example.cryptowalet.ui.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.cryptowalet.R

class SplashActivity : AppCompatActivity() {

    private val time: Int = 3000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar?.hide()

        Handler().postDelayed(Runnable {
            val i = Intent(
                this@SplashActivity, MainActivity::class.java
            )
            startActivity(i)
            finish()
        }, time.toLong())
    }
}
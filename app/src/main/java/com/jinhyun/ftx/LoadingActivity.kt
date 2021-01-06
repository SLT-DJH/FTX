package com.jinhyun.ftx

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

class LoadingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

        startloading()
    }

    private fun startloading(){
        Handler().postDelayed({
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)

            finish()
        }, 2000)
    }
}
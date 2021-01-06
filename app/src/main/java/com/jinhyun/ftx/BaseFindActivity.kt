package com.jinhyun.ftx

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_base_find.*

class BaseFindActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base_find)

        btn_skip.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

            finish()
        }
    }
}
package com.jinhyun.ftx

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_base_select.*

class BaseSelectActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base_select)

        btn_start.setOnClickListener {
            val intent = Intent(this, BaseFindActivity::class.java)
            startActivity(intent)

            finish()
        }
    }
}
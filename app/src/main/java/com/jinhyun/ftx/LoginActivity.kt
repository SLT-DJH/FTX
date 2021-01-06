package com.jinhyun.ftx

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btn_login.setOnClickListener {
            if(et_email.text.toString() == "test@test.com" &&
                et_password.text.toString() == "123456"){
                val intent = Intent(this, BaseSelectActivity::class.java)
                startActivity(intent)

                finish()
            }
        }

        btn_registration.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}
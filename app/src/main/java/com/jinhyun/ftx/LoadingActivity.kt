package com.jinhyun.ftx

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoadingActivity : AppCompatActivity() {

    val mAuth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

        startloading()
    }

    private fun startloading(){
        Handler().postDelayed({

            val firebaseUser = mAuth.currentUser

            if(firebaseUser != null){
                if(firebaseUser.isEmailVerified){
                    db.collection("Users").document(mAuth.uid.toString())
                        .get().addOnSuccessListener { it ->
                            if(it.data!!.get("base") == ""){
                                val intent = Intent(this, BaseSelectActivity::class.java)
                                startActivity(intent)
                                finish()
                            }else{
                                val intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                        }
                }else{
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)

                    finish()
                }
            }else{
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)

                finish()
            }
        }, 2000)
    }
}
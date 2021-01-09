package com.jinhyun.ftx

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    val TAG = "LoginActivity"

    val mAuth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    val authStateListner = FirebaseAuth.AuthStateListener { firebaseAuth ->
        val firebaseUser = firebaseAuth.currentUser
        if(firebaseUser != null){
            ProgressView_logi.visibility = View.VISIBLE
            db.collection("Users").document(firebaseAuth.uid.toString())
                .get().addOnSuccessListener { it ->
                    if(it.data!!.get("base") == ""){
                        val intent = Intent(this, BaseSelectActivity::class.java)
                        startActivity(intent)
                        ProgressView_logi.visibility = View.INVISIBLE
                        finish()
                    }else{
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        ProgressView_logi.visibility = View.INVISIBLE
                        finish()
                    }
                }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        FirebaseAuth.AuthStateListener { firebaseAuth ->
            val firebaseUser = firebaseAuth.currentUser
            if(firebaseUser != null){
                ProgressView_logi.visibility = View.VISIBLE
                db.collection("Users").document(firebaseAuth.uid.toString())
                    .get().addOnSuccessListener { it ->
                        if(it.data!!.get("base") == ""){
                            val intent = Intent(this, BaseSelectActivity::class.java)
                            startActivity(intent)
                            ProgressView_logi.visibility = View.INVISIBLE
                            finish()
                        }else{
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            ProgressView_logi.visibility = View.INVISIBLE
                            finish()
                        }
                    }
            }
        }

        btn_login.setOnClickListener {
            val email = et_email.text.toString()
            val password = et_password.text.toString()

            if (email.isEmpty() || password.isEmpty()){
                Toast.makeText(this, R.string.request_email_pw, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                if(!it.isSuccessful) return@addOnCompleteListener

                Log.d(TAG, "sign in with uid : ${it.result?.user?.uid}")
            }.addOnFailureListener {
                Log.d(TAG, "failed to sign in : ${it.message}")
            }
        }

        btn_registration.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        mAuth.addAuthStateListener(this.authStateListner)
    }

    override fun onStop() {
        super.onStop()
        mAuth.removeAuthStateListener(this.authStateListner)
    }
}
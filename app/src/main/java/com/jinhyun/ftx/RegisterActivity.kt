package com.jinhyun.ftx

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    val TAG = "RegistrerActivity"

    val mAuth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    val authStateListner = FirebaseAuth.AuthStateListener { firebaseAuth ->
        val firebaseUser = firebaseAuth.currentUser
        if(firebaseUser != null){
            if(firebaseUser.isEmailVerified){
                ProgressView_regi.visibility = View.VISIBLE
                db.collection("Users").document(firebaseAuth.uid.toString())
                    .get().addOnSuccessListener { it ->
                        if(it.data?.get("base") == ""){
                            val intent = Intent(this, BaseSelectActivity::class.java)
                            startActivity(intent)
                            ProgressView_regi.visibility = View.INVISIBLE
                            finish()
                        }else{
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            ProgressView_regi.visibility = View.INVISIBLE
                            finish()
                        }
                    }
            }else{
                Toast.makeText(this, R.string.request_email_verify_complete,Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        FirebaseAuth.AuthStateListener { firebaseAuth ->
            val firebaseUser = firebaseAuth.currentUser
            if(firebaseUser != null){
                if(firebaseUser.isEmailVerified){
                    ProgressView_regi.visibility = View.VISIBLE
                    db.collection("Users").document(firebaseAuth.uid.toString())
                        .get().addOnSuccessListener { it ->
                            if(it.data!!.get("base") == ""){
                                val intent = Intent(this, BaseSelectActivity::class.java)
                                startActivity(intent)
                                ProgressView_regi.visibility = View.INVISIBLE
                                finish()
                            }else{
                                val intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)
                                ProgressView_regi.visibility = View.INVISIBLE
                                finish()
                            }
                        }
                }else{
                    Toast.makeText(this, R.string.request_email_verify_complete,Toast.LENGTH_SHORT).show()
                }
            }
        }

        btn_regi_registration.setOnClickListener {
            val email = et_regi_email.text.toString()
            val password = et_regi_password.text.toString()
            val confirmPassword = et_regi_password_confirm.text.toString()

            if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()){
                Toast.makeText(this, R.string.request_email_pw, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword){
                Toast.makeText(this, R.string.request_pw_confirm, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                if(!it.isSuccessful){
                    return@addOnCompleteListener
                }else{
                    val name = email.substring(0, email.indexOf("@"))

                    val userId = it.result?.user?.uid.toString()
                    val userinfo = hashMapOf(
                        "name" to name,
                        "email" to email,
                        "password" to password,
                        "uid" to userId,
                        "base" to ""
                    )
                    val storage = FirebaseStorage.getInstance()
                    val storageRef = storage.getReferenceFromUrl("gs://trainingfield-ed0a1.appspot.com")
                        .child("Profile/$userId.png")
                    val uri = Uri.parse("android.resource://com.jinhyun.ftx/drawable/default_profile")

                    storageRef.putFile(uri).addOnSuccessListener {
                        Log.d(TAG, "Profile Image upload finish")
                    }.addOnFailureListener{
                        Log.d(TAG, "Profile Image upload failed")
                    }

                    db.collection("Users").document(userId).set(userinfo)
                        .addOnSuccessListener {
                            Toast.makeText(this, R.string.registration_succeed,Toast.LENGTH_SHORT).show()
                            verifyEmail()
                        }.addOnFailureListener {
                            Toast.makeText(this, R.string.registration_failed, Toast.LENGTH_SHORT).show()
                            Log.d(TAG, "document failed : ${it.message}")
                        }
                    Log.d(TAG, "created id : $userId")
                }
            }.addOnFailureListener {
                Toast.makeText(this, R.string.registration_failed, Toast.LENGTH_SHORT).show()
                Log.d(TAG, "Failed to create user : ${it.message}")
            }
        }
    }

    private fun verifyEmail(){
        mAuth.currentUser?.sendEmailVerification()?.addOnCompleteListener {
            if(it.isSuccessful){
                Toast.makeText(this, R.string.request_email_verify,Toast.LENGTH_SHORT).show()

                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
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
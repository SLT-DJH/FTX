package com.jinhyun.ftx

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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
            if(firebaseUser.isEmailVerified){
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
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        FirebaseAuth.AuthStateListener { firebaseAuth ->
            val firebaseUser = firebaseAuth.currentUser
            if(firebaseUser != null){
                if(firebaseUser.isEmailVerified){
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
        }

        tv_find_password.setOnClickListener {
            showpopup()
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

                if(!mAuth.currentUser!!.isEmailVerified){
                    Toast.makeText(this, R.string.request_email_verify_complete,Toast.LENGTH_SHORT).show()
                    return@addOnCompleteListener
                }else{
                    ProgressView_logi.visibility = View.VISIBLE
                    db.collection("Users").document(mAuth.uid.toString())
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
                Log.d(TAG, "sign in with uid : ${it.result?.user?.uid}")
            }.addOnFailureListener {
                Log.d(TAG, "failed to sign in : ${it.message}")
                Toast.makeText(this, R.string.login_fail,Toast.LENGTH_SHORT).show()
            }
        }

        btn_registration.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun showpopup(){
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.find_id_popup, null)
        val findemail : EditText = view.findViewById(R.id.et_find_email)

        val alertDialog = AlertDialog.Builder(this)
            .setTitle(R.string.find_password).setPositiveButton(R.string.confirm)
            {dialog, which ->
                if (findemail.text.isEmpty()){
                    Toast.makeText(this, R.string.request_email_pw, Toast.LENGTH_SHORT).show()
                }else{
                    mAuth.sendPasswordResetEmail(findemail.text.toString()).addOnCompleteListener {
                        task ->
                        if(task.isSuccessful){
                            Toast.makeText(this, R.string.find_password_email_sent, Toast.LENGTH_SHORT).show()
                        }else{
                            Toast.makeText(this, R.string.chekc_email, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }.setNegativeButton(R.string.cancel, null).create()

        alertDialog.setView(view)
        alertDialog.show()
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
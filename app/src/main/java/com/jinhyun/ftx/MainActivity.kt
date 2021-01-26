package com.jinhyun.ftx

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jinhyun.ftx.fragment.ChatFragment
import com.jinhyun.ftx.fragment.GroupFragment
import com.jinhyun.ftx.fragment.MissionFragment
import com.jinhyun.ftx.fragment.ProfileFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.custom_actionbar.*

class MainActivity : AppCompatActivity() {

    val TAG = "MainActivity"
    val db = FirebaseFirestore.getInstance()
    val mAuth = FirebaseAuth.getInstance()
    val userref = db.collection("Users").document(mAuth.uid.toString())
    var userBase = ""
    var userName = ""
    var userImage = ""
    var userAcess = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        userref.addSnapshotListener { value, error ->

            if (error != null) {
                Log.d(TAG, "Listen failed. $error")
                return@addSnapshotListener
            }

            if(value != null && value.exists()){
                userName = value.get("name").toString()
                userBase = value.get("base").toString()
                if (value.get("access") == true){
                    userAcess = true
                    Log.d(TAG, "user access true!!")
                }else{
                    Log.d(TAG, "user access false!!")
                }
            }else{
                Log.d(TAG, "Current data: null")

                progressBar.visibility = View.INVISIBLE
            }
        }

        if (userBase == ""){
            progressBar.visibility = View.VISIBLE
            //get userref data
            userref.get().addOnSuccessListener {
                if(it.data!!.get("base") != ""){
                    userBase = it.data!!.get("base").toString()
                    setFrag(0)
                    progressBar.visibility = View.INVISIBLE
                }
            }
        }
        // 0 : MissionFrag, 1 : GroupFrag, 2 : ChatFrag, 3 : ProfileFrag

        lay_mission.setOnClickListener {
            setFrag(0)
        }
        lay_group.setOnClickListener {
            setFrag(1)
        }
        lay_chat.setOnClickListener {
            setFrag(2)
        }
        lay_profile.setOnClickListener {
            setFrag(3)
        }
    }

    private fun startFragement(base : String){
        setFrag(0)
        progressBar.visibility = View.INVISIBLE
    }

    private fun setFrag(fragNum : Int) {
        val ft = supportFragmentManager.beginTransaction()
        when(fragNum){
            0 -> {
                ft.replace(R.id.container, MissionFragment().apply {
                    arguments = Bundle().apply {
                        putString("base", userBase)
                    }
                }).commit()
                iv_mission.setImageResource(R.drawable.assignment_black)
                iv_group.setImageResource(R.drawable.group_white)
                iv_chat.setImageResource(R.drawable.chat_white)
                iv_profile.setImageResource(R.drawable.profile_white)

            }
            1 -> {
                ft.replace(R.id.container, GroupFragment()).commit()
                iv_mission.setImageResource(R.drawable.assignment_white)
                iv_group.setImageResource(R.drawable.group_black)
                iv_chat.setImageResource(R.drawable.chat_white)
                iv_profile.setImageResource(R.drawable.profile_white)
            }
            2 -> {
                ft.replace(R.id.container, ChatFragment()).commit()
                iv_mission.setImageResource(R.drawable.assignment_white)
                iv_group.setImageResource(R.drawable.group_white)
                iv_chat.setImageResource(R.drawable.chat_black)
                iv_profile.setImageResource(R.drawable.profile_white)
            }
            3 -> {
                ft.replace(R.id.container, ProfileFragment().apply {
                    arguments = Bundle().apply {
                        putString("name", userName)
                        putBoolean("access", userAcess)
                    }
                }).commit()
                iv_mission.setImageResource(R.drawable.assignment_white)
                iv_group.setImageResource(R.drawable.group_white)
                iv_chat.setImageResource(R.drawable.chat_white)
                iv_profile.setImageResource(R.drawable.profile_black)
            }
        }
    }
}

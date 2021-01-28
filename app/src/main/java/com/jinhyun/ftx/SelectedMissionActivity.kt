package com.jinhyun.ftx

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_selected_mission.*

class SelectedMissionActivity : AppCompatActivity() {

    val storage = FirebaseStorage.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selected_mission)

        val missionTitle = intent.getStringExtra("missionTitle").toString()
        val missionContent = intent.getStringExtra("missionContent").toString()
        val missionYear = intent.getIntExtra("missionYear", 0)
        val missionMonth = intent.getIntExtra("missionMonth", 0)
        val missionDay = intent.getIntExtra("missionDay", 0)
        val missionPrice = intent.getStringExtra("missionPrice").toString()
        val missionPlace = intent.getStringExtra("missionPlace").toString()
        val missionCategory = intent.getStringExtra("missionCategory").toString()
        val userName = intent.getStringExtra("userName").toString()
        val userImage = intent.getStringExtra("userImage").toString()
        val userBase = intent.getStringExtra("userBase").toString()
        val missionHour = intent.getIntExtra("missionHour", 0)
        val missionMinute = intent.getIntExtra("missionMinute", 0)

        val storageRef = storage.getReferenceFromUrl("gs://trainingfield-ed0a1.appspot.com")
            .child("Profile/${userImage}.png")

        val profileImage = findViewById<ImageView>(R.id.iv_selected_mission_profile)

        storageRef.downloadUrl.addOnSuccessListener { task ->

            Glide.with(this).load(task).into(profileImage)

        }

        tv_selected_mission_name.text = userName
        tv_selected_mission_base.text = userBase
        tv_selected_mission_category.text = missionCategory
        tv_selected_mission_title.text = missionTitle
        tv_selected_mission_content.text = missionContent
        tv_selected_mission_mission_date.text = "$missionYear-$missionMonth-$missionDay"
        tv_selected_mission_mission_price.text = missionPrice
        tv_selected_mission_mission_place.text = missionPlace
        if(missionHour < 10 && missionMinute < 10){
            tv_selected_mission_mission_time.text = "0$missionHour:0$missionMinute"
        }
        if(missionHour < 10 && missionMinute >= 10){
            tv_selected_mission_mission_time.text = "0$missionHour:$missionMinute"
        }
        if(missionHour >= 10 && missionMinute < 10){
            tv_selected_mission_mission_time.text = "$missionHour:0$missionMinute"
        }
        if(missionHour >= 10 && missionMinute >= 10){
            tv_selected_mission_mission_time.text = "$missionHour:$missionMinute"
        }

        iv_selected_mission_back.setOnClickListener {
            onBackPressed()
        }


    }
}
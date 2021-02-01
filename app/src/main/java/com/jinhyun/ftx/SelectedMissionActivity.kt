package com.jinhyun.ftx

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_selected_mission.*
import java.util.*

class SelectedMissionActivity : AppCompatActivity() {

    val storage = FirebaseStorage.getInstance()
    val TAG = "SelectedMissionActivity"

    private lateinit var timer : CountDownTimer

    var startTimer = false

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

        startCountdown(missionYear, missionMonth, missionDay, missionHour, missionMinute)

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

        val hourText : String
        val minuteText : String

        if(missionHour < 10){
            hourText = "0$missionHour"
        }else{
            hourText = "$missionHour"
        }

        if(missionMinute < 10){
            minuteText = "0$missionMinute"
        }else{
            minuteText = "$missionMinute"
        }

        tv_selected_mission_mission_time.text = "$hourText:$minuteText"

        iv_selected_mission_back.setOnClickListener {
            onBackPressed()
        }


    }

    private fun startCountdown(year : Int, month : Int, day : Int, hour : Int, minute : Int){
        Log.d(TAG, "startCountdown $year, $month, $day, $hour, $minute")
        val setCalendar = Calendar.getInstance()

        setCalendar.set(year, month-1, day, hour, minute)

        val setMill = setCalendar.timeInMillis

        val needMill = setMill - System.currentTimeMillis()

        if (needMill > 0){
            Log.d(TAG, "startCountdown needMill > 0!")
            timer = object : CountDownTimer(needMill, 1000){
                override fun onTick(millisUntilFinished: Long) {
                    updateText(millisUntilFinished)
                }

                override fun onFinish() {

                }
            }.start()
            startTimer = true
        }else{
            Log.d(TAG, "startCountdown needMill <= 0!")
            updateText(0)

            startTimer = false
        }
    }

    private fun updateText(num : Long){
        val days = ((((num / 1000) / 60) / 60) / 24).toInt()
        val hours = ((((num  / 1000) / 60) / 60) % 24).toInt()
        val minutes = (((num / 1000) / 60) % 60).toInt()
        val seconds =((num / 1000) % 60).toInt()

        tv_selected_mission_countdown.text = "${days}일 ${hours}시 ${minutes}분 ${seconds}초"
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if(startTimer){
            timer.cancel()
        }
    }
}
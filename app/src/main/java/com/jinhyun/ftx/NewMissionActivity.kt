package com.jinhyun.ftx

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.jinhyun.ftx.dialog.CategoryDialog
import kotlinx.android.synthetic.main.activity_new_mission.*
import java.text.DecimalFormat
import java.util.*

class NewMissionActivity : AppCompatActivity() {

    val db = FirebaseFirestore.getInstance()
    val mAuth = FirebaseAuth.getInstance()
    val missionRef = db.collection("Missions")

    private val df = DecimalFormat("#,###")
    private var priceResult = ""
    private val watcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if(!TextUtils.isEmpty(s.toString()) && !s.toString().equals(priceResult)){
                priceResult = df.format(s.toString().replace(",","").toDouble())
                et_new_mission_price.setText(priceResult)
                et_new_mission_price.setSelection(priceResult.length)
            }
        }

        override fun afterTextChanged(s: Editable?) {

        }
    }
    private var category = ""
    private var title = ""
    private var place = ""
    private var price = ""
    private var dateText = ""
    private var timeText = ""
    private var year = 0
    private var month = 0
    private var day = 0
    private var hour = 0
    private var minute = 0
    private var missionContent = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_mission)

        val userBase = intent.getStringExtra("base").toString()

        val itemRef = missionRef.document(userBase).collection("Items")

        et_new_mission_price.addTextChangedListener(watcher)

        iv_new_mission_back.setOnClickListener {
            onBackPressed()
        }

        LN_new_mission_category.setOnClickListener {
            val dlg = CategoryDialog(this)
            dlg.setOnOKClickedListener { content ->
                tv_new_mission_category.text = content
                category = content
            }
            dlg.start()
        }

        tv_new_mission_date_pick.setOnClickListener {
            val cal = Calendar.getInstance()
            DatePickerDialog(this, DatePickerDialog.OnDateSetListener{datePicker, y, m, d ->
                tv_new_mission_date_pick.text = "$y-${m+1}-$d"
                dateText = "$y-${m+1}-$d"
                year = y
                month = m+1
                day = d
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE)).show()
        }

        tv_new_mission_time_pick.setOnClickListener {
            val cal = Calendar.getInstance()
            TimePickerDialog(this, TimePickerDialog.OnTimeSetListener{timePicker, h, m ->

                hour = h
                minute = m

                val hourText : String
                val minuteText : String

                if(h < 10){
                    hourText = "0$h"
                }else{
                    hourText = "$h"
                }

                if(m < 10){
                    minuteText = "0$m"
                }else{
                    minuteText = "$m"
                }

                tv_new_mission_time_pick.text = "$hourText:$minuteText"
                timeText = "$hourText:$minuteText"
            }, cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE), true).show()
        }

        tv_new_mission_done.setOnClickListener {
            LN_new_mission_progress.visibility = View.VISIBLE
            title = et_new_mission_title.text.toString()
            price = et_new_mission_price.text.toString()
            place = et_new_mission_place.text.toString()
            missionContent = et_new_mission_content.text.toString()

            if(category == "" || title == "" || place == "" || price == "" || dateText == "" ||
                timeText == "" || missionContent == ""){
                Toast.makeText(this, R.string.request_email_pw, Toast.LENGTH_SHORT).show()
                LN_new_mission_progress.visibility = View.INVISIBLE
                return@setOnClickListener
            }

            val missionData = hashMapOf(
                "category" to category,
                "title" to title,
                "place" to place,
                "content" to missionContent,
                "price" to price,
                "year" to year,
                "month" to month,
                "day" to day,
                "hour" to hour,
                "minute" to minute,
                "writer" to mAuth.uid.toString()
            )

            itemRef.add(missionData).addOnSuccessListener {
                Toast.makeText(this, R.string.success_mission_register, Toast.LENGTH_SHORT).show()
                finish()
                LN_new_mission_progress.visibility = View.INVISIBLE
            }.addOnFailureListener {
                Toast.makeText(this, R.string.fail_mission_register, Toast.LENGTH_SHORT).show()
                LN_new_mission_progress.visibility = View.INVISIBLE
            }
        }

    }

}
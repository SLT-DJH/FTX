package com.jinhyun.ftx

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.jinhyun.ftx.dialog.CategoryDialog
import kotlinx.android.synthetic.main.activity_new_mission.*
import java.text.DecimalFormat
import java.util.*

class NewMissionActivity : AppCompatActivity() {

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
    private var date = ""
    private var time = ""
    private var missionContent = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_mission)

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
                date = "$y-${m+1}-$d"
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE)).show()
        }

        tv_new_mission_time_pick.setOnClickListener {
            val cal = Calendar.getInstance()
            TimePickerDialog(this, TimePickerDialog.OnTimeSetListener{timePicker, h, m ->
                tv_new_mission_time_pick.text = "$h : $m"
                time = "$h : $m"
            }, cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE), true).show()
        }

        tv_new_mission_done.setOnClickListener {
            title = et_new_mission_title.text.toString()
            price = et_new_mission_price.text.toString()
            place = et_new_mission_place.text.toString()
            missionContent = et_new_mission_content.text.toString()

            if(category == "" || title == "" || place == "" || price == "" || date == "" ||
                time == "" || missionContent == ""){
                Toast.makeText(this, R.string.request_email_pw, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Toast.makeText(this, "잘 하셨습니다!", Toast.LENGTH_SHORT).show()
            onBackPressed()
        }

    }

}
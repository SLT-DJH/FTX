package com.jinhyun.ftx

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.jinhyun.ftx.dialog.CategoryPostDialog
import kotlinx.android.synthetic.main.activity_new_post.*

class NewPostActivity : AppCompatActivity() {

    private var category = ""
    private var postcontent = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_post)

        LN_new_post_category.setOnClickListener {
            val dlg = CategoryPostDialog(this)
            dlg.setOnOKClickedListener { content ->
                tv_new_post_category.text = content
                category = content
            }
            dlg.start()
        }

        tv_view_principle.setOnClickListener {
            val dlg = Dialog(this)
            dlg.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dlg.setContentView(R.layout.principle_popup)
            dlg.setCancelable(true)

            dlg.show()

        }

        iv_new_post_back.setOnClickListener {
            onBackPressed()
        }

        tv_new_post_done.setOnClickListener {

            LN_new_post_progress.visibility = View.VISIBLE

            postcontent = et_new_post_content.text.toString()

            if(!btn_check_principle.isChecked){
                Toast.makeText(this, R.string.request_check_sns_principle, Toast.LENGTH_SHORT).show()
                LN_new_post_progress.visibility = View.INVISIBLE
                return@setOnClickListener
            }

            if(postcontent == "" || category == ""){
                Toast.makeText(this, R.string.request_email_pw, Toast.LENGTH_SHORT).show()
                LN_new_post_progress.visibility = View.INVISIBLE
                return@setOnClickListener
            }

            Toast.makeText(this, "잘 하셨습니다!", Toast.LENGTH_SHORT).show()
            LN_new_post_progress.visibility = View.INVISIBLE

            finish()

        }
    }
}
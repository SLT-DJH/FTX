package com.jinhyun.ftx.dialog

import android.app.Dialog
import android.content.Context
import android.view.Window
import android.widget.LinearLayout
import android.widget.TextView
import com.jinhyun.ftx.R

class CategoryDialog(context: Context) {
    private val dlg = Dialog(context)
    private lateinit var listener : categoryOkClickedListener
    private lateinit var errand : TextView
    private lateinit var laundry: TextView
    private lateinit var delivery : TextView
    private lateinit var cleaning : TextView
    private lateinit var lesson : TextView
    private lateinit var teach : TextView
    private lateinit var consult : TextView
    private lateinit var etc : TextView


    fun start() {
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dlg.setContentView(R.layout.category_popup)
        dlg.setCancelable(true)

        errand = dlg.findViewById(R.id.tv_category_errand)
        laundry = dlg.findViewById(R.id.tv_category_laundry)
        delivery = dlg.findViewById(R.id.tv_category_delivery)
        cleaning = dlg.findViewById(R.id.tv_category_cleaning)
        lesson = dlg.findViewById(R.id.tv_category_lesson)
        teach = dlg.findViewById(R.id.tv_category_teach)
        consult = dlg.findViewById(R.id.tv_category_consult)
        etc = dlg.findViewById(R.id.tv_category_etc)

        dlg.findViewById<LinearLayout>(R.id.LN_category_errand).setOnClickListener {
            listener.onOKClicked(errand.text.toString())
            dlg.dismiss()
        }

        dlg.findViewById<LinearLayout>(R.id.LN_category_laundry).setOnClickListener {
            listener.onOKClicked(laundry.text.toString())
            dlg.dismiss()
        }

        dlg.findViewById<LinearLayout>(R.id.LN_category_delivery).setOnClickListener {
            listener.onOKClicked(delivery.text.toString())
            dlg.dismiss()
        }

        dlg.findViewById<LinearLayout>(R.id.LN_category_cleaning).setOnClickListener {
            listener.onOKClicked(cleaning.text.toString())
            dlg.dismiss()
        }

        dlg.findViewById<LinearLayout>(R.id.LN_category_lesson).setOnClickListener {
            listener.onOKClicked(lesson.text.toString())
            dlg.dismiss()
        }

        dlg.findViewById<LinearLayout>(R.id.LN_category_teach).setOnClickListener {
            listener.onOKClicked(teach.text.toString())
            dlg.dismiss()
        }

        dlg.findViewById<LinearLayout>(R.id.LN_category_consult).setOnClickListener {
            listener.onOKClicked(consult.text.toString())
            dlg.dismiss()
        }

        dlg.findViewById<LinearLayout>(R.id.LN_category_etc).setOnClickListener {
            listener.onOKClicked(etc.text.toString())
            dlg.dismiss()
        }

        dlg.show()
    }

    fun setOnOKClickedListener(listener : (String) -> Unit) {
        this.listener = object : categoryOkClickedListener {
            override fun onOKClicked(content: String) {
                listener(content)
            }
        }
    }

    interface categoryOkClickedListener {
        fun onOKClicked(content : String)
    }
}
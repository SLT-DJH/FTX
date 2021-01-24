package com.jinhyun.ftx.dialog

import android.app.Dialog
import android.content.Context
import android.view.Window
import android.widget.LinearLayout
import android.widget.TextView
import com.jinhyun.ftx.R

class CategoryPostDialog(context : Context) {
    private val dlg = Dialog(context)
    private lateinit var listener : categoryOkClickedListener
    private lateinit var lost : TextView
    private lateinit var question: TextView
    private lateinit var routine : TextView
    private lateinit var news : TextView
    private lateinit var hobby : TextView
    private lateinit var zoo : TextView
    private lateinit var plant : TextView
    private lateinit var health : TextView
    private lateinit var vacation : TextView
    private lateinit var etc : TextView

    fun start(){
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dlg.setContentView(R.layout.category_post_popup)
        dlg.setCancelable(true)

        lost = dlg.findViewById(R.id.tv_category_lost)
        question = dlg.findViewById(R.id.tv_category_question)
        routine = dlg.findViewById(R.id.tv_category_routine)
        news = dlg.findViewById(R.id.tv_category_news)
        hobby = dlg.findViewById(R.id.tv_category_hobby)
        zoo = dlg.findViewById(R.id.tv_category_zoo)
        plant = dlg.findViewById(R.id.tv_category_plant)
        health = dlg.findViewById(R.id.tv_category_health)
        vacation = dlg.findViewById(R.id.tv_category_vacation)
        etc = dlg.findViewById(R.id.tv_category_etc)

        dlg.findViewById<LinearLayout>(R.id.LN_category_lost).setOnClickListener {
            listener.onOKClicked(lost.text.toString())
            dlg.dismiss()
        }

        dlg.findViewById<LinearLayout>(R.id.LN_category_question).setOnClickListener {
            listener.onOKClicked(question.text.toString())
            dlg.dismiss()
        }

        dlg.findViewById<LinearLayout>(R.id.LN_category_routine).setOnClickListener {
            listener.onOKClicked(routine.text.toString())
            dlg.dismiss()
        }

        dlg.findViewById<LinearLayout>(R.id.LN_category_news).setOnClickListener {
            listener.onOKClicked(news.text.toString())
            dlg.dismiss()
        }

        dlg.findViewById<LinearLayout>(R.id.LN_category_hobby).setOnClickListener {
            listener.onOKClicked(hobby.text.toString())
            dlg.dismiss()
        }

        dlg.findViewById<LinearLayout>(R.id.LN_category_zoo).setOnClickListener {
            listener.onOKClicked(zoo.text.toString())
            dlg.dismiss()
        }
        dlg.findViewById<LinearLayout>(R.id.LN_category_plant).setOnClickListener {
            listener.onOKClicked(plant.text.toString())
            dlg.dismiss()
        }

        dlg.findViewById<LinearLayout>(R.id.LN_category_health).setOnClickListener {
            listener.onOKClicked(health.text.toString())
            dlg.dismiss()
        }

        dlg.findViewById<LinearLayout>(R.id.LN_category_vacation).setOnClickListener {
            listener.onOKClicked(vacation.text.toString())
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
package com.jinhyun.ftx

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jinhyun.ftx.fragment.ChatFragment
import com.jinhyun.ftx.fragment.GroupFragment
import com.jinhyun.ftx.fragment.MissionFragment
import com.jinhyun.ftx.fragment.ProfileFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // 0 : MissionFrag, 1 : GroupFrag, 2 : ChatFrag, 3 : ProfileFrag
        setFrag(0)

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

    private fun setFrag(fragNum : Int) {
        val ft = supportFragmentManager.beginTransaction()
        when(fragNum){
            0 -> {
                ft.replace(R.id.container, MissionFragment()).commit()
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
                ft.replace(R.id.container, ProfileFragment()).commit()
                iv_mission.setImageResource(R.drawable.assignment_white)
                iv_group.setImageResource(R.drawable.group_white)
                iv_chat.setImageResource(R.drawable.chat_white)
                iv_profile.setImageResource(R.drawable.profile_black)
            }
        }
    }
}

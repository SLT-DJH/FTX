package com.jinhyun.ftx.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.jinhyun.ftx.R
import com.jinhyun.ftx.adapter.MissionAdapter
import com.jinhyun.ftx.data.MissionData
import kotlinx.android.synthetic.main.fragment_mission.*
import kotlinx.android.synthetic.main.fragment_mission.view.*

class MissionFragment : Fragment() {

    val missionList = arrayListOf<MissionData>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_mission, container, false)

        view.tv_title.text = arguments?.getString("base")

        missionList.add(
            MissionData("test", "심부름 구합니다.",
                "BOQ 11동 212호", "2021. 1. 10.", "2,000원"))
        missionList.add(MissionData("test", "배달이 필요합니다. \n정문에서 치킨..",
            "BOQ 11동 201호", "2021. 1. 8.", "3,000원"))
        missionList.add(MissionData("test", "물건을 옮겨주세요!!",
            "창조생활관 2층 9호", "2021. 1. 2.", "5,000원"))

        postToList()

        val mAdapter = MissionAdapter(activity!!.applicationContext, missionList)
        view.missionRecyclerView.adapter = mAdapter
        view.missionRecyclerView.layoutManager = LinearLayoutManager(activity!!.applicationContext)

        return view
    }

    private fun addToList(missionImage : String, missionText : String, placeText : String,
                          dateText : String, priceText : String){
        missionList.add(MissionData(missionImage, missionText, placeText, dateText, priceText))
    }

    private fun postToList(){
        for (i in 1..30){
            addToList("test", "심부름 $i", "생활관 ${i}층 ${i}호",
                "2021. 1. ${i}.", "${i},000원")
        }
    }
}
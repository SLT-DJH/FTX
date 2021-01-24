package com.jinhyun.ftx.fragment

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.jinhyun.ftx.NewMissionActivity
import com.jinhyun.ftx.R
import com.jinhyun.ftx.adapter.MissionAdapter
import com.jinhyun.ftx.data.MissionData
import kotlinx.android.synthetic.main.custom_actionbar.*
import kotlinx.android.synthetic.main.custom_actionbar.view.*
import kotlinx.android.synthetic.main.fragment_mission.*
import kotlinx.android.synthetic.main.fragment_mission.view.*

class MissionFragment : Fragment() {

    val missionList = arrayListOf<MissionData>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_mission, container, false)

        activity?.findViewById<TextView>(R.id.toolbar_title)?.text = arguments?.getString("base")

        setHasOptionsMenu(true)

        missionList.add(
            MissionData(getString(R.string.errand), "심부름 구합니다.",
                "BOQ 11동 212호", "2021. 1. 10.", "2,000원"))
        missionList.add(MissionData(getString(R.string.delivery), "배달이 필요합니다. \n정문에서 치킨..",
            "BOQ 11동 201호", "2021. 1. 8.", "3,000원"))
        missionList.add(MissionData(getString(R.string.cleaning), "물건을 옮겨주세요!!",
            "창조생활관 2층 9호", "2021. 1. 2.", "5,000원"))

        postToList()

        val mAdapter = MissionAdapter(activity!!.applicationContext, missionList)
        view.missionRecyclerView.adapter = mAdapter
        view.missionRecyclerView.layoutManager = LinearLayoutManager(activity!!.applicationContext)

        view.btn_mission_create.setOnClickListener {
            val intent = Intent(activity, NewMissionActivity::class.java)
            startActivity(intent)
        }

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater?.inflate(R.menu.menu_mission_bar,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when(item?.itemId){
            R.id.mission_search -> {
                return true
            }
            R.id.mission_tune -> {
                return true
            }
            R.id.mission_alert -> {
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun addToList(missionImage : String, missionText : String, placeText : String,
                          dateText : String, priceText : String){
        missionList.add(MissionData(missionImage, missionText, placeText, dateText, priceText))
    }

    private fun postToList(){
        for (i in 1..30){
            if (i in 1..8){
                addToList(getString(R.string.laundry), "심부름 $i", "생활관 ${i}층 ${i}호",
                    "2021. 1. ${i}.", "${i},000원")
            }
            if (i in 9..15){
                addToList(getString(R.string.lesson), "심부름 $i", "생활관 ${i}층 ${i}호",
                    "2021. 1. ${i}.", "${i},000원")
            }
            if (i in 16..21){
                addToList(getString(R.string.teach), "심부름 $i", "생활관 ${i}층 ${i}호",
                    "2021. 1. ${i}.", "${i},000원")
            }
            if(i in 22..26){
                addToList(getString(R.string.consult), "심부름 $i", "생활관 ${i}층 ${i}호",
                    "2021. 1. ${i}.", "${i},000원")
            }
            if(i in 27..30){
                addToList(getString(R.string.etc), "심부름 $i", "생활관 ${i}층 ${i}호",
                    "2021. 1. ${i}.", "${i},000원")
            }
        }
    }
}
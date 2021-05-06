package com.jinhyun.ftx.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.jinhyun.ftx.NewMissionActivity
import com.jinhyun.ftx.R
import com.jinhyun.ftx.SelectedMissionActivity
import com.jinhyun.ftx.adapter.MissionAdapter
import com.jinhyun.ftx.data.MissionData
import kotlinx.android.synthetic.main.custom_actionbar.*
import kotlinx.android.synthetic.main.custom_actionbar.view.*
import kotlinx.android.synthetic.main.fragment_mission.*
import kotlinx.android.synthetic.main.fragment_mission.view.*

class MissionFragment : Fragment(), MissionAdapter.OnItemClickListener {

    val db = FirebaseFirestore.getInstance()

    val TAG = "MissionFragment"

    val userRef = db.collection("Users")

    var missionList = arrayListOf<MissionData>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_mission, container, false)

        activity?.findViewById<TextView>(R.id.toolbar_title)?.text = arguments?.getString("base")

        setHasOptionsMenu(true)

        val itemRef = db.collection("Missions")
            .document(arguments?.getString("base").toString()).collection("Items")

        itemRef.orderBy("timestamp", Query.Direction.ASCENDING).addSnapshotListener { value, error ->
            if (error != null){
                return@addSnapshotListener
            }

            missionList = arrayListOf()

            val mAdapter = MissionAdapter(activity!!.applicationContext, missionList, this)
            view.missionRecyclerView.adapter = mAdapter
            view.missionRecyclerView.layoutManager = LinearLayoutManager(activity!!.applicationContext)

            for (doc in value!!){
                val MissionCategory = doc.getString("category").toString()
                val MissionText = doc.getString("title").toString()
                val PlaceText = doc.getString("place").toString()
                val MissionYear = doc.getLong("year")!!.toInt()
                val MissionMonth = doc.getLong("month")!!.toInt()
                val MissionDay = doc.getLong("day")!!.toInt()
                val MissionHour = doc.getLong("hour")!!.toInt()
                val MissionMinute = doc.getLong("minute")!!.toInt()
                val MissionContent = doc.getString("content").toString()
                //UserImage = Writer Id
                val UserImage = doc.getString("writer").toString()
                val PriceText = doc.getString("price").toString()

                userRef.document(UserImage).addSnapshotListener { value, error ->
                    if (error != null){
                        return@addSnapshotListener
                    }

                    if (value != null && value.exists()){
                        val resultUserName = value.get("name").toString()
                        val resultUserBase = value.get("base").toString()

                        addToList(MissionCategory, MissionText, PlaceText,
                            MissionYear, MissionMonth, MissionDay,PriceText,
                            MissionContent, resultUserName, resultUserBase,
                            MissionHour, MissionMinute, UserImage)

                        mAdapter.notifyDataSetChanged()
                    }
                }

            }

        }

        view.btn_mission_create.setOnClickListener {
            val intent = Intent(activity, NewMissionActivity::class.java)
            intent.putExtra("base", arguments?.getString("base"))
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

    private fun addToList(category : String, title : String, place : String,
                          year : Int, month : Int, day : Int, price : String,
                          content : String, name : String, base : String, hour : Int,
                          minute : Int, image : String){

        missionList.add(MissionData(category, title, place, year, month, day, hour,
            minute, price, content, name, image, base))
//        Log.d(TAG, "data added!! $category, $title, $place, $date, $price, " +
//                "$content, $name, $image, $base, $time")

    }

    override fun onMissionItemClick(position: Int) {
        val clickedItem = missionList[position]

        val intent = Intent(activity, SelectedMissionActivity::class.java)
        intent.putExtra("missionTitle", clickedItem.missionText)
        intent.putExtra("missionYear", clickedItem.missionYear)
        intent.putExtra("missionMonth", clickedItem.missionMonth)
        intent.putExtra("missionDay", clickedItem.missionDay)
        intent.putExtra("missionCategory", clickedItem.missionImage)
        intent.putExtra("missionPrice", clickedItem.priceText)
        intent.putExtra("missionContent", clickedItem.missionContent)
        intent.putExtra("missionPlace", clickedItem.placeText)
        intent.putExtra("userName", clickedItem.userName)
        intent.putExtra("writerID", clickedItem.userImage)
        intent.putExtra("userBase", clickedItem.userBase)
        intent.putExtra("missionHour", clickedItem.missionHour)
        intent.putExtra("missionMinute", clickedItem.missionMinute)
        startActivity(intent)
    }
}
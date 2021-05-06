package com.jinhyun.ftx.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jinhyun.ftx.R
import com.jinhyun.ftx.adapter.viewholder.MissionViewHolder
import com.jinhyun.ftx.data.MissionData
import kotlinx.android.synthetic.main.custom_mission_list.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MissionAdapter(
    val mContext: Context,
    val missionList : ArrayList<MissionData>,
    val listener: OnItemClickListener
) : RecyclerView.Adapter<MissionViewHolder>() {

    interface OnItemClickListener {
        fun onMissionItemClick(position : Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MissionViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.custom_mission_list, parent, false)
        return MissionViewHolder(view, mContext, listener)
    }

    override fun getItemCount(): Int {
        return missionList.size
    }

    override fun onBindViewHolder(holder: MissionViewHolder, position: Int) {
        holder.bind(missionList[position])
    }
}
package com.jinhyun.ftx.adapter.viewholder

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jinhyun.ftx.R
import com.jinhyun.ftx.adapter.MissionAdapter
import com.jinhyun.ftx.data.MissionData
import java.text.SimpleDateFormat
import java.util.*

class MissionViewHolder(
    private val view : View,
    private val mContext : Context,
    private val listener : MissionAdapter.OnItemClickListener
) : RecyclerView.ViewHolder(view), View.OnClickListener{
    val missionImage = view.findViewById<ImageView>(R.id.iv_mission)
    val missionName = view.findViewById<TextView>(R.id.tv_mission_title)
    val missionPlace = view.findViewById<TextView>(R.id.tv_place)
    val missionDate = view.findViewById<TextView>(R.id.tv_date)
    val missionPrice = view.findViewById<TextView>(R.id.tv_price)
    val missionTime = view.findViewById<TextView>(R.id.tv_time)

    init {
        view.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if(adapterPosition != RecyclerView.NO_POSITION){
            listener.onMissionItemClick(adapterPosition)
        }
    }

    fun bind(mission : MissionData){
        when(mission.missionImage){
            mContext.getString(R.string.errand) -> missionImage.setImageResource(R.drawable.errand_icon)
            mContext.getString(R.string.laundry) -> missionImage.setImageResource(R.drawable.laundry_icon)
            mContext.getString(R.string.delivery) -> missionImage.setImageResource(R.drawable.delivery_icon)
            mContext.getString(R.string.cleaning) -> missionImage.setImageResource(R.drawable.cleaning_icon)
            mContext.getString(R.string.lesson) -> missionImage.setImageResource(R.drawable.lesson_icon)
            mContext.getString(R.string.teach) -> missionImage.setImageResource(R.drawable.teach_icon)
            mContext.getString(R.string.consult) -> missionImage.setImageResource(R.drawable.consult_icon)
            mContext.getString(R.string.etc) -> missionImage.setImageResource(R.drawable.etc_icon)
        }
        missionName.text = mission.missionText
        missionPrice.text = mission.priceText
        missionPlace.text = mission.placeText

        val selectedCal = Calendar.getInstance()
        selectedCal.set(mission.missionYear, mission.missionMonth - 1, mission.missionDay,
            mission.missionHour, mission.missionMinute)
        val dateText = SimpleDateFormat("yyyy. M. dd").format(selectedCal.time)
        val timeText = SimpleDateFormat("HH:mm").format(selectedCal.time)

        missionDate.text = dateText
        missionTime.text = timeText
    }
}
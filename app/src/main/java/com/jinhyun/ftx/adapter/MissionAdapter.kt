package com.jinhyun.ftx.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jinhyun.ftx.R
import com.jinhyun.ftx.data.MissionData
import kotlinx.android.synthetic.main.custom_mission_list.view.*

class MissionAdapter(val context: Context, val missionList : ArrayList<MissionData>) :
    RecyclerView.Adapter<MissionAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.custom_mission_list, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return missionList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(missionList[position], context)
    }

    inner class Holder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val missionImage = itemView.findViewById<ImageView>(R.id.iv_mission)
        val missionName = itemView.findViewById<TextView>(R.id.tv_mission_title)
        val missionPlace = itemView.findViewById<TextView>(R.id.tv_place)
        val missionDate = itemView.findViewById<TextView>(R.id.tv_date)
        val missionPrice = itemView.findViewById<TextView>(R.id.tv_price)

        fun bind(mission : MissionData, context: Context){
            when(mission.missionImage){
                context.getString(R.string.errand) -> missionImage.setImageResource(R.drawable.errand_icon)
                context.getString(R.string.laundry) -> missionImage.setImageResource(R.drawable.laundry_icon)
                context.getString(R.string.delivery) -> missionImage.setImageResource(R.drawable.delivery_icon)
                context.getString(R.string.cleaning) -> missionImage.setImageResource(R.drawable.cleaning_icon)
                context.getString(R.string.lesson) -> missionImage.setImageResource(R.drawable.lesson_icon)
                context.getString(R.string.teach) -> missionImage.setImageResource(R.drawable.teach_icon)
                context.getString(R.string.consult) -> missionImage.setImageResource(R.drawable.consult_icon)
                context.getString(R.string.etc) -> missionImage.setImageResource(R.drawable.etc_icon)
            }
            missionName.text = mission.missionText
            missionPrice.text = mission.priceText
            missionDate.text = mission.dateText
            missionPlace.text = mission.placeText
        }
    }
}
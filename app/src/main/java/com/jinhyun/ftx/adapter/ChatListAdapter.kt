package com.jinhyun.ftx.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jinhyun.ftx.R
import com.jinhyun.ftx.data.ChatListData
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ChatListAdapter(val mcontext : Context, val chatlistList : ArrayList<ChatListData>,
                      val listener : OnItemClickListener) :
    RecyclerView.Adapter<ChatListAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(mcontext).inflate(R.layout.custom_chatlist_list, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return chatlistList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(chatlistList[position], mcontext)
    }

    inner class Holder(itemView : View) : RecyclerView.ViewHolder(itemView), View.OnClickListener{
        val chatListImage = itemView.findViewById<ImageView>(R.id.iv_chat_list_profile)
        val chatListName = itemView.findViewById<TextView>(R.id.tv_chat_list_name)
        val chatListBase = itemView.findViewById<TextView>(R.id.tv_chat_list_base)
        val chatListDate = itemView.findViewById<TextView>(R.id.tv_chat_list_date)
        val chatListContent = itemView.findViewById<TextView>(R.id.tv_chat_list_content)

        fun bind(chatListData : ChatListData, context: Context){
            Glide.with(mcontext).load(chatListData.profileImage).into(chatListImage)
            chatListName.text = chatListData.nameText
            chatListBase.text = chatListData.baseText
            chatListContent.text = chatListData.contentText

            val currentCal = Calendar.getInstance()
            val currentYear = currentCal.get(Calendar.YEAR)
            val currentMonth = currentCal.get(Calendar.MONTH) + 1
            val currentDay = currentCal.get(Calendar.DATE)

            val selectedCal = Calendar.getInstance()
            selectedCal.time = chatListData.date.toDate()
            val selectedYear = selectedCal.get(Calendar.YEAR)
            val selectedMonth = selectedCal.get(Calendar.MONTH) + 1
            val selectedDay = selectedCal.get(Calendar.DATE)

            if ((currentYear- selectedYear) == 0 && (currentMonth - selectedMonth) == 0){
                if ((currentDay - selectedDay) == 1){
                    chatListDate.text = "어제"
                }else if ((currentDay - selectedDay) == 2){
                    chatListDate.text = "그저께"
                }else if ((currentDay - selectedDay) == 0){
                    val sdf = SimpleDateFormat("a h:mm").format(chatListData.date.toDate())
                    chatListDate.text = sdf
                }else{
                    chatListDate.text = "$selectedYear. $selectedMonth. $selectedDay."
                }
            }else{
                chatListDate.text = "$selectedYear. $selectedMonth. $selectedDay."
            }


        }

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION){
                listener.onChatItemClick(position)
            }
        }

    }

    interface OnItemClickListener {
        fun onChatItemClick(position : Int)
    }
}
package com.jinhyun.ftx.adapter.viewholder

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jinhyun.ftx.R
import com.jinhyun.ftx.adapter.ChatListAdapter
import com.jinhyun.ftx.data.ChatListData
import java.text.SimpleDateFormat
import java.util.*

class ChatListViewHolder(
    private val view : View,
    private val mContext : Context,
    private val listener : ChatListAdapter.OnItemClickListener
) : RecyclerView.ViewHolder(view), View.OnClickListener {
    private val chatListImage = view.findViewById<ImageView>(R.id.iv_chat_list_profile)
    private val chatListName = view.findViewById<TextView>(R.id.tv_chat_list_name)
    private val chatListBase = view.findViewById<TextView>(R.id.tv_chat_list_base)
    private val chatListDate = view.findViewById<TextView>(R.id.tv_chat_list_date)
    private val chatListContent = view.findViewById<TextView>(R.id.tv_chat_list_content)

    init {
        view.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (adapterPosition != RecyclerView.NO_POSITION) {
            listener?.onChatItemClick(adapterPosition)
        }
    }

    fun bind(chatListData: ChatListData) {
        Glide.with(mContext).load(chatListData.profileImage).into(chatListImage)
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

        if ((currentYear - selectedYear) == 0 && (currentMonth - selectedMonth) == 0) {
            if ((currentDay - selectedDay) == 1) {
                chatListDate.text = "어제"
            } else if ((currentDay - selectedDay) == 2) {
                chatListDate.text = "그저께"
            } else if ((currentDay - selectedDay) == 0) {
                val sdf = SimpleDateFormat("a h:mm").format(chatListData.date.toDate())
                chatListDate.text = sdf
            } else {
                chatListDate.text = "$selectedYear. $selectedMonth. $selectedDay."
            }
        } else {
            chatListDate.text = "$selectedYear. $selectedMonth. $selectedDay."
        }
    }
}
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
import com.jinhyun.ftx.adapter.viewholder.ChatListViewHolder
import com.jinhyun.ftx.data.ChatListData
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ChatListAdapter(
    val mContext : Context,
    val chatlistList : ArrayList<ChatListData>,
    val listener : OnItemClickListener
) :
    RecyclerView.Adapter<ChatListViewHolder>() {

    interface OnItemClickListener {
        fun onChatItemClick(position : Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatListViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.custom_chatlist_list, parent, false)
        return ChatListViewHolder(view, mContext, listener)
    }

    override fun getItemCount(): Int {
        return chatlistList.size
    }

    override fun onBindViewHolder(holder: ChatListViewHolder, position: Int) {
        holder.bind(chatlistList[position])
    }
}
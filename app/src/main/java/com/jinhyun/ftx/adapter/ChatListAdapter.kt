package com.jinhyun.ftx.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jinhyun.ftx.R
import com.jinhyun.ftx.data.ChatListData

class ChatListAdapter(val context : Context, val chatlistList : ArrayList<ChatListData>) :
    RecyclerView.Adapter<ChatListAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.custom_chatlist_list, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return chatlistList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(chatlistList[position], context)
    }

    inner class Holder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val chatListImage = itemView.findViewById<ImageView>(R.id.iv_chat_list_profile)
        val chatListName = itemView.findViewById<TextView>(R.id.tv_chat_list_name)
        val chatListBase = itemView.findViewById<TextView>(R.id.tv_chat_list_base)
        val chatListDate = itemView.findViewById<TextView>(R.id.tv_chat_list_date)
        val chatListContent = itemView.findViewById<TextView>(R.id.tv_chat_list_content)

        fun bind(chatListData : ChatListData, context: Context){
            chatListImage.setImageResource(R.drawable.default_profile)
            chatListName.text = chatListData.nameText
            chatListBase.text = chatListData.baseText
            chatListDate.text = chatListData.dateText
            chatListContent.text = chatListData.contentText
        }

    }
}
package com.jinhyun.ftx.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.jinhyun.ftx.R
import com.jinhyun.ftx.adapter.viewholder.ChatViewHolder
import com.jinhyun.ftx.data.ChatData
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ChatAdapter(
    val mContext : Context,
    val chatList : ArrayList<ChatData>,
    val imageUrl : String
) : RecyclerView.Adapter<ChatViewHolder>() {

    val TAG = "ChatAdapter"

    var firebaseUser : FirebaseUser = FirebaseAuth.getInstance().currentUser!!

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ChatViewHolder {

        return if (position == 1){
            Log.d(TAG, "position 1")
            val view = LayoutInflater.from(mContext).inflate(R.layout.message_item_right, parent, false)
            ChatViewHolder(view, mContext, imageUrl, firebaseUser)
        }else{
            Log.d(TAG, "position 0")
            val view = LayoutInflater.from(mContext).inflate(R.layout.message_item_left, parent,false)
            ChatViewHolder(view, mContext, imageUrl, firebaseUser)
        }

    }

    override fun getItemCount(): Int {
        return chatList.size
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        Log.d(TAG, "start onBindViewHolder")
        holder.bind(chatList[position])
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {

        return if (chatList[position].sender == firebaseUser.uid){
            1
        }else{
            0
        }
    }
}
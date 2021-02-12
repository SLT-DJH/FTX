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
import com.jinhyun.ftx.data.ChatData
import com.jinhyun.ftx.model.Chat
import kotlinx.android.synthetic.main.message_item_left.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ChatAdapter(val mContext : Context, val chatList : ArrayList<ChatData>, val imageUrl : String) :
    RecyclerView.Adapter<ChatAdapter.Holder>() {

    val TAG = "ChatAdapter"

    var firebaseUser : FirebaseUser = FirebaseAuth.getInstance().currentUser!!

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): Holder {

        return if (position == 1){
            Log.d(TAG, "position 1")
            val view = LayoutInflater.from(mContext).inflate(R.layout.message_item_right, parent, false)
            Holder(view)
        }else{
            Log.d(TAG, "position 0")
            val view = LayoutInflater.from(mContext).inflate(R.layout.message_item_left, parent,false)
            Holder(view)
        }

    }

    override fun getItemCount(): Int {
        return chatList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        Log.d(TAG, "start onBindViewHolder")
        holder.bind(chatList[position], mContext)
    }


    inner class Holder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val profile_image = itemView.findViewById<ImageView>(R.id.iv_message_profile)
        val left_linear_text = itemView.findViewById<LinearLayout>(R.id.LN_message_left_layout)
        val right_linear_text = itemView.findViewById<LinearLayout>(R.id.LN_message_right_layout)
        val message_text = itemView.findViewById<TextView>(R.id.tv_message)
        val left_card_image = itemView.findViewById<CardView>(R.id.card_message_left_image)
        val right_card_image = itemView.findViewById<CardView>(R.id.card_message_right_image)
        val left_image = itemView.findViewById<ImageView>(R.id.iv_message_left_image)
        val right_image = itemView.findViewById<ImageView>(R.id.iv_message_right_image)
        val left_linear_image = itemView.findViewById<LinearLayout>(R.id.LN_image_message_left)
        val right_linear_image = itemView.findViewById<LinearLayout>(R.id.LN_image_message_right)
        val date_text = itemView.findViewById<TextView>(R.id.tv_date)
        val image_date_text = itemView.findViewById<TextView>(R.id.tv_image_date)

        fun bind(chatData : ChatData, context: Context){
            if (chatData.sender != firebaseUser.uid){
                Glide.with(mContext).load(imageUrl).into(profile_image)
            }

            //image message
            if (chatData.message == "" && chatData.url != ""){
                //image message -right side
                if (chatData.sender == firebaseUser.uid){
                    right_linear_text.visibility = View.GONE
                    right_linear_image.visibility = View.VISIBLE
                    Glide.with(mContext).load(chatData.url).into(right_image)
                }//image message - left side
                else if (chatData.sender != firebaseUser.uid){
                    left_linear_text.visibility = View.GONE
                    left_linear_image.visibility = View.VISIBLE
                    Glide.with(mContext).load(chatData.url).into(left_image)
                }
            }//text message
            else{
                if (chatData.sender == firebaseUser.uid){
                    right_linear_text.visibility = View.VISIBLE
                    right_linear_image.visibility = View.GONE

                    message_text.text = chatData.message
                }//image message - left side
                else if (chatData.sender != firebaseUser.uid){
                    left_linear_text.visibility = View.VISIBLE
                    left_linear_image.visibility = View.GONE

                    message_text.text = chatData.message
                }
            }

            //날짜계산
            val currentCal = Calendar.getInstance()
            val currentYear = currentCal.get(Calendar.YEAR)
            val currentMonth = currentCal.get(Calendar.MONTH) + 1
            val currentDay = currentCal.get(Calendar.DATE)

            val selectedCal = Calendar.getInstance()
            selectedCal.time = chatData.timestamp.toDate()
            val selectedYear = selectedCal.get(Calendar.YEAR)
            val selectedMonth = selectedCal.get(Calendar.MONTH) + 1
            val selectedDay = selectedCal.get(Calendar.DATE)

            if ((currentYear- selectedYear) == 0 && (currentMonth - selectedMonth) == 0){
                if ((currentDay - selectedDay) == 1){
                    date_text.text = "어제"
                    image_date_text.text = "어제"
                }else if ((currentDay - selectedDay) == 2){
                    date_text.text = "그저께"
                    image_date_text.text = "그저께"
                }else if ((currentDay - selectedDay) == 0){
                    val sdf = SimpleDateFormat("a h:mm").format(chatData.timestamp.toDate())
                    date_text.text = sdf
                    image_date_text.text = sdf
                }else{
                    date_text.text = "$selectedYear. $selectedMonth. $selectedDay."
                    image_date_text.text = "$selectedYear. $selectedMonth. $selectedDay."
                }
            }else{
                date_text.text = "$selectedYear. $selectedMonth. $selectedDay."
                image_date_text.text = "$selectedYear. $selectedMonth. $selectedDay."
            }
        }

    }

    override fun getItemViewType(position: Int): Int {

        Log.d(TAG, "start getItemViewType")

        return if (chatList[position].sender == firebaseUser.uid){
            1
        }else{
            0
        }
    }
}
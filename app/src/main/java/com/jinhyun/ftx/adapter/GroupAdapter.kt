package com.jinhyun.ftx.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.jinhyun.ftx.R
import com.jinhyun.ftx.data.GroupData
import kotlinx.android.synthetic.main.custom_group_list.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class GroupAdapter(val mcontext : Context, val postList : ArrayList<GroupData>) :
    RecyclerView.Adapter<GroupAdapter.Holder>(){

    val TAG = "GroupAdapter"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(mcontext).inflate(R.layout.custom_group_list, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(postList[position], mcontext)
    }

    inner class Holder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val postCategory = itemView.findViewById<TextView>(R.id.tv_post_category)
        val postDate = itemView.findViewById<TextView>(R.id.tv_update_date)
        val userName = itemView.findViewById<TextView>(R.id.tv_post_name)
        val postContent = itemView.findViewById<TextView>(R.id.tv_post_content)
        val postImage = itemView.findViewById<ImageView>(R.id.iv_post_image)

        val postLike = itemView.findViewById<ImageView>(R.id.iv_post_like)
        val postComment = itemView.findViewById<ImageView>(R.id.iv_post_comment)

        fun bind(group : GroupData, context: Context){
            postCategory.text = group.categoryText
            userName.text = group.nameText
            postContent.text = group.postText
            if(group.postImage != ""){
                Glide.with(mcontext).load(group.postImage).into(postImage)
                postImage.visibility = View.VISIBLE
            }
            postLike.setImageResource(R.drawable.ic_favorite_border_black_24dp)
            postComment.setImageResource(R.drawable.ic_chat_bubble_outline_black_24dp)

            val currentCal = Calendar.getInstance()
            val currentYear = currentCal.get(Calendar.YEAR)
            val currentMonth = currentCal.get(Calendar.MONTH) + 1
            val currentDay = currentCal.get(Calendar.DATE)

            val selectedCal = Calendar.getInstance()
            selectedCal.time = group.timestamp.toDate()
            val selectedYear = selectedCal.get(Calendar.YEAR)
            val selectedMonth = selectedCal.get(Calendar.MONTH) + 1
            val selectedDay = selectedCal.get(Calendar.DATE)

            if ((currentYear- selectedYear) == 0 && (currentMonth - selectedMonth) == 0){
                if ((currentDay - selectedDay) == 1){
                    postDate.text = "어제"
                }else if ((currentDay - selectedDay) == 2){
                    postDate.text = "그저께"
                }else if ((currentDay - selectedDay) == 0){
                    val sdf = SimpleDateFormat("a h:mm").format(group.timestamp.toDate())
                    postDate.text = sdf
                }else{
                    postDate.text = "$selectedYear. $selectedMonth. $selectedDay."
                }
            }else{
                postDate.text = "$selectedYear. $selectedMonth. $selectedDay."
            }
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

}
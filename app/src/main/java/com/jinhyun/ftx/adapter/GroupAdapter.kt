package com.jinhyun.ftx.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jinhyun.ftx.R
import com.jinhyun.ftx.data.GroupData
import kotlinx.android.synthetic.main.custom_group_list.view.*

class GroupAdapter(val context : Context, val postList : ArrayList<GroupData>) :
    RecyclerView.Adapter<GroupAdapter.Holder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.custom_group_list, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(postList[position], context)
    }

    inner class Holder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val postCategory = itemView.findViewById<TextView>(R.id.tv_post_category)
        val postDate = itemView.findViewById<TextView>(R.id.tv_update_date)
        val userImage = itemView.findViewById<ImageView>(R.id.iv_post_profile_image)
        val userName = itemView.findViewById<TextView>(R.id.tv_post_name)
        val postContent = itemView.findViewById<TextView>(R.id.tv_post_content)
        val postImage = itemView.findViewById<ImageView>(R.id.iv_post_image)

        fun bind(group : GroupData, context: Context){
            postCategory.text = group.categoryText
            postDate.text = group.dateText
            userImage.setImageResource(R.drawable.default_profile)
            userName.text = group.userText
            postContent.text = group.postText
            postImage.setImageResource(R.drawable.default_back)
        }
    }

}
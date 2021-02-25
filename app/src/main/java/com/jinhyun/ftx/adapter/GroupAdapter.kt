package com.jinhyun.ftx.adapter

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.jinhyun.ftx.MainActivity
import com.jinhyun.ftx.R
import com.jinhyun.ftx.SelectedPostActivity
import com.jinhyun.ftx.data.GroupData
import kotlinx.android.synthetic.main.custom_group_list.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class GroupAdapter(val mcontext : Context, val postList : ArrayList<GroupData>, val listener : OnItemClickListener, val base : String) :
    RecyclerView.Adapter<GroupAdapter.Holder>(){

    val db = FirebaseFirestore.getInstance()
    val postRef = db.collection("Missions").document(base).collection("Posts")
    val mAuth = FirebaseAuth.getInstance()

    val TAG = "GroupAdapter"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(mcontext).inflate(R.layout.custom_group_list, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = postList[position]

        holder.bind(item, mcontext)

        holder.postLikeLN.setOnClickListener {
            if(holder.postLikeText.text == "좋아요") {
                postRef.document(item.postID).collection("Likes")
                    .document(mAuth.uid.toString()).set({})
            }else{
                postRef.document(item.postID).collection("Likes")
                    .document(mAuth.uid.toString()).delete()
            }

        }

        holder.postCommentLN.setOnClickListener {
            val intent = Intent(mcontext, SelectedPostActivity::class.java)

            intent.putExtra("category", item.categoryText)
            intent.putExtra("name", item.nameText)
            intent.putExtra("imageUrl", item.postImage)
            intent.putExtra("content", item.postText)
            intent.putExtra("time", holder.postDate.text)
            intent.putExtra("postID", item.postID)
            intent.putExtra("base", base)
            intent.putExtra("writerID", item.writerID)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            mcontext.startActivity(intent)
        }
    }

    inner class Holder(itemView : View) : RecyclerView.ViewHolder(itemView), View.OnClickListener{
        val postCategory = itemView.findViewById<TextView>(R.id.tv_post_category)
        val postDate = itemView.findViewById<TextView>(R.id.tv_update_date)
        val userName = itemView.findViewById<TextView>(R.id.tv_post_name)
        val postContent = itemView.findViewById<TextView>(R.id.tv_post_content)
        val postImage = itemView.findViewById<ImageView>(R.id.iv_post_image)

        val postLike = itemView.findViewById<ImageView>(R.id.iv_post_like)
        val postLikeText = itemView.findViewById<TextView>(R.id.tv_post_like)
        val postComment = itemView.findViewById<ImageView>(R.id.iv_post_comment)
        val postCommentText = itemView.findViewById<TextView>(R.id.tv_post_comment)

        val postLikeLN = itemView.findViewById<LinearLayout>(R.id.LN_post_like)
        val postCommentLN = itemView.findViewById<LinearLayout>(R.id.LN_post_comment)

        val postLikeNum = itemView.findViewById<TextView>(R.id.tv_post_like_num)
        val postCommentNum = itemView.findViewById<TextView>(R.id.tv_post_comment_num)

        fun bind(group : GroupData, context: Context){
            postCategory.text = group.categoryText
            userName.text = group.nameText
            postContent.text = group.postText
            if(group.postImage != ""){
                Glide.with(mcontext).load(group.postImage).into(postImage)
                postImage.visibility = View.VISIBLE
            }
            initLike(group.postID, postLike, postLikeText, postLikeNum)
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

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION){
                listener.onItemClick(position, v!!)
            }
        }
    }

    private fun initLike(postID : String, imageView : ImageView, textView : TextView, numTextView : TextView){
        //is like?
        postRef.document(postID).collection("Likes").document(mAuth.uid.toString())
            .addSnapshotListener { value, error ->
                if(error != null){
                    return@addSnapshotListener
                }

                if (value != null && value.exists()){
                    imageView.setImageResource(R.drawable.ic_favorite_red_24dp)
                    textView.text = "좋아함"
                    textView.setTextColor(ContextCompat.getColor(mcontext, R.color.red3))
                }else{
                    imageView.setImageResource(R.drawable.ic_favorite_border_black_24dp)
                    textView.text = "좋아요"
                    textView.setTextColor(ContextCompat.getColor(mcontext, R.color.DarkGrey))
                }
            }

        //num like
        postRef.document(postID).collection("Likes").addSnapshotListener { value, error ->
            if (error != null){
                return@addSnapshotListener
            }

            if (value != null){
                val size = value.size()
                numTextView.text = "좋아요 $size"
            }else{
                numTextView.text = "좋아요 0"
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position : Int, view : View)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

}
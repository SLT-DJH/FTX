package com.jinhyun.ftx.adapter.viewholder

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jinhyun.ftx.R
import com.jinhyun.ftx.SelectedPostActivity
import com.jinhyun.ftx.adapter.GroupAdapter
import com.jinhyun.ftx.data.GroupData
import java.text.SimpleDateFormat
import java.util.*

class GroupViewHolder(
    private val view : View,
    private val mContext : Context,
    private val firebaseFirestore: FirebaseFirestore,
    private val mAuth : FirebaseAuth,
    private val base : String,
    private val listener : GroupAdapter.OnItemClickListener
) : RecyclerView.ViewHolder(view), View.OnClickListener {
    val postRef = firebaseFirestore.collection("Missions").document(base).collection("Posts")

    init {
        view.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (adapterPosition != RecyclerView.NO_POSITION){
            listener?.onGroupItemClick(adapterPosition, view)
        }
    }

    val postCategory = view.findViewById<TextView>(R.id.tv_post_category)
    val postDate = view.findViewById<TextView>(R.id.tv_update_date)
    val userName = view.findViewById<TextView>(R.id.tv_post_name)
    val postContent = view.findViewById<TextView>(R.id.tv_post_content)
    val postImage = view.findViewById<ImageView>(R.id.iv_post_image)

    val postLike = view.findViewById<ImageView>(R.id.iv_post_like)
    val postLikeText = view.findViewById<TextView>(R.id.tv_post_like)
    val postComment = view.findViewById<ImageView>(R.id.iv_post_comment)
    val postCommentText = view.findViewById<TextView>(R.id.tv_post_comment)

    val postLikeLN = view.findViewById<LinearLayout>(R.id.LN_post_like)
    val postCommentLN = view.findViewById<LinearLayout>(R.id.LN_post_comment)

    val postLikeNum = view.findViewById<TextView>(R.id.tv_post_like_num)
    val postCommentNum = view.findViewById<TextView>(R.id.tv_post_comment_num)

    fun bind(group : GroupData){
        postCategory.text = group.categoryText
        userName.text = group.nameText
        postContent.text = group.postText

        if(group.postImage != ""){
            Glide.with(mContext).load(group.postImage).into(postImage)
            postImage.visibility = View.VISIBLE
        }

        initLike(group.postID, postLike, postLikeText, postLikeNum)

        initComment(group.postID, postCommentNum)

        postComment.setImageResource(R.drawable.ic_chat_bubble_outline_black_24dp)

        postLikeLN.setOnClickListener {
            if(postLikeText.text == "좋아요") {
                postRef.document(group.postID).collection("Likes")
                    .document(mAuth.uid.toString()).set({})
            }else{
                postRef.document(group.postID).collection("Likes")
                    .document(mAuth.uid.toString()).delete()
            }

        }

        postCommentLN.setOnClickListener {
            val intent = Intent(mContext, SelectedPostActivity::class.java)

            intent.putExtra("category", group.categoryText)
            intent.putExtra("name", group.nameText)
            intent.putExtra("imageUrl", group.postImage)
            intent.putExtra("content", group.postText)
            intent.putExtra("time", postDate.text)
            intent.putExtra("postID", group.postID)
            intent.putExtra("base", base)
            intent.putExtra("writerID", group.writerID)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            mContext.startActivity(intent)
        }

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
                    textView.setTextColor(ContextCompat.getColor(mContext, R.color.red3))
                }else{
                    imageView.setImageResource(R.drawable.ic_favorite_border_black_24dp)
                    textView.text = "좋아요"
                    textView.setTextColor(ContextCompat.getColor(mContext, R.color.DarkGrey))
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

    private fun initComment(postID : String, numTextView : TextView){
        postRef.document(postID).collection("Comments").addSnapshotListener { value, error ->
            if (error != null){
                return@addSnapshotListener
            }

            if (value != null){
                val size = value.size()
                numTextView.text = "댓글 $size"
            }else{
                numTextView.text = "댓글 0"
            }
        }
    }
}
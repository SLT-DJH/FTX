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
import com.google.firebase.firestore.FirebaseFirestore
import com.jinhyun.ftx.R
import com.jinhyun.ftx.data.CommentData
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CommentAdapter(val mcontext : Context, val commentList : ArrayList<CommentData>) : RecyclerView.Adapter<CommentAdapter.Holder>() {

    val db = FirebaseFirestore.getInstance()
    val userRef = db.collection("Users")
    val mAuth = FirebaseAuth.getInstance()

    val TAG = "GroupAdapter"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(mcontext).inflate(R.layout.custom_comment_list, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return commentList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = commentList[position]

        holder.bind(item)
    }

    inner class Holder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val writerImage = itemView.findViewById<ImageView>(R.id.iv_comment_profile)
        val writerName = itemView.findViewById<TextView>(R.id.tv_comment_name)
        val commentDate = itemView.findViewById<TextView>(R.id.tv_comment_date)
        val commentText = itemView.findViewById<TextView>(R.id.tv_comment_comment)

        fun bind(comment : CommentData){
            userRef.document(comment.writerID).addSnapshotListener { value, error ->
                if (error != null){
                    return@addSnapshotListener
                }

                if (value != null && value.exists()){
                    val userName = value.get("name").toString()
                    val userProfile = value.get("profile").toString()

                    writerName.text = userName

                    Glide.with(mcontext).load(userProfile).into(writerImage)
                }
            }

            commentText.text = comment.comment

            val currentCal = Calendar.getInstance()
            val currentYear = currentCal.get(Calendar.YEAR)
            val currentMonth = currentCal.get(Calendar.MONTH) + 1
            val currentDay = currentCal.get(Calendar.DATE)

            val selectedCal = Calendar.getInstance()
            selectedCal.time = comment.timestamp.toDate()
            val selectedYear = selectedCal.get(Calendar.YEAR)
            val selectedMonth = selectedCal.get(Calendar.MONTH) + 1
            val selectedDay = selectedCal.get(Calendar.DATE)

            if ((currentYear- selectedYear) == 0 && (currentMonth - selectedMonth) == 0){
                if ((currentDay - selectedDay) == 1){
                    commentDate.text = "어제"
                }else if ((currentDay - selectedDay) == 2){
                    commentDate.text = "그저께"
                }else if ((currentDay - selectedDay) == 0){
                    val sdf = SimpleDateFormat("a h:mm").format(comment.timestamp.toDate())
                    commentDate.text = sdf
                }else{
                    commentDate.text = "$selectedYear. $selectedMonth. $selectedDay."
                }
            }else{
                commentDate.text = "$selectedYear. $selectedMonth. $selectedDay."
            }
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
}
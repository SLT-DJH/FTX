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
import com.jinhyun.ftx.adapter.viewholder.CommentViewHolder
import com.jinhyun.ftx.data.CommentData
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CommentAdapter(
    val mContext : Context,
    val commentList : ArrayList<CommentData>
) : RecyclerView.Adapter<CommentViewHolder>() {

    val firebaseFirestore : FirebaseFirestore = FirebaseFirestore.getInstance()

    val TAG = "GroupAdapter"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.custom_comment_list, parent, false)
        return CommentViewHolder(view, mContext, firebaseFirestore)
    }

    override fun getItemCount(): Int {
        return commentList.size
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val item = commentList[position]

        holder.bind(item)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
}
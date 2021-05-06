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
import com.jinhyun.ftx.adapter.viewholder.GroupViewHolder
import com.jinhyun.ftx.data.GroupData
import kotlinx.android.synthetic.main.custom_group_list.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class GroupAdapter(
    val mContext : Context,
    val postList : ArrayList<GroupData>,
    val base : String,
    val listener : OnItemClickListener
) : RecyclerView.Adapter<GroupViewHolder>(){

    val firebaseFirestore = FirebaseFirestore.getInstance()
    val mAuth : FirebaseAuth = FirebaseAuth.getInstance()

    val TAG = "GroupAdapter"

    interface OnItemClickListener {
        fun onGroupItemClick(position : Int, view : View)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.custom_group_list, parent, false)
        return GroupViewHolder(view, mContext, firebaseFirestore, mAuth, base, listener)
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        val item = postList[position]

        holder.bind(item)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

}
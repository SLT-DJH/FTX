package com.jinhyun.ftx

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_selected_post.*

class SelectedPostActivity : AppCompatActivity() {
    val TAG = "SelectedPostActivity"

    val db = FirebaseFirestore.getInstance()
    val mAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selected_post)

        val postCategory = intent.getStringExtra("category")
        val userName = intent.getStringExtra("name")
        val postImage = intent.getStringExtra("imageUrl")
        val postContent = intent.getStringExtra("content")
        val postTime = intent.getStringExtra("time")
        val postID = intent.getStringExtra("postID")
        val userBase = intent.getStringExtra("base")
        val writerID = intent.getStringExtra("writerID")

        db.collection("Users").document(writerID).addSnapshotListener { value, error ->
            if(error != null){
                return@addSnapshotListener
            }

            if(value != null && value.exists()){
                val userImage = value.get("profile")
                val profileView = findViewById<ImageView>(R.id.iv_selected_post_profile)

                Glide.with(this).load(userImage).into(profileView)
            }
        }

        tv_selected_post_name.text = userName
        tv_selected_post_base.text = userBase
        tv_selected_post_category.text = postCategory
        tv_selected_post_content.text = postContent
        tv_selected_post_date.text = postTime

        if (postImage != ""){
            val imageView = findViewById<ImageView>(R.id.iv_selected_post_image)

            Glide.with(this).load(postImage).into(imageView)

            imageView.visibility = View.VISIBLE
        }



    }
}
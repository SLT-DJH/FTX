package com.jinhyun.ftx

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.jinhyun.ftx.adapter.CommentAdapter
import com.jinhyun.ftx.data.CommentData
import kotlinx.android.synthetic.main.activity_selected_post.*
import kotlinx.android.synthetic.main.custom_group_list.*
import java.util.*

class SelectedPostActivity : AppCompatActivity() {
    val TAG = "SelectedPostActivity"

    val db = FirebaseFirestore.getInstance()
    val mAuth = FirebaseAuth.getInstance()

    var commentList = arrayListOf<CommentData>()
    var commentIDList = arrayListOf<String>()

    var mAdapter : CommentAdapter? = null

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

        val commentRef = db.collection("Missions")
            .document(userBase).collection("Posts")
            .document(postID).collection("Comments")

        val likeRef = db.collection("Missions")
            .document(userBase).collection("Posts")
            .document(postID).collection("Likes")

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

        //좋아요 반영하기
        likeRef.document(mAuth.uid.toString()).addSnapshotListener { value, error ->
            if(error != null){
                return@addSnapshotListener
            }

            if (value != null && value.exists()){
                iv_selected_post_like.setImageResource(R.drawable.ic_favorite_red_24dp)
                tv_selected_post_like.text = "좋아함"
                tv_selected_post_like.setTextColor(ContextCompat.getColor(applicationContext, R.color.red3))
            }else{
                iv_selected_post_like.setImageResource(R.drawable.ic_favorite_border_black_24dp)
                tv_selected_post_like.text = "좋아요"
                tv_selected_post_like.setTextColor(ContextCompat.getColor(applicationContext, R.color.DarkGrey))
            }
        }
        //좋아요 숫자 반영하기
        likeRef.addSnapshotListener { value, error ->
            if (error != null){
                return@addSnapshotListener
            }

            if (value != null){
                val size = value.size()
                tv_selected_post_like_num.text = "좋아요 $size"
            }else{
                tv_selected_post_like_num.text = "좋아요 0"
            }
        }
        //댓글 숫자 반영하기
        commentRef.addSnapshotListener { value, error ->
            if (error != null){
                return@addSnapshotListener
            }

            if (value != null){
                val size = value.size()
                tv_selected_post_comment_num.text = "댓글 $size"
            }else{
                tv_selected_post_comment_num.text = "댓글 0"
            }
        }

        if (postImage != ""){
            val imageView = findViewById<ImageView>(R.id.iv_selected_post_image)

            Glide.with(this).load(postImage).into(imageView)

            imageView.visibility = View.VISIBLE
        }

        //댓글 불러오기

        mAdapter = CommentAdapter(applicationContext, commentList)
        mAdapter!!.setHasStableIds(true)
        rv_selected_post_comment.adapter = mAdapter
        var LN = LinearLayoutManager(applicationContext)
        rv_selected_post_comment.layoutManager = LN

        commentRef.orderBy("timestamp", Query.Direction.DESCENDING).addSnapshotListener { value, error ->
            if (error != null){
                return@addSnapshotListener
            }

            for(doc in value!!){
                val comment = CommentData(doc.get("comment").toString(),
                    doc.get("timestamp") as Timestamp, doc.get("writerID").toString())

                if (!commentIDList.contains(doc.get("commentID").toString())){
                    commentList.add(comment)
                    commentIDList.add(doc.get("commentID").toString())
                }
            }

            mAdapter!!.notifyDataSetChanged()
        }

        LN_selected_post_like.setOnClickListener {
            if(tv_selected_post_like.text == "좋아요") {
                likeRef.document(mAuth.uid.toString()).set({})
            }else{
                likeRef.document(mAuth.uid.toString()).delete()
            }
        }

        et_selected_post_comment.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                if(et_selected_post_comment.text.isBlank()){
                    iv_selected_post_send.visibility = View.GONE
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                iv_selected_post_send.visibility = View.VISIBLE
            }
        })

        iv_selected_post_send.setOnClickListener {
            val comment = et_selected_post_comment.text.toString()
            val writerID = mAuth.uid.toString()
            val randomID = commentRef.document().id
            val timestamp = Timestamp(Date())

            val commentInfo = hashMapOf(
                "comment" to comment,
                "writerID" to writerID,
                "commentID" to randomID,
                "timestamp" to timestamp
            )

            commentRef.document(randomID).set(commentInfo).addOnSuccessListener {
                et_selected_post_comment.text.clear()
            }.addOnFailureListener {
                Toast.makeText(this, R.string.failed_input_comment, Toast.LENGTH_SHORT).show()
            }
        }



    }
}
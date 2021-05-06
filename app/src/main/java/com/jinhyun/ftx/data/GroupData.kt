package com.jinhyun.ftx.data

data class GroupData (
    val categoryText : String,
    val postText : String,
    val postImage : String,
    val timestamp : com.google.firebase.Timestamp,
    val nameText : String,
    val postID : String,
    val writerID : String
)
package com.jinhyun.ftx.data

import com.google.firebase.Timestamp

data class CommentData(
    val comment : String,
    val timestamp : Timestamp,
    val writerID : String
)
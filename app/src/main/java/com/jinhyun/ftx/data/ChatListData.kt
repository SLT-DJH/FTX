package com.jinhyun.ftx.data

import com.google.firebase.Timestamp

data class ChatListData(
    val profileImage : String,
    val nameText : String,
    val baseText : String,
    val date : Timestamp,
    val contentText : String,
    val userID : String
)
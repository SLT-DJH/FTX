package com.jinhyun.ftx.data

import com.google.firebase.Timestamp

class ChatData (
    val message : String,
    val sender : String,
    val receiver : String,
    val url : String,
    val timestamp: Timestamp
)
package com.jinhyun.ftx.data

import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class ChatData (
    val message : String,
    val sender : String,
    val receiver : String,
    val url : String,
    val timestamp: Timestamp
)
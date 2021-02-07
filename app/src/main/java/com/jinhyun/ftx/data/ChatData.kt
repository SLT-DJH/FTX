package com.jinhyun.ftx.data

import com.google.firebase.firestore.ServerTimestamp
import java.util.*

class ChatData (
    val message : String,
    val sender : String,
    val receiver : String,
    val url : String,
    @ServerTimestamp val timestamp: Date
)
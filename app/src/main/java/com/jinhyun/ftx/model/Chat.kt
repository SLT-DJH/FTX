package com.jinhyun.ftx.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp
import java.util.*

class Chat {
    private var message : String = ""
    private var sender : String = ""
    private var receiver : String = ""
    private var url : String = ""
    @ServerTimestamp private var timestamp : Date? = null

    constructor(message: String, sender: String, receiver: String, url: String, timestamp : Date) {
        this.message = message
        this.sender = sender
        this.receiver = receiver
        this.url = url
        this.timestamp = timestamp
    }

    fun getMessage() : String? {
        return message
    }

    fun setMessage(message : String?) {
        this.message = message!!
    }

    fun getSender() : String? {
        return sender
    }

    fun setSender(sender : String?) {
        this.sender = sender!!
    }

    fun getReceiver() : String? {
        return receiver
    }

    fun setReceiver(receiver : String?) {
        this.receiver = receiver!!
    }

    fun getUrl() : String? {
        return url
    }

    fun setUrl(url : String?) {
        this.url = url!!
    }

    fun getTimestamp() : Date? {
        return timestamp
    }

    fun setTimestamp(timestamp : Date?){
        this.timestamp = timestamp!!
    }


}
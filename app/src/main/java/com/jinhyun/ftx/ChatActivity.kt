package com.jinhyun.ftx

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ServerTimestamp
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_chat.*
import java.util.*

class ChatActivity : AppCompatActivity() {

    val storage = FirebaseStorage.getInstance()
    val TAG = "ChatActivity"
    val db = FirebaseFirestore.getInstance()
    val mAuth = FirebaseAuth.getInstance()
    val chatRef = db.collection("ChatLists")
    val userRef = db.collection("Users")

    private lateinit var receiver : String
    private lateinit var receiverName : String
    private lateinit var sender : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        receiver = intent.getStringExtra("receiver")
        receiverName = intent.getStringExtra("receiverName")
        sender = mAuth.uid.toString()

        tv_chatboard_title.text = receiverName

        et_chatboard_message.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                if(et_chatboard_message.text.isBlank()){
                    iv_chatboard_send.visibility = View.GONE
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                iv_chatboard_send.visibility = View.VISIBLE
            }
        })

        iv_chatboard_send.setOnClickListener {
            val message = et_chatboard_message.text.toString()

            val timestamp = Timestamp(Date())

            val messageHashMap = hashMapOf<String, Any?>(
                "message" to message,
                "sender" to sender,
                "receiver" to receiver,
                "url" to "",
                "timestamp" to timestamp
            )

            val senderChatRef = chatRef.document(sender)
                .collection("Chatrooms").document(receiver)

            val receiverChatRef = chatRef.document(receiver)
                .collection("Chatrooms").document(sender)

            senderChatRef.collection("Messages").add(messageHashMap).addOnSuccessListener {
                receiverChatRef.collection("Messages").add(messageHashMap).addOnSuccessListener {
                    et_chatboard_message.text.clear()
                }
            }
        }
    }
}
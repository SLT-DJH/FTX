package com.jinhyun.ftx

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.jinhyun.ftx.adapter.ChatAdapter
import com.jinhyun.ftx.data.ChatData
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_chat.*
import java.lang.RuntimeException
import java.util.*

class ChatActivity : AppCompatActivity() {

    val storage = FirebaseStorage.getInstance()
    val TAG = "ChatActivity"
    val db = FirebaseFirestore.getInstance()
    val mAuth = FirebaseAuth.getInstance()
    val chatRef = db.collection("ChatLists")
    val chatRoomRef = db.collection("Chatrooms")
    val userRef = db.collection("Users")

    var mAdapter : ChatAdapter? = null

    var chatList = arrayListOf<ChatData>()
    var chatIDList = arrayListOf<String>()

    var chated = false
    var chatroomID = ""

    val PERMISSION_CODE = 1001
    val IMAGE_PICK_CODE = 1000

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

        //get receiver profile image

        val storageRef = storage.getReferenceFromUrl("gs://trainingfield-ed0a1.appspot.com")
            .child("Profile/${receiver}.png")

        chatRef.document(sender).collection("Channel")
            .document(receiver).get().addOnSuccessListener {
                if(it.exists()){

                    chated = true

                    chatroomID = it.get("chatroomID").toString()

                }
            }

        storageRef.downloadUrl.addOnSuccessListener {task ->
            initMessageRecyclerView(task.toString())
        }

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
            if (!chated){

                chatroomID = chatRoomRef.document().id

                val channelInfo = hashMapOf(
                    "chatroomID" to chatroomID
                )

                chatRef.document(sender).collection("Channel")
                    .document(receiver).set(channelInfo)

                chatRef.document(receiver).collection("Channel")
                    .document(sender).set(channelInfo)

                sendMessage()
            }else{
                sendMessage()
            }

        }

        iv_chatboard_attachment.setOnClickListener {
            LN_chatboard_progress.visibility = View.VISIBLE
            if (!chated){

                chatroomID = chatRoomRef.document().id

                val channelInfo = hashMapOf(
                    "chatroomID" to chatroomID
                )

                chatRef.document(sender).collection("Channel")
                    .document(receiver).set(channelInfo)

                chatRef.document(receiver).collection("Channel")
                    .document(sender).set(channelInfo)

                changePicture()
            }else{
                changePicture()
            }
        }
    }

    private fun sendMessage(){
        val message = et_chatboard_message.text.toString()

        et_chatboard_message.text.clear()

        val timestamp = Timestamp(Date())
        val randomID = chatRoomRef.document(chatroomID).collection("Messages").document().id

        val messageHashMap = hashMapOf<String, Any?>(
            "message" to message,
            "sender" to sender,
            "receiver" to receiver,
            "url" to "",
            "timestamp" to timestamp,
            "messageID" to randomID
        )

        val messageListHashMap = hashMapOf<String, Any?>(
            "last" to message,
            "timestamp" to timestamp
        )
        chatRoomRef.document(chatroomID).set(messageListHashMap)
        chatRoomRef.document(chatroomID).collection("Messages").document(randomID)
            .set(messageHashMap)
        chatRef.document(sender).collection("Channel")
            .document(receiver).update(messageListHashMap)
        chatRef.document(receiver).collection("Channel").document(sender)
            .update(messageListHashMap)
    }

    private fun changePicture(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                if(ActivityCompat.shouldShowRequestPermissionRationale(this,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE)){
                }else{
                    ActivityCompat.requestPermissions(this,
                        arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), PERMISSION_CODE)
                }
            }else{
                pickImageFromGallery()
            }
        }else{
            pickImageFromGallery()
        }
    }

    private fun pickImageFromGallery(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.setType("image/*")
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    private fun retrieveMessage(sender : String, receiver : String, profile : String){

        chatRoomRef.document(chatroomID).collection("Messages").
                orderBy("timestamp", Query.Direction.ASCENDING).addSnapshotListener { value, error ->

            if (error != null){
                return@addSnapshotListener
            }

            for(doc in value!!){
                val chat = ChatData(doc.get("message").toString(), doc.get("sender").toString(),
                    doc.get("receiver").toString(), doc.get("url").toString(),
                    doc.get("timestamp") as Timestamp)

                if(!chatIDList.contains(doc.get("messageID").toString())){
                    chatList.add(chat)
                    chatIDList.add(doc.get("messageID").toString())
                    rv_chatboard_chat.smoothScrollToPosition(chatList.size - 1)
                }

            }

            mAdapter!!.notifyDataSetChanged()

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE){
            val imageUri = data?.data

            CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this)
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            val result = CropImage.getActivityResult(data)
            if(resultCode == Activity.RESULT_OK){
                val resultUri = result.uri

                val storageRef = storage.
                    getReferenceFromUrl("gs://trainingfield-ed0a1.appspot.com").
                    child("Message/${mAuth.uid.toString()}/${System.currentTimeMillis()}.png")

                storageRef.putFile(resultUri).addOnSuccessListener {
                    storageRef.downloadUrl.addOnSuccessListener {
                        val timestamp = Timestamp(Date())

                        val randomID = chatRoomRef.document(chatroomID).collection("Messages").document().id

                        val messageHashMap = hashMapOf<String, Any?>(
                            "message" to "",
                            "sender" to sender,
                            "receiver" to receiver,
                            "url" to it.toString(),
                            "timestamp" to timestamp,
                            "messageID" to randomID
                        )

                        val messageListHashMap = hashMapOf<String, Any?>(
                            "last" to getString(R.string.picture),
                            "timestamp" to timestamp
                        )
                        chatRef.document(sender).collection("Channel")
                            .document(receiver).update(messageListHashMap)
                        chatRef.document(receiver).collection("Channel").document(sender)
                            .update(messageListHashMap)
                        chatRoomRef.document(chatroomID).set(messageListHashMap)
                        chatRoomRef.document(chatroomID).collection("Messages").document(randomID)
                            .set(messageHashMap).addOnSuccessListener {
                            LN_chatboard_progress.visibility = View.INVISIBLE
                        }

                    }
                }
            }
        }else{
            LN_chatboard_progress.visibility = View.INVISIBLE
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            PERMISSION_CODE -> {
                if(grantResults.isEmpty()){
                    throw RuntimeException("Empty Permission Result")
                }
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    pickImageFromGallery()
                }else{
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun initMessageRecyclerView(profile : String){

        mAdapter = ChatAdapter(this, chatList, profile)
        mAdapter!!.setHasStableIds(true)
        rv_chatboard_chat.adapter = mAdapter
        var LN = LinearLayoutManager(applicationContext)
        LN.stackFromEnd = true
        rv_chatboard_chat.layoutManager = LN

        retrieveMessage(sender, receiver, profile)
    }
}
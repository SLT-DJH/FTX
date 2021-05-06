package com.jinhyun.ftx.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.jinhyun.ftx.ChatActivity
import com.jinhyun.ftx.R
import com.jinhyun.ftx.adapter.ChatListAdapter
import com.jinhyun.ftx.data.ChatListData
import kotlinx.android.synthetic.main.fragment_chat.view.*
import java.text.FieldPosition
import java.text.SimpleDateFormat
import java.util.*

class ChatFragment : Fragment() , ChatListAdapter.OnItemClickListener{

    var chatlistList = arrayListOf<ChatListData>()
    val db = FirebaseFirestore.getInstance()
    val mAuth = FirebaseAuth.getInstance()
    val userRef = db.collection("Users")
    val chatlistRef = db.collection("ChatLists").document(mAuth.currentUser!!.uid)
        .collection("Channel")

    var TAG = "ChatFragment"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_chat, container, false)

        activity?.findViewById<TextView>(R.id.toolbar_title)?.setText(R.string.chat)

        setHasOptionsMenu(true)

        chatlistRef.orderBy("timestamp", Query.Direction.DESCENDING).addSnapshotListener { value, error ->
            if(error != null){
                return@addSnapshotListener
            }

            chatlistList = arrayListOf()

            val mAdapter = ChatListAdapter(activity!!.applicationContext, chatlistList, this)
            view.chatlistRecyclerView.adapter = mAdapter
            view.chatlistRecyclerView.layoutManager = LinearLayoutManager(activity!!.applicationContext)

            for(doc in value!!){

                var userName  = ""
                var userBase  = ""
                var userImage = ""

                userRef.document(doc.id).get().addOnSuccessListener {
                    userName = it.get("name").toString()
                    userBase = it.get("base").toString()
                    userImage = it.get("profile").toString()

                    val chatList = ChatListData(userImage, userName, userBase,
                        doc.get("timestamp") as Timestamp, doc.get("last").toString(), doc.id)

                    chatlistList.add(chatList)

                    mAdapter.notifyDataSetChanged()
                }


            }
        }


        return view
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater?.inflate(R.menu.menu_chat_bar, menu)
    }

    override fun onChatItemClick(position: Int) {
        Log.d(TAG, "started onChatItemClick")
        val clickItem = chatlistList[position]

        val intent = Intent(activity, ChatActivity::class.java)
        intent.putExtra("receiver", clickItem.userID)
        intent.putExtra("receiverName", clickItem.nameText)
        startActivity(intent)
    }
}
package com.jinhyun.ftx.fragment

import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.jinhyun.ftx.R
import com.jinhyun.ftx.adapter.ChatListAdapter
import com.jinhyun.ftx.data.ChatListData
import kotlinx.android.synthetic.main.fragment_chat.view.*

class ChatFragment : Fragment() {

    val chatlistList = arrayListOf<ChatListData>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_chat, container, false)

        activity?.findViewById<TextView>(R.id.toolbar_title)?.setText(R.string.chat)

        setHasOptionsMenu(true)

        chatlistList.add(ChatListData("test", "도둑냥", "공군 제10전투비행단",
            "2021. 1. 24.", "도진현님, 반갑습니다! 어쩐일로"))
        chatlistList.add(ChatListData("test", "검소한놈", "공군 제10전투비행단",
            "2021. 1. 24.", "지금 미국은 잘 시간 아님?"))
        chatlistList.add(ChatListData("test", "서로고갯집", "공군 제10전투비행단",
            "2021. 1. 24.", "필승! 보도정문 택배보관소 내 택배"))

        val mAdapter = ChatListAdapter(activity!!.applicationContext, chatlistList)
        view.chatlistRecyclerView.adapter = mAdapter

        val lm = LinearLayoutManager(activity!!.applicationContext)
        view.chatlistRecyclerView.layoutManager = lm
        view.chatlistRecyclerView.setHasFixedSize(true)

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater?.inflate(R.menu.menu_chat_bar, menu)
    }
}
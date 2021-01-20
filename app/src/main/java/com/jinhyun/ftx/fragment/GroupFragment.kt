package com.jinhyun.ftx.fragment

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.jinhyun.ftx.R
import com.jinhyun.ftx.adapter.GroupAdapter
import com.jinhyun.ftx.data.GroupData
import kotlinx.android.synthetic.main.fragment_group.view.*

class GroupFragment : Fragment() {

    val groupList = arrayListOf<GroupData>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_group, container, false)

        activity?.findViewById<TextView>(R.id.toolbar_title)?.setText(R.string.group)

        setHasOptionsMenu(true)

        groupList.add(GroupData("부대생활질문", "2021. 1. 16.", "test",
            "도진현", "내일이 전역인데 피복 반납은 어떻게 하는 건가요?", "test"))
        groupList.add(GroupData("분실센터", "2021. 1. 16.", "test",
            "김군대", "지갑을 분실했습니다!!", "test"))
        groupList.add(GroupData("고양이", "2021. 1. 16.", "test",
            "이고양", "수송대대 앞 고양이가 많이 귀엽습니다 ^^", "test"))

        val mAdapter = GroupAdapter(activity!!.applicationContext, groupList)
        view.groupRecyclerView.adapter = mAdapter

        val lm = LinearLayoutManager(activity!!.applicationContext)
        view.groupRecyclerView.layoutManager = lm
        view.groupRecyclerView.setHasFixedSize(true)

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater?.inflate(R.menu.menu_group_bar, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.group_search -> {
                return true
            }
            R.id.group_tune -> {
                return true
            }
            R.id.group_alert -> {
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
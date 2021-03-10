package com.jinhyun.ftx.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.jinhyun.ftx.NewPostActivity
import com.jinhyun.ftx.R
import com.jinhyun.ftx.SelectedPostActivity
import com.jinhyun.ftx.adapter.GroupAdapter
import com.jinhyun.ftx.data.GroupData
import kotlinx.android.synthetic.main.custom_group_list.view.*
import kotlinx.android.synthetic.main.fragment_group.view.*

class GroupFragment : Fragment(), GroupAdapter.OnItemClickListener {

    var groupList = arrayListOf<GroupData>()

    val TAG = "GroupFragment"

    val db = FirebaseFirestore.getInstance()
    val userRef = db.collection("Users")
    var base = ""
    var userprofile = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_group, container, false)

        activity?.findViewById<TextView>(R.id.toolbar_title)?.setText(R.string.group)

        setHasOptionsMenu(true)

        base = arguments?.getString("base").toString()

        // postref 설정
        val postRef = db.collection("Missions")
            .document(base).collection("Posts")

//        val mAdapter = GroupAdapter(activity!!.applicationContext, groupList, this)
//        mAdapter.setHasStableIds(true)
//        view.groupRecyclerView.adapter = mAdapter
//
//        val lm = LinearLayoutManager(activity!!.applicationContext)
//        view.groupRecyclerView.layoutManager = lm

        postRef.orderBy("timestamp", Query.Direction.DESCENDING).get().addOnSuccessListener { document ->
            if(document.isEmpty){
                return@addOnSuccessListener
            }

            groupList = arrayListOf()

            val mAdapter = GroupAdapter(activity!!.applicationContext, groupList, this, base)
            mAdapter.setHasStableIds(true)
            view.groupRecyclerView.adapter = mAdapter
            view.groupRecyclerView.layoutManager = LinearLayoutManager(activity!!.applicationContext)

            for (doc in document){
                userRef.document(doc.get("writer").toString()).get().addOnSuccessListener {
                    val name = it.get("name").toString()

                    val post = GroupData(doc.get("category").toString(), doc.get("content").toString(),
                        doc.get("imageurl").toString(), doc.getTimestamp("timestamp") as Timestamp,
                        name, doc.get("postID").toString(), doc.get("writer").toString())

                    groupList.add(post)
                    Log.d(TAG, "added! $groupList")

                    mAdapter.notifyDataSetChanged()
                }

            }
        }

        view.btn_post_create.setOnClickListener {
            val intent = Intent(activity, NewPostActivity::class.java)
            intent.putExtra("base", arguments?.getString("base").toString())
            startActivity(intent)
        }

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

    override fun onItemClick(position: Int, view : View) {
        val clickedItem = groupList[position]

        val intent = Intent(activity, SelectedPostActivity::class.java)
        intent.putExtra("category", clickedItem.categoryText)
        intent.putExtra("name", clickedItem.nameText)
        intent.putExtra("imageUrl", clickedItem.postImage)
        intent.putExtra("content", clickedItem.postText)
        intent.putExtra("time", view.tv_update_date.text)
        intent.putExtra("postID", clickedItem.postID)
        intent.putExtra("base", base)
        intent.putExtra("writerID", clickedItem.writerID)
        startActivity(intent)
    }
}
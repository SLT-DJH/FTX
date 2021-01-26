package com.jinhyun.ftx

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.android.synthetic.main.activity_base_find.*

class BaseFindActivity : AppCompatActivity() {

    val TAG = "BaseFindActivity"

    val db = FirebaseFirestore.getInstance()
    val missionsCollection = db.collection("Missions")
    val mAuth = FirebaseAuth.getInstance()
    val userDoc = db.collection("Users").document(mAuth.uid.toString())
    val baseList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base_find)

        val search = findViewById<SearchView>(R.id.sv_base_find)
        val listview = findViewById<ListView>(R.id.lv_base_list)

        missionsCollection.addSnapshotListener { value, error ->

            if(error != null){
                Log.d(TAG, "calling mission collection failed : $error")
                return@addSnapshotListener
            }

            for (doc in value!!){
                baseList.add(doc.id)
            }

            if(baseList != null){
                val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, baseList)
                listview.adapter = adapter

                search.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                    override fun onQueryTextChange(newText: String?): Boolean {
                        adapter.filter.filter(newText)
                        return false
                    }

                    override fun onQueryTextSubmit(query: String?): Boolean {
                        search.clearFocus()
                        if(baseList.contains(query)){
                            adapter.filter.filter(query)
                        }else{
                            Toast.makeText(applicationContext, R.string.no_base, Toast.LENGTH_SHORT).show()
                        }
                        return false
                    }
                })
            }

            listview.setOnItemClickListener { parent, view, position, id ->
                LN_find_base_progress.visibility = View.VISIBLE
                val element = parent.getItemAtPosition(position).toString()
                Log.d(TAG, "clicked! : $element")
                userDoc.set(hashMapOf(
                    "base" to element
                ), SetOptions.merge()).addOnSuccessListener {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    LN_find_base_progress.visibility = View.INVISIBLE
                    finish()
                }.addOnFailureListener {
                    Toast.makeText(this, "error!", Toast.LENGTH_SHORT).show()
                    LN_find_base_progress.visibility = View.INVISIBLE
                }
            }

        }
    }
}
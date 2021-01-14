package com.jinhyun.ftx.fragment

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.jinhyun.ftx.LoginActivity
import com.jinhyun.ftx.ProfileEditActivity
import com.jinhyun.ftx.R
import kotlinx.android.synthetic.main.fragment_profile.view.*

class ProfileFragment : Fragment() {

    val TAG = "ProfileFragment"

    val mAuth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    val userref = db.collection("Users").document(mAuth.uid.toString())
    val storage = FirebaseStorage.getInstance()
    val storageRef = storage.getReferenceFromUrl("gs://trainingfield-ed0a1.appspot.com")
        .child("Profile/${mAuth.currentUser?.uid.toString()}.png")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        view.tv_profile_name.text = arguments?.getString("name")

        //retrieve profile image
        val profileImage = view.findViewById<ImageView>(R.id.iv_profile_image)

        storageRef.downloadUrl.addOnSuccessListener { task ->
            context?.let {
                Glide.with(it).load(task).into(profileImage)
                Log.d(TAG, "result : $task")
            }
        }

        view.iv_profile_setting.setOnClickListener {
            val intent = Intent(activity, ProfileEditActivity::class.java)
            intent.putExtra("name", arguments?.getString("name"))

            startActivity(intent)
        }

        view.logout_layout.setOnClickListener {
            mAuth.signOut()
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
            activity!!.finish()
        }

        view.deleteuser_layout.setOnClickListener {
            showDeleteAlert()
        }

        return view
    }

    private fun showDeleteAlert(){
        val inflater = activity!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.delete_user_popup, null)

        val alertDialog = AlertDialog.Builder(context)
            .setTitle(R.string.delete_user)
            .setPositiveButton(R.string.confirm){dialog, which ->
                userref.delete().addOnSuccessListener {
                    Log.d(TAG, "userref deleted!")
                }

                mAuth.currentUser!!.delete().addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        Toast.makeText(context, R.string.user_deleted, Toast.LENGTH_SHORT).show()

                        val intent = Intent(activity, LoginActivity::class.java)
                        startActivity(intent)
                        activity!!.finish()
                    }
                }
            }.setNegativeButton(R.string.cancel, null).create()

        alertDialog.setView(view)
        alertDialog.show()
    }
}
package com.jinhyun.ftx.fragment

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.jinhyun.ftx.BaseFindActivity
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

        activity?.findViewById<TextView>(R.id.toolbar_title)?.setText(R.string.profile)

        setHasOptionsMenu(true)

        view.tv_profile_name.text = arguments?.getString("name")
        if(arguments?.getBoolean("access") == true){
            view.tv_profile_access.visibility = View.GONE
        }

        //retrieve profile image
        val profileImage = view.findViewById<ImageView>(R.id.iv_profile_image)

        userref.addSnapshotListener { value, error ->
            if(error != null){
                return@addSnapshotListener
            }

            if(value!!.get("profile") != ""){
                Glide.with(activity!!.applicationContext).load(value!!.get("profile")).into(profileImage)
            }else{
                profileImage.setImageResource(R.drawable.default_profile)
            }
        }

//        storageRef.downloadUrl.addOnSuccessListener { task ->
//            context?.let {
//                Glide.with(it).load(task).into(profileImage)
//                Log.d(TAG, "result : $task")
//            }
//        }

        view.tv_profile_access.setOnClickListener {
            Toast.makeText(activity, "프로필 인증 절차 ㄱ", Toast.LENGTH_SHORT).show()
        }

        view.iv_profile_image.setOnClickListener {
            gotoSetting()
        }

        view.LN_base_setting.setOnClickListener {
            showBaseSettingAlert()
        }

        view.LN_logout.setOnClickListener {
            mAuth.signOut()
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
            activity!!.finish()
        }

        view.LN_deleteuser.setOnClickListener {
            showDeleteAlert()
        }

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater?.inflate(R.menu.menu_profile_bar, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.profile_setting -> {
                gotoSetting()

                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun gotoSetting(){
        val intent = Intent(activity, ProfileEditActivity::class.java)
        intent.putExtra("name", arguments?.getString("name"))

        startActivity(intent)
    }

    private fun showBaseSettingAlert(){
        val alertDialog = AlertDialog.Builder(context)
            .setTitle(R.string.base_select)
            .setMessage(R.string.ask_base_select).setPositiveButton(R.string.confirm){ dialog, which ->
                val intent = Intent(activity, BaseFindActivity::class.java)
                startActivity(intent)
                activity!!.finish()
            }.setNegativeButton(R.string.cancel, null).create()

        alertDialog.show()
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
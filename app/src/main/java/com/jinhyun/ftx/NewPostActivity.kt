package com.jinhyun.ftx

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.jinhyun.ftx.dialog.CategoryPostDialog
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.activity_new_post.*
import java.lang.RuntimeException
import java.util.*

class NewPostActivity : AppCompatActivity() {

    val storage = FirebaseStorage.getInstance()
    val db = FirebaseFirestore.getInstance()
    val mAuth = FirebaseAuth.getInstance()
    val storageRef = storage.getReferenceFromUrl("gs://trainingfield-ed0a1.appspot.com")
        .child("Post/${mAuth.currentUser?.uid.toString()}/${System.currentTimeMillis()}}.png")

    val TAG = "NewPostActivity"

    private var category = ""
    private var postcontent = ""

    val PERMISSION_CODE = 1001
    val IMAGE_PICK_CODE = 1000

    var imageUrl = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_post)

        val userBase = intent.getStringExtra("base").toString()

        val postRef = db.collection("Missions").document(userBase)
            .collection("Posts")

        LN_new_post_category.setOnClickListener {
            val dlg = CategoryPostDialog(this)
            dlg.setOnOKClickedListener { content ->
                tv_new_post_category.text = content
                category = content
            }
            dlg.start()
        }

        tv_view_principle.setOnClickListener {
            val dlg = Dialog(this)
            dlg.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dlg.setContentView(R.layout.principle_popup)
            dlg.setCancelable(true)

            dlg.show()

        }

        iv_new_post_back.setOnClickListener {
            onBackPressed()
        }

        btn_new_post_add_photo.setOnClickListener {
            changePicture()
        }

        cv_new_post_image_delete.setOnClickListener {
            container_new_post_image.visibility = View.GONE
            LN_new_post_add_photo.visibility = View.VISIBLE
        }

        tv_new_post_done.setOnClickListener {

            LN_new_post_progress.visibility = View.VISIBLE

            postcontent = et_new_post_content.text.toString()

            if(postcontent == "" || category == ""){
                Toast.makeText(this, R.string.request_email_pw, Toast.LENGTH_SHORT).show()
                LN_new_post_progress.visibility = View.INVISIBLE
                return@setOnClickListener
            }

            if(!btn_check_principle.isChecked){
                Toast.makeText(this, R.string.request_check_sns_principle, Toast.LENGTH_SHORT).show()
                LN_new_post_progress.visibility = View.INVISIBLE
                return@setOnClickListener
            }

            if (imageUrl == ""){
                val timestamp = Timestamp(Date())
                val randomID = postRef.document().id

                val post = hashMapOf(
                    "imageurl" to "",
                    "category" to category,
                    "content" to postcontent,
                    "timestamp" to timestamp,
                    "writer" to mAuth.uid.toString(),
                    "postID" to randomID
                )

                postRef.document(randomID).set(post).addOnSuccessListener {
                    Toast.makeText(this, "게시글을 등록하였습니다.", Toast.LENGTH_SHORT).show()
                    LN_new_post_progress.visibility = View.INVISIBLE
                    finish()
                }.addOnFailureListener {
                    Toast.makeText(this, "게시글 등록에 실패하였습니다.", Toast.LENGTH_SHORT).show()
                }

            }else{
                storageRef.putFile(Uri.parse(imageUrl)).addOnSuccessListener { task ->
                    storageRef.downloadUrl.addOnSuccessListener {
                        val timestamp = Timestamp(Date())
                        val randomID = postRef.document().id

                        val post = hashMapOf(
                            "imageurl" to it.toString(),
                            "category" to category,
                            "content" to postcontent,
                            "timestamp" to timestamp,
                            "writer" to mAuth.uid.toString(),
                            "postID" to randomID
                        )

                        postRef.document(randomID).set(post).addOnSuccessListener {
                            Toast.makeText(this, "게시글을 등록하였습니다.", Toast.LENGTH_SHORT).show()
                            LN_new_post_progress.visibility = View.INVISIBLE
                            finish()
                        }.addOnFailureListener {
                            Toast.makeText(this, "게시글 등록에 실패하였습니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }.addOnFailureListener{
                    Toast.makeText(this, "게시글 등록에 실패하였습니다.", Toast.LENGTH_SHORT).show()
                }
            }

        }
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
                val imageView = findViewById<ImageView>(R.id.iv_new_post_image)

                Glide.with(this).load(resultUri).into(imageView)

                imageUrl = resultUri.toString()

                container_new_post_image.visibility = View.VISIBLE
                LN_new_post_add_photo.visibility = View.GONE
            }
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
}
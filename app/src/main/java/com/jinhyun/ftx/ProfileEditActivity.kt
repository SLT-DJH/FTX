package com.jinhyun.ftx

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_profileedit.*
import java.lang.RuntimeException
import java.util.jar.Manifest

class ProfileEditActivity : AppCompatActivity() {

    val TAG = "ProfileEditActivity"

    val mAuth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    val userref = db.collection("Users").document(mAuth.uid.toString())
    val storage = FirebaseStorage.getInstance()
    val storageRef = storage.getReferenceFromUrl("gs://trainingfield-ed0a1.appspot.com")
        .child("Profile/${mAuth.currentUser?.uid.toString()}.png")

    val PERMISSION_CODE = 1001
    val IMAGE_PICK_CODE = 1000

    var imageUri = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profileedit)

        //사진 받아오기
        val profileImage = findViewById<ImageView>(R.id.iv_edit_profile_image)

        storageRef.downloadUrl.addOnSuccessListener { task ->
            applicationContext?.let{
                Glide.with(it).load(task).into(profileImage)
            }
        }

        //이름 받아오기
        et_edit_profile_name.setText(intent.extras!!.getString("name"))

        container_edit_profile.setOnClickListener {
            changePicture()
        }

        iv_edit_profile_save.setOnClickListener {
            if(et_edit_profile_name.text.isEmpty()){
                Toast.makeText(this,R.string.request_name,Toast.LENGTH_SHORT).show()
            }else{
                progressbar_edit_profile.visibility = View.VISIBLE

                userref.update("name", et_edit_profile_name.text.toString()).addOnSuccessListener {
                    if(imageUri != ""){
                        //이미지 변경이 있을 시
                        storageRef.putFile(Uri.parse(imageUri)).addOnSuccessListener {
                            progressbar_edit_profile.visibility = View.INVISIBLE
                            Toast.makeText(this,R.string.change_complete,Toast.LENGTH_SHORT).show()
                        }.addOnFailureListener{
                            progressbar_edit_profile.visibility = View.INVISIBLE
                            Toast.makeText(this,R.string.change_failed,Toast.LENGTH_SHORT).show()
                        }
                    }else{
                        //이름만 바꿀 시
                        progressbar_edit_profile.visibility = View.INVISIBLE
                        Toast.makeText(this,R.string.change_complete,Toast.LENGTH_SHORT).show()
                    }
                }.addOnFailureListener {
                    Log.d(TAG,"failed : ${it.message}")
                    progressbar_edit_profile.visibility = View.INVISIBLE
                    Toast.makeText(this,R.string.change_failed,Toast.LENGTH_SHORT).show()
                }

            }
        }

        iv_edit_profile_back.setOnClickListener {
            onBackPressed()
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
                .setAspectRatio(1,1)
                .start(this)
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            val result = CropImage.getActivityResult(data)
            if(resultCode == Activity.RESULT_OK){
                val resultUri = result.uri
                val profileImage  = findViewById<ImageView>(R.id.iv_edit_profile_image)

                Glide.with(this).load(resultUri).into(profileImage)

                Log.d(TAG, "result Uri : $resultUri")
                imageUri = resultUri.toString()
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
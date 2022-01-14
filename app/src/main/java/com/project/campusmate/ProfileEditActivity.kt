package com.project.campusmate

import android.app.Activity
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.project.campusmate.databinding.ActivityProfileEditBinding

class ProfileEditActivity : AppCompatActivity() {

    //view binding
    private lateinit var binding: ActivityProfileEditBinding

    //firebase Auth
    private lateinit var firebaseAuth: FirebaseAuth

    //image uri (which we will pick)
    private var imageUri: Uri?= null

    //progress bar
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //setup progress dialog
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please Wait")
        progressDialog.setCanceledOnTouchOutside(false)

        //init firebase
        firebaseAuth = FirebaseAuth.getInstance()
        loadUserInfo()

        //handle click back
        binding.backBtn.setOnClickListener {
            onBackPressed()
        }

        //handle click pick image
       /* binding.profileTv.setOnClickListener {
            showImageAttachMenu()
        } */

        //handle click update
        binding.updateBtn.setOnClickListener {
            validateData()
        }
    }

    private var name = ""
    private fun validateData() {
        //get data
        name = binding.nameEt.text.toString().trim()

        //validate data
        if (name.isEmpty()){
            Toast.makeText(this, "Enter name",Toast.LENGTH_SHORT).show()
        }
        else {

            if (imageUri == null){
            //update without image
            updateProfile("")
            }
            else {
                //update with image
                uploadImage()

            }
        }
    }

    private fun uploadImage() {
        progressDialog.setMessage("Uploading image")
        progressDialog.show()

        //image path and name
        val filePathAndName = "ProfileImages/"+firebaseAuth.uid

        //storage reference
        val reference = FirebaseStorage.getInstance().getReference(filePathAndName)
        reference.putFile(imageUri!!)
            .addOnSuccessListener { taskSnapshot->

                val uriTask: Task<Uri> = taskSnapshot.storage.downloadUrl
                while (!uriTask.isSuccessful);
                val uploadedImageUrl = "${uriTask.result}"

                updateProfile(uploadedImageUrl)

            }
            .addOnFailureListener {e->
                progressDialog.dismiss()
                Toast.makeText(this, "Failed to upload image due to ${e.message}",Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateProfile(uploadedImageUri: String) {
        progressDialog.setMessage("Updating profile")

        //info to update to db
        val hashmap: HashMap<String, Any> = HashMap()
        hashmap["name"] = "$name"
        if (imageUri != null){
            hashmap["profileImage"] = uploadedImageUri
        }
        //update to db
        val reference = FirebaseDatabase.getInstance().getReference("Users")
        reference.child(firebaseAuth.uid!!)
            .updateChildren(hashmap)
            .addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(this, "Profile Updated",Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {e->
                progressDialog.dismiss()
                Toast.makeText(this, "Failed to update profile due to ${e.message}",Toast.LENGTH_SHORT).show()

            }
    }

    private fun loadUserInfo() {
        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.child(firebaseAuth.uid!!)
            .addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val name = "${snapshot.child("name").value}"
                   // val profileImage = "${snapshot.child("profileImage").value}"
                    val timestamp = "${snapshot.child("timestamp").value}"

                    //set data
                    binding.nameEt.setText(name)

                 /*   //set image
                    try {
                        Glide.with(this@ProfileEditActivity).load(profileImage)
                            .placeholder(R.drawable.ic_person_gray).into(binding.profileTv)
                    }
                    catch (e: Exception){

                    }  */
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }

 /*   private fun showImageAttachMenu(){
        //setup popup menu
        val popupMenu = PopupMenu(this, binding.profileTv)
        popupMenu.menu.add(Menu.NONE, 0,0,"Camera")
        popupMenu.menu.add(Menu.NONE, 1,1,"Gallery")
        popupMenu.show()

        //handle popup menu
        popupMenu.setOnMenuItemClickListener { item->
            //get id of clicked item
            val id = item.itemId
            if (id == 0){
                //camera
                pickImageCamera()
            }
            else if (id == 1){
                //gallery
                pickImageGallery()
            }
            true
        }
    }  */

 /*   private fun pickImageCamera() {
        //intent to pick from camera
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "Temp_title")
        values.put(MediaStore.Images.Media.DESCRIPTION, "Temp_Description")

        imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        cameraActivityResultLauncher.launch(intent)
        galleryActivityResultLauncher.launch(intent)
    }   */

 /*   private fun pickImageGallery() {
        //intent to pick from gallery
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/(should add star symbol)"
    }  */
        //handle result of camera
 /*   private val cameraActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback<ActivityResult>{result ->
        //get uri of image
        if (result.resultCode == Activity.RESULT_OK){
            val data = result.data
            //imageUri = data!!.data

            //set to imageview
            binding.profileTv.setImageURI(imageUri)
        }
            else {
                Toast.makeText(this, "Cancelled",Toast.LENGTH_SHORT).show()
        }
        }
    )  */

 /*   //handle result of gallery
    private val galleryActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback<ActivityResult> {result ->
            //get uri of image
            if (result.resultCode == Activity.RESULT_OK){
                val data = result.data
                imageUri = data!!.data

                //set to imageview
                binding.profileTv.setImageURI(imageUri)
            }
            else {
                Toast.makeText(this, "Cancelled",Toast.LENGTH_SHORT).show()
            }
        }
    )  */
}
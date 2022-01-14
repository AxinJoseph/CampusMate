package com.project.campusmate

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.project.campusmate.databinding.ActivityForgotPasswordBinding

class ForgotPasswordActivity : AppCompatActivity() {

    //view Binding
    private lateinit var binding: ActivityForgotPasswordBinding

    //firebase Auth
    private lateinit var firebaseAuth: FirebaseAuth

    //progress dialog
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance()

        //init progress dialog, while login
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setCanceledOnTouchOutside(false)

        //handle click goback
        binding.backBtn.setOnClickListener {
            onBackPressed()
        }

        //handle click recovery
        binding.submitBtn.setOnClickListener {
            validateData()
        }
    }

    private var email =""

    private fun validateData() {
        //get data
        email = binding.emailEt.text.toString().trim()

        //validate data
        if (email.isEmpty()){
            Toast.makeText(this, "Enter Email-Id", Toast.LENGTH_SHORT).show()
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            //invalid email
            Toast.makeText(this, "Invalid Email-Id", Toast.LENGTH_SHORT).show()
        }
        else {
            recoverPassword()
        }
    }

    private fun recoverPassword() {
        progressDialog.setMessage("Sending Password recovery link to $email")
        progressDialog.show()

        //send recovery
        firebaseAuth.sendPasswordResetEmail(email)
            .addOnSuccessListener {
                //success
                progressDialog.dismiss()
                Toast.makeText(this, "Password reset link send to $email", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e->
                progressDialog.dismiss()
                Toast.makeText(this, "Failed to send reset mail due to ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
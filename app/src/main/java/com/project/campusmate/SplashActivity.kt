package com.project.campusmate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.project.campusmate.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {


    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        firebaseAuth = FirebaseAuth.getInstance()

        Handler().postDelayed(Runnable {
            checkUser()
        },1000) // 1 seconds splash screen
    }

    private fun checkUser() {
        //get user log info
        val firebaseUser = firebaseAuth.currentUser
        if(firebaseUser == null){
            //not logged in
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        else {
            //logged in, check user type
            val ref = FirebaseDatabase.getInstance().getReference("Users")
            ref.child(firebaseUser.uid)
                .addListenerForSingleValueEvent(object : ValueEventListener {

                    override fun onDataChange(snapshot: DataSnapshot) {


                        //get user type
                        val userType = snapshot.child("userType").value
                        if (userType == "user"){
                            startActivity(Intent(this@SplashActivity, DashboardUserActivity::class.java))
                            finish()
                        }
                        else if (userType == "admin"){
                            startActivity(Intent(this@SplashActivity, DashboardAdminActivity::class.java))
                            finish()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }
                })
        }
    }
}


package com.project.campusmate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.project.campusmate.databinding.ActivityPdfDetailUserBinding

class PdfDetailUserActivity : AppCompatActivity() {


    private lateinit var binding: ActivityPdfDetailUserBinding

    private var PlacementId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPdfDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        PlacementId = intent.getStringExtra("PlacementId")!!

        MyApplication.incrementPlacementViewCount(PlacementId)

        loadPlacementDetails()

        //handle back button
        binding.backBtn.setOnClickListener {
            onBackPressed()
        }
    }

    private fun loadPlacementDetails() {
        //Placement > PlacementId > details
        val ref = FirebaseDatabase.getInstance().getReference("Placements")
        ref.child(PlacementId)
            .addListenerForSingleValueEvent(object: ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    //get data
                    val categoryId = "${snapshot.child("categoryId").value}"
                    val description = "${snapshot.child("description").value}"
                    val timestamp = "${snapshot.child("timestamp").value}"
                    val title = "${snapshot.child("title").value}"
                    val uid = "${snapshot.child("uid").value}"
                    val url = "${snapshot.child("url").value}"

                    // val id = snapshot.child("id").value

                    //format date
                    val date = MyApplication.formatTimeStamp(timestamp.toLong())

                    //load pdf category
                    MyApplication.loadCategory(categoryId, binding.categoryTv)

                    //load pdf thumbnail
                    //MyApplication.loadPdfFromUrlSinglePage("$url", "$title", binding.pdfView, binding.progressBar)

                    //set data
                    binding.titleTv.text = title
                    binding.descriptionTv.text = description
                    binding.dateTv.text = date

                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }
}
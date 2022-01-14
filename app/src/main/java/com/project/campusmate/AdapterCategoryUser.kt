package com.project.campusmate

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.constraintlayout.helper.widget.Carousel
import androidx.recyclerview.widget.RecyclerView
import com.project.campusmate.databinding.RowCategoryBinding
import com.project.campusmate.databinding.RowCategoryUserBinding

class AdapterCategoryUser: RecyclerView.Adapter<AdapterCategoryUser.HolderCategory>, Filterable{

    private val context: Context
    public var categoryArrayList: ArrayList<ModelCategory>
    private var filterList: ArrayList<ModelCategory>

    private var filter: FilterCategoryUser? =null


    private lateinit var binding: RowCategoryUserBinding
    //constructor
    constructor(context: Context, categoryArrayList: ArrayList<ModelCategory>) {
        this.context = context
        this.categoryArrayList = categoryArrayList
        this.filterList = categoryArrayList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderCategory {
       binding = RowCategoryUserBinding.inflate(LayoutInflater.from(context), parent, false)

        return HolderCategory(binding.root)
    }

    override fun onBindViewHolder(holder: HolderCategory, position: Int) {

        //get data
        val model = categoryArrayList[position]
        val id = model.id
        val category = model.category
        val uid = model.uid
        val timestamp = model.timestamp

        //set data
        holder.userCategoryTv.text = category

        holder.itemView.setOnClickListener {
            val intent = Intent(context, PdfListUserActivity::class.java)
            intent.putExtra("categoryId", id)
            intent.putExtra("category", category)
            context.startActivity(intent)

        }

    }

    override fun getItemCount(): Int {
        return categoryArrayList.size
    }



    inner class HolderCategory(itemView: View): RecyclerView.ViewHolder(itemView){

        var userCategoryTv:TextView = binding.userCategoryTv
    }

    override fun getFilter(): Filter {
        if (filter == null){
            filter = FilterCategoryUser(filterList, this)
        }
        return filter as FilterCategoryUser
    }

}
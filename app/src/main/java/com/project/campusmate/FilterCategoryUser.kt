package com.project.campusmate

import android.widget.Filter

class FilterCategoryUser: Filter {

    private var filterList: ArrayList<ModelCategory>

    private val adapterCategoryUser: AdapterCategoryUser

    //constructor
    constructor(
        filterList: ArrayList<ModelCategory>, adapterCategoryUser: AdapterCategoryUser) : super() {
        this.filterList = filterList
        this.adapterCategoryUser = adapterCategoryUser
    }

    override fun performFiltering(constraint: CharSequence?): FilterResults {
        var constraint = constraint
        val results = FilterResults()

        if (constraint != null && constraint.isNotEmpty()){

            constraint = constraint.toString().uppercase()
            val filteredModels:ArrayList<ModelCategory> = ArrayList()
            for (i in 0 until filterList.size){

                if (filterList[i].category.uppercase().contains(constraint)){
                    //add to filtered list
                    filteredModels.add(filterList[i])
                }
            }
            results.count = filteredModels.size
            results.values = filteredModels
        }
        else {
            results.count = filterList.size
            results.values = filterList
        }
        return results
    }

    override fun publishResults(constraint: CharSequence?, results: FilterResults) {
        adapterCategoryUser.categoryArrayList = results.values as ArrayList<ModelCategory>

        adapterCategoryUser.notifyDataSetChanged()
    }


}
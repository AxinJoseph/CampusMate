package com.project.campusmate

import android.widget.Filter

class FilterCategory: Filter {

    //array for search
    private var filterlist: ArrayList<ModelCategory>

    //adapter for filtering
    private var adapterCategory: AdapterCategory

    //constructor
    constructor(filterlist: ArrayList<ModelCategory>, adapterCategory: AdapterCategory) {
        this.filterlist = filterlist
        this.adapterCategory = adapterCategory
    }

    override fun performFiltering(constraint: CharSequence?): FilterResults {
        var constraint = constraint
        val results =FilterResults()

        if (constraint !=null && constraint.isNotEmpty()){
            //value searched is null not empty

            //avoid case sensitivity
            constraint = constraint.toString().uppercase()
            val filteredModels: ArrayList<ModelCategory> = ArrayList()
            for (i in 0 until filterlist.size){
                //validate
                if (filterlist[i].category.uppercase().contains(constraint)){
                    //add to filtered list
                    filteredModels.add(filterlist[i])
                }
            }
            results.count = filteredModels.size
            results.values = filteredModels
        }
        else{
            //search value is either null or empty
            results.count = filterlist.size
            results.values = filterlist
        }
        return results
    }

    override fun publishResults(constraint: CharSequence?, results: FilterResults) {
        //apply filter changes
        adapterCategory.categoryArrayList = results.values as ArrayList<ModelCategory>

        //notify changes
        adapterCategory.notifyDataSetChanged()
    }
}
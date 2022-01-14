package com.project.campusmate

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.project.campusmate.databinding.RowPdfUserBinding

class AdapterPdfUser :RecyclerView.Adapter<AdapterPdfUser.HolderPdfUser>, Filterable{

    //context
    private var context: Context

    //array list to hold pdf
    public var pdfArrayList: ArrayList<ModelPdf>
    private val filterList:ArrayList<ModelPdf>

    //view binding
    private lateinit var binding: RowPdfUserBinding

    //filter object
    private var filter: FilterPdfUser? = null

    //constructor
    constructor(context: Context, pdfArrayList: ArrayList<ModelPdf>) : super() {
        this.context = context
        this.pdfArrayList = pdfArrayList
        this.filterList = pdfArrayList
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderPdfUser {
       //bind/inflate layout for row pdf admin.xml
        binding = RowPdfUserBinding.inflate(LayoutInflater.from(context), parent, false)

        return HolderPdfUser(binding.root)
    }

    override fun onBindViewHolder(holder: HolderPdfUser, position: Int) {
        //get data
        val model = pdfArrayList[position]
        val pdfId = model.id
        val categoryId = model.categoryId
        val title = model.title
        val description = model.description
        val pdfUrl = model.url
        val timestamp = model.timestamp
        //convert time stamp
        val formattedDate = MyApplication.formatTimeStamp(timestamp)

        //set data
        holder.titleTv.text = title
        holder.descriptionTv.text = description
        holder.dateTv.text = formattedDate

        //load category
        MyApplication.loadCategory(categoryId, holder.categoryTv)

        //load thumbnail
        MyApplication.loadPdfFromUrlSinglePage(pdfUrl, title, holder.pdfView, holder.progressBar)

        holder.itemView.setOnClickListener {
            //intent with placement id
            val intent = Intent(context, PdfDetailUserActivity::class.java)
            intent.putExtra("PlacementId", pdfId)
            context.startActivity(intent)
        }


    }

    override fun getItemCount(): Int {
        return pdfArrayList.size //items count
    }



    override fun getFilter(): Filter {
        if (filter == null){
            filter = FilterPdfUser(filterList, this)
        }
        return  filter as FilterPdfUser
    }
    //view holder for row pdf user.xml
    inner class HolderPdfUser(itemView: View) : RecyclerView.ViewHolder(itemView){
        //views of row_pdf_admin.xml
        val pdfView = binding.pdfView
        val progressBar = binding.progressBar
        val titleTv = binding.titleTv
        val descriptionTv = binding.descriptionTv
        val categoryTv = binding.categoryTv
        val dateTv = binding.dateTv
    }
}
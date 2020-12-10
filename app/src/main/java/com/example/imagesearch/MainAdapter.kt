package com.example.imagesearch

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.image.model.data.SearchImageResponse
import com.squareup.picasso.Picasso

class MainAdapter(val context: Context, val imgList: SearchImageResponse) :
    RecyclerView.Adapter<MainAdapter.Holder>() {

    interface ItemClick{
        fun onClick(view: View, position: Int)
    }
    var itemClick: ItemClick? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_image, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return imgList.documents.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(imgList.documents[position], context)
        if (itemClick != null){
            holder.itemView.setOnClickListener { v ->
                itemClick!!.onClick(v,position)
            }
        }
    }

    inner class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        val imgview = itemView?.findViewById<ImageView>(R.id.imageView)
        val picasso = Picasso.get()

        fun bind(image: SearchImageResponse.Documents, context: Context) {
            picasso.load(image.thumbnailUrl).into(imgview)
        }

    }
}
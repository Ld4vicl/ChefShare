package com.android.chefshare

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

data class SearchResult(
    val title: String,
    val description: String,
    val author: String,
    val imageUrl: String
)

class SearchResultAdapter(private val results: List<SearchResult>) :
    RecyclerView.Adapter<SearchResultAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvDescription: TextView = itemView.findViewById(R.id.tvDescription)
        val tvAuthor: TextView = itemView.findViewById(R.id.tvAuthor)
        val imgDish: ImageView = itemView.findViewById(R.id.imgDish)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_search_result, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val result = results[position]
        holder.tvTitle.text = result.title
        holder.tvDescription.text = result.description
        holder.tvAuthor.text = result.author

        Glide.with(holder.itemView.context)
            .load(result.imageUrl)
            .placeholder(R.drawable.ic_launcher_foreground) // ảnh tạm khi chưa tải xong
            .into(holder.imgDish)
    }


    override fun getItemCount(): Int = results.size
}

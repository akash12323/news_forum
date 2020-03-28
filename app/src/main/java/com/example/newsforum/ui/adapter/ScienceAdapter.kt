package com.example.newsforum.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.newsforum.R
import com.example.newsforum.data.res.entertainment.EntertainmentArticlesItem
import com.example.newsforum.data.res.science.ScienceArticlesItem
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item.view.*

class ScienceAdapter(val user:List<ScienceArticlesItem>):
    RecyclerView.Adapter<ScienceAdapter.ItemVieHolder>(){

    var onItemClick:((user: ScienceArticlesItem)->Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemVieHolder {
        return ItemVieHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item,
                parent,
                false
            )
        )
    }

    override fun getItemCount() = user.size

    override fun onBindViewHolder(holder: ItemVieHolder, position: Int) {
        holder.bind(user[position])
    }
    inner class ItemVieHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(user: ScienceArticlesItem){
            itemView.apply {
                author.text = user.author.toString()
                publishedAt.text = user.publishedAt
                title.text = user.title
                desc.text = user.description.toString()
                source.text = user.source?.name

                Picasso.get().load(user.urlToImage.toString()).into(img)
                setOnClickListener{
                    onItemClick?.invoke(user)
                }
            }
        }
    }


}

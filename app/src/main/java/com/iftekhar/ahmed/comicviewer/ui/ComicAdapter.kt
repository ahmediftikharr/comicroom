package com.iftekhar.ahmed.comicviewer.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iftekhar.ahmed.comicviewer.databinding.ComicItemLayoutBinding
import com.michaelfotiads.xkcdreader.data.db.entity.ComicEntity
import java.util.*

val monthSet = listOf<String>(
    "January",
    "February",
    "March",
    "April",
    "May",
    "June",
    "July",
    "August",
    "September",
    "October",
    "November",
    "December",
)

class ComicAdapter : RecyclerView.Adapter<CardViewHolder>() {

    private var itemClickListener: ((ComicEntity) -> Unit)? = null

    fun setOnItemCLickListener(listener: (ComicEntity) -> Unit) {
        itemClickListener = listener
    }

    var models =  listOf<ComicEntity>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        val binding = ComicItemLayoutBinding.inflate(layoutInflater, parent, false)
        return CardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bind(models[position])
        holder.itemView.setOnClickListener {
            itemClickListener?.invoke(models[holder.adapterPosition])
        }
    }
    fun update(list: List<ComicEntity>) {
        this.models = list
        notifyDataSetChanged()
    }
    override fun getItemCount() = models.size
}

class CardViewHolder internal constructor(private val binding: ComicItemLayoutBinding) :
    RecyclerView.ViewHolder(binding.root) {
    internal fun bind(model: ComicEntity) {
        binding.textTitle.text = model.title
        val monthINdex = model.month.toIntOrNull() ?: 2 - 1

//        binding.textDate.text = "${model.day} ${monthSet[monthINdex]} ${model.year}"
        binding.textDescription.text = model.alt
        Glide.with(binding.root.context).load(model.img).into(binding.imageComic)
    }
}

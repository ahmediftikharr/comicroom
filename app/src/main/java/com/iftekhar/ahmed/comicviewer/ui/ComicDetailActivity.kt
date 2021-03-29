package com.iftekhar.ahmed.comicviewer.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.iftekhar.ahmed.comicviewer.App
import com.iftekhar.ahmed.comicviewer.R
import com.iftekhar.ahmed.comicviewer.databinding.ActivityComicDetailBinding
import com.michaelfotiads.xkcdreader.data.db.entity.ComicEntity
import glideLoad
import kotlinx.coroutines.launch
import toast

class ComicDetailActivity : BaseActivity() {
    var isCurrentComicFavourite: Boolean = false

    companion object {
        var sComicEntity: ComicEntity? = null

        @JvmStatic
        fun start(context: Context, model: ComicEntity) {
            val starter = Intent(context, ComicDetailActivity::class.java)
            sComicEntity = model
            context.startActivity(starter)
        }
    }


    lateinit var binding: ActivityComicDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityComicDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        updateUI()
        checkIsFavourite()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Comic Detail"
        binding.imageviewShare.setOnClickListener { sComicEntity?.let { share(it) } }
        binding.imageviewFavourite.setOnClickListener { sComicEntity?.let { toggleFavourite(it) } }
        binding.buttonExplation.setOnClickListener { toast("Not Implemented Yet") }
        binding.imageComic.setOnClickListener {
            sComicEntity?.img?.let {
                FullScreenImageActivity.start(this, arrayListOf(it), 0)
            }
        }
    }

    private fun updateUI() {
        if (sComicEntity != null) {
            binding.textTitle.text = sComicEntity!!.title
            val monthINdex = sComicEntity!!.month.toIntOrNull() ?: 2 - 1
            binding.textDate.text =
                "${sComicEntity!!.day} ${monthSet[monthINdex]} ${sComicEntity!!.year}"
            binding.textDescription.text = sComicEntity!!.alt

            glideLoad(sComicEntity!!.img,binding.progressImageLoad,binding.imageComic)
        }


    }


    private fun toggleFavourite(it: ComicEntity) = lifecycleScope.launch {
        if (isCurrentComicFavourite) {
            App.instance.db.comicsDao().deleteById(it.num)
            isCurrentComicFavourite = false
            toast("Removed From Favorites")
        } else {
            App.instance.db.comicsDao().insert(it)
            isCurrentComicFavourite = true
            toast("Added From Favorites")
        }
        updateFavIcon()
    }

    private fun checkIsFavourite() = lifecycleScope.launch {
        isCurrentComicFavourite =
            sComicEntity?.let { App.instance.db.comicsDao().getForId(it.num) != null } ?: false
        updateFavIcon()
    }

    private fun updateFavIcon() {
        binding.imageviewFavourite.setImageResource(
            if (isCurrentComicFavourite) R.drawable.ic_baseline_favorite_24 else R.drawable.ic_baseline_favorite_border_24
        )
    }
}
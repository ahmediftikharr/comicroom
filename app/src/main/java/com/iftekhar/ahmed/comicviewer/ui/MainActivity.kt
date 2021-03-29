package com.iftekhar.ahmed.comicviewer.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.iftekhar.ahmed.comicviewer.App
import com.iftekhar.ahmed.comicviewer.R
import com.iftekhar.ahmed.comicviewer.databinding.ActivityMainBinding
import com.iftekhar.ahmed.comicviewer.viewmodel.MainViewModel
import com.michaelfotiads.xkcdreader.data.db.entity.ComicEntity
import glideLoad
import kotlinx.coroutines.launch
import toast

class MainActivity : BaseActivity() {
    var currentComicId: Int = -1
    var isCurrentComicFavourite: Boolean = false
    var currentComic: ComicEntity? = null
    lateinit var binding: ActivityMainBinding
    val viewModel by viewModels<MainViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        getComic()
    }

    fun getComic() {
        binding.layoutProgress.visibility = View.VISIBLE
        binding.layoutContent.visibility = View.GONE
        viewModel.getComic(currentComicId)
    }

    private fun initView() {
        viewModel.comicResult.observe(this, {
            binding.layoutProgress.visibility = View.GONE
            it.onSuccess {
                binding.layoutContent.visibility = View.VISIBLE
                currentComic = it
                currentComicId=it.num
                supportActionBar?.subtitle=it.num.toString()
                checkIsFavourite(it)
                updateUI(it)
            }.onFailure {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }
        })
        binding.imageviewShare.setOnClickListener { currentComic?.let { share(it) } }
        binding.imageviewFavourite.setOnClickListener { currentComic?.let { toggleFavourite(it) } }
        binding.imagePrevious.setOnClickListener { currentComicId--; getComic() }
        binding.imageNext.setOnClickListener { currentComicId++; getComic() }
        binding.buttonExplation.setOnClickListener { toast("Not Implemented Yet")}

        binding.buttonRadom.setOnClickListener {
            val intRange = 1..2442
            currentComicId = intRange.random()
            getComic()
        }
        binding.imageComic.setOnClickListener {
            currentComic?.img?.let {
                FullScreenImageActivity.start(this, arrayListOf(it), 0)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_favorites -> ComicListActivity.start(this, false)
            else -> ComicListActivity.start(this, true)
        }
        return true
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

    private fun checkIsFavourite(it: ComicEntity) {
        lifecycleScope.launch {
            isCurrentComicFavourite = App.instance.db.comicsDao().getForId(it.num) != null
            updateFavIcon()
        }

    }

    private fun updateFavIcon() {
        binding.imageviewFavourite.setImageResource(
            if (isCurrentComicFavourite) R.drawable.ic_baseline_favorite_24 else R.drawable.ic_baseline_favorite_border_24
        )
    }

    private fun updateUI(model: ComicEntity) {
        binding.textTitle.text = model.title
        val monthINdex = model.month.toIntOrNull() ?: 2 - 1
        binding.textDate.text = "${model.day} ${monthSet[monthINdex]} ${model.year}"
        binding.textDescription.text = model.alt
        glideLoad(model.img,binding.progressImageLoad,binding.imageComic)


    }


}














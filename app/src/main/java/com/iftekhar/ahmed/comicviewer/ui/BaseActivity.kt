package com.iftekhar.ahmed.comicviewer.ui

import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ShareCompat
import androidx.lifecycle.lifecycleScope
import com.iftekhar.ahmed.comicviewer.App
import com.iftekhar.ahmed.comicviewer.R
import com.michaelfotiads.xkcdreader.data.db.entity.ComicEntity
import kotlinx.coroutines.launch

abstract class BaseActivity:AppCompatActivity() {




    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            //            overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
            return true
        }
        return super.onOptionsItemSelected(item)
    }
    fun share(comicEntity: ComicEntity) {
        val shareText =
            getString(R.string.share_info, comicEntity.title, comicEntity.link)
        ShareCompat.IntentBuilder.from(this)
            .setType("text/plain")
            .setChooserTitle(getString(R.string.share_title))
            .setText(shareText)
            .startChooser()

    }


}

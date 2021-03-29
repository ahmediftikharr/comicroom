package com.iftekhar.ahmed.comicviewer

import android.app.Activity
import android.app.Application
import android.content.ComponentName
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ShareCompat
import com.michaelfotiads.xkcdreader.data.db.ComicsDatabase
import com.michaelfotiads.xkcdreader.data.db.entity.ComicEntity

class App : Application() {

    lateinit var db: ComicsDatabase

    companion object {
        lateinit var instance: App
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        db = ComicsDatabase.getInstance(this)
        /*  val value = MyPrefs.theme
          if (value.isNotEmpty()) {
              val currentTheme = AppTheme.valueOf(value)
              applyTheme(currentTheme)
          }*/


    }

    fun applyTheme(appTheme: AppTheme) {
        if (appTheme == AppTheme.DEFAULT) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        } else if (appTheme == AppTheme.DARK) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}

enum class AppTheme {
    DEFAULT,
    LIGHT,
    DARK

}

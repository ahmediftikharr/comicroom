package com.iftekhar.ahmed.comicviewer.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iftekhar.ahmed.comicviewer.App
import com.iftekhar.ahmed.comicviewer.data.Webservice
import com.michaelfotiads.xkcdreader.data.db.entity.ComicEntity
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {

    private val _comicsData = MutableLiveData<Result<List<ComicEntity>>>()
    val comicsData: LiveData<Result<List<ComicEntity>>>
        get() = _comicsData


    fun searchComics(needSearch: Boolean, query: String) = viewModelScope.launch {

        _comicsData.value = kotlin.runCatching {
            if (needSearch) {
                val number = query.toIntOrNull()
                if (number != null) {
                    listOf(Webservice.comicApi.getForId(number.toString()))
                } else {
                    Webservice.comicApi.getXkcdsSearchResult(query)
                }
            } else {
                App.instance.db.comicsDao().getAllComics()
            }

        }


    }


}
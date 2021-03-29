package com.iftekhar.ahmed.comicviewer.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iftekhar.ahmed.comicviewer.data.Webservice
import com.michaelfotiads.xkcdreader.data.db.entity.ComicEntity
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val _comicResult = MutableLiveData<Result<ComicEntity>>()
    public val comicResult: LiveData<Result<ComicEntity>>
        get() = _comicResult

    fun getComic(id: Int) = viewModelScope.launch { //-1 for latest comic
        _comicResult.value = kotlin.runCatching {
            if (id <= 0) Webservice.comicApi.getLatest() else Webservice.comicApi.getForId(id.toString())
        }

    }
}

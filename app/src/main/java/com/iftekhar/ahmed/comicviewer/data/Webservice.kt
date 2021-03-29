package com.iftekhar.ahmed.comicviewer.data

import com.michaelfotiads.xkcdreader.data.db.entity.ComicEntity
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

object Webservice {
    private val BASE_URL = "https://xkcd.com/"
    var comicApi: ComicApi

    init {
        comicApi =
            Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
                .client(
                    OkHttpClient.Builder().build()
                ).build()
                .create(ComicApi::class.java)
    }
}

interface ComicApi {

    @GET("/{comicId}/info.0.json")
    suspend fun getForId(@Path("comicId") comicId: String): ComicEntity

    @GET("/info.0.json")
    suspend fun getLatest(): ComicEntity

    @GET("https://api.jienan.xyz/xkcd/xkcd-suggest")
    suspend fun getXkcdsSearchResult(@Query("q") query: String): List<ComicEntity>

}
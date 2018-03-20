package com.remybarbosa.rxkotlinexample.service

import com.remybarbosa.rxkotlinexample.service.model.ArticleRemoteModel
import com.remybarbosa.rxkotlinexample.utils.GsonBuilder.getGson
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface ArticleRetrofitDataSource {

    @GET("/repos/vmg/redcarpet/issues/{number}")
    fun getArticle(@Path("number") number: Int): Observable<ArticleRemoteModel>

    object Creator {

        fun newArticleRetrofitDataSource(): ArticleRetrofitDataSource {
            val gson = getGson()
            val retrofit = Retrofit.Builder()
                    .baseUrl("https://api.github.com")
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()
            return retrofit.create(ArticleRetrofitDataSource::class.java)
        }
    }
}
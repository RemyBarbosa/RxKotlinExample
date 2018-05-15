package com.remybarbosa.rxkotlinexample.service

import com.remybarbosa.rxkotlinexample.service.model.autocomplete.AutocompleteRemoteModel
import com.remybarbosa.rxkotlinexample.utils.GsonBuilder.getGson
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface AutocompleteRetrofitDataSource {

    @GET("/maps/api/place/autocomplete/json")
    fun search(@Query("input") input: String,
               @Query("types") types: String,
               @Query("components") components: String,
               @Query("language") language: String,
               @Query("key") key: String): Observable<AutocompleteRemoteModel>

    object Creator {

        fun newAutocompleteRetrofitDataSource(): AutocompleteRetrofitDataSource {
            val gson = getGson()
            val retrofit = Retrofit.Builder()
                    .baseUrl("https://maps.googleapis.com")
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()
            return retrofit.create(AutocompleteRetrofitDataSource::class.java)
        }
    }
}
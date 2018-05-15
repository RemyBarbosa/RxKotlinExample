package com.remybarbosa.rxkotlinexample.activity_part_autocomplete

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.ArrayAdapter
import com.remybarbosa.rxkotlinexample.BuildConfig
import com.remybarbosa.rxkotlinexample.R
import com.remybarbosa.rxkotlinexample.part3.toObservable
import com.remybarbosa.rxkotlinexample.service.AutocompleteRetrofitDataSource
import com.remybarbosa.rxkotlinexample.service.model.autocomplete.AutocompleteMapper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_part_autocomplete.*
import java.util.concurrent.TimeUnit


class PartAutoCompleteBlank : AppCompatActivity() {

    private lateinit var adapter: ArrayAdapter<String>
    private val disposables: CompositeDisposable = CompositeDisposable()
    private val autocompleteRetrofitDataSource: AutocompleteRetrofitDataSource = AutocompleteRetrofitDataSource.Creator.newAutocompleteRetrofitDataSource()

    companion object {
        private fun intent(context: Context): Intent {
            return Intent(context, PartAutoCompleteBlank::class.java)
        }

        fun start(context: Context) {
            context.startActivity(intent(context))
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_part_autocomplete)

        /* Steps :

         * See what's happening
         *** 'subscribe' to textField
         *** 'disposeBag'
         *** 'debug'

         * Filter input text
         *** 'filter' count < 2

         * Bind to API
         *** 'flatMap' / 'flatMapLatest' to call API
         *** 'map' done by retrofit

         * Bind to UI methods
         *** 'map' to [String]
         *** 'subscribeOn'
         *** 'observeOn' main thread for UI
         *** 'subscribeBy' onNext/onError with UI methods

         * Optimizations : fire less request + background thread
         *** 'debounce' to wait input
         *** 'distinctUntilChanged' to avoid firing same request
         *** 'flatMapLatest' to cancel previous request(s) if any

         * Handle short input text
         *** remove 'filter'
         *** throw custom error to display a different message, some ways to do it :
         ***** add 'do' to count and throw error
         ***** count and throw error inside 'flatMap'
         ***** count and throw error inside 'flatMap'
         */

        disposables.add(
                editText.toObservable()
                        .filter { text ->
                            text.count() > 2
                        }
                        .distinctUntilChanged()
                        .debounce(500, TimeUnit.MILLISECONDS)
                        .flatMap { text ->
                            autocompleteRetrofitDataSource.search(text, "(cities)", "country:fr", "fr_FR", BuildConfig.GOOGLE_KEY)
                        }
                        .map { autocompleteRemoteModel ->
                            AutocompleteMapper().remoteEntityToViewModel(autocompleteRemoteModel)
                        }
                        .map { autocompleteViewModel ->
                            autocompleteViewModel.predictions
                        }
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeBy(
                                onNext = { cities ->
                                    textView.text = cities.joinToString("\n")
                                },
                                onError = { error ->
                                    Log.e("TAG", "error : " + error.message)
                                }
                        )
        )
    }

    override fun onDestroy() {
        disposables.clear()
        super.onDestroy()
    }
}

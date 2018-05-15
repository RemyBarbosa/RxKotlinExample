package com.remybarbosa.rxkotlinexample.activity_part_autocomplete

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import com.remybarbosa.rxkotlinexample.R
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import com.remybarbosa.rxkotlinexample.part3.toObservable
import com.remybarbosa.rxkotlinexample.service.AutocompleteRetrofitDataSource
import com.remybarbosa.rxkotlinexample.service.model.autocomplete.AutocompleteMapper
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_part_autocomplete.*
import java.util.concurrent.TimeUnit


class PartAutoComplete : AppCompatActivity() {

    private lateinit var adapter: ArrayAdapter<String>
    private val disposables: CompositeDisposable = CompositeDisposable()
    private val autocompleteRetrofitDataSource: AutocompleteRetrofitDataSource = AutocompleteRetrofitDataSource.Creator.newAutocompleteRetrofitDataSource()

    companion object {
        private fun intent(context: Context): Intent {
            return Intent(context, PartAutoComplete::class.java)
        }

        fun start(context: Context) {
            context.startActivity(intent(context))
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_part_autocomplete)


        disposables.add(
                editText.toObservable()
                        .filter { text ->
                            text.count() > 2
                        }
                        .distinctUntilChanged()
                        .debounce(500, TimeUnit.MILLISECONDS)
                        .flatMap { text ->
                            autocompleteRetrofitDataSource.search(text, "(cities)", "country:fr", "fr_FR", "AIzaSyAT0cLTt1IYLxpdAAPyicOEgrmjnZ2bpT0")
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
                                onNext = {
                                    adapter.clear()
                                    adapter.addAll(it)
                                    adapter.notifyDataSetChanged()
                                },
                                onError = {
                                    error -> Log.e("TAG", "error : " + error.message)
                                }
                        )
        )
    }

    override fun onDestroy() {
        disposables.clear()
        super.onDestroy()
    }
}

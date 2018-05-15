package com.remybarbosa.rxkotlinexample.part4and5

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.remybarbosa.rxkotlinexample.R
import com.remybarbosa.rxkotlinexample.service.ArticleRetrofitDataSource
import io.reactivex.disposables.CompositeDisposable

/////////////
/// Part 4. : Make async request with Rx & retrofit
/// Part 4.1. : chain operators on simple request
/// Part 5. : Change execution Thread

class Part4And5Blank : AppCompatActivity() {

    private val disposables: CompositeDisposable = CompositeDisposable()
    // PART 4
    private val articleDataSource: ArticleRetrofitDataSource = ArticleRetrofitDataSource.Creator.newArticleRetrofitDataSource()

    companion object {
        private fun intent(context: Context): Intent {
            return Intent(context, Part4And5Blank::class.java)
        }

        fun start(context: Context) {
            context.startActivity(intent(context))
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_part_4_and_5)

    }


    override fun onDestroy() {
        disposables.clear()
        super.onDestroy()
    }
}

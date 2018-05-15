package com.remybarbosa.rxkotlinexample.part6

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.remybarbosa.rxkotlinexample.R
import com.remybarbosa.rxkotlinexample.service.ArticleRetrofitDataSource
import com.remybarbosa.rxkotlinexample.service.model.article.ArticleRemoteModel
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit

/////////////
/// Part 6. : handle error
/// Part 6.1 : retry

class Part6Blank : AppCompatActivity() {

    private val disposables: CompositeDisposable = CompositeDisposable()
    private val articleDataSource: ArticleRetrofitDataSource = ArticleRetrofitDataSource.Creator.newArticleRetrofitDataSource()

    companion object {
        private fun intent(context: Context): Intent {
            return Intent(context, Part6Blank::class.java)
        }

        fun start(context: Context) {
            context.startActivity(intent(context))
        }
    }


    private val TIMEOUT: Duration = Duration(60, TimeUnit.SECONDS)
    private val DELAY_BEFORE_RETRY: Duration = Duration(1, TimeUnit.SECONDS)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_part_6)

        val observableSuccess = articleDataSource.getArticle(456)

    }

    // Create Observable who throw Exception until 3 third try, do not look it too much i'm not proud of it :')
    fun toObservableAndError(observableSuccess: Observable<ArticleRemoteModel>): Observable<ArticleRemoteModel> {
        var count = 0
        return Observable.create({
            if (count < 2) {
                count = count.inc()
                it.onError(throw Exception("OUPS :/"))
            } else {
                count = count.inc()
                it.onNext(observableSuccess.blockingFirst())
                it.onComplete()
            }
        })
    }

    override fun onDestroy() {
        disposables.clear()
        super.onDestroy()
    }

}
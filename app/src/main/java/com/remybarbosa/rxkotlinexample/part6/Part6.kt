package com.remybarbosa.rxkotlinexample.part6

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.remybarbosa.rxkotlinexample.R
import com.remybarbosa.rxkotlinexample.service.ArticleRetrofitDataSource
import com.remybarbosa.rxkotlinexample.service.model.ArticleMapper
import com.remybarbosa.rxkotlinexample.service.model.ArticleRemoteModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.Observables
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_part_6.*
import java.util.concurrent.TimeUnit

/////////////
/// Part 6. : handle error
/// Part 6.1 : retry

class Part6 : AppCompatActivity() {

    private val disposables: CompositeDisposable = CompositeDisposable()
    private val articleDataSource: ArticleRetrofitDataSource = ArticleRetrofitDataSource.Creator.newArticleRetrofitDataSource()

    companion object {
        private fun intent(context: Context): Intent {
            return Intent(context, Part6::class.java)
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

        disposables.add(toObservableAndError(observableSuccess)
                // PART 6
                .doOnError {
                    e -> textView.text = getString(R.string.error_message).plus("\n\n" + e.message)
                }
                // PART 6.1
                .retryWhen {
                    Observables.zip(
                            it.map { it as? Exception ?: throw it },
                            Observable.interval(DELAY_BEFORE_RETRY.duration, DELAY_BEFORE_RETRY.timeUnit))
                            .map { if (it.second >= TIMEOUT.duration) throw it.first }
                }
                // Remember PART 4
                .filter { articleRemoteModel -> articleRemoteModel.createdAt.isBeforeNow }
                .map { articleRemoteModel -> ArticleMapper().remoteEntityToViewModel(articleRemoteModel) }
                .map { articleViewModel ->
                    articleViewModel.url + "\n\n" +
                            articleViewModel.title + "\n" +
                            articleViewModel.createdAt + "\n"
                }
                // Remember PART 5
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { formattedString -> textView.text = formattedString }
                ))
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
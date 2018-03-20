package com.remybarbosa.rxkotlinexample.part4and5

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.remybarbosa.rxkotlinexample.R
import com.remybarbosa.rxkotlinexample.service.ArticleRetrofitDataSource
import com.remybarbosa.rxkotlinexample.service.model.ArticleMapper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_part_4_and_5.*

/////////////
/// Part 4. : Make async request with Rx & retrofit
/// Part 4.1. : chain operators on simple request
/// Part 5. : Change execution Thread

class Part4And5 : AppCompatActivity() {

    private val disposables: CompositeDisposable = CompositeDisposable()
    // PART 4
    private val articleDataSource: ArticleRetrofitDataSource = ArticleRetrofitDataSource.Creator.newArticleRetrofitDataSource()

    companion object {
        private fun intent(context: Context): Intent {
            return Intent(context, Part4And5::class.java)
        }

        fun start(context: Context) {
            context.startActivity(intent(context))
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_part_4_and_5)

        disposables.add(articleDataSource.getArticle(22)
                // PART 4.1
                .filter { articleRemoteModel -> articleRemoteModel.createdAt.isBeforeNow }
                .map { articleRemoteModel -> ArticleMapper().remoteEntityToViewModel(articleRemoteModel) }
                .map { articleViewModel ->
                    articleViewModel.url + "\n\n" +
                            articleViewModel.title + "\n" +
                            articleViewModel.createdAt + "\n"
                }
                // PART 5
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { formattedString -> textView.text = formattedString }
                ))
    }


    override fun onDestroy() {
        disposables.clear()
        super.onDestroy()
    }
}

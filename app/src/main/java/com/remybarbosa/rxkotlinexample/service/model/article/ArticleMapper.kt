package com.remybarbosa.rxkotlinexample.service.model.article

class ArticleMapper {
    fun remoteEntityToViewModel(articleRemote: ArticleRemoteModel): ArticleViewModel {
        return ArticleViewModel(articleRemote.title, articleRemote.url, articleRemote.createdAt.toString("yyyy-MM-dd"))
    }
}

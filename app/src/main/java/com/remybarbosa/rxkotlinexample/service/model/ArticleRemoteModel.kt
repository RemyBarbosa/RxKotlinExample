package com.remybarbosa.rxkotlinexample.service.model

import com.google.gson.annotations.SerializedName
import org.joda.time.DateTime

data class ArticleRemoteModel(val title: String, val url: String, @SerializedName("created_at") val createdAt: DateTime)

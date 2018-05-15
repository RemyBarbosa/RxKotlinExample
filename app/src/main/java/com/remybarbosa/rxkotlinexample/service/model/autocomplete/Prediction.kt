package com.remybarbosa.rxkotlinexample.service.model.autocomplete

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Prediction(val description: String? = null, val placeId:String? = null)
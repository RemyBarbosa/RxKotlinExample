package com.remybarbosa.rxkotlinexample.service.model.autocomplete

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AutocompleteViewModel(val predictions: List<String>)

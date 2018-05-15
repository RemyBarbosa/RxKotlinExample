package com.remybarbosa.rxkotlinexample.service.model.autocomplete

class AutocompleteMapper {
    fun remoteEntityToViewModel(autocompleteRemoteModel: AutocompleteRemoteModel): AutocompleteViewModel {
        autocompleteRemoteModel.predictions
                ?.filter { it.description != null }
                ?.map { it.description!! }
                ?.let {
                    return AutocompleteViewModel(it)
                }
        return AutocompleteViewModel(emptyList())
    }
}

package com.mmfsin.musicmaster.presentation.categories.language

import com.mmfsin.musicmaster.base.BaseViewModel
import com.mmfsin.musicmaster.domain.usecases.GetCategoriesByLanguageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CategoriesByLanguageViewModel @Inject constructor(
    private val getCategoriesByLanguageUseCase: GetCategoriesByLanguageUseCase
) : BaseViewModel<CategoriesByLanguageEvent>() {

    fun getCategoriesByLanguage(language: String) {
        executeUseCase(
            { getCategoriesByLanguageUseCase.execute(GetCategoriesByLanguageUseCase.Params(language)) },
            { result ->
                _event.value = if (result.isEmpty()) CategoriesByLanguageEvent.SomethingWentWrong
                else CategoriesByLanguageEvent.Categories(result)
            },
            { _event.value = CategoriesByLanguageEvent.SomethingWentWrong }
        )
    }
}
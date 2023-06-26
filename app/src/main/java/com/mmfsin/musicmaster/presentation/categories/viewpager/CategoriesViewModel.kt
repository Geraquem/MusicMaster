package com.mmfsin.musicmaster.presentation.categories.viewpager

import com.mmfsin.musicmaster.base.BaseViewModel
import com.mmfsin.musicmaster.domain.usecases.GetCategoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase
) : BaseViewModel<CategoriesEvent>() {

    fun getCategories() {
        executeUseCase(
            { getCategoriesUseCase.execute() },
            { result ->
                _event.value = if (result.isEmpty()) CategoriesEvent.SomethingWentWrong
                else CategoriesEvent.Categories(result)
            },
            { _event.value = CategoriesEvent.SomethingWentWrong }
        )
    }
}
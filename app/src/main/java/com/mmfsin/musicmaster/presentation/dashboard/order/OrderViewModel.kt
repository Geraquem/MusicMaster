package com.mmfsin.musicmaster.presentation.dashboard.order

import com.mmfsin.musicmaster.base.BaseViewModel
import com.mmfsin.musicmaster.domain.models.Order
import com.mmfsin.musicmaster.domain.usecases.CheckOrderSolutionUseCase
import com.mmfsin.musicmaster.domain.usecases.GetCategoryByIdUseCase
import com.mmfsin.musicmaster.domain.usecases.GetMusicDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val getCategoryByIdUseCase: GetCategoryByIdUseCase,
    private val getMusicDataUseCase: GetMusicDataUseCase,
    private val checkOrderSolutionUseCase: CheckOrderSolutionUseCase
) : BaseViewModel<OrderEvent>() {

    fun getCategory(categoryId: String) {
        executeUseCase(
            { getCategoryByIdUseCase.execute(GetCategoryByIdUseCase.Params(categoryId)) },
            { result ->
                _event.value = result?.let { OrderEvent.CategoryData(it) }
                    ?: run { OrderEvent.SomethingWentWrong }
            },
            { _event.value = OrderEvent.SomethingWentWrong }
        )
    }

    fun getMusicData(categoryId: String) {
        executeUseCase(
            { getMusicDataUseCase.execute(GetMusicDataUseCase.Params(categoryId)) },
            { result ->
                _event.value = if (result.isEmpty()) OrderEvent.SomethingWentWrong
                else OrderEvent.MusicData(result)
            },
            { _event.value = OrderEvent.SomethingWentWrong }
        )
    }

    fun response(yearToGuess: Long, actualYear: Long, older: Order) {
        executeUseCase(
            {
                checkOrderSolutionUseCase.execute(
                    CheckOrderSolutionUseCase.Params(yearToGuess, actualYear, older)
                )
            },
            { result -> _event.value = OrderEvent.Solution(result) },
            { _event.value = OrderEvent.SomethingWentWrong }
        )
    }
}
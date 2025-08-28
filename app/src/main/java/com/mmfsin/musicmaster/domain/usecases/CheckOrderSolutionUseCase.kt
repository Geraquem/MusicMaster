package com.mmfsin.musicmaster.domain.usecases

import com.mmfsin.musicmaster.base.BaseUseCase
import com.mmfsin.musicmaster.domain.models.Order
import com.mmfsin.musicmaster.domain.models.Order.NEWER
import com.mmfsin.musicmaster.domain.models.Order.OLDER
import com.mmfsin.musicmaster.domain.models.OrderResponse
import javax.inject.Inject

class CheckOrderSolutionUseCase @Inject constructor() :
    BaseUseCase<CheckOrderSolutionUseCase.Params, OrderResponse>() {

    override suspend fun execute(params: Params): OrderResponse {
        if (params.yearToGuess == params.actualYear) return OrderResponse(sameYear = true)
        return when (params.order) {
            NEWER -> OrderResponse(isCorrect = params.actualYear > params.yearToGuess)
            OLDER -> OrderResponse(isCorrect = params.actualYear < params.yearToGuess)
        }
    }

    data class Params(
        val yearToGuess: Long,
        val actualYear: Long,
        val order: Order
    )
}
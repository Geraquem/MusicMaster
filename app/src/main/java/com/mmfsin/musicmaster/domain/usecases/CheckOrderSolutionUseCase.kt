package com.mmfsin.musicmaster.domain.usecases

import com.mmfsin.musicmaster.base.BaseUseCase
import com.mmfsin.musicmaster.domain.models.OrderSelected
import com.mmfsin.musicmaster.domain.models.OrderSelected.NEWER
import com.mmfsin.musicmaster.domain.models.OrderSelected.OLDER
import com.mmfsin.musicmaster.domain.models.OrderSelected.SAME_YEAR
import com.mmfsin.musicmaster.domain.models.OrderSolution
import com.mmfsin.musicmaster.domain.models.OrderSolution.BAD
import com.mmfsin.musicmaster.domain.models.OrderSolution.GOOD
import javax.inject.Inject

class CheckOrderSolutionUseCase @Inject constructor() :
    BaseUseCase<CheckOrderSolutionUseCase.Params, Pair<OrderSelected, OrderSolution>>() {

    override suspend fun execute(params: Params): Pair<OrderSelected, OrderSolution> {
        val result = when (params.selected) {
            OLDER -> {
                if (params.actualYear < params.yearToGuess) GOOD
                else if (params.yearToGuess == params.actualYear) OrderSolution.SAME_YEAR
                else BAD
            }

            NEWER -> {
                if (params.actualYear > params.yearToGuess) GOOD
                else if (params.yearToGuess == params.actualYear) OrderSolution.SAME_YEAR
                else BAD
            }

            SAME_YEAR -> if (params.yearToGuess == params.actualYear) GOOD else BAD
        }

        return Pair(params.selected, result)
    }

    data class Params(
        val selected: OrderSelected,
        val yearToGuess: Long,
        val actualYear: Long
    )
}
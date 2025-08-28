package com.mmfsin.musicmaster.domain.usecases

import com.mmfsin.musicmaster.base.BaseUseCase
import com.mmfsin.musicmaster.domain.models.Order
import javax.inject.Inject

class CheckOrderSolutionUseCase @Inject constructor() :
    BaseUseCase<CheckOrderSolutionUseCase.Params, Boolean>() {

    override suspend fun execute(params: Params): Boolean {
        return true
    }

    data class Params(
        val yearToGuess: Long,
        val actualYear: Long,
        val order: Order
    )
}
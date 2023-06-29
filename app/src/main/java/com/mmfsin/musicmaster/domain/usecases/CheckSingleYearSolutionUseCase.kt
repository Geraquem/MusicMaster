package com.mmfsin.musicmaster.domain.usecases

import com.mmfsin.musicmaster.base.BaseUseCase
import com.mmfsin.musicmaster.domain.interfaces.IMusicRepository
import com.mmfsin.musicmaster.presentation.models.SolutionType
import com.mmfsin.musicmaster.presentation.models.SolutionType.*
import javax.inject.Inject
import kotlin.math.absoluteValue

class CheckSingleYearSolutionUseCase @Inject constructor(private val repository: IMusicRepository) :
    BaseUseCase<CheckSingleYearSolutionUseCase.Params, SolutionType?>() {

    override suspend fun execute(params: Params): SolutionType? {
        return try {
            val solution = params.solution.toInt()
            val answer = params.answer.toInt()
            when ((solution - answer).absoluteValue) {
                0 -> GOOD
                1, 2, 3 -> ALMOST_GOOD
                else -> BAD
            }

        } catch (e: Exception) {
            null
        }
    }

    data class Params(
        val solution: Long,
        val answer: String,
    )
}
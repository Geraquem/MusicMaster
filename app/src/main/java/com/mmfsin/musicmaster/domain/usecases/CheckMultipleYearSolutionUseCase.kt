package com.mmfsin.musicmaster.domain.usecases

import com.mmfsin.musicmaster.base.BaseUseCase
import com.mmfsin.musicmaster.domain.interfaces.IMusicRepository
import com.mmfsin.musicmaster.presentation.models.SolutionType
import com.mmfsin.musicmaster.presentation.models.SolutionType.*
import javax.inject.Inject
import kotlin.math.absoluteValue

class CheckMultipleYearSolutionUseCase @Inject constructor(private val repository: IMusicRepository) :
    BaseUseCase<CheckMultipleYearSolutionUseCase.Params, Pair<SolutionType, SolutionType>?>() {

    override suspend fun execute(params: Params): Pair<SolutionType, SolutionType>? {
        return try {
            val solution = params.solution.toInt()
            val answerTeamOne = params.answerTeamOne.toInt()
            val answerTeamTwo = params.answerTeamTwo.toInt()
            val first = getSolutionType(solution, answerTeamOne)
            val second = getSolutionType(solution, answerTeamTwo)
            Pair(first, second)
        } catch (e: Exception) {
            null
        }
    }

    private fun getSolutionType(solution: Int, answer: Int): SolutionType {
        return when ((solution - answer).absoluteValue) {
            0 -> GOOD
            1, 2, 3 -> ALMOST_GOOD
            else -> BAD
        }
    }

    data class Params(
        val solution: Long,
        val answerTeamOne: String,
        val answerTeamTwo: String,
    )
}
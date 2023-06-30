package com.mmfsin.musicmaster.domain.usecases

import com.mmfsin.musicmaster.base.BaseUseCase
import com.mmfsin.musicmaster.presentation.models.SolutionType
import com.mmfsin.musicmaster.presentation.models.SolutionType.*
import org.apache.commons.text.similarity.LevenshteinDistance
import javax.inject.Inject

class CheckTitleSolutionUseCase @Inject constructor() :
    BaseUseCase<CheckTitleSolutionUseCase.Params, SolutionType?>() {

    override suspend fun execute(params: Params): SolutionType? {
        return try {
            val solution = params.solution.setUpString()
            val answer = params.answer.setUpString()

            when (LevenshteinDistance().apply(solution, answer)) {
                0, 1 -> GOOD
                2, 3, 4 -> ALMOST_GOOD
                else -> BAD
            }
        } catch (e: Exception) {
            null
        }
    }

    private fun String.setUpString(): String {
        return this.trim().lowercase()
            .replace(" ", "")
            .replace("'", "")
            .replace("/", "")
            .replace("(", "")
            .replace(")", "")
            .replace("-", "")
            .replace("á", "a")
            .replace("é", "e")
            .replace("í", "i")
            .replace("ó", "o")
            .replace("ú", "u")
    }

    data class Params(
        val solution: String,
        val answer: String,
    )
}
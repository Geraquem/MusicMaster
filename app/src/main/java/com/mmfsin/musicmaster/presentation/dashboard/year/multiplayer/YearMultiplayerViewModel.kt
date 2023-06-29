package com.mmfsin.musicmaster.presentation.dashboard.year.multiplayer

import com.mmfsin.musicmaster.base.BaseViewModel
import com.mmfsin.musicmaster.domain.usecases.CheckMultipleYearSolutionUseCase
import com.mmfsin.musicmaster.domain.usecases.GetCategoryByIdUseCase
import com.mmfsin.musicmaster.domain.usecases.GetMusicDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class YearMultiplayerViewModel @Inject constructor(
    private val getCategoryByIdUseCase: GetCategoryByIdUseCase,
    private val getMusicDataUseCase: GetMusicDataUseCase,
    private val checkMultipleYearSolutionUseCase: CheckMultipleYearSolutionUseCase
) : BaseViewModel<YearMultiplayerEvent>() {

    fun getCategory(categoryId: String) {
        executeUseCase(
            { getCategoryByIdUseCase.execute(GetCategoryByIdUseCase.Params(categoryId)) },
            { result ->
                _event.value = result?.let { YearMultiplayerEvent.CategoryData(it) }
                    ?: run { YearMultiplayerEvent.SomethingWentWrong }
            },
            { _event.value = YearMultiplayerEvent.SomethingWentWrong }
        )
    }

    fun getMusicData(categoryId: String) {
        executeUseCase(
            { getMusicDataUseCase.execute(GetMusicDataUseCase.Params(categoryId)) },
            { result ->
                _event.value = if (result.isEmpty()) YearMultiplayerEvent.SomethingWentWrong
                else YearMultiplayerEvent.MusicData(result)
            },
            { _event.value = YearMultiplayerEvent.SomethingWentWrong }
        )
    }

    fun checkSolution(solution: Long, answerTeamOne: String, answerTeamTwo: String) {
        executeUseCase(
            {
                checkMultipleYearSolutionUseCase.execute(
                    CheckMultipleYearSolutionUseCase.Params(solution, answerTeamOne, answerTeamTwo)
                )
            },
            { result ->
                _event.value = result?.let { YearMultiplayerEvent.Solution(it) }
                    ?: run { YearMultiplayerEvent.SomethingWentWrong }
            },
            { _event.value = YearMultiplayerEvent.SomethingWentWrong }
        )
    }
}
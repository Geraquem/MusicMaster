package com.mmfsin.musicmaster.presentation.dashboard.year.single

import com.mmfsin.musicmaster.base.BaseViewModel
import com.mmfsin.musicmaster.domain.usecases.CheckSingleYearSolutionUseCase
import com.mmfsin.musicmaster.domain.usecases.GetCategoryByIdUseCase
import com.mmfsin.musicmaster.domain.usecases.GetMusicDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class YearSingleViewModel @Inject constructor(
    private val getCategoryByIdUseCase: GetCategoryByIdUseCase,
    private val getMusicDataUseCase: GetMusicDataUseCase,
    private val checkSingleYearSolutionUseCase: CheckSingleYearSolutionUseCase
) : BaseViewModel<YearSingleEvent>() {

    fun getCategory(categoryId: String){
        executeUseCase(
            { getCategoryByIdUseCase.execute(GetCategoryByIdUseCase.Params(categoryId)) },
            { result ->
                _event.value = result?.let { YearSingleEvent.CategoryData(it) }
                    ?: run { YearSingleEvent.SomethingWentWrong }
            },
            { _event.value = YearSingleEvent.SomethingWentWrong }
        )
    }

    fun getMusicData(categoryId: String) {
        executeUseCase(
            { getMusicDataUseCase.execute(GetMusicDataUseCase.Params(categoryId)) },
            { result ->
                _event.value = if (result.isEmpty()) YearSingleEvent.SomethingWentWrong
                else YearSingleEvent.MusicData(result)
            },
            { _event.value = YearSingleEvent.SomethingWentWrong }
        )
    }

    fun checkSolution(solution: Long, answer: String) {
        executeUseCase(
            {
                checkSingleYearSolutionUseCase.execute(
                    CheckSingleYearSolutionUseCase.Params(solution, answer)
                )
            },
            { result ->
                _event.value = result?.let { YearSingleEvent.Solution(it) }
                    ?: run { YearSingleEvent.SomethingWentWrong }
            },
            { _event.value = YearSingleEvent.SomethingWentWrong }
        )
    }
}
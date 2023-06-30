package com.mmfsin.musicmaster.presentation.dashboard.title

import com.mmfsin.musicmaster.base.BaseViewModel
import com.mmfsin.musicmaster.domain.usecases.CheckTitleSolutionUseCase
import com.mmfsin.musicmaster.domain.usecases.GetCategoryByIdUseCase
import com.mmfsin.musicmaster.domain.usecases.GetMusicDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TitleViewModel @Inject constructor(
    private val getCategoryByIdUseCase: GetCategoryByIdUseCase,
    private val getMusicDataUseCase: GetMusicDataUseCase,
    private val checkTitleSolutionUseCase: CheckTitleSolutionUseCase
) : BaseViewModel<TitleEvent>() {

    fun getCategory(categoryId: String) {
        executeUseCase(
            { getCategoryByIdUseCase.execute(GetCategoryByIdUseCase.Params(categoryId)) },
            { result ->
                _event.value = result?.let { TitleEvent.CategoryData(it) }
                    ?: run { TitleEvent.SomethingWentWrong }
            },
            { _event.value = TitleEvent.SomethingWentWrong }
        )
    }

    fun getMusicData(categoryId: String) {
        executeUseCase(
            { getMusicDataUseCase.execute(GetMusicDataUseCase.Params(categoryId)) },
            { result ->
                _event.value = if (result.isEmpty()) TitleEvent.SomethingWentWrong
                else TitleEvent.MusicData(result)
            },
            { _event.value = TitleEvent.SomethingWentWrong }
        )
    }

    fun checkSolution(solution: String, answer: String) {
        executeUseCase(
            {
                checkTitleSolutionUseCase.execute(
                    CheckTitleSolutionUseCase.Params(solution, answer)
                )
            },
            { result ->
                _event.value = result?.let { TitleEvent.Solution(it) }
                    ?: run { TitleEvent.SomethingWentWrong }
            },
            { _event.value = TitleEvent.SomethingWentWrong }
        )
    }
}
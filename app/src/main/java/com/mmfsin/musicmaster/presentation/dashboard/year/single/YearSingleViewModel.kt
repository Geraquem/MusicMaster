package com.mmfsin.musicmaster.presentation.dashboard.year.single

import com.mmfsin.musicmaster.base.BaseViewModel
import com.mmfsin.musicmaster.domain.usecases.GetMusicDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class YearSingleViewModel @Inject constructor(
    private val getMusicDataUseCase: GetMusicDataUseCase
) : BaseViewModel<YearSingleEvent>() {

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
}
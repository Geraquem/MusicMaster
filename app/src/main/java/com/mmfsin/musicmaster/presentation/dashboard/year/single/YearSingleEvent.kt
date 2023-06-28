package com.mmfsin.musicmaster.presentation.dashboard.year.single

import com.mmfsin.musicmaster.domain.models.Music
import com.mmfsin.musicmaster.presentation.models.SolutionType

sealed class YearSingleEvent {
    class MusicData(val data: List<Music>) : YearSingleEvent()
    class Solution(val result: SolutionType) : YearSingleEvent()
    object SomethingWentWrong : YearSingleEvent()
}
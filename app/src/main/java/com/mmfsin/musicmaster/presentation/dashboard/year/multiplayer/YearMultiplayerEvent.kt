package com.mmfsin.musicmaster.presentation.dashboard.year.multiplayer

import com.mmfsin.musicmaster.domain.models.Category
import com.mmfsin.musicmaster.domain.models.Music
import com.mmfsin.musicmaster.presentation.models.SolutionType

sealed class YearMultiplayerEvent {
    class CategoryData(val category: Category) : YearMultiplayerEvent()
    class MusicData(val data: List<Music>) : YearMultiplayerEvent()
    class Solution(val result: SolutionType) : YearMultiplayerEvent()
    object SomethingWentWrong : YearMultiplayerEvent()
}
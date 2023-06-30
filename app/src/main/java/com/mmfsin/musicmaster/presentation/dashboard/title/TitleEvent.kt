package com.mmfsin.musicmaster.presentation.dashboard.title

import com.mmfsin.musicmaster.domain.models.Category
import com.mmfsin.musicmaster.domain.models.Music
import com.mmfsin.musicmaster.presentation.models.SolutionType

sealed class TitleEvent {
    class CategoryData(val category: Category) : TitleEvent()
    class MusicData(val data: List<Music>) : TitleEvent()
    class Solution(val result: SolutionType) : TitleEvent()
    object SomethingWentWrong : TitleEvent()
}
package com.mmfsin.musicmaster.presentation.dashboard.year.single

import com.mmfsin.musicmaster.domain.models.Music

sealed class YearSingleEvent {
    class MusicData(val result: List<Music>) : YearSingleEvent()
    object SomethingWentWrong : YearSingleEvent()
}
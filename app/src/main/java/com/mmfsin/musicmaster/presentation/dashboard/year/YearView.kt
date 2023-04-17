package com.mmfsin.musicmaster.presentation.dashboard.year

import com.mmfsin.musicmaster.domain.models.MusicDTO

interface YearView {
    fun musicData(list: List<MusicDTO>)
}
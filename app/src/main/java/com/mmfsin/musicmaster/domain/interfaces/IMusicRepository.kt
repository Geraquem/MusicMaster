package com.mmfsin.musicmaster.domain.interfaces

import com.mmfsin.musicmaster.domain.models.MusicDTO

interface IMusicRepository {
    fun musicData(list: List<MusicDTO>)
    fun somethingWentWrong()
}
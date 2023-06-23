package com.mmfsin.musicmaster.presentation.dashboard.year

import com.mmfsin.musicmaster.domain.models.MusicDTO
import com.mmfsin.musicmaster.domain.types.ResultType

interface YearView {
    fun musicData(list: List<MusicDTO>)

    fun solution(type: ResultType)
    fun multiSolution(solutions: Pair<ResultType, ResultType>)

    fun somethingWentWrong()
}
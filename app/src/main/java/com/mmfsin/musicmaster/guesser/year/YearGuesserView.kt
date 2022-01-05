package com.mmfsin.musicmaster.guesser.year

import com.mmfsin.musicmaster.guesser.model.MusicVideoDTO

interface YearGuesserView {
    fun setMusicVideoData(musicVideo: MusicVideoDTO)
    fun setMusicVideoList(list: List<String>)

    fun setSolutionMessage(solutionResult: Int)

    fun somethingWentWrong()
}
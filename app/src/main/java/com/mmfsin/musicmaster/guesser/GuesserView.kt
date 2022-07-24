package com.mmfsin.musicmaster.guesser

import com.mmfsin.musicmaster.guesser.model.MusicVideoDTO

interface GuesserView {
    fun setMusicVideoData(musicVideo: MusicVideoDTO)
    fun setMusicVideoList(list: List<String>)

    fun setSolutionMessage(solutionResult: Int)
    fun setMultiSolutionMessage(gOnePoints: Int, gTwoPoints: Int)

    fun somethingWentWrong()
}
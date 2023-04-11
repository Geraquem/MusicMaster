package com.mmfsin.musicmaster.presentation.guesser.common

import com.mmfsin.musicmaster.presentation.guesser.GuesserView
import com.mmfsin.musicmaster.presentation.guesser.model.MusicVideoDTO
import com.mmfsin.musicmaster.presentation.guesser.repository.FirebaseRepo

class CommonPresenter(private val view: GuesserView) :
    FirebaseRepo.IRepo {

    private val repository by lazy { FirebaseRepo(this) }

    fun getMusicVideoList(category: String) {
        repository.getMusicVideoList(category)
    }

    fun getMusicVideoData(category: String, video: String) {
        repository.getMusicVideo(category, video)
    }

    /////////

    override fun musicVideoList(list: List<String>) {
        view.setMusicVideoList(list)
    }

    override fun musicVideoData(musicVideo: MusicVideoDTO) {
        view.setMusicVideoData(musicVideo)
    }

    override fun somethingWentWrong() {
        view.somethingWentWrong()
    }
}
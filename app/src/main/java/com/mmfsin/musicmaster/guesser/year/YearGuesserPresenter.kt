package com.mmfsin.musicmaster.guesser.year

import com.mmfsin.musicmaster.guesser.model.MusicVideoDTO
import com.mmfsin.musicmaster.guesser.repository.FirebaseRepo

class YearGuesserPresenter(private val view: YearGuesserView) : FirebaseRepo.IRepo {

    private val repository by lazy { FirebaseRepo(this) }

    fun getMusicVideoList(category: String) {
        repository.getMusicVideoList(category)
    }

    fun getMusicVideo(category: String, video: String) {
        repository.getMusicVideo(category, video)
    }

    fun isValidYear(pinViewText: String): Boolean {
        return (pinViewText.length == 4)
    }

    fun setSolutionMessage(userYearStr: String, correctYearStr: String) {
        val userYear = userYearStr.toIntOrNull()
        val correctYear = correctYearStr.toInt()
        if (userYear != null) {
            if (userYear == correctYear) {
                view.setSolutionMessage(0)
            } else if (userYear > (correctYear - 3) && userYear < (correctYear + 3) && userYear != correctYear) {
                view.setSolutionMessage(1)
            } else {
                view.setSolutionMessage(2)
            }
        }else{
            view.somethingWentWrong()
        }
    }

    override fun musicVideoList(size: Long) {
        val list = mutableListOf<String>()
        for (i in 1..size) {
            list.add("video$i")
        }
        list.shuffle()
        view.setMusicVideoList(list)
    }

    override fun musicVideo(musicVideo: MusicVideoDTO) {
        view.setMusicVideoData(musicVideo)
    }

    override fun somethingWentWrong() {
        view.somethingWentWrong()
    }
}
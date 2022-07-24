package com.mmfsin.musicmaster.guesser.helper

import com.mmfsin.musicmaster.guesser.GuesserView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

class YearGuesserHelper(private val view: GuesserView) {

    fun isValidYear(pinViewText: String): Boolean {
        return (pinViewText.length == 4)
    }

    fun playVideo(youtubePlayerView: YouTubePlayerView, url: String) {
        youtubePlayerView.getYouTubePlayerWhenReady(object : YouTubePlayerCallback {
            override fun onYouTubePlayer(youTubePlayer: YouTubePlayer) {
                youTubePlayer.loadVideo(url, 0f)
            }
        })
    }

    fun pauseVideo(youtubePlayerView: YouTubePlayerView) {
        youtubePlayerView.getYouTubePlayerWhenReady(object : YouTubePlayerCallback {
            override fun onYouTubePlayer(youTubePlayer: YouTubePlayer) {
                youTubePlayer.pause()
            }
        })
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
        } else {
            view.somethingWentWrong()
        }
    }

    fun setMultiSolutionMessage(gOneYearStr: String, gTwoYearStr: String, correctYearStr: String) {
        val gOneYear = gOneYearStr.toIntOrNull()
        val gTwoYear = gTwoYearStr.toIntOrNull()
        val correctYear = correctYearStr.toInt()

        if (gOneYear != null && gTwoYear != null) {
            view.setMultiSolutionMessage(
                getPoints(gOneYear, correctYear),
                getPoints(gTwoYear, correctYear)
            )
        } else {
            view.somethingWentWrong()
        }
    }


    private fun getPoints(userYear: Int, correctYear: Int): Int {
        return if (userYear == correctYear) {
            2
        } else if (userYear > (correctYear - 3) && userYear < (correctYear + 3) && userYear != correctYear) {
            1
        } else {
            0
        }
    }
}

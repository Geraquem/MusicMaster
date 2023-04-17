package com.mmfsin.musicmaster.presentation.dashboard.year

import com.mmfsin.musicmaster.data.repository.MusicRepository
import com.mmfsin.musicmaster.domain.interfaces.IMusicRepository
import com.mmfsin.musicmaster.domain.models.MusicDTO
import com.mmfsin.musicmaster.domain.types.ResultType.*
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.math.absoluteValue

class YearPresenter(private val view: YearView) : IMusicRepository, CoroutineScope {

    override val coroutineContext: CoroutineContext = Dispatchers.Main

    private val repository by lazy { MusicRepository(this) }

    fun year4digits(year: String): Boolean = (year.length == 4)

    fun getMusicData(category: String) =
        launch(Dispatchers.IO) { repository.getMusicData(category) }

    override fun musicData(list: List<MusicDTO>) {
        launch { view.musicData(list) }
    }

    fun solution(userAnswerStr: String, correctYear: Long) {
        val userAnswer = userAnswerStr.toIntOrNull()
        userAnswer?.let { userYear ->
            val type = when ((userYear - correctYear.toInt()).absoluteValue) {
                0 -> GOOD
                1 -> ALMOST_GOOD
                2 -> ALMOST_GOOD
                3 -> ALMOST_GOOD
                else -> BAD
            }
            view.solution(type)
        } ?: run {
            view.somethingWentWrong()
        }
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

    override fun somethingWentWrong() {

    }
}
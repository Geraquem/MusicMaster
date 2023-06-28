//package com.mmfsin.musicmaster.presentation.dashboard.year
//
//import com.mmfsin.musicmaster.data.repository.MusicRepository
//import com.mmfsin.musicmaster.domain.interfaces.IMusicRepository
//import com.mmfsin.musicmaster.domain.models.Music
//import com.mmfsin.musicmaster.domain.types.ResultType
//import com.mmfsin.musicmaster.domain.types.ResultType.*
//import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
//import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback
//import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import kotlin.coroutines.CoroutineContext
//import kotlin.math.absoluteValue
//
//class YearPresenter(private val view: YearView) : IMusicRepository, CoroutineScope {
//
//    override val coroutineContext: CoroutineContext = Dispatchers.Main
//
//
//    fun year4digits(year: String): Boolean = (year.length == 4)
//
//    fun getMusicData(category: String) {}
////        launch(Dispatchers.IO) { repository.getMusicData(category) }
//
//    override fun musicData(list: List<Music>) {
//        launch { view.musicData(list) }
//    }
//
//    fun solution(userAnswerStr: String, correctYear: Long) {
//        val userAnswer = userAnswerStr.toIntOrNull()
//        userAnswer?.let { userYear ->
//            val type = getTypeFromSolution(userYear - correctYear.toInt())
//            view.solution(type)
//        } ?: run {
//            view.somethingWentWrong()
//        }
//    }
//
//    fun multiSolution(answers: Pair<String, String>, correctYear: Long) {
//        try {
//            val group1Answer = answers.first.toInt()
//            val group2Answer = answers.second.toInt()
//
//            val type1 = getTypeFromSolution(group1Answer - correctYear.toInt())
//            val type2 = getTypeFromSolution(group2Answer - correctYear.toInt())
//
//            view.multiSolution(Pair(type1, type2))
//
//        } catch (e: java.lang.Exception) {
//            view.somethingWentWrong()
//        }
//    }
//
//    private fun getTypeFromSolution(solution: Int): ResultType {
//        return when (solution.absoluteValue) {
//            0 -> GOOD
//            1 -> ALMOST_GOOD
//            2 -> ALMOST_GOOD
//            3 -> ALMOST_GOOD
//            else -> BAD
//        }
//    }
//
//    fun playVideo(youtubePlayerView: YouTubePlayerView, url: String) {
//        youtubePlayerView.getYouTubePlayerWhenReady(object : YouTubePlayerCallback {
//            override fun onYouTubePlayer(youTubePlayer: YouTubePlayer) {
//                youTubePlayer.loadVideo(url, 0f)
//            }
//        })
//    }
//
//    fun pauseVideo(youtubePlayerView: YouTubePlayerView) {
//        youtubePlayerView.getYouTubePlayerWhenReady(object : YouTubePlayerCallback {
//            override fun onYouTubePlayer(youTubePlayer: YouTubePlayer) {
//                youTubePlayer.pause()
//            }
//        })
//    }
//
//    override fun somethingWentWrong() {
//
//    }
//}
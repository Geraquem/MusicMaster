package com.mmfsin.musicmaster.presentation.dashboard

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

fun String.has4digits() = (this.length == 4)

fun YouTubePlayerView.playVideo(url: String) {
    this.getYouTubePlayerWhenReady(object : YouTubePlayerCallback {
        override fun onYouTubePlayer(youTubePlayer: YouTubePlayer) {
            youTubePlayer.loadVideo(url, 0f)
        }
    })
}

fun YouTubePlayerView.pauseVideo() {
    this.getYouTubePlayerWhenReady(object : YouTubePlayerCallback {
        override fun onYouTubePlayer(youTubePlayer: YouTubePlayer) {
            youTubePlayer.pause()
        }
    })
}
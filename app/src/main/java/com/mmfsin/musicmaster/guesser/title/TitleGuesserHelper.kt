package com.mmfsin.musicmaster.guesser.title

import android.view.View
import android.widget.ImageView
import com.mmfsin.musicmaster.R
import com.mmfsin.musicmaster.guesser.GuesserView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.ui.views.YouTubePlayerSeekBar
import com.pierfrancescosoffritti.androidyoutubeplayer.core.ui.views.YouTubePlayerSeekBarListener
import org.apache.commons.text.similarity.LevenshteinDistance


class TitleGuesserHelper(private val view: GuesserView) {

    fun shouldPauseMusic(
        shouldPause: Boolean,
        playButton: ImageView,
        youTubePlayerView: YouTubePlayerView
    ) {
        if (shouldPause) {
            pauseMusic(youTubePlayerView)
            playButton.tag = "paused"
            playButton.setImageResource(R.drawable.ic_play)
        } else {
            playMusic(youTubePlayerView)
            playButton.tag = "playing"
            playButton.setImageResource(R.drawable.ic_pause)
        }
    }

    fun pauseMusic(youTubePlayerView: YouTubePlayerView) {
        youTubePlayerView.getYouTubePlayerWhenReady(object : YouTubePlayerCallback {
            override fun onYouTubePlayer(youTubePlayer: YouTubePlayer) {
                youTubePlayer.pause()
            }
        })
    }

    fun playMusic(youTubePlayerView: YouTubePlayerView) {
        youTubePlayerView.getYouTubePlayerWhenReady(object : YouTubePlayerCallback {
            override fun onYouTubePlayer(youTubePlayer: YouTubePlayer) {
                youTubePlayer.play()
            }
        })
    }

    fun playYoutubeSeekBar(
        youTubePlayerView: YouTubePlayerView,
        url: String,
        seekBar: YouTubePlayerSeekBar,
        loading: View
    ) {
        val unstarted_cont = Array(10) { 0 };

        youTubePlayerView.getYouTubePlayerWhenReady(object : YouTubePlayerCallback {
            override fun onYouTubePlayer(youTubePlayer: YouTubePlayer) {
                youTubePlayer.loadVideo(url, 0F)
                youTubePlayer.addListener(object : YouTubePlayerListener {
                    override fun onApiChange(youTubePlayer: YouTubePlayer) {}

                    override fun onCurrentSecond(youTubePlayer: YouTubePlayer, second: Float) {}

                    override fun onError(
                        youTubePlayer: YouTubePlayer,
                        error: PlayerConstants.PlayerError
                    ) {
                    }

                    override fun onPlaybackQualityChange(
                        youTubePlayer: YouTubePlayer,
                        playbackQuality: PlayerConstants.PlaybackQuality
                    ) {
                    }

                    override fun onPlaybackRateChange(
                        youTubePlayer: YouTubePlayer,
                        playbackRate: PlayerConstants.PlaybackRate
                    ) {
                    }

                    override fun onReady(youTubePlayer: YouTubePlayer) {}

                    override fun onStateChange(
                        youTubePlayer: YouTubePlayer,
                        state: PlayerConstants.PlayerState
                    ) {
                        if (state == PlayerConstants.PlayerState.PLAYING) {
                            loading.visibility = View.GONE
                        }
                        if (state == PlayerConstants.PlayerState.UNSTARTED) {
                            unstarted_cont[0]++
                            if (unstarted_cont[0] == 2) {
                                unstarted_cont[0] = 0
                                loading.visibility = View.GONE
                            }
                        }
                    }

                    override fun onVideoDuration(youTubePlayer: YouTubePlayer, duration: Float) {}

                    override fun onVideoId(youTubePlayer: YouTubePlayer, videoId: String) {}

                    override fun onVideoLoadedFraction(
                        youTubePlayer: YouTubePlayer,
                        loadedFraction: Float
                    ) {
                    }

                })
                youTubePlayer.addListener(seekBar)
                seekBar.youtubePlayerSeekBarListener = object : YouTubePlayerSeekBarListener {
                    override fun seekTo(time: Float) {
                        youTubePlayer.seekTo(time)
                    }
                }
            }
        })
    }

    fun setSolutionMessage(userTitleStr: String, correctTitleStr: String) {
        val discordance = LevenshteinDistance()
        val percentage = discordance.apply(
            userTitleStr.map { it.uppercase() }.toString(),
            correctTitleStr.map { it.uppercase() }.toString()
        )
        when {
            (percentage == 0 || percentage == 1) -> view.setSolutionMessage(0)
            (percentage == 2 || percentage == 3 || percentage == 4) -> view.setSolutionMessage(1)
            else -> view.setSolutionMessage(2)
        }
    }
}
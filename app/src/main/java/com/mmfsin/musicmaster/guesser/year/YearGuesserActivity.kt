package com.mmfsin.musicmaster.guesser.year

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mmfsin.musicmaster.R
import com.mmfsin.musicmaster.guesser.model.MusicVideoDTO
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback
import kotlinx.android.synthetic.main.activity_year_guesser.*
import kotlinx.android.synthetic.main.include_solution_year.view.*

class YearGuesserActivity : AppCompatActivity(), YearGuesserView {

    private val presenter by lazy { YearGuesserPresenter(this) }

    private lateinit var goodPhrases: List<String>
    private lateinit var almostPhrases: List<String>
    private lateinit var badPhrases: List<String>

    private lateinit var category: String
    private lateinit var videoList: List<String>
    private lateinit var correctYear: String
    private var position = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_year_guesser)

        //loading VISIBLE

        pinView.addTextChangedListener(textWatcher)

        category = intent.getStringExtra("category").toString()
        if (category != "null") {

            goodPhrases = resources.getStringArray(R.array.goodPhrases).toList()
            almostPhrases = resources.getStringArray(R.array.almostPhrases).toList()
            badPhrases = resources.getStringArray(R.array.badPhrases).toList()

            presenter.getMusicVideoList(category)
        } else {
            somethingWentWrong()
        }

        comprobarButton.setOnClickListener {
            if (presenter.isValidYear(pinView.text.toString())) {
                comprobarButton.isEnabled = false
                pinView.isEnabled = false
                presenter.setSolutionMessage(pinView.text.toString(), correctYear)
            }
        }
    }

    override fun setMusicVideoList(list: List<String>) {
        videoList = list
        if (videoList.isNotEmpty()) {
            presenter.getMusicVideo(category, videoList[position])
        } else {
            somethingWentWrong()
        }
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun afterTextChanged(p0: Editable?) {
            if (presenter.isValidYear(pinView.text.toString())) {
                closeKeyboard()
            }
        }
    }

    override fun setMusicVideoData(musicVideo: MusicVideoDTO) {
        titleText.text = musicVideo.titulo
        artistText.text = musicVideo.artista
        youtubePlayerView.getYouTubePlayerWhenReady(object : YouTubePlayerCallback {
            override fun onYouTubePlayer(youTubePlayer: YouTubePlayer) {
                youTubePlayer.loadVideo(musicVideo.url, 0f)
            }
        })
        correctYear = musicVideo.year
        solution.solutionYear.text = correctYear

        //loading GONE
    }

    override fun setSolutionMessage(solutionResult: Int) {
        when (solutionResult) {
            0 -> {
                solution.messageText.text = goodPhrases[(goodPhrases.indices).random()]
                solution.messageText.setTextColor(resources.getColor(R.color.goodPhrase, null))
            }
            1 -> {
                solution.messageText.text = almostPhrases[(almostPhrases.indices).random()]
                solution.messageText.setTextColor(resources.getColor(R.color.almostPhrase, null))
            }
            2 -> {
                solution.messageText.text = badPhrases[(badPhrases.indices).random()]
                solution.messageText.setTextColor(resources.getColor(R.color.badPhrase, null))
            }
        }
        solution.visibility = View.VISIBLE
    }

    override fun somethingWentWrong() {
        Toast.makeText(this, getString(R.string.somethingWentWrong), Toast.LENGTH_SHORT).show()
    }

    private fun closeKeyboard() {
        this.currentFocus?.let { view ->
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}
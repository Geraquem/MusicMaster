package com.mmfsin.musicmaster.guesser.year

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.SweetAlertDialog
import com.mmfsin.musicmaster.R
import com.mmfsin.musicmaster.guesser.adapter.SwipeListener
import com.mmfsin.musicmaster.guesser.model.MusicVideoDTO
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback
import kotlinx.android.synthetic.main.activity_year_guesser.*
import kotlinx.android.synthetic.main.include_solution_year.view.*
import kotlin.properties.Delegates


class YearGuesserActivity : AppCompatActivity(), YearGuesserView {

    private val presenter by lazy { YearGuesserPresenter(this) }

    private lateinit var goodPhrases: List<String>
    private lateinit var almostPhrases: List<String>
    private lateinit var badPhrases: List<String>

    private lateinit var category: String
    private lateinit var videoList: List<String>
    private lateinit var correctYear: String
    private var position = 0

    private var scoreGood = 0
    private var scoreAlmost = 0
    private var scoreBad = 0

    private var showOnce = true

    //RPBA = Rock Pop Before2000 After2000
    private var isRPBA by Delegates.notNull<Boolean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_year_guesser)

        loading.visibility = View.VISIBLE

        pinView.addTextChangedListener(textWatcher)

        category = intent.getStringExtra("category").toString()
        if (category != "null") {
            isRPBA = presenter.isRPBA(category)
            goodPhrases = resources.getStringArray(R.array.goodPhrases).toList()
            almostPhrases = resources.getStringArray(R.array.almostPhrases).toList()
            badPhrases = resources.getStringArray(R.array.badPhrases).toList()
            presenter.getMusicVideoList(category)
        } else {
            somethingWentWrong()
        }

        comprobarButton.setOnClickListener {
            if (presenter.isValidYear(pinView.text.toString())) {
                pinView.isEnabled = false
                comprobarButton.isEnabled = false
                presenter.setSolutionMessage(pinView.text.toString(), correctYear)
            }
        }

        scrollView.setOnTouchListener(object : SwipeListener(this) {
            override fun onSwipeLeft() {
                position++
                if (position < videoList.size) {
                    loading.visibility = View.VISIBLE
                    initialAttributes()
                    setMusicVideo()
                }
            }

            override fun onSwipeRight() {}
            override fun onSwipeUp() {}
            override fun onSwipeDown() {}
        })
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

    override fun setMusicVideoList(list: List<String>) {
        videoList = list
        if (videoList.isNotEmpty()) {
            if (showOnce) {
                showOnce = false
                presenter.showSweetAlertSwipe(this)
            }
            setMusicVideo()

        } else {
            somethingWentWrong()
        }
    }

    private fun setMusicVideo() {
        if (isRPBA) {
            presenter.getMusicVideo("mix", videoList[position])
        } else {
            presenter.getMusicVideo(category, videoList[position])
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

        loading.visibility = View.GONE
    }

    override fun setSolutionMessage(solutionResult: Int) {
        when (solutionResult) {
            0 -> {
                solution.messageText.text = goodPhrases[(goodPhrases.indices).random()]
                solution.messageText.setTextColor(resources.getColor(R.color.goodPhrase, null))
                scoreGood++
                goodScore.text = scoreGood.toString()
            }
            1 -> {
                solution.messageText.text = almostPhrases[(almostPhrases.indices).random()]
                solution.messageText.setTextColor(resources.getColor(R.color.almostPhrase, null))
                scoreAlmost++
                almostScore.text = scoreAlmost.toString()
            }
            2 -> {
                solution.messageText.text = badPhrases[(badPhrases.indices).random()]
                solution.messageText.setTextColor(resources.getColor(R.color.badPhrase, null))
                scoreBad++
                badScore.text = scoreBad.toString()
            }
        }
        solution.visibility = View.VISIBLE
    }

    private fun initialAttributes() {
        closeKeyboard()
        pinView.isEnabled = true
        pinView.text = null
        comprobarButton.isEnabled = true
        solution.visibility = View.GONE
    }

    override fun somethingWentWrong() {
        presenter.showSweetAlertError(this)
    }

    private fun closeKeyboard() {
        this.currentFocus?.let { view ->
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    override fun onBackPressed() {
        SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
            .setTitleText(getString(R.string.wannaExit))
            .setConfirmText(getString(R.string.yes))
            .setConfirmClickListener { finish() }
            .setCancelButton(getString(R.string.no)) { sDialog -> sDialog.dismissWithAnimation() }
            .show()
    }
}
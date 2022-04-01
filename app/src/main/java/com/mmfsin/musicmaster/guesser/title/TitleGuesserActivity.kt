package com.mmfsin.musicmaster.guesser.title

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.mmfsin.musicmaster.R
import com.mmfsin.musicmaster.guesser.GuesserView
import com.mmfsin.musicmaster.guesser.adapter.SwipeListener
import com.mmfsin.musicmaster.guesser.common.Common
import com.mmfsin.musicmaster.guesser.common.CommonPresenter
import com.mmfsin.musicmaster.guesser.model.MusicVideoDTO
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import kotlinx.android.synthetic.main.activity_title_guesser.*
import kotlinx.android.synthetic.main.include_score_board.view.*
import kotlinx.android.synthetic.main.include_solution_title.view.*
import kotlinx.android.synthetic.main.include_toolbar_dashboard.view.*
import kotlin.properties.Delegates

class TitleGuesserActivity : AppCompatActivity(), GuesserView {

    /******* INSTERTICIAL (CRTL + SHIFT + R)
     * REAL  ca-app-pub-4515698012373396/3110817258
     * PRUEBAS ca-app-pub-3940256099942544/1033173712
     */

    private val helper by lazy { TitleGuesserHelper(this) }
    private val presenter by lazy { CommonPresenter(this, this) }

    private lateinit var youTubePlayerView: YouTubePlayerView

    private lateinit var goodPhrases: List<String>
    private lateinit var almostPhrases: List<String>
    private lateinit var badPhrases: List<String>

    private lateinit var category: String
    private lateinit var videoList: List<String>
    private lateinit var correctTitle: String
    private var position = 0

    private var scoreGood = 0
    private var scoreAlmost = 0
    private var scoreBad = 0

    private var showOnce = true

    //RPBA = Rock Pop Before2000 After2000
    private var isRPBA by Delegates.notNull<Boolean>()

    private var mInterstitialAd: InterstitialAd? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_title_guesser)

        loading.visibility = View.VISIBLE

        MobileAds.initialize(this) {}
        loadInterstitial(AdRequest.Builder().build())

        youTubePlayerView = YouTubePlayerView(this)

        category = intent.getStringExtra("category").toString()
        if (category != "null") {
            Common().getCategoryTitle(this, toolbar.category, category)
            isRPBA = Common().isRPBA(this, category)
            goodPhrases = resources.getStringArray(R.array.goodPhrases).toList()
            almostPhrases = resources.getStringArray(R.array.almostPhrases).toList()
            badPhrases = resources.getStringArray(R.array.badPhrases).toList()
            presenter.getMusicVideoList(category)
        } else {
            somethingWentWrong()
        }

        val playButton = play_pause_button
        play_pause_button.setOnClickListener {
            if (playButton.tag == "paused") {
                helper.shouldPauseMusic(false, playButton, youTubePlayerView)
            } else {
                helper.shouldPauseMusic(true, playButton, youTubePlayerView)
            }
        }

        comprobarButton.setOnClickListener {
            if (et_title.text.toString().isNotEmpty()) {
                et_title.isEnabled = false
                comprobarButton.isEnabled = false
                helper.setSolutionMessage(et_title.text.toString(), correctTitle)
            }
        }

        scrollView.setOnTouchListener(object : SwipeListener(this) {
            override fun onSwipeLeft() {
                position++
                if (position < videoList.size) {
                    loading.visibility = View.VISIBLE
                    showIntersticial()
                    initialAttributes()
                    getMusicVideoData()
                }
            }

            override fun onSwipeRight() {}
            override fun onSwipeUp() {}
            override fun onSwipeDown() {}
        })
    }

    override fun setMusicVideoData(musicVideo: MusicVideoDTO) {
        correctTitle = musicVideo.titulo
        solution.solutionTitle.text = correctTitle
        helper.playYoutubeSeekBar(youTubePlayerView, musicVideo.url, youtubeSeekBar, loading)
    }

    override fun setMusicVideoList(list: List<String>) {
        videoList = list
        if (videoList.isNotEmpty()) {
            if (showOnce) {
                showOnce = false
                Common().showSweetAlertSwipe(this)
            }
            getMusicVideoData()

        } else {
            somethingWentWrong()
        }
    }

    private fun getMusicVideoData() {
        if (isRPBA) {
            presenter.getMusicVideoData("mix", videoList[position])
        } else {
            presenter.getMusicVideoData(category, videoList[position])
        }
    }

    private fun initialAttributes() {
        helper.pauseMusic(youTubePlayerView)
        closeKeyboard()
        et_title.isEnabled = true
        et_title.text = null
        comprobarButton.isEnabled = true
        solution.visibility = View.GONE
    }

    private fun closeKeyboard() {
        this.currentFocus?.let { view ->
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    override fun setSolutionMessage(solutionResult: Int) {
        when (solutionResult) {
            0 -> {
                solution.messageText.text = goodPhrases[(goodPhrases.indices).random()]
                solution.messageText.setTextColor(resources.getColor(R.color.goodPhrase, null))
                scoreGood++
                includeScore.goodScore.text = scoreGood.toString()
                includeScore.lottieGood.playAnimation()
            }
            1 -> {
                solution.messageText.text = almostPhrases[(almostPhrases.indices).random()]
                solution.messageText.setTextColor(resources.getColor(R.color.almostPhrase, null))
                scoreAlmost++
                includeScore.almostScore.text = scoreAlmost.toString()
                includeScore.lottieAlmost.playAnimation()
            }
            2 -> {
                solution.messageText.text = badPhrases[(badPhrases.indices).random()]
                solution.messageText.setTextColor(resources.getColor(R.color.badPhrase, null))
                scoreBad++
                includeScore.badScore.text = scoreBad.toString()
                includeScore.lottieBad.playAnimation()
            }
        }
        solution.visibility = View.VISIBLE
    }

    override fun somethingWentWrong() {
        Common().showSweetAlertError(this)
    }

    override fun onBackPressed() {
        SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
            .setTitleText(getString(R.string.wannaExit))
            .setConfirmText(getString(R.string.yes))
            .setConfirmClickListener {
                helper.pauseMusic(youTubePlayerView)
                finish()
            }
            .setCancelButton(getString(R.string.no)) { sDialog -> sDialog.dismissWithAnimation() }
            .show()
    }

    override fun onStop() {
        super.onStop()
        helper.shouldPauseMusic(true, play_pause_button, youTubePlayerView)
    }


    private fun loadInterstitial(adRequest: AdRequest) {
        InterstitialAd.load(
            this,
            "ca-app-pub-3940256099942544/1033173712",
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    mInterstitialAd = null
                    loadInterstitial(AdRequest.Builder().build())
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    mInterstitialAd = interstitialAd
                }
            })
    }

    private fun showIntersticial() {
        if ((position % 20) == 0 && mInterstitialAd != null) {
            mInterstitialAd!!.show(this)
            loadInterstitial(AdRequest.Builder().build())
            helper.shouldPauseMusic(true, play_pause_button, youTubePlayerView)
        }
    }
}
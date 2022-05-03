package com.mmfsin.musicmaster.guesser.year

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import kotlinx.android.synthetic.main.activity_year_guesser.*
import kotlinx.android.synthetic.main.include_score_board.view.*
import kotlinx.android.synthetic.main.include_solution_year.view.*
import kotlinx.android.synthetic.main.include_toolbar_dashboard.view.*
import kotlin.properties.Delegates


class YearGuesserActivity : AppCompatActivity(), GuesserView {

    /******* INSTERTICIAL (CRTL + SHIFT + R)
     * REAL  ca-app-pub-4515698012373396/4423898926
     * PRUEBAS ca-app-pub-3940256099942544/1033173712
     */

    private val helper by lazy { YearGuesserHelper(this) }
    private val presenter by lazy { CommonPresenter(this, this) }

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

    private var mInterstitialAd: InterstitialAd? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_year_guesser)

        loading.visibility = View.VISIBLE

        MobileAds.initialize(this) {}
        loadInterstitial(AdRequest.Builder().build())

        pinView.addTextChangedListener(textWatcher)

        toolbar.arrowBack.setOnClickListener { onBackPressed() }

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

        comprobarButton.setOnClickListener {
            if (helper.isValidYear(pinView.text.toString())) {
                pinView.isEnabled = false
                comprobarButton.isEnabled = false
                helper.setSolutionMessage(pinView.text.toString(), correctYear)
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

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun afterTextChanged(p0: Editable?) {
            if (helper.isValidYear(pinView.text.toString())) {
                closeKeyboard()
            }
        }
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

    override fun setMusicVideoData(musicVideo: MusicVideoDTO) {
        titleText.text = musicVideo.titulo
        artistText.text = musicVideo.artista
        helper.playVideo(youtubePlayerView, musicVideo.url)
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

    private fun initialAttributes() {
        closeKeyboard()
        pinView.isEnabled = true
        pinView.text = null
        comprobarButton.isEnabled = true
        solution.visibility = View.GONE
    }

    override fun somethingWentWrong() {
        Common().showSweetAlertError(this)
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

    private fun loadInterstitial(adRequest: AdRequest) {
        InterstitialAd.load(
            this,
            "ca-app-pub-4515698012373396/4423898926",
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    mInterstitialAd = null
                    loadInterstitial(AdRequest.Builder().build())
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    mInterstitialAd = interstitialAd
                    helper.pauseVideo(youtubePlayerView)
                }
            })
    }

    private fun showIntersticial() {
        if ((position % 20) == 0 && mInterstitialAd != null) {
            mInterstitialAd!!.show(this)
            loadInterstitial(AdRequest.Builder().build())
        }
    }
}
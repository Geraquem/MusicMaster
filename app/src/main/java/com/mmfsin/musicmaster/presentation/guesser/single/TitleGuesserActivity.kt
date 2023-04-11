package com.mmfsin.musicmaster.presentation.guesser.single

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
import com.mmfsin.musicmaster.databinding.ActivityTitleGuesserBinding
import com.mmfsin.musicmaster.presentation.guesser.GuesserView
import com.mmfsin.musicmaster.presentation.guesser.common.Common
import com.mmfsin.musicmaster.presentation.guesser.common.CommonPresenter
import com.mmfsin.musicmaster.presentation.guesser.helper.TitleGuesserHelper
import com.mmfsin.musicmaster.presentation.guesser.model.MusicVideoDTO
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import kotlin.properties.Delegates

class TitleGuesserActivity : AppCompatActivity(), GuesserView {

    /******* INSTERTICIAL (CRTL + SHIFT + R)
     * REAL  ca-app-pub-4515698012373396/4423898926
     * PRUEBAS ca-app-pub-3940256099942544/1033173712
     */

    private lateinit var binding: ActivityTitleGuesserBinding

    private val helper by lazy { TitleGuesserHelper(this) }
    private val presenter by lazy { CommonPresenter(this) }

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

    //RPBA = Rock Pop Before2000 After2000
    private var isRPBA by Delegates.notNull<Boolean>()

    private var mInterstitialAd: InterstitialAd? = null

    //    private val mInterstitalId = "ca-app-pub-3940256099942544/1033173712"
    private val mInterstitalId = "ca-app-pub-4515698012373396/4423898926"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTitleGuesserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loading.root.visibility = View.VISIBLE

        MobileAds.initialize(this) {}
        loadInterstitial(AdRequest.Builder().build())

        youTubePlayerView = YouTubePlayerView(this)

        category = intent.getStringExtra("category").toString()
        if (category != "null") {
            Common().getCategoryTitle(this, binding.toolbar.category, category)
            isRPBA = Common().isRPBA(this, category)
            goodPhrases = resources.getStringArray(R.array.goodPhrases).toList()
            almostPhrases = resources.getStringArray(R.array.almostPhrases).toList()
            badPhrases = resources.getStringArray(R.array.badPhrases).toList()

            /** START */
            presenter.getMusicVideoList(category)
        } else {
            somethingWentWrong()
        }

        listeners()
    }

    private fun listeners() {
        val playButton = binding.playPauseButton
        binding.playPauseButton.setOnClickListener {
            if (playButton.tag == "paused") {
                helper.shouldPauseMusic(false, playButton, youTubePlayerView)
            } else {
                helper.shouldPauseMusic(true, playButton, youTubePlayerView)
            }
        }

        binding.comprobarButton.setOnClickListener {
            if (binding.etTitle.text.toString().isNotEmpty()) {
                binding.etTitle.isEnabled = false
                binding.comprobarButton.isEnabled = false
                helper.setSolutionMessage(binding.etTitle.text.toString(), correctTitle)
            }
        }

        binding.btnNext.setOnClickListener {
            position++
            if (position < videoList.size) {
                binding.loading.root.visibility = View.VISIBLE
                showIntersticial()
                initialAttributes()
                getMusicVideoData()
            }
        }
    }

    override fun setMusicVideoData(musicVideo: MusicVideoDTO) {
        correctTitle = musicVideo.titulo
        binding.solution.solutionTitle.text = correctTitle
        helper.playYoutubeSeekBar(
            youTubePlayerView, musicVideo.url, binding.youtubeSeekBar, binding.loading.root
        )
    }

    override fun setMusicVideoList(list: List<String>) {
        videoList = list
        if (videoList.isNotEmpty()) getMusicVideoData() else somethingWentWrong()
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
        binding.etTitle.isEnabled = true
        binding.etTitle.text = null
        binding.comprobarButton.isEnabled = true
        binding.solution.root.visibility = View.GONE
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
                binding.solution.messageText.text = goodPhrases[(goodPhrases.indices).random()]
                binding.solution.messageText.setTextColor(
                    resources.getColor(
                        R.color.goodPhrase, null
                    )
                )
                scoreGood++
                binding.includeScore.goodScore.text = scoreGood.toString()
                binding.includeScore.lottieGood.playAnimation()
            }
            1 -> {
                binding.solution.messageText.text = almostPhrases[(almostPhrases.indices).random()]
                binding.solution.messageText.setTextColor(
                    resources.getColor(
                        R.color.almostPhrase, null
                    )
                )
                scoreAlmost++
                binding.includeScore.almostScore.text = scoreAlmost.toString()
                binding.includeScore.lottieAlmost.playAnimation()
            }
            2 -> {
                binding.solution.messageText.text = badPhrases[(badPhrases.indices).random()]
                binding.solution.messageText.setTextColor(
                    resources.getColor(
                        R.color.badPhrase, null
                    )
                )
                scoreBad++
                binding.includeScore.badScore.text = scoreBad.toString()
                binding.includeScore.lottieBad.playAnimation()
            }
        }
        binding.solution.root.visibility = View.VISIBLE
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
        helper.shouldPauseMusic(true, binding.playPauseButton, youTubePlayerView)
    }


    private fun loadInterstitial(adRequest: AdRequest) {
        InterstitialAd.load(this, mInterstitalId, adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                mInterstitialAd = null
                loadInterstitial(AdRequest.Builder().build())
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                mInterstitialAd = interstitialAd
                helper.shouldPauseMusic(true, binding.playPauseButton, youTubePlayerView)
            }
        })
    }

    private fun showIntersticial() {
        if ((position % 20) == 0 && mInterstitialAd != null) {
            mInterstitialAd!!.show(this)
            loadInterstitial(AdRequest.Builder().build())
        }
    }

    override fun setMultiSolutionMessage(gOnePoints: Int, gTwoPoints: Int) {}
}
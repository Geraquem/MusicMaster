package com.mmfsin.musicmaster.presentation.guesser.multiplayer

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
import com.mmfsin.musicmaster.databinding.ActivityMultiYearGuesserBinding
import com.mmfsin.musicmaster.presentation.guesser.GuesserView
import com.mmfsin.musicmaster.presentation.guesser.common.Common
import com.mmfsin.musicmaster.presentation.guesser.common.CommonPresenter
import com.mmfsin.musicmaster.presentation.guesser.helper.YearGuesserHelper
import com.mmfsin.musicmaster.presentation.guesser.model.MusicVideoDTO
import kotlin.properties.Delegates

class MultiYearGuesser : AppCompatActivity(), GuesserView {

    /******* INSTERTICIAL (CRTL + SHIFT + R)
     * REAL  ca-app-pub-4515698012373396/4423898926
     * PRUEBAS ca-app-pub-3940256099942544/1033173712
     */

    private lateinit var binding: ActivityMultiYearGuesserBinding

    private val helper by lazy { YearGuesserHelper(this) }
    private val presenter by lazy { CommonPresenter(this) }

    private lateinit var category: String
    private lateinit var videoList: List<String>
    private lateinit var correctYear: String

    private var position = 0

    private var scoreG1 = 0
    private var scoreG2 = 0

    //RPBA = Rock Pop Before2000 After2000
    private var isRPBA by Delegates.notNull<Boolean>()

    private var mInterstitialAd: InterstitialAd? = null

        private val mInterstitalId = "ca-app-pub-3940256099942544/1033173712"
//    private val mInterstitalId = "ca-app-pub-4515698012373396/4423898926"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMultiYearGuesserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loading.root.visibility = View.VISIBLE

        MobileAds.initialize(this) {}
        loadInterstitial(AdRequest.Builder().build())

        binding.pinViewOne.addTextChangedListener(textWatcherOne)
        binding.pinViewTwo.addTextChangedListener(textWatcherTwo)
        binding.toolbar.arrowBack.setOnClickListener { onBackPressed() }

        category = intent.getStringExtra("category").toString()
        if (category != "null") {
            Common().getCategoryTitle(this, binding.toolbar.category, category)
            isRPBA = Common().isRPBA(this, category)

            /** START */
            presenter.getMusicVideoList(category)
        } else {
            somethingWentWrong()
        }

        listeners()
    }

    private val textWatcherOne = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun afterTextChanged(p0: Editable?) {
            if (helper.isValidYear(binding.pinViewOne.text.toString())) {
                closeKeyboard()
            }
        }
    }
    private val textWatcherTwo = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun afterTextChanged(p0: Editable?) {
            if (helper.isValidYear(binding.pinViewTwo.text.toString())) {
                closeKeyboard()
            }
        }
    }

    private fun listeners() {
        binding.comprobarButton.setOnClickListener {
            if (helper.isValidYear(binding.pinViewOne.text.toString()) &&
                helper.isValidYear(binding.pinViewTwo.text.toString())
            ) {
                binding.pinViewOne.isEnabled = false
                binding.pinViewTwo.isEnabled = false
                binding.comprobarButton.isEnabled = false
                helper.setMultiSolutionMessage(
                    binding.pinViewOne.text.toString(),
                    binding.pinViewTwo.text.toString(),
                    correctYear
                )
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

    override fun setMusicVideoData(musicVideo: MusicVideoDTO) {
        binding.titleText.text = musicVideo.titulo
        binding.artistText.text = musicVideo.artista
        helper.playVideo(binding.youtubePlayerView, musicVideo.url)
        correctYear = musicVideo.year
        binding.solution.solutionYear.text = correctYear

        binding.loading.root.visibility = View.GONE
    }

    override fun setSolutionMessage(solutionResult: Int) {}

    override fun setMultiSolutionMessage(gOnePoints: Int, gTwoPoints: Int) {

        when (gOnePoints) {
            0 -> binding.solution.pointsgOne.setTextColor(
                resources.getColor(R.color.badPhrase, null)
            )
            1 -> {
                binding.solution.pointsgOne.setTextColor(
                    resources.getColor(R.color.almostPhrase, null)
                )
                binding.includeScore.lottieGoodOne.playAnimation()
            }
            2 -> {
                binding.solution.pointsgOne.setTextColor(
                    resources.getColor(R.color.goodPhrase, null)
                )
                binding.includeScore.lottieGoodOne.playAnimation()
            }
        }

        when (gTwoPoints) {
            0 -> binding.solution.pointsgTwo.setTextColor(
                resources.getColor(R.color.badPhrase, null)
            )
            1 -> {
                binding.solution.pointsgTwo.setTextColor(
                    resources.getColor(R.color.almostPhrase, null)
                )
                binding.includeScore.lottieGoodTwo.playAnimation()
            }
            2 -> {
                binding.solution.pointsgTwo.setTextColor(
                    resources.getColor(R.color.goodPhrase, null)
                )
                binding.includeScore.lottieGoodTwo.playAnimation()
            }
        }

        binding.solution.pointsgOne.text =
            getString(R.string.gOneSolution, gOnePoints.toString())
        binding.solution.pointsgTwo.text =
            getString(R.string.gTwoSolution, gTwoPoints.toString())

        scoreG1 += gOnePoints
        scoreG2 += gTwoPoints
        binding.includeScore.scoreOne.text = scoreG1.toString()
        binding.includeScore.scoreTwo.text = scoreG2.toString()

        binding.solution.root.visibility = View.VISIBLE
    }

    private fun initialAttributes() {
        closeKeyboard()
        binding.pinViewOne.isEnabled = true
        binding.pinViewTwo.isEnabled = true
        binding.pinViewOne.text = null
        binding.pinViewTwo.text = null
        binding.comprobarButton.isEnabled = true
        binding.solution.root.visibility = View.GONE
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
        InterstitialAd.load(this, mInterstitalId, adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                mInterstitialAd = null
                loadInterstitial(AdRequest.Builder().build())
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                mInterstitialAd = interstitialAd
                helper.pauseVideo(binding.youtubePlayerView)
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
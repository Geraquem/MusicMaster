package com.mmfsin.musicmaster.guesser.single

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
import com.mmfsin.musicmaster.databinding.ActivityYearGuesserBinding
import com.mmfsin.musicmaster.guesser.GuesserView
import com.mmfsin.musicmaster.guesser.common.Common
import com.mmfsin.musicmaster.guesser.common.CommonPresenter
import com.mmfsin.musicmaster.guesser.helper.YearGuesserHelper
import com.mmfsin.musicmaster.guesser.model.MusicVideoDTO
import kotlin.properties.Delegates


class YearGuesserActivity : AppCompatActivity(), GuesserView {

    /******* INSTERTICIAL (CRTL + SHIFT + R)
     * REAL  ca-app-pub-4515698012373396/4423898926
     * PRUEBAS ca-app-pub-3940256099942544/1033173712
     */

    private lateinit var binding: ActivityYearGuesserBinding

    private val helper by lazy { YearGuesserHelper(this) }
    private val presenter by lazy { CommonPresenter(this) }

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

    //RPBA = Rock Pop Before2000 After2000
    private var isRPBA by Delegates.notNull<Boolean>()

    private var mInterstitialAd: InterstitialAd? = null

    //    private val mInterstitalId = "ca-app-pub-3940256099942544/1033173712"
    private val mInterstitalId = "ca-app-pub-4515698012373396/4423898926"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityYearGuesserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loading.root.visibility = View.VISIBLE

        MobileAds.initialize(this) {}
        loadInterstitial(AdRequest.Builder().build())

        binding.pinView.addTextChangedListener(textWatcher)
        binding.toolbar.arrowBack.setOnClickListener { onBackPressed() }

        category = intent.getStringExtra("category").toString()
        if (category != "null") {
            Common().getCategoryTitle(this, binding.toolbar.category, category)
            isRPBA = Common().isRPBA(this, category)
            goodPhrases = resources.getStringArray(R.array.goodPhrases).toList()
            almostPhrases = resources.getStringArray(R.array.almostPhrases).toList()
            badPhrases = resources.getStringArray(R.array.badPhrases).toList()

            /** START */
            presenter.getMusicVideoList(category)
        } else somethingWentWrong()
        listeners()
    }

    private fun listeners() {
        binding.comprobarButton.setOnClickListener {
            if (helper.isValidYear(binding.pinView.text.toString())) {
                binding.pinView.isEnabled = false
                binding.comprobarButton.isEnabled = false
                helper.setSolutionMessage(binding.pinView.text.toString(), correctYear)
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

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun afterTextChanged(p0: Editable?) {
            if (helper.isValidYear(binding.pinView.text.toString())) {
                closeKeyboard()
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

    override fun setSolutionMessage(solutionResult: Int) {
        when (solutionResult) {
            0 -> {
                binding.solution.messageText.text = goodPhrases[(goodPhrases.indices).random()]
                binding.solution.messageText.setTextColor(
                    resources.getColor(R.color.goodPhrase, null)
                )
                scoreGood++
                binding.includeScore.goodScore.text = scoreGood.toString()
                binding.includeScore.lottieGood.playAnimation()
            }
            1 -> {
                binding.solution.messageText.text = almostPhrases[(almostPhrases.indices).random()]
                binding.solution.messageText.setTextColor(
                    resources.getColor(R.color.almostPhrase, null)
                )
                scoreAlmost++
                binding.includeScore.almostScore.text = scoreAlmost.toString()
                binding.includeScore.lottieAlmost.playAnimation()
            }
            2 -> {
                binding.solution.messageText.text = badPhrases[(badPhrases.indices).random()]
                binding.solution.messageText.setTextColor(
                    resources.getColor(R.color.badPhrase, null)
                )
                scoreBad++
                binding.includeScore.badScore.text = scoreBad.toString()
                binding.includeScore.lottieBad.playAnimation()
            }
        }
        binding.solution.root.visibility = View.VISIBLE
    }

    private fun initialAttributes() {
        closeKeyboard()
        binding.pinView.isEnabled = true
        binding.pinView.text = null
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

    override fun setMultiSolutionMessage(gOnePoints: Int, gTwoPoints: Int) {}
}
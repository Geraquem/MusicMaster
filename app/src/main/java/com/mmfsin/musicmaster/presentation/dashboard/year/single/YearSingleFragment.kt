package com.mmfsin.musicmaster.presentation.dashboard.year.single

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.mmfsin.musicmaster.R
import com.mmfsin.musicmaster.common.BaseFragment
import com.mmfsin.musicmaster.databinding.FragmentYearSingleBinding
import com.mmfsin.musicmaster.domain.models.MusicDTO
import com.mmfsin.musicmaster.presentation.dashboard.IDashboardListener
import com.mmfsin.musicmaster.presentation.dashboard.year.YearPresenter
import com.mmfsin.musicmaster.presentation.dashboard.year.YearView

class YearSingleFragment(val category: String, val listener: IDashboardListener) :
    BaseFragment<FragmentYearSingleBinding>(), YearView {

    //    private val helper by lazy { YearGuesserHelper(this) }
    private val presenter by lazy { YearPresenter(this) }

    private lateinit var goodPhrases: List<String>
    private lateinit var almostPhrases: List<String>
    private lateinit var badPhrases: List<String>

    private lateinit var videoList: List<String>
    private lateinit var correctYear: String
    private var position = 0

    private var scoreGood = 0
    private var scoreAlmost = 0
    private var scoreBad = 0

    override fun inflateView(
        inflater: LayoutInflater, container: ViewGroup?
    ) = FragmentYearSingleBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        goodPhrases = resources.getStringArray(R.array.good_phrases).toList()
        almostPhrases = resources.getStringArray(R.array.almost_phrases).toList()
        badPhrases = resources.getStringArray(R.array.bad_phrases).toList()

        presenter.getMusicData(category)
    }

    override fun setUI() {
        binding.apply {
            listener.changeToolbar(category)
            pinView.addTextChangedListener(textWatcher)
            pinView.isCursorVisible = false
        }
    }

    override fun setListeners() {
        binding.apply {
            pinView.setOnClickListener {
            }
        }
    }

    override fun musicData(list: List<MusicDTO>) {
        binding.loading.root.visibility = View.GONE
        Toast.makeText(this@YearSingleFragment.requireContext(), "acabó", Toast.LENGTH_SHORT).show()
    }

//    private fun listeners() {
//        binding.comprobarButton.setOnClickListener {
//            if (helper.isValidYear(binding.pinView.text.toString())) {
//                binding.pinView.isEnabled = false
//                binding.comprobarButton.isEnabled = false
//                helper.setSolutionMessage(binding.pinView.text.toString(), correctYear)
//            }
//        }
//
//        binding.btnNext.setOnClickListener {
//            position++
//            if (position < videoList.size) {
//                binding.loading.root.visibility = View.VISIBLE
//                showIntersticial()
//                initialAttributes()
//                getMusicVideoData()
//            }
//        }
//    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun afterTextChanged(p0: Editable?) {
            if (presenter.year4digits(binding.pinView.text.toString())) {
                listener.closeKeyboard()
            }
        }
    }

//    override fun setMusicVideoList(list: List<String>) {
//        videoList = list
//        if (videoList.isNotEmpty()) getMusicVideoData() else somethingWentWrong()
//    }

//    private fun getMusicVideoData() {
//        if (isRPBA) {
//            presenter.getMusicVideoData("mix", videoList[position])
//        } else {
//            presenter.getMusicVideoData(category, videoList[position])
//        }
//    }

//    override fun setMusicVideoData(musicVideo: MusicVideoDTO) {
//        binding.titleText.text = musicVideo.titulo
//        binding.artistText.text = musicVideo.artista
//        helper.playVideo(binding.youtubePlayerView, musicVideo.url)
//        correctYear = musicVideo.year
//        binding.solution.solutionYear.text = correctYear
//
//        binding.loading.root.visibility = View.GONE
//    }
//
//    override fun setSolutionMessage(solutionResult: Int) {
//        when (solutionResult) {
//            0 -> {
//                binding.solution.messageText.text = goodPhrases[(goodPhrases.indices).random()]
//                binding.solution.messageText.setTextColor(
//                    resources.getColor(R.color.goodPhrase, null)
//                )
//                scoreGood++
//                binding.includeScore.goodScore.text = scoreGood.toString()
//                binding.includeScore.lottieGood.playAnimation()
//            }
//            1 -> {
//                binding.solution.messageText.text = almostPhrases[(almostPhrases.indices).random()]
//                binding.solution.messageText.setTextColor(
//                    resources.getColor(R.color.almostPhrase, null)
//                )
//                scoreAlmost++
//                binding.includeScore.almostScore.text = scoreAlmost.toString()
//                binding.includeScore.lottieAlmost.playAnimation()
//            }
//            2 -> {
//                binding.solution.messageText.text = badPhrases[(badPhrases.indices).random()]
//                binding.solution.messageText.setTextColor(
//                    resources.getColor(R.color.badPhrase, null)
//                )
//                scoreBad++
//                binding.includeScore.badScore.text = scoreBad.toString()
//                binding.includeScore.lottieBad.playAnimation()
//            }
//        }
//        binding.solution.root.visibility = View.VISIBLE
//    }

//    private fun initialAttributes() {
//        closeKeyboard()
//        binding.pinView.isEnabled = true
//        binding.pinView.text = null
//        binding.comprobarButton.isEnabled = true
//        binding.solution.root.visibility = View.GONE
//    }
//
//    override fun onBackPressed() {
//        SweetAlertDialog(
//            this,
//            SweetAlertDialog.WARNING_TYPE
//        ).setTitleText(getString(R.string.wannaExit)).setConfirmText(getString(R.string.yes))
//            .setConfirmClickListener { finish() }
//            .setCancelButton(getString(R.string.no)) { sDialog -> sDialog.dismissWithAnimation() }
//            .show()
//    }
}
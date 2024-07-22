package com.mmfsin.musicmaster.presentation.dashboard.year.single

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.mmfsin.musicmaster.R
import com.mmfsin.musicmaster.base.BaseFragment
import com.mmfsin.musicmaster.base.bedrock.BedRockActivity
import com.mmfsin.musicmaster.databinding.FragmentYearSingleBinding
import com.mmfsin.musicmaster.domain.mappers.getFontFamily
import com.mmfsin.musicmaster.domain.models.Music
import com.mmfsin.musicmaster.presentation.MainActivity
import com.mmfsin.musicmaster.presentation.dashboard.dialog.NoMoreDialog
import com.mmfsin.musicmaster.presentation.dashboard.has4digits
import com.mmfsin.musicmaster.presentation.dashboard.pauseVideo
import com.mmfsin.musicmaster.presentation.dashboard.playVideo
import com.mmfsin.musicmaster.presentation.models.SolutionType
import com.mmfsin.musicmaster.presentation.models.SolutionType.ALMOST_GOOD
import com.mmfsin.musicmaster.presentation.models.SolutionType.BAD
import com.mmfsin.musicmaster.presentation.models.SolutionType.GOOD
import com.mmfsin.musicmaster.utils.CATEGORY_ID
import com.mmfsin.musicmaster.utils.closeKeyboard
import com.mmfsin.musicmaster.utils.countDown
import com.mmfsin.musicmaster.utils.shouldShowInterstitial
import com.mmfsin.musicmaster.utils.showErrorDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class YearSingleFragment : BaseFragment<FragmentYearSingleBinding, YearSingleViewModel>() {

    override val viewModel: YearSingleViewModel by viewModels()
    private lateinit var mContext: Context

    private var categoryId: String? = null

    private lateinit var goodPhrases: List<String>
    private lateinit var almostPhrases: List<String>
    private lateinit var badPhrases: List<String>

    private lateinit var music: List<Music>
    private var position = 0
    private var solutionYear: Long = 0

    private var scoreGood = 0
    private var scoreAlmost = 0
    private var scoreBad = 0

    override fun inflateView(
        inflater: LayoutInflater, container: ViewGroup?
    ) = FragmentYearSingleBinding.inflate(inflater, container, false)

    override fun getBundleArgs() {
        arguments?.let { categoryId = it.getString(CATEGORY_ID) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        categoryId?.let { viewModel.getCategory(it) } ?: run { error() }
    }

    override fun setUI() {
        binding.apply {
            loading.root.visibility = View.VISIBLE
            goodPhrases = resources.getStringArray(R.array.good_phrases).toList().shuffled()
            almostPhrases = resources.getStringArray(R.array.almost_phrases).toList().shuffled()
            badPhrases = resources.getStringArray(R.array.bad_phrases).toList().shuffled()
            pinView.addTextChangedListener(textWatcher)
            pinView.isCursorVisible = false
            restartAnimations()
        }
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun afterTextChanged(p0: Editable?) {
            if (binding.pinView.text.toString().has4digits()) activity?.closeKeyboard()
        }
    }

    override fun setListeners() {
        binding.apply {
            btnCheck.setOnClickListener {
                if (pinView.text.toString().has4digits()) {
                    pinView.isEnabled = false
                    btnCheck.isEnabled = false

                    btnCheck.animate().alpha(0.0f).duration = 200
                    countDown(200) { btnCheck.visibility = View.GONE }
                    countDown(200) {
                        viewModel.checkSolution(solutionYear, pinView.text.toString())
                    }
                }
            }

            btnNext.setOnClickListener {
                position++
                if (position < music.size) {
                    activity?.shouldShowInterstitial(position)
                    setData()
                } else {
                    activity?.let { NoMoreDialog().show(it.supportFragmentManager, "") }
                }
            }
        }
    }

    override fun observe() {
        viewModel.event.observe(this) { event ->
            when (event) {
                is YearSingleEvent.CategoryData -> {
                    setToolbar(event.category.title, event.category.id.getFontFamily())
                    viewModel.getMusicData(event.category.id)
                }

                is YearSingleEvent.MusicData -> {
                    music = event.data
                    setData()
                    binding.loading.root.visibility = View.GONE
                }

                is YearSingleEvent.Solution -> solutionResult(event.result)
                is YearSingleEvent.SomethingWentWrong -> error()
            }
        }
    }

    private fun setToolbar(title: String, fontFamily: Int) {
        (activity as BedRockActivity).apply {
            setUpToolbar(title, fontFamily)
        }
    }

    private fun setData() {
        binding.apply {
            try {
                restartAnimations()
                btnCheck.animate().alpha(1.0f).duration = 500
                btnCheck.visibility = View.VISIBLE

                pinView.isEnabled = true
                pinView.text = null
                btnCheck.isEnabled = true
                val data = music[position]
                tvTitle.text = data.title
                tvArtist.text = data.artist
                youtubePlayerView.playVideo(data.videoUrl)
                setGroupImage(data.image)
                solutionYear = data.year
                solution.tvCorrectYear.text = data.year.toString()
            } catch (e: Exception) {
                error()
            }
        }
    }

    private fun setGroupImage(image: String?) {
        binding.apply {
            image?.let {
                Glide.with(mContext).load(it).into(ivMusicImage)
                llImage.visibility = View.VISIBLE
            } ?: run { llImage.visibility = View.GONE }
        }
    }

    private fun solutionResult(type: SolutionType) {
        binding.apply {
            when (type) {
                GOOD -> {
                    scoreGood++
                    score.goodScore.text = scoreGood.toString()
                    score.lottieGood.playAnimation()
                    solutionUI(goodPhrases, R.color.good_result)
                }

                ALMOST_GOOD -> {
                    scoreAlmost++
                    score.almostScore.text = scoreAlmost.toString()
                    score.lottieAlmost.playAnimation()
                    solutionUI(almostPhrases, R.color.almost_good_result)
                }

                BAD -> {
                    scoreBad++
                    score.badScore.text = scoreBad.toString()
                    score.lottieBad.playAnimation()
                    solutionUI(badPhrases, R.color.bad_result)
                }
            }
        }
    }

    private fun solutionUI(phrases: List<String>, color: Int) {
        val message = phrases[(phrases.indices).random()]
        binding.solution.apply {
            tvMessage.text = message
            tvMessage.setTextColor(getColor(mContext, color))
            root.visibility = View.VISIBLE
            llSolution.animate().alpha(1.0f).duration = 500
            countDown(750) {
                tvMessage.animate().alpha(1.0f).duration = 500
            }
        }
    }

    private fun restartAnimations() {
        binding.solution.apply {
            root.visibility = View.INVISIBLE
            llSolution.animate().alpha(0f).duration = 10
            tvMessage.animate().alpha(0f).duration = 10
        }
    }

    private fun error() = activity?.showErrorDialog()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onStop() {
        binding.youtubePlayerView.pauseVideo()
        super.onStop()
    }

    override fun onDestroy() {
        binding.youtubePlayerView.pauseVideo()
        super.onDestroy()
    }
}
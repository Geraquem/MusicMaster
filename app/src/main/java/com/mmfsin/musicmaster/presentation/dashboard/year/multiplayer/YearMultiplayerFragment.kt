package com.mmfsin.musicmaster.presentation.dashboard.year.multiplayer

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.viewModels
import com.mmfsin.musicmaster.R
import com.mmfsin.musicmaster.base.BaseFragment
import com.mmfsin.musicmaster.databinding.FragmentYearMultiplayerBinding
import com.mmfsin.musicmaster.domain.mappers.getFontFamily
import com.mmfsin.musicmaster.domain.models.Music
import com.mmfsin.musicmaster.presentation.MainActivity
import com.mmfsin.musicmaster.presentation.dashboard.changeLayersColor
import com.mmfsin.musicmaster.presentation.dashboard.dialog.NoMoreDialog
import com.mmfsin.musicmaster.presentation.dashboard.has4digits
import com.mmfsin.musicmaster.presentation.dashboard.pauseVideo
import com.mmfsin.musicmaster.presentation.dashboard.playVideo
import com.mmfsin.musicmaster.presentation.models.SolutionType
import com.mmfsin.musicmaster.presentation.models.SolutionType.*
import com.mmfsin.musicmaster.utils.CATEGORY_ID
import com.mmfsin.musicmaster.utils.closeKeyboard
import com.mmfsin.musicmaster.utils.showErrorDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class YearMultiplayerFragment :
    BaseFragment<FragmentYearMultiplayerBinding, YearMultiplayerViewModel>() {

    override val viewModel: YearMultiplayerViewModel by viewModels()
    private lateinit var mContext: Context

    private var categoryId: String? = null

    private lateinit var music: List<Music>
    private var position = 0
    private var solutionYear: Long = 0

    private var scoreTeamOne = 0
    private var scoreTeamTwo = 0

    override fun inflateView(
        inflater: LayoutInflater, container: ViewGroup?
    ) = FragmentYearMultiplayerBinding.inflate(inflater, container, false)

    override fun getBundleArgs() {
        arguments?.let {
            categoryId = it.getString(CATEGORY_ID)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).inDashboard = true
        categoryId?.let { viewModel.getCategory(it) } ?: run { error() }
    }

    override fun setUI() {
        binding.apply {
            loading.root.visibility = View.VISIBLE
            pinviewOne.addTextChangedListener(textWatcherOne)
            pinviewTwo.addTextChangedListener(textWatcherTwo)
            pinviewOne.isCursorVisible = false
            pinviewTwo.isCursorVisible = false
            solution.root.visibility = View.GONE
        }
    }

    private val textWatcherOne = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun afterTextChanged(p0: Editable?) {
            if (binding.pinviewOne.text.toString().has4digits()) activity?.closeKeyboard()
        }
    }

    private val textWatcherTwo = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun afterTextChanged(p0: Editable?) {
            if (binding.pinviewTwo.text.toString().has4digits()) activity?.closeKeyboard()
        }
    }

    override fun setListeners() {
        binding.apply {
            btnCheck.setOnClickListener {
                if (pinviewOne.text.toString().has4digits() &&
                    pinviewTwo.text.toString().has4digits()
                ) {
                    pinviewOne.isEnabled = false
                    pinviewTwo.isEnabled = false
                    btnCheck.isEnabled = false
                    viewModel.checkSolution(
                        solutionYear, pinviewOne.text.toString(), pinviewTwo.text.toString()
                    )
                }
            }

            btnNext.setOnClickListener {
                position++
                if (position < music.size) setData()
                else {
                    (activity as MainActivity).inDashboard = false
                    activity?.let { NoMoreDialog().show(it.supportFragmentManager, "") }
                }
            }
        }
    }

    override fun observe() {
        viewModel.event.observe(this) { event ->
            when (event) {
                is YearMultiplayerEvent.CategoryData -> {
                    setToolbar(event.category.title, event.category.id.getFontFamily())
                    viewModel.getMusicData(event.category.id)
                }
                is YearMultiplayerEvent.MusicData -> {
                    music = event.data.take(3)
                    setData()
                    binding.loading.root.visibility = View.GONE
                }
                is YearMultiplayerEvent.Solution -> solutionResult(event.result)
                is YearMultiplayerEvent.SomethingWentWrong -> error()
            }
        }
    }

    private fun setToolbar(title: String, fontFamily: Int) {
        (activity as MainActivity).apply {
            showBanner(visible = true)
            setMainToolbar(showLogo = false, title, fontFamily)
        }
    }

    private fun setData() {
        binding.apply {
            try {
                pinviewOne.isEnabled = true
                pinviewTwo.isEnabled = true
                pinviewOne.text = null
                pinviewTwo.text = null
                btnCheck.isEnabled = true
                solution.root.visibility = View.GONE
                val data = music[position]
                tvTitle.text = data.title
                tvArtist.text = data.artist
                youtubePlayerView.playVideo(data.videoUrl)
                solutionYear = data.year
                solution.tvCorrectYear.text = data.year.toString()
            } catch (e: Exception) {
                error()
            }
        }
    }

    private fun solutionResult(types: Pair<SolutionType, SolutionType>) {
        binding.apply {
            when (types.first) {
                GOOD -> {
                    scoreTeamOne += 2
                    solution.tvPointsTeamOne.text = getString(R.string.dashboard_two)
                    solution.tvPointsTeamOne.setTintColor(R.color.good_result)
                    solution.tvPointsOne.setTintColor(R.color.good_result)
                    score.lottieTeamOne.setColorFilter(getColor(mContext, R.color.good_result))
                    score.lottieTeamOne.playAnimation()
                }
                ALMOST_GOOD -> {
                    scoreTeamOne += 1
                    solution.tvPointsTeamOne.text = getString(R.string.dashboard_one)
                    solution.tvPointsTeamOne.setTintColor(R.color.almost_good_result)
                    solution.tvPointsOne.setTintColor(R.color.almost_good_result)
                    score.lottieTeamOne.setColorFilter(
                        getColor(
                            mContext,
                            R.color.almost_good_result
                        )
                    )
                    score.lottieTeamOne.playAnimation()
                }
                BAD -> {
                    solution.tvPointsTeamOne.text = getString(R.string.dashboard_zero)
                    solution.tvPointsTeamOne.setTintColor(R.color.bad_result)
                    solution.tvPointsOne.setTintColor(R.color.bad_result)

                    val yourColor = getColor(mContext, R.color.bad_result)

                    score.lottieTeamOne.changeLayersColor(R.color.bad_result)

                    score.lottieTeamOne.playAnimation()
                }
            }
            when (types.second) {
                GOOD -> {}
                ALMOST_GOOD -> {}
                BAD -> {}
            }
            score.tvScoreOne.text = scoreTeamOne.toString()
            score.tvScoreTwo.text = scoreTeamTwo.toString()
            solution.root.visibility = View.VISIBLE
        }
    }

    private fun TextView.setTintColor(color: Int) {
        this.setTextColor(getColor(mContext, color))
    }

    private fun solutionUI(phrases: List<String>, color: Int) {
        val message = phrases[(phrases.indices).random()]
//        binding.solution.apply {
//            tvMessage.text = message
//            tvMessage.setTextColor(getColor(mContext, color))
//            root.visibility = View.VISIBLE
//        }
    }

    private fun error() {
        (activity as MainActivity).inDashboard = false
        activity?.showErrorDialog()
    }

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
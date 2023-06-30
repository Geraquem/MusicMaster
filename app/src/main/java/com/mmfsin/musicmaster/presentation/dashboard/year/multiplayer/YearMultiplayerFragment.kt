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
import com.airbnb.lottie.LottieAnimationView
import com.mmfsin.musicmaster.R
import com.mmfsin.musicmaster.base.BaseFragment
import com.mmfsin.musicmaster.databinding.FragmentYearMultiplayerBinding
import com.mmfsin.musicmaster.domain.mappers.getFontFamily
import com.mmfsin.musicmaster.domain.models.Music
import com.mmfsin.musicmaster.presentation.MainActivity
import com.mmfsin.musicmaster.presentation.dashboard.dialog.NoMoreDialog
import com.mmfsin.musicmaster.presentation.dashboard.has4digits
import com.mmfsin.musicmaster.presentation.dashboard.pauseVideo
import com.mmfsin.musicmaster.presentation.dashboard.playVideo
import com.mmfsin.musicmaster.presentation.models.SolutionType
import com.mmfsin.musicmaster.presentation.models.SolutionType.*
import com.mmfsin.musicmaster.utils.CATEGORY_ID
import com.mmfsin.musicmaster.utils.changeLayersColor
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
        arguments?.let { categoryId = it.getString(CATEGORY_ID) }
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
                if (pinviewOne.text.toString().has4digits() && pinviewTwo.text.toString()
                        .has4digits()
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
                    music = event.data
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
                    setResultUI(
                        tvTeamPoints = solution.tvPointsTeamOne,
                        tvPoints = solution.tvPointsOne,
                        nPoints = R.string.dashboard_two,
                        lottie = score.lottieTeamOne,
                        color = R.color.good_result
                    )
                }
                ALMOST_GOOD -> {
                    scoreTeamOne += 1
                    setResultUI(
                        tvTeamPoints = solution.tvPointsTeamOne,
                        tvPoints = solution.tvPointsOne,
                        nPoints = R.string.dashboard_one,
                        lottie = score.lottieTeamOne,
                        color = R.color.almost_good_result,
                        onePoint = true
                    )
                }
                BAD -> {
                    setResultUI(
                        tvTeamPoints = solution.tvPointsTeamOne,
                        tvPoints = solution.tvPointsOne,
                        nPoints = R.string.dashboard_zero,
                        lottie = score.lottieTeamOne,
                        color = R.color.bad_result
                    )
                }
            }
            when (types.second) {
                GOOD -> {
                    scoreTeamTwo += 2
                    setResultUI(
                        tvTeamPoints = solution.tvPointsTeamTwo,
                        tvPoints = solution.tvPointsTwo,
                        nPoints = R.string.dashboard_two,
                        lottie = score.lottieTeamTwo,
                        color = R.color.good_result
                    )
                }
                ALMOST_GOOD -> {
                    scoreTeamTwo += 1
                    setResultUI(
                        tvTeamPoints = solution.tvPointsTeamTwo,
                        tvPoints = solution.tvPointsTwo,
                        nPoints = R.string.dashboard_one,
                        lottie = score.lottieTeamTwo,
                        color = R.color.almost_good_result,
                        onePoint = true
                    )
                }
                BAD -> {
                    setResultUI(
                        tvTeamPoints = solution.tvPointsTeamTwo,
                        tvPoints = solution.tvPointsTwo,
                        nPoints = R.string.dashboard_zero,
                        lottie = score.lottieTeamTwo,
                        color = R.color.bad_result
                    )
                }
            }
            score.tvScoreOne.text = scoreTeamOne.toString()
            score.tvScoreTwo.text = scoreTeamTwo.toString()
            solution.root.visibility = View.VISIBLE
        }
    }

    private fun setResultUI(
        tvTeamPoints: TextView,
        tvPoints: TextView,
        nPoints: Int,
        lottie: LottieAnimationView,
        color: Int,
        onePoint: Boolean = false
    ) {
        tvTeamPoints.text = getString(nPoints)
        tvTeamPoints.setTintColor(color)
        tvPoints.text = if (onePoint) getString(R.string.multi_team_point)
        else getString(R.string.multi_team_points)
        tvPoints.setTintColor(color)
        lottie.changeLayersColor(color)
        lottie.playAnimation()
    }

    private fun TextView.setTintColor(color: Int) {
        this.setTextColor(getColor(mContext, color))
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
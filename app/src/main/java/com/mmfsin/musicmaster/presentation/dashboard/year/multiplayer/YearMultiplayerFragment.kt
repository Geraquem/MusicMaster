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
import com.bumptech.glide.Glide
import com.mmfsin.musicmaster.R
import com.mmfsin.musicmaster.base.BaseFragment
import com.mmfsin.musicmaster.base.bedrock.BedRockActivity
import com.mmfsin.musicmaster.databinding.FragmentYearMultiplayerBinding
import com.mmfsin.musicmaster.domain.mappers.getFontFamily
import com.mmfsin.musicmaster.domain.models.Music
import com.mmfsin.musicmaster.presentation.dashboard.dialog.NoMoreDialog
import com.mmfsin.musicmaster.presentation.dashboard.has4digits
import com.mmfsin.musicmaster.presentation.dashboard.pauseVideo
import com.mmfsin.musicmaster.presentation.dashboard.playVideo
import com.mmfsin.musicmaster.presentation.models.SolutionType
import com.mmfsin.musicmaster.presentation.models.SolutionType.ALMOST_GOOD
import com.mmfsin.musicmaster.presentation.models.SolutionType.BAD
import com.mmfsin.musicmaster.presentation.models.SolutionType.GOOD
import com.mmfsin.musicmaster.utils.BEDROCK_STR_ARGS
import com.mmfsin.musicmaster.utils.closeKeyboard
import com.mmfsin.musicmaster.utils.countDown
import com.mmfsin.musicmaster.utils.shouldShowInterstitial
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
        categoryId = activity?.intent?.getStringExtra(BEDROCK_STR_ARGS)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        categoryId?.let { viewModel.getCategory(it) } ?: run { error() }
    }

    override fun setUI() {
        binding.apply {
            loading.root.visibility = View.VISIBLE
            pinviewOne.addTextChangedListener(textWatcherOne)
            pinviewTwo.addTextChangedListener(textWatcherTwo)
            pinviewOne.isCursorVisible = false
            pinviewTwo.isCursorVisible = false
            restartAnimations()
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

                    btnCheck.animate().alpha(0.0f).duration = 200
                    countDown(200) { btnCheck.visibility = View.GONE }
                    countDown(200) {
                        viewModel.checkSolution(
                            solutionYear,
                            pinviewOne.text.toString(),
                            pinviewTwo.text.toString()
                        )
                    }
                }
            }

            btnNext.setOnClickListener {
                position++
                if (position < music.size) setData()
                else activity?.let { NoMoreDialog().show(it.supportFragmentManager, "") }
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

                pinviewOne.isEnabled = true
                pinviewTwo.isEnabled = true
                pinviewOne.text = null
                pinviewTwo.text = null
                btnCheck.isEnabled = true

                val data = music[position]
                tvTitle.text = data.title
                tvArtist.text = data.artist
                youtubePlayerView.playVideo(data.videoUrl)
                setGroupImage(data.image)
                solutionYear = data.year
                solution.tvCorrectYear.text = data.year.toString()

                val showed = activity?.shouldShowInterstitial(position)
                if (showed != null && showed) youtubePlayerView.pauseVideo()

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

    private fun solutionResult(types: Pair<SolutionType, SolutionType>) {
        binding.apply {
            when (types.first) {
                GOOD -> {
                    scoreTeamOne += 2
                    setResultUI(
                        tvTeamPoints = solution.tvPointsTeamOne,
                        tvPoints = solution.tvPointsOne,
                        nPoints = R.string.dashboard_two,
                        color = R.color.good_result
                    )
                }

                ALMOST_GOOD -> {
                    scoreTeamOne += 1
                    setResultUI(
                        tvTeamPoints = solution.tvPointsTeamOne,
                        tvPoints = solution.tvPointsOne,
                        nPoints = R.string.dashboard_one,
                        color = R.color.almost_good_result,
                        onePoint = true
                    )
                }

                BAD -> {
                    setResultUI(
                        tvTeamPoints = solution.tvPointsTeamOne,
                        tvPoints = solution.tvPointsOne,
                        nPoints = R.string.dashboard_zero,
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
                        color = R.color.good_result
                    )
                }

                ALMOST_GOOD -> {
                    scoreTeamTwo += 1
                    setResultUI(
                        tvTeamPoints = solution.tvPointsTeamTwo,
                        tvPoints = solution.tvPointsTwo,
                        nPoints = R.string.dashboard_one,
                        color = R.color.almost_good_result,
                        onePoint = true
                    )
                }

                BAD -> {
                    setResultUI(
                        tvTeamPoints = solution.tvPointsTeamTwo,
                        tvPoints = solution.tvPointsTwo,
                        nPoints = R.string.dashboard_zero,
                        color = R.color.bad_result
                    )
                }
            }
            score.tvScoreOne.text = scoreTeamOne.toString()
            score.tvScoreTwo.text = scoreTeamTwo.toString()

            solution.apply {
                root.visibility = View.VISIBLE
                llSolution.animate().alpha(1.0f).duration = 500
                countDown(500) {
                    llTeamOne.animate().alpha(1.0f).duration = 500
                    llTeamTwo.animate().alpha(1.0f).duration = 500
                }
            }
        }
    }

    private fun setResultUI(
        tvTeamPoints: TextView,
        tvPoints: TextView,
        nPoints: Int,
        color: Int,
        onePoint: Boolean = false
    ) {
        tvTeamPoints.text = getString(nPoints)
        tvTeamPoints.setTintColor(color)
        tvPoints.text = if (onePoint) getString(R.string.multi_team_point)
        else getString(R.string.multi_team_points)
        tvPoints.setTintColor(color)
    }

    private fun TextView.setTintColor(color: Int) {
        this.setTextColor(getColor(mContext, color))
    }

    private fun restartAnimations() {
        binding.solution.apply {
            root.visibility = View.INVISIBLE
            llSolution.animate().alpha(0f).duration = 10
            llTeamOne.animate().alpha(0f).duration = 10
            llTeamTwo.animate().alpha(0f).duration = 10
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
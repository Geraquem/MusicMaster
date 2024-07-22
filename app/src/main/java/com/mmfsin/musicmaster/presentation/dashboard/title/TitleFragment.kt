package com.mmfsin.musicmaster.presentation.dashboard.title

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.viewModels
import com.mmfsin.musicmaster.R
import com.mmfsin.musicmaster.base.BaseFragment
import com.mmfsin.musicmaster.base.bedrock.BedRockActivity
import com.mmfsin.musicmaster.databinding.FragmentTitleBinding
import com.mmfsin.musicmaster.domain.mappers.getFontFamily
import com.mmfsin.musicmaster.domain.models.Music
import com.mmfsin.musicmaster.presentation.dashboard.dialog.NoMoreDialog
import com.mmfsin.musicmaster.presentation.dashboard.pauseSeekbar
import com.mmfsin.musicmaster.presentation.dashboard.playSeekbar
import com.mmfsin.musicmaster.presentation.dashboard.playYoutubeSeekBar
import com.mmfsin.musicmaster.presentation.models.SolutionType
import com.mmfsin.musicmaster.presentation.models.SolutionType.ALMOST_GOOD
import com.mmfsin.musicmaster.presentation.models.SolutionType.BAD
import com.mmfsin.musicmaster.presentation.models.SolutionType.GOOD
import com.mmfsin.musicmaster.utils.BEDROCK_STR_ARGS
import com.mmfsin.musicmaster.utils.closeKeyboard
import com.mmfsin.musicmaster.utils.countDown
import com.mmfsin.musicmaster.utils.shouldShowInterstitial
import com.mmfsin.musicmaster.utils.showErrorDialog
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TitleFragment : BaseFragment<FragmentTitleBinding, TitleViewModel>() {

    override val viewModel: TitleViewModel by viewModels()
    private lateinit var mContext: Context

    private var categoryId: String? = null

    private var youtubePlayerView: YouTubePlayerView? = null
    private var isPlaying = true

    private lateinit var goodPhrases: List<String>
    private lateinit var almostPhrases: List<String>
    private lateinit var badPhrases: List<String>

    private lateinit var music: List<Music>
    private var position = 0
    private var solutionTitle: String = ""

    private var scoreGood = 0
    private var scoreAlmost = 0
    private var scoreBad = 0

    override fun inflateView(
        inflater: LayoutInflater, container: ViewGroup?
    ) = FragmentTitleBinding.inflate(inflater, container, false)

    override fun getBundleArgs() {
        categoryId = activity?.intent?.getStringExtra(BEDROCK_STR_ARGS)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let { youtubePlayerView = YouTubePlayerView(it) }
        categoryId?.let { viewModel.getCategory(it) } ?: run { error() }
    }

    override fun setUI() {
        binding.apply {
            loading.root.visibility = View.VISIBLE
            btnPlay.setImageResource(R.drawable.ic_pause)
            goodPhrases = resources.getStringArray(R.array.good_phrases).toList().shuffled()
            almostPhrases = resources.getStringArray(R.array.almost_phrases).toList().shuffled()
            badPhrases = resources.getStringArray(R.array.bad_phrases).toList().shuffled()
            etTitle.text = null
            etTitle.isEnabled = true
            restartAnimations()
        }
    }

    override fun setListeners() {
        binding.apply {
            btnPlay.setOnClickListener {
                if (isPlaying) {
                    youtubePlayerView?.pauseSeekbar()
                    btnPlay.setImageResource(R.drawable.ic_play)
                } else {
                    youtubePlayerView?.playSeekbar()
                    btnPlay.setImageResource(R.drawable.ic_pause)
                }
                isPlaying = !isPlaying
            }

            btnCheck.setOnClickListener {
                val answer = etTitle.text.toString()
                if (answer.isNotEmpty()) {
                    activity?.closeKeyboard()
                    etTitle.isEnabled = false
                    btnCheck.isEnabled = false

                    btnCheck.animate().alpha(0.0f).duration = 200
                    countDown(200) { btnCheck.visibility = View.GONE }
                    countDown(200) {
                        viewModel.checkSolution(solutionTitle, answer)
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
                is TitleEvent.CategoryData -> {
                    setToolbar(event.category.title, event.category.id.getFontFamily())
                    viewModel.getMusicData(event.category.id)
                }

                is TitleEvent.MusicData -> {
                    music = event.data
                    setData()
                    binding.loading.root.visibility = View.GONE
                }

                is TitleEvent.Solution -> solutionResult(event.result)
                is TitleEvent.SomethingWentWrong -> error()
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
                btnPlay.setImageResource(R.drawable.ic_pause)
                etTitle.text = null
                etTitle.isEnabled = true
                btnCheck.isEnabled = true
                val data = music[position]
                youtubePlayerView?.playYoutubeSeekBar(data.videoUrl, binding.youtubePlayerSeekbar)
                solutionTitle = data.title
                solution.tvTitle.text = data.title
                solution.tvArtist.text = data.artist

                val showed = activity?.shouldShowInterstitial(position)
                if (showed != null && showed) {
                    youtubePlayerView?.pauseSeekbar()
                    btnPlay.setImageResource(R.drawable.ic_play)
                    isPlaying = false
                }

            } catch (e: Exception) {
                error()
            }
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
            solution.apply {
                root.visibility = View.VISIBLE
                llSolution.animate().alpha(1.0f).duration = 500
                countDown(750) {
                    tvMessage.animate().alpha(1.0f).duration = 500
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
        isPlaying = false
        binding.btnPlay.setImageResource(R.drawable.ic_play)
        youtubePlayerView?.pauseSeekbar()
        super.onStop()
    }

    override fun onDestroy() {
        isPlaying = false
        binding.btnPlay.setImageResource(R.drawable.ic_play)
        youtubePlayerView?.pauseSeekbar()
        super.onDestroy()
    }
}
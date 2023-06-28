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
import com.mmfsin.musicmaster.R
import com.mmfsin.musicmaster.base.BaseFragment
import com.mmfsin.musicmaster.databinding.FragmentYearSingleBinding
import com.mmfsin.musicmaster.domain.models.Music
import com.mmfsin.musicmaster.presentation.MainActivity
import com.mmfsin.musicmaster.presentation.dashboard.has4digits
import com.mmfsin.musicmaster.presentation.dashboard.playVideo
import com.mmfsin.musicmaster.presentation.models.SolutionType
import com.mmfsin.musicmaster.presentation.models.SolutionType.*
import com.mmfsin.musicmaster.utils.CATEGORY_ID
import com.mmfsin.musicmaster.utils.closeKeyboard
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
        arguments?.let {
            categoryId = it.getString(CATEGORY_ID)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        categoryId?.let { viewModel.getCategory(it) } ?: run { error() }
    }

    override fun setUI() {
        binding.apply {
            loading.root.visibility = View.VISIBLE
            goodPhrases = resources.getStringArray(R.array.good_phrases).toList()
            almostPhrases = resources.getStringArray(R.array.almost_phrases).toList()
            badPhrases = resources.getStringArray(R.array.bad_phrases).toList()
            pinView.addTextChangedListener(textWatcher)
            pinView.isCursorVisible = false
            solution.root.visibility = View.GONE
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
                    viewModel.checkSolution(solutionYear, pinView.text.toString())
                }
            }

            btnNext.setOnClickListener {
                position++
                if (position < music.size) setData()
                else error() // dead end
            }
        }
    }

    override fun observe() {
        viewModel.event.observe(this) { event ->
            when (event) {
                is YearSingleEvent.CategoryData -> {
                    setToolbar(event.category.title)
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

    private fun setToolbar(title: String) {
        (activity as MainActivity).apply {
            showBanner(visible = true)
            setMainToolbar(showLogo = false, title)
        }
    }

    private fun setData() {
        binding.apply {
            try {
                pinView.isEnabled = true
                pinView.text = null
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
            solution.root.visibility = View.VISIBLE
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

    private fun error() = activity?.showErrorDialog()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }
}
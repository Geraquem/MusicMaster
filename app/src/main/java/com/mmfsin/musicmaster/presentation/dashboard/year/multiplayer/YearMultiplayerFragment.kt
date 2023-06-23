package com.mmfsin.musicmaster.presentation.dashboard.year.multiplayer

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.mmfsin.musicmaster.R
import com.mmfsin.musicmaster.common.BaseFragment
import com.mmfsin.musicmaster.databinding.FragmentYearMultiplayerBinding
import com.mmfsin.musicmaster.domain.models.MusicDTO
import com.mmfsin.musicmaster.domain.types.ResultType
import com.mmfsin.musicmaster.domain.types.ResultType.*
import com.mmfsin.musicmaster.presentation.dashboard.IDashboardListener
import com.mmfsin.musicmaster.presentation.dashboard.year.YearPresenter
import com.mmfsin.musicmaster.presentation.dashboard.year.YearView

class YearMultiplayerFragment(val category: String, val listener: IDashboardListener) :
    BaseFragment<FragmentYearMultiplayerBinding>(), YearView {

    private val presenter by lazy { YearPresenter(this) }

    private lateinit var goodPhrases: List<String>
    private lateinit var almostPhrases: List<String>
    private lateinit var badPhrases: List<String>

    private lateinit var data: List<MusicDTO>
    private var correctYear: Long = 0
    private var position = 0

    private var scoreGroup1 = 0
    private var scoreGroup2 = 0

    override fun inflateView(
        inflater: LayoutInflater, container: ViewGroup?
    ) = FragmentYearMultiplayerBinding.inflate(inflater, container, false)

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
            solution.root.visibility = View.GONE
            pinviewOne.addTextChangedListener(textWatcher1)
            pinviewTwo.addTextChangedListener(textWatcher2)
            pinviewOne.isCursorVisible = false
            pinviewTwo.isCursorVisible = false
        }
    }

    override fun setListeners() {
        binding.apply {
            btnCheck.setOnClickListener {
                val text1 = pinviewOne.text.toString()
                val text2 = pinviewTwo.text.toString()
                if (presenter.year4digits(text1) && presenter.year4digits(text2)) {
                    pinviewOne.isEnabled = false
                    pinviewTwo.isEnabled = false
                    btnCheck.isEnabled = false
                    presenter.multiSolution(Pair(text1, text2), correctYear)
                }
            }

            btnNext.setOnClickListener {
                position += 1
                if (position < data.size) {
                    initialAttributes()
                    setMusicData(data[position])
                } else listener.noMoreData()
            }
        }
    }

    private fun initialAttributes() {
        binding.apply {
            listener.closeKeyboard()
            pinviewOne.isEnabled = true
            pinviewTwo.isEnabled = true
            pinviewOne.text = null
            pinviewTwo.text = null
            btnCheck.isEnabled = true
            solution.root.visibility = View.GONE
        }
    }

    override fun musicData(list: List<MusicDTO>) {
        data = list
        setMusicData(data[position])
    }

    private fun setMusicData(data: MusicDTO) {
        binding.apply {
            tvTitle.text = data.title
            tvArtist.text = data.artist
            presenter.playVideo(youtubePlayerView, data.videoUrl)
            correctYear = data.year

            loading.root.visibility = View.GONE
        }
    }

    override fun solution(type: ResultType) {}

    override fun multiSolution(solutions: Pair<ResultType, ResultType>) {
        binding.apply {
            when (solutions.first) {
                GOOD -> {
                    scoreGroup1 += 2
                    solution.pointsGroupOne.text = getString(R.string.g_one_solution, "2")
                    solution.pointsGroupOne.setTextColor(getColor(R.color.goodPhrase))
                    score.lottieGoodOne.playAnimation()
                }
                ALMOST_GOOD -> {
                    scoreGroup1 += 1
                    solution.pointsGroupOne.text = getString(R.string.g_one_solution, "1")
                    solution.pointsGroupOne.setTextColor(getColor(R.color.almostPhrase))
                    score.lottieGoodOne.playAnimation()
                }
                else -> {
                    solution.pointsGroupOne.text = getString(R.string.g_one_solution, "0")
                    solution.pointsGroupOne.setTextColor(getColor(R.color.badPhrase))
                }
            }

            when (solutions.second) {
                GOOD -> {
                    scoreGroup2 += 2
                    solution.pointsGroupTwo.text = getString(R.string.g_one_solution, "2")
                    solution.pointsGroupTwo.setTextColor(getColor(R.color.goodPhrase))
                    score.lottieGoodTwo.playAnimation()
                }
                ALMOST_GOOD -> {
                    scoreGroup2 += 1
                    solution.pointsGroupTwo.text = getString(R.string.g_one_solution, "1")
                    solution.pointsGroupTwo.setTextColor(getColor(R.color.almostPhrase))
                    score.lottieGoodTwo.playAnimation()
                }
                else -> {
                    solution.pointsGroupTwo.text = getString(R.string.g_one_solution, "0")
                    solution.pointsGroupTwo.setTextColor(getColor(R.color.badPhrase))
                }
            }

            score.scoreOne.text = scoreGroup1.toString()
            score.scoreTwo.text = scoreGroup2.toString()

            solution.tvCorrectYear.text = correctYear.toString()

            solution.root.visibility = View.VISIBLE
        }
    }

    private fun getColor(color: Int) =
        ContextCompat.getColor(this@YearMultiplayerFragment.requireContext(), color)

    private val textWatcher1 = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun afterTextChanged(p0: Editable?) {
            if (presenter.year4digits(binding.pinviewOne.text.toString())) {
                listener.closeKeyboard()
            }
        }
    }

    private val textWatcher2 = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun afterTextChanged(p0: Editable?) {
            if (presenter.year4digits(binding.pinviewTwo.text.toString())) {
                listener.closeKeyboard()
            }
        }
    }

    override fun somethingWentWrong() {

    }
}
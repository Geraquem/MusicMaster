package com.mmfsin.musicmaster.presentation.dashboard.year.single

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.mmfsin.musicmaster.base.BaseFragment
import com.mmfsin.musicmaster.databinding.FragmentYearSingleBinding
import com.mmfsin.musicmaster.domain.models.Music
import com.mmfsin.musicmaster.presentation.dashboard.playVideo
import com.mmfsin.musicmaster.utils.CATEGORY_ID
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
        categoryId?.let {
//            getCategory from realm ???
            viewModel.getMusicData(it)
        }
    }

    override fun setUI() {
        binding.apply {
            solution.root.visibility = View.GONE
        }
    }

    override fun setListeners() {
        binding.apply {
            btnCheck.setOnClickListener {
                solution.root.visibility = View.VISIBLE
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
                is YearSingleEvent.MusicData -> {
                    music = event.data
                    setData()
                }
                is YearSingleEvent.SomethingWentWrong -> error()
            }
        }
    }

    private fun setData() {
        binding.apply {
            try {
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

    private fun error() = activity?.showErrorDialog()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }
}


//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        goodPhrases = resources.getStringArray(R.array.good_phrases).toList()
//        almostPhrases = resources.getStringArray(R.array.almost_phrases).toList()
//        badPhrases = resources.getStringArray(R.array.bad_phrases).toList()
//        presenter.getMusicData(category)
//    }

//    override fun setUI() {
//        binding.apply {
//            listener.changeToolbar(category)
//            solution.root.visibility = View.GONE
//            pinView.addTextChangedListener(textWatcher)
//            pinView.isCursorVisible = false
//        }
//    }
//
//    override fun setListeners() {
//        binding.apply {
//            btnCheck.setOnClickListener {
//                if (presenter.year4digits(pinView.text.toString())) {
//                    pinView.isEnabled = false
//                    btnCheck.isEnabled = false
//                    presenter.solution(pinView.text.toString(), correctYear)
//                }
//            }
//
//            btnNext.setOnClickListener {
//                position += 1
//                if (position < data.size) {
//                    initialAttributes()
//                    setMusicData(data[position])
//                } else listener.noMoreData()
//            }
//        }
//        }
//
//    private fun initialAttributes() {
//        binding.apply {
//            listener.closeKeyboard()
//            pinView.isEnabled = true
//            pinView.text = null
//            btnCheck.isEnabled = true
//            solution.root.visibility = View.GONE
//        }
//    }
//
//    override fun musicData(list: List<MusicDTO>) {
//        data = list
//        setMusicData(data[position])
//    }
//
//    private fun setMusicData(data: MusicDTO) {
//        binding.apply {
//            tvTitle.text = data.title
//            tvArtist.text = data.artist
////            presenter.playVideo(youtubePlayerView, data.videoUrl)
//            correctYear = data.year
//
//            loading.root.visibility = View.GONE
//        }
//    }
//
//    override fun solution(type: ResultType) {
//        binding.apply {
//            when (type) {
//                GOOD -> {
//                    solutionUI(randomPhrase(goodPhrases), getColor(R.color.goodPhrase))
//                    scoreGood++
//                    score.goodScore.text = scoreGood.toString()
//                    score.lottieGood.playAnimation()
//                }
//                ALMOST_GOOD -> {
//                    solutionUI(randomPhrase(almostPhrases), getColor(R.color.almostPhrase))
//                    scoreAlmost++
//                    score.almostScore.text = scoreAlmost.toString()
//                    score.lottieAlmost.playAnimation()
//                }
//                BAD -> {
//                    solutionUI(randomPhrase(badPhrases), getColor(R.color.badPhrase))
//                    scoreBad++
//                    score.badScore.text = scoreBad.toString()
//                    score.lottieBad.playAnimation()
//                }
//            }
//        }
//    }
//
//    override fun multiSolution(solutions: Pair<ResultType, ResultType>) {}
//
//    private fun randomPhrase(phrases: List<String>): String {
//        return phrases[(phrases.indices).random()]
//    }
//
//    private fun getColor(color: Int) =
//        ContextCompat.getColor(this@YearSingleFragment.requireContext(), color)
//
//    private fun solutionUI(message: String, color: Int) {
//        binding.solution.apply {
//            tvCorrectYear.text = correctYear.toString()
//            tvMessage.text = message
//            tvMessage.setTextColor(color)
//            root.visibility = View.VISIBLE
//        }
//    }
//
//    private val textWatcher = object : TextWatcher {
//        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
//        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
//        override fun afterTextChanged(p0: Editable?) {
////            if (presenter.year4digits(binding.pinView.text.toString())) {
////                listener.closeKeyboard()
////            }
//        }
//    }
//
//    override fun somethingWentWrong() {
//
//    }

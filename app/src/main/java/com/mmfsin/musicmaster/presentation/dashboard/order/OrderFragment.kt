package com.mmfsin.musicmaster.presentation.dashboard.order

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColorStateList
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.mmfsin.musicmaster.R
import com.mmfsin.musicmaster.base.BaseFragment
import com.mmfsin.musicmaster.base.bedrock.BedRockActivity
import com.mmfsin.musicmaster.databinding.FragmentOrderBinding
import com.mmfsin.musicmaster.domain.mappers.getFontFamily
import com.mmfsin.musicmaster.domain.models.Music
import com.mmfsin.musicmaster.domain.models.OrderSelected
import com.mmfsin.musicmaster.domain.models.OrderSelected.NEWER
import com.mmfsin.musicmaster.domain.models.OrderSelected.OLDER
import com.mmfsin.musicmaster.domain.models.OrderSelected.SAME_YEAR
import com.mmfsin.musicmaster.domain.models.OrderSolution
import com.mmfsin.musicmaster.presentation.dashboard.dialog.NoMoreDialog
import com.mmfsin.musicmaster.presentation.dashboard.order.dialogs.LoserDialog
import com.mmfsin.musicmaster.presentation.dashboard.pauseVideo
import com.mmfsin.musicmaster.presentation.dashboard.playVideo
import com.mmfsin.musicmaster.utils.BEDROCK_STR_ARGS
import com.mmfsin.musicmaster.utils.countDown
import com.mmfsin.musicmaster.utils.shouldShowInterstitial
import com.mmfsin.musicmaster.utils.showErrorDialog
import com.mmfsin.musicmaster.utils.showFragmentDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderFragment : BaseFragment<FragmentOrderBinding, OrderViewModel>() {

    override val viewModel: OrderViewModel by viewModels()
    private lateinit var mContext: Context

    private var categoryId: String? = null

    private lateinit var music: List<Music>
    private var position = 0

    private var yearToGuess: Long = 2000
    private var actualYear: Long = 0

    private var streak = 0

    override fun inflateView(
        inflater: LayoutInflater, container: ViewGroup?
    ) = FragmentOrderBinding.inflate(inflater, container, false)

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
            tvYearToGuess.text = "$yearToGuess"
            tvStreak.text = "$streak"
        }
    }


    override fun setListeners() {
        binding.apply {
            llOlder.setOnClickListener { guess(OLDER) }
            llNewer.setOnClickListener { guess(NEWER) }
            llSameYear.setOnClickListener { guess(SAME_YEAR) }
        }
    }

    private fun guess(selected: OrderSelected) =
        viewModel.response(selected, yearToGuess, actualYear)

    override fun observe() {
        viewModel.event.observe(this) { event ->
            when (event) {
                is OrderEvent.CategoryData -> {
                    setToolbar(event.category.title, event.category.id.getFontFamily())
                    viewModel.getMusicData(event.category.id)
                }

                is OrderEvent.MusicData -> {
                    music = event.data
                    setData()
                    binding.loading.root.visibility = View.GONE
                }

                is OrderEvent.Solution -> checkSolution(event.solution)
                is OrderEvent.SomethingWentWrong -> error()
            }
        }
    }

    private fun setToolbar(title: String, fontFamily: Int) =
        (activity as BedRockActivity).apply { setUpToolbar(title, fontFamily) }

    private fun setData() {
        binding.apply {
            try {
                val data = music[position]
                tvTitle.text = data.title
                tvArtist.text = data.artist
                youtubePlayerView.playVideo(data.videoUrl)
                setGroupImage(data.image)
                actualYear = data.year

                val showed = activity?.shouldShowInterstitial(position)
                if (showed != null && showed) {
                    countDown(1500) { youtubePlayerView.pauseVideo() }
                }

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

    private fun checkSolution(solution: Pair<OrderSelected, OrderSolution>) {
        binding.apply {
            val goodColor = getColorStateList(mContext, R.color.good_result)
            val almostColor = getColorStateList(mContext, R.color.almost_good_result)
            val badColor = getColorStateList(mContext, R.color.bad_result)

            when (solution.second) {
                OrderSolution.GOOD -> {
                    when (solution.first) {
                        OLDER -> llOlder.backgroundTintList = goodColor
                        NEWER -> llNewer.backgroundTintList = goodColor
                        SAME_YEAR -> llSameYear.backgroundTintList = goodColor
                    }
                    streak++
                    tvStreak.text = "$streak"
                    nextSong()
                }

                OrderSolution.BAD -> {
                    when (solution.first) {
                        OLDER -> llOlder.backgroundTintList = badColor
                        NEWER -> llNewer.backgroundTintList = badColor
                        SAME_YEAR -> llSameYear.backgroundTintList = badColor
                    }
                    looseGame()
                }

                OrderSolution.SAME_YEAR -> {
                    llSameYear.backgroundTintList = almostColor
                    nextSong()
                }
            }
        }
    }

    private fun nextSong() {
        binding.apply {
            countDown(1000) {
                llOlder.backgroundTintList = null
                llNewer.backgroundTintList = null
                llSameYear.backgroundTintList = null
                yearToGuess = actualYear
                tvYearToGuess.text = "$yearToGuess"

                position++
                if (position < music.size) setData()
                else activity?.let { NoMoreDialog().show(it.supportFragmentManager, "") }
            }
        }
    }

    private fun looseGame() {
        binding.apply {
            countDown(1000) {
                activity?.let {
                    it.showFragmentDialog(
                        LoserDialog(
                            restart = {
                                music = emptyList()
                                position = 0
                                yearToGuess = 2000
                                streak = 0

                                llOlder.backgroundTintList = null
                                llNewer.backgroundTintList = null
                                llSameYear.backgroundTintList = null

                                tvYearToGuess.text = "$yearToGuess"
                                tvStreak.text = "$streak"

                                categoryId?.let { id -> viewModel.getCategory(id) }
                                    ?: run { error() }
                            },
                            exit = { it.finish() })
                    )
                }
            }
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
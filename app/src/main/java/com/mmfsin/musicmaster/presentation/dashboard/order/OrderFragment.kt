package com.mmfsin.musicmaster.presentation.dashboard.order

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.mmfsin.musicmaster.base.BaseFragment
import com.mmfsin.musicmaster.base.bedrock.BedRockActivity
import com.mmfsin.musicmaster.databinding.FragmentOrderBinding
import com.mmfsin.musicmaster.domain.mappers.getFontFamily
import com.mmfsin.musicmaster.domain.models.Music
import com.mmfsin.musicmaster.domain.models.Order.NEWER
import com.mmfsin.musicmaster.domain.models.Order.OLDER
import com.mmfsin.musicmaster.domain.models.OrderResponse
import com.mmfsin.musicmaster.presentation.dashboard.dialog.NoMoreDialog
import com.mmfsin.musicmaster.presentation.dashboard.pauseVideo
import com.mmfsin.musicmaster.presentation.dashboard.playVideo
import com.mmfsin.musicmaster.utils.BEDROCK_STR_ARGS
import com.mmfsin.musicmaster.utils.countDown
import com.mmfsin.musicmaster.utils.shouldShowInterstitial
import com.mmfsin.musicmaster.utils.showErrorDialog
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
            llOlder.setOnClickListener { viewModel.response(yearToGuess, actualYear, OLDER) }
            llNewer.setOnClickListener { viewModel.response(yearToGuess, actualYear, NEWER) }
        }
    }

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

    private fun checkSolution(solution: OrderResponse) {
        if (solution.sameYear == true) {


        } else {
            if (solution.isCorrect == true) {
                streak++
                binding.tvStreak.text = "$streak"
            } else {
                streak = 0
            }
        }

        position++
        if (position < music.size) setData()
        else activity?.let { NoMoreDialog().show(it.supportFragmentManager, "") }
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
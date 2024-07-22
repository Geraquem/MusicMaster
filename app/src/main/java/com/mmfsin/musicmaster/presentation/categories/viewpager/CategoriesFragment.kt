package com.mmfsin.musicmaster.presentation.categories.viewpager

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.mmfsin.musicmaster.R
import com.mmfsin.musicmaster.base.BaseFragment
import com.mmfsin.musicmaster.databinding.FragmentCategoriesBinding
import com.mmfsin.musicmaster.presentation.MainActivity
import com.mmfsin.musicmaster.presentation.categories.viewpager.adapter.ViewPagerAdapter
import com.mmfsin.musicmaster.presentation.categories.viewpager.bottomsheet.BSheetSelector
import com.mmfsin.musicmaster.presentation.categories.viewpager.bottomsheet.interfaces.IBSheetSelectorListener
import com.mmfsin.musicmaster.presentation.models.GameMode
import com.mmfsin.musicmaster.presentation.models.GameMode.GUESS_TITLE
import com.mmfsin.musicmaster.presentation.models.GameMode.GUESS_YEAR_MULTIPLAYER
import com.mmfsin.musicmaster.presentation.models.GameMode.GUESS_YEAR_SINGLE
import com.mmfsin.musicmaster.utils.animateY
import com.mmfsin.musicmaster.utils.countDown
import com.mmfsin.musicmaster.utils.showErrorDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoriesFragment : BaseFragment<FragmentCategoriesBinding, CategoriesViewModel>(),
    IBSheetSelectorListener {

    override val viewModel: CategoriesViewModel by viewModels()
    private lateinit var mContext: Context

    override fun inflateView(
        inflater: LayoutInflater, container: ViewGroup?
    ) = FragmentCategoriesBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getCategories()
    }

    override fun setUI() {
        binding.apply {
            loading.root.visibility = View.VISIBLE
            if ((activity as MainActivity).firstAccessVP) {
                tabs.root.visibility = View.INVISIBLE
                tabs.root.animateY(-1000f, 10)
            }
        }
    }

    override fun observe() {
        viewModel.event.observe(this) { event ->
            when (event) {
                is CategoriesEvent.Categories -> setUpViewPager()
                is CategoriesEvent.SomethingWentWrong -> error()
            }
        }
    }

    private fun setUpViewPager() {
        binding.apply {
            activity?.let {
                viewPager.adapter = ViewPagerAdapter(it, this@CategoriesFragment)
                TabLayoutMediator(tabs.tabLayout, viewPager) { tab, position ->
                    when (position) {
                        0 -> tab.setText(R.string.category_vp_spanish)
                        1 -> tab.setText(R.string.category_vp_english)
                    }
                }.attach()
                endFlow()
            }
        }
    }

    private fun endFlow() {
        binding.apply {
            loading.root.visibility = View.GONE
            if ((activity as MainActivity).firstAccessVP) {
                (activity as MainActivity).firstAccessVP = false
                countDown(500) {
                    tabs.root.visibility = View.VISIBLE
                    tabs.root.animateY(0f, 1000)
                }
            }
        }
    }

    override fun onItemClick(categoryId: String) {
        val bottomSheet = BSheetSelector { mode -> navigateToDashboard(categoryId, mode) }
        activity?.let { bottomSheet.show(it.supportFragmentManager, "") }
    }

    private fun navigateToDashboard(categoryId: String, mode: GameMode) {
        val navGraph = when (mode) {
            GUESS_YEAR_SINGLE -> R.navigation.nav_graph_year_single
            GUESS_YEAR_MULTIPLAYER -> R.navigation.nav_graph_year_multiple
            GUESS_TITLE -> R.navigation.nav_graph_title
        }
        navigateTo(navGraph, categoryId)
    }

    private fun navigateTo(navGraph: Int, categoryId: String) =
        (activity as MainActivity).openBedRockActivity(navGraph, categoryId)

    private fun error() = activity?.showErrorDialog()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }
}
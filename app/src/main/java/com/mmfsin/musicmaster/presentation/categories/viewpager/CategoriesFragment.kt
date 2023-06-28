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
import com.mmfsin.musicmaster.utils.showErrorDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoriesFragment : BaseFragment<FragmentCategoriesBinding, CategoriesViewModel>() {

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
            setToolbar()
        }
    }

    private fun setToolbar() {
        (activity as MainActivity).apply {
            showBanner(visible = false)
            setMainToolbar(showLogo = true, getString(R.string.app_name), R.font.text)
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
                viewPager.adapter = ViewPagerAdapter(it)
                TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                    when (position) {
                        0 -> tab.setText(R.string.category_vp_spanish)
                        1 -> tab.setText(R.string.category_vp_english)
                    }
                }.attach()
                loading.root.visibility = View.GONE
            }
        }
    }

    private fun error() = activity?.showErrorDialog()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }
}
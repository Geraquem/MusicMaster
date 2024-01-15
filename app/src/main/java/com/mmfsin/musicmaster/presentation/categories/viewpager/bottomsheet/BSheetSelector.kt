package com.mmfsin.musicmaster.presentation.categories.viewpager.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.tabs.TabLayoutMediator
import com.mmfsin.musicmaster.R
import com.mmfsin.musicmaster.databinding.BottomSheetSelectorBinding
import com.mmfsin.musicmaster.presentation.categories.viewpager.adapter.ViewPagerAdapter
import com.mmfsin.musicmaster.presentation.models.GameMode
import com.mmfsin.musicmaster.presentation.models.GameMode.GUESS_TITLE
import com.mmfsin.musicmaster.presentation.models.GameMode.GUESS_YEAR_MULTIPLAYER
import com.mmfsin.musicmaster.presentation.models.GameMode.GUESS_YEAR_SINGLE

class BSheetSelector(val action: (mode: GameMode) -> Unit) : BottomSheetDialogFragment() {

    private var _binding: BottomSheetSelectorBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetSelectorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUI()
        setListeners()
    }

    private fun setUI() {
        binding.apply {
            activity?.let {
                viewPager.adapter = ViewPagerAdapter(it)
                TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                    when (position) {
                        0 -> tab.setText(R.string.selector_guess_year_mode_single)
                        1 -> tab.setText(R.string.selector_guess_year_mode_multiplayer)
                    }
                }.attach()
            }
        }
    }

    private fun setListeners() {
        binding.apply {
            btnYearSingle.setOnClickListener {
                when (tabLayout.selectedTabPosition) {
                    0 -> modeClicked(GUESS_YEAR_SINGLE)
                    else -> modeClicked(GUESS_YEAR_MULTIPLAYER)
                }
            }
            btnTitle.setOnClickListener { modeClicked(GUESS_TITLE) }
        }
    }

    private fun modeClicked(mode: GameMode) {
        action(mode)
        dismiss()
    }
}
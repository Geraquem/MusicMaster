package com.mmfsin.musicmaster.presentation.categories.language.dialog

import android.app.Dialog
import android.view.LayoutInflater
import com.google.android.material.tabs.TabLayoutMediator
import com.mmfsin.musicmaster.R
import com.mmfsin.musicmaster.base.BaseDialog
import com.mmfsin.musicmaster.databinding.DialogSelectorBinding
import com.mmfsin.musicmaster.presentation.categories.viewpager.adapter.ViewPagerAdapter
import com.mmfsin.musicmaster.presentation.models.GameMode
import com.mmfsin.musicmaster.presentation.models.GameMode.*

class SelectorDialog(private val action: (gameMode: GameMode) -> Unit) :
    BaseDialog<DialogSelectorBinding>() {

    override fun inflateView(inflater: LayoutInflater) = DialogSelectorBinding.inflate(inflater)

    override fun setCustomViewDialog(dialog: Dialog) = bottomViewDialog(dialog)

    override fun setUI() {
        isCancelable = true
        binding.apply {
            activity?.let {
                modeTabs.viewPager.adapter = ViewPagerAdapter(it)
                TabLayoutMediator(modeTabs.tabLayout, modeTabs.viewPager) { tab, position ->
                    when (position) {
                        0 -> tab.setText(R.string.selector_guess_year_mode_single)
                        1 -> tab.setText(R.string.selector_guess_year_mode_multiplayer)
                    }
                }.attach()
            }
        }
    }

    override fun setListeners() {
        binding.apply {
            btnYearSingle.setOnClickListener {
                when (modeTabs.tabLayout.selectedTabPosition) {
                    0 -> action(GUESS_YEAR_SINGLE)
                    else -> action(GUESS_YEAR_MULTIPLAYER)
                }
            }
            btnTitle.setOnClickListener { action(GUESS_TITLE) }
        }
    }
}
package com.mmfsin.musicmaster.presentation.categories.language.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.view.GestureDetector
import android.view.LayoutInflater
import android.widget.Toast
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
        setSwipeListener()
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

    override fun setListeners() {
        binding.apply {
            btnYearSingle.setOnClickListener {
                when (tabLayout.selectedTabPosition) {
                    0 -> action(GUESS_YEAR_SINGLE)
                    else -> action(GUESS_YEAR_MULTIPLAYER)
                }
            }
            btnTitle.setOnClickListener { action(GUESS_TITLE) }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setSwipeListener() {
        binding.apply {
            val swipeListener = SwipeListener { direction ->
                when (direction) {
                    SwipeListener.Direction.BOTTOM -> dismiss()
                    else -> {}
                }
            }

            val gestureDetector = GestureDetector(requireActivity(), swipeListener)
            llMain.setOnTouchListener { _, event -> gestureDetector.onTouchEvent(event) }
        }
    }
}
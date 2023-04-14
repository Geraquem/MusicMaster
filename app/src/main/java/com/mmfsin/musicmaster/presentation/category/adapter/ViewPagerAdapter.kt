package com.mmfsin.musicmaster.presentation.category.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mmfsin.musicmaster.R
import com.mmfsin.musicmaster.domain.types.Languages
import com.mmfsin.musicmaster.domain.types.Languages.ENGLISH
import com.mmfsin.musicmaster.domain.types.Languages.SPANISH
import com.mmfsin.musicmaster.presentation.category.CategoryFragment
import com.mmfsin.musicmaster.presentation.selector.IFragmentSelector

class ViewPagerAdapter(
    private val listener: IFragmentSelector,
    private val fragmentActivity: FragmentActivity
) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> CategoryFragment(listener, SPANISH)
            else -> CategoryFragment(listener, ENGLISH)
        }
    }
}
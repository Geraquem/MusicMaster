package com.mmfsin.musicmaster.category.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mmfsin.musicmaster.R
import com.mmfsin.musicmaster.category.CategoryFragment
import com.mmfsin.musicmaster.selector.IFragmentSelector

class ViewPagerAdapter(
    private val listener: IFragmentSelector,
    private val fragmentActivity: FragmentActivity
) :
    FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> CategoryFragment(listener, fragmentActivity.getString(R.string.english))
            1 -> CategoryFragment(listener, fragmentActivity.getString(R.string.spanish))
            else -> CategoryFragment(listener, fragmentActivity.getString(R.string.english))
        }
    }
}
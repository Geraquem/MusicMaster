package com.mmfsin.musicmaster.category.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mmfsin.musicmaster.R
import com.mmfsin.musicmaster.category.CategoryFragment

class ViewPagerAdapter(private val fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> CategoryFragment(fragmentActivity.getString(R.string.english))
            1 -> CategoryFragment(fragmentActivity.getString(R.string.spanish))
            else -> CategoryFragment(fragmentActivity.getString(R.string.english))
        }
    }
}
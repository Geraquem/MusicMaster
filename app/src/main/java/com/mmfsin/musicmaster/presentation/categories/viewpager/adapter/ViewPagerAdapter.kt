package com.mmfsin.musicmaster.presentation.categories.viewpager.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mmfsin.musicmaster.domain.models.Language
import com.mmfsin.musicmaster.domain.models.Language.ENGLISH
import com.mmfsin.musicmaster.domain.models.Language.SPANISH
import com.mmfsin.musicmaster.utils.LANGUAGE
import com.mmfsin.musicmaster.presentation.categories.language.CategoriesByLanguageFragment

class ViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> getFragment(SPANISH)
            else -> getFragment(ENGLISH)
        }
    }

    private fun getFragment(language: Language): Fragment {
        val fragment = CategoriesByLanguageFragment()
        val bundle = Bundle().apply {
            putString(LANGUAGE, language.name.lowercase())
        }
        fragment.arguments = bundle
        return fragment
    }
}
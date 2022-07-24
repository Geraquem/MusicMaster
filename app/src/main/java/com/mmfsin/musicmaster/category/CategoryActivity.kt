package com.mmfsin.musicmaster.category

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.mmfsin.musicmaster.R
import com.mmfsin.musicmaster.category.adapter.ViewPagerAdapter
import com.mmfsin.musicmaster.databinding.ActivityCategoryBinding
import com.mmfsin.musicmaster.guesser.multiplayer.MultiYearGuesser
import com.mmfsin.musicmaster.guesser.single.TitleGuesserActivity
import com.mmfsin.musicmaster.guesser.single.YearGuesserActivity
import com.mmfsin.musicmaster.selector.FragmentSelector
import com.mmfsin.musicmaster.selector.IFragmentSelector

class CategoryActivity : AppCompatActivity(), IFragmentSelector {

    private lateinit var binding: ActivityCategoryBinding

    private val pagerAdapter by lazy { ViewPagerAdapter(this, this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        Thread.sleep(1500)
        setTheme(R.style.Theme_MusicMaster)
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewPager.adapter = pagerAdapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.setText(R.string.english)
                1 -> tab.setText(R.string.spanish)
            }
        }.attach()
    }

    override fun openFragmentSelector(category: String) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, FragmentSelector(this, category))
            .addToBackStack(null)
            .commit()
    }

    override fun closeFragmentSelector() = supportFragmentManager.popBackStack()

    override fun openActivityDashboard(isYear: Boolean, category: String) {
        startActivity(generateSingleModeIntent(isYear, category))
    }

    override fun openActivityDashMultiplayer(isYear: Boolean, category: String) {
        startActivity(generateMultiplayerModeIntent(isYear, category))
    }

    private fun generateSingleModeIntent(isYear: Boolean, category: String): Intent {
        val intent = if (isYear) {
            Intent(this, YearGuesserActivity()::class.java)
        } else {
            Intent(this, TitleGuesserActivity()::class.java)
        }
        return intent.apply {
            putExtra("category", category)
        }
    }

    private fun generateMultiplayerModeIntent(isYear: Boolean, category: String): Intent {
        val intent = if (isYear) {
            Intent(this, MultiYearGuesser()::class.java)
        } else {
            Intent(this, TitleGuesserActivity()::class.java)
        }
        return intent.apply {
            putExtra("category", category)
        }
    }
}
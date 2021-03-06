package com.mmfsin.musicmaster.category

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.mmfsin.musicmaster.R
import com.mmfsin.musicmaster.category.adapter.ViewPagerAdapter
import com.mmfsin.musicmaster.fragmentselector.FragmentSelector
import com.mmfsin.musicmaster.guesser.title.TitleGuesserActivity
import com.mmfsin.musicmaster.guesser.year.YearGuesserActivity
import kotlinx.android.synthetic.main.activity_category.*

class CategoryActivity : AppCompatActivity(), CategoryFragment.ICategoryFragment,
    FragmentSelector.IFragmentSelector {

    private val pagerAdapter by lazy { ViewPagerAdapter(this, this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        Thread.sleep(1500)
        setTheme(R.style.Theme_MusicMaster)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        viewPager.adapter = pagerAdapter
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
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
        startActivity(generateIntent(isYear, category))
    }

    private fun generateIntent(isYear: Boolean, category: String): Intent {
        val intent = if (isYear) {
            Intent(this, YearGuesserActivity()::class.java)
        } else {
            Intent(this, TitleGuesserActivity()::class.java)
        }
        return intent.apply {
            putExtra("category", category)
        }
    }
}
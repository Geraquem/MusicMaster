package com.mmfsin.musicmaster.category

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.mmfsin.musicmaster.R
import com.mmfsin.musicmaster.category.adapter.ViewPagerAdapter
import com.mmfsin.musicmaster.fragmentselector.FragmentSelector
import com.mmfsin.musicmaster.title.TitleGuesserActivity
import com.mmfsin.musicmaster.year.YearGuesserActivity
import kotlinx.android.synthetic.main.activity_category.*

class CategoryActivity : AppCompatActivity(), CategoryFragment.ICategoryFragment,
    FragmentSelector.IFragmentSelector {

    private val pagerAdapter by lazy { ViewPagerAdapter(this, this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        viewPager.adapter = pagerAdapter
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.setText(R.string.english)
                }
                1 -> {
                    tab.setText(R.string.spanish)
                }
            }
        }.attach()
    }

    override fun openFragmentSelector(id: String) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, FragmentSelector(this, id))
            .addToBackStack(null)
            .commit()
    }

    override fun closeFragmentSelector() {
        supportFragmentManager.popBackStack()
    }

    private fun generateIntent(isYear: Boolean, id: String): Intent {
        val intent = if (isYear) {
            Intent(this, YearGuesserActivity::class.java)
        } else {
            Intent(this, TitleGuesserActivity::class.java)
        }
        return intent.apply {
            putExtra("id", id)
        }
    }

    override fun openActivityDashboard(isYear: Boolean, id: String) {
        startActivity(generateIntent(isYear, id))
    }
}
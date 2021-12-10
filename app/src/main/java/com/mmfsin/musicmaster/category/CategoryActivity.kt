package com.mmfsin.musicmaster.category

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.mmfsin.musicmaster.R
import com.mmfsin.musicmaster.category.adapter.ViewPagerAdapter
import com.mmfsin.musicmaster.fragmentselector.FragmentSelector
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

    override fun openActivityDashboard(isYear: Boolean, id: String) {
        Toast.makeText(this, id, Toast.LENGTH_SHORT).show()
    }
}
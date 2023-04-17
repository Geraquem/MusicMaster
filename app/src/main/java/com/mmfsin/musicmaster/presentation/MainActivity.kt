package com.mmfsin.musicmaster.presentation

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.mmfsin.musicmaster.R
import com.mmfsin.musicmaster.databinding.ActivityMainBinding
import com.mmfsin.musicmaster.domain.types.GameMode
import com.mmfsin.musicmaster.domain.utils.CATEGORY
import com.mmfsin.musicmaster.domain.utils.GAME_MODE
import com.mmfsin.musicmaster.presentation.category.adapter.ViewPagerAdapter
import com.mmfsin.musicmaster.presentation.dashboard.DashboardActivity
import com.mmfsin.musicmaster.presentation.selector.FragmentSelector
import com.mmfsin.musicmaster.presentation.selector.IFragmentSelector
import io.realm.Realm

class MainActivity : AppCompatActivity(), IFragmentSelector {

    private lateinit var binding: ActivityMainBinding

    private val pagerAdapter by lazy { ViewPagerAdapter(this, this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        Thread.sleep(500)
        setTheme(R.style.Theme_MusicMaster)

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Realm.init(this)

        binding.viewPager.adapter = pagerAdapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.setText(R.string.spanish)
                1 -> tab.setText(R.string.english)
            }
        }.attach()
    }

    override fun openFragmentSelector(category: String) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, FragmentSelector(this, category))
            .addToBackStack(null)
            .commit()
    }

    override fun closeFragmentSelector() = supportFragmentManager.popBackStack()

    override fun openActivityDashboard(mode: GameMode, category: String) {
        startActivity(Intent(this, DashboardActivity()::class.java).apply {
            putExtra(GAME_MODE, mode.name)
            putExtra(CATEGORY, category)
        })
        closeFragmentSelector()
    }
}
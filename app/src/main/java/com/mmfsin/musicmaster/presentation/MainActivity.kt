package com.mmfsin.musicmaster.presentation

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.mmfsin.musicmaster.R
import com.mmfsin.musicmaster.databinding.ActivityMainBinding
import com.mmfsin.musicmaster.presentation.category.adapter.ViewPagerAdapter
import com.mmfsin.musicmaster.presentation.guesser.multiplayer.MultiYearGuesser
import com.mmfsin.musicmaster.presentation.guesser.single.TitleGuesserActivity
import com.mmfsin.musicmaster.presentation.guesser.single.YearGuesserActivity
import com.mmfsin.musicmaster.presentation.selector.FragmentSelector
import com.mmfsin.musicmaster.presentation.selector.IFragmentSelector

class MainActivity : AppCompatActivity(), IFragmentSelector {

    private lateinit var binding: ActivityMainBinding

    private val pagerAdapter by lazy { ViewPagerAdapter(this, this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        Thread.sleep(500)
        setTheme(R.style.Theme_MusicMaster)

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Realm.init

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
            .setCustomAnimations(R.anim.slide_up, 0, 0, R.anim.slide_down)
            .replace(R.id.fragment_container, FragmentSelector(this, category))
            .addToBackStack(null)
            .commit()
    }

    override fun closeFragmentSelector() = supportFragmentManager.popBackStack()

    override fun openActivityDashboard(isYear: Boolean, category: String) =
        startActivity(generateSingleModeIntent(isYear, category))

    override fun openActivityDashMultiplayer(isYear: Boolean, category: String) =
        startActivity(generateMultiplayerModeIntent(isYear, category))

    private fun generateSingleModeIntent(isYear: Boolean, category: String): Intent {
        val intent = if (isYear) {
            Intent(this, YearGuesserActivity()::class.java)
        } else {
            Intent(this, TitleGuesserActivity()::class.java)
        }
        return intent.apply { putExtra("category", category) }
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
package com.mmfsin.musicmaster.presentation

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat.getFont
import androidx.core.view.isVisible
import com.google.android.gms.ads.AdRequest
import com.mmfsin.musicmaster.R
import com.mmfsin.musicmaster.databinding.ActivityMainBinding
import com.mmfsin.musicmaster.presentation.dashboard.dialog.ExitDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    var inDashboard = false

    override fun onCreate(savedInstanceState: Bundle?) {
        Thread.sleep(500)
        setTheme(R.style.Theme_MusicMaster)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setAds()
    }

    private fun setAds() {
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)
        showBanner(visible = false)
//        loadInterstitial(AdRequest.Builder().build())
    }

    fun showBanner(visible: Boolean) {
        binding.adView.visibility = if (visible) View.VISIBLE else View.GONE
    }

    fun setMainToolbar(showLogo: Boolean, title: String, fontFamily: Int) {
        binding.toolbar.apply {
            ivLogo.isVisible = showLogo
            ivBack.isVisible = !showLogo
            tvTitle.text = title
            tvTitle.typeface = getFont(this@MainActivity, fontFamily)

            ivBack.setOnClickListener {
                inDashboard = false
                onBackPressed()
            }
        }
    }

    override fun onBackPressed() {
        val dialog = ExitDialog() { super.onBackPressed() }
        if (inDashboard) dialog.show(supportFragmentManager, "")
        else super.onBackPressed()
    }
}
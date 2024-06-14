package com.mmfsin.musicmaster.presentation

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat.getFont
import androidx.core.view.WindowInsetsControllerCompat
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.mmfsin.musicmaster.R
import com.mmfsin.musicmaster.databinding.ActivityMainBinding
import com.mmfsin.musicmaster.presentation.dashboard.dialog.ExitDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var mInterstitialAd: InterstitialAd? = null

    var firstAccessVP = true
    var firstAccessRV = true
    var inDashboard = false

    override fun onCreate(savedInstanceState: Bundle?) {
        Thread.sleep(200)
        setTheme(R.style.Theme_MusicMaster)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        changeStatusBar()
        setAds()
    }

    private fun changeStatusBar() {
        window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        val controller = WindowInsetsControllerCompat(window, window.decorView)
        controller.isAppearanceLightStatusBars = true
    }

    private fun setAds() {
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)
        showBanner(visible = false)
        loadInterstitial(AdRequest.Builder().build())
    }

    fun showBanner(visible: Boolean) {
        binding.adView.visibility = if (visible) View.VISIBLE else View.GONE
    }

    private fun loadInterstitial(adRequest: AdRequest) {
        InterstitialAd.load(this,
            getString(R.string.interstitial),
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    mInterstitialAd = null
                    loadInterstitial(AdRequest.Builder().build())
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    mInterstitialAd = interstitialAd
                }
            })
    }

    fun showInterstitial(position: Int) {
        if (position == -1) {
//        if (position % 2 == 0) {
            mInterstitialAd?.let { ad ->
                ad.show(this)
                loadInterstitial(AdRequest.Builder().build())
            }
        }
    }

    fun hideMainToolbar() {
        binding.toolbar.root.visibility = View.GONE
    }

    fun setDashboardToolbar(title: String, fontFamily: Int) {
        binding.toolbar.apply {
            tvTitle.text = title
            tvTitle.typeface = getFont(this@MainActivity, fontFamily)

            ivBack.setOnClickListener {
                inDashboard = false
                onBackPressed()
            }
            root.visibility = View.VISIBLE
        }
    }

    override fun onBackPressed() {
        val dialog = ExitDialog() { super.onBackPressed() }
        if (inDashboard) dialog.show(supportFragmentManager, "")
        else super.onBackPressed()
    }
}
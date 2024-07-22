package com.mmfsin.musicmaster.base.bedrock

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.fragment.NavHostFragment
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.mmfsin.musicmaster.R
import com.mmfsin.musicmaster.databinding.ActivityBedrockBinding
import com.mmfsin.musicmaster.presentation.dashboard.dialog.ExitDialog
import com.mmfsin.musicmaster.utils.ROOT_ACTIVITY_NAV_GRAPH
import com.mmfsin.musicmaster.utils.showErrorDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BedRockActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBedrockBinding

    private var mInterstitialAd: InterstitialAd? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBedrockBinding.inflate(layoutInflater)
        setContentView(binding.root)

        changeStatusBar()
        setUpNavGraph()
        setAds()
    }

    private fun changeStatusBar() {
        window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        val controller = WindowInsetsControllerCompat(window, window.decorView)
        controller.isAppearanceLightStatusBars = true
    }

    private fun setUpNavGraph() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.br_fragment_container) as NavHostFragment
        val navController = navHostFragment.navController
        val navGraph = intent.getIntExtra(ROOT_ACTIVITY_NAV_GRAPH, -1)
        navController.apply { if (navGraph != -1) setGraph(navGraph) else error() }
    }

    fun setUpToolbar(title: String, fontFamily: Int) {
        binding.toolbar.apply {
            tvTitle.text = title
            tvTitle.typeface = ResourcesCompat.getFont(this@BedRockActivity, fontFamily)

            ivBack.setOnClickListener { onBackPressed() }
        }
    }

    private fun setAds() {
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)
        loadInterstitial(AdRequest.Builder().build())
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

    fun showInterstitial(position: Int): Boolean {
        return if (position != 0 && position % 2 == 0) {
            mInterstitialAd?.let { ad ->
                ad.show(this)
                loadInterstitial(AdRequest.Builder().build())
            }
            true
        } else false
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        val dialog = ExitDialog { super.onBackPressed() }
        dialog.show(supportFragmentManager, "")
    }

    private fun error() = showErrorDialog()
}
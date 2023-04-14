package com.mmfsin.musicmaster.presentation.dashboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.mmfsin.musicmaster.R
import com.mmfsin.musicmaster.domain.mappers.toGameMode
import com.mmfsin.musicmaster.domain.utils.CATEGORY
import com.mmfsin.musicmaster.domain.utils.GAME_MODE

class DashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)


//        MobileAds.initialize(this) {}
//        loadInterstitial(AdRequest.Builder().build())

        getArgs()
    }

    private fun getArgs(){
        val category = intent.getStringExtra(CATEGORY)
        val mode = intent.getStringExtra(GAME_MODE)?.toGameMode()
    }
}
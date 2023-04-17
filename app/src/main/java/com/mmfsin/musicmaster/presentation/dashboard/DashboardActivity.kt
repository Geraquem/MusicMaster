package com.mmfsin.musicmaster.presentation.dashboard

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.SweetAlertDialog
import com.mmfsin.musicmaster.R
import com.mmfsin.musicmaster.databinding.ActivityDashboardBinding
import com.mmfsin.musicmaster.domain.mappers.toGameMode
import com.mmfsin.musicmaster.domain.types.GameMode
import com.mmfsin.musicmaster.domain.utils.CATEGORY
import com.mmfsin.musicmaster.domain.utils.GAME_MODE
import com.mmfsin.musicmaster.presentation.utils.sww

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        MobileAds.initialize(this) {}
//        loadInterstitial(AdRequest.Builder().build())

        getArgs()
    }

    private fun getArgs() {
        val category = intent.getStringExtra(CATEGORY)
        val mode = intent.getStringExtra(GAME_MODE)?.toGameMode()

        mode?.let { selectGameMode(mode) } ?: run { sww(this@DashboardActivity).show() }
    }

    private fun selectGameMode(mode: GameMode) {}

}
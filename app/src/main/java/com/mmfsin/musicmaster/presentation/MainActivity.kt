package com.mmfsin.musicmaster.presentation

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.mmfsin.musicmaster.R
import com.mmfsin.musicmaster.base.bedrock.BedRockActivity
import com.mmfsin.musicmaster.databinding.ActivityMainBinding
import com.mmfsin.musicmaster.utils.BEDROCK_STR_ARGS
import com.mmfsin.musicmaster.utils.ROOT_ACTIVITY_NAV_GRAPH
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    var firstAccessVP = true
    var firstAccessRV = true

    override fun onCreate(savedInstanceState: Bundle?) {
        Thread.sleep(200)
        setTheme(R.style.Theme_MusicMaster)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        changeStatusBar()
    }

    private fun changeStatusBar() {
        window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        val controller = WindowInsetsControllerCompat(window, window.decorView)
        controller.isAppearanceLightStatusBars = true
    }

    fun openBedRockActivity(navGraph: Int, strArgs: String) {
        val intent = Intent(this, BedRockActivity::class.java)
        intent.putExtra(BEDROCK_STR_ARGS, strArgs)
        intent.putExtra(ROOT_ACTIVITY_NAV_GRAPH, navGraph)
        startActivity(intent)
    }
}
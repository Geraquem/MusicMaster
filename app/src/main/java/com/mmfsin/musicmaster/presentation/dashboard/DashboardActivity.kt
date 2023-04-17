package com.mmfsin.musicmaster.presentation.dashboard

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.mmfsin.musicmaster.R
import com.mmfsin.musicmaster.databinding.ActivityDashboardBinding
import com.mmfsin.musicmaster.domain.mappers.getFontFamily
import com.mmfsin.musicmaster.domain.mappers.getToolbarTitle
import com.mmfsin.musicmaster.domain.mappers.toGameMode
import com.mmfsin.musicmaster.domain.types.GameMode
import com.mmfsin.musicmaster.domain.types.GameMode.*
import com.mmfsin.musicmaster.domain.utils.CATEGORY
import com.mmfsin.musicmaster.domain.utils.GAME_MODE
import com.mmfsin.musicmaster.presentation.dashboard.year.single.YearSingleFragment
import com.mmfsin.musicmaster.presentation.utils.sww

class DashboardActivity : AppCompatActivity(), IDashboardListener {

    private lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getArgs()
    }

    private fun getArgs() {
        val mode = intent.getStringExtra(GAME_MODE)?.toGameMode()
        val category = intent.getStringExtra(CATEGORY) ?: ""

        mode?.let { selectGameMode(mode, category) } ?: run { sww(this@DashboardActivity) }
    }

    private fun selectGameMode(mode: GameMode, category: String) {
        when (mode) {
            GUESS_YEAR_SINGLE -> open(YearSingleFragment(category, this))
            GUESS_YEAR_MULTIPLAYER -> {}
            GUESS_TITLE -> {}
        }
    }

    private fun open(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.dashboard_container, fragment)
            .addToBackStack(null).commit()
        binding.loading.root.visibility = View.GONE
    }

    override fun closeKeyboard() {
        this.currentFocus?.let { view ->
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    override fun changeToolbar(category: String) {
        binding.toolbar.apply {
            icon.setImageResource(R.drawable.ic_back)
            tvTitle.text = getString(category.getToolbarTitle())
            tvTitle.typeface =
                ResourcesCompat.getFont(this@DashboardActivity, category.getFontFamily())
        }
    }
}
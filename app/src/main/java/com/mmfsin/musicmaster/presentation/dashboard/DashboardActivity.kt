package com.mmfsin.musicmaster.presentation.dashboard

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import cn.pedant.SweetAlert.SweetAlertDialog
import cn.pedant.SweetAlert.SweetAlertDialog.WARNING_TYPE
import com.mmfsin.musicmaster.R
import com.mmfsin.musicmaster.databinding.ActivityDashboardBinding
import com.mmfsin.musicmaster.domain.mappers.getFontFamily
import com.mmfsin.musicmaster.domain.mappers.getToolbarTitle
import com.mmfsin.musicmaster.domain.mappers.toGameMode
import com.mmfsin.musicmaster.presentation.dashboard.year.multiplayer.YearMultiplayerFragment
import com.mmfsin.musicmaster.presentation.dashboard.year.single.YearSingleFragment
import com.mmfsin.musicmaster.presentation.models.GameMode
import com.mmfsin.musicmaster.presentation.models.GameMode.*
import com.mmfsin.musicmaster.presentation.utils.sww
import com.mmfsin.musicmaster.utils.CATEGORY
import com.mmfsin.musicmaster.utils.GAME_MODE

class DashboardActivity : AppCompatActivity(), IDashboardListener {

    private lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.icon.setOnClickListener { exit() }
        getArgs()
    }

    private fun getArgs() {
        val mode = intent.getStringExtra(GAME_MODE)?.toGameMode()
        val category = intent.getStringExtra(CATEGORY) ?: ""

        mode?.let { selectGameMode(mode, category) } ?: run { sww(this@DashboardActivity) }
    }

    private fun selectGameMode(mode: GameMode, category: String) {
        when (mode) {
            GUESS_YEAR_SINGLE -> open(YearSingleFragment())
            GUESS_YEAR_MULTIPLAYER -> open(YearMultiplayerFragment(category, this))
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

    override fun noMoreData() {
        SweetAlertDialog(this, WARNING_TYPE).setTitleText(getString(R.string.no_more_data))
            .setConfirmText(getString(R.string.ok)).setConfirmClickListener { finish() }
            .show()
    }

    override fun exit() = finish()

    override fun onBackPressed() {
        SweetAlertDialog(this, WARNING_TYPE).setTitleText(getString(R.string.exit))
            .setConfirmText(getString(R.string.yes)).setConfirmClickListener { finish() }
            .setCancelButton(getString(R.string.no)) { sDialog -> sDialog.dismissWithAnimation() }
            .show()
    }
}
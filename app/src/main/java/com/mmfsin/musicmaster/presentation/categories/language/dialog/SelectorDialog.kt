package com.mmfsin.musicmaster.presentation.categories.language.dialog

import android.app.Dialog
import android.view.LayoutInflater
import com.mmfsin.musicmaster.base.BaseDialog
import com.mmfsin.musicmaster.databinding.DialogSelectorBinding
import com.mmfsin.musicmaster.presentation.models.GameMode
import com.mmfsin.musicmaster.presentation.models.GameMode.*

class SelectorDialog(private val action: (gameMode: GameMode) -> Unit) :
    BaseDialog<DialogSelectorBinding>() {

    override fun inflateView(inflater: LayoutInflater) = DialogSelectorBinding.inflate(inflater)

    override fun setCustomViewDialog(dialog: Dialog) = bottomViewDialog(dialog)

    override fun setUI() {
        isCancelable = true
    }

    override fun setListeners() {
        binding.apply {
            btnYearSingle.setOnClickListener { action(GUESS_YEAR_SINGLE) }
            btnYearMultiplayer.setOnClickListener { action(GUESS_YEAR_MULTIPLAYER) }
            btnTitle.setOnClickListener { action(GUESS_TITLE) }
        }
    }
}
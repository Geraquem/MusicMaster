package com.mmfsin.musicmaster.presentation.dashboard.order.dialogs

import android.app.Dialog
import android.view.LayoutInflater
import com.mmfsin.musicmaster.R
import com.mmfsin.musicmaster.base.BaseDialog
import com.mmfsin.musicmaster.databinding.DialogOrderLoserBinding
import com.mmfsin.musicmaster.domain.models.OrderSelected
import com.mmfsin.musicmaster.domain.models.OrderSelected.NEWER
import com.mmfsin.musicmaster.domain.models.OrderSelected.OLDER
import com.mmfsin.musicmaster.domain.models.OrderSelected.SAME_YEAR
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoserDialog(
    val selected: OrderSelected,
    val songTitle: String,
    val yearToGuess: Long,
    val restart: () -> Unit,
    val exit: () -> Unit
) : BaseDialog<DialogOrderLoserBinding>() {

    override fun inflateView(inflater: LayoutInflater) = DialogOrderLoserBinding.inflate(inflater)

    override fun setCustomViewDialog(dialog: Dialog) = centerViewDialog(dialog)

    override fun setUI() {
        isCancelable = false
        when (selected) {
            OLDER -> setText(R.string.order_loser_title_said_older)
            NEWER -> setText(R.string.order_loser_title_said_newer)
            SAME_YEAR -> setText(R.string.order_loser_title_said_same_year)
        }
    }

    private fun setText(text: Int) {
        binding.tvText.text = getString(text, songTitle, yearToGuess.toString())
    }

    override fun setListeners() {
        binding.apply {
            btnRestart.setOnClickListener {
                restart()
                dismiss()
            }
            btnExit.setOnClickListener {
                exit()
                dismiss()
            }
        }
    }
}
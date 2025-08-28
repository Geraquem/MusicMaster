package com.mmfsin.musicmaster.presentation.dashboard.order.dialogs

import android.app.Dialog
import android.view.LayoutInflater
import com.mmfsin.musicmaster.base.BaseDialog
import com.mmfsin.musicmaster.databinding.DialogOrderLoserBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoserDialog(
    val restart: () -> Unit,
    val exit: () -> Unit
) : BaseDialog<DialogOrderLoserBinding>() {

    override fun inflateView(inflater: LayoutInflater) = DialogOrderLoserBinding.inflate(inflater)

    override fun setCustomViewDialog(dialog: Dialog) = centerViewDialog(dialog)

    override fun setUI() {
        isCancelable = false
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
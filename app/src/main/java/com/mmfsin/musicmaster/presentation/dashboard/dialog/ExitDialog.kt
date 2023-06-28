package com.mmfsin.musicmaster.presentation.dashboard.dialog

import android.app.Dialog
import android.view.LayoutInflater
import com.mmfsin.musicmaster.base.BaseDialog
import com.mmfsin.musicmaster.databinding.DialogExitBinding

class ExitDialog(val action: () -> Unit) : BaseDialog<DialogExitBinding>() {

    override fun inflateView(inflater: LayoutInflater) = DialogExitBinding.inflate(inflater)

    override fun setCustomViewDialog(dialog: Dialog) = centerViewDialog(dialog)

    override fun setUI() {
        isCancelable = true
    }

    override fun setListeners() {
        binding.apply {
            btnStay.setOnClickListener { dismiss() }
            btnExit.setOnClickListener {
                action()
                dismiss()
            }
        }
    }
}
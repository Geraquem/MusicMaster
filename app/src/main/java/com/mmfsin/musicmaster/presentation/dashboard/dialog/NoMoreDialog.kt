package com.mmfsin.musicmaster.presentation.dashboard.dialog

import android.app.Dialog
import android.view.LayoutInflater
import com.mmfsin.musicmaster.base.BaseDialog
import com.mmfsin.musicmaster.databinding.DialogNoMoreBinding

class NoMoreDialog : BaseDialog<DialogNoMoreBinding>() {

    override fun inflateView(inflater: LayoutInflater) = DialogNoMoreBinding.inflate(inflater)

    override fun setCustomViewDialog(dialog: Dialog) = centerViewDialog(dialog)

    override fun setListeners() {
        binding.apply {
            btnAccept.setOnClickListener {
                activity?.onBackPressed()
                dismiss()
            }
        }
    }
}
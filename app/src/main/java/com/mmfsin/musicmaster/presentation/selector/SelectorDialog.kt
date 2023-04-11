package com.mmfsin.musicmaster.presentation.selector

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.mmfsin.musicmaster.databinding.DialogSelectorBinding

class SelectorDialog: DialogFragment() {

    private var _binding: DialogSelectorBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val activity = this.activity ?: return super.onCreateDialog(savedInstanceState)
        val dialog = Dialog(activity)
        _binding = DialogSelectorBinding.inflate(activity.layoutInflater)
        dialog.setContentView(binding.root)
        setCustomViewDialog(dialog)
        dialog.show()
        return dialog
    }

    private fun setCustomViewDialog(dialog: Dialog) {
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val width = resources.displayMetrics.widthPixels * 0.9
        dialog.window?.setLayout(width.toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
    }
}
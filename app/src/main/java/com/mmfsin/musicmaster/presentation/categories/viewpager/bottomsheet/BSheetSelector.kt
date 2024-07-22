package com.mmfsin.musicmaster.presentation.categories.viewpager.bottomsheet

import android.app.Dialog
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mmfsin.musicmaster.R
import com.mmfsin.musicmaster.databinding.BottomSheetSelectorBinding
import com.mmfsin.musicmaster.presentation.models.GameMode
import com.mmfsin.musicmaster.presentation.models.GameMode.GUESS_TITLE
import com.mmfsin.musicmaster.presentation.models.GameMode.GUESS_YEAR_MULTIPLAYER
import com.mmfsin.musicmaster.presentation.models.GameMode.GUESS_YEAR_SINGLE

class BSheetSelector(val action: (mode: GameMode) -> Unit) : BottomSheetDialogFragment() {

    private var _binding: BottomSheetSelectorBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetSelectorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.setOnShowListener { dialogInterface ->
            val bottomSheetDialog = dialogInterface as BottomSheetDialog
            val bottomSheet =
                bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)

            bottomSheet?.let {
                it.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_top_box)
            }
        }
        return dialog
    }

    private fun setListeners() {
        binding.apply {
            btnYearSingle.setOnClickListener { modeClicked(GUESS_YEAR_SINGLE) }
            btnYearMultiple.setOnClickListener { modeClicked(GUESS_YEAR_MULTIPLAYER) }
            btnTitle.setOnClickListener { modeClicked(GUESS_TITLE) }
        }
    }

    private fun modeClicked(mode: GameMode) {
        action(mode)
        dismiss()
    }
}
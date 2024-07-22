package com.mmfsin.musicmaster.presentation.categories.viewpager.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
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
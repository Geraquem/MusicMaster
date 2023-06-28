package com.mmfsin.musicmaster.presentation.selector

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mmfsin.musicmaster.databinding.FragmentSelectorBinding
import com.mmfsin.musicmaster.presentation.models.GameMode
import com.mmfsin.musicmaster.presentation.models.GameMode.*

class FragmentSelector(private val listener: IFragmentSelector, val category: String) : Fragment() {

    private var _bdg: FragmentSelectorBinding? = null
    private val binding get() = _bdg!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _bdg = FragmentSelectorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listeners()
    }

    private fun listeners() {
        binding.apply {
            background.setOnClickListener { listener.closeFragmentSelector() }
            btnYearSingle.setOnClickListener { openDashboard(GUESS_YEAR_SINGLE) }
            btnYearMultiplayer.setOnClickListener { openDashboard(GUESS_YEAR_MULTIPLAYER) }
            btnTitle.setOnClickListener { openDashboard(GUESS_TITLE) }
        }
    }

    private fun openDashboard(mode: GameMode) = listener.openActivityDashboard(mode, category)
}
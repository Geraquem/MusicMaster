package com.mmfsin.musicmaster.fragmentselector

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mmfsin.musicmaster.databinding.FragmentSelectorBinding

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

        binding.background.setOnClickListener {
            listener.closeFragmentSelector()
        }

        binding.buttonYear.setOnClickListener {
            openActivityDashboard(true)
        }
        binding.buttonTitle.setOnClickListener {
            openActivityDashboard(false)
        }
    }

    private fun openActivityDashboard(isYear: Boolean) {
        listener.openActivityDashboard(isYear, category)
        listener.closeFragmentSelector()
    }

    interface IFragmentSelector {
        fun openActivityDashboard(isYear: Boolean, category: String)
        fun closeFragmentSelector()
    }
}
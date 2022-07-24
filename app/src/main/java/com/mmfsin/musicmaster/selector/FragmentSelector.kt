package com.mmfsin.musicmaster.selector

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getDrawable
import androidx.fragment.app.Fragment
import com.mmfsin.musicmaster.R
import com.mmfsin.musicmaster.databinding.FragmentSelectorBinding

class FragmentSelector(private val listener: IFragmentSelector, val category: String) : Fragment() {

    private var _bdg: FragmentSelectorBinding? = null
    private val binding get() = _bdg!!

    private var singleMode = true

    private lateinit var mContext: Context

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _bdg = FragmentSelectorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.background.setOnClickListener { listener.closeFragmentSelector() }

        listeners()
    }

    private fun listeners() {
        val unselected = getDrawable(mContext, R.drawable.bg_button_unselected)
        val selected = getDrawable(mContext, R.drawable.bg_button_selected)

        with(binding) {
            buttonSingleplayer.setOnClickListener {
                buttonSingleplayer.background = selected
                buttonMultiplayer.background = unselected
                singleMode = true
            }

            buttonMultiplayer.setOnClickListener {
                buttonSingleplayer.background = unselected
                buttonMultiplayer.background = selected
                singleMode = false
            }

            buttonYear.setOnClickListener { openActivityDashboard(true) }
            buttonTitle.setOnClickListener { openActivityDashboard(false) }
        }
    }

    private fun openActivityDashboard(isYear: Boolean) {
        if (singleMode) {
            listener.openActivityDashboard(isYear, category)
        }else{
            listener.openActivityDashMultiplayer(isYear, category)
        }
        listener.closeFragmentSelector()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }
}
package com.mmfsin.musicmaster.fragmentselector

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mmfsin.musicmaster.R
import kotlinx.android.synthetic.main.fragment_selector.*

class FragmentSelector(private val listener: IFragmentSelector, val id: String) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_selector, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        background.setOnClickListener {
            listener.closeFragmentSelector()
        }

        buttonYear.setOnClickListener {
            openActivityDashboard(true)
        }
        buttonTitle.setOnClickListener {
            openActivityDashboard(false)
        }
    }

    private fun openActivityDashboard(isYear: Boolean){
        listener.openActivityDashboard(isYear, id)
        listener.closeFragmentSelector()
    }

    interface IFragmentSelector {
        fun openActivityDashboard(isYear: Boolean, id: String)
        fun closeFragmentSelector()
    }
}
package com.mmfsin.musicmaster.base.bedrock

import android.view.LayoutInflater
import android.view.ViewGroup
import com.mmfsin.musicmaster.base.BaseFragmentNoVM
import com.mmfsin.musicmaster.databinding.FragmentBlankBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BlankFragment : BaseFragmentNoVM<FragmentBlankBinding>() {

    override fun inflateView(
        inflater: LayoutInflater, container: ViewGroup?
    ) = FragmentBlankBinding.inflate(inflater, container, false)
}
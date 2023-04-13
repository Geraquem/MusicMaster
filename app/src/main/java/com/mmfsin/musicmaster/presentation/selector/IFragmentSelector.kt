package com.mmfsin.musicmaster.presentation.selector

import com.mmfsin.musicmaster.domain.types.GameMode

interface IFragmentSelector {

    fun openFragmentSelector(category: String)
    fun closeFragmentSelector()

    fun openActivityDashboard(mode: GameMode, category: String)
}
package com.mmfsin.musicmaster.presentation.selector

interface IFragmentSelector {
    fun openFragmentSelector(category: String)
    fun openActivityDashboard(isYear: Boolean, category: String)
    fun openActivityDashMultiplayer(isYear: Boolean, category: String)
}
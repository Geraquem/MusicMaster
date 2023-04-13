package com.mmfsin.musicmaster.presentation.selector

interface IFragmentSelector {

    fun openFragmentSelector(category: String)
    fun closeFragmentSelector()

    fun openActivityDashboard(isYear: Boolean, category: String)
    fun openActivityDashMultiplayer(isYear: Boolean, category: String)
}
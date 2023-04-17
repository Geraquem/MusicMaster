package com.mmfsin.musicmaster.presentation.dashboard

interface IDashboardListener {
    fun closeKeyboard()
    fun changeToolbar(category: String)
    fun noMoreData()
    fun exit()
}
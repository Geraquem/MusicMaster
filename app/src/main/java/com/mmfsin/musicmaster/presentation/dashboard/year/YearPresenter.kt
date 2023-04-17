package com.mmfsin.musicmaster.presentation.dashboard.year

class YearPresenter(private val view: YearView) {

    fun year4digits(year: String): Boolean {
        return year.length == 4
    }
}
package com.mmfsin.musicmaster.guesser.year

import android.content.Context
import com.mmfsin.musicmaster.guesser.GuesserView
import com.mmfsin.musicmaster.guesser.repository.FirebaseRepo

class YearGuesserHelper(private val view: GuesserView) {

    fun isValidYear(pinViewText: String): Boolean {
        return (pinViewText.length == 4)
    }

    fun setSolutionMessage(userYearStr: String, correctYearStr: String) {
        val userYear = userYearStr.toIntOrNull()
        val correctYear = correctYearStr.toInt()
        if (userYear != null) {
            if (userYear == correctYear) {
                view.setSolutionMessage(0)
            } else if (userYear > (correctYear - 3) && userYear < (correctYear + 3) && userYear != correctYear) {
                view.setSolutionMessage(1)
            } else {
                view.setSolutionMessage(2)
            }
        } else {
            view.somethingWentWrong()
        }
    }
}
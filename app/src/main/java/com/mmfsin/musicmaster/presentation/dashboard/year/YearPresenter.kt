package com.mmfsin.musicmaster.presentation.dashboard.year

import com.mmfsin.musicmaster.data.repository.MusicRepository
import com.mmfsin.musicmaster.domain.interfaces.IMusicRepository
import com.mmfsin.musicmaster.domain.models.MusicDTO
import com.mmfsin.musicmaster.domain.types.ResultType.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.math.absoluteValue

class YearPresenter(private val view: YearView) : IMusicRepository, CoroutineScope {

    override val coroutineContext: CoroutineContext = Dispatchers.Main

    private val repository by lazy { MusicRepository(this) }

    fun year4digits(year: String): Boolean = (year.length == 4)

    fun getMusicData(category: String) =
        launch(Dispatchers.IO) { repository.getMusicData(category) }

    override fun musicData(list: List<MusicDTO>) {
        launch { view.musicData(list) }
    }

    fun solution(userAnswerStr: String, correctYear: Long) {
        val userAnswer = userAnswerStr.toIntOrNull()
        userAnswer?.let { userYear ->
            val type = when ((userYear - correctYear.toInt()).absoluteValue) {
                0 -> GOOD
                1 -> ALMOST_GOOD
                2 -> ALMOST_GOOD
                3 -> ALMOST_GOOD
                else -> BAD
            }
            view.solution(type)
        } ?: run {
            view.somethingWentWrong()
        }
    }

    override fun somethingWentWrong() {

    }
}
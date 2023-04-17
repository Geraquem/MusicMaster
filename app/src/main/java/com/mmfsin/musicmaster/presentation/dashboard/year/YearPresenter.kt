package com.mmfsin.musicmaster.presentation.dashboard.year

import com.mmfsin.musicmaster.data.repository.MusicRepository
import com.mmfsin.musicmaster.domain.interfaces.IMusicRepository
import com.mmfsin.musicmaster.domain.models.MusicDTO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class YearPresenter(private val view: YearView) : IMusicRepository, CoroutineScope {

    override val coroutineContext: CoroutineContext = Dispatchers.Main

    private val repository by lazy { MusicRepository(this) }

    fun year4digits(year: String): Boolean = (year.length == 4)

    fun getMusicData(category: String) =
        launch(Dispatchers.IO) { repository.getMusicData(category) }


    override fun musicData(list: List<MusicDTO>) {
        launch { view.musicData(list) }
    }

    override fun somethingWentWrong() {

    }
}
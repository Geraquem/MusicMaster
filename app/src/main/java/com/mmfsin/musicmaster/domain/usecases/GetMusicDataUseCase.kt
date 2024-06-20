package com.mmfsin.musicmaster.domain.usecases

import com.mmfsin.musicmaster.base.BaseUseCase
import com.mmfsin.musicmaster.domain.interfaces.IMusicRepository
import com.mmfsin.musicmaster.domain.models.Music
import javax.inject.Inject

class GetMusicDataUseCase @Inject constructor(private val repository: IMusicRepository) :
    BaseUseCase<GetMusicDataUseCase.Params, List<Music>>() {

    override suspend fun execute(params: Params): List<Music> {
        val musicList = repository.getMusicDataFromFirebase(params.categoryId)

        return try {
            val random = System.currentTimeMillis().toString().last().toString().toInt()
            val iteration = if (random == 0) 2
            else if (random < 4) random
            else 3

            var randomList = listOf<Music>()
            for (i in 1..iteration) {
                randomList = musicList.shuffled()
            }
            randomList
        } catch (e: Exception) {
            musicList.shuffled()
        }
    }

    data class Params(
        val categoryId: String
    )
}
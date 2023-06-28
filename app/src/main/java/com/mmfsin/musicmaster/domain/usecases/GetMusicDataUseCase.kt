package com.mmfsin.musicmaster.domain.usecases

import com.mmfsin.musicmaster.base.BaseUseCase
import com.mmfsin.musicmaster.domain.interfaces.IMusicRepository
import com.mmfsin.musicmaster.domain.models.Music
import javax.inject.Inject

class GetMusicDataUseCase @Inject constructor(private val repository: IMusicRepository) :
    BaseUseCase<GetMusicDataUseCase.Params, List<Music>>() {

    override suspend fun execute(params: Params) =
        repository.getMusicDataFromFirebase(params.categoryId)

    data class Params(
        val categoryId: String
    )
}
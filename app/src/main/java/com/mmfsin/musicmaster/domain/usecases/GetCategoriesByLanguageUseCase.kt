package com.mmfsin.musicmaster.domain.usecases

import com.mmfsin.musicmaster.base.BaseUseCase
import com.mmfsin.musicmaster.domain.interfaces.ICategoryRepository
import com.mmfsin.musicmaster.domain.models.Category
import javax.inject.Inject

class GetCategoriesByLanguageUseCase @Inject constructor(private val repository: ICategoryRepository) :
    BaseUseCase<GetCategoriesByLanguageUseCase.Params, List<Category>>() {

    override suspend fun execute(params: Params) =
        repository.getCategoriesByLanguage(params.language)

    data class Params(
        val language: String
    )
}
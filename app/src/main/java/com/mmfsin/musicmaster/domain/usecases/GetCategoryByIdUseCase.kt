package com.mmfsin.musicmaster.domain.usecases

import com.mmfsin.musicmaster.base.BaseUseCase
import com.mmfsin.musicmaster.domain.interfaces.ICategoryRepository
import com.mmfsin.musicmaster.domain.models.Category
import javax.inject.Inject

class GetCategoryByIdUseCase @Inject constructor(private val repository: ICategoryRepository) :
    BaseUseCase<GetCategoryByIdUseCase.Params, Category?>() {

    override suspend fun execute(params: Params) = repository.getCategoryById(params.categoryId)

    data class Params(
        val categoryId: String
    )
}
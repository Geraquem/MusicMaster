package com.mmfsin.musicmaster.domain.usecases

import com.mmfsin.musicmaster.base.BaseUseCaseNoParams
import com.mmfsin.musicmaster.domain.interfaces.ICategoryRepository
import com.mmfsin.musicmaster.domain.models.Category
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(private val repository: ICategoryRepository) :
    BaseUseCaseNoParams<List<Category>>() {

    override suspend fun execute(): List<Category> {
        val categories = repository.getCategoriesFromRealm()
//        return categories.ifEmpty { repository.getCategoriesFromFirebase() }
        return repository.getCategoriesFromFirebase()
    }
}
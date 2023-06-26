package com.mmfsin.musicmaster.domain.interfaces

import com.mmfsin.musicmaster.domain.models.Category

interface ICategoryRepository {
    fun getCategoriesFromRealm(): List<Category>
    fun getCategoryFromRealm(id: String): Category?
    suspend fun getCategoriesFromFirebase(): List<Category>
}
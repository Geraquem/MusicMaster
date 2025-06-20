package com.mmfsin.musicmaster.domain.interfaces

import com.mmfsin.musicmaster.domain.models.Category

interface ICategoryRepository {
    fun getCategoriesFromRealm(): List<Category>
    fun getCategoriesByLanguage(language: String): List<Category>
    fun getCategoryById(id: String): Category?
    suspend fun getCategoriesFromFirebase(): List<Category>
}
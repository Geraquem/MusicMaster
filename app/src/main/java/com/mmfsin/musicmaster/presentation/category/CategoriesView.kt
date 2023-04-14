package com.mmfsin.musicmaster.presentation.category

import com.mmfsin.musicmaster.domain.models.CategoryDTO

interface CategoriesView {
    fun categoriesReady()
    fun getCategoriesInfo(info : List<CategoryDTO>)
}
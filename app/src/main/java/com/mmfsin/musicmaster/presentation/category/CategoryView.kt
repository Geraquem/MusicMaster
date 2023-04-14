package com.mmfsin.musicmaster.presentation.category

import com.mmfsin.musicmaster.domain.models.CategoryDTO

interface CategoryView {
    fun getCategoriesInfo(info : List<CategoryDTO>)
    fun showFragmentSelector(category: String)
}
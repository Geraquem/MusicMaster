package com.mmfsin.musicmaster.category

import com.mmfsin.musicmaster.category.model.CategoryDTO

interface CategoryView {
    fun completeEnglishRV(data : List<CategoryDTO>)
    fun completeSpanishRV(data : List<CategoryDTO>)
    fun showFragmentSelector()
    fun navigateToDashboard()
}
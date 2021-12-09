package com.mmfsin.musicmaster.category

import com.mmfsin.musicmaster.category.model.CategoryDTO

interface CategoryView {
    fun initRecyclerView(data : List<CategoryDTO>)
    fun showFragmentSelector()
    fun navigateToDashboard()
}
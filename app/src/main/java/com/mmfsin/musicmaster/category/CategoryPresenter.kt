package com.mmfsin.musicmaster.category

import com.mmfsin.musicmaster.category.model.CategoryDTO

class CategoryPresenter(private val categoryView: CategoryView?): CategoryInteractor.OnDataRetrieved{

    private val categoryInteractor = CategoryInteractor(this)

    fun setRecyclerViewsData() {
        categoryInteractor.getRecyclerViewsData()
    }

    fun navigateToSelector(){

    }

    override fun onEnglishDataSuccess(data : List<CategoryDTO>) {
        categoryView?.completeEnglishRV(data)
    }

    override fun onSpanishDataSuccess(data: List<CategoryDTO>) {
        categoryView?.completeSpanishRV(data)
    }
}
package com.mmfsin.musicmaster.category

import com.mmfsin.musicmaster.category.model.CategoryDTO

class CategoryPresenter(private val categoryView: CategoryView?): CategoryInteractor.OnDataRetrieved{

    private val categoryInteractor = CategoryInteractor(this)

    fun setEnglishRVData() {
        categoryInteractor.getEnglishRVData()
    }

    fun setSpanishRVData() {
        categoryInteractor.getSpanishRVData()
    }

    fun navigateToSelector() {

    }

    override fun onSuccess(data: List<CategoryDTO>) {
        categoryView?.initRecyclerView(data)
    }
}
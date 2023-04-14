package com.mmfsin.musicmaster.presentation.category

class CategoryPresenter(private var categoryView: CategoryView?) {

    fun getCategories(){

    }

    fun setEnglishRVData() {
        categoryView?.getCategoriesInfo(emptyList())
    }

    fun setSpanishRVData() {
        categoryView?.getCategoriesInfo(emptyList())
    }

    fun navigateToFragmentSelector(category: String) {
        categoryView?.showFragmentSelector(category)
    }
}
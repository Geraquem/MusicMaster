package com.mmfsin.musicmaster.presentation.category

import com.mmfsin.musicmaster.data.local.CategoryData.getEnglishData
import com.mmfsin.musicmaster.data.local.CategoryData.getSpanishData

class CategoryPresenter(private var categoryView: CategoryView?) {

    fun setEnglishRVData() {
        categoryView?.initRecyclerView(getEnglishData())
    }

    fun setSpanishRVData() {
        categoryView?.initRecyclerView(getSpanishData())
    }

    fun navigateToFragmentSelector(category: String) {
        categoryView?.showFragmentSelector(category)
    }
}
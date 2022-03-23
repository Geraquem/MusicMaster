package com.mmfsin.musicmaster.category

import com.mmfsin.musicmaster.category.data.Data.getEnglishData
import com.mmfsin.musicmaster.category.data.Data.getSpanishData

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
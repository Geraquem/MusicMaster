package com.mmfsin.musicmaster.category

import com.mmfsin.musicmaster.category.data.Data.getEnglishData
import com.mmfsin.musicmaster.category.data.Data.getSpanishData
import com.mmfsin.musicmaster.category.model.CategoryDTO

class CategoryInteractor(val listener: OnDataRetrieved) {

    fun getRecyclerViewsData() {
        listener.onEnglishDataSuccess(getEnglishData())
        listener.onSpanishDataSuccess(getSpanishData())
    }

    interface OnDataRetrieved {
        fun onEnglishDataSuccess(data: List<CategoryDTO>)
        fun onSpanishDataSuccess(data: List<CategoryDTO>)
    }
}
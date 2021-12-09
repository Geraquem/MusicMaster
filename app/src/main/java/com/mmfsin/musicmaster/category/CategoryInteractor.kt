package com.mmfsin.musicmaster.category

import com.mmfsin.musicmaster.category.data.Data.getEnglishData
import com.mmfsin.musicmaster.category.data.Data.getSpanishData
import com.mmfsin.musicmaster.category.model.CategoryDTO

class CategoryInteractor(val listener: OnDataRetrieved) {

    fun getEnglishRVData() {
        listener.onSuccess(getEnglishData())
    }

    fun getSpanishRVData() {
        listener.onSuccess(getSpanishData())
    }

    interface OnDataRetrieved {
        fun onSuccess(data: List<CategoryDTO>)
    }
}
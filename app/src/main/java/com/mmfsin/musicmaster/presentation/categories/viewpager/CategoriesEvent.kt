package com.mmfsin.musicmaster.presentation.categories.viewpager

import com.mmfsin.musicmaster.domain.models.Category

sealed class CategoriesEvent {
    class Categories(val result: List<Category>) : CategoriesEvent()
    object SomethingWentWrong : CategoriesEvent()
}
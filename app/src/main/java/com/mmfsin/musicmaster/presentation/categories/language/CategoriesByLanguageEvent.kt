package com.mmfsin.musicmaster.presentation.categories.language

import com.mmfsin.musicmaster.domain.models.Category

sealed class CategoriesByLanguageEvent {
    class Categories(val result: List<Category>) : CategoriesByLanguageEvent()
    object SomethingWentWrong : CategoriesByLanguageEvent()
}
package com.mmfsin.musicmaster.presentation.category

import com.mmfsin.musicmaster.data.repository.CategoryRepository
import com.mmfsin.musicmaster.domain.interfaces.ICategoryRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class CategoryPresenter(private var view: CategoriesView) : ICategoryRepository, CoroutineScope {

    override val coroutineContext: CoroutineContext = Dispatchers.Main

    private val repository by lazy { CategoryRepository(this) }

    fun getCategories() {
        launch(Dispatchers.IO) { repository.getCategoriesInfo() }
    }

    fun getEnglishCategories() = view.getCategoriesInfo(repository.getEnglishCategories())

    fun getSpanishCategories() = view.getCategoriesInfo(repository.getSpanishCategories())

    override fun categoriesReady() {
        launch { view.categoriesReady() }
    }

    override fun somethingWentWrong() {
    }
}
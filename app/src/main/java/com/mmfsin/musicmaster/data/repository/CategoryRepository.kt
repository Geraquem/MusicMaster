package com.mmfsin.musicmaster.data.repository

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.mmfsin.musicmaster.data.mappers.toCategory
import com.mmfsin.musicmaster.data.mappers.toCategoryList
import com.mmfsin.musicmaster.data.models.CategoryDTO
import com.mmfsin.musicmaster.domain.interfaces.ICategoryRepository
import com.mmfsin.musicmaster.domain.interfaces.IRealmDatabase
import com.mmfsin.musicmaster.domain.models.Category
import com.mmfsin.musicmaster.utils.CATEGORIES
import com.mmfsin.musicmaster.utils.LANGUAGE
import io.realm.kotlin.where
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.CountDownLatch
import javax.inject.Inject

class CategoryRepository @Inject constructor(
    private val realmDatabase: IRealmDatabase
) : ICategoryRepository {

    private val reference = Firebase.database.reference.child(CATEGORIES)

    override fun getCategoriesFromRealm(): List<Category> {
        return realmDatabase.getObjectsFromRealm { where<CategoryDTO>().findAll() }.toCategoryList()
    }

    override fun getCategoryById(id: String): Category? {
        val categories =
            realmDatabase.getObjectsFromRealm { where<CategoryDTO>().equalTo("id", id).findAll() }
        return if (categories.isEmpty()) null else categories.first().toCategory()
    }

    override fun getCategoriesByLanguage(language: String): List<Category> {
        return realmDatabase.getObjectsFromRealm {
            where<CategoryDTO>().equalTo(LANGUAGE, language).findAll()
        }.sortedBy { it.order }.toCategoryList()
    }

    override suspend fun getCategoriesFromFirebase(): List<Category> {
        val latch = CountDownLatch(1)
        val categories = mutableListOf<CategoryDTO>()
        reference.get().addOnSuccessListener {
            for (child in it.children) {
                child.getValue(CategoryDTO::class.java)?.let { category ->
                    categories.add(category)
                    saveCategory(category)
                }
            }
            latch.countDown()
        }.addOnFailureListener { latch.countDown() }

        withContext(Dispatchers.IO) {
            latch.await()
        }
        return categories.toCategoryList()
    }

    private fun saveCategory(category: CategoryDTO) = realmDatabase.addObject { category }
}
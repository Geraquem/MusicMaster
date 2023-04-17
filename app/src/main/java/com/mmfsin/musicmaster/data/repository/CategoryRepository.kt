package com.mmfsin.musicmaster.data.repository

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.mmfsin.musicmaster.data.database.RealmDatabase
import com.mmfsin.musicmaster.domain.interfaces.ICategoryRepository
import com.mmfsin.musicmaster.domain.models.CategoryDTO
import com.mmfsin.musicmaster.domain.utils.CATEGORY_DATA
import com.mmfsin.musicmaster.domain.utils.CATEGORY_INFO
import io.realm.kotlin.where

class CategoryRepository(private val listener: ICategoryRepository) {

    private val rootInfo = Firebase.database.reference.child(CATEGORY_INFO)

    private val realm by lazy { RealmDatabase() }

    fun getCategoriesInfo() {
//        realm.deleteCategoryData()
//        realm.deleteMusic()
        val categories = realm.getObjectsFromRealm { where<CategoryDTO>().findAll() }

        if (categories.isEmpty()) getCategoriesFromFirebase()
        else listener.categoriesReady()
    }

    private fun getCategoriesFromFirebase() {
        rootInfo.get().addOnSuccessListener {
            for (child in it.children) {
                child.getValue(CategoryDTO::class.java)?.let { category ->
                    saveCategories(category)
                }
            }
            getCategoriesInfo()

        }.addOnFailureListener {
            listener.somethingWentWrong()
        }
    }

    private fun saveCategories(category: CategoryDTO) = realm.addObject { category }

    fun getEnglishCategories(): List<CategoryDTO> {
        return realm.getObjectsFromRealm {
            where<CategoryDTO>().equalTo("language", "english").findAll()
        }.sortedBy { it.order }
    }

    fun getSpanishCategories(): List<CategoryDTO> {
        return realm.getObjectsFromRealm {
            where<CategoryDTO>().equalTo("language", "spanish").findAll()
        }.sortedBy { it.order }
    }
}
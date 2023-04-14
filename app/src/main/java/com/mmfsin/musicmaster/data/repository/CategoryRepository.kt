package com.mmfsin.musicmaster.data.repository

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.mmfsin.musicmaster.domain.interfaces.ICategoryRepository
import com.mmfsin.musicmaster.domain.utils.CATEGORY_DATA
import com.mmfsin.musicmaster.domain.utils.CATEGORY_INFO

class CategoryRepository(private val listener: ICategoryRepository) {

    private val rootInfo = Firebase.database.reference.child(CATEGORY_INFO)
    private val rootData = Firebase.database.reference.child(CATEGORY_DATA)

    fun getCategoryInfo(category: String) {
        rootInfo.child(category).get().addOnSuccessListener {

        }.addOnFailureListener {
            listener.somethingWentWrong()
        }
    }
}
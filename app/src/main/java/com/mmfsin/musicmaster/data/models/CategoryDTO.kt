package com.mmfsin.musicmaster.data.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class CategoryDTO(
    @PrimaryKey
    var id: String = "",
    var order: Int = 0,
    var language: String = "",
    var title: String = "",
    var description: String = "",
    var icon: String = "",
    var color: String? = null
) : RealmObject()

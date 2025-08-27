package com.mmfsin.musicmaster.data.models

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

open class CategoryDTO : RealmObject {
    @PrimaryKey
    var id: String = ""
    var order: Int = 0
    var language: String = ""
    var title: String = ""
    var description: String = ""
    var icon: String = ""
    var color: String? = null
}

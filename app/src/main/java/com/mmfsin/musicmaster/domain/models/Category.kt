package com.mmfsin.musicmaster.domain.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Category(
    @PrimaryKey
    var id: String = "",
    var order: Int = 0,
    var title: String = "",
    var description: String = "",
    var icon: String = ""
) : RealmObject()

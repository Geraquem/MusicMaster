package com.mmfsin.musicmaster.domain.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class MusicDTO(
    @PrimaryKey
    var title: String = "",
    var artist: String = "",
    var year: Long = 0,
    var videoUrl: String = "",
) : RealmObject()
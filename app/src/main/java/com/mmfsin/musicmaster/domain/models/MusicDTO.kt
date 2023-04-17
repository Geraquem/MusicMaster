package com.mmfsin.musicmaster.domain.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class MusicDTO(
    @PrimaryKey
    var title: String = "",
    var artist: String = "",
    var year: String = "",
    var videoUrl: String = "",
) : RealmObject()
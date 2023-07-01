package com.mmfsin.musicmaster.domain.models

data class Music(
    var title: String = "",
    var artist: String = "",
    var year: Long = 0,
    var videoUrl: String = "",
    var image: String? = null
)
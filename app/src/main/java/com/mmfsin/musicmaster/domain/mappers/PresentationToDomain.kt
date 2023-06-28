package com.mmfsin.musicmaster.domain.mappers

import com.mmfsin.musicmaster.R
import com.mmfsin.musicmaster.domain.types.Categories.*

fun String.getFontFamily(): Int = when (this) {
    ENGLISH_MIX.name.lowercase() -> R.font.mix
    ROCK.name.lowercase() -> R.font.rock
    POP.name.lowercase() -> R.font.pop
    HIPHOP.name.lowercase() -> R.font.hiphop
    POPULARES.name.lowercase() -> R.font.popular
    RAP.name.lowercase() -> R.font.rap
    else -> R.font.reggaeton
}
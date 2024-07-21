package com.mmfsin.musicmaster.domain.mappers

import com.mmfsin.musicmaster.R
import com.mmfsin.musicmaster.domain.types.Categories.CLASICAZOS
import com.mmfsin.musicmaster.domain.types.Categories.DANCE
import com.mmfsin.musicmaster.domain.types.Categories.POP
import com.mmfsin.musicmaster.domain.types.Categories.RAP
import com.mmfsin.musicmaster.domain.types.Categories.ROCK

fun String.getFontFamily(): Int = when (this.lowercase()) {
    ROCK.name.lowercase() -> R.font.rock
    POP.name.lowercase() -> R.font.pop
    DANCE.name.lowercase() -> R.font.electronic
    CLASICAZOS.name.lowercase() -> R.font.popular
    RAP.name.lowercase() -> R.font.rap
    else -> R.font.reggaeton
}

fun getColorByCategory(category: String): Int = when (category.lowercase()) {
    ROCK.name.lowercase() -> R.color.rock_color
    POP.name.lowercase() -> R.color.pop_color
    DANCE.name.lowercase() -> R.color.dance_color
    CLASICAZOS.name.lowercase() -> R.color.clasicazos_color
    RAP.name.lowercase() -> R.color.rap_color
    else -> R.color.reggaeton_color
}
package com.mmfsin.musicmaster.domain.mappers

import com.mmfsin.musicmaster.R
import com.mmfsin.musicmaster.domain.types.Categories.*
import com.mmfsin.musicmaster.domain.types.GameMode

fun String.toGameMode(): GameMode {
    return when (this) {
        "GUESS_YEAR_SINGLE" -> GameMode.GUESS_YEAR_SINGLE
        "GUESS_YEAR_MULTIPLAYER" -> GameMode.GUESS_YEAR_MULTIPLAYER
        else -> GameMode.GUESS_TITLE
    }
}

fun String.getFontFamily(): Int {
    return when (this) {
        ENGLISH_MIX.name.lowercase() -> R.font.mix
        ROCK.name.lowercase() -> R.font.rock
        POP.name.lowercase() -> R.font.pop
        HIPHOP.name.lowercase() -> R.font.hiphop
        POPULARES.name.lowercase() -> R.font.popular
        RAP.name.lowercase() -> R.font.rap
        else -> R.font.reggaeton
    }
}
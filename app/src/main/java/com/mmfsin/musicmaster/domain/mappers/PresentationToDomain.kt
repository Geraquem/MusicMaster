package com.mmfsin.musicmaster.domain.mappers

import com.mmfsin.musicmaster.R
import com.mmfsin.musicmaster.domain.types.Categories.*
import com.mmfsin.musicmaster.presentation.models.GameMode
import com.mmfsin.musicmaster.presentation.models.GameMode.*

fun String.toGameMode(): GameMode = when (this) {
    "GUESS_YEAR_SINGLE" -> GUESS_YEAR_SINGLE
    "GUESS_YEAR_MULTIPLAYER" -> GUESS_YEAR_MULTIPLAYER
    else -> GUESS_TITLE
}

fun String.getFontFamily(): Int = when (this) {
    ENGLISH_MIX.name.lowercase() -> R.font.mix
    ROCK.name.lowercase() -> R.font.rock
    POP.name.lowercase() -> R.font.pop
    HIPHOP.name.lowercase() -> R.font.hiphop
    POPULARES.name.lowercase() -> R.font.popular
    RAP.name.lowercase() -> R.font.rap
    else -> R.font.reggaeton
}

fun String.getToolbarTitle(): Int = when (this) {
    ENGLISH_MIX.name.lowercase() -> R.string.english_mix
    ROCK.name.lowercase() -> R.string.rock
    POP.name.lowercase() -> R.string.pop
    HIPHOP.name.lowercase() -> R.string.hiphop
    INDIE.name.lowercase() -> R.string.indie
    POPULARES.name.lowercase() -> R.string.populares
    RAP.name.lowercase() -> R.string.rap
    else -> R.string.reggaeton
}
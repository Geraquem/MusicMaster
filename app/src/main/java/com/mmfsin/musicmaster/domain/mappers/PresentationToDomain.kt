package com.mmfsin.musicmaster.domain.mappers

import com.mmfsin.musicmaster.domain.types.GameMode

fun String.toGameMode(): GameMode {
    return when (this) {
        "GUESS_YEAR_SINGLE" -> GameMode.GUESS_YEAR_SINGLE
        "GUESS_YEAR_MULTIPLAYER" -> GameMode.GUESS_YEAR_MULTIPLAYER
        else -> GameMode.GUESS_TITLE
    }
}
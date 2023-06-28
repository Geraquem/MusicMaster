package com.mmfsin.musicmaster.presentation.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GameInfo(
    val categoryId: String,
    val mode: GameMode
) : Parcelable

enum class GameMode {
    GUESS_YEAR_SINGLE,
    GUESS_YEAR_MULTIPLAYER,
    GUESS_TITLE
}
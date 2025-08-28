package com.mmfsin.musicmaster.domain.models

enum class Order {
    OLDER,
    NEWER
}

data class OrderResponse(
    val isCorrect: Boolean? = null,
    val sameYear: Boolean? = null,
)

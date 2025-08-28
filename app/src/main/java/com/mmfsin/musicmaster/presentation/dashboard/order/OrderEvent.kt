package com.mmfsin.musicmaster.presentation.dashboard.order

import com.mmfsin.musicmaster.domain.models.Category
import com.mmfsin.musicmaster.domain.models.Music

sealed class OrderEvent {
    class CategoryData(val category: Category) : OrderEvent()
    class MusicData(val data: List<Music>) : OrderEvent()
    class Solution(val solution: Boolean) : OrderEvent()
    data object SomethingWentWrong : OrderEvent()
}
package com.mmfsin.musicmaster.presentation.dashboard.order

import com.mmfsin.musicmaster.domain.models.Category
import com.mmfsin.musicmaster.domain.models.Music
import com.mmfsin.musicmaster.domain.models.OrderSelected
import com.mmfsin.musicmaster.domain.models.OrderSolution

sealed class OrderEvent {
    class CategoryData(val category: Category) : OrderEvent()
    class MusicData(val data: List<Music>) : OrderEvent()
    class Solution(val solution: Pair<OrderSelected, OrderSolution>) : OrderEvent()
    data object SomethingWentWrong : OrderEvent()
}
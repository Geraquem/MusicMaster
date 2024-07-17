package com.mmfsin.musicmaster.data.mappers

import com.mmfsin.musicmaster.data.models.CategoryDTO
import com.mmfsin.musicmaster.domain.models.Category

fun CategoryDTO.toCategory() = Category(
    id = id,
    title = title,
    description = description,
    icon = icon,
    color = color
)

fun List<CategoryDTO>.toCategoryList() = this.map { it.toCategory() }
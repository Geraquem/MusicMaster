package com.mmfsin.musicmaster.data.local

import com.mmfsin.musicmaster.R
import com.mmfsin.musicmaster.domain.models.CategoryDTO

object CategoryData {

    fun getEnglishData() = listOf(
        CategoryDTO("mix", R.drawable.ic_category_mix, R.font.mix, R.string.mix, R.string.mixArtists),
        CategoryDTO("rock", R.drawable.ic_category_rock, R.font.rock, R.string.rock, R.string.rockArtists),
        CategoryDTO("pop", R.drawable.ic_category_pop, R.font.pop, R.string.pop, R.string.popArtists),
        CategoryDTO("hiphop", R.drawable.ic_category_hiphop, R.font.hiphop, R.string.hiphop, R.string.hiphopArtists),
        CategoryDTO("antes2000", R.drawable.ic_category_antes, R.font.antes, R.string.before2000, R.string.beforeArtists),
        CategoryDTO("despues2000", R.drawable.ic_category_despues, R.font.despues, R.string.after2000, R.string.afterArtists)
    )

    fun getSpanishData() = listOf(
        CategoryDTO("populares", R.drawable.ic_category_populares, R.font.popular, R.string.populares, R.string.popularesArtists),
        CategoryDTO("rap", R.drawable.ic_category_rap, R.font.rap, R.string.rap, R.string.rapArtists),
        CategoryDTO("reggaeton", R.drawable.ic_category_reggaeton, R.font.reggaeton, R.string.reggaeton, R.string.reggaetonArtists)
    )
}
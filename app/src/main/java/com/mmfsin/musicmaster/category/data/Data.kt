package com.mmfsin.musicmaster.category.data

import com.mmfsin.musicmaster.R
import com.mmfsin.musicmaster.category.model.CategoryDTO

object Data {

    fun getEnglishData(): List<CategoryDTO> {
        val englishData = mutableListOf<CategoryDTO>()
        englishData.add(CategoryDTO("mix", R.drawable.ic_category_mix, R.font.mix, R.string.mix, R.string.mixArtists))
        englishData.add(CategoryDTO("rock", R.drawable.ic_category_rock, R.font.rock, R.string.rock, R.string.rockArtists))
        englishData.add(CategoryDTO("pop", R.drawable.ic_category_pop, R.font.pop, R.string.pop, R.string.popArtists))
        englishData.add(CategoryDTO("hiphop", R.drawable.ic_category_hiphop, R.font.hiphop, R.string.hiphop, R.string.hiphopArtists))
        englishData.add(CategoryDTO("antes2000", R.drawable.ic_category_antes, R.font.antes, R.string.before2000, R.string.beforeArtists))
        englishData.add(CategoryDTO("despues2000", R.drawable.ic_category_despues, R.font.despues, R.string.after2000, R.string.afterArtists))
        return englishData
    }

    fun getSpanishData(): List<CategoryDTO> {
        val englishData = mutableListOf<CategoryDTO>()
        englishData.add(CategoryDTO("populares", R.drawable.ic_category_populares, R.font.popular, R.string.populares, R.string.popularesArtists))
        englishData.add(CategoryDTO("rap", R.drawable.ic_category_rap, R.font.rap, R.string.rap, R.string.rapArtists))
        englishData.add(CategoryDTO("reggaeton", R.drawable.ic_category_reggaeton, R.font.reggaeton, R.string.reggaeton, R.string.reggaetonArtists))
        return englishData
    }
}
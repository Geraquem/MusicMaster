package com.mmfsin.musicmaster.category.data

import com.mmfsin.musicmaster.R
import com.mmfsin.musicmaster.category.model.CategoryDTO

object Data {

    fun getEnglishData(): List<CategoryDTO> {
        val englishData = mutableListOf<CategoryDTO>()
        englishData.add(CategoryDTO(R.drawable.ic_rock, R.string.rock, R.string.rockArtists))
        englishData.add(CategoryDTO(R.drawable.ic_pop, R.string.pop, R.string.popArtists))
        englishData.add(CategoryDTO(R.drawable.ic_hiphop, R.string.hiphop, R.string.hiphopArtists))

        /*font family y id para la bbdd*/

        return englishData
    }

    fun getSpanishData(): List<CategoryDTO> {
        val englishData = mutableListOf<CategoryDTO>()
        englishData.add(CategoryDTO(R.drawable.ic_popular, R.string.rock, R.string.popularesArtists))
        englishData.add(CategoryDTO(R.drawable.ic_rap, R.string.pop, R.string.rapArtists))
        englishData.add(CategoryDTO(R.drawable.ic_reggaeton, R.string.reggaeton, R.string.reggaetonArtists))
        return englishData
    }
}
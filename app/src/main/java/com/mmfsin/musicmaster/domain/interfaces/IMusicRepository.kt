package com.mmfsin.musicmaster.domain.interfaces

import com.mmfsin.musicmaster.domain.models.Music

interface IMusicRepository {
    suspend fun getMusicDataFromFirebase(categoryId: String): List<Music>
}
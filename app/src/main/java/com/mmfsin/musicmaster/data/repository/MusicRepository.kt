package com.mmfsin.musicmaster.data.repository

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.mmfsin.musicmaster.domain.interfaces.IMusicRepository
import com.mmfsin.musicmaster.domain.models.Music
import com.mmfsin.musicmaster.utils.MUSIC
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.CountDownLatch
import javax.inject.Inject

class MusicRepository @Inject constructor() : IMusicRepository {

    private val reference = Firebase.database.reference.child(MUSIC)

    override suspend fun getMusicDataFromFirebase(categoryId: String): List<Music> {
        val latch = CountDownLatch(1)
        val data = mutableListOf<Music>()
        reference.child(categoryId).get().addOnSuccessListener {
            for (child in it.children) {
                child.getValue(Music::class.java)?.let { category -> data.add(category) }
            }
            latch.countDown()
        }.addOnFailureListener { latch.countDown() }

        withContext(Dispatchers.IO) {
            latch.await()
        }
        return data
    }
}
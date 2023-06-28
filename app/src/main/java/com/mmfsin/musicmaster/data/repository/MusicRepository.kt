package com.mmfsin.musicmaster.data.repository

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.mmfsin.musicmaster.domain.interfaces.IMusicRepository
import com.mmfsin.musicmaster.domain.models.MusicDTO
import com.mmfsin.musicmaster.utils.MUSIC

class MusicRepository(private val listener: IMusicRepository) {

    private val rootMusic = Firebase.database.reference.child(MUSIC)

//    private val realm by lazy { RealmDatabasea() }
//
//    fun getMusicData(category: String) {
//        rootMusic.child(category).get().addOnSuccessListener {
//            val data = mutableListOf<MusicDTO>()
//            for (child in it.children) {
//                child.getValue(MusicDTO::class.java)?.let { music -> data.add(music) }
//            }
//            listener.musicData(data)
//
//        }.addOnFailureListener {
//            listener.somethingWentWrong()
//        }
//    }
}
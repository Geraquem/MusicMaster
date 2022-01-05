package com.mmfsin.musicmaster.guesser.repository

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.mmfsin.musicmaster.guesser.model.MusicVideoDTO

class FirebaseRepo(val listener: IRepo) {

    fun getMusicVideoList(category: String) {
        Firebase.database.reference.child(category).get()
            .addOnSuccessListener {
                listener.musicVideoList(it.childrenCount)

            }.addOnFailureListener {
                listener.somethingWentWrong()
            }
    }

    fun getMusicVideo(category: String, video: String) {
        Firebase.database.reference.child(category).child(video).get()
            .addOnSuccessListener {
                it.getValue(MusicVideoDTO::class.java)?.let { it1 -> listener.musicVideo(it1) }

            }.addOnFailureListener {
                listener.somethingWentWrong()
            }
    }

    interface IRepo {
        fun musicVideo(musicVideo: MusicVideoDTO)
        fun musicVideoList(size: Long)
        fun somethingWentWrong()
    }
}
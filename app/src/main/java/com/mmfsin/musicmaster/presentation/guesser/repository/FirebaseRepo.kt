package com.mmfsin.musicmaster.presentation.guesser.repository

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.mmfsin.musicmaster.presentation.guesser.model.MusicVideoDTO

class FirebaseRepo(private val listener: IRepo) {

    fun getMusicVideoList(category: String) {
        Firebase.database.reference.child(category).get()
            .addOnSuccessListener {
                val list = mutableListOf<String>()
                for (video in it.children) {
                    video.key?.let { element -> list.add(element) }
                }
                list.shuffle()
                listener.musicVideoList(list)

            }.addOnFailureListener {
                listener.somethingWentWrong()
            }
    }

    fun getMusicVideo(category: String, video: String) {
        Firebase.database.reference.child(category).child(video).get()
            .addOnSuccessListener {
                it.getValue(MusicVideoDTO::class.java)?.let { it1 -> listener.musicVideoData(it1) }

            }.addOnFailureListener {
                listener.somethingWentWrong()
            }
    }

    interface IRepo {
        fun musicVideoList(list: List<String>)
        fun musicVideoData(musicVideo: MusicVideoDTO)
        fun somethingWentWrong()
    }
}
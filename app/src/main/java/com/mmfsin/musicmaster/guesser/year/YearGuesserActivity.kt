package com.mmfsin.musicmaster.guesser.year

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.mmfsin.musicmaster.R
import kotlinx.android.synthetic.main.activity_year_guesser.*

class YearGuesserActivity : AppCompatActivity(), YearGuesserView {

    private val presenter by lazy { YearGuesserPresenter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_year_guesser)
        read()
    }

    private fun read() {
        Firebase.database.reference.child("rap").child("video1").child("titulo").get()
            .addOnSuccessListener {
                text.text = it.getValue().toString()

            }.addOnFailureListener {
            text.text = "DATABASE ERROR"
        }
    }
}
package com.mmfsin.musicmaster.guesser.year

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.mmfsin.musicmaster.R
import com.mmfsin.musicmaster.guesser.repository.FirebaseRepo
import kotlinx.android.synthetic.main.activity_year_guesser.*

class YearGuesserPresenter(private val view: YearGuesserView) : FirebaseRepo.IRepo {




    override fun getMusicVideo() {
        TODO("Not yet implemented")
    }

    override fun somethingWentWrong() {
        TODO("Not yet implemented")
    }

}
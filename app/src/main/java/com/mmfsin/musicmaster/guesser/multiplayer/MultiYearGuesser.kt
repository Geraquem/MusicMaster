package com.mmfsin.musicmaster.guesser.multiplayer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mmfsin.musicmaster.databinding.ActivityMultiYearGuesserBinding
import com.mmfsin.musicmaster.guesser.GuesserView
import com.mmfsin.musicmaster.guesser.common.CommonPresenter
import com.mmfsin.musicmaster.guesser.helper.YearGuesserHelper
import com.mmfsin.musicmaster.guesser.model.MusicVideoDTO
import kotlin.properties.Delegates

class MultiYearGuesser : AppCompatActivity(), GuesserView {

    private lateinit var binding: ActivityMultiYearGuesserBinding

    private val helper by lazy { YearGuesserHelper(this) }
    private val presenter by lazy { CommonPresenter(this) }

    private lateinit var category: String
    private lateinit var videoList: List<String>
    private lateinit var correctYear: String

    private var position = 0

    //RPBA = Rock Pop Before2000 After2000
    private var isRPBA by Delegates.notNull<Boolean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMultiYearGuesserBinding.inflate(layoutInflater)
        setContentView(binding.root)    }

    override fun setMusicVideoData(musicVideo: MusicVideoDTO) {
    }

    override fun setMusicVideoList(list: List<String>) {
    }

    override fun setSolutionMessage(solutionResult: Int) {
    }

    override fun somethingWentWrong() {
    }
}
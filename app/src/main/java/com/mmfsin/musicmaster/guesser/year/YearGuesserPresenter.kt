package com.mmfsin.musicmaster.guesser.year

import android.content.Context
import android.text.BoringLayout
import cn.pedant.SweetAlert.SweetAlertDialog
import com.mmfsin.musicmaster.R
import com.mmfsin.musicmaster.guesser.model.MusicVideoDTO
import com.mmfsin.musicmaster.guesser.repository.FirebaseRepo

class YearGuesserPresenter(private val view: YearGuesserView) : FirebaseRepo.IRepo {

    private val repository by lazy { FirebaseRepo(this) }

    fun isRPBA(category: String): Boolean {
        return when (category) {
            "rock" -> true
            "pop" -> true
            "antes2000" -> true
            "despues2000" -> true
            else -> false
        }
    }

    fun showSweetAlertSwipe(context: Context) {
        SweetAlertDialog(context, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
            .setCustomImage(R.drawable.ic_swipe_left)
            .setContentText(context.getString(R.string.swipeLeft))
            .setConfirmText(context.getString(R.string.ok))
            .setConfirmClickListener { sDialog -> sDialog.dismissWithAnimation() }
            .show()
    }

    fun getMusicVideoList(category: String) {
        repository.getMusicVideoList(category)
    }

    fun getMusicVideo(category: String, video: String) {
        repository.getMusicVideo(category, video)
    }

    fun isValidYear(pinViewText: String): Boolean {
        return (pinViewText.length == 4)
    }

    fun setSolutionMessage(userYearStr: String, correctYearStr: String) {
        val userYear = userYearStr.toIntOrNull()
        val correctYear = correctYearStr.toInt()
        if (userYear != null) {
            if (userYear == correctYear) {
                view.setSolutionMessage(0)
            } else if (userYear > (correctYear - 3) && userYear < (correctYear + 3) && userYear != correctYear) {
                view.setSolutionMessage(1)
            } else {
                view.setSolutionMessage(2)
            }
        } else {
            view.somethingWentWrong()
        }
    }

    override fun musicVideoList(list: List<String>) {
        view.setMusicVideoList(list)
    }

    fun showSweetAlertError(context: Context) {
        SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
            .setTitleText(context.getString(R.string.oops))
            .setContentText(context.getString(R.string.somethingWentWrong))
            .show()
    }

    override fun musicVideo(musicVideo: MusicVideoDTO) {
        view.setMusicVideoData(musicVideo)
    }

    override fun somethingWentWrong() {
        view.somethingWentWrong()
    }
}
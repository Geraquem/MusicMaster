package com.mmfsin.musicmaster.guesser.common

import android.content.Context
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat.getFont
import cn.pedant.SweetAlert.SweetAlertDialog
import com.mmfsin.musicmaster.R

class Common {

    fun getCategoryTitle(context: Context, tv: TextView, category: String) {
        tv.text = getTitleFromCategory(context, category)
        tv.typeface = when (category) {
            context.getString(R.string.bbdd_mix) -> getFont(context, R.font.mix)
            context.getString(R.string.bbdd_rock) -> getFont(context, R.font.rock)
            context.getString(R.string.bbdd_pop) -> getFont(context, R.font.pop)
            context.getString(R.string.bbdd_hiphop) -> getFont(context, R.font.hiphop)
            context.getString(R.string.bbdd_antes2000) -> getFont(context, R.font.antes)
            context.getString(R.string.bbdd_despues2000) -> getFont(context, R.font.despues)
            context.getString(R.string.bbdd_populares) -> getFont(context, R.font.popular)
            context.getString(R.string.bbdd_rap) -> getFont(context, R.font.rap)
            context.getString(R.string.bbdd_reggaeton) -> getFont(context, R.font.reggaeton)
            else -> getFont(context, R.font.text)
        }
    }

    private fun getTitleFromCategory(context: Context, category: String): String {
        return when (category) {
            context.getString(R.string.bbdd_mix) -> context.getString(R.string.mix)
            context.getString(R.string.bbdd_rock) -> context.getString(R.string.rock)
            context.getString(R.string.bbdd_pop) -> context.getString(R.string.pop)
            context.getString(R.string.bbdd_hiphop) -> context.getString(R.string.hiphop)
            context.getString(R.string.bbdd_antes2000) -> context.getString(R.string.before2000)
            context.getString(R.string.bbdd_despues2000) -> context.getString(R.string.after2000)
            context.getString(R.string.bbdd_populares) -> context.getString(R.string.populares)
            context.getString(R.string.bbdd_rap) -> context.getString(R.string.rap)
            context.getString(R.string.bbdd_reggaeton) -> context.getString(R.string.reggaeton)
            else -> context.getString(R.string.somethingWentWrong)
        }
    }

    fun isRPBA(context: Context, category: String): Boolean {
        return when (category) {
            context.getString(R.string.bbdd_rock) -> true
            context.getString(R.string.bbdd_pop) -> true
            context.getString(R.string.bbdd_antes2000) -> true
            context.getString(R.string.bbdd_despues2000) -> true
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

    fun showSweetAlertError(context: Context) {
        SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
            .setTitleText(context.getString(R.string.oops))
            .setContentText(context.getString(R.string.somethingWentWrong))
            .show()
    }
}
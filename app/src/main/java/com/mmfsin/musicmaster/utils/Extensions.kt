package com.mmfsin.musicmaster.utils

import android.app.Activity
import android.content.Context
import android.graphics.ColorFilter
import android.view.inputmethod.InputMethodManager
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.SimpleColorFilter
import com.airbnb.lottie.model.KeyPath
import com.airbnb.lottie.value.LottieValueCallback
import com.mmfsin.musicmaster.base.dialog.ErrorDialog

fun FragmentActivity.showErrorDialog() {
    val dialog = ErrorDialog()
    this.let { dialog.show(it.supportFragmentManager, "") }
}

fun Activity.closeKeyboard() {
    this.currentFocus?.let { view ->
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(view.windowToken, 0)
    }
}

fun LottieAnimationView.changeLayersColor(@ColorRes colorRes: Int) {
    val color = ContextCompat.getColor(context, colorRes)
    val filter = SimpleColorFilter(color)
    val keyPath = KeyPath("**")
    val callback: LottieValueCallback<ColorFilter> = LottieValueCallback(filter)
    addValueCallback(keyPath, LottieProperty.COLOR_FILTER, callback)
}
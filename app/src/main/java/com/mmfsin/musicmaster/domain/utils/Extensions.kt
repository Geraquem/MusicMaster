package com.mmfsin.musicmaster.domain.utils

import androidx.fragment.app.FragmentActivity
import com.mmfsin.musicmaster.base.dialog.ErrorDialog

fun FragmentActivity.showErrorDialog() {
    val dialog = ErrorDialog()
    this.let { dialog.show(it.supportFragmentManager, "") }
}
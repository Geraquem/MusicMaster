package com.mmfsin.musicmaster.presentation.utils

import android.content.Context
import cn.pedant.SweetAlert.SweetAlertDialog
import com.mmfsin.musicmaster.R
import com.mmfsin.musicmaster.domain.types.Categories
import com.mmfsin.musicmaster.domain.types.GameMode

fun sww(context: Context) {
    SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
        .setTitleText(context.getString(R.string.oops))
        .setContentText(context.getString(R.string.sww))
        .show()
}
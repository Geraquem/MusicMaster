package com.mmfsin.musicmaster.presentation.utils

import android.content.Context
import cn.pedant.SweetAlert.SweetAlertDialog
import com.mmfsin.musicmaster.R

fun sww(context: Context): SweetAlertDialog {
    return SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
        .setTitleText(context.getString(R.string.oops))
        .setContentText(context.getString(R.string.sww))
}
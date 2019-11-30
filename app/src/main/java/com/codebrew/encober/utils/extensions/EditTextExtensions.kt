package com.codebrew.encober.utils.extensions

import android.widget.EditText


fun invalidString(editText: EditText, errorMsg: String) {
    editText.error = errorMsg
    editText.requestFocus()
}
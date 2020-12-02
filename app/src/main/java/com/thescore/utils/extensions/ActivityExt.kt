package com.thescore.utils.extensions

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar
import com.thescore.R

fun Activity.showMessage(@StringRes messageId: Int) {
    if (!this.isFinishing) {
        Snackbar.make(this.window.decorView.findViewById(R.id.content),
            this.getString(messageId), Snackbar.LENGTH_LONG).show()
    }
}

fun Activity.hideKeyboard(view: View) {
    val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.applicationWindowToken, 0)
}

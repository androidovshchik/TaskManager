/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

@file:Suppress("unused")

package defpackage.taskmanager.extensions

import android.widget.EditText

fun EditText.setTextSelection(text: CharSequence?) {
    text.toString().let {
        setText(it)
        setSelection(it.length)
    }
}
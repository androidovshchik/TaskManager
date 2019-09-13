/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

@file:Suppress("unused")

package defpackage.taskmanager.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.ViewManager
import android.widget.CheckBox
import org.jetbrains.anko.custom.ankoView

fun ViewManager.programCheckBox(theme: Int = 0) = programCheckBox(theme) {}

inline fun ViewManager.programCheckBox(theme: Int = 0, init: ProgramCheckBox.() -> Unit) =
    ankoView({ ProgramCheckBox(it) }, theme, init)

class ProgramCheckBox @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : CheckBox(context, attrs, defStyleAttr) {

    var programmatically = false

    override fun setChecked(checked: Boolean) {
        programmatically = false
        super.setChecked(checked)
    }

    fun setCheckedProgrammatically(checked: Boolean) {
        programmatically = true
        super.setChecked(checked)
    }
}
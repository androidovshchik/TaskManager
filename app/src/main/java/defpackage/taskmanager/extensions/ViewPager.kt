/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

@file:Suppress("unused")

package defpackage.taskmanager.extensions

import android.view.ViewManager
import androidx.viewpager.widget.ViewPager
import org.jetbrains.anko.custom.ankoView

fun ViewManager.viewPager(theme: Int = 0) = viewPager(theme) {}

inline fun ViewManager.viewPager(theme: Int = 0, init: ViewPager.() -> Unit) =
    ankoView({ ViewPager(it) }, theme, init)
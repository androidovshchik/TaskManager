/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

@file:Suppress("unused")

package defpackage.taskmanager.extensions

import android.view.ViewManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import org.jetbrains.anko.custom.ankoView

fun ViewManager.swipeRefreshLayout(theme: Int = 0) = swipeRefreshLayout(theme) {}

inline fun ViewManager.swipeRefreshLayout(theme: Int = 0, init: SwipeRefreshLayout.() -> Unit) =
    ankoView({ SwipeRefreshLayout(it) }, theme, init)
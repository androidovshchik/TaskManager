/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

@file:Suppress("unused")

package defpackage.taskmanager.extensions

import android.net.Uri

fun String.toUri(): Uri = Uri.parse(this)

fun String.toFileUri(): Uri = Uri.parse("file://$this")
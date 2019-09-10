/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

package defpackage.taskmanager

import android.Manifest
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter

val DANGER_PERMISSIONS = arrayOf(
    Manifest.permission.WRITE_EXTERNAL_STORAGE
)

/**
 * Date&Time
 */

const val FORMAT_DATE = "dd.MM.yyyy"

const val FORMAT_TIME = "HH:mm"

const val FORMAT_DATETIME = "$FORMAT_DATE $FORMAT_TIME"

val PATTERN_DATE: DateTimeFormatter = DateTimeFormat.forPattern(FORMAT_DATE)

val PATTERN_TIME: DateTimeFormatter = DateTimeFormat.forPattern(FORMAT_TIME)

val PATTERN_DATETIME: DateTimeFormatter = DateTimeFormat.forPattern(FORMAT_DATETIME)

/**
 * Extras
 */

const val EXTRA_TASK = "extra_task"

const val EXTRA_RESULT = "extra_result"

const val EXTRA_RECEIVER = "extra_receiver"

const val EXTRA_ID = "extra_id"

const val EXTRA_TITLE = "extra_title"
/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

package defpackage.taskmanager

import android.Manifest
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import java.text.SimpleDateFormat
import java.util.*

val DANGER_PERMISSIONS = arrayOf(
    Manifest.permission.WRITE_EXTERNAL_STORAGE
)

const val QUERY_LIMIT = 200

const val ACTION_UNKNOWN = 0

const val ACTION_COMPLETED = 10

const val ACTION_DEFERRED = 20

const val ACTION_CANCELLED = 30

/**
 * Date&Time
 */

const val FORMAT_DATE = "dd.MM.yyyy"

const val FORMAT_TIME = "HH:mm"

const val FORMAT_DATETIME = "$FORMAT_DATE $FORMAT_TIME"

val PATTERN_DATE: DateTimeFormatter = DateTimeFormat.forPattern(FORMAT_DATE)

val PATTERN_TIME: DateTimeFormatter = DateTimeFormat.forPattern(FORMAT_TIME)

val PATTERN_DATETIME: DateTimeFormatter = DateTimeFormat.forPattern(FORMAT_DATETIME)

val SIMPLE_DATE: SimpleDateFormat = SimpleDateFormat(FORMAT_DATE, Locale.US)

val SIMPLE_TIME: SimpleDateFormat = SimpleDateFormat(FORMAT_TIME, Locale.US)

val SIMPLE_DATETIME: SimpleDateFormat = SimpleDateFormat(FORMAT_DATETIME, Locale.US)

/**
 * Extras
 */

// dummy extra
const val EXTRA_NONE = "extra_none"

const val EXTRA_ID = "extra_id"

const val EXTRA_TITLE = "extra_title"

const val EXTRA_ACTION = "extra_action"
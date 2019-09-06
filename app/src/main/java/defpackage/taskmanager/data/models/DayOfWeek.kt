/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

package defpackage.taskmanager.data.models

import java.util.*

enum class DayOfWeek(val id: Long) {
    MONDAY(1),
    TUESDAY(2),
    WEDNESDAY(3),
    THURSDAY(4),
    FRIDAY(5),
    SATURDAY(6),
    SUNDAY(7);

    fun toCalendarDay(): Int = when (this) {
        MONDAY -> Calendar.MONDAY
        TUESDAY -> Calendar.TUESDAY
        WEDNESDAY -> Calendar.WEDNESDAY
        THURSDAY -> Calendar.THURSDAY
        FRIDAY -> Calendar.FRIDAY
        SATURDAY -> Calendar.SATURDAY
        SUNDAY -> Calendar.SUNDAY
    }

    companion object {

        @JvmStatic
        private val map = values().associateBy(DayOfWeek::id)

        @JvmStatic
        fun fromId(value: Long?): DayOfWeek? = value?.let { map[value] }
    }
}
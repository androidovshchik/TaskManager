/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

package defpackage.taskmanager.data.local

import androidx.room.TypeConverter
import defpackage.taskmanager.data.models.DayOfWeek
import defpackage.taskmanager.data.models.Signal
import org.joda.time.LocalDate
import org.joda.time.LocalTime

object Converters {

    @TypeConverter
    fun toLocalTime(value: String?): LocalTime? {
        value?.trim()?.let {
            // todo check
            return LocalTime.parse(value)
        }
        return null
    }

    @TypeConverter
    fun toLocalDate(value: String?): LocalDate? {
        value?.trim()?.let {
            // todo check
            return LocalDate.parse(value)
        }
        return null
    }

    @TypeConverter
    fun toDayOfWeek(value: Long?): DayOfWeek? = DayOfWeek.fromId(value)

    @TypeConverter
    fun toSignal(value: Long?): Signal? = Signal.fromId(value)
}
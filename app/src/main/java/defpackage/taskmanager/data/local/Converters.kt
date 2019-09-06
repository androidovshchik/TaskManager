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
        // todo check
        value?.trim()?.let {
            return LocalTime.parse(value)
        }
        return null
    }

    @TypeConverter
    fun fromLocalTime(value: LocalTime?): String? {
        // todo check
        value?.toString()
        return null
    }

    @TypeConverter
    fun toLocalDate(value: String?): LocalDate? {
        // todo check
        value?.trim()?.let {
            return LocalDate.parse(value)
        }
        return null
    }

    @TypeConverter
    fun fromLocalDate(value: LocalDate?): String? {
        // todo check
        value?.toString()
        return null
    }

    @TypeConverter
    fun toDayOfWeek(value: Long?): DayOfWeek? = DayOfWeek.fromId(value)

    @TypeConverter
    fun fromDayOfWeek(value: DayOfWeek?): Long? = value?.id

    @TypeConverter
    fun toSignal(value: Long?): Signal? = Signal.fromId(value)

    @TypeConverter
    fun fromSignal(value: Signal?): Long? = value?.id
}
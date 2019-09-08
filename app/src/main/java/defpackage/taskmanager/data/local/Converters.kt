/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

package defpackage.taskmanager.data.local

import androidx.room.TypeConverter
import defpackage.taskmanager.data.models.Behavior
import defpackage.taskmanager.data.models.DayOfWeek
import org.joda.time.LocalDate
import org.joda.time.LocalTime

@Suppress("unused")
object Converters {

    private const val ISO8601 = "yyyy-MM-dd HH:mm:ss"

    @TypeConverter
    fun toLocalDate(value: String?): LocalDate? {
        // todo check
        return value?.trim()?.let {
            return LocalDate.parse(value)
        }
    }

    @TypeConverter
    fun fromLocalDate(value: LocalDate?): String? = value?.toString(ISO8601.split(" ")[0])

    @TypeConverter
    fun toLocalTime(value: String?): LocalTime? {
        // todo check
        return value?.trim()?.let {
            return LocalTime.parse(value)
        }
    }

    @TypeConverter
    fun fromLocalTime(value: LocalTime?): String? = value?.toString(ISO8601.split(" ")[1])

    @TypeConverter
    fun toDayOfWeek(value: Long?): DayOfWeek? = DayOfWeek.fromId(value)

    @TypeConverter
    fun fromDayOfWeek(value: DayOfWeek?): Long? = value?.id

    @TypeConverter
    fun toBehavior(value: Long?): Behavior? = Behavior.fromId(value)

    @TypeConverter
    fun fromBehavior(value: Behavior?): Long? = value?.id
}
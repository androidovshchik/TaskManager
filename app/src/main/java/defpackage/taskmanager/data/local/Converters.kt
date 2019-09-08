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
    @JvmStatic
    fun toLocalDate(value: String?): LocalDate? {
        // todo check
        return value?.trim()?.let {
            return LocalDate.parse(value)
        }
    }

    @TypeConverter
    @JvmStatic
    fun fromLocalDate(value: LocalDate?): String? = value?.toString(ISO8601.split(" ")[0])

    @TypeConverter
    @JvmStatic
    fun toLocalTime(value: String?): LocalTime? {
        // todo check
        return value?.trim()?.let {
            return LocalTime.parse(value)
        }
    }

    @TypeConverter
    @JvmStatic
    fun fromLocalTime(value: LocalTime?): String? = value?.toString(ISO8601.split(" ")[1])

    @TypeConverter
    @JvmStatic
    fun toDayOfWeek(value: Long?): DayOfWeek? = DayOfWeek.fromId(value)

    @TypeConverter
    @JvmStatic
    fun fromDayOfWeek(value: DayOfWeek?): Long? = value?.id

    @TypeConverter
    @JvmStatic
    fun toBehavior(value: Long?): Behavior? = Behavior.fromId(value)

    @TypeConverter
    @JvmStatic
    fun fromBehavior(value: Behavior?): Long? = value?.id
}
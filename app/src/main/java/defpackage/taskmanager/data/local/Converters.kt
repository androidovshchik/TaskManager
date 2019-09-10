/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

package defpackage.taskmanager.data.local

import androidx.room.TypeConverter
import com.elvishew.xlog.XLog
import defpackage.taskmanager.PATTERN_DATE
import defpackage.taskmanager.PATTERN_TIME
import defpackage.taskmanager.data.models.Behavior
import defpackage.taskmanager.data.models.DayOfWeek
import org.joda.time.LocalDate
import org.joda.time.LocalTime

@Suppress("unused")
object Converters {

    private val DELIMITER_REGEX = "[^0-9]".toRegex()

    @TypeConverter
    @JvmStatic
    fun toLocalDate(value: String?): LocalDate? {
        return try {
            value?.trim()?.let { text ->
                val n = text.split(DELIMITER_REGEX)
                    .map { it.toInt() }
                require(n[1] in 1..12 && (n[0] > 2000 && n[2] in 1..31 || n[0] in 1..31 && n[2] > 2000))
                LocalDate.parse(
                    when {
                        n[0] > 2000 -> String.format("%02d.%02d.%04d", n[2], n[1], n[0])
                        else -> String.format("%02d.%02d.%04d", n[0], n[1], n[2])
                    }, PATTERN_DATE
                )
            }
        } catch (e: Exception) {
            XLog.e(e.localizedMessage, e)
            null
        }
    }

    @TypeConverter
    @JvmStatic
    fun fromLocalDate(value: LocalDate?): String? = value?.toString(PATTERN_DATE)

    @TypeConverter
    @JvmStatic
    fun toLocalTime(value: String?): LocalTime? {
        return try {
            value?.trim()?.let { text ->
                val n = text.split(DELIMITER_REGEX)
                    .map { it.toInt() }
                LocalTime.parse(
                    when {
                        n.size == 1 && n[0] in 0..59 -> String.format("00:%02d", n[0], n[1])
                        n[0] == 24 && n[1] == 0 -> "00:00"
                        else -> String.format("%02d:%02d", n[0], n[1])
                    }, PATTERN_TIME
                )
            }
        } catch (e: Exception) {
            XLog.e(e.localizedMessage, e)
            null
        }
    }

    @TypeConverter
    @JvmStatic
    fun fromLocalTime(value: LocalTime?): String? = value?.toString(PATTERN_TIME)

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
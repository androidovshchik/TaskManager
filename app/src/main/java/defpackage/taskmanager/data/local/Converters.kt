/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

package defpackage.taskmanager.data.local

import android.text.TextUtils
import androidx.room.TypeConverter
import defpackage.taskmanager.data.models.DayOfWeek
import defpackage.taskmanager.data.models.Signal
import org.joda.time.LocalDate
import org.joda.time.LocalTime

object Converters {

    private val dateRegex = Regex("c|d")

    private val timeRegex = Regex("c|d")

    private val datetimeRegex = Regex("c|d")

    private val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)

    @TypeConverter
    fun datetimeToDate(value: String?): Date? {
        value?.trim()?.let {
            try {
                val datetime = when {
                    value.length in 1..2 && TextUtils.isDigitsOnly(value) -> String.format(
                        Locale.US,
                        "00:00:%02d",
                        value.toInt()
                    )
                    else -> return null
                }
                return formatter.parse()
            } catch (e: Exception) {
            }
        }
        return null
    }

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
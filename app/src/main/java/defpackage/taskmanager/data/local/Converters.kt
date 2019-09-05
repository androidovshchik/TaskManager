package defpackage.taskmanager.data.local

import androidx.room.TypeConverter
import defpackage.taskmanager.data.models.Day
import defpackage.taskmanager.data.models.Signal
import java.text.ParseException
import java.util.*

object Converters {

    internal var df: DateFormat = SimpleDateFormat(Constants.TIME_STAMP_FORMAT)

    @TypeConverter
    fun fromTimestamp(value: String?): Date? {
        if (value != null) {
            try {
                return df.parse(value)
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            return null
        } else {
            return null
        }
    }

    @TypeConverter
    fun fromId(value: Long?): Day? = Day.fromId(value)

    @TypeConverter
    fun fromId(value: Long?): Signal? = Signal.fromId(value)
}
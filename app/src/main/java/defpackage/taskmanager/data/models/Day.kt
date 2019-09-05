package defpackage.taskmanager.data.models

enum class Day(val id: Long) {
    MONDAY(1),
    TUESDAY(2),
    WEDNESDAY(3),
    THURSDAY(4),
    FRIDAY(5),
    SATURDAY(6),
    SUNDAY(7);

    companion object {

        @JvmStatic
        private val map = values().associateBy(Day::id)

        @JvmStatic
        fun fromId(value: Long?): Day? = value?.let { map[value] }
    }
}
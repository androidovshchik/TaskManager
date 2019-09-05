package defpackage.taskmanager.data.models

enum class Signal(val id: Long) {
    SOUNDLESS(1),
    VIBRATION(2),
    SOUND(3),
    DEFAULT(4);

    companion object {

        @JvmStatic
        private val map = values().associateBy(Signal::id)

        @JvmStatic
        fun fromId(value: Long?): Signal? = value?.let { map[value] }
    }
}
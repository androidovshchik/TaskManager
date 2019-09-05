/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

package defpackage.taskmanager

import android.app.Application
import androidx.room.Room
import com.rapid.removebg.data.local.AppDatabase
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import java.util.logging.Logger

class MainApplication : Application(), KodeinAware {

    override val kodein by Kodein.lazy {
        import(androidXModule(this@DemoApplication))

        bind() from instance(Logger())

        import(electricHeaterModule)

        bind<Coffee>() with provider {
            Room.databaseBuilder(context, AppDatabase::class.java, "app.db").build()
        }

        // this is bound in the scope of an activity so any retrieval using the same activity will return the same Kettle instance
        bind<Kettle<Coffee>>() with scoped(WeakContextScope.of<Activity>()).singleton {
            Kettle<Coffee>(
                instance(),
                instance(),
                instance(),
                provider()
            )
        }
    }

    override fun onCreate() {
        super.onCreate()
        val k = kodein
        println(k)
    }
}
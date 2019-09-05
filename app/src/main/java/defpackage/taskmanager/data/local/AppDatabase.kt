package com.rapid.removebg.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.rapid.removebg.data.models.MyPayment
import defpackage.taskmanager.data.local.Converters

@Database(entities = [MyPayment::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun myPaymentDao(): MyPaymentDao
}
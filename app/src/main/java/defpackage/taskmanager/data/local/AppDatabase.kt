package com.rapid.removebg.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rapid.removebg.data.models.MyPayment

@Database(entities = [MyPayment::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun myPaymentDao(): MyPaymentDao
}
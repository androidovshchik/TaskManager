package com.rapid.removebg.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.rapid.removebg.data.models.MyPayment

@Dao
interface MyPaymentDao {

    @Query("SELECT * FROM payments WHERE is_processed = 0 AND account_uid = :accountUid")
    fun getNotProcessed(accountUid: String): List<MyPayment>

    @Insert
    fun insert(vararg myPayments: MyPayment)

    @Update
    fun update(vararg myPayments: MyPayment)
}
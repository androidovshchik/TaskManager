package defpackage.taskmanager.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import java.io.Serializable

@Entity(tableName = "payments")
class Task : Serializable {

    @PrimaryKey(autoGenerate = true)
    var id: Long? = null

    @ColumnInfo(name = "account_uid")
    var accountUid = ""

    @field:Json(name = "product_id")
    @ColumnInfo(name = "product_id")
    var productId = ""

    @field:Json(name = "purchase_token")
    @ColumnInfo(name = "purchase_token")
    var purchaseToken = ""

    @ColumnInfo(name = "response_data")
    var responseData = ""

    @ColumnInfo(name = "signature")
    var signature = ""

    @ColumnInfo(name = "is_processed")
    var isProcessed = false

    override fun toString(): String {
        return "Task(id=$id, accountUid='$accountUid', productId='$productId', purchaseToken='$purchaseToken', responseData='$responseData', signature='$signature', isProcessed=$isProcessed)"
    }
}
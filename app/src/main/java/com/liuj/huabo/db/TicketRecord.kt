package com.liuj.huabo.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ticket_check_record")
class TicketRecord {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    @ColumnInfo(name = "uuid")
    var uuid: String? = null

    @ColumnInfo(name = "timeStamp")
    var timeStamp: Long = 0

    //yyyy-mm-dd
    @ColumnInfo(name = "date")
    var date : String?=null

    // Y 接口调用成功   N 没调用接口   E 接口调用错误
    @ColumnInfo(name = "status")
    var status: String ?=null

}
package com.liuj.huabo.db

import androidx.room.*
import androidx.room.OnConflictStrategy.*


@Dao
interface TicketRecordDao {

    @Insert(onConflict = REPLACE)
    fun addRecord(vararg records: TicketRecord)

    //通过实体的主键进行update
    @Update()
    fun updateRecord(vararg records : TicketRecord)

    @Delete
    fun deleteRecord(vararg records : TicketRecord)

    @Query("DELETE FROM ticket_check_record where 1")
    fun deleteAllRecord()

    @Query("SELECT * FROM ticket_check_record")
    fun loadAllRecord():Array<TicketRecord>

    @Query("SELECT * FROM ticket_check_record WHERE id = :id")
    fun loadRecordById( id : Int ):Array<TicketRecord>

}
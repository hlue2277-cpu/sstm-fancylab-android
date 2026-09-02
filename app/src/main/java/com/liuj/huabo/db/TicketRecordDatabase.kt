package com.liuj.huabo.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [TicketRecord::class], version = 1, exportSchema = false)
abstract class TicketRecordDatabase : RoomDatabase() {
    abstract val ticketRecordDao: TicketRecordDao

    companion object {
        private var ticketRecordDatabase: TicketRecordDatabase? = null
        fun getTicketRecordDatabase(context: Context): TicketRecordDatabase? {
            if (ticketRecordDatabase == null) {
                synchronized(TicketRecordDatabase::class.java) {
                    if (ticketRecordDatabase == null) {
                        ticketRecordDatabase = Room.databaseBuilder(context.applicationContext, TicketRecordDatabase::class.java,
                                "huabo_db")
                                .allowMainThreadQueries()
                                .build()
                    }
                }
            }
            return ticketRecordDatabase
        }
    }
}
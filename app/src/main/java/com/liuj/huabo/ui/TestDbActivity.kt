package com.liuj.huabo.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.liuj.huabo.R
import com.liuj.huabo.db.TicketRecord
import com.liuj.huabo.db.TicketRecordDatabase

class TestDbActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_db)

        var ticketRecordDao = TicketRecordDatabase.getTicketRecordDatabase(this)!!.ticketRecordDao

        ticketRecordDao.deleteAllRecord()

        var ticketRecord = TicketRecord()
        ticketRecord.timeStamp = System.currentTimeMillis()
        ticketRecord.uuid = "13965026764"
        ticketRecord.date = "2021-04-20"
        ticketRecord.status = "N"
        ticketRecordDao.addRecord(ticketRecord)

        var ticketRecord2 = TicketRecord()
        ticketRecord2.timeStamp = System.currentTimeMillis()
        ticketRecord2.uuid = "13965026764"
        ticketRecord2.date = "2021-04-20"
        ticketRecord2.status = "Y"
        ticketRecordDao.addRecord(ticketRecord2)


        var ticketRecord3 = TicketRecord()
        ticketRecord3.timeStamp = System.currentTimeMillis()
        ticketRecord3.uuid = "13965026764 update 222222"
        ticketRecord3.id = 2
        ticketRecord3.date = "2021-04-20"
        ticketRecord3.status = "Y"
        ticketRecordDao.updateRecord(ticketRecord3)

//        ticketRecordDao.deleteRecord(ticketRecord3)

        ticketRecordDao.loadAllRecord().forEach {
            Log.e("room", "==query==${it.id},${it.uuid},${it.timeStamp},${it.date},${it.status}")
        }

    }




}
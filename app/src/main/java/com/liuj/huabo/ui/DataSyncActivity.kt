package com.liuj.huabo.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import cn.ycbjie.ycstatusbarlib.bar.StateAppBar
import com.liuj.huabo.R
import com.liuj.huabo.db.TicketRecord
import com.liuj.huabo.db.TicketRecordByDay
import com.liuj.huabo.db.TicketRecordDao
import com.liuj.huabo.db.TicketRecordDatabase
import com.liuj.huabo.util.AsyncTaskManager
import com.wega.library.loadingDialog.LoadingDialog
import com.wgke.adapter.cell.Cell
import com.wgke.adapter.cell.CellAdapter
import com.wgke.adapter.cell.MultiCell
import kotlinx.android.synthetic.main.activity_data_sync.*
import kotlinx.android.synthetic.main.item_data_to_sync.view.*

class DataSyncActivity : AppCompatActivity() {


    private val cells = ArrayList<Cell>()

    private var mList = ArrayList<TicketRecordByDay>()

    var ticketRecordDao: TicketRecordDao? = null
    var loadingDialog: LoadingDialog? = null

    var data = ArrayList<TicketRecordByDay>()

    private lateinit var cellAdapter: CellAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_sync)
        StateAppBar.setStatusBarColor(this, ContextCompat.getColor(this, R.color.mainActivityStatusBarColor))
        ticketRecordDao = TicketRecordDatabase.getTicketRecordDatabase(this)!!.ticketRecordDao
        cellAdapter = CellAdapter(this)
        rvSync.adapter = cellAdapter
        rvSync.layoutManager = LinearLayoutManager(this)

        initLoad()

        tv_confirm_sync.setOnClickListener {
            loadingDialog!!.show()
            loadingDialog!!.loading()
            Handler().postDelayed(Runnable {
                loadingDialog!!.loadSuccess()
            }, 3000)
        }


        AsyncTaskManager.getInstance().executeTask {
            data.clear()
            ticketRecordDao!!.loadAllRecord().map {
                var ticketRecordByDay = TicketRecordByDay()
                ticketRecordByDay.day = it.date
                if (data.contains(ticketRecordByDay)) {
                    var ticketRecordByDayOld = data[data.indexOf(ticketRecordByDay)]
                    ticketRecordByDayOld.records.add(it)
                } else {
                    var itemList = ArrayList<TicketRecord>()
                    itemList.add(it)
                    ticketRecordByDay.records = itemList
                    data.add(ticketRecordByDay)
                }
                data.sortWith(Comparator { o1, o2 -> ((o2.records[0].timeStamp / 10000) - o1.records[0].timeStamp / 10000).toInt() })
                fillData(data, cells)
            }
        }

    }

    private fun fillData(ticketFolders: ArrayList<TicketRecordByDay>, cells: ArrayList<Cell>) {
        cells.clear()
        for (ticketByDay in ticketFolders) {
            cells.add(MultiCell.convert(R.layout.item_data_to_sync, ticketByDay, { h, c ->
                h.itemView.tv_date.text = ticketByDay.day
                h.itemView.tv_count.text = "${ticketByDay.records.size}人"
            }))
        }
        runOnUiThread {
            cellAdapter.dataList = cells
            cellAdapter.notifyDataSetChanged()
        }
    }

    public fun initLoad() {
        loadingDialog = LoadingDialog.Builder(this).setLoading_text("同步中...").setFail_text("同步失败！！！").setSuccess_text("同步成功")
                .create()
    }

}
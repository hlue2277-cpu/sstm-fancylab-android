package com.liuj.huabo.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.alibaba.fastjson.JSONObject
import com.blankj.utilcode.util.ToastUtils
import com.bumptech.glide.Glide
import com.liuj.huabo.R
import com.liuj.huabo.api.net.HttpEngine
import com.liuj.huabo.api.net.RequestConfig
import com.liuj.huabo.api.net.callback.BaseResponseBean
import com.liuj.huabo.api.net.callback.JSONCallback
import com.liuj.huabo.bean.CheckTicketResultBean
import com.liuj.huabo.bean.Display
import com.liuj.huabo.common.Constants
import com.liuj.huabo.db.TicketRecord
import com.liuj.huabo.db.TicketRecordDao
import com.liuj.huabo.db.TicketRecordDatabase
import com.liuj.huabo.util.DateUtils
import com.liuj.huabo.util.DensityUtil
//import com.liuj.huabo.util.IDServiceUtil
import com.liuj.huabo.util.ScanUtil
import com.liuj.huabo.util.UIUtil
import com.scandecode.ScanDecode
import com.scandecode.inf.ScanInterface
import kotlinx.android.synthetic.main.item_check_ticket_result.view.*
import kotlinx.android.synthetic.main.item_check_ticket_result_three.view.*
import kotlinx.android.synthetic.main.item_check_ticket_result_two.view.*
import kotlinx.android.synthetic.main.layout_check_ticket_pass.*
import okhttp3.Call
import org.jetbrains.anko.startActivity
import java.io.IOException

class CheckResultActivity : AppCompatActivity() {


    var checkTicketResultBean: CheckTicketResultBean? = null
    private var mType = 0 // 0 纸质票  1 身份证   3 人脸检票过来的

    private var ticketRecordDao: TicketRecordDao? = null

    public var initFlag = false



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_result)
        ticketRecordDao = TicketRecordDatabase.getTicketRecordDatabase(this)!!.ticketRecordDao
        checkTicketResultBean = intent.getSerializableExtra("result") as CheckTicketResultBean

        mType = intent.getIntExtra("type", 0)


        //继续检票
        continue_check_ticket.setOnClickListener {
            finish()
        }
        //人脸补录
        tv_face_bulu.setOnClickListener {
            startActivity<FaceDetectActivity>(
                FaceDetectActivity.SOURCE to 1,
                FaceDetectActivity.UUID to checkTicketResultBean!!.uuid
            )
        }
        //取消核验
        tv_cancel_hexiao.setOnClickListener {
            finish()
        }
        //确认核销
        tv_confirm_hexiao.setOnClickListener {
            checkTicket(checkTicketResultBean!!.uuid, true)
        }
        initView()

    }

    override fun onResume() {
        super.onResume()

        initFlag = true
    }


    override fun onPause() {
        super.onPause()
        ScanUtil.getInstance()!!.stopScan()
    }

    private fun initView() {
        when (mType) {
            0 -> {
                ll_check.visibility = View.VISIBLE
                ll_recheck.visibility = View.GONE
            }
            // 人脸检票跳过来的接口
            3 -> {
                ll_check.visibility = View.VISIBLE
                ll_recheck.visibility = View.GONE
            }

            1 -> {
                ll_check.visibility = View.GONE
                ll_recheck.visibility = View.VISIBLE
            }

            else -> {
                ll_check.visibility = View.GONE
                ll_recheck.visibility = View.VISIBLE
            }
        }

        if (checkTicketResultBean != null) {
            showCheckResult(checkTicketResultBean!!)
        }
    }

    private fun showCheckResult(bean: CheckTicketResultBean) {
        //1 success 0 fail
        if (bean.status == 1) {
//            rl_title_bg.setBackgroundColor(Color.parseColor("#06BD85"))
            //检票成功 显示人脸补录
            tv_face_bulu.visibility = View.VISIBLE
        } else {
//            rl_title_bg.setBackgroundColor(Color.parseColor("#E57C6D"))
            //检票失败则不显示人脸补录
            tv_face_bulu.visibility =
                if (TextUtils.isEmpty(checkTicketResultBean?.lastCheckTime)) View.GONE else View.VISIBLE
        }
        if (!Constants.supportAddface || bean.printType == "movie") {
            tv_face_bulu.visibility = View.GONE
        }
        /*
        if(bean.display==null || bean.display.title == null) return
        bean?.display?.title?.left?.run {
            tv_ticket_status.setStyle( this)
        }
        bean?.display?.title?.background.run {
            rl_title_bg.setBackgroundColor(Color.parseColor(this))
        }
         */
        tv_ticket_status.setStyle(bean.display.title.left)
        rl_title_bg.setBackgroundColor(Color.parseColor(bean.display.title.background))


        ll_container.removeAllViews()
        /*
        for (detail in bean.display.details) {
            lateinit var itemView: View
            when (detail.type) {
                1 -> {
                    itemView =
                        LayoutInflater.from(this).inflate(R.layout.item_check_ticket_result, null)
                    detail?.left?.run {
                        itemView.tv_title.setStyle(this)
                    }
                }
                2 -> {
                    itemView = LayoutInflater.from(this)
                        .inflate(R.layout.item_check_ticket_result_two, null)
                    detail?.left?.run {
                        itemView.tv_title_left.setStyle(this)
                    }
                    detail?.right?.run {
                        itemView.tv_title_right.setStyle(this)
                    }

                }
                3 -> {
                    itemView = LayoutInflater.from(this)
                        .inflate(R.layout.item_check_ticket_result_three, null)
                    detail?.url?.run {
                        Glide.with(this@CheckResultActivity).load(this).into(itemView.iv_pic)
                    }
                }
            }
            ll_container.addView(itemView)
        }
         */
        for (detail in bean.display.details) {
            lateinit var itemView: View
            when (detail.type) {
                1 -> {
                    itemView =
                            LayoutInflater.from(this).inflate(R.layout.item_check_ticket_result, null)
                    itemView.tv_title.setStyle(detail.left)
                }
                2 -> {
                    itemView = LayoutInflater.from(this)
                            .inflate(R.layout.item_check_ticket_result_two, null)
                    itemView.tv_title_left.setStyle(detail.left)
                    itemView.tv_title_right.setStyle(detail.right)
                }
                3 -> {
                    itemView = LayoutInflater.from(this)
                            .inflate(R.layout.item_check_ticket_result_three, null)
                    //Glide.with(this).load(detail.url).into(itemView.iv_pic)
                }
            }
            ll_container.addView(itemView)
        }
    }


    /**
     * 检票
     */
    private fun checkTicket(uuid: String, hexiao: Boolean) {
        var param = JSONObject()
        param["uuid"] = uuid
        var certType= UIUtil.getLastCertType()//2026.8.7
        param["certificateType"] = certType  // 新增//2026.8.7
        HttpEngine.post(if (hexiao) RequestConfig.Url.HE_XIAO else RequestConfig.Url.CHECK_TICKET)
            .params(param).execute(object : JSONCallback() {
                override fun onSuccess(baseResponseBean: BaseResponseBean) {
                    recordTicketCheck(uuid, "Y")
                    var bean = JSONObject.parseObject(
                        baseResponseBean.data,
                        CheckTicketResultBean::class.java
                    )
                    if (bean != null) {
                        bean.msg = baseResponseBean.msg
                        bean.status = 1
                        bean.uuid = uuid
                        mType =if(bean.needConfirm == "Y")  1 else 0
                        checkTicketResultBean = bean
                        initView()
                    }
                }

                override fun onFailed(bean: BaseResponseBean) {
                    super.onFailed(bean)
                    recordTicketCheck(uuid, "Y")
                    if (!bean.success) {
                        var error =
                            JSONObject.parseObject(bean.data, CheckTicketResultBean::class.java)
                        if (error == null) {
                            error = CheckTicketResultBean()
                        }
                        error.status = 0
                        error.msg = bean.msg
                        error.uuid = uuid
                        checkTicketResultBean = error
                        mType = if(error.needConfirm == "Y")  1 else 0
                        initView()
                    }
                }

                override fun onError(call: Call?, e: IOException?) {
                    super.onError(call, e)
                    recordTicketCheck(uuid, "Y")
                }
            })
    }


    private fun recordTicketCheck(uuid: String, status: String) {
        var record = TicketRecord()
        record.timeStamp = System.currentTimeMillis()
        record.status = status
        record.uuid = uuid
        record.date = DateUtils.currentDateStr()
        ticketRecordDao?.addRecord(record)
        ticketRecordDao?.loadAllRecord()?.forEach {
            Log.e("room", "==query==${it.id},${it.uuid},${it.timeStamp},${it.date},${it.status}")
        }
    }


}

public fun TextView.setStyle(display: Display.DisplayItem) {
    this.textSize = display.fontSize.toFloat()
    this.paint.isFakeBoldText = display.bold
    this.text = display.text
}
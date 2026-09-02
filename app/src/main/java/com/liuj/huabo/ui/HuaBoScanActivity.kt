package com.liuj.huabo.ui

import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.marginRight
import com.alibaba.fastjson.JSONObject
import com.blankj.utilcode.util.ToastUtils
import com.hxdl.lib_qrcode.test.QrCodeScanActivity
import com.liuj.huabo.api.net.HttpEngine
import com.liuj.huabo.api.net.RequestConfig
import com.liuj.huabo.api.net.callback.BaseResponseBean
import com.liuj.huabo.api.net.callback.JSONCallback
import com.liuj.huabo.bean.BaseQueryBatResDataBean
import com.liuj.huabo.bean.CheckTicketResultBean
import com.liuj.huabo.db.TicketRecord
import com.liuj.huabo.db.TicketRecordDao
import com.liuj.huabo.db.TicketRecordDatabase
import com.liuj.huabo.util.*
import com.liuj.huabo.widget.BottomFIllDataDialog
import com.scandecode.ScanDecode
import com.scandecode.inf.ScanInterface
import com.spd.id2.impl.CardInfo
import com.spd.id2.impl.IIdReadCallBack
import com.spd.id2.impl.IdManager
//import com.spd.id2.impl.PowerEntity

//import com.speedata.libid2.IDManager
//import com.speedata.libid2.IID2Service

import com.speedata.utils.ProgressDialogUtils
import okhttp3.Call
import org.jetbrains.anko.startActivity
import java.io.IOException
import java.util.List

class HuaBoScanActivity : QrCodeScanActivity(), IIdReadCallBack {

    private var mReceiver: BroadcastReceiver? = null


    private var source = 0; //0 online   1 offline

    private var ticketRecordDao: TicketRecordDao? = null

    private var iCurBatCheckOrNot = 0//HHJT2023
    private var ChkRtJS: String? = null
    private var ChkRtShow: String? = null
    private var timeStamp = System.currentTimeMillis()
    //private var lastCalltimeStamp = System.currentTimeMillis()
    //private var bBatShowed=false

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        iCurBatCheckOrNot=UIUtil.getBatCheckOrNot()
        PlaySoundUtils.initSoundPool(this)
        ticketRecordDao = TicketRecordDatabase.getTicketRecordDatabase(this)!!.ticketRecordDao
        source = intent.getIntExtra("source", 0)

        mTvIDFace.visibility = if (source == 1) View.GONE else View.VISIBLE
        mTvDataSync.visibility = if (source == 0) View.GONE else View.VISIBLE
        mTvDataSync.setOnClickListener {
            startActivity<DataSyncActivity>()
        }

        mTvManual.setOnClickListener {
            var dialog = BottomFIllDataDialog(this)
            dialog.setType(currentMode)
            dialog.setCallBack {
//                ToastUtils.showShort(it)
                //scanResult(it)
                //2026.8.7
                if(iCurBatCheckOrNot==0)
                    //checkTicket(it,"ManualInput")
                    checkTicket(it,"3")
                else {
                    //UIUtil.setLastCertType("ManualInput")
                    UIUtil.setLastCertType("3")
                    myqueryTicketBat(it)
                }
            }
            dialog.show()
        }

        mZXingView.visibility = View.VISIBLE
        mRlIDCard.visibility = View.GONE
        mTvPaperTicket.setOnClickListener {
            currentMode = 0
            mTvManual.text = "手动输入票号"
            mTvPaperTicket.setBackgroundColor(Color.parseColor("#06BD85"))
            mTvIDCard.setBackgroundColor(Color.parseColor("#292929"))
            mRlIDCard.visibility = View.GONE
            if (!currentScanFlag) {
                mZXingView.startCamera()
                mZXingView.startSpotAndShowRect()
                mZXingView.visibility = View.VISIBLE
            }
        }

        mTvIDCard.setOnClickListener {
            mZXingView.stopCamera()
            ScanUtil.getInstance().startScan()
            currentMode = 1
            mTvManual.text = "手动输入证件号"
            mTvPaperTicket.setBackgroundColor(Color.parseColor("#292929"))
            mTvIDCard.setBackgroundColor(Color.parseColor("#06BD85"))
            mZXingView.visibility = View.GONE
            mRlIDCard.visibility = View.VISIBLE
            //IDServiceUtil.getInstance().keepScan()
        }

        //扫描模式切换
        mSwitchScan.setOnClickListener {
            currentScanFlag = !currentScanFlag
            if (currentScanFlag) {
                mSwitchScan.text = "相机模式"
                mZXingView.stopCamera()
                mZXingView.visibility = View.GONE
            } else {
                mSwitchScan.text = "红外模式"
                mZXingView.startCamera()
                mZXingView.startSpotAndShowRect()
                mZXingView.visibility = View.VISIBLE
            }

        }

        mTvIDFace.setOnClickListener {
            startActivity<FaceDetectActivity>(FaceDetectActivity.SOURCE to 0)
        }
        //IDServiceUtil.getInstance().initID(this)
        //val id2Entity = PowerEntity("/dev/ttyS1", 115200, "NEW_MAIN_FG", java.util.List.of(165), "SD60")
        //IdManager.getInstance().initDev(id2Entity, this)
        IdManager.getInstance().initDev(getApplicationContext(), this)
    }


    override fun scanResult(result: String) {
        super.scanResult(result)
        if(iCurBatCheckOrNot==0)
            //checkTicket(result,"QRCode")
            checkTicket(result,"2")
        else {
            //UIUtil.setLastCertType("QRCode")
            UIUtil.setLastCertType("2")
            myqueryTicketBat(result)
        }
    }

    /**
     * 检票
     */
    private fun checkTicket(uuid: String, certType: String = "na") {

        //2026.8.7
        var CertType=certType
        if(CertType=="na") CertType=UIUtil.getLastCertType()
        //if(CertType=="") CertType="idcard"
        if(CertType=="") CertType="1"
        UIUtil.setLastCertType(CertType)

        if(iCurBatCheckOrNot==0) {//231225
            if (System.currentTimeMillis() - timeStamp > 1500) {
                timeStamp = System.currentTimeMillis()
            } else {
                return
            }
        }

        if (source == 1) {
            recordTicketCheck(uuid, "N")
            ToastUtils.showShort("检票成功")
            Handler().postDelayed({
                mZXingView.startSpotAndShowRect()
            }, 2000)
            return
        }

        var param = JSONObject()
        param["uuid"] = uuid
        param["certificateType"] = CertType  // 新增//2026.8.7
        HttpEngine.post(RequestConfig.Url.CHECK_TICKET).params(param).execute(object : JSONCallback() {
            override fun onSuccess(baseResponseBean: BaseResponseBean) {
                var bean = JSONObject.parseObject(baseResponseBean.data, CheckTicketResultBean::class.java)
                if (bean != null) {
                    bean.msg = baseResponseBean.msg
                    bean.status = 1
                    bean.uuid = uuid
                    recordTicketCheck(uuid, "Y")
                    startActivity<CheckResultActivity>("result" to bean, "type" to if(bean.needConfirm == "Y") 1 else 0)
                }
            }

            override fun onFailed(bean: BaseResponseBean) {
                super.onFailed(bean)
                    recordTicketCheck(uuid, "E")
                    var error = JSONObject.parseObject(bean.data, CheckTicketResultBean::class.java)
                    if (error == null) {error = CheckTicketResultBean()}
                    error.status = if(bean.success) 1 else 0
                    error.msg = bean.msg
                    error.uuid = uuid
                    startActivity<CheckResultActivity>("result" to error, "type" to if(error.needConfirm == "Y") 1 else 0)
            }

            override fun onError(call: Call?, e: IOException?) {
                super.onError(call, e)
                recordTicketCheck(uuid, "E")
                ToastUtils.showLong("网络错误")
            }
        })
    }

    private fun showNormalDialog(sTitle: String, sMsg: String?) {
        val normalDialog = AlertDialog.Builder(this)
        //normalDialog.setIcon(R.drawable.icon_dialog);
        normalDialog.setTitle(sTitle)
        normalDialog.setMessage(sMsg)
        normalDialog.setPositiveButton("确定"
        ) { dialog, which ->
            //...To-do
        }
        /*normalDialog.setNegativeButton("关闭",
        new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //...To-do
            }
        });*/
        // 显示
        normalDialog.show()
    }

    fun myqueryTicketBat(uuid: String?): Int {
        /*
        if(System.currentTimeMillis() - timeStamp > 1500) {
            timeStamp = System.currentTimeMillis()
        }
        else{
            return -1
        }
         */
        //if(bBatShowed) return -1
        if(UIUtil.isForeground(this,this.localClassName)==false) return -1
        ChkRtJS = ""
        ChkRtShow = ""

        if(uuid==null) return -1
        if (source == 1) {
            recordTicketCheck(uuid, "N")
            ToastUtils.showShort("检票成功")
            Handler().postDelayed({
                mZXingView.startSpotAndShowRect()
            }, 2000)
            return 0
        }

        var iThisUUID:Int;//
        iThisUUID=-1
        var paramBat = JSONObject()
        //paramBat["certificateNo"] = encrypt2ToMD5(uuid)
        paramBat["certificateNo"] = uuid
        //paramBat["certificateNo"] = "142627199502120114"//暂时
        HttpEngine.post(RequestConfig.Url.QueryBat).params(paramBat).execute(object : JSONCallback() {
            override fun onSuccess(baseResponseBean: BaseResponseBean) {
                //ChkRtJS = baseResponseBean.toString();
                try {
                    if (baseResponseBean.errcode != "0000") {
                        ChkRtShow = baseResponseBean.msg
                        showNormalDialog("Err", ChkRtShow);
                        iThisUUID = -101;
                    } else {
                        try {
                            ChkRtJS = baseResponseBean.data
                            val bean: BaseQueryBatResDataBean = JSONObject.parseObject<Any>(ChkRtJS, BaseQueryBatResDataBean::class.java) as BaseQueryBatResDataBean
                            //ChkRtJS = bean.errorCode
                            val itktCnt: Int = bean.ticketInfo.size
                            iThisUUID = itktCnt;
                            ChkRtShow = baseResponseBean.msg
                            if(itktCnt>1) {
                                //bBatShowed=true
                                startActivity<BatCheck>("result" to ChkRtJS)
                                //bBatShowed=false
                            }
                            else{
                                if(itktCnt==1)
                                    checkTicket(uuid,"3")
                                else
                                    showNormalDialog("Err", uuid + "\r\n" + "暂无有效票信息");
                            }
                        } catch (eParaData: java.lang.Exception) {
                            ChkRtShow = eParaData.toString()
                            showNormalDialog("Err", ChkRtShow);
                            iThisUUID = -102;
                        }
                        ChkRtShow = ChkRtJS
                    }
                } catch (ejs: java.lang.Exception) {
                    ChkRtShow = "请求操作失败(3)"
                    showNormalDialog("Err", ejs.toString());
                    iThisUUID = -103;
                }
            }

            override fun onFailed(bean: BaseResponseBean) {
                showNormalDialog("提示", uuid + "\r\n" + bean.msg);
                ChkRtShow = bean.msg
                iThisUUID = -104;
            }

            override fun onError(call: Call?, e: IOException?) {
                showNormalDialog("提示", "网络异常");
                ChkRtShow = "网络异常"
                iThisUUID = -105;
            }
        })
        return iThisUUID
    }

    private fun recordTicketCheck(uuid: String, status: String) {
        var record = TicketRecord()
        record.timeStamp = System.currentTimeMillis()
        record.status = status
        record.uuid = uuid
        record.date = DateUtils.currentDateStr()
        ticketRecordDao?.addRecord(record)
        ticketRecordDao?.loadAllRecord()?.forEach {
            Log.d("room", "==query== id:${it.id} uuid:${it.uuid} timeStamp:${it.timeStamp} date:${it.date}, status:${it.status}")
        }
    }


    override fun onResume() {
        super.onResume()
        ScanUtil.getInstance()!!.getBarCode {

            if(iCurBatCheckOrNot==0)
                checkTicket(it,"2")
            else
                myqueryTicketBat(it)
        }
        ScanUtil.getInstance()!!.startScan()

    }


    override fun onPause() {
        super.onPause()
        ScanUtil.getInstance()!!.stopScan()

    }

    override fun onDestroy() {//241231

        IdManager.getInstance().releaseDev()
        super.onDestroy()
    }

    override fun onReadId2(cardInfo: CardInfo) {
        try {

            //checkTicket(cardInfo.cardNum,"1")

            if(iCurBatCheckOrNot==0)
                checkTicket(cardInfo.cardNum,"1")
            else
                myqueryTicketBat(cardInfo.cardNum)

        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }


//    private fun showResult(result: Boolean, msg: String) {
//        runOnUiThread {
//            ProgressDialogUtils.dismissProgressDialog()
//            if (!result) {
//                AlertDialog.Builder(this@HuaBoScanActivity).setCancelable(false).setMessage("二代证模块初始化失败,请前往工具中修改参数$msg")
//                        .setPositiveButton("确定") { dialogInterface, i ->
//                            //todo 失败相关UI操作
//                            openConfig()
//                        }.show()
//            } else {
//                showToast("初始化成功")
//            }
//        }
//    }

    /**
     * 打开调试工具  修改配置
     */
    private fun openConfig() {
        //打开失败去下载
        try {
            val intent = Intent()
            intent.action = "speedata.config"
            startActivity(intent)
        } catch (e: Exception) {
            //            downLoadDeviceApp();
            AlertDialog.Builder(this@HuaBoScanActivity).setCancelable(false).setMessage("请去应用市场下载思必拓调试工具进行配置")
                    .setPositiveButton("确定", null).show()
        }
    }

    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

}
package com.liuj.huabo.ui

import android.content.*
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import cn.ycbjie.ycstatusbarlib.bar.StateAppBar
import com.alibaba.fastjson.JSONObject
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.ToastUtils
import com.liuj.huabo.R
import com.liuj.huabo.api.net.HttpEngine
import com.liuj.huabo.api.net.RequestConfig
import com.liuj.huabo.api.net.callback.BaseResponseBean
import com.liuj.huabo.api.net.callback.JSONCallback
import com.liuj.huabo.bean.CheckTicketResultBean
import com.liuj.huabo.common.Constants
import com.liuj.huabo.util.AppInfoUtil
import com.liuj.huabo.util.PermissionUtil
import com.liuj.huabo.util.UIUtil
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity

class MainActivity : AppCompatActivity() {


    private var mReceiver: BroadcastReceiver? = null

    private var enterTrance = 0;  // 0 online  1 offline

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        StateAppBar.setStatusBarColor(this, ContextCompat.getColor(this, R.color.mainActivityStatusBarColor))

        ll_check_ticket_online.setOnClickListener {
            if (UIUtil.isValidClick()) {
                enterTrance = 0
                UIUtil.setBatCheckOrNot(0)
                PermissionUtil.getInstance().request(this, enterTrance)
            }
        }

        ll_check_ticket_offline.setOnClickListener {
            if (UIUtil.isValidClick()) {
                enterTrance = 1
                PermissionUtil.getInstance().request(this, enterTrance)
            }
        }

        ll_team_check.setOnClickListener {
            if (UIUtil.isValidClick()) {
                startActivity<TeamRecordActivity>()
            }
        }

        ll_batcheck.setOnClickListener {
            if (UIUtil.isValidClick()) {
                enterTrance = 0
                UIUtil.setBatCheckOrNot(1)
                //startActivity<BatCheck>()
                PermissionUtil.getInstance().request(this, enterTrance)
            }
        }

        ll_face_detect_exit.setOnClickListener {
            startActivity<FaceDetectActivity>(FaceDetectActivity.SOURCE to 4)
        }

        ll_face_check_twice.setOnClickListener {
            startActivity<FaceDetectActivity>(FaceDetectActivity.SOURCE to 0)
        }

        tv_logout.setOnClickListener {
            val builder: AlertDialog.Builder =
                AlertDialog.Builder(this).setIcon(R.mipmap.ic_launcher).setTitle(title)
                    .setMessage("是否退出登陆，并清空用户名和密码？").setPositiveButton("确认",
                        DialogInterface.OnClickListener { dialogInterface, i ->
                            SPUtils.getInstance().put("username", "")
                            SPUtils.getInstance().put("password", "")
                            finish()
                        })
                    .setNegativeButton("取消",DialogInterface.OnClickListener { dialogInterface, i -> dialogInterface.dismiss() })
                    .setCancelable(false)
            val alertDialog = builder.create()
            alertDialog.show()

        }

        //注册newland扫描枪
        mReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                Log.d("BroadcastReceiver", "broadcast")
                val scanResult_1 = intent.getStringExtra("SCAN_BARCODE1")
//                val scanResult_2 = intent.getStringExtra("SCAN_BARCODE2")
//                val barcodeType = intent.getIntExtra("SCAN_BARCODE_TYPE", -1) // -1:unknown
                val scanStatus = intent.getStringExtra("SCAN_STATE")
                if ("ok" == scanStatus && !scanResult_1.isNullOrBlank()) {
                    checkTicket(scanResult_1)
                    ToastUtils.showShort("$scanResult_1|$scanStatus")
                }
            }
        }


        setNewLandOutPutMode()

        tv_title.text = Constants.name

        tv_version.text = "v"+AppInfoUtil.getAppVersion()
    }


    override fun onResume() {
        super.onResume()
        registerReceiver()
    }


    override fun onPause() {
        super.onPause()
        unRegisterReceiver()
    }


    fun registerReceiver() {
        val mFilter = IntentFilter("nlscan.action.SCANNER_RESULT")
        registerReceiver(mReceiver, mFilter)
    }

    fun unRegisterReceiver() {
        try {
            unregisterReceiver(mReceiver);
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun setNewLandOutPutMode() {
        var intent = Intent("ACTION_BAR_SCANCFG")
        intent.putExtra("EXTRA_SCAN_MODE", 3)
        intent.putExtra("EXTRA_SCAN_AUTOEND", 1)
        sendBroadcast(intent)
    }


    fun testApi() {
        var param = JSONObject()
        param["uuid"] = "VCRzQzFLwtgSZkm4"
        HttpEngine.post(RequestConfig.Url.TEST).params(param).execute(object : JSONCallback() {
            override fun onSuccess(baseResponseBean: BaseResponseBean?) {

            }
        })
    }

    /**
     * 检票
     */
    private fun checkTicket(uuid: String) {
        var param = JSONObject()
        param["uuid"] = uuid
        HttpEngine.post(RequestConfig.Url.TEST).params(param).execute(object : JSONCallback() {
            override fun onSuccess(baseResponseBean: BaseResponseBean) {
                var bean = JSONObject.parseObject(baseResponseBean.data, CheckTicketResultBean::class.java)
                if (bean != null) {
                    bean.msg = baseResponseBean.msg
                    bean.status = 0
                    startActivity<CheckResultActivity>("result" to bean)

                }
            }

            override fun onFailed(bean: BaseResponseBean) {
                super.onFailed(bean)
                if (!bean.success) {
                    var error = JSONObject.parseObject(bean.data, CheckTicketResultBean::class.java)
                    if (error != null) {
                        error.status = 1
                        error.msg = bean.msg
                        startActivity<CheckResultActivity>("result" to error)
                    } else {
                        var checkTicketResultBean = CheckTicketResultBean()
                        checkTicketResultBean.status = 1
                        checkTicketResultBean.msg = bean.msg
                        startActivity<CheckResultActivity>("result" to checkTicketResultBean)
                    }
                }
            }
        })
//        HttpEngine.post(RequestConfig.Url.TEST).params(param).execute(object : NestCallback<CheckTicketResultBean>() {
//            override fun onSuccess(t: CheckTicketResultBean) {
//                t.status = 0
//                startActivity<CheckResultActivity>("result" to t)
//            }
//
//            override fun onFailed(bean: BaseResponseBean) {
//                super.onFailed(bean)
//                if(!bean.success){
//                    var error = JSONObject.parseObject(bean.data,CheckTicketResultBean::class.java)
//                    if(error!=null) {
//                        error.status = 1
//                        startActivity<CheckResultActivity>("result" to error)
//                    }else{
//                        var checkTicketResultBean = CheckTicketResultBean()
//                        checkTicketResultBean.status = 1
//                        startActivity<CheckResultActivity>("result" to checkTicketResultBean)
//                    }
//                }
//            }
//        })

    }




    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == 101 && data != null) {
            val uuid = data.getStringExtra("result")
            ToastUtils.showShort(uuid)
            if (!uuid.isNullOrBlank()) {
                checkTicket(uuid)
            }
        }
    }


}
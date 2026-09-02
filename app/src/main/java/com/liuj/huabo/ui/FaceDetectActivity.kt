package com.liuj.huabo.ui

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import com.alibaba.fastjson.JSONObject
import com.blankj.utilcode.util.ToastUtils
import com.liuj.huabo.R
import com.liuj.huabo.api.net.HttpEngine
import com.liuj.huabo.api.net.RequestConfig
import com.liuj.huabo.api.net.callback.BaseResponseBean
import com.liuj.huabo.api.net.callback.JSONCallback
import com.liuj.huabo.api.net.callback.NestCallback
import com.liuj.huabo.bean.CheckTicketResultBean
import com.liuj.huabo.bean.FaceDetectBean
import com.liuj.huabo.util.AppUtil
import com.liuj.huabo.util.AsyncTaskManager
import com.liuj.huabo.util.BitmapUtil
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.PictureResult
import com.speedata.utils.ToolUtils
import com.wega.library.loadingDialog.LoadingDialog
import kotlinx.android.synthetic.main.activity_face_detect.*
import okhttp3.Call
import org.jetbrains.anko.startActivity
import java.io.IOException
import java.net.URL
import java.net.URLEncoder
import java.util.*

class FaceDetectActivity : AppCompatActivity() {

    var source = 0  // 2 团体票 人脸采集    0 人脸检票务    1 人脸补录  4 出馆人脸核销
    var reserveNo = ""
    var uuid = ""
    var maxFaceNum = 3
    var currentNum = 0;

    var loadingDialog: LoadingDialog? = null

    companion object {
        @JvmField
        val SOURCE = "source"
        @JvmField
        val RERSERVE_NO = "reserveNo"
        @JvmField
        val UUID = "uuid"
        @JvmField
        val CURRENTNUM = "current_num"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_face_detect)
        source = intent!!.getIntExtra(SOURCE, 0)
        reserveNo = intent!!.getStringExtra(RERSERVE_NO).toString()
        uuid = intent!!.getStringExtra(UUID).toString()
        currentNum = intent!!.getIntExtra(CURRENTNUM,0)
        if (source == 0) {
            tv_title.text = "人脸检票"
            tv_count.visibility = View.GONE
        } else if (source == 1) {
            tv_title.text = "单个人脸补录"
            tv_count.visibility = View.GONE
        } else if(source == 4) {
            tv_title.text = "出馆人脸采集"
            tv_count.visibility = View.GONE
            start_face_detect.text = "录入"
        } else{
            tv_count.visibility = View.VISIBLE
            tv_count.text = "${currentNum}"///${maxFaceNum}"
            tv_title.text = "团体人脸补录"
            ToastUtils.showLong("请录入三个人脸")
        }
        initView()
        initLoad()
    }

    override fun onResume() {
        super.onResume()
        camera.open()
//        camera.toggleFacing()
    }


    private fun initView() {
        camera.setLifecycleOwner(this)
        camera.addCameraListener(object : CameraListener() {

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onPictureTaken(result: PictureResult) {
                super.onPictureTaken(result)
                result.toBitmap {
                    if (it != null) {
                        var scaleBitmap = BitmapUtil.scaleBitmap(it, it!!.width / 8, it.height / 8, false)
                        runOnUiThread {
                            shot_preview.setImageBitmap(scaleBitmap)
                        }
                        var picStr = BitmapUtil.bitmapToBase64String(scaleBitmap)
                        Log.d("onPictureTaken", picStr)
                        if (source == 0) {
                            faceCheckTicket(picStr)
                        } else if (source == 2) {
                            faceCollect(picStr)
                        } else if(source == 1){
                            faceBuLu(picStr, uuid)
                        }else{
                            faceBuLu(picStr,"")
                        }
                    }
                }
            }
        })

        fl_back.setOnClickListener {
            finish()
        }

        start_face_detect.setOnClickListener {
            loadingDialog?.show()
            loadingDialog?.loading("识别中...")
//            camera.takePicture()
            camera.takePictureSnapshot()
        }
    }



    //人脸检票
    private fun faceCheckTicket(imageStr: String) {
        var param = JSONObject()
        param["image"] = imageStr
        HttpEngine.post(RequestConfig.Url.FACE_QUERY).params(param).execute(object : JSONCallback() {
            override fun onSuccess(baseResponseBean: BaseResponseBean) {
                var faceDetectBean = JSONObject.parseObject(baseResponseBean.data, FaceDetectBean::class.java)
                if (!TextUtils.isEmpty(faceDetectBean.uuid)) {
                    checkTicket(faceDetectBean.uuid)
                } else {
                    faceReCheckTicket(faceDetectBean.faceId)
                }
            }

            override fun onFailed(bean: BaseResponseBean?) {
                super.onFailed(bean)
                loadingDialog?.loadFail("识别失败");
            }

            override fun onError(call: Call?, e: IOException?) {
                super.onError(call, e)
                loadingDialog?.loadFail("识别失败");
            }
        })

    }


    private fun faceReCheckTicket(faceId: String) {
        var param = JSONObject()
        param["faceId"] = faceId
        HttpEngine.post(RequestConfig.Url.RE_CHECK_TICKET).params(param).execute(object : JSONCallback() {
            override fun onSuccess(baseResponseBean: BaseResponseBean) {
                loadingDialog?.loadSuccess("检票成功")

                var checkTicketResultBean = JSONObject.parseObject(baseResponseBean.data, CheckTicketResultBean::class.java)
                if (checkTicketResultBean != null) {
                    checkTicketResultBean.msg = baseResponseBean.msg
                    checkTicketResultBean.status = 1
                    startActivity<CheckResultActivity>(
                        "result" to checkTicketResultBean,
                        "type" to 3
                    )
                }
            }

            override fun onFailed(bean: BaseResponseBean?) {
                super.onFailed(bean)
                loadingDialog?.loadFail("识别失败");
            }

            override fun onError(call: Call?, e: IOException?) {
                super.onError(call, e)
                loadingDialog?.loadFail("识别失败");
            }

        })
    }


    private fun checkTicket(uuid: String) {
        var param = JSONObject()
        param["uuid"] = uuid
        HttpEngine.post(RequestConfig.Url.CHECK_TICKET).params(param).execute(object : JSONCallback() {
            override fun onSuccess(baseResponseBean: BaseResponseBean) {
                loadingDialog?.loadSuccess("检票成功")
                var bean = JSONObject.parseObject(baseResponseBean.data, CheckTicketResultBean::class.java)
                if (bean != null) {
                    bean.msg = baseResponseBean.msg
                    bean.status = 1
                    bean.uuid = uuid
                    startActivity<CheckResultActivity>("result" to bean, "type" to 3)
                }
            }

            override fun onFailed(bean: BaseResponseBean?) {
                super.onFailed(bean)
                loadingDialog?.loadFail("识别失败");
            }

            override fun onError(call: Call?, e: IOException?) {
                super.onError(call, e)
                loadingDialog?.loadFail("识别失败");
            }
        })


    }


    private fun faceCollect(imageStr: String) {
        var param = JSONObject()
        param["image"] = imageStr//URLEncoder.encode(imageStr)
        param["reserveNo"] = reserveNo
        HttpEngine.post(RequestConfig.Url.FACE_COLLECT).params(param).execute(object : JSONCallback() {
            override fun onSuccess(baseResponseBean: BaseResponseBean?) {
                loadingDialog?.loadSuccess("录入成功")
                currentNum++
                tv_count.text = "${currentNum}"// /${maxFaceNum}"
//                if (currentNum == maxFaceNum) {
//                    ToastUtils.showShort("已完成录入")
//                    Handler().postDelayed({ finish()},1000)
//                }
            }

            override fun onFailed(bean: BaseResponseBean?) {
                super.onFailed(bean)
                loadingDialog?.loadFail("识别失败");
            }

            override fun onError(call: Call?, e: IOException?) {
                super.onError(call, e)
                loadingDialog?.loadFail("识别失败");
            }

        })
    }

    /**
     * 人脸补录
     */
    private fun faceBuLu(imageStr: String, uuId: String) {
        var param = JSONObject()
        param["image"] = imageStr//URLEncoder.encode(imageStr)
        if(!TextUtils.isEmpty(uuId)) {
            param["uuid"] = uuId
        }
        param["recheck"] = "Y"
        HttpEngine.post(RequestConfig.Url.FACE_COLLECT).params(param).execute(object : JSONCallback() {
            override fun onSuccess(baseResponseBean: BaseResponseBean?) {
                if(source == 1) {
                    loadingDialog?.loadSuccess("人脸补录成功")
                    Handler().postDelayed({ finish()},3000)
                }else{
                    loadingDialog?.loadSuccess("录入成功！")
                }

            }

            override fun onFailed(bean: BaseResponseBean?) {
                super.onFailed(bean)
//                ToastUtils.showLong(bean?.msg)
                loadingDialog?.loadFail("识别失败");
            }

            override fun onError(call: Call?, e: IOException?) {
                super.onError(call, e)
                loadingDialog?.loadFail("识别失败");
            }
        })


    }

    private fun initLoad() {
        loadingDialog = LoadingDialog.Builder(this).setLoading_text("同步中...").setFail_text("同步失败！！！").setSuccess_text("同步成功")
                .create()
    }


}
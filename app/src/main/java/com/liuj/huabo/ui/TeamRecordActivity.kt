package com.liuj.huabo.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import com.alibaba.fastjson.JSONObject
import com.blankj.utilcode.util.ToastUtils
import com.liuj.huabo.R
import com.liuj.huabo.api.net.HttpEngine
import com.liuj.huabo.api.net.RequestConfig
import com.liuj.huabo.api.net.callback.NestCallback
import com.liuj.huabo.bean.TeamNoQueryBean
import kotlinx.android.synthetic.main.activity_team_record.*
import org.jetbrains.anko.startActivity

class TeamRecordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team_record)

        fl_back.setOnClickListener {
            finish()
        }

        continue_check_ticket.setOnClickListener {
            if(TextUtils.isEmpty(et_conetnt.text.toString().trim())){
                ToastUtils.showShort("请输入团体票号")
                return@setOnClickListener
            }

            teamNOQuery()

        }
    }

    public fun teamNOQuery(){
        var param = JSONObject()
        param["reserveNo"] = et_conetnt.text.toString().trim()
        HttpEngine.post(RequestConfig.Url.TEAM_NO_QUERY).params(param).execute(object :NestCallback<TeamNoQueryBean>(){
            override fun onSuccess(t: TeamNoQueryBean) {
                startActivity<FaceDetectActivity>(FaceDetectActivity.SOURCE to 2 , FaceDetectActivity.RERSERVE_NO to et_conetnt.text.toString().trim(),
                        FaceDetectActivity.CURRENTNUM to  t.faceCount)
            }
        })
    }


}
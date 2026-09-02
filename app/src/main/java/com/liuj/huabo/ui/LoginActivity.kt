package com.liuj.huabo.ui

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.View
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
import com.liuj.huabo.api.net.callback.NestCallback
import com.liuj.huabo.bean.UpdateBean
import com.liuj.huabo.bean.UserInfoBean
import com.liuj.huabo.common.Constants
import com.liuj.huabo.util.AppInfoUtil
import com.liuj.huabo.util.UIUtil
import com.liuj.huabo.widget.BottomFIllDataDialog
import com.liuj.huabo.widget.FIllPwdDialog
import com.youchain.pda.ui.activity.set.SetServerActivity
import kotlinx.android.synthetic.main.activity_login.*
import okhttp3.Call
import org.jetbrains.anko.startActivity
import java.io.IOException

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        StateAppBar.setStatusBarColor(
            this,
            ContextCompat.getColor(this, R.color.mainActivityStatusBarColor)
        )
        iv_login_confim.setOnClickListener {
            if (UIUtil.isValidClick(1000)) {
                login(et_username.text.toString().trim(), et_password.text.toString().trim())
            }
        }

//        iv_server.setOnClickListener {
//            if (UIUtil.isValidClick(1000)) {
//                startActivity<SetServerActivity>()
//            }
//        }

        iv_server.setOnLongClickListener {
            var dialog = FIllPwdDialog(this)
            dialog.setCallBack {
                if(it.toString().trim() == "618") {
                    startActivity<SetServerActivity>()
                }else{
                    ToastUtils.showShort("密码错误！！！")
                }
            }
            dialog.show()

            true
        }


        tv_version.text = "v"+AppInfoUtil.getAppVersion()

    }

    override fun onResume() {
        super.onResume()
        var username = SPUtils.getInstance().getString("username","")
        var password = SPUtils.getInstance().getString("password","")
        et_username.setText(username)
        et_password.setText(password)
//        checkUpdate()
    }


    private fun login(username: String, pwd: String) {
        /*startActivity<MainActivity>()
        Constants.name = "test"
        return*/

        var param = JSONObject()
        param["username"] = username
        param["password"] = pwd
        HttpEngine.post(RequestConfig.Url.LOGIN).params(param)
            .execute(object : NestCallback<UserInfoBean>() {
                override fun onSuccess(t: UserInfoBean) {
                    Constants.APP_KEY = t.appkey
                    Constants.SECRET_CODE = t.secretCode
                    Constants.name = t.title
                    Constants.supportAddface = t.supportAddface
                    SPUtils.getInstance().put("username", username)
                    SPUtils.getInstance().put("password", pwd)
                    startActivity<MainActivity>()
                }

                override fun onFailed(bean: BaseResponseBean?) {
                    super.onFailed(bean)
                    ToastUtils.showShort("用户名或密码错误")
                }

                override fun onError(call: Call?, e: IOException?) {
                    super.onError(call, e)
                    ToastUtils.showShort("服务器错误")
                }
            })
    }



    private fun checkUpdate(){
        var param = JSONObject()
        HttpEngine.post(RequestConfig.Url.CHECK_UPDATE).params(param).execute(object :NestCallback<UpdateBean>(){
            override fun onSuccess(t: UpdateBean) {

            }
        })
    }

    private fun showUpdateDialog(url:String){
        val alert = android.app.AlertDialog.Builder(this@LoginActivity)
        alert.setTitle("软件升级")
            .setMessage("发现新版本,建议立即更新使用.")
            .setPositiveButton("更新") { dialog, which ->
                //开启更新服务UpdateService
                //这里为了把update更好模块化，可以传一些updateService依赖的值
                //如布局ID，资源ID，动态获取的标题,这里以app_name为例
                //installNormal(Login.this, Environment.getExternalStorageDirectory().getPath()+"/appdown/18-4-16-4.apk");
                try {
                    /**
                     * 通过浏览器下载APK更新安装
                     * @param context    上下文
                     * @param httpUrlApk APK下载地址
                     */
                    val uri = Uri.parse(url)
                    val viewIntent = Intent(Intent.ACTION_VIEW, uri)
                    startActivity(viewIntent)
                } catch (e: Exception) {
                   e.printStackTrace()
                }
            }
            .setNegativeButton("取消") { dialog, which -> dialog.dismiss() }
        alert.create().show()
    }

}
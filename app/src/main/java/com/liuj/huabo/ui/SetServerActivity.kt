package com.youchain.pda.ui.activity.set


import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.liuj.huabo.R
import com.liuj.huabo.api.net.RequestConfig
import com.liuj.huabo.common.Constants


import kotlinx.android.synthetic.main.activity_set_server.*

class SetServerActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_server)
        initView()
    }


     fun initView() {
        now_ip.text = RequestConfig.Url.URL_SERVER
         new_ip.setText(RequestConfig.Url.URL_SERVER)//231209
        submit_btn.setOnClickListener {
            val editor = getSharedPreferences(Constants.App_Config, Context.MODE_PRIVATE).edit()
            editor.putString(Constants.Server_Url,new_ip.text.trim().toString())
            editor.apply()
            RequestConfig.Url.URL_SERVER =new_ip.text.trim().toString()
            RequestConfig.Url.refreshParentUrl()
            Toast.makeText(this,"修改服务配置成功",Toast.LENGTH_SHORT).show()
        }
    }


}

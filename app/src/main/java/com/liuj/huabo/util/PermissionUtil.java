package com.liuj.huabo.util;

import android.Manifest;
import android.content.Intent;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.blankj.utilcode.util.SPUtils;
import com.liuj.huabo.ui.HuaBoScanActivity;
import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.ExplainReasonCallbackWithBeforeParam;
import com.permissionx.guolindev.callback.ForwardToSettingsCallback;
import com.permissionx.guolindev.callback.RequestCallback;
import com.permissionx.guolindev.request.ExplainScope;
import com.permissionx.guolindev.request.ForwardScope;

import java.util.List;

/**
 * Created by liujun on 2020/4/26.
 */
public class PermissionUtil {

    public static PermissionUtil INSTANCE;

    private String[] permissionsGroup = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
    };

    private PermissionUtil() {
    }

    public static PermissionUtil getInstance() {
        if (INSTANCE == null) {
            synchronized (PermissionUtil.class) {
                INSTANCE = new PermissionUtil();
            }
        }
        return INSTANCE;
    }

    public void request(final FragmentActivity activity,int mode) {
        PermissionX.init(activity)
                .permissions(permissionsGroup)
                .explainReasonBeforeRequest()
                .onExplainRequestReason((scope, deniedList, beforeRequest) -> scope.showRequestReasonDialog(deniedList, "需要以下权限才能正常工作哦", "去申请"))
                .onForwardToSettings((scope, deniedList) -> scope.showForwardToSettingsDialog(deniedList, "Please allow following permissions in settings", "Allow"))
                .request((allGranted, grantedList, deniedList) -> {
                    if (allGranted) {
                        Intent intent =new Intent(activity, HuaBoScanActivity.class);
                        intent.putExtra("source",mode);
                        activity.startActivityForResult(intent, 100);
                    } else {
                        Toast.makeText(activity, "The following permissions are denied：" + deniedList, Toast.LENGTH_SHORT).show();
                    }
                });

    }




}

package com.liuj.huabo.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.ToastUtils;
import com.liuj.huabo.LanguageAdapter;
import com.liuj.huabo.LanguageBean;
import com.liuj.huabo.R;
import com.liuj.huabo.api.net.HttpEngine;
import com.liuj.huabo.api.net.RequestConfig;
import com.liuj.huabo.api.net.callback.BaseResponseBean;
import com.liuj.huabo.api.net.callback.JSONCallback;
import com.liuj.huabo.bean.BaseQueryBatResDataBean;
import com.liuj.huabo.bean.CheckTicketResultBean;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import okhttp3.Call;


public class BatCheck extends AppCompatActivity {

    Button buttonQuery;
    Button buttonOK;
    Button buttonCancel;
    Button buttonSelectAll;
    EditText edtBatChkRes;
    TextView tvCount;
    List<LanguageBean> mData = new LinkedList<>();

    private LanguageAdapter languageAdapter;
    private ListView listView;
    private AlertDialog progressDialog;
    private int checkedCount = 0;
    private int totalToCheck = 0;

    String strBatQueryRtJs;
    private String sResShow = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bat_check);

        // ★ 必须先 findViewById，再调用 getBatListFromJs
        buttonQuery = findViewById(R.id.buttonQuery);
        buttonOK = findViewById(R.id.buttonOK);
        buttonCancel = findViewById(R.id.buttonCancel);
        buttonSelectAll = findViewById(R.id.buttonSelectAll);
        edtBatChkRes = findViewById(R.id.edtBatCheckResult);
        tvCount = findViewById(R.id.tvCount);
        listView = findViewById(R.id.listview);

        edtBatChkRes.setKeyListener(null);

        strBatQueryRtJs = getIntent().getStringExtra("result");
        getBatListFromJs(strBatQueryRtJs);

        buttonQuery.setOnClickListener(v -> {});

        buttonOK.setOnClickListener(v -> showOkCancelDialog("请确认操作", "确定核销？"));

        buttonCancel.setOnClickListener(v -> finish());

        // 全选 / 取消全选
        buttonSelectAll.setOnClickListener(v -> {
            boolean allSelected = true;
            for (LanguageBean bean : mData) {
                if (bean.getCanCheck() && !bean.getChecked()) {
                    allSelected = false;
                    break;
                }
            }
            boolean newChecked = !allSelected;
            for (LanguageBean bean : mData) {
                if (bean.getCanCheck()) {
                    bean.setChecked(newChecked);
                }
            }
            updateListView();
            updateCount();
        });
    }

    protected void getBatListFromJs(String sss) {
        mData.clear();

        try {
            BaseQueryBatResDataBean bean = JSONObject.parseObject(sss, BaseQueryBatResDataBean.class);
            if (bean != null && bean.ticketInfo != null) {
                for (int i = 0; i < bean.ticketInfo.length; i++) {
                    String sName = bean.ticketInfo[i].realname;
                    String sCertificateNo = bean.ticketInfo[i].certificateNo;
                    String sTyp = bean.ticketInfo[i].scheduleName;
                    String sUuid = bean.ticketInfo[i].uuid;
                    String sSta = bean.ticketInfo[i].status;
                    String sStaShow = "";
                    boolean bCanChk = false;
                    switch (sSta) {
                        case "Y":
                            sStaShow = "未使用";
                            bCanChk = true;
                            break;
                        case "N":
                            sStaShow = "无效";
                            break;
                        case "T":
                            sStaShow = "已退票";
                            break;
                        case "U":
                            sStaShow = "已使用";
                            break;
                    }
                    mData.add(new LanguageBean(sName, sCertificateNo, sTyp, sStaShow, bCanChk, false, sUuid));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 传入回调，Checkbox 变化时刷新计数
        languageAdapter = new LanguageAdapter((LinkedList<LanguageBean>) mData, getApplicationContext(),
                () -> updateCount());
        listView.setAdapter(languageAdapter);

        // 点击整行切换选中
        listView.setOnItemClickListener((parent, view, position, id) -> {
            if (position >= 0 && position < mData.size()) {
                LanguageBean bean = mData.get(position);
                if (bean.getCanCheck()) {
                    bean.setChecked(!bean.getChecked());
                    updateListView();
                    updateCount();
                }
            }
        });

        updateCount();
    }

    private void showOkCancelDialog(String sTitle, String sMsg) {
        new AlertDialog.Builder(this)
                .setTitle(sTitle)
                .setMessage(sMsg)
                .setPositiveButton("确定", (dialog, which) -> DoCheckIn())
                .setNegativeButton("取消", null)
                .show();
    }

    private void DoCheckIn() {
        sResShow = "";
        edtBatChkRes.setText(sResShow);

        final ArrayList<LanguageBean> toCheck = new ArrayList<>();
        for (LanguageBean bean : mData) {
            if (bean.getChecked() && bean.getCanCheck()) {
                toCheck.add(bean);
            }
        }

        if (toCheck.isEmpty()) {
            Toast.makeText(this, "没有可核销的票", Toast.LENGTH_SHORT).show();
            return;
        }

        showProgressDialog(toCheck.size());
        checkNextTicket(toCheck, 0);
    }

    private void showProgressDialog(int total) {
        totalToCheck = total;
        checkedCount = 0;
        progressDialog = new AlertDialog.Builder(this)
                .setTitle("核销进行中")
                .setMessage("已核销: 0/" + total + "\n请稍候...")
                .setCancelable(false)
                .create();
        progressDialog.show();
    }

    private void updateProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.setMessage("已核销: " + checkedCount + "/" + totalToCheck + "\n请稍候...");
        }
    }

    private void checkNextTicket(final ArrayList<LanguageBean> toCheck, final int index) {
        if (index >= toCheck.size()) {
            if (progressDialog != null) {
                progressDialog.dismiss();
                progressDialog = null;
            }
            updateListView();
            updateCount();
            return;
        }

        LanguageBean bean = toCheck.get(index);
        checkTicketWithCallback(bean.getUuid(), bean.getName(), bean.getPassnbr(), () -> {
            checkedCount++;
            updateProgress();
            checkNextTicket(toCheck, index + 1);
        });
    }

    private void checkTicketWithCallback(final String uuid, final String theName, final String thePassNbr, final Runnable onComplete) {
        JSONObject param = new JSONObject();
        param.put("uuid", uuid);
        param.put("certificateType","3");
        HttpEngine.post(RequestConfig.Url.CHECK_TICKET).params(param).execute(new JSONCallback() {
            @Override
            public void onSuccess(@NotNull BaseResponseBean baseResponseBean) {
                CheckTicketResultBean bean = JSONObject.parseObject(baseResponseBean.data, CheckTicketResultBean.class);
                if (bean != null) {
                    sResShow += (sResShow.isEmpty() ? "" : "\r\n") + theName + thePassNbr + "核销完成";
                    edtBatChkRes.setText(sResShow);
                }
                updateBeanStatus(uuid, true);
                if (onComplete != null) onComplete.run();
            }

            @Override
            public void onFailed(@NotNull BaseResponseBean bean) {
                sResShow += (sResShow.isEmpty() ? "" : "\r\n") + theName + thePassNbr + "核销失败:" + bean.msg;
                edtBatChkRes.setText(sResShow);
                if (onComplete != null) onComplete.run();
            }

            @Override
            public void onError(Call call, IOException e) {
                ToastUtils.showLong("网络错误");
                sResShow += (sResShow.isEmpty() ? "" : "\r\n") + theName + thePassNbr + "核销失败:网络错误";
                edtBatChkRes.setText(sResShow);
                if (onComplete != null) onComplete.run();
            }
        });
    }

    private void updateBeanStatus(String uuid, boolean success) {
        for (LanguageBean bean : mData) {
            if (bean.getUuid().equals(uuid) && success) {
                bean.setUsesta("已使用");
                bean.setCanCheck(false);
                bean.setChecked(false);
                break;
            }
        }
    }

    private void updateListView() {
        if (languageAdapter != null) {
            // sortCheckedToEnd(); // 如需把已选移到末尾可打开
            languageAdapter.notifyDataSetChanged();
        }
    }

    private void sortCheckedToEnd() {
        java.util.Collections.sort(mData, (a, b) -> {
            boolean ca = a.getChecked();
            boolean cb = b.getChecked();
            return Boolean.compare(ca, cb);
        });
    }

    private void updateCount() {
        if (tvCount == null) return;
        int total = mData.size();
        int checked = 0;
        for (LanguageBean bean : mData) {
            if (bean.getChecked()) checked++;
        }
        tvCount.setText("已选" + checked + "/总" + total);
    }
}
package com.liuj.huabo.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.ScreenUtils;
import com.liuj.huabo.R;


public class FIllPwdDialog extends Dialog {
    private static final String TAG = "FIllPwdDialog";
    private Context mContext;

    private int mType = 0;
    private TextView mTvTitle;

    private TextView mTvConfirm;
    private TextView mTvCancel;
    private EditText mEditText;

    private ResultCallBack mCallBack;


    public FIllPwdDialog(Context context) {
        this(context, 0);
    }

    protected FIllPwdDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
        init();
    }

    public FIllPwdDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
        init();
    }


    private void init() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_pwd_fill);
        mTvTitle = findViewById(R.id.title);
        mTvConfirm = findViewById(R.id.tv_confirm);
        mTvCancel = findViewById(R.id.tv_cancel);
        mEditText = findViewById(R.id.et_conetnt);
        Window window = getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setWindowAnimations(R.style.BottomDialog_Animation);
            WindowManager.LayoutParams params = window.getAttributes();
            params.gravity = Gravity.BOTTOM;
            params.width = ScreenUtils.getScreenWidth();
            params.y = 0;
            window.setAttributes(params);
        }
        setCancelable(false);
        setCanceledOnTouchOutside(false);

        mTvCancel.setOnClickListener(v -> dismiss());

        mTvConfirm.setOnClickListener(v -> {
            if(mCallBack!=null){
                mCallBack.result(mEditText.getText().toString());
            }
            dismiss();
        });
    }



    public void setCallBack(ResultCallBack mCallBack) {
        this.mCallBack = mCallBack;
    }

    public interface ResultCallBack{
        void result(String content);
    }

}

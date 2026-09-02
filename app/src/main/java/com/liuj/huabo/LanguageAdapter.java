package com.liuj.huabo;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.LinkedList;

public class LanguageAdapter extends BaseAdapter {

    private LinkedList<LanguageBean> mData;
    private Context mContext;
    private OnCheckedChangeListener onCheckedChangeListener;

    public interface OnCheckedChangeListener {
        void onCheckedChanged();
    }

    public LanguageAdapter(LinkedList<LanguageBean> mData, Context mContext) {
        this.mData = mData;
        this.mContext = mContext;
    }

    public LanguageAdapter(LinkedList<LanguageBean> mData, Context mContext, OnCheckedChangeListener listener) {
        this.mData = mData;
        this.mContext = mContext;
        this.onCheckedChangeListener = listener;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int index = position;
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listview_item, parent, false);
            holder = new ViewHolder();
            holder.name = convertView.findViewById(R.id.name);
            holder.passnbr = convertView.findViewById(R.id.passnbr);
            holder.tkttyp = convertView.findViewById(R.id.tkttyp);
            holder.usesta = convertView.findViewById(R.id.usesta);
            holder.checked = convertView.findViewById(R.id.checked);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        LanguageBean bean = mData.get(index);

        holder.name.setText(bean.getName());
        holder.passnbr.setText(bean.getPassnbr());
        holder.tkttyp.setText(bean.getTkttyp());
        holder.usesta.setText(bean.getUsesta());

        // 先清空监听，再设置真实状态，再重新设置监听（防止误触发）
        holder.checked.setOnCheckedChangeListener(null);
        holder.checked.setChecked(bean.getChecked());
        holder.checked.setEnabled(bean.getCanCheck());

        holder.checked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // 只响应用户真实点击
                if (!buttonView.isPressed()) return;

                mData.get(index).setChecked(isChecked);

                // 通知 Activity 刷新计数
                if (onCheckedChangeListener != null) {
                    onCheckedChangeListener.onCheckedChanged();
                }

                // 立即刷新当前行的颜色
                if (isChecked || (bean.getUsesta() != null && bean.getUsesta().contains("已"))) {
                    holder.usesta.setTextColor(Color.RED);
                } else {
                    holder.usesta.setTextColor(Color.BLACK);
                }
            }
        });


        // 已使用/已选中显示红色
        if (bean.getUsesta() != null && (bean.getUsesta().contains("已") || bean.getChecked())) {
            holder.usesta.setTextColor(Color.RED);
        } else {
            holder.usesta.setTextColor(Color.BLACK);
        }




        return convertView;
    }

    static class ViewHolder {
        TextView name;
        TextView passnbr;
        TextView tkttyp;
        TextView usesta;
        CheckBox checked;
    }
}
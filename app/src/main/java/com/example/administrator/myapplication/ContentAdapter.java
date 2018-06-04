package com.example.administrator.myapplication;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Administrator on 2018/6/3.
 */

public class ContentAdapter extends BaseAdapter {

    private Activity activity;
    private List<BookBean> mlist;

    public ContentAdapter(Activity activity, List<BookBean> list) {
        this.activity = activity;
        if (this.mlist != null){
            this.mlist.clear();
        }

        this.mlist = list;
    }

    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public Object getItem(int position) {
        return mlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(activity).inflate(R.layout.history_details_item
                    , null);
            holder.ptitle = (TextView) convertView.findViewById(R.id.ptitle);
            holder.ostyle_text = (TextView) convertView.findViewById(R.id.ostyle_text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.ptitle.setText(mlist.get(position).bookname);
        holder.ostyle_text.setText(mlist.get(position).author);
        return convertView;
    }

    private static class ViewHolder {
        TextView ptitle;
        TextView ostyle_text;
    }
}

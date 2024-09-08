package com.example.androidjsonapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class FetchAdapter extends BaseAdapter {
    private Context context;
    private List<FetchItem> items;
    private LayoutInflater inflater;

    public FetchAdapter(Context context, List<FetchItem> items) {
        this.context = context;
        this.items = items;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return items.get(position).id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_fetch, parent, false);
        }
        TextView nameView = convertView.findViewById(R.id.item_name);
        FetchItem item = items.get(position);
        nameView.setText(item.name);
        return convertView;
    }
}
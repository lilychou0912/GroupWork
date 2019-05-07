package com.example.myapplication.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.telecom.Call;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View.OnClickListener;


import com.example.myapplication.Bean.Collections;

import com.example.myapplication.R;

import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.lzy.ninegrid.preview.NineGridViewClickAdapter;


import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MyAdapterC  extends BaseAdapter {
    private List<Collections> list;
    private Context context;



    public MyAdapterC(Context context, List<Collections> list) {
        this.context = context;
        this.list = list;

    }

    public void addCollections(List<Collections> list) {
        this.list = list;

    }

    @Override
    public int getCount() {
        if (list == null)
            return 0;
        else
            return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.collections_item, null);
            holder = new ViewHolder();

            holder.content= (TextView) view.findViewById(R.id.title_collection);


            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }




        holder.content.setText((list.get(i).getTitle()));

        return view;
    }





    private class ViewHolder {


        private TextView content;



    }
}

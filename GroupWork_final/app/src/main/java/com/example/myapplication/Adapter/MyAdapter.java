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

import com.example.myapplication.Bean.Schedule;
import com.example.myapplication.R;

import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.lzy.ninegrid.preview.NineGridViewClickAdapter;


import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MyAdapter  extends BaseAdapter {
    private List<Schedule> list;
    private Context context;



    public MyAdapter(Context context, List<Schedule> list) {
        this.context = context;
        this.list = list;

    }

    public void addSchedule(List<Schedule> list) {
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
            view = LayoutInflater.from(context).inflate(R.layout.list_item, null);
            holder = new ViewHolder();

            holder.tag = (TextView) view.findViewById(R.id.duration);
            holder.content = (TextView) view.findViewById(R.id.title);
            holder.icon = (ImageView) view.findViewById(R.id.pic);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }




        holder.content.setText((list.get(i).getTitle()));
        holder.tag.setText((list.get(i).getTag()));



        return view;
    }
    private Drawable loadImageFromNetwork(String imageUrl)
    {
        Drawable drawable = null;
        try {
            drawable = Drawable.createFromStream(
                    new URL(imageUrl).openStream(), "image.jpg");
        } catch (IOException e) {
            Log.d("test", e.getMessage());
        }
        if (drawable == null) {
            Log.d("test", "null drawable");
        } else {
            Log.d("test", "not null drawable");
        }

        return drawable ;
    }




    private class ViewHolder {

        private TextView tag;
        private TextView content;
        private ImageView icon;


    }
}

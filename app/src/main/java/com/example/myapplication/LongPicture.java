package com.example.myapplication;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.LayoutInflater;
import android.view.View;

public class LongPicture {

    public void onBindViewHolder(final ScheduleAdapter.MyViewHolder holder, final int position) {
        //生成长图
        holder.sPicture.setOnClickListener(new View.OnClickListener() {
            private View picView;

            @Override
            public void onClick(View v) {
                LayoutInflater factorys = LayoutInflater.from(this);
                final View textEntryView = factorys.inflate(R.layout.long_picture, null);
                picView = (View) textEntryView.findViewById(R.id.long_picture);
            }
        });

    }

    /*生成长图的方法*/
    public static Bitmap getViewDrawingCacheBitmap(View view) {
        view = view.getRootView();
        if (!view.isDrawingCacheEnabled()) {
            view.setDrawingCacheEnabled(true);
        }
        view.destroyDrawingCache();
        view.buildDrawingCache();
        Bitmap bm = view.getDrawingCache();
        view.setDrawingCacheEnabled(false);
        return bm;
    }
}

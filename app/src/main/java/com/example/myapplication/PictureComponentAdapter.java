package com.example.myapplication;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.LinkedList;

import static org.litepal.LitePalApplication.getContext;

/************************长图里混合排版的图片组件adapter****************/
public class PictureComponentAdapter extends BaseAdapter {
    Context context;
    LinkedList<Integer> imgList;
    PictureComponentAdapter(Context context , String[] imgList){
        this.context = context;
        this.imgList = imgList;
    }
    @Override
    public int getCount() {
        return imgList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHodler viewHodler;

        if (convertView == null){
            viewHodler   = new ViewHodler();
            convertView = View.inflate(context,R.layout.picture_component_img_item,null);
            viewHodler.imageView = (ImageView) convertView.findViewById(R.id.imgview);

            //获取屏幕宽度
            WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
            int width = wm.getDefaultDisplay().getWidth();
            //根据图片的数量设置图片的大小，这里的大小是写的具体数值，也可以获得手机屏幕的尺寸来设置图片的大小
            if (imgList.size() == 1){
                ViewGroup.LayoutParams params = viewHodler.imageView.getLayoutParams();
                params.height = 800;
                params.width = width;
                viewHodler.imageView.setLayoutParams(params);
            }else if (imgList.size() == 2){
                ViewGroup.LayoutParams params = viewHodler.imageView.getLayoutParams();
                params.height = 600;
                params.width = width;
                viewHodler.imageView.setLayoutParams(params);
            }else if (imgList.size() == 3){
                ViewGroup.LayoutParams params = viewHodler.imageView.getLayoutParams();
                params.height = 600;
                params.width = width;
                viewHodler.imageView.setLayoutParams(params);
            }else{
                ViewGroup.LayoutParams params = viewHodler.imageView.getLayoutParams();
                params.height = 400;
                params.width = 400;
                viewHodler.imageView.setLayoutParams(params);
            }
            convertView.setTag(viewHodler);
        }else{
            viewHodler =(ViewHodler) convertView.getTag();
        }

        viewHodler.imageView.setImageResource(imgList.get(position));
        return  convertView;
    }

    class  ViewHodler{
        ImageView imageView;
    }
}

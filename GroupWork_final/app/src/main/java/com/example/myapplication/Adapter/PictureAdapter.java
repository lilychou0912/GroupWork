package com.example.myapplication.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myapplication.Bean.Schedule;
import com.example.myapplication.Bean.ScheduleItem;
import com.example.myapplication.R;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

public class PictureAdapter extends BaseAdapter {
    private List<ScheduleItem> list;
    private Context context;
    private String id;

    public PictureAdapter(Context context, List<ScheduleItem> list, String id) {
        this.context = context;
        this.list = list;
        this.id = id;
    }

    public void addScheduleItem(List<ScheduleItem> list) {
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
        //Bmob初始化

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.picture_item, null);
            holder = new ViewHolder();
            holder.Date = (TextView) view.findViewById(R.id.item_date);
            holder.time = (TextView) view.findViewById(R.id.item_time);
            holder.Type = (TextView) view.findViewById(R.id.item_type);
            holder.Description = (TextView) view.findViewById(R.id.item_description);
            holder.Picture_1 = view.findViewById(R.id.item_image_1);
            holder.Picture_2 = view.findViewById(R.id.item_image_2);
            holder.Picture_3 = view.findViewById(R.id.item_image_3);
            holder.Picture_4 = view.findViewById(R.id.item_image_4);
            //holder.nineGrid = (NineGridView) view.findViewById(R.id.post_nineGrid);
            holder.Share = (LinearLayout) view.findViewById(R.id.pic_share);
            holder.Title = view.findViewById(R.id.item_title);

            view.setTag(holder);

        } else {
            holder = (ViewHolder) view.getTag();
        }

        /*
        if (list.get(i).getIimgUrl() != null) {//判断是否有图片
            ArrayList<ImageInfo> imageInfo = new ArrayList<>();
            for (int j = 0; j < list.get(i).getIimgUrl().size(); j++) {
                ImageInfo info = new ImageInfo();
                info.setThumbnailUrl(list.get(i).getIimgUrl().get(j));
                info.setBigImageUrl(list.get(i).getIimgUrl().get(j));
                info.setImageViewHeight(1000);
                info.setImageViewWidth(1000);
                imageInfo.add(info);
            }
            holder.nineGrid.setAdapter(new NineGridViewClickAdapter(context, imageInfo));
        } else {
            holder.nineGrid.setVisibility(View.GONE);
        } */
        if (list.get(i).getIimgUrl() != null) {
            switch (list.get(i).getIimgUrl().size()) {
                case 1: {
                    Bitmap bitmap1 = getLocalBitmap(list.get(i).getIimgUrl ().get(0));
                    holder.Picture_1.setImageBitmap(bitmap1);
                    //Glide.with (view).load (list.get(i).getIimgUrl ().get(0)).into (holder.Picture_1);
                }
                break;
                case 2: {
                    Bitmap bitmap1 = getLocalBitmap(list.get(i).getIimgUrl ().get(0));
                    holder.Picture_1.setImageBitmap(bitmap1);
                    Bitmap bitmap2 = getLocalBitmap(list.get(i).getIimgUrl ().get(1));
                    holder.Picture_2.setImageBitmap(bitmap2);
                    //Glide.with (view).load (list.get(i).getIimgUrl ().get(0)).into (holder.Picture_1);
                    //Glide.with (view).load (list.get(i).getIimgUrl ().get(1)).into (holder.Picture_2);
                }
                break;
                case 3: {
                    Bitmap bitmap1 = getLocalBitmap(list.get(i).getIimgUrl ().get(0));
                    holder.Picture_1.setImageBitmap(bitmap1);
                    Bitmap bitmap2 = getLocalBitmap(list.get(i).getIimgUrl ().get(1));
                    holder.Picture_2.setImageBitmap(bitmap2);
                    Bitmap bitmap3 = getLocalBitmap(list.get(i).getIimgUrl ().get(2));
                    holder.Picture_3.setImageBitmap(bitmap3);
                    //Glide.with (view).load (list.get(i).getIimgUrl ().get(0)).into (holder.Picture_1);
                    //Glide.with (view).load (list.get(i).getIimgUrl ().get(1)).into (holder.Picture_2);
                    //Glide.with (view).load (list.get(i).getIimgUrl ().get(2)).into (holder.Picture_3);
                }
                break;
                case 4: {
                    Bitmap bitmap1 = getLocalBitmap(list.get(i).getIimgUrl ().get(0));
                    holder.Picture_1.setImageBitmap(bitmap1);
                    Bitmap bitmap2 = getLocalBitmap(list.get(i).getIimgUrl ().get(1));
                    holder.Picture_2.setImageBitmap(bitmap2);
                    Bitmap bitmap3 = getLocalBitmap(list.get(i).getIimgUrl ().get(2));
                    holder.Picture_3.setImageBitmap(bitmap3);
                    Bitmap bitmap4 = getLocalBitmap(list.get(i).getIimgUrl ().get(3));
                    holder.Picture_4.setImageBitmap(bitmap4);
                    //Glide.with (view).load (list.get(i).getIimgUrl ().get(0)).into (holder.Picture_1);
                    //Glide.with (view).load (list.get(i).getIimgUrl ().get(1)).into (holder.Picture_2);
                    //Glide.with (view).load (list.get(i).getIimgUrl ().get(2)).into (holder.Picture_3);
                    //Glide.with (view).load (list.get(i).getIimgUrl ().get(3)).into (holder.Picture_4);
                }
                break;
            }
        }

        holder.Date.setText(list.get(i).getIstarttime().substring(0,10));
        holder.time.setText(list.get(i).getIstarttime().substring(11,16) + "-" + list.get(i).getIendtime().substring(11,16));
        holder.Type.setText(list.get(i).getIcategory());
        holder.Description.setText(list.get(i).getIdiscription());
        Bmob.initialize(view.getContext(), "1768baa1a8670ebb020369f5672e443f" );
        BmobQuery<Schedule> query = new BmobQuery<Schedule>();
        query.getObject(id, new QueryListener<Schedule>() {
            @Override
            public void done(Schedule schedule, BmobException e) {
                holder.Title.setText(schedule.getName());
            }
        });

        if(i == 0){
            holder.Title.setVisibility(View.VISIBLE);
        } else {
            holder.Title.setVisibility(View.GONE);
        }

        if (i == (list.size() - 1)) {
            holder.Share.setVisibility(View.VISIBLE);
        } else {
            holder.Share.setVisibility(View.GONE);
        }

        return view;
    }

    //将本地图片路径转化为bitmap
    public static Bitmap getLocalBitmap(String url) {
        //压缩方法
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private class ViewHolder {
        private TextView Date;
        private TextView time;
        private TextView Type;
        private TextView Description;
        private ImageView Picture_1;
        private ImageView Picture_2;
        private ImageView Picture_3;
        private ImageView Picture_4;
        //private com.lzy.ninegrid.NineGridView nineGrid;
        private LinearLayout Share; //底部logo
        private TextView Title;//大标题
    }
}

package com.example.myapplication.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myapplication.Adapter.PictureAdapter;
import com.example.myapplication.Bean.Schedule;
import com.example.myapplication.Bean.ScheduleItem;
import com.example.myapplication.R;
import com.lzy.ninegrid.NineGridView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;


public class LongPictureActivity extends Activity{

    private ListView pListView;
    private PictureAdapter pAdapter;
    private List<ScheduleItem> Item = new ArrayList<>();
    //private DBhelper dBhelper = new DBhelper();
    private String ssId;
    private FloatingActionButton SaveBtn;
    private FloatingActionButton ShareBtn;


    private LinearLayout relativeLayout;
    private FileOutputStream out;
    private String bitmapName = "longpicture.jpg";
    private File file;
    private String TravelFilePath;
    private String longPicPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.long_picture);
        pListView = findViewById(R.id.pic_list_view);
        relativeLayout = findViewById(R.id.pic_layout);
        SaveBtn = findViewById(R.id.save_note_btn);
        ShareBtn = findViewById(R.id.share_note_btn);

        Intent intent = getIntent();
        ssId = intent.getStringExtra("sId_extra");
        initData();
        bottomListener();
        NineGridView.setImageLoader(new GlideImageLoader());
        // 添加动画
        //pRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //saveBitmap(getRelativeLayoutBitmap(pictureView));
    }

    //初始化行程事件
    private void initData() {
        BmobQuery<Schedule> query = new BmobQuery<Schedule>();
        query.getObject(ssId, new QueryListener<Schedule>() {
            @Override
            public void done(Schedule schedule, BmobException e) {
                BmobQuery<ScheduleItem> query = new BmobQuery<ScheduleItem>();
                query.addWhereEqualTo("note_pointer", schedule );
                query.order("istarttime");
                query.findObjects(new FindListener<ScheduleItem>() {
                    @Override
                    public void done(List<ScheduleItem> items, BmobException e) {
                        if(items != null){
                            Item.clear();
                            Item.addAll(items);
                            initRecycler();
                        }else {
                            //sItem.clear();
                        }
                    }
                });
            }
        });
    }

    private void initRecycler() {
        pAdapter = new PictureAdapter(this, Item, ssId);
        pListView.setAdapter(pAdapter);

        //setContentView(R.layout.long_picture);
    }

    /** Glide加载 */
    private class GlideImageLoader implements NineGridView.ImageLoader {
        @Override
        public void onDisplayImage(Context context, ImageView imageView, String url) {
            Glide.with(context)
                    .load(url)
                    .into(imageView);
        }

        @Override
        public Bitmap getCacheImage(String url) {
            return null;
        }

    }

    /****初始化item里的图片组件
    private void initGridView (ArrayList<String> pictures) {
        View contentView = LayoutInflater.from(LongPictureActivity.this).inflate(R.layout.picture_item, null);
        GridView imgGridVeiw = (GridView) findViewById(R.id.longGridView);
        //在这里判断图片的数量，根据图片的数量改变GridView的列数，图片的大小要在Adapter里设置才有效
        if (pictures.size()>=3) imgGridVeiw.setNumColumns(3);
        if (pictures.size()==1) imgGridVeiw.setNumColumns(1);
        //绑定自定义的adapter
        GridView.LayoutManager layoutManager;
        layoutManager = new LinearLayoutManager(this);
        PictureComponentAdapter imgaAdapter = new PictureComponentAdapter(this,pictures);
        imgGridVeiw.setAdapter(imgaAdapter);
    }***********/

    private void bottomListener(){
        //给SaveBtn绑定监听事件
        SaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 给SaveBtn添加点击响应事件
                //点击跳到顶部
               pListView.smoothScrollToPosition(0);

                Bitmap bitmap = getRelativeLayoutBitmap(pListView);
                saveBitmap(bitmap);

            }
        });

        ShareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pListView.smoothScrollToPosition(0);
                Bitmap bitmap = getRelativeLayoutBitmap(pListView);
                saveBitmap(bitmap);

                // 给SaveBtn添加点击响应事件
                Intent intent = new Intent(LongPictureActivity.this, ShareActivity.class);
                intent.putExtra("sId_extra", ssId);
                intent.putExtra("longPicPath_extra", longPicPath);
                //启动
                startActivity(intent);
            }
        });
    }

    public static Bitmap getLinearLayoutBitmap(LinearLayout linearLayout) {
        int h = 0;
        // 获取LinearLayout实际高度
        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            linearLayout.getChildAt(i).measure(0, 0);
            h += linearLayout.getChildAt(i).getMeasuredHeight();
        }
        linearLayout.measure(0, 0);
        // 创建相应大小的bitmap
        Bitmap bitmap = Bitmap.createBitmap(linearLayout.getMeasuredWidth(), h,
                Bitmap.Config.RGB_565);
        final Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.WHITE);
        linearLayout.draw(canvas);
        return bitmap;
    }

    public  Bitmap getRelativeLayoutBitmap(ListView relativeLayout) {
        ListAdapter adapter  = relativeLayout.getAdapter();
        int itemscount  = adapter.getCount();
        int allitemsheight = 0;
        List<Bitmap> bmps = new ArrayList<Bitmap>();
        for (int i = 0; i < itemscount; i++) {
            View childView = adapter.getView(i, null, relativeLayout);
            childView.measure(View.MeasureSpec.makeMeasureSpec(relativeLayout.getWidth(), View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            childView.layout(0, 0, childView.getMeasuredWidth(), childView.getMeasuredHeight());
            childView.setDrawingCacheEnabled(true);
            childView.buildDrawingCache();
            bmps.add(childView.getDrawingCache());
            allitemsheight+=childView.getMeasuredHeight();
        }
        /**
        Bitmap bigbitmap = Bitmap.createBitmap(relativeLayout.getMeasuredWidth(), allitemsheight, Bitmap.Config.ARGB_8888);
        Canvas bigcanvas = new Canvas(bigbitmap);
        Paint paint = new Paint();
        int iHeight = 0;
        for (int i = 0; i < bmps.size(); i++) {
            Bitmap bmp = bmps.get(i);
            bigcanvas.drawBitmap(bmp, 0, iHeight, paint);
            iHeight+=bmp.getHeight();
            bmp.recycle();
            bmp=null;
        }
        relativeLayout.draw(bigcanvas);
         **/
        Bitmap bitmap = Bitmap.createBitmap(relativeLayout.getMeasuredWidth(), allitemsheight,
                Bitmap.Config.RGB_565);
        final Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.WHITE);
        relativeLayout.draw(canvas);
        return bitmap;


    }


    public int setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return 0;
        }

        int totalHeight =0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        System.out.println("aaa==="+totalHeight);
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        return totalHeight;
    }

    /**
    public int setListViewHeightBasedOnChildren(ListView listView1) {
        BaseAdapter listAdapter = (BaseAdapter) listView1.getAdapter();
        if (listAdapter == null) {
            return 0;
        }
        int totalHeight = 0;
        //获取listView的宽度
        ViewGroup.LayoutParams params = listView1.getLayoutParams();
        int listViewWidth  = getActivity().getWindowManager().getDefaultDisplay().getWidth();
        int widthSpec = View.MeasureSpec.makeMeasureSpec(listViewWidth, View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView1);
            //给item的measure设置参数是listView的宽度就可以获取到真正每一个item的高度
            listItem.measure(widthSpec, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        params.height = totalHeight
                + (listView1.getDividerHeight() * (listAdapter.getCount() + 1));
        listView1.setLayoutParams(params);
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

        if (bm == null) {
            Log.d("df", "dijduthfdiuhgfiuko");
        }
        return bm;
    }

    //保存获取的bitmap图片
    public void saveBitmap(Bitmap bmp) {

        try { // 获取SDCard指定目录下
            String sdCardDir = Environment.getExternalStorageDirectory() + "/travel1U/";
            File dirFile = new File(sdCardDir);  //目录转化成文件夹
            if (!dirFile.exists()) {              //如果不存在，那就建立这个文件夹
                dirFile.mkdirs();
            }                          //文件夹有啦，就可以保存图片啦
            //获取系统时间
            SimpleDateFormat df = new SimpleDateFormat("yyyy_MM_dd_HH_MM_SS");
            Calendar calendar = Calendar.getInstance();
            String time = df.format(calendar.getTime());
            bitmapName = time + ".jpg";
            file = new File(sdCardDir, bitmapName);// 在SDcard的目录下创建图片文,以当前时间为其命名
            out = new FileOutputStream(file);
            longPicPath = file.getAbsolutePath();
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, out);
            Log.e("saveBitMap", "saveBitmap: 图片保存到" + Environment.getExternalStorageDirectory() + "/travel1U/" + "00");
            TravelFilePath = Environment.getExternalStorageDirectory() + "/travel1U/" + "00";
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Toast.makeText(LongPictureActivity.this,"已经保存至"+Environment.getExternalStorageDirectory()+"/travel1U/"+"目录文件夹下", Toast.LENGTH_SHORT).show();
    }

}


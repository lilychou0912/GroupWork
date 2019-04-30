package com.example.myapplication.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import com.example.myapplication.utils.GradScrollView;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Parcelable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.Adapter.ScheduleAdapter;
import com.example.myapplication.Bean.Post;
import com.example.myapplication.Bean.User;
import com.example.myapplication.utils.ImageLoader;
import com.example.myapplication.utils.MyListview;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.ninegrid.ImageInfo;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadBatchListener;
import cn.bmob.v3.listener.UploadFileListener;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener, GradScrollView.ScrollViewListener{
    private ImageView backGroundImg;
    private GradScrollView scrollView;
    private RelativeLayout spaceTopChange;
    private int height;
    private List<Post> list;
    private MyListview lv;
    private SwipeRefreshLayout refresh;
    private AlertDialog al;
    private com.makeramen.roundedimageview.RoundedImageView userIcon;
    private String headUrl = "";
    private ArrayList<ImageItem> imageItems;
    private BmobUser user;
    private EditText SearchText;
    private Button btn_search;
    private String target;
    private ScheduleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_search);
        intiView();
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search();
            }
        });
    }
        private void intiView(){
            btn_search = findViewById(R.id.search);
            SearchText = findViewById(R.id.edit_search);

            lv = (MyListview) findViewById(R.id.lv);
            user = BmobUser.getCurrentUser();
            scrollView = (GradScrollView) findViewById(R.id.scrollview);
            spaceTopChange = (RelativeLayout) findViewById(R.id.spaceTopChange);
            list = new ArrayList<>();
        }



    private void search() {
        target = SearchText.getText().toString();

        list.clear();
        BmobQuery<Post> query = new BmobQuery<>();
        query.order("-createdAt");
        query.setLimit(20);
        query.addWhereEqualTo("target",target);
        query.findObjects(new FindListener<Post>() {
            @Override
            public void done(List<Post> lists, BmobException e) {
                if (e == null) {

                    list = lists;
                    adapter.addPost(list);
                    adapter.notifyDataSetChanged();
                    al.dismiss();

                }
            }
        });




    }
}


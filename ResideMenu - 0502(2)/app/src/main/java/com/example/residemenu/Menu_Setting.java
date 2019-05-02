package com.example.residemenu;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.content.Intent;
import android.provider.MediaStore;
import android.net.Uri;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.util.Log;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.app.ActionBar;

import java.io.File;

public class Menu_Setting extends AppCompatActivity {

    private Button back;
    private ImageView mImage;
    private TextView username;
    private TextView signature;
    private TextView sexuality;
    private TextView age;
    private TextView location;
    private TextView favorite;
    private RelativeLayout account;

    private SharedPreferences pref;
    private String name;
    private String sign;
    private String sex;
    private String year;
    private String city;
    private String hobby;

    private Bitmap mBitmap;
    protected static final int CHOOSE_PICTURE = 0;
    protected static final int TAKE_PICTURE = 1;
    protected static Uri tempUri;
    private static final int CROP_SMALL_PICTURE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu__setting);

        init();
        initListeners();

        //按下确认键
        backListener();

        //按下账户设置键
        accountListener();

    }

    private void accountListener() {
        //对"账户设置"按钮的响应，跳转至修改账户状态页面
        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!name.equals("")){
                    Intent intent = new Intent(Menu_Setting.this, Regulate.class);
                    startActivity(intent);
                }
                else{
                    Intent intent = new Intent(Menu_Setting.this, LoginActivity.class);
                    startActivity(intent);
                }

            }
        });
    }

    private void init() {
        pref = getSharedPreferences("LoginData", MODE_PRIVATE);
        mImage= (ImageView) findViewById(R.id.iv_image);
        username = (TextView)findViewById(R.id.in_name);
        signature = (TextView)findViewById(R.id.sign_in);
        sexuality = (TextView)findViewById(R.id.in_sex);
        age = (TextView)findViewById(R.id.in_age);
        location = (TextView)findViewById(R.id.in_city);
        favorite = (TextView)findViewById(R.id.in_hobby);

        name = pref.getString("userName", "");
        sign = pref.getString("signature", "");
        sex = pref.getString("sexuality", "");
        year = pref.getString("age", "");
        city = pref.getString("loction", "");
        hobby = pref.getString("favorite", "");

        if (!name.equals("")) {
            username.setText(name);
            signature.setText(sign);
            sexuality.setText(sex);
            age.setText(year);
            location.setText(city);
            favorite.setText(hobby);
        }
        else{
            username.setText("未登录");
            signature.setText("未登录");
            sexuality.setText("");
            age.setText("");
            location.setText("");
            favorite.setText("");
        }

        setImageToView(getIntent());
    }

    private void initListeners() {
        mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChoosePicDialog();
            }
        });
    }

    /**
     * 显示修改图片的对话框
     */

    protected void showChoosePicDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Menu_Setting.this);

        builder.setTitle("添加图片");
        String[] items = { "选择本地照片", "拍照" };

        builder.setNegativeButton("取消", null);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case CHOOSE_PICTURE: // 选择本地照片
                        Intent openAlbumIntent = new Intent (Intent.ACTION_GET_CONTENT);

                        openAlbumIntent.setType("image/*");
                        //用startActivityForResult方法，待会儿重写onActivityResult()方法，拿到图片做裁剪操作
                        startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
                        break;
                    case TAKE_PICTURE: // 拍照
                        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        tempUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "temp_image.jpg"));

                        // 将拍照所得的相片保存到SD卡根目录
                        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
                        startActivityForResult(openCameraIntent, TAKE_PICTURE);
                        break;
                }
            }

        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == MainActivity.RESULT_OK) {
            switch (requestCode) {
                case TAKE_PICTURE:
                    cutImage(tempUri); // 对图片进行裁剪处理
                    break;
                case CHOOSE_PICTURE:
                    cutImage(data.getData()); // 对图片进行裁剪处理
                    break;
                case CROP_SMALL_PICTURE:
                    if (data != null) {
                        setImageToView(data); // 让刚才选择裁剪得到的图片显示在界面上
                    }
                    break;
            }
        }
    }

    /**
     * 裁剪图片方法实现
     */
    protected void cutImage(Uri uri) {
        if (uri == null) {
            Log.i("alanjet", "The uri is not exist.");
        }
        tempUri = uri;
        Intent intent = new Intent("com.android.camera.action.CROP");
        //com.android.camera.action.CROP这个action是用来裁剪图片用的
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_SMALL_PICTURE);
    }

    /**
     * 保存裁剪之后的图片数据
     */
    protected void setImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            mBitmap = extras.getParcelable("data");
            mImage.setImageBitmap(mBitmap);//显示图片
        }
    }

    //返回键监听
    private void backListener(){
        back = (Button)findViewById(R.id.title_back);
        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //返回
                finish();
            }
        });
    }



}

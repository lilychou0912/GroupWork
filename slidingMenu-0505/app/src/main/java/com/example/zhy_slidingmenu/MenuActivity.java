package com.example.zhy_slidingmenu;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.io.File;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

public class Menu_Setting extends AppCompatActivity {


    private ImageView mImage;
    private TextView username;
    private TextView signature;
    private RelativeLayout information;
    private RelativeLayout account;
    private RelativeLayout aboutus;

    private SharedPreferences pref;
    private String name;
    private String sign;

    public static String obgID;
    private SharedPreferences.Editor editor;

    private Bitmap mBitmap;

    String path = "";
    private File mFile;
    protected static final int CHOOSE_PICTURE = 0;
    protected static final int TAKE_PICTURE = 1;
    protected static Uri tempUri;
    private static final int CROP_SMALL_PICTURE = 2;

    public static final int CHOOSE_PHOTO = 2;

    public static final int CUT_PHOTO = 3;

    public static final int TAKE_PHOTO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        user = (MyUser) getBundle().getSerializable("u");

        mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = "选择获取图片方式";
                String[] items = new String[]{"拍照", "相册"};

                new AlertDialog.Builder(Menu_Setting.this)
                        .setTitle(title)
                        .setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                switch (which) {
                                    case 0:
                                        //选择拍照
                                        pickImageFromCamera();
                                        break;
                                    case 1:
                                        //选择相册
                                        pickImageFromAlbum();
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }).show();
            }
        });

        init();
        //initListeners();

        informationListener();

        accountListener();

        aboutusListener();



    }

    private void informationListener(){
        //对“个人信息”按钮的相应，跳转至修改个人信息页面
        information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!name.equals("")) {
                    Intent intent = new Intent(Menu_Setting.this, Information_Setting.class);
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(Menu_Setting.this, LoginActivity.class);
                }
            }
        });
    }

    private void accountListener() {
        //对"账户设置"按钮的响应，跳转至修改账户状态页面
        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!name.equals("")){
                    Intent intent = new Intent(Menu_Setting.this, Account_Setting.class);
                    startActivity(intent);
                }
                else{
                    Intent intent = new Intent(Menu_Setting.this, LoginActivity.class);
                    startActivity(intent);
                }

            }
        });
    }

    private void aboutusListener() {
        //对"关于我们"按钮的响应，跳转至关于我们页面
        aboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!name.equals("")){
                    Intent intent = new Intent(Menu_Setting.this, AboutUs.class);
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
        mImage = (ImageView) findViewById(R.id.iv_image);
        username = (TextView) findViewById(R.id.in_name);
        signature = (TextView) findViewById(R.id.sign_in);

        name = pref.getString("userName", "");
        sign = pref.getString("signature", "");

        if (!name.equals("")) {
            username.setText(name);
            signature.setText(sign);

        } else {
            username.setText("未登录");
            signature.setText("未登录");
        }
    }

    //拍照
    public void pickImageFromCamera(){
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            if (!file.exists()) {
                file.mkdirs();
            }
            mFile = new File(file, System.currentTimeMillis() + ".jpg");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mFile));
            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
            startActivityForResult(intent, TAKE_PHOTO);
        } else {
            Toast.makeText(this, "请确认已经插入SD卡", Toast.LENGTH_SHORT).show();
        }
    }
    //从相册获取图片
    public void pickImageFromAlbum(){
        Intent picIntent = new Intent(Intent.ACTION_PICK, null);
        picIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(picIntent, CHOOSE_PHOTO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case TAKE_PHOTO:
                    startPhotoZoom(Uri.fromFile(mFile));
                    break;
                case CHOOSE_PHOTO:

                    if (data == null || data.getData() == null) {
                        return;
                    }
                    try {
                        Bitmap bm = null;
                        Uri originalUri = data.getData();        //获得图片的uri


                        bm = MediaStore.Images.Media.getBitmap(getContentResolver(), originalUri);        //显得到bitmap图片


                        //这里开始的第二部分，获取图片的路径：


                        String[] proj = {MediaStore.Images.Media.DATA};


                        //好像是android多媒体数据库的封装接口，具体的看Android文档

                        Cursor cursor = managedQuery(originalUri, proj, null, null, null);

                        //按我个人理解 这个是获得用户选择的图片的索引值

                        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

                        //将光标移至开头 ，这个很重要，不小心很容易引起越界

                        cursor.moveToFirst();

                        //最后根据索引值获取图片路径

                        path = cursor.getString(column_index);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    startPhotoZoom(data.getData());

                    break;
                case CUT_PHOTO:

                    if (data != null) {
                        setPicToView(data);
                    }
                    break;


            }
        }

    }
    /**
     * 打开系统图片裁剪功能
     *
     * @param uri  uri
     */
    private void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", true);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("scale", true); //黑边
        intent.putExtra("scaleUpIfNeeded", true); //黑边
        intent.putExtra("return-data", true);
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, CUT_PHOTO);

    }
    private void setPicToView(Intent data) {
        Bundle bundle = data.getExtras();
        if (bundle != null) {

//
//            Uri selectedImage = data.getData();
//
//            String[] filePathColumn = { MediaStore.Images.Media.DATA };
//
//            Cursor cursor = getContentResolver().query(selectedImage,
//                    filePathColumn, null, null, null);
//            cursor.moveToFirst();
//
//            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//            String picturePath = cursor.getString(columnIndex);
//

            //这里也可以做文件上传
            mBitmap = bundle.getParcelable("data");
            // ivHead.setImageBitmap(mBitmap);
            mImage.setImageBitmap(mBitmap);
//
//            if (picturePath!=null){
//                path = picturePath;
//            }

            if(mFile != null){
                path = mFile.getPath();
            }

            Toast.makeText(Menu_Setting.this,"path:"+path,Toast.LENGTH_SHORT).show();

            final BmobFile bmobFile = new BmobFile(new File(path));
            //Bmob这个上传文件的貌似不成功..........................
            bmobFile.uploadblock(new UploadFileListener() {

                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        Toast.makeText(Menu_Setting.this, "pic is success", Toast.LENGTH_SHORT).show();
                        // MyUser myUser =MyUser.getCurrentUser(MyUser.class);
                        //得到上传的图片地址
                        String fileUrl = bmobFile.getFileUrl();
                        user.setAvatar(fileUrl);
                        //更新图片地址
                        user.update(user.getObjectId(), new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    Toast.makeText(Menu_Setting.this, "update", Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
                    }
                }
            });

        }
    }

    // setImageToView(getIntent());


    //private void initListeners() {
    //mImage.setOnClickListener(new View.OnClickListener() {
    // @Override
    // public void onClick(View v) {

    // }
    //  });
    //        }



    /**
     * 显示修改图片的对话框
     */

    /****
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


     protected void setImageToView(Intent data) {
     Bundle extras = data.getExtras();
     if (extras != null) {
     mBitmap = extras.getParcelable("data");
     mImage.setImageBitmap(mBitmap);//显示图片
     }
     }
     ****/





}

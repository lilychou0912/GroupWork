package com.example.myapplication.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myapplication.Bean.MyUser;
import com.example.myapplication.R;
import com.example.myapplication.SlidingMenu;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

@SuppressLint({ "SetJavaScriptEnabled", "JavascriptInterface" })
public class SlidingActivity extends Activity {
	private SlidingMenu mMenu;

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
	private String savePath;
	private File filePic;

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


	private WebView myWebView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_zuhe);
		mMenu = (SlidingMenu) findViewById(R.id.id_menu);
		//注册组件
		init();
		//对更换头像进行监听
		imageListenser();
		informationListener();
		accountListener();
		aboutusListener();

		RadioButton MapBtn = (RadioButton) findViewById(R.id.button_map);
		MapBtn.setChecked(true);

		myWebView = (WebView) findViewById(R.id.mapWebview);
		//点击底部按钮
		bottomListener();
		//初始化界面，载入已标记地图
		//退出当前Activity或者跳转到新Activity时被调用
		WebSettings webSettings = myWebView.getSettings();
		//允许JavaScript执行
		webSettings.setJavaScriptEnabled(true);
		webSettings.setDefaultTextEncodingName("GBK");
		myWebView.setWebViewClient(new WebViewClient());

		myWebView.setWebChromeClient(new WebChromeClient() {

			@Override
			public boolean onJsAlert(WebView view, String url, String message,
									 JsResult result) {
				// TODO Auto-generated method stub
				return super.onJsAlert(view, url, message, result);
			}

		});
		// 添加一个对象, 让javascript可以访问该对象的方法,
		myWebView.addJavascriptInterface(new SlidingActivity.WebAppInterface(this),
				"myInterfaceName");

		// 载入页面：本地html资源文件，放在assets文件夹下
		myWebView.loadUrl("file:///android_asset/html/map.html");
		//设置 缓存模式
		myWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		//myWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
		// 开启 DOM storage API 功能
		myWebView.getSettings().setDomStorageEnabled(true);
		//这样你就可以在返回前一个页面的时候不刷新了

	}

	private void imageListenser(){
		mImage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String title = "选择获取图片方式";
				String[] items = new String[]{"拍照", "相册"};

				new AlertDialog.Builder(SlidingActivity.this)
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

	}

	private void informationListener(){
		//对“个人信息”按钮的相应，跳转至修改个人信息页面
		information.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final MyUser userInfo =  BmobUser.getCurrentUser(MyUser.class);
				name = userInfo.getUsername();
				if(!name.equals("")) {
					Intent intent = new Intent(SlidingActivity.this, InformationSettingActivity.class);
					startActivityForResult(intent,100);
				}
				else {
					Intent intent = new Intent(SlidingActivity.this, LoginActivity.class);
				}
			}
		});
	}

	private void accountListener() {
		//对"退出登录按钮的响应
		account.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				BmobUser.logOut();
				Intent intent = new Intent(SlidingActivity.this, LoginActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}

	private void aboutusListener() {
		//对"关于我们"按钮的响应，跳转至关于我们页面
		aboutus.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final MyUser userInfo =  BmobUser.getCurrentUser(MyUser.class);
				name = userInfo.getUsername();
				if(!name.equals("")){
					Intent intent = new Intent(SlidingActivity.this, AboutUsActivity.class);
					startActivity(intent);
				}
				else{
					Intent intent = new Intent(SlidingActivity.this, LoginActivity.class);
					startActivity(intent);
				}

			}
		});
	}

	private void init() {

		mImage = (ImageView) findViewById(R.id.iv_image);
		username = (TextView) findViewById(R.id.in_name);
		signature = (TextView) findViewById(R.id.sign_in);
		aboutus = findViewById(R.id.aboutus);
		account = findViewById(R.id.accouont);
		information = findViewById(R.id.information);

		MyUser user = BmobUser.getCurrentUser(MyUser.class);
		name = user.getUsername();
		sign = user.getSignature();
		Glide.with(SlidingActivity.this)
				.load(user.getImage())
				.into(mImage);

		if (!name.equals("")) {
			username.setText(name);
			signature.setText(sign);


		} else {
			username.setText("未登录");
			signature.setText("未登录");
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		init();
	}

	@Override
	protected void onStart() {
		super.onStart();
		init();
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
			mBitmap = bundle.getParcelable("data");
			mImage.setImageBitmap(mBitmap);

			if(mFile != null){
				path = mFile.getPath();
			}

			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				savePath = "/sdcard/dskqxt/pic/";
			} else {
				Log.d("xxx", "saveBitmap: 1return");
				return;
			}
			try {
				filePic = new File(savePath + 1 + ".jpg");
				if (!filePic.exists()) {
					filePic.getParentFile().mkdirs();
					filePic.createNewFile();
				}
				FileOutputStream fos = new FileOutputStream(filePic);
				mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
				fos.flush();
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
				Log.d("xxx", "saveBitmap: 2return");
				return;
			}
			Log.d("xxx", "saveBitmap: " + filePic.getAbsolutePath());
		}

			Toast.makeText(SlidingActivity.this,"path:"+path,Toast.LENGTH_SHORT).show();

			final BmobFile bmobFile = new BmobFile(new File(filePic.getAbsolutePath()));
			//Bmob这个上传文件的貌似不成功..........................
			bmobFile.uploadblock(new UploadFileListener() {

				@Override
				public void done(BmobException e) {
					               if (e == null) {
					                  Toast.makeText(SlidingActivity.this, "pic is success", Toast.LENGTH_SHORT).show();
					                  MyUser user =MyUser.getCurrentUser(MyUser.class);
					//得到上传的图片地址
					                      user.setImage( bmobFile.getFileUrl());
					                     // user.setAvatar(fileUrl);
					//更新图片地址
					                     user.update(user.getObjectId(), new UpdateListener() {
					                      @Override
					                       public void done(BmobException e) {
					if (e == null) {
						                             Toast.makeText(SlidingActivity.this, "update", Toast.LENGTH_SHORT).show();

						                             }
						                       }
						                   });
					}
				}
			});

		}

		@Override
		protected void onNewIntent(Intent intent) {
		    super.onNewIntent(intent);
		}

	    public class WebAppInterface {
		private String tag;
		private String code;

		Context mContext;

		WebAppInterface(Context c) {
			mContext = c;
		}

		@JavascriptInterface
		public void backToApp() {
			finish();
		}

		@JavascriptInterface
		public String getTag() {
			return tag;
		}

		@JavascriptInterface
		public void setTag(String tag) {
			this.tag = tag;
		}

		@JavascriptInterface
		public String getCode() {
			return code;
		}

		@JavascriptInterface
		public void setCode(String code) {
			this.code = code;
		}
	}

	//对底部按钮的响应
	private void bottomListener() {
		RadioButton NtOverviewBtn = (RadioButton) findViewById(R.id.button_edit);
		RadioButton SearchBtn = (RadioButton) findViewById(R.id.button_search);
		RadioButton CollectBtn = (RadioButton) findViewById(R.id.button_collect);
		//给btn1绑定监听事件
		NtOverviewBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// 给bnt添加点击响应事件
				Intent intent = new Intent(SlidingActivity.this, NoteOverviewActivity.class);
				//启动
				startActivity(intent);
				overridePendingTransition(0,0);
			}
		});
		SearchBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// 给bnt添加点击响应事件
				Intent intent = new Intent(SlidingActivity.this, SearchActivity.class);
				//启动
				startActivity(intent);
				overridePendingTransition(0,0);
			}
		});
		CollectBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// 给bnt添加点击响应事件
				Intent intent = new Intent(SlidingActivity.this, CollectionActivity.class);
				//启动
				startActivity(intent);
				overridePendingTransition(0,0);
			}
		});
	}


//设置返回按钮：不应该退出程序---而是返回桌面
	//复写onKeyDown事件
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent home = new Intent(Intent.ACTION_MAIN);
			home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			home.addCategory(Intent.CATEGORY_HOME);
			startActivity(home);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}





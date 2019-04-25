package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.Toast;
@SuppressLint({ "SetJavaScriptEnabled", "JavascriptInterface" })
public class NoteEditActivity extends AppCompatActivity {

    private WebView timeWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_note);
        ImageButton NtOverviewBtn = (ImageButton) findViewById(R.id.button_edit);
        ImageButton MapBtn = (ImageButton) findViewById(R.id.button_map);
        ImageButton SearchBtn = (ImageButton) findViewById(R.id.button_search);
        ImageButton CollectBtn = (ImageButton) findViewById(R.id.button_collect);
        timeWebView = (WebView) findViewById(R.id.timeWebview);
        //退出当前Activity或者跳转到新Activity时被调用
        WebSettings webSettings=timeWebView.getSettings();
        //允许JavaScript执行
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDefaultTextEncodingName("GBK");
        timeWebView.setWebViewClient(new WebViewClient());

        timeWebView.setWebChromeClient(new WebChromeClient()
        {

            @Override
            public boolean onJsAlert(WebView view, String url, String message,
                                     JsResult result)
            {
                // TODO Auto-generated method stub
                return super.onJsAlert(view, url, message, result);
            }

        });
        // 添加一个对象, 让javascript可以访问该对象的方法,
        timeWebView.addJavascriptInterface(new WebAppInterface(this),
                "timeInterfaceName");

        // 载入页面：本地html资源文件，放在assets文件夹下
        timeWebView.loadUrl("file:///android_asset/html/time.html");
        //设置 缓存模式
        timeWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        //myWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        // 开启 DOM storage API 功能
        timeWebView.getSettings().setDomStorageEnabled(true);
        //这样你就可以在返回前一个页面的时候不刷新了

    }

    class WebAppInterface{
        Context mContext;
        WebAppInterface(Context c){
            mContext=c;
        }
        public void showToast(String toast){
            Toast.makeText(mContext, toast, Toast.LENGTH_LONG).show();            }
    }
}

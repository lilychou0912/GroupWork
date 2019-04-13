package com.example.test;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class BottomNavigationViewActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation);

        final TextView tv_whichItemSelected = (TextView) findViewById(R.id.tv_whichItemSelected);

        BottomNavigationView bnv_001 = (BottomNavigationView) findViewById(R.id.bnv_001);

        //为底部导航设置条目选中监听
        bnv_001.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(
                    @NonNull
                            MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_map:
                        tv_whichItemSelected.setText(item.getTitle());
                        break;
                    case R.id.action_edit:
                        tv_whichItemSelected.setText(item.getTitle());
                        break;
                    case R.id.action_collect:
                        tv_whichItemSelected.setText(item.getTitle());
                        break;
                }
                item.setChecked(true);
                return true;    //这里返回true，表示事件已经被处理。如果返回false，为了达到条目选中效果，还需要下面的代码
                //   不论点击了哪一个，都手动设置为选中状态true（该控件并没有默认实现)
                // 。如果不设置，只有第一个menu展示的时候是选中状态，其他的即便被点击选中了，图标和文字也不会做任何更改
            }
        });

        //默认选中底部导航栏中的第1三个
        bnv_001.getMenu().getItem(0).setChecked(true);
    }
}


package com.example.a90537.partone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FirstActivity extends AppCompatActivity {

    Button but_in_app;
    Button but_out_app;
    Intent a, b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        but_in_app = (Button) findViewById(R.id.button1);
        but_in_app.setOnClickListener(new ButtonListener());

        but_out_app = (Button) findViewById(R.id.button2);
        but_out_app.setOnClickListener(new ButtonListener());
    }


    private class ButtonListener implements View.OnClickListener {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.button1:
                    a = new Intent(FirstActivity.this, MainActivity.class);
                    startActivity(a);
                    break;
                case R.id.button2:
                    b = new Intent(FirstActivity.this, Main2Activity.class);
                    startActivity(b);
                    break;
                default:
                    break;
            }
        }
    }
}

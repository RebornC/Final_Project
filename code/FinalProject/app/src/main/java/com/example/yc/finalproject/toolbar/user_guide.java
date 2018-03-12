package com.example.yc.finalproject.toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yc.finalproject.R;


/**
 * Created by yc on 2017/12/18.
 */

public class user_guide extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_guide);
        ImageView close = (ImageView) findViewById(R.id.close_2);
        TextView text = (TextView) findViewById(R.id.introduction) ;
        text.setMovementMethod(ScrollingMovementMethod.getInstance());
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}

package com.example.yc.finalproject.toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.example.yc.finalproject.main.MainActivity;
import com.example.yc.finalproject.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by yc on 2017/12/15.
 */

public class Welcome extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.welcome);
        final Intent it = new Intent(this, MainActivity.class); //下一步转向Mainctivity
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                startActivity(it); //执行意图
            }
        };
        timer.schedule(task, 1000 * 1); //2秒后跳转
    }
}

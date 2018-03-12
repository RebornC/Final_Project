package com.example.yc.finalproject.dairyMemo;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.yc.finalproject.R;
import com.example.yc.finalproject.dataBase.birthDataBase;

/**
 * Created by yc on 2017/12/20.
 */

public class dairy_content extends AppCompatActivity {
    private TextView time_text;
    private TextView topic_Text;
    private TextView content_Text;
    private Button fix_diary;
    private boolean fix_state; //若内容修改过则为true
    private int pos;
    private String topic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dairy_content);

        fix_state = false;
        time_text = (TextView) findViewById(R.id.time);
        topic_Text = (TextView) findViewById(R.id.topic);
        content_Text = (TextView) findViewById(R.id.content);
        fix_diary = (Button) findViewById(R.id.fix_dairy);

        Intent intent = getIntent();
        pos = intent.getIntExtra("pos",-1);

        birthDataBase db = new birthDataBase(getBaseContext());
        SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();
        final Cursor cursor = sqLiteDatabase.rawQuery("select * from Info_4", null);
        cursor.moveToPosition(pos);
        topic = cursor.getString(2);
        time_text.setText(cursor.getString(1));
        topic_Text.setText("『"+cursor.getString(2)+"』");
        content_Text.setText(cursor.getString(3));

        //点击按钮 可跳转到编辑页面
        fix_diary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(dairy_content.this, dairy_Additem.class);
                intent.putExtra("code",2);
                intent.putExtra("topic",topic);
                startActivityForResult(intent, 2);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intentData) {
        if (requestCode == 2) {
            fix_state = true;
            String topic = intentData.getStringExtra("topic");
            if (!topic.equals("null")){
                birthDataBase db = new birthDataBase(getBaseContext());
                SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();
                Cursor cursor = sqLiteDatabase.rawQuery("select * from Info_4 where topic like ?", new String[]{topic});
                if (cursor.moveToFirst()) {
                    topic_Text.setText("『"+topic+"』");
                    this.topic = topic;
                    content_Text.setText(cursor.getString(3));
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        if (fix_state) {
            intent.putExtra("fix",1);
            intent.putExtra("topic",this.topic);
            intent.putExtra("pos",pos);
            setResult(2,intent);
            finish();
        } else {
            intent.putExtra("fix",0);
            setResult(2,intent);
            finish();
        }
        super.onBackPressed();
    }

}
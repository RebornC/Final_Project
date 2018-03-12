package com.example.yc.finalproject.dairyMemo;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.yc.finalproject.R;
import com.example.yc.finalproject.dataBase.birthDataBase;

import java.util.Calendar;

/**
 * Created by yc on 2017/12/18.
 */

public class dairy_Additem extends AppCompatActivity {
    private Button add_confirm;
    private EditText topic_Text;
    private EditText content_Text;
    private int request_code;
    private String topic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dairy_memo_additem);

        request_code = getIntent().getIntExtra("code",1);

        findView();

        if (request_code == 1) {
            myClick_addDairy();
        } else if (request_code == 2) {
            topic = getIntent().getStringExtra("topic");
            myClick_fixDairy();
        }
    }

    void findView() {
        add_confirm = (Button) findViewById(R.id.add_confirm_4);
        topic_Text = (EditText) findViewById(R.id.topic_text);
        content_Text = (EditText) findViewById(R.id.content_text);
    }

    void myClick_addDairy() {
        add_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                birthDataBase db = new birthDataBase(getBaseContext());
                SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();
                if (topic_Text.getText().toString().equals("")) {
                    Toast.makeText(dairy_Additem.this, "麻烦填写主题哦", Toast.LENGTH_SHORT).show();
                } else {
                    Calendar c = Calendar.getInstance();
                    String year = setString(c.get(Calendar.YEAR));
                    String month = setString(c.get(Calendar.MONTH)+1);
                    String day = setString(c.get(Calendar.DAY_OF_MONTH));
                    String hour = setString(c.get(Calendar.HOUR_OF_DAY));
                    String minute = setString(c.get(Calendar.MINUTE));
                    String current_time = year + "/" + month +"/" + day +" " + hour + ":" + minute;
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("time", current_time);
                    contentValues.put("topic", topic_Text.getText().toString());
                    contentValues.put("content", content_Text.getText().toString());
                    sqLiteDatabase.insert("Info_4", null, contentValues);
                    sqLiteDatabase.close();
                    setResult(1, new Intent());
                    finish();
                }
            }
        });
    }

    void myClick_fixDairy() {
        add_confirm.setText("修改");
        birthDataBase db = new birthDataBase(getBaseContext());
        final SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();
        final Cursor cursor = sqLiteDatabase.rawQuery("select * from Info_4 where topic like ?", new String[]{topic});
        if (cursor.moveToFirst()) {
            topic_Text.setText(topic);
            content_Text.setText(cursor.getString(3));
        }
        add_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (topic_Text.getText().toString().equals("")) {
                    Toast.makeText(dairy_Additem.this, "麻烦填写主题哦", Toast.LENGTH_SHORT).show();
                } else {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("time", cursor.getString(1));
                    contentValues.put("topic", topic_Text.getText().toString());
                    contentValues.put("content", content_Text.getText().toString());
                    sqLiteDatabase.update("Info_4",contentValues,"topic = ?",new String[]{topic});
                    sqLiteDatabase.close();
                    Intent intent = new Intent();
                    intent.putExtra("topic",topic_Text.getText().toString());
                    setResult(2, intent);
                    finish();
                }
            }
        });
    }

    String setString(int num) {
        String str;
        if (num < 10) { // 比如 将秒数5变为05
            str = "0" + Integer.toString(num);
        } else {
            str = Integer.toString(num);
        }
        return str;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("topic","null");
        setResult(2, intent);
        finish();
        super.onBackPressed();
    }
}

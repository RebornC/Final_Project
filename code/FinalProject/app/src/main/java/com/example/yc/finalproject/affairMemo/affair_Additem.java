package com.example.yc.finalproject.affairMemo;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yc.finalproject.R;
import com.example.yc.finalproject.dataBase.birthDataBase;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by yc on 2017/12/18.
 */

public class affair_Additem extends AppCompatActivity {
    private Context mcontext;
    private Button add_confirm;
    private EditText affair_Text;
    private TextView deadline_Text;
    private int pos;
    private String time;

    private int Year;
    private int Month;
    private int Day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.affair_memo_additem);

        initDate();
        pos = getIntent().getIntExtra("pos",-1);
        findView();
        if (pos == -1) {
            myClick_addItem();
        } else {
            time = getIntent().getStringExtra("time");
            myClick_fixItem();
        }
    }

    void initDate() {
        android.icu.util.Calendar d = android.icu.util.Calendar.getInstance(Locale.CHINA);
        Date myDate = new Date();
        d.setTime(myDate);
        Year = d.get(android.icu.util.Calendar.YEAR);
        Month = d.get(android.icu.util.Calendar.MONTH);
        Day = d.get(android.icu.util.Calendar.DAY_OF_MONTH);
    }

    void findView() {
        mcontext = affair_Additem.this;
        add_confirm = (Button) findViewById(R.id.add_confirm_3);
        affair_Text = (EditText) findViewById(R.id.affair_text);
        deadline_Text = (TextView) findViewById(R.id.deadline_text);
    }

    void myClick_addItem() {
        deadline_Text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(mcontext, DatePickerDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Year = year;
                        Month = month;
                        Day = dayOfMonth;

                        String y = String.valueOf(year);
                        String m = String.valueOf(month);
                        String d = String.valueOf(dayOfMonth);
                        if (month + 1 < 10) {m = "0" + String.valueOf(month + 1);}
                        if (dayOfMonth < 10) {d = "0" + String.valueOf(dayOfMonth);}

                        deadline_Text.setText(y + "-" + m + "-" + d);
                    }
                }, Year, Month, Day);
                datePickerDialog.show();
            }
        });

        add_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                birthDataBase db = new birthDataBase(getBaseContext());
                SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();
                if (affair_Text.getText().toString().equals("")) {
                    Toast.makeText(affair_Additem.this, "请完善内容哦", Toast.LENGTH_SHORT).show();
                } else if (deadline_Text.getText().toString().equals("")) {
                    Toast.makeText(affair_Additem.this, "日期不能為空", Toast.LENGTH_SHORT).show();
                } else {
                    Calendar c = Calendar.getInstance();
                    String year = setString(c.get(Calendar.YEAR));
                    String month = setString(c.get(Calendar.MONTH)+1);
                    String day = setString(c.get(Calendar.DAY_OF_MONTH));
                    String hour = setString(c.get(Calendar.HOUR_OF_DAY));
                    String minute = setString(c.get(Calendar.MINUTE));
                    if (month.length() < 2) {
                        month = "0" + month;
                    }
                    String current_time = year + "-" + month +"-" + day +" " + hour + ":" + minute;
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("time", current_time);
                    contentValues.put("affair", affair_Text.getText().toString());
                    contentValues.put("deadline", deadline_Text.getText().toString());
                    contentValues.put("finish", "0");
                    sqLiteDatabase.insert("Info_3", null, contentValues);
                    sqLiteDatabase.close();
                    setResult(1, new Intent());
                    finish();
                }
            }
        });
    }

    void myClick_fixItem() {
        add_confirm.setText("修改");
        birthDataBase db = new birthDataBase(getBaseContext());
        final SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();
        final Cursor cursor = sqLiteDatabase.rawQuery("select * from Info_3 where time like ?", new String[]{time});
        if (cursor.moveToFirst()) {
            affair_Text.setText(cursor.getString(2));
            deadline_Text.setText(cursor.getString(3));
        }

        add_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (affair_Text.getText().toString().equals("")) {
                    Toast.makeText(affair_Additem.this, "请完善内容哦", Toast.LENGTH_SHORT).show();
                } else if (deadline_Text.getText().toString().equals("")) {
                    Toast.makeText(affair_Additem.this, "日期不能為空", Toast.LENGTH_SHORT).show();
                } else {
                    Calendar c = Calendar.getInstance();
                    String year = setString(c.get(Calendar.YEAR));
                    String month = setString(c.get(Calendar.MONTH)+1);
                    String day = setString(c.get(Calendar.DAY_OF_MONTH));
                    String hour = setString(c.get(Calendar.HOUR_OF_DAY));
                    String minute = setString(c.get(Calendar.MINUTE));
                    if (month.length() < 2) {
                        month = "0" + month;
                    }
                    String current_time = year + "-" + month +"-" + day +" " + hour + ":" + minute;
                    time = current_time;
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("time", current_time);
                    contentValues.put("affair", affair_Text.getText().toString());
                    contentValues.put("deadline", deadline_Text.getText().toString());
                    contentValues.put("finish", "0");
                    sqLiteDatabase.update("Info_3",contentValues,"time = ?",new String[]{time});
                    sqLiteDatabase.close();
                    Intent intent = new Intent();
                    intent.putExtra("time",current_time);
                    intent.putExtra("pos",pos);
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
        intent.putExtra("time","null");
        setResult(2, intent);
        finish();
        super.onBackPressed();
    }
}

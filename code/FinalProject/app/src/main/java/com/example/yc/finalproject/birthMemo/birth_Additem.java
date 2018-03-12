package com.example.yc.finalproject.birthMemo;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.util.Calendar;
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

import java.util.Date;
import java.util.Locale;

/**
 * Created by yc on 2017/12/18.
 */

public class birth_Additem extends AppCompatActivity {
    private Context mcontext;
    private Button add_confirm;
    private EditText name_Text;
    private TextView birth_Text;
    private EditText gift_Text;

    private int Year;
    private int Month;
    private int Day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.birth_memo_additem);
        initDate();
        findView();
        myClick();
    }

    void initDate() {
        Calendar d = Calendar.getInstance(Locale.CHINA);
        Date myDate = new Date();
        d.setTime(myDate);
        Year = d.get(Calendar.YEAR);
        Month = d.get(Calendar.MONTH);
        Day = d.get(Calendar.DAY_OF_MONTH);
    }

    void findView() {
        mcontext = birth_Additem.this;
        add_confirm = (Button) findViewById(R.id.add_confirm);
        name_Text = (EditText) findViewById(R.id.name_text);
        birth_Text = (TextView) findViewById(R.id.birth_text);
        gift_Text = (EditText) findViewById(R.id.gift_text);
    }

    void myClick() {
        add_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                birthDataBase db = new birthDataBase(getBaseContext());
                SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();
                Cursor cursor = sqLiteDatabase.rawQuery("select * from Info where name like ?", new String[]{name_Text.getText().toString()});

                if (name_Text.getText().toString().equals("")) {
                    Toast.makeText(birth_Additem.this, "名字为空，请完善", Toast.LENGTH_SHORT).show();
                } else if (birth_Text.getText().toString().equals("")) {
                    Toast.makeText(birth_Additem.this, "日期不能為空", Toast.LENGTH_SHORT).show();
                } else if (cursor.moveToFirst()) {
                    Toast.makeText(birth_Additem.this, "名字重复啦，请检查", Toast.LENGTH_SHORT).show();
                } else {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("name", name_Text.getText().toString());
                    contentValues.put("birth", birth_Text.getText().toString());
                    contentValues.put("gift", gift_Text.getText().toString());
                    sqLiteDatabase.insert("Info", null, contentValues);
                    sqLiteDatabase.close();
                    setResult(1, new Intent());
                    finish();
                }
            }
        });

        birth_Text.setOnClickListener(new View.OnClickListener() {
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
                        birth_Text.setText(y + "-" + m + "-" + d);
                    }
                }, Year, Month, Day);
                datePickerDialog.show();
            }
        });
    }
}

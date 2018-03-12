package com.example.yc.finalproject.pawMeno;

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

/**
 * Created by yc on 2017/12/18.
 */

public class psw_Additem extends AppCompatActivity {
    private Button add_confirm;
    private EditText name_Text;
    private EditText id_Text;
    private EditText psw_Text;
    private String name;
    private int pos;//若是修改条目，则记录修改的是第几个条目

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.psw_memo_additem);
        //name=null，则增加条目；name不为null，则修改已有条目
        name = getIntent().getExtras().getString("name");
        pos = getIntent().getExtras().getInt("position");
        findView();

        //判断是增加条目还是修改已有条目后，为按钮设置相应监听器
        if (name.equals("null")) {
            myClick_addItem();
        } else {
            myClick_fixItem();
        }
    }

    void findView() {
        add_confirm = (Button) findViewById(R.id.add_confirm_2);
        name_Text = (EditText) findViewById(R.id.name_text);
        id_Text = (EditText) findViewById(R.id.id_text);
        psw_Text = (EditText) findViewById(R.id.psw_text);
    }

    void myClick_addItem() {
        add_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                birthDataBase db = new birthDataBase(getBaseContext());
                SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();
                Cursor cursor = sqLiteDatabase.rawQuery("select * from Info_2 where name like ?", new String[]{name_Text.getText().toString()});
                if (name_Text.getText().toString().equals("")) {
                    Toast.makeText(psw_Additem.this, "账号所属类别为空，请完善", Toast.LENGTH_SHORT).show();
                } else if (cursor.moveToFirst()) {
                    Toast.makeText(psw_Additem.this, "账号类别重复啦，请检查", Toast.LENGTH_SHORT).show();
                } else {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("name", name_Text.getText().toString());
                    contentValues.put("id", id_Text.getText().toString());
                    contentValues.put("password", psw_Text.getText().toString());
                    sqLiteDatabase.insert("Info_2", null, contentValues);
                    sqLiteDatabase.close();
                    setResult(1, new Intent());
                    finish();
                }
            }
        });
    }

    void myClick_fixItem() {
        add_confirm.setText("修改");
        final birthDataBase db = new birthDataBase(getBaseContext());
        final SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();
        final Cursor cursor = sqLiteDatabase.rawQuery("select * from Info_2 where name like ?", new String[]{name});
        if (cursor.moveToFirst()) {
            name_Text.setText(cursor.getString(1));
            id_Text.setText(cursor.getString(2));
            psw_Text.setText(cursor.getString(3));
        }
        add_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name_Text.getText().toString().equals("")) {
                    Toast.makeText(psw_Additem.this, "账号所属类别为空，请完善", Toast.LENGTH_SHORT).show();
                } else if (cursor.moveToFirst() && !name.equals(name_Text.getText().toString())){
                    Toast.makeText(psw_Additem.this, "不可更改类别名", Toast.LENGTH_SHORT).show();
                } else {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("name", name_Text.getText().toString());
                    contentValues.put("id", id_Text.getText().toString());
                    contentValues.put("password", psw_Text.getText().toString());
                    sqLiteDatabase.update("Info_2",contentValues,"name = ?", new String[]{name});
                    sqLiteDatabase.close();
                    Intent intent = new Intent();
                    intent.putExtra("position",pos);
                    intent.putExtra("name",name_Text.getText().toString());
                    setResult(2, intent);
                    finish();
                }
            }
        });
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("position",-1);
        intent.putExtra("name","null");
        setResult(2, intent);
        finish();
        super.onBackPressed();
    }
}

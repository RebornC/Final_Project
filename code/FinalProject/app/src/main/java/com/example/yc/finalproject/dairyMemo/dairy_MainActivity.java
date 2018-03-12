package com.example.yc.finalproject.dairyMemo;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.yc.finalproject.R;
import com.example.yc.finalproject.adapter.dairy_cardAdapter;
import com.example.yc.finalproject.dataBase.birthDataBase;
import com.example.yc.finalproject.model.dairy_user;

import java.util.ArrayList;
import java.util.List;

public class dairy_MainActivity extends AppCompatActivity {

    private List<dairy_user> userList;
    private RecyclerView recyclerView;
    private dairy_cardAdapter adapter;
    private birthDataBase db;
    private SQLiteDatabase sqLiteDatabase;
    private Cursor cursor;
    private FloatingActionButton add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dairy_activity_main);
        findView();
        init();//对列表里应该呈现的数据信息进行更新
        myClick();
    }

    void init() {
        db = new birthDataBase(getBaseContext());
        sqLiteDatabase = db.getWritableDatabase();
        userList = new ArrayList<>();
        cursor = sqLiteDatabase.rawQuery("select * from Info_4", null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                userList.add(new dairy_user(cursor.getString(1), cursor.getString(2), cursor.getString(3)));
            }
            GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
            recyclerView.setLayoutManager(layoutManager);
            adapter = new dairy_cardAdapter(userList);
            recyclerView.setAdapter(adapter);
        }
    }

    void findView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        add = (FloatingActionButton) findViewById(R.id.add_dairy);
    }

    void myClick() {
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(dairy_MainActivity.this, dairy_Additem.class);
                intent.putExtra("code",1);
                startActivityForResult(intent, 1);
            }
        });

        adapter.setOnClickListener(new dairy_cardAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                //单击事件
                Intent it = new Intent(dairy_MainActivity.this, dairy_content.class);
                it.putExtra("pos", position);
                startActivityForResult(it,2);
            }

            @Override
            public void onLongClick(int position) {//长按事件
                final int pos = position;
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(dairy_MainActivity.this);
                alertDialog.setMessage("是否删除？")
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                db = new birthDataBase(getBaseContext());
                                sqLiteDatabase = db.getWritableDatabase();
                                sqLiteDatabase.execSQL("DELETE FROM Info_4 WHERE time  = ?", new String[]{userList.get(pos).getTime()});
                                userList.remove(pos);
                                adapter.notifyItemRemoved(pos);
                            }
                        })
                        .setNegativeButton("否", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        }).create();
                alertDialog.show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intentData) {
        db = new birthDataBase(getBaseContext());
        sqLiteDatabase = db.getWritableDatabase();
        if (requestCode == 1) {
                cursor = sqLiteDatabase.rawQuery("select * from Info_4", null);
                cursor.moveToLast();
                userList.add(new dairy_user(cursor.getString(1), cursor.getString(2), cursor.getString(3)));
                adapter.notifyDataSetChanged();
        } else if (requestCode == 2) {
            int fix = intentData.getIntExtra("fix",0);
            if (fix == 1) {
                String topic = intentData.getStringExtra("topic");
                cursor = sqLiteDatabase.rawQuery("select * from Info_4 where topic like ?", new String[]{topic});
                if (cursor.moveToFirst()) {
                    int pos = intentData.getIntExtra("pos",0);
                    userList.set(pos,new dairy_user(cursor.getString(1), cursor.getString(2), cursor.getString(3)));
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }
}

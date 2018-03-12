package com.example.yc.finalproject.pawMeno;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.yc.finalproject.R;
import com.example.yc.finalproject.dataBase.birthDataBase;
import com.example.yc.finalproject.adapter.psw_cardAdapter;
import com.example.yc.finalproject.model.psw_user;

import java.util.ArrayList;
import java.util.List;

public class psw_MainActivity extends AppCompatActivity {

    private List<psw_user> userList;
    private RecyclerView recyclerView;
    private psw_cardAdapter adapter;
    private birthDataBase db;
    private SQLiteDatabase sqLiteDatabase;
    private Cursor cursor;
    private FloatingActionButton add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.psw_activity_main);
        findView();
        init();//对列表里应该呈现的数据信息进行更新
        myClick();
    }

    void init() {
        db = new birthDataBase(getBaseContext());
        sqLiteDatabase = db.getWritableDatabase();
        userList = new ArrayList<>();
        cursor = sqLiteDatabase.rawQuery("select * from Info_2", null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                userList.add(new psw_user(cursor.getString(1), "ID：" + cursor.getString(2), "Password：" + cursor.getString(3)));
            }
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            adapter = new psw_cardAdapter(userList);
            recyclerView.setAdapter(adapter);
        }
    }

    void findView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        add = (FloatingActionButton) findViewById(R.id.add_psw);
    }

    void myClick() {
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(psw_MainActivity.this, psw_Additem.class);
                intent.putExtra("name","null");//条目类别为null，表示增加条目
                intent.putExtra("postion",-1);
                startActivityForResult(intent, 1);
            }
        });

        adapter.setOnClickListener(new psw_cardAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                //单击 修改/编辑 已有密码条目
                final int pos = position;
                Intent intent = new Intent(psw_MainActivity.this, psw_Additem.class);
                intent.putExtra("name",userList.get(pos).getLogin());//条目为已有的某一项的类别，表示修改条目
                intent.putExtra("position",pos);
                startActivityForResult(intent, 2);
            }

            @Override
            public void onLongClick(int position) {//长按删除条目
                final int pos = position;
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(psw_MainActivity.this);
                alertDialog.setMessage("是否删除？")
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                db = new birthDataBase(getBaseContext());
                                sqLiteDatabase = db.getWritableDatabase();
                                sqLiteDatabase.execSQL("DELETE FROM Info_2 WHERE name  = ?", new String[]{userList.get(pos).getLogin()});
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
        if (requestCode == 1) {
            if (resultCode == 1) {
                db = new birthDataBase(getBaseContext());
                sqLiteDatabase = db.getWritableDatabase();
                cursor = sqLiteDatabase.rawQuery("select * from Info_2", null);
                cursor.moveToLast();
                userList.add(new psw_user(cursor.getString(1), "ID：" + cursor.getString(2), "Password：" + cursor.getString(3)));
                adapter.notifyDataSetChanged();
            }
        } else if (requestCode == 2) {
            int pos = intentData.getIntExtra("position",-1);
            if (pos > -1) {
                String name = intentData.getStringExtra("name");
                db = new birthDataBase(getBaseContext());
                sqLiteDatabase = db.getWritableDatabase();
                cursor = sqLiteDatabase.rawQuery("select * from Info_2 where name like ?", new String[]{name});
                if (cursor.moveToFirst() && pos > -1) {
                    userList.set(pos,new psw_user(cursor.getString(1), "ID：" + cursor.getString(2), "Password：" + cursor.getString(3)));
                    adapter.notifyDataSetChanged();
                }
            }

        }
    }
}

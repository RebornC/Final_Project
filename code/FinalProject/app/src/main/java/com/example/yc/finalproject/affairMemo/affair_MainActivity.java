package com.example.yc.finalproject.affairMemo;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.yc.finalproject.R;
import com.example.yc.finalproject.adapter.affair_cardAdapter;
import com.example.yc.finalproject.model.affair_user;
import com.example.yc.finalproject.dataBase.birthDataBase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class affair_MainActivity extends AppCompatActivity {

    private List<affair_user> userList;
    private RecyclerView recyclerView;
    private affair_cardAdapter adapter;
    private birthDataBase db;
    private SQLiteDatabase sqLiteDatabase;
    private Cursor cursor;
    private FloatingActionButton add;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.affair_memo_main);
        findView();
        init();//对列表里应该呈现的数据信息进行更新
        myClick();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        for (int i = 0; i < userList.size(); i++) {
            sqLiteDatabase.execSQL("update Info_3 set finish  = ? where _id = ?", new Object[]{userList.get(i).getFinish(), userList.get(i).getId()});
        }
    }

    void init() {
        db = new birthDataBase(getBaseContext());
        sqLiteDatabase = db.getWritableDatabase();
        userList = new ArrayList<>();
        cursor = sqLiteDatabase.rawQuery("select * from Info_3", null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                userList.add(new affair_user(cursor.getInt(cursor.getColumnIndex("_id")), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4)));
            }
        }
        cursor.close();


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new affair_cardAdapter(userList);
        recyclerView.setAdapter(adapter);

        sortDeadlineUp(userList);
    }

    void findView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        add = (FloatingActionButton) findViewById(R.id.add_affair);
        image = (ImageView) findViewById(R.id.img);
    }

    void myClick() {
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (image.getTag().equals(0)) {
                    userList = sortDeadlineUp(userList);
                    adapter.updateData(userList);
                    image.setTag(1);
                } else {
                    userList = sortDeadlineDown(userList);
                    adapter.updateData(userList);
                    image.setTag(0);
                }

            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(affair_MainActivity.this, affair_Additem.class);
                intent.putExtra("pos",-1);
                startActivityForResult(intent, 1);
            }
        });

        adapter.setOnClickListener(new affair_cardAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                //单击进入编辑
                Intent intent = new Intent(affair_MainActivity.this, affair_Additem.class);
                intent.putExtra("pos",position);
                intent.putExtra("time",userList.get(position).getTime());
                startActivityForResult(intent, 2);


//                if (userList.get(position).getFinish().equals("1")) {
//                    userList.get(position).setFinish("0");
//                    sqLiteDatabase = db.getWritableDatabase();
//                    sqLiteDatabase.execSQL("update Info_3 set finish  = ? where _id = ?", new Object[]{"0", userList.get(position).getId()});
//                } else {
//                    userList.get(position).setFinish("1");
//                    sqLiteDatabase = db.getWritableDatabase();
//                    sqLiteDatabase.execSQL("update Info_3 set finish  = ? where _id = ?", new Object[]{"1", userList.get(position).getId()});
//                }
//                adapter.updateData(userList);

            }

            @Override
            public void onLongClick(int position) {//长按删除
                final int pos = position;
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(affair_MainActivity.this);
                alertDialog.setMessage("是否删除？")
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                db = new birthDataBase(getBaseContext());
                                sqLiteDatabase = db.getWritableDatabase();
                                sqLiteDatabase.execSQL("DELETE FROM Info_3 WHERE time  = ?", new String[]{userList.get(pos).getTime()});
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
            cursor = sqLiteDatabase.rawQuery("select * from Info_3", null);
            cursor.moveToLast();
            userList.add(new affair_user(cursor.getInt(cursor.getColumnIndex("_id")), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4)));
            adapter.notifyDataSetChanged();
        } else if (requestCode == 2) {
            String time = intentData.getStringExtra("time");
            if (!time.equals("null")) {
                cursor = sqLiteDatabase.rawQuery("select * from Info_3 where time like ?", new String[]{time});
                if (cursor.moveToFirst()) {
                    int pos = intentData.getIntExtra("pos",0);
                    userList.set(pos,new affair_user(cursor.getInt(cursor.getColumnIndex("_id")), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4)));
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }

    private List<affair_user> sortDeadlineUp(List<affair_user> users) {

        Collections.sort(users, new Comparator<affair_user>() {
            @Override
            public int compare(affair_user o1, affair_user o2) {
                if (o1.getFinish().equals("1") && o2.getFinish().equals("0")) {
                    if (o1.getYear() == o2.getYear() && o1.getMon() == o2.getMon()) {
                        return o1.getDay() - o2.getDay()+100;
                    }
                    if (o1.getYear() == o2.getYear() && o1.getMon() != o2.getMon()) {
                        return o1.getMon() - o2.getMon()+100;
                    }
                    if (o1.getYear() != o2.getYear()) {
                        return o1.getYear() - o2.getYear()+100;
                    }
                } else if (o2.getFinish().equals("1") && o1.getFinish().equals("0")) {
                    if (o1.getYear() == o2.getYear() && o1.getMon() == o2.getMon()) {
                        return o1.getDay() - o2.getDay()-100;
                    }
                    if (o1.getYear() == o2.getYear() && o1.getMon() != o2.getMon()) {
                        return o1.getMon() - o2.getMon()-100;
                    }
                    if (o1.getYear() != o2.getYear()) {
                        return o1.getYear() - o2.getYear()-100;
                    }
                } else {
                    if (o1.getYear() == o2.getYear() && o1.getMon() == o2.getMon()) {
                        return o1.getDay() - o2.getDay();
                    }
                    if (o1.getYear() == o2.getYear() && o1.getMon() != o2.getMon()) {
                        return o1.getMon() - o2.getMon();
                    }
                    if (o1.getYear() != o2.getYear()) {
                        return o1.getYear() - o2.getYear();
                    }
                }
                return 1;
            }
        });
        return users;
    }

    private List<affair_user> sortDeadlineDown(List<affair_user> users) {

        Collections.sort(users, new Comparator<affair_user>() {
            @Override
            public int compare(affair_user o1, affair_user o2) {
                if (o1.getFinish().equals("1") && o2.getFinish().equals("0")) {
                    if (o1.getYear() == o2.getYear() && o1.getMon() == o2.getMon()) {
                        return o2.getDay() - o1.getDay()+100;
                    }
                    if (o1.getYear() == o2.getYear() && o1.getMon() != o2.getMon()) {
                        return o2.getMon() - o1.getMon()+100;
                    }
                    if (o1.getYear() != o2.getYear()) {
                        return o2.getYear() - o1.getYear()+100;
                    }
                } else if (o2.getFinish().equals("1") && o1.getFinish().equals("0")) {
                    if (o1.getYear() == o2.getYear() && o1.getMon() == o2.getMon()) {
                        return o2.getDay() - o1.getDay()-100;
                    }
                    if (o1.getYear() == o2.getYear() && o1.getMon() != o2.getMon()) {
                        return o2.getMon() - o1.getMon()-100;
                    }
                    if (o1.getYear() != o2.getYear()) {
                        return o2.getYear() - o1.getYear()-100;
                    }
                } else {
                    if (o1.getYear() == o2.getYear() && o1.getMon() == o2.getMon()) {
                        return o2.getDay() - o1.getDay();
                    }
                    if (o1.getYear() == o2.getYear() && o1.getMon() != o2.getMon()) {
                        return o2.getMon() - o1.getMon();
                    }
                    if (o1.getYear() != o2.getYear()) {
                        return o2.getYear() - o1.getYear();
                    }
                }
                return 1;
            }
        });
        return users;
    }
}

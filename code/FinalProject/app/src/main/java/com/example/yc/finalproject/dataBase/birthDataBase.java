package com.example.yc.finalproject.dataBase;

/**
 * Created by yc on 2017/12/18.
 */

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

//虽然该数据库叫birthDtaBase 但是它指代的是总的数据库哦！

public class birthDataBase extends SQLiteOpenHelper {
    private static final String DB_NAME = "MyDB.db";
    private static final String TABLE_NAME = "Info";//生日列表
    private static final String TABLE_NAME_2 = "Info_2";//密码列表
    private static final String TABLE_NAME_3 = "Info_3";//待办事项列表
    private static final String TABLE_NAME_4 = "Info_4";//日志列表
    private static final int DB_VERSION = 1;

    public birthDataBase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public birthDataBase(Context context) {
        this(context, DB_NAME, null, DB_VERSION);
    }

    // 创建数据库，直接执行SQl语句即可
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TABLE = "Create Table "
                + TABLE_NAME
                + "(_id integer primary key AUTOINCREMENT, "
                + "name text, "
                + "birth text, "
                + "gift text);";
        sqLiteDatabase.execSQL(CREATE_TABLE);
        //创建第二张表
        String CREATE_TABLE_2 = "Create Table "
                + TABLE_NAME_2
                + "(_id integer primary key , "
                + "name text, "
                + "id text, "
                + "password text);";
        sqLiteDatabase.execSQL(CREATE_TABLE_2);
        //创建第三张表
        String CREATE_TABLE_3 = "Create Table "
                + TABLE_NAME_3
                + "(_id integer primary key AUTOINCREMENT, "
                + "time text, "
                + "affair text, "
                + "deadline text, "
                + "finish text);";
        sqLiteDatabase.execSQL(CREATE_TABLE_3);
        //创建第四张表
        String CREATE_TABLE_4 = "Create Table "
                + TABLE_NAME_4
                + "(_id integer primary key , "
                + "time text, "
                + "topic text, "
                + "content text);";
        sqLiteDatabase.execSQL(CREATE_TABLE_4);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}

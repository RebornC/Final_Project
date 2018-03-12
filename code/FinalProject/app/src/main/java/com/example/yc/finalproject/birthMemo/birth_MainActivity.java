package com.example.yc.finalproject.birthMemo;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.util.Calendar;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yc.finalproject.R;
import com.example.yc.finalproject.dataBase.birthDataBase;
import com.example.yc.finalproject.model.affair_user;
import com.example.yc.finalproject.model.birthday_user;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class birth_MainActivity extends AppCompatActivity {
    private Context mcontext;
    private FloatingActionButton add;
    private ImageView image;
    private ListView listView;
    private List<birthday_user> userList;
    private SimpleAdapter simpleAdapter;
    private List<Map<String, String>> data;
    private birthDataBase db;
    private SQLiteDatabase sqLiteDatabase;
    private Cursor cursor;

    private int Year;
    private int Month;
    private int Day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.birth_memo_main);

        findView();
        init();//对列表里应该呈现的数据信息进行更新
        myClick();
    }

    void findView() {
        add = (FloatingActionButton) findViewById(R.id.add);
        listView = (ListView) findViewById(R.id.listview);
        image = (ImageView) findViewById(R.id.img);
    }

    void init() {
        //init. date
        Calendar d = Calendar.getInstance(Locale.CHINA);
        Date myDate = new Date();
        d.setTime(myDate);
        Year = d.get(Calendar.YEAR);
        Month = d.get(Calendar.MONTH);
        Day = d.get(Calendar.DAY_OF_MONTH);

        mcontext = birth_MainActivity.this;

        db = new birthDataBase(getBaseContext());
        sqLiteDatabase = db.getWritableDatabase();
        cursor = sqLiteDatabase.rawQuery("select * from Info", null);
        data = new ArrayList<>();
        userList = new ArrayList<>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                userList.add(new birthday_user(cursor.getInt(cursor.getColumnIndex("_id")), cursor.getString(1), cursor.getString(2), cursor.getString(3)));
            }
        }
        cursor.close();

        for (int i = 0; i < userList.size(); i++) {
            String name_ = userList.get(i).getName();
            String birth_ = userList.get(i).getBirth();
            String gift_ = userList.get(i).getGift();
            Map<String, String> temp = new HashMap<>();
            temp.put("name", name_);
            temp.put("birth", birth_);
            temp.put("gift", gift_);
            data.add(temp);
        }

        simpleAdapter = new SimpleAdapter(birth_MainActivity.this, data, R.layout.birth_memo_item,
                new String[]{"name", "birth", "gift"}, new int[]{R.id.Name, R.id.Birth, R.id.Gift});
        listView.setAdapter(simpleAdapter);
    }

    void myClick() {
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (image.getTag().equals(0)) {
                    userList = sortDeadlineUp(userList);
                    image.setTag(1);
                } else {
                    userList = sortDeadlineDown(userList);
                    image.setTag(0);
                }

                data.clear();
                for (int i = 0; i < userList.size(); i++) {
                    String name_ = userList.get(i).getName();
                    String birth_ = userList.get(i).getBirth();
                    String gift_ = userList.get(i).getGift();
                    Map<String, String> temp = new HashMap<>();
                    temp.put("name", name_);
                    temp.put("birth", birth_);
                    temp.put("gift", gift_);
                    data.add(temp);
                }

                simpleAdapter.notifyDataSetChanged();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(birth_MainActivity.this, birth_Additem.class);
                startActivityForResult(intent, 1);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                showDialog(birth_MainActivity.this, data.get(arg2));//弹出信息dialog
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    final int arg = arg2;
                    HashMap<String, String> map = (HashMap<String, String>) arg0.getItemAtPosition(arg2);
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(birth_MainActivity.this);
                    alertDialog.setMessage("是否删除？")
                            .setPositiveButton("是", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    db = new birthDataBase(getBaseContext());
                                    sqLiteDatabase = db.getWritableDatabase();
                                    sqLiteDatabase.execSQL("DELETE FROM Info WHERE name  = ?", new String[]{data.get(arg).get("name")});
                                    data.remove(arg);
                                    userList.remove(arg);
                                    simpleAdapter.notifyDataSetChanged();
                                }
                            })
                            .setNegativeButton("否", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Toast.makeText(birth_MainActivity.this, "不删除联系人", Toast.LENGTH_SHORT).show();
                                }
                            }).create();
                    alertDialog.show();
                    return true;
                }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intentData) {
        if (requestCode == 1) {
            if (resultCode == 1) {
                init();
            }
        }
    }

    private List<birthday_user> sortDeadlineUp(List<birthday_user> users) {

        Collections.sort(users, new Comparator<birthday_user>() {
            @Override
            public int compare(birthday_user o1, birthday_user o2) {
                return o1.getBirthInInt().compareTo(o2.getBirthInInt());
            }
        });
        return users;
    }

    private List<birthday_user> sortDeadlineDown(List<birthday_user> users) {

        Collections.sort(users, new Comparator<birthday_user>() {
            @Override
            public int compare(birthday_user o1, birthday_user o2) {
                return o2.getBirthInInt().compareTo(o1.getBirthInInt());
            }
        });
        return users;
    }

    //创建一个自定义的dialog，里面包含信息
    public void showDialog(Context context , Map<String, String> map) {
        View view = LayoutInflater.from(context).inflate(R.layout.memo_dialog_style, null);
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(view);

        final TextView Name = (TextView) view.findViewById(R.id.Name_text);
        final EditText Birth = (EditText) view.findViewById(R.id.Birth_text);
        final EditText Gift = (EditText) view.findViewById(R.id.Gift_text);
        Button abandon = (Button) view.findViewById(R.id.add_abandon);
        Button confirm = (Button) view.findViewById(R.id.add_confirm);

        Name.setText(map.get("name"));
        Birth.setText(map.get("birth"));
        Gift.setText(map.get("gift"));

        abandon.setOnClickListener(new View.OnClickListener() {//点击“放弃修改”button
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        Birth.setOnClickListener(new View.OnClickListener() {
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

                        Birth.setText(y + "-" + m + "-" + d);
                    }
                }, Year, Month, Day);
                datePickerDialog.show();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {//点击“保存修改”button
            public void onClick(View view) {
                String old_name = Name.getText().toString();
                String new_birth = Birth.getText().toString();
                String new_gift = Gift.getText().toString();
                db = new birthDataBase(getBaseContext());
                sqLiteDatabase = db.getWritableDatabase();
                sqLiteDatabase.execSQL("update Info set birth = ? where name = ?", new Object[]{new_birth, old_name});
                sqLiteDatabase.execSQL("update Info set gift = ? where name = ?", new Object[]{new_gift, old_name});
                sqLiteDatabase.close();
                init();
                dialog.dismiss();
            }
        });

        dialog.show();
        //设置dialog的大小
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = 920;
        lp.height = 820;
        dialog.getWindow().setAttributes(lp);
    }
}

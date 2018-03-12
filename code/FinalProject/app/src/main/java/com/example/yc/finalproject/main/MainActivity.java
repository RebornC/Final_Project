package com.example.yc.finalproject.main;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yc.finalproject.model.Memo;
import com.example.yc.finalproject.R;
import com.example.yc.finalproject.adapter.MemoAdapter;
import com.example.yc.finalproject.toolbar.develop;
import com.example.yc.finalproject.toolbar.user_guide;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.OvershootInLeftAnimator;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    /* 请求识别码 */
    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int CODE_RESULT_REQUEST = 0xa2;
    // 裁剪后图片的宽(X)和高(Y),200 X 200的正方形。
    private static int output_X = 200;
    private static int output_Y = 200;

    private List<Memo> memoList = new ArrayList<>();
    private RecyclerView recyclerView;
    private int selectedIndex;
    private NavigationView navigationView;
    private View headerLayout;
    private ImageView myHead;//头像
    private TextView myName;//昵称
    private TextView myMood;//签名
    private SharedPreferences sp;//利用sharepreferences来保存头像/昵称/签名信息
    private String photoUri;//若自定义头像了头像，则记录自定义的URI


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        selectedIndex = 0;
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        headerLayout = navigationView.inflateHeaderView(R.layout.nav_header_main);
        myHead = (ImageView) headerLayout.findViewById(R.id.imageView);//头像
        myName = (TextView) headerLayout.findViewById(R.id.name);//昵称
        myMood = (TextView) headerLayout.findViewById(R.id.mood);//签名
        sp = this.getSharedPreferences("MY_PREFERENCE",this.MODE_PRIVATE);//存储数据

        //判断是否已存入新的昵称和签名
        String new_name = sp.getString("new_name","");
        String new_mood = sp.getString("new_mood","");
        photoUri = sp.getString("uri","");
        if (!new_name.equals("")) {
            myName.setText(new_name);
            myMood.setText(new_mood);
        }
        if (!photoUri.equals("")) {
            Uri uri =  Uri.parse(photoUri);
            Log.i("uri:",photoUri);

                myHead.setImageBitmap(BitmapFactory.decodeFile(uri.toString()));
                //myHead.setImageBitmap(MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri));

        }

        initMemos();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        MemoAdapter adapter = new MemoAdapter(memoList);
        ScaleInAnimationAdapter animationAdapter = new ScaleInAnimationAdapter(adapter);
        animationAdapter.setDuration(1000);
        recyclerView.setAdapter(animationAdapter);
        recyclerView.setItemAnimator(new OvershootInLeftAnimator());

        myHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//点击头像可更改头像
                choseHeadImageFromGallery();
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


    void initMemos() {
        Memo psw_memo = new Memo("> > 密码管家", "信息时代 记忆爆炸"+"\n"+"在此写下你的各项账号与对应的密码吧", R.drawable.psw);
        memoList.add(psw_memo);
        Memo dairy_memo = new Memo("> > 日志", "乱七八糟的思绪"+"\n"+"还有经历的每个瞬间"+"\n"+"尝试用文字承载记忆吧", R.drawable.dairy);
        memoList.add(dairy_memo);
        Memo birth_memo = new Memo("> > 生日备忘录", "不要再忽视身边人的生日啦"+"\n"+"悄咪咪地准备惊喜吧", R.drawable.birth);
        memoList.add(birth_memo);
        Memo affair_memo = new Memo("> > 待办事项", "Let your life be filled with B 数"+"\n"+"不再落后或错过 让生活更有规划", R.drawable.affair);
        memoList.add(affair_memo);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            System.exit(0);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.change_image) {
            choseHeadImageFromGallery();
        } else if (id == R.id.change_name) {
            EditText Name = (EditText) findViewById(R.id.name_text);
            showDialog(MainActivity.this);
        } else if (id == R.id.function) {
            Intent it = new Intent(this, user_guide.class);
            startActivity(it);
        } else if (id == R.id.developer) {
            Intent it = new Intent(this, develop.class);
            startActivity(it);
        } else if (id == R.id.bg1) {
            final String[] arrayFruit = new String[] { "小团圆", "糖风日", "姜撞猫", "百雪集", "星浪原" };
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("请选择你喜欢的界面风格")
                    .setIcon(R.drawable.ic_cloud_queue_black_24dp)
                    //this ;
                    .setSingleChoiceItems(arrayFruit, selectedIndex,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    // TODO Auto-generated method stub
                                    selectedIndex = which;
                                }
                            })
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            chooseStyle(selectedIndex);
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub

                        }
                    }).create();
            dialog.show();
        } else if (id == R.id.bg2) {
            selectedIndex = 0;
            recyclerView.setBackgroundColor(getResources().getColor(R.color.white));
            LinearLayout head_bg = (LinearLayout) findViewById(R.id.head_bg);;
            TextView _name = (TextView) findViewById(R.id.name);
            TextView _mood = (TextView) findViewById(R.id.mood);
            head_bg.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            _name.setTextColor(getResources().getColor(R.color.white));
            _mood.setTextColor(getResources().getColor(R.color.white_tra));
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    void chooseStyle(int selected) {
        if (selected == 0) {
            selectedIndex = 0;
            recyclerView.setBackgroundResource(R.mipmap.bg6);
            LinearLayout head_bg = (LinearLayout) findViewById(R.id.head_bg);;
            TextView _name = (TextView) findViewById(R.id.name);
            TextView _mood = (TextView) findViewById(R.id.mood);
            head_bg.setBackgroundColor(getResources().getColor(R.color.color_head_bg1));
            _name.setTextColor(getResources().getColor(R.color.white));
            _mood.setTextColor(getResources().getColor(R.color.white_tra));
        } else if (selected == 1) {
            selectedIndex = 1;
            recyclerView.setBackgroundResource(R.mipmap.bg8);
            LinearLayout head_bg = (LinearLayout) findViewById(R.id.head_bg);;
            TextView _name = (TextView) findViewById(R.id.name);
            TextView _mood = (TextView) findViewById(R.id.mood);
            head_bg.setBackgroundColor(getResources().getColor(R.color.color_head_bg2));
            _name.setTextColor(getResources().getColor(R.color.black));
            _mood.setTextColor(getResources().getColor(R.color.black_tra));
        } else if (selected == 2) {
            selectedIndex = 2;
            recyclerView.setBackgroundResource(R.mipmap.bg9);
            LinearLayout head_bg = (LinearLayout) findViewById(R.id.head_bg);;
            TextView _name = (TextView) findViewById(R.id.name);
            TextView _mood = (TextView) findViewById(R.id.mood);
            head_bg.setBackgroundColor(getResources().getColor(R.color.color_head_bg3));
            _name.setTextColor(getResources().getColor(R.color.black));
            _mood.setTextColor(getResources().getColor(R.color.black_tra));
        } else if (selected == 3) {
            selectedIndex = 3;
            recyclerView.setBackgroundResource(R.mipmap.bg7);
            LinearLayout head_bg = (LinearLayout) findViewById(R.id.head_bg);;
            TextView _name = (TextView) findViewById(R.id.name);
            TextView _mood = (TextView) findViewById(R.id.mood);
            head_bg.setBackgroundColor(getResources().getColor(R.color.color_head_bg4));
            _name.setTextColor(getResources().getColor(R.color.white));
            _mood.setTextColor(getResources().getColor(R.color.white_tra));
        } else if (selected == 4) {
            selectedIndex = 4;
            recyclerView.setBackgroundResource(R.mipmap.bg10);
            LinearLayout head_bg = (LinearLayout) findViewById(R.id.head_bg);;
            TextView _name = (TextView) findViewById(R.id.name);
            TextView _mood = (TextView) findViewById(R.id.mood);
            head_bg.setBackgroundColor(getResources().getColor(R.color.color_head_bg5));
            _name.setTextColor(getResources().getColor(R.color.white));
            _mood.setTextColor(getResources().getColor(R.color.white_tra));
        }
    }

    private void choseHeadImageFromGallery() {
        Intent intentFromGallery = new Intent();
        // 设置文件类型
        intentFromGallery.setType("image/*");
        intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intentFromGallery, CODE_GALLERY_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent intent) {
        // 用户没有进行有效的设置操作，返回
        if (resultCode == RESULT_CANCELED) {
            Toast.makeText(getApplication(), "取消", Toast.LENGTH_LONG).show();
            return;
        }
        switch (requestCode) {
            case CODE_GALLERY_REQUEST:
                cropRawPhoto(intent.getData());
                break;
            case CODE_RESULT_REQUEST:
                if (intent != null) {
                    setImageToHeadView(intent);
                }
                break;
            default: break;
        }

    }

    /**
     * 裁剪原始的图片
     */
    public void cropRawPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX , aspectY :宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX , outputY : 裁剪图片宽高
        intent.putExtra("outputX", output_X);
        intent.putExtra("outputY", output_Y);
        intent.putExtra("return-data", true);

        startActivityForResult(intent, CODE_RESULT_REQUEST);
    }

    /**
     * 提取保存裁剪之后的图片数据，并设置头像部分的View
     */
    private void setImageToHeadView(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            //提取图片数据，将头像的View设置为自定义图片
            Bitmap photo = extras.getParcelable("data");
            myHead.setImageBitmap(photo);

            //创建文件夹，储存截好的头像，方便下次打开的时候读取
            File newfile = new File(Environment.getExternalStorageDirectory(),"Pic");
            if (!newfile.exists()) {
                newfile.mkdir();
            }
            File file = new File(Environment.getExternalStorageDirectory()+"/Pic", "head.jpg");
            FileOutputStream out = null;
            try {//打开输出流 将图片数据填入文件中
                out = new FileOutputStream(file);
                photo.compress(Bitmap.CompressFormat.JPEG, 90, out);
                try {
                    out.flush();
                    photoUri = Environment.getExternalStorageDirectory()+"/Pic/head.jpg";
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            //储存自定义头像的Uri到本地
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("uri", photoUri);
            editor.commit();
        }
    }

    /**
     * 检查设备是否存在SDCard的工具方法
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            // 有存储的SDCard
            return true;
        } else {
            return false;
        }
    }

    //创建一个自定义的dialog，修改昵称和签名
    public void showDialog(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.chang_name, null);
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(view);
        final EditText Name = (EditText) view.findViewById(R.id.name_text);
        final EditText Mood = (EditText) view.findViewById(R.id.mood_text);
        Button abandon = (Button) view.findViewById(R.id.abandon);
        Button confirm = (Button) view.findViewById(R.id.confirm);

        Name.setText(myName.getText().toString());
        Mood.setText(myMood.getText().toString());

        abandon.setOnClickListener(new View.OnClickListener() {//点击“放弃修改”button
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {//点击“保存修改”button
            public void onClick(View view) {
                String str1 = Name.getText().toString();
                String str2 = Mood.getText().toString();
                if (str1.equals("") || str2.equals("")) {
                    Toast.makeText(getApplicationContext(), "不能留空哦", Toast.LENGTH_SHORT).show();
                } else {
                    myName.setText(str1);
                    myMood.setText(str2);
                    //储存
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("new_name", str1);
                    editor.putString("new_mood", str2);
                    editor.commit();
                    dialog.dismiss();
                }
            }
        });

        dialog.show();
        //设置dialog的大小
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = 1000;
        lp.height = 800;
        dialog.getWindow().setAttributes(lp);
    }
}

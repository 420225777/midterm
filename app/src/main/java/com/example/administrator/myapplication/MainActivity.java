package com.example.administrator.myapplication;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import	android.support.v4.content.ContextCompat;

public class MainActivity extends Activity {

    private ImageView img;
    private ListView listview;
    private BookDbUtils dbUtils;
    private ArrayList<BookBean> list;
    private ContentAdapter adapter;
    private EditText serach;
    private TextView daochu;
    public static final int REQUEST_CODE = 10;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

            final String[] PERMISSIONS_STORAGE = {
                    "android.permission.READ_EXTERNAL_STORAGE",
                    "android.permission.WRITE_EXTERNAL_STORAGE" };







        dbUtils = new BookDbUtils(MainActivity.this);
        img = findViewById(R.id.img);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this,EditActivity.class));
                finish();
            }
        });
        listview = findViewById(R.id.listview);
        list = dbUtils.query();
        if (list == null || list.size() <= 0){

        }else {
            adapter = new ContentAdapter(MainActivity.this,list);
            listview.setAdapter(adapter);
            adapter.notifyDataSetChanged();


        }


        serach = findViewById(R.id.serach);
        serach.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String seaStr = s.toString();
                ArrayList<BookBean> mList = dbUtils.queryType(seaStr);
                if (mList == null || mList.size() <= 0){
                    adapter = new ContentAdapter(MainActivity.this,list);
                    listview.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }else {
                    adapter = new ContentAdapter(MainActivity.this,mList);
                    listview.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        daochu = findViewById(R.id.daochu);
        daochu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        list = dbUtils.query();
                Log.e("长度",list.size()+"");
                        if (list == null || list.size() <= 0){
                            Toast.makeText(MainActivity.this, "您没有存入任何数据", Toast.LENGTH_SHORT).show();
                            return;
                        }else {
                            //检查权限
                            //检查版本是否大于M
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                    //进入到这里代表没有权限.

                                    if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                                        //已经禁止提示了
                                        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                                        builder.setCancelable(false)
                                                .setMessage("应用需要存储权限来让您导出文件")
                                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        Toast.makeText(MainActivity.this,"点击了取消按钮",Toast.LENGTH_LONG).show();
                                                    }
                                                })
                                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        ActivityCompat.requestPermissions(MainActivity.this, PERMISSIONS_STORAGE, REQUEST_CODE);
                                                    }
                                                }).show();

                                    }else{
                                        ActivityCompat.requestPermissions(MainActivity.this, PERMISSIONS_STORAGE, REQUEST_CODE);

                                    }

                                } else {
//                                    for (int i = 0;i<list.size();i++){
                                        try {
                                            savaFileToSD("notepad.txt",list.toString());
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
//                                    }
                                }
                            }


                        }

            }
        });

    }


    @Override
    public void onRequestPermissionsResult(int requestCode,  String[] permissions, int[] grantResults) {
        switch (requestCode){
            case REQUEST_CODE:
                if(grantResults.length >0 &&grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    //用户同意授权
                    list = dbUtils.query();
                    if (list == null || list.size() <= 0){
                        Toast.makeText(MainActivity.this, "您没有存入任何数据", Toast.LENGTH_SHORT).show();
                        return;
                    }else {
//                        for (int i = 0;i<list.size();i++){
                            try {
                                savaFileToSD("notepad.txt",list.toString());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
//                        }
                    }
                }else{
                    //用户拒绝授权
                    Toast.makeText(MainActivity.this,"您拒绝了授予权限，请授权",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
//    private void callAlbum(){
////        savaFileToSD();
//    }

   // 往SD卡写入文件的方法
    public synchronized void savaFileToSD(String filename, String filecontent) throws Exception {

        String sdCardDir =Environment.getExternalStorageDirectory().getAbsolutePath();
        File saveFile = new File(sdCardDir, filename);
        FileOutputStream outStream = new FileOutputStream(saveFile);
        outStream.write(filecontent.getBytes());
        outStream.close();
        Toast.makeText(MainActivity.this, "导出成功，路径:"+sdCardDir+File.separator+"notepad.txt", Toast.LENGTH_SHORT).show();

        //如果手机已插入sd卡,且app具有读写sd卡的权限
//        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//            filename = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + filename;
//
//            //这里就不要用openFileOutput了,那个是往手机内存中写数据的
//            FileOutputStream output = new FileOutputStream(filename);
//            output.write(filecontent.getBytes());
//            //将String字符串以字节流的形式写入到输出流中
//            output.close();
//            Toast.makeText(MainActivity.this, "路径:"+filename, Toast.LENGTH_SHORT).show();
//            //关闭输出流
//        } else Toast.makeText(MainActivity.this, "SD卡不存在或者不可读写", Toast.LENGTH_SHORT).show();
    }
}

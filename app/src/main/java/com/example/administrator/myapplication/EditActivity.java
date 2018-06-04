package com.example.administrator.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.text.SimpleDateFormat;

/**
 * Created by Administrator on 2018/6/3.
 */

public class EditActivity extends Activity {

    private ImageView save;
    private EditText edit;
    private BookDbUtils dbUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.edit);
        dbUtils = new BookDbUtils(EditActivity.this);
        save = findViewById(R.id.save);
        edit = findViewById(R.id.edit);
        save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
               String editStr =  edit.getText().toString().trim();
                if (editStr.equals("")){
                    Toast.makeText(EditActivity.this,"不能为空",Toast.LENGTH_SHORT).show();
                    return;

                }else {
                    //存数据库
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                    String date = df.format(System.currentTimeMillis());
                    BookBean mBookBean = new BookBean();
                    mBookBean.bookname = editStr;
                    mBookBean.author = date;
                    dbUtils.add(mBookBean);
                    startActivity(new Intent(EditActivity.this,MainActivity.class));
                    finish();

                }
            }
        });
    }
}

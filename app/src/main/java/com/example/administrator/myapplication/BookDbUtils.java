package com.example.administrator.myapplication;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;



public class BookDbUtils {

    private SQLiteDatabase db;
    private BookOpenHelper mySqliteOpenHelper;

    public BookDbUtils(Context context) {
        mySqliteOpenHelper = new BookOpenHelper(context);
    }

    public void add(BookBean bean) {
        db = mySqliteOpenHelper.getReadableDatabase();
        db.execSQL("insert into book (bookname,author) values(?,?)", new Object[]{bean.bookname, bean.author});
        db.close();
    }

    public ArrayList<BookBean> query() {
        db = mySqliteOpenHelper.getReadableDatabase();
        ArrayList<BookBean> list = new ArrayList<BookBean>();
        Cursor cursor = db.rawQuery("select * from book;", null);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                BookBean bean = new BookBean();
                bean.bookname = cursor.getString(cursor.getColumnIndex("bookname"));
                bean.author = cursor.getString(cursor.getColumnIndex("author"));
                list.add(bean);
            }
            cursor.close();
        }
        db.close();
        return list;
    }


    public ArrayList<BookBean> queryType(String type) {
        db = mySqliteOpenHelper.getReadableDatabase();
        ArrayList<BookBean> list = new ArrayList<BookBean>();
        Cursor cursor = db.rawQuery("select * from book where bookname=?;", new String[]{type});
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                BookBean bean = new BookBean();
                bean.bookname = cursor.getString(cursor.getColumnIndex("bookname"));
                bean.author = cursor.getString(cursor.getColumnIndex("author"));
                list.add(bean);
            }
            cursor.close();
        }
        db.close();
        return list;
    }
}

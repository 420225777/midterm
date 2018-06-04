package com.example.administrator.myapplication;

/**
 * Created by zhuhai on 2017/12/26.
 */

public class BookBean {
    public String bookname;
    public String author;

    @Override
    public String toString() {
        return "每项内容{" +
                "内容='" + bookname + '\'' +
                ", 时间='" + author + '\'' +
                '}'+"\n";
    }
}

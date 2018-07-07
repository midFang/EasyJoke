package com.example.framelibrary.db;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by fangsf on 2018/7/7.
 * Useful:
 */
public interface IDaoSupport<T> {

     void init(SQLiteDatabase sqLiteDatabase, Class<T> tClass);

     void insert(T tClass);

}

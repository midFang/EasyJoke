package com.example.framelibrary.db;

import android.database.sqlite.SQLiteDatabase;

import java.util.List;

/**
 * Created by fangsf on 2018/7/7.
 * Useful:
 */
public interface IDaoSupport<T> {

     // 出入化数据库表
     void init(SQLiteDatabase sqLiteDatabase, Class<T> tClass);

     // 插入单条数据
     long insert(T clazz);

     // 插入多条数据
     void insert(List<T> clazz);

     // 查询所有
      List<T> query();

     int delete(String whereClause, String[] whereArgs);

     int update(T obj, String whereClause, String... whereArgs);

}

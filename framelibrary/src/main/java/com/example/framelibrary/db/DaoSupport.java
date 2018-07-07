package com.example.framelibrary.db;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.lang.reflect.Field;

/**
 * Created by fangsf on 2018/7/7.
 * Useful:
 */
public class DaoSupport<T> implements IDaoSupport<T> {
    private static final String TAG = "DaoSupport";

    private SQLiteDatabase mSQLiteDatabase;
    private Class<T> mClazz;

    @Override
    public void init(SQLiteDatabase sqLiteDatabase, Class<T> clazz) {
        this.mSQLiteDatabase = sqLiteDatabase;
        this.mClazz = clazz;

        /** 根据sql 语句动态创建表
         *
         create table if not exists Person ("
         + "id integer primary key autoincrement, "
         + "name text, "
         + "age integer, "
         + "flag boolean)";

         */

        // 需要创建数据库表
        StringBuffer sb = new StringBuffer();
        sb.append("create table if not exists ")
                .append(DaoUtils.getTableName(mClazz))
                .append("(id integer primary key autoincrement, ");

        Field[] fields = mClazz.getDeclaredFields();//$changenull 反射时候会获取这个, 需要关闭install run
        for (Field field : fields) {
            field.setAccessible(true);
            // 获取字段属性,动态创建表 结构关系
            String name = field.getName();
            String type = field.getType().getSimpleName();
            sb.append(name).append(DaoUtils.getColumnType(type))
                    .append(", ");
        }

        sb.replace(sb.length() - 2, sb.length(), ")");
        String createSQL = sb.toString();
        Log.i(TAG, " createSQL: " + createSQL);
        mSQLiteDatabase.execSQL(createSQL);
    }

    // T 可以是任意对象
    @Override
    public void insert(T tClass) {

    }
}

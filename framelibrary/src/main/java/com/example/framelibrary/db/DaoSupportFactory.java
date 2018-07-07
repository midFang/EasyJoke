package com.example.framelibrary.db;

import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import java.io.File;

/**
 * Created by fangsf on 2018/7/7.
 * Useful:
 */
public class DaoSupportFactory {

    // 创建操作数据库的类
    private SQLiteDatabase mSQLiteDatabase;

    private DaoSupportFactory(){

        // 创建数据库文件
        File dbRoot = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator
                + "nhdz" + File.separator + "dateBase");
        if (!dbRoot.exists()) {
            dbRoot.mkdirs();
        }
        File dbFile = new File(dbRoot, "nhdz.db");

        // 操作数据库
        mSQLiteDatabase = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
    }

    private static DaoSupportFactory mFactory;

    public static DaoSupportFactory getFactory(){
        if (mFactory == null) {
            synchronized (DaoSupportFactory.class){
                if (mFactory ==  null) {
                    mFactory = new DaoSupportFactory();
                }
            }
        }
        return mFactory;
    }

    /**
     * 操作数据库
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> IDaoSupport<T> getDao(Class<T> clazz) {

        IDaoSupport<T> daoSupport = new DaoSupport();

        daoSupport.init(mSQLiteDatabase, clazz);

        return daoSupport;
    }
}

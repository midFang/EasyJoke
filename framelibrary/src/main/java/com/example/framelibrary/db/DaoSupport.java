package com.example.framelibrary.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.view.menu.MenuView;
import android.util.ArrayMap;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.xml.transform.sax.TransformerHandler;

/**
 * Created by fangsf on 2018/7/7.
 * Useful:
 */
public class DaoSupport<T> implements IDaoSupport<T> {
    private static final String TAG = "DaoSupport";

    private SQLiteDatabase mSQLiteDatabase;
    private Class<T> mClazz;

    // 缓存反射方法 以及参数
    private static Object[] mPutMethodArgs = new Object[2];
    @SuppressLint("NewApi")
    public static Map<String, Method> mPutMethods = new ArrayMap<>();


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
    public long insert(T clazz) {
        ContentValues values = contentValueByObj(clazz);

        return mSQLiteDatabase.insert(DaoUtils.getTableName(mClazz), null, values);
    }

    @Override
    public void insert(List<T> clazz) {
        // 性能优化的方法, 开始事物的方式
        mSQLiteDatabase.beginTransaction();
        for (T t : clazz) {
            insert(t);
        }
        mSQLiteDatabase.setTransactionSuccessful();
        mSQLiteDatabase.endTransaction();
    }

    @Override
    public List<T> query() {
        Cursor cursor = mSQLiteDatabase.query(DaoUtils.getTableName(mClazz),
                null, null, null, null, null, null);

        return cursorToList(cursor);
    }

    private List<T> cursorToList(Cursor cursor) {
        List<T> list = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                try {
                    T instance = mClazz.newInstance(); // 是一个无参数的构造方法
                    mClazz.newInstance();
                    Field[] fields = mClazz.getDeclaredFields();
                    for (Field field : fields) {
                        field.setAccessible(true);
                        String name = field.getName();
                        // 获取角标
                        int index = cursor.getColumnIndex(name);
                        if (index == -1) {
                            continue;
                        }

                        Method cursorMethod = cursorMethod(field.getType());
                        if (cursorMethod != null) {
                            Object value = cursorMethod.invoke(cursor, index);
                            if (value == null) {
                                continue;
                            }

                            // 处理特殊的部分
                            if (field.getType() == boolean.class || field.getType() == Boolean.class) {
                                if ("0".equals(String.valueOf(value))) {
                                    value = false;
                                } else if ("1".equals(String.valueOf(value))) {
                                    value = true;
                                }
                            } else if (field.getType() == char.class || field.getType() == Character.class) {
                                value = ((String) value).charAt(0);
                            } else if (field.getType() == Date.class) {
                                long date = (Long) value;
                                if (date <= 0) {
                                    value = null;
                                } else {
                                    value = new Date(date);
                                }
                            }
                            // 反射注入
                            field.set(instance, value);
                        }
                    }

                    list.add(instance);

                } catch (Exception e) { // 查询数据库的事件,实体类需要提供无参数的构造方法
                    e.printStackTrace();
                    Log.i(TAG, " cursorToList: " + e.getMessage());
                }
            } while (cursor.moveToNext());
        }

        cursor.close();
        return list;
    }

    private Method cursorMethod(Class<?> type) throws Exception {
        String methodName = getColumnMethodName(type);
        Method method = Cursor.class.getMethod(methodName, int.class);
        return method;
    }

    private String getColumnMethodName(Class<?> fieldType) {
        String typeName;
        if (fieldType.isPrimitive()) {
            typeName = DaoUtils.getCapitalize(fieldType.getName());
        } else {
            typeName = fieldType.getSimpleName();
        }
        String methodName = "get" + typeName;
        if ("getBoolean".equals(methodName)) {
            methodName = "getInt";
        } else if ("getChar".equals(methodName) || "getCharacter".equals(methodName)) {
            methodName = "getString";
        } else if ("getDate".equals(methodName)) {
            methodName = "getLong";
        } else if ("getInteger".equals(methodName)) {
            methodName = "getInt";
        }
        return methodName;
    }

    // 根据类型动态插入数据
    private ContentValues contentValueByObj(T clazz) {
        ContentValues values = new ContentValues();

        Field[] fields = clazz.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                // 插入数据 value(age, 22)
                String key = field.getName();
                // 获取对象的 value 值
                Object value = field.get(clazz);

                // 参考view 的创建的 方式,做的缓存, 性能优化
                mPutMethodArgs[0] = key;
                mPutMethodArgs[1] = value;

                String fileTypeName = field.getType().getName(); //会有 int, java.lang.string, 作为key会被缓存起来
                //String fileTypeName = mClazz.getSimpleName(); // todo 所有的类型都只缓存一次 ?(这种方法是错误的)
                Method putMethod = mPutMethods.get(fileTypeName); // 将反射方法缓存起来
                if (putMethod == null) {
                    // values.put(key, value);  //value 必须要具体的类型的, 所以只有通过反射注入 put方法中

                    // 反射 获取 put(key, value); 方法
                    putMethod = values.getClass().getMethod("put", String.class, value.getClass());
                    mPutMethods.put(fileTypeName, putMethod);
                }
                putMethod.invoke(values, mPutMethodArgs);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "contentValueByObj: 插入失败 " + e.getMessage());
            } finally {
                mPutMethodArgs[0] = null;
                mPutMethodArgs[1] = null;
            }
        }

        return values;
    }

    public int delete(String whereClause, String[] whereArgs) {
        return mSQLiteDatabase.delete(DaoUtils.getTableName(mClazz), whereClause, whereArgs);
    }

    public int update(T obj, String whereClause, String... whereArgs) {
        ContentValues values = contentValueByObj(obj);
        return mSQLiteDatabase.update(DaoUtils.getTableName(mClazz), values,
                whereClause, whereArgs);
    }

}

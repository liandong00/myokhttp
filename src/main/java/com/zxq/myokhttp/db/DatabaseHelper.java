package com.zxq.myokhttp.db;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.orhanobut.logger.Logger;
import com.zxq.myokhttp.entity.CacheBean;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * 数据库管理工具
 *
 * @author 朱侠强
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DB_NAME = "myokhttp.db";

    private Map<String, Dao> daos = new HashMap<String, Dao>();

    private DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
        Logger.v(TAG, "DatabaseHelper(context)");
    }

    @Override
    public void onCreate(SQLiteDatabase database,
                         ConnectionSource connectionSource) {
        Logger.d(TAG, "onCreate()");
        try {
            TableUtils.createTable(connectionSource, CacheBean.class);
        } catch (Exception e) {
          Logger.e(e, e.getMessage());
        }

        Logger.d(TAG, "run complete");

    }

    @Override
    public void onUpgrade(SQLiteDatabase database,
                          ConnectionSource connectionSource, int oldVersion, int newVersion) {
        Logger.d("onUpgrade");
        Logger.d( "onUpgrade() run complete");

    }

    private static DatabaseHelper instance;

    /**
     * 获取单例
     *
     * @param context
     * @return
     */
    public static synchronized DatabaseHelper getHelper(Context context) {
        Logger.d("getHelper(Context context)");
        if (instance == null) {
            synchronized (DatabaseHelper.class) {
                if (instance == null)
                    instance = new DatabaseHelper(context);
            }
        }

        return instance;
    }

    /**
     * 获得Dao
     */
    public synchronized Dao getDao(Class clazz) throws SQLException {
        Logger.d( "getDao(Class clazz)");
        Dao dao = null;
        String className = clazz.getSimpleName();

        if (daos.containsKey(className)) {
            dao = daos.get(className);
        }
        if (dao == null) {
            dao = super.getDao(clazz);
            daos.put(className, dao);
        }
        return dao;
    }

    /**
     * 释放资源
     */
    @Override
    public void close() {
        super.close();

        for (String key : daos.keySet()) {
            Dao dao = daos.get(key);
            dao = null;
        }
    }

}

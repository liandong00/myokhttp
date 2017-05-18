package com.zxq.myokhttp.db;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.orhanobut.logger.Logger;
import com.zxq.myokhttp.entity.CacheBean;

import java.sql.SQLException;


/**
 * 已记忆单词库
 */
public class CacheDao {

    private static CacheDao instance;
    private Context context;
    private Dao<CacheBean, Integer> cacheDao;
    private DatabaseHelper helper;

    private CacheDao(Context context) {
        super();
        try {
            helper = DatabaseHelper.getHelper(context);
            cacheDao = helper.getDao(CacheBean.class);
            this.context = context;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static CacheDao getInstance(Context context) {
        if (instance == null) {
            synchronized (CacheDao.class) {
                if (instance == null) {
                    instance = new CacheDao(context);
                }
            }
        }
        return instance;
    }

    /**
     * 添加用户选择的词库数据
     * @param entity
     */
    public void add(CacheBean entity) {
        try {
            CacheBean cacheBean= findByData(entity);
            if (cacheBean == null) {
                cacheDao.create(entity);
            } else {
                cacheBean.setTimeStamp(entity.getTimeStamp());
                cacheBean.setData(entity.getData());
                cacheDao.update(cacheBean);
            }
        } catch (Exception ex) {
            Logger.e(ex, ex.getMessage());
        }
    }

    /**
     * 添加用户选择的词库数据
     * @param entity
     */
    public void del(CacheBean entity) {
        try {
//            CacheBean cacheBean= findByData(entity);

            DeleteBuilder<CacheBean, Integer> deleteBuilder = cacheDao.deleteBuilder();
            deleteBuilder .where()
                    .eq("userPhone", entity.getData())
                    .and()
                    .eq("hashCode", entity.getHashCode());
            deleteBuilder.delete();



        } catch (Exception ex) {
            Logger.e(ex, ex.getMessage());
        }

    }

    /**
     * 临时增加用户名字参数
     * @param hashCode
     * @param userName
     * @return
     */
    public CacheBean findByData(int hashCode,String userName){
        try {
            return cacheDao.queryBuilder()
                    .where()
                    .eq("userPhone", userName)
                    .and()
                    .eq("hashCode", hashCode)
                    .queryForFirst();
        } catch (SQLException ex) {
            Logger.e(ex, ex.getMessage());
        }
        return null;
    }
    public CacheBean findByData(CacheBean entity){
        try {
            return cacheDao.queryBuilder()
                    .where()
                    .eq("userPhone", entity.getUserPhone())
                    .and()
                    .eq("hashCode", entity.getHashCode())
                    .queryForFirst();
        } catch (SQLException ex) {
            Logger.e(ex, ex.getMessage());
        }
        return null;
    }


}

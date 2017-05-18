package com.zxq.myokhttp.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zxq.myokhttp.util.Global;

/**
 * 缓存类
 */
@DatabaseTable(tableName = "cache")
public class CacheBean {
    //主键
    @DatabaseField(generatedId = true)
    private int id;
    //用户ID
    @DatabaseField(columnName = "userPhone")
    private String userPhone;

    //特征码
    @DatabaseField(columnName = "hashCode")
    private int hashCode;
    //数据
    @DatabaseField(columnName = "data")
    private String data;
    //时间戳
    @DatabaseField(columnName = "timeStamp")
    private long timeStamp;


    /**
     * @TODO
     * 临时添加
     * @param userPhone
     * @param hashCode
     * @param data
     */
    public CacheBean(String userPhone,int hashCode,String data) {
        this.userPhone = userPhone;
        this.hashCode = hashCode;
        this.data = data;
        this.timeStamp = Global.getTIMESTAMP();
    }

    public CacheBean() {
    }

    public String getUserPhone() {
        return userPhone;
    }

    public int getHashCode() {
        return hashCode;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public static int getHashCode(String... parames) {
        StringBuilder sb = new StringBuilder();
        for (String parame :
                parames) {
            sb.append(parame);
        }
        return sb.toString().hashCode();
//        return hashCode;
    }
}

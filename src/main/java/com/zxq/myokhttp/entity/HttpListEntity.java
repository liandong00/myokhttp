package com.zxq.myokhttp.entity;

import java.util.List;

/**
 * 集合实体
 * @param <T>
 */
public class HttpListEntity<T> extends CodeMsgBean{
    private List<T> data;

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}

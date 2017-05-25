package com.zxq.myokhttp.entity;

/**
 * 统一解析Object
 * @param <T>
 */
public class HttpEntity<T> extends CodeMsgBean {

    private T data;

    public T getData() {
        return (T) data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
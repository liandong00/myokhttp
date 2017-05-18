package com.zxq.myokhttp.entity;

/**
 * Page Size Date 请求体
 */
public class RequestPageSizeDateEntity {
    private String date;
    private int page;
    private int size;

    public RequestPageSizeDateEntity() {
    }


    public RequestPageSizeDateEntity(String date, int page, int size) {
        this.date = date;
        this.page = page;
        this.size = size;
    }

}

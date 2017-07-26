package com.zxq.myokhttp.entity;

/**
 * CodeMsg
 */
public class CodeMsgBean {
    private int code;
//    @SerializedName(value = "msg", alternate = {"msg", "message"})
    private String msg;
    private int max;
    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }

}

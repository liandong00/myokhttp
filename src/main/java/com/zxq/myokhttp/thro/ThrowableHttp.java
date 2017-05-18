package com.zxq.myokhttp.thro;


import com.zxq.myokhttp.entity.CodeMsgBean;

/**
 * 统一异常
 */
public class ThrowableHttp extends Throwable {
    private CodeMsgBean codeMsgBean;

    public CodeMsgBean getCodeMsgBean() {
        return codeMsgBean;
    }

    public void setCodeMsgBean(CodeMsgBean codeMsgBean) {
        this.codeMsgBean = codeMsgBean;
    }

    public ThrowableHttp(CodeMsgBean s) {
        super(s.getMsg());
        this.codeMsgBean = s;
    }
}
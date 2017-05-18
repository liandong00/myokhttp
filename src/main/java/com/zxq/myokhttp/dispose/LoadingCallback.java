package com.zxq.myokhttp.dispose;


/**
 * 交互反馈
 */
public interface LoadingCallback {

    void startLoad();
    void loadCompleted();
    void loadError(Throwable e);
}

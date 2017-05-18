package com.zxq.myokhttp;

import okhttp3.Interceptor;

/**
 * 配置信息
 */
public interface NetWorkConfigInfo {

    /**
     * ULR
     * @return
     */
    String getBaseUrl();

    /**
     * 连接超时时间
     * @return
     */
    int getCONNECT_TIMEOUT();

    /**
     * 读取超时时间
     * @return
     */
    int getREAD_TIMEOUT();
    /**
     * 写入超时时间
     * @return
     */
    int getWRITE_TIMEOUT();

    Interceptor getDefauleInterceptor();
    Interceptor getTokenInterceptor();
    Interceptor getReTokenInterceptor();

}

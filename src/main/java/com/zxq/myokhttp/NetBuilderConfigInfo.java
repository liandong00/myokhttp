package com.zxq.myokhttp;

/**
 * 配置
 */
public interface NetBuilderConfigInfo {
//    // 缓存时间5分钟
//    long CACHE_TIME_SHORT = 1 * 60 * 5;
//    long CACHE_TIME_ORDINARY = 1 * 60 * 30;
//    long CACHE_TIME_LONG = 1 * 60 * 60 * 24;
    long getShortTime();
    long getOrdinaryTime();
    long getLongTime();
    Class getClazz();
}

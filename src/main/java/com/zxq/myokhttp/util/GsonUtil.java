package com.zxq.myokhttp.util;


import com.google.gson.Gson;

/**
 * GSON初始化
 */
public class GsonUtil
{
    private static class GsonHolder{
        private static final Gson INSTANCE = new Gson();
    }

    /**
     * 获取Gson实例，由于Gson是线程安全的，这里共同使用同一个Gson实例
     */
    public static Gson getGsonInstance()
    {
        return GsonHolder.INSTANCE;
    }
}

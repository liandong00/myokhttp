package com.zxq.myokhttp;


import android.content.Context;

import com.zxq.myokhttp.util.Global;

/**
 * 初始化信息
 */
public class HttpInitConfig {
    public static void initConfig(Context context, String unique, NetWorkConfigInfo netWorkConfigInfo, NetBuilderConfigInfo netBuilderConfigInfo) {
        Global.initConfig(context, unique);
        Network.setConfig(netWorkConfigInfo);
        NetBuilder.setNetBuilderConfigInfo(netBuilderConfigInfo);
    }
}

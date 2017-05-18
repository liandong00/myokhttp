package com.zxq.myokhttp;


import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.orhanobut.logger.Logger;
import com.zxq.myokhttp.okhttp.NetworkCacheInterceptor;
import com.zxq.myokhttp.util.GsonUtil;

import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public final class Network {


//    private static String baseUrl = "http://120.77.149.156:91";
//    private static int CONNECT_TIMEOUT = 8;
//    private static int READ_TIMEOUT = 15;
//    private static int WRITE_TIMEOUT = 15;

    private static OkHttpClient okHttpClient;
    private static Retrofit retrofit;


    private static NetWorkConfigInfo config;

    protected static void setConfig(NetWorkConfigInfo config) {
        Network.config = config;
    }

    public static RequestType getRequestType() {
        return requestType;
    }


    public static RequestBody getBody(String data) {
//        Gson gson = new Gson();
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), data);
        Logger.d("请求body:\n" +
                "      ******\n" +
                "        __\n" +
                "      /`__`\\\n" +
                "  .=.| ('') |.=.\n" +
                " /.-.\\ _)(_ /.-.\\\n" +
                "|:    / ~~ \\    :|\n" +
                "\\ :  | (__) |  : /\n" +
                " | :  \\_/\\_/  : |\n" +
                " |:  /|    |\\  :|\n" +
                " \\_/` |    | `\\_/\n" +
                "      |    |\n" +
                "      |    |\n" +
                "      |~~~~|\n" +
                "      '----'");
        Logger.json(data);
        return body;
    }

    /**
     * 请求类型
     */
    public enum RequestType {
        DefaultInterceptor, TokenInterceptor, ReTokenInterceptor
    }

    private static RequestType requestType;

    /**
     * 实体转RequestBody
     *
     * @param data
     */
    public static RequestBody getBody(Object data) {
        String dataStr = GsonUtil.getGsonInstance().toJson(data);
        return getBody(dataStr);
    }


    protected static Retrofit getRetrofit(RequestType requestType) {
        if (Network.requestType != requestType) {
            retrofit = null;
            okHttpClient = null;
//            Logger.d("枚举类型不相等");
        }
        if (retrofit == null) {
            synchronized (Network.class) {

                if (retrofit == null) {
                    OkHttpClient.Builder okHttpClient = getOkHttpClient().newBuilder();
                    if (requestType == RequestType.TokenInterceptor) {
                        if (config.getTokenInterceptor() != null) {
                            okHttpClient.addInterceptor(config.getTokenInterceptor());
                        }
                    } else if (requestType == RequestType.ReTokenInterceptor) {
                        if (config.getReTokenInterceptor() != null) {
                            okHttpClient.addInterceptor(config.getReTokenInterceptor());
                        }
                    }
                    retrofit = new Retrofit.Builder()
                            .client(okHttpClient.build())
                            .baseUrl(config.getBaseUrl())
                            .addConverterFactory(GsonConverterFactory.create())
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            //增加返回值为String的支持
                            //增加返回值为Gson的支持(以实体类返回)
                            .build();
                }
            }
        }
//        com.zxq.myokhttp.Network.requestType = requestType;
        return retrofit;
    }

    private static OkHttpClient getOkHttpClient() {
        if (okHttpClient == null) {
            synchronized (Network.class) {
                if (okHttpClient == null) {

                    OkHttpClient.Builder builder = new OkHttpClient.Builder()
                            .readTimeout(config.getREAD_TIMEOUT(), TimeUnit.SECONDS)//设置读取超时时间
                            .writeTimeout(config.getWRITE_TIMEOUT(), TimeUnit.SECONDS)//设置写的超时时间
                            .connectTimeout(config.getCONNECT_TIMEOUT(), TimeUnit.SECONDS);//设置连接超时时间
                    if (config.getDefauleInterceptor() != null) {
                        builder.addInterceptor(config.getDefauleInterceptor());
                    }

                    builder.addNetworkInterceptor(new NetworkCacheInterceptor());
                    okHttpClient = builder.build();
                }
            }
        }
        return okHttpClient;
    }


}

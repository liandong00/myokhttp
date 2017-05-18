package com.zxq.myokhttp.util;

import com.orhanobut.logger.Logger;
import com.zxq.myokhttp.NetBuilder;
import com.zxq.myokhttp.Network;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.RequestBody;
import okio.Buffer;

/**
 * Created by Johnny-R on 2017/5/17.
 */

public class Utils {



    /**
     * 获取 hashCode 专用
     * @param parames
     * @return
     */
    public static int getHashCode(String... parames) {
        StringBuilder sb = new StringBuilder();
        for (String parame :
                parames) {
            sb.append(parame);
        }
        return sb.toString().hashCode();
    }

    /**
     * 通过方法名请求体得到hashCode
     * @param shortUrl
     * @param requestBody
     * @param <T>
     * @return
     */
    public static <T> int getHashCode(String shortUrl,RequestBody requestBody) {
        String baseUrl = Network.getConfig().getBaseUrl();
        Class clazz = NetBuilder.config.getClazz();
//        String url = getMethodPostValue(clazz, methodName, parameterTypes);
        String body=requestBodyToStr(requestBody);
        StringBuilder sb = new StringBuilder();
        sb.append(baseUrl);
        sb.append(shortUrl);
        sb.append(body);
        return sb.toString().hashCode();
    }

    /**
     * 通过方法名,请求参数得到 hashCode
     * @param shortUrl
     * @param parames
     * @param <T>
     * @return
     */
    public static <T> int getHashCode(String shortUrl,String[] parames) {
        String baseUrl = Network.getConfig().getBaseUrl();
        Class clazz = NetBuilder.config.getClazz();
//        String url = getMethodPostValue(clazz, methodName, parameterTypes);
        StringBuilder sb = new StringBuilder();
        sb.append(baseUrl);
        sb.append(shortUrl);
        if (parames != null) {
            for (int i=0;i<parames.length;i++) {
                sb.append(parames[i]);
                if (i != parames.length - 1) {
                    sb.append("&");
                }
            }
        }
        Logger.d("URL拼接:%s", sb.toString());
        return sb.toString().hashCode();
    }

    /**
     * RequestBody转Str
     * @param requestBody
     * @return
     */
    public static String requestBodyToStr(RequestBody requestBody){
        Buffer buffer = new Buffer();
        try {
            requestBody.writeTo(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String body = buffer.readString(Charset.forName("UTF-8"));
        return body;
    }


    /**
     * 反射获取POST注解值
     * @param clazz
     * @param methodName
     * @param requestBody
     * @return
     */
    /*private static <T> String  getMethodPostValue(Class clazz, String methodName,Class<T>...requestBody) {
        try {
            Method method;
            if (requestBody != null) {
                method = clazz.getDeclaredMethod(methodName, requestBody);
            } else {
                method = clazz.getDeclaredMethod(methodName);
            }
            Logger.d("method:%s", method);
            Annotation[] annotations = method.getAnnotations();
//            Logger.d("annotations:%s", annotations);
            Annotation annotation = annotations[0];
            Logger.d("annotation:%s", annotation);

            POST mode = (POST) annotation;
            Logger.d("mode:%s", mode.value());
            return mode.value();
        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
            Logger.e(e, e.getMessage());
        }
        return "";
    }*/

}

package com.zxq.myokhttp.util;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.RequestBody;
import okio.Buffer;

/**
 * Created by Johnny-R on 2017/5/17.
 */

public class Utils {

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
}

package com.zxq.myokhttp.okhttp;


import android.text.TextUtils;

import com.orhanobut.logger.Logger;
import com.zxq.myokhttp.Network;
import com.zxq.myokhttp.db.CacheDao;
import com.zxq.myokhttp.entity.CacheBean;
import com.zxq.myokhttp.entity.CodeMsgBean;
import com.zxq.myokhttp.util.Global;
import com.zxq.myokhttp.util.GsonUtil;
import com.zxq.myokhttp.util.Utils;

import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * 缓存配置
 */
public class NetworkCacheInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Logger.i("请求URL:%s", request.url().toString());
        if (request.body() != null) {
            String body = Utils.requestBodyToStr(request.body());
            if (!TextUtils.isEmpty(Global.getUserUnique())) {
                Response originalResponse = chain.proceed(request);
                if (originalResponse.code() == 200) {
                    String value = originalResponse.body().string();
                    CodeMsgBean bean = GsonUtil.getGsonInstance().fromJson(value, CodeMsgBean.class);
                    if (bean.getCode() == 0) { //如果请求结果是成功的就缓存数据
//                    String body = Utils.requestBodyToStr(request.body());
//                        Logger.i("请求URL:%s,请求体:%s", request.url().toString(), body);
                        cacheData(request.url().toString(), body, value);
                    } else {
                        value.replace("[]", null);
                    }
                    Logger.json(value);
                    // 这里值得注意。由于前面value.bytes()把响应流读完并关闭了，所以这里需要重新生成一个response，否则数据就无法正常解析了
                    originalResponse = originalResponse.newBuilder()
                            .body(ResponseBody.create(null, value))
                            .build();
                }
                return originalResponse;
            }
        }

        return chain.proceed(request);
    }

    /**
     * 缓存数据
     *
     * @param url
     * @param body
     * @param data
     */
    private void cacheData(final String url, final String body, final String data) {
        Observable
                .create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(ObservableEmitter<String> e) throws Exception {
                        boolean isCache = filter(url);
                        if (isCache) {
                            int hashCode = Utils.getHashCode(url, body);
                            CacheBean cacheBean = new CacheBean(Global.getUserUnique(),hashCode, data);
                            CacheDao.getInstance(Global.getmContext()).add(cacheBean);
                        }
                        e.onNext("");
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {

                    }
                });

    }

    /**
     * 过滤不需要缓存
     *
     * @param url
     * @return
     */
    private boolean filter(String url) {
        String[] cacheUrl = Network.getConfig().cacheUrl();
        for (String cache : cacheUrl) {
            if (!cache.equalsIgnoreCase(url)) {

            } else {
                return true;
            }
        }
            return false;
    }


}

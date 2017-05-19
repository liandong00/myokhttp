package com.zxq.myokhttp;


import com.orhanobut.logger.Logger;
import com.zxq.myokhttp.db.CacheDao;
import com.zxq.myokhttp.dispose.BaseHttpResultSubscriber;
import com.zxq.myokhttp.entity.CacheBean;
import com.zxq.myokhttp.util.Global;
import com.zxq.myokhttp.util.GsonUtil;
import com.zxq.myokhttp.util.NetUtils;

import java.lang.reflect.Type;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class NetBuilder {

    public static NetBuilderConfigInfo config;

    protected static void setNetBuilderConfigInfo(NetBuilderConfigInfo netBuilderConfigInfo) {
        config = netBuilderConfigInfo;
    }


    //请求类型
    private Network.RequestType requestType = Network.RequestType.DefaultInterceptor;
    //解析
    private Type type;

    private int hashCode = -1;
    private long cacheTime = config.getShortTime();


    public NetBuilder setCache(int hashCode) {
        this.hashCode = hashCode;
        return this;
    }

    public NetBuilder setType(Type type) {
        this.type = type;
        return this;
    }


    public NetBuilder setCacheTime(long cacheTime) {
        this.cacheTime = cacheTime;
        return this;
    }



    public NetBuilder setRequestType(Network.RequestType requestType) {
        this.requestType = requestType;
        return this;
    }


    public NetBuilder() {

    }


    static Object refClass;


    public <T> T create() {
        if (requestType != Network.getRequestType()) {
            NetBuilder.refClass = null;
        }
        if (NetBuilder.refClass == null) {
            NetBuilder.refClass = Network.getRetrofit(requestType).create(config.getClazz());
        }
        return (T) NetBuilder.refClass;
    }


    private BaseHttpResultSubscriber subscriber;

    /**
     * 聚合 需要缓存时使用
     *
     * @param observable
     * @param observable2
     * @param <T>
     * @return 合并之后的数据
     */
    public <T> Observable<T> build(Observable<T> observable, Observable<T> observable2, BaseHttpResultSubscriber<T> subscriber) {
        this.subscriber = subscriber;
        if (observable == null) {
            observable2
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
        } else {
            observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
        }

        return null;
    }

    /**
     * 单例
     *
     * @param observable
     * @param <T>
     */
    public <T> void build(Observable<T> observable, BaseHttpResultSubscriber<T> subscriber) {
        this.subscriber = subscriber;
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }


    /**
     * 读取缓存
     *
     * @param <T>
     * @return
     */
    public <T> Observable<T> getCacheData() {
        if (hashCode != -1) {
            final CacheBean cacheBean = CacheDao.getInstance(Global.getmContext())
                    .findByData(hashCode, Global.getUserUnique());
            if (cacheBean != null && (Global.getTIMESTAMP() - cacheBean.getTimeStamp() <= cacheTime || !NetUtils.isConnected(Global.getmContext()))) {
                return Observable.create(new ObservableOnSubscribe<T>() {
                    @Override
                    public void subscribe(ObservableEmitter<T> emitter) throws Exception {
                        Logger.i("有效期之内,或者没有网络,使用缓存");
                        T entity = GsonUtil.getGsonInstance().fromJson(cacheBean.getData(), NetBuilder.this.type);
                        emitter.onNext(entity);
                        emitter.onComplete();
                    }
                });
            }
        }
        return null;
    }

    public void dispose() {
        if (subscriber != null) {
            if (subscriber.disposable != null) {
                subscriber.disposable.dispose();
            }
        }
    }
}

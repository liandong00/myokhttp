package com.zxq.myokhttp;


import com.orhanobut.logger.Logger;
import com.zxq.myokhttp.db.CacheDao;
import com.zxq.myokhttp.dispose.BaseHttpResultEntitySubscriber;
import com.zxq.myokhttp.dispose.BaseHttpResultListSubscriber;
import com.zxq.myokhttp.dispose.BaseHttpResultSubscriber;
import com.zxq.myokhttp.entity.CacheBean;
import com.zxq.myokhttp.entity.HttpEntity;
import com.zxq.myokhttp.entity.HttpListEntity;
import com.zxq.myokhttp.util.Global;
import com.zxq.myokhttp.util.GsonUtil;
import com.zxq.myokhttp.util.NetUtils;

import java.lang.reflect.Type;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class NetBuilder {

    public static NetBuilderConfigInfo config;

    protected static void setNetBuilderConfigInfo(NetBuilderConfigInfo netBuilderConfigInfo) {
        config = netBuilderConfigInfo;
    }

    private boolean isCACHE = false;

    //请求类型
    private Network.RequestType requestType = Network.RequestType.DefaultInterceptor;
    //解析
    private Type type;
    private Parsing parsing = Parsing.Obj;

    private int hashCode;
    private long cacheTime = config.getShortTime();

    public enum Parsing {
        List, Obj
    }


    public NetBuilder setCACHE(boolean CACHE) {
        isCACHE = CACHE;
        return this;
    }

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

    public NetBuilder setParsing(Parsing parsing) {
        this.parsing = parsing;
        return this;
    }


    public void setRequestType(Network.RequestType requestType) {
        this.requestType = requestType;
    }


    public NetBuilder() {

    }


    static Object refClass;


    public <T> T create() {
        if (NetBuilder.refClass == null) {
            NetBuilder.refClass =Network.getRetrofit(requestType).create(config.getClazz());
        }
        return (T) NetBuilder.refClass;
    }


    private BaseHttpResultSubscriber subscriber;


    public <T> void build(Observable<T> observable, BaseHttpResultSubscriber<T> subscriber) {
        if (hashCode != 0 && isCACHE) {
            CacheBean cacheBean = CacheDao.getInstance(Global.getmContext())
                    .findByData(hashCode, Global.getUserUnique());
            if (cacheBean != null && (Global.getTIMESTAMP() - cacheBean.getTimeStamp() <= cacheTime || !NetUtils.isConnected(Global.getmContext()))) {
                //有效期之内,或者没有网络,使用缓存
                Logger.i("有效期之内,或者没有网络,使用缓存");
                try {
                    if (parsing == Parsing.Obj) {
                        HttpEntity entity = GsonUtil.getGsonInstance().fromJson(cacheBean.getData(), this.type);
                        ((BaseHttpResultEntitySubscriber) subscriber).onNext(entity);
                    } else {
                        HttpListEntity entity = GsonUtil.getGsonInstance().fromJson(cacheBean.getData(), this.type);
                        ((BaseHttpResultListSubscriber) subscriber).onNext(entity);
                    }
                } catch (Exception ex) {
                    Logger.e(ex, ex.getMessage());
                }
                return;

            } else {
                if (cacheBean != null && NetUtils.isConnected(Global.getmContext()) && Global.getTIMESTAMP() - cacheBean.getTimeStamp() > cacheTime) {
                    Logger.i("数据过期,删除本地数据");
                    // 删除本地数据,重新加载
                    CacheDao.getInstance(Global.getmContext()).del(cacheBean);
                }
            }
        } else if (!isCACHE) {

        }
        this.subscriber = subscriber;
        Logger.i("加载网络数据");
        observable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(subscriber);
    }

    public void dispose() {
        if (subscriber != null) {
            if (subscriber.disposable != null) {
                subscriber.disposable.dispose();
            }
        }
    }
}

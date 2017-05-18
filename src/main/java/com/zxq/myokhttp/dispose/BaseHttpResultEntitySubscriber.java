package com.zxq.myokhttp.dispose;

import com.orhanobut.logger.Logger;
import com.zxq.myokhttp.entity.HttpEntity;
import com.zxq.myokhttp.thro.ThrowableHttp;

/**
 * 结果统一处理和分发
 */
public abstract class BaseHttpResultEntitySubscriber<T> extends BaseHttpResultSubscriber<HttpEntity<T>> {


    @Override
    public void onNext(HttpEntity<T> tHttpEntity) {
        Logger.d("服务端返回状态\n" +
                "                   .-' _..`.\n" +
                "                  /  .'_.'.'\n" +
                "                 | .' (.)`.\n" +
                "                 ;'   ,_   `.\n" +
                " .--.__________.'    ;  `.;-'\n" +
                "|  ./               /\n" +
                "|  |               / \n" +
                "`..'`-._  _____, ..'\n" +
                "     / | |     | |\\ \\\n" +
                "    / /| |     | | \\ \\\n" +
                "   / / | |     | |  \\ \\\n" +
                "  /_/  |_|     |_|   \\_\\\n" +
                " |__\\  |__\\    |__\\  |__\\" + tHttpEntity.getCode());

        if (tHttpEntity.getCode() == 0) {
            onSuccess(tHttpEntity);
        } else {
            ThrowableHttp throwableHttp = new ThrowableHttp(tHttpEntity);
            onError(throwableHttp);
        }
    }


    public abstract void onSuccess(HttpEntity<T> t);


}

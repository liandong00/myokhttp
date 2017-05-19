package com.zxq.myokhttp.dispose;


import com.orhanobut.logger.Logger;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 结果统一处理和分发
 */
public abstract class BaseHttpResultSubscriber<T>  implements Observer<T> {
    public Disposable disposable;
    private LoadingCallback loadingCallback;

    public void setLoadingCallback(LoadingCallback loadingCallback) {
        this.loadingCallback = loadingCallback;
    }

    @Override
    public void onSubscribe(Disposable d) {
        disposable = d;
        if (loadingCallback != null) {
            loadingCallback.startLoad();
        }
        Logger.d("\n" +
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
                " |__\\  |__\\    |__\\  |__\\开始执行:onSubscribe()");
    }

    @Override
    public void onError(Throwable e) {
        if (loadingCallback != null) {
            //加载异常
            loadingCallback.loadError(e);
        }
        _onError(e);
    }

    @Override
    public void onComplete() {
        Logger.d("\n" +
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
                " |__\\  |__\\    |__\\  |__\\执行完成:onCompleted()");
        if (loadingCallback != null) {
            loadingCallback.loadCompleted();
        }
    }

    public abstract void _onError(Throwable e);
}

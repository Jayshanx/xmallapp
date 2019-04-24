package com.me.guanpj.mall.library.rx

import com.me.guanpj.mall.library.mvp.IBaseView
import rx.Subscriber

/*
  Rx订阅者默认实现
 */
open class BaseSubscriber<T>(val baseView: IBaseView) : Subscriber<T>() {

    override fun onCompleted() {
        baseView.hideLoading()
    }

    override fun onNext(t: T) {
    }

    override fun onError(e: Throwable?) {
        baseView.hideLoading()
        if (e is BaseException) {
            baseView.onError(e.msg)
        }
    }
}

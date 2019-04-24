package com.me.guanpj.mall.order.module.order.list

import com.me.guanpj.mall.library.ext.excute
import com.me.guanpj.mall.library.mvp.presenter.BasePresenter
import com.me.guanpj.mall.library.rx.BaseSubscriber
import com.me.guanpj.mall.order.data.Order
import com.me.guanpj.mall.order.service.OrderService
import javax.inject.Inject

/*
  订单列表Presenter
 */
class OrderListPresenter @Inject constructor() : BasePresenter<OrderListContract.View>(), OrderListContract.Presenter {

    @Inject
    lateinit var orderService: OrderService

    /*
      根据订单状态获取订单列表
     */
    fun getOrderList(orderStatus: Int) {
        if (!checkNetWork()) {
            return
        }
        getView().showLoading()
        orderService.getOrderList(orderStatus).excute(object : BaseSubscriber<MutableList<Order>?>(getView()) {
            override fun onNext(t: MutableList<Order>?) {
                getView().onGetOrderListResult(t)
            }
        }, mLifecycleProvider)
    }

    /*
      订单确认收货
     */
    fun confirmOrder(orderId: Int) {
        if (!checkNetWork()) {
            return
        }
        getView().showLoading()
        orderService.confirmOrder(orderId).excute(object : BaseSubscriber<Boolean>(getView()) {
            override fun onNext(t: Boolean) {
                getView().onConfirmOrderResult(t)
            }
        }, mLifecycleProvider)
    }

    /*
      取消订单
     */
    fun cancelOrder(orderId: Int) {
        if (!checkNetWork()) {
            return
        }
        getView().showLoading()
        orderService.cancelOrder(orderId).excute(object : BaseSubscriber<Boolean>(getView()) {
            override fun onNext(t: Boolean) {
                getView().onCancelOrderResult(t)
            }
        }, mLifecycleProvider)
    }
}

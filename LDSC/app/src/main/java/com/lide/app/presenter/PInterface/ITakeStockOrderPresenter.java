package com.lide.app.presenter.PInterface;

import com.lubin.bean.TakeStockOrder;

/**
 * Created by lubin on 2016/7/18.
 */
public interface ITakeStockOrderPresenter {
    //获取盘点单
    void getTakeStockOrders();

    //绑定任务
    void bindCheckTask(TakeStockOrder takeStockOrder);
}
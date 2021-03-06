package com.lide.app.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lide.app.R;
import com.lide.app.persistence.widget.view.SwipeItemLayout;
import com.lide.app.presenter.outbound.ControlOBOrderByCreatePresenter;
import com.lubin.bean.OutBoundOrder;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by lubin on 2016/11/19.
 */

public class OutBoundOrderAdapter extends BaseListAdapter<OutBoundOrder> {

    private View slideView;
    private ControlOBOrderByCreatePresenter mPresenter;

    public OutBoundOrderAdapter(Context context, List<OutBoundOrder> list, ControlOBOrderByCreatePresenter mPresenter) {
        super(context, list);
        this.mPresenter = mPresenter;
    }

    @Override
    public View bindView(int position, View convertView, ViewGroup parent) {

        View container = mInflater.inflate(R.layout.item_order_layout, parent, false);

        final OutBoundOrder outBoundOrder = getList().get(position);
        TextView order_code = (TextView) container.findViewById(R.id.order_name);
        TextView order_address = (TextView) container.findViewById(R.id.order_address);
        TextView order_state = (TextView) container.findViewById(R.id.order_audit_state);
        TextView order_date = (TextView) container.findViewById(R.id.order_date);
        TextView text_state = (TextView) container.findViewById(R.id.text_state);

        order_code.setText(outBoundOrder.getCode());
        order_address.setText(outBoundOrder.getToWarehouseName() + "," + outBoundOrder.getQty() + "/" + outBoundOrder.getOperateQty() + "件");
        order_state.setText(outBoundOrder.getStatus());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        order_date.setText(format.format(outBoundOrder.getCreateTime()));
        if (outBoundOrder.getStatus() != null && outBoundOrder.getStatus().equals("已完成")) {
            text_state.setVisibility(View.VISIBLE);
            order_state.setTextColor(Color.BLUE);
        } else {
            order_state.setTextColor(Color.BLACK);
            text_state.setVisibility(View.INVISIBLE);
        }

        slideView = LayoutInflater.from(mContext).inflate(R.layout.item_order_item, null);
        TextView orderAudit = (TextView) slideView.findViewById(R.id.order_audit);
        orderAudit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog("审核", outBoundOrder);
            }
        });
        TextView orderDel = (TextView) slideView.findViewById(R.id.order_del);
        orderDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog("删除", outBoundOrder);
            }
        });

        return new SwipeItemLayout(container, slideView, null, null);
    }

    public void showDialog(final String msg, final OutBoundOrder data) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("是否" + msg + ":");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (msg.equals("审核")) {
                    mPresenter.confirmOutBoundOrder(data.getOrderId());
                } else {
                    mPresenter.deleteOutBoundOrder(data);
                }
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }
}

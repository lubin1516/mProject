package com.lide.app.ui.takeStock.urentry;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;

import com.lide.app.R;
import com.lide.app.adapter.TakeStockOrderURAdapter;
import com.lide.app.bean.JsonToBean.TakeStockOrderList;
import com.lide.app.config.Configs;
import com.lide.app.persistence.util.Utils;
import com.lide.app.persistence.view.xlist.XListView;
import com.lide.app.presenter.takeStock.DownLoadTackStockOrderListPresenterImpl;
import com.lide.app.ui.FragmentBase;
import com.lide.app.ui.LoginActivity;
import com.lide.app.ui.VInterface.IDataFragmentView;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author DaiJiCheng
 * @time 2017/2/23  15:12
 * @desc ${根据盘点单Id获取对应的任务列表}
 */
public class SearchCheckOrderListFragment extends FragmentBase implements IDataFragmentView<List<TakeStockOrderList.DataBean>>, XListView.IXListViewListener {

    @BindView(R.id.xlv_task_order)
    XListView lvOrders;
    @BindView(R.id.search_check_text)
    EditText searchText;
    private View view;
    DownLoadTackStockOrderListPresenterImpl mPresenter;
    ControlCheckTaskActivity mActivity;

    List<TakeStockOrderList.DataBean> takeStockOrders;
    TakeStockOrderURAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search_check_order, container, false);
        ButterKnife.bind(this, view);
        mActivity = (ControlCheckTaskActivity) getActivity();
        mPresenter = new DownLoadTackStockOrderListPresenterImpl(this);
        initView();
        return view;
    }

    private void initView() {
        lvOrders.setPullLoadEnable(false);
        lvOrders.setPullRefreshEnable(false);
        lvOrders.setXListViewListener(this);

        takeStockOrders = new ArrayList<>();
        mAdapter = new TakeStockOrderURAdapter(mContext, takeStockOrders);
        lvOrders.setAdapter(mAdapter);
        lvOrders.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TakeStockOrderList.DataBean takeStockOrder = mAdapter.getList().get(position - 1);
                mActivity.setCurrentFragment(2, takeStockOrder, text,index);
            }
        });
    }

    @Override
    public void ShowData(final List<TakeStockOrderList.DataBean> takeStockOrderList) {
        if (flag) takeStockOrders.clear();
        runOnUiThread(new TimerTask() {
            @Override
            public void run() {
                if (takeStockOrderList != null && takeStockOrderList.size() > 0) {
                    takeStockOrders.addAll(takeStockOrderList);
                    mAdapter.notifyDataSetChanged();
                } else {
                    showDialog("没有新数据了");
                    lvOrders.setPullLoadEnable(false);
                }
            }
        });
    }


    @Override
    public void onRefresh() {

    }

    @Override
    public void onResume() {
        super.onResume();
        if (text != null) {
            mPresenter.downLoadTackStockOrderList(Utils.getCurrentUser().getWarehouseId(), text, true, index, 10 * index);
            lvOrders.setPullLoadEnable(true);
        }
    }

    int index = 1;

    boolean flag = false;
    public String text = null;

    @Override
    public void onLoadMore() {
        index++;
        String search = searchText.getText().toString();
        mPresenter.downLoadTackStockOrderList(Utils.getCurrentUser().getWarehouseId(), search, true, index, 10);
        text = search;
        flag = false;
    }

    @OnClick(R.id.search_check_btn)
    public void onClick() {
        if (Utils.getCurrentUser() != null) {
            //分页，第一页，20条
            String search = searchText.getText().toString();
            mPresenter.downLoadTackStockOrderList(Utils.getCurrentUser().getWarehouseId(), search, true, 1, 10);
            lvOrders.setPullLoadEnable(true);
            flag = true;
            index = 1;
            text = search;
        } else {
            //未登录
            Intent login = new Intent(getActivity(), LoginActivity.class);
            login.putExtra("isAtNet", true);
            startActivityForResult(login, Configs.LOGIN);
        }
    }
}

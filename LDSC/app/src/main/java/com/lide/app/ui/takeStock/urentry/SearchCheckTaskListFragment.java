package com.lide.app.ui.takeStock.urentry;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.lide.app.R;
import com.lide.app.adapter.TaskOrderAdapter;
import com.lide.app.bean.JsonToBean.UR.TaskList;
import com.lide.app.persistence.view.xlist.XListView;
import com.lide.app.presenter.takeStock.DownLoadTackStockOrderListPresenterImpl;
import com.lide.app.ui.FragmentBase;
import com.lide.app.ui.VInterface.IDataFragmentView;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author DaiJiCheng
 * @time 2017/2/23  15:11
 * @desc ${盘点单列表界面}
 */
public class SearchCheckTaskListFragment extends FragmentBase implements IDataFragmentView<List<TaskList.DataBean>>, XListView.IXListViewListener {

    @BindView(R.id.xlv_task_by_order)
    XListView lvTask;
    private View view;

    DownLoadTackStockOrderListPresenterImpl mPresenter;
    List<TaskList.DataBean> beanList;
    TaskOrderAdapter mAdapter;

    public String orderId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search_check_task, container, false);
        ButterKnife.bind(this, view);
        mPresenter = new DownLoadTackStockOrderListPresenterImpl(this);
        initView();
        return view;
    }

    @Override

    public void onResume() {
        super.onResume();
        mPresenter.downLoadTaskList(orderId, true, 1, 20);
    }

    private void initView() {
        lvTask.setPullLoadEnable(true);
        lvTask.setPullRefreshEnable(false);
        lvTask.setXListViewListener(this);

        beanList = new ArrayList<>();
        mAdapter = new TaskOrderAdapter(getActivity(), beanList);
        lvTask.setAdapter(mAdapter);
        lvTask.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //下载逻辑
                mPresenter.loadTaskDetail(beanList.get(position - 1));
            }
        });
    }

    @Override
    public void showDialog(String result) {
        super.showDialog(result);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void ShowData(final List<TaskList.DataBean> dataBeanList) {
        runOnUiThread(new TimerTask() {
            @Override
            public void run() {
                if (dataBeanList != null && dataBeanList.size() > 0) {
                    beanList.addAll(dataBeanList);
                    mAdapter.notifyDataSetChanged();
                } else {
                    showDialog("没有新数据了");
                    lvTask.setPullLoadEnable(false);
                }
            }
        });
    }

    /*
    * 全部下载
    * */
    @OnClick(R.id.btn_order_task_down_load)
    public void onClick() {
        mPresenter.loadAllTaskDetail(beanList);
    }

    @Override
    public void onRefresh() {

    }

    int index = 1;

    @Override
    public void onLoadMore() {
        index++;
        mPresenter.downLoadTaskList(orderId, true, index, 20);
    }
}

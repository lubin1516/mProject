package com.lide.app.ui.outbound.createOrder;

import android.content.Intent;
import android.extend.util.KeyboardUtils;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.internal.LinkedTreeMap;
import com.lide.app.R;
import com.lide.app.adapter.ScanBarcodeInOutBoundOrderAdapter;
import com.lide.app.config.Configs;
import com.lide.app.listener.OnFinishListener;
import com.lide.app.persistence.util.DBOperator;
import com.lide.app.persistence.util.SoundUtils;
import com.lide.app.persistence.util.Utils;
import com.lide.app.presenter.ScanPresenter;
import com.lide.app.presenter.outbound.ManageOBOrderByCreatePresenter;
import com.lide.app.ui.FragmentBase;
import com.lide.app.ui.LoginActivity;
import com.lide.app.ui.VInterface.IDataFragmentView;
import com.lubin.bean.OutBoundDetail;
import com.lubin.bean.OutBoundOperate;
import com.lubin.bean.OutBoundOrder;
import com.lubin.dao.OutBoundDetailDao;
import com.lubin.dao.OutBoundOperateDao;
import com.lubin.dao.OutBoundOrderDao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lubin on 2016/11/21.
 */

public class ScanningBarcodeInOrderByCreateFragment extends FragmentBase implements IDataFragmentView<List<LinkedTreeMap>> {

    @BindView(R.id.scan_et_sku)
    EditText mEt_barcode;
    @BindView(R.id.scan_et_num)
    EditText mEt_num;
    @BindView(R.id.all_num)
    TextView mSum;
    @BindView(R.id.scan_lv_sku)
    ListView lv_container_barcode;


    private View mView;
    private ScanOBOrderByCreateActivity mActivity;
    private ScanBarcodeInOutBoundOrderAdapter mAdapter;
    private List<OutBoundDetail> mList = new ArrayList<>();
    private DBOperator<OutBoundOrderDao, OutBoundOrder> orderDBOperator;
    private DBOperator<OutBoundDetailDao, OutBoundDetail> detailDBOperator;
    private DBOperator<OutBoundOperateDao, OutBoundOperate> operateDBOperator;
    private ManageOBOrderByCreatePresenter outboundPresenter;
    private ScanPresenter scanPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_scan_barcode, container, false);
        ButterKnife.bind(this, mView);
        mActivity = (ScanOBOrderByCreateActivity) getActivity();
        initView();
        initListView();
        orderDBOperator = OutboundTransaction.getOrderDBOperator();
        detailDBOperator = OutboundTransaction.getDetailDBOperator();
        operateDBOperator = OutboundTransaction.getOperateDBOperator();
        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        initScanPresenter();
        refreshData();
        mEt_barcode.requestFocus();
    }

    @Override
    public void onPause() {
        super.onPause();
        scanPresenter.setMode(0);
        scanPresenter.removeListener();
    }

    private void initView() {
        mEt_barcode.setImeOptions(EditorInfo.IME_ACTION_SEND);
        mEt_barcode.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    saveData(mEt_barcode.getText().toString());
                    return true;
                }
                return false;
            }
        });
        mEt_num.setText("1");
    }

    public void initListView() {
        mAdapter = new ScanBarcodeInOutBoundOrderAdapter(mActivity, mList);
        lv_container_barcode.setAdapter(mAdapter);
    }

    //初始化扫描器
    private void initScanPresenter() {
        scanPresenter = new ScanPresenter(this);
        //该读写器设置为只能扫描条码，不能读取RFID
        scanPresenter.setMode(2);
        scanPresenter.setListener(new OnFinishListener() {
            @Override
            public void OnFinish(String data) {
                saveData(data);
            }
        });
        outboundPresenter = new ManageOBOrderByCreatePresenter(this);
    }

    public void refreshData() {
        OutBoundOrder outBoundOrder = orderDBOperator.getItemByID(mActivity.orderId);
        List<OutBoundDetail> details = outBoundOrder.getDetails();
        Collections.sort(details, new Comparator<OutBoundDetail>() {
            @Override
            public int compare(OutBoundDetail lhs, OutBoundDetail rhs) {
                return rhs.getRefreshTime().compareTo(lhs.getRefreshTime());
            }
        });
        mAdapter.addAll(details);
        int sum = 0;
        for (OutBoundDetail outBoundDetail : mList) {
            sum += outBoundDetail.getOperateQty();
        }
        mSum.setText(String.valueOf(sum));
    }

    //根据条码查找本地条码明细，如果条码明细已经存在，更新条码明细的操作数量，如果该条码不存在，
    //请求API验证条码是否存在数据库，如果存在情况下生成根据该条码生成条码明细
    private void saveData(final String barcode) {
        SoundUtils.play(1);
        if (Utils.getApiKey() == null) {
            Intent login = new Intent(getActivity(), LoginActivity.class);
            login.putExtra("isAtNet", true);
            startActivityForResult(login, Configs.LOGIN);
            return;
        }
        if (barcode == null) {
            ShowToast("没有数据！");
            return;
        }
        mEt_barcode.setText(barcode);
        int addNum = Integer.parseInt(mEt_num.getText().toString());
        OutBoundOrder outBoundOrder = orderDBOperator.getItemByID(mActivity.orderId);
        OutBoundDetail outBoundDetail = null;
        for (OutBoundDetail bean : outBoundOrder.getDetails()) {
            if (bean.getBarcode().equals(barcode)) {
                outBoundDetail = bean;
                break;
            }
        }
        if (outBoundDetail == null) {
            if (addNum < 0) {
                showDialog("不能小于0!");
                return;
            }
            //判断是不是单
            if (outBoundOrder.getIsCreate())
                outboundPresenter.SearchSkuName(barcode);//验证条码是否存在
        } else {
            //更新条码的操作数量
            updateDetail(outBoundDetail);
        }
    }

    //更新条码的操作数量
    private void updateDetail(OutBoundDetail outBoundDetail) {
        OutBoundOrder outBoundOrder = orderDBOperator.getItemByID(mActivity.orderId);
        int addNum = Integer.parseInt(mEt_num.getText().toString());
        OutBoundOperate operate = null;
        for (OutBoundOperate outBoundOperate : outBoundDetail.getOperates()) {
            if (outBoundOperate.getBarcode() != null) {
                if ((outBoundOperate.getOperateQty() + addNum) < 0) {
                    showDialog("不能小于0!");
                    return;
                }
                operate = outBoundOperate;
                break;
            }
        }
        if (operate == null) {
            if (addNum < 0) {
                showDialog("不能小于0!");
                return;
            }
            operate = new OutBoundOperate();
            operate.setBarcode(outBoundDetail.getBarcode());
            operate.setOperateQty(addNum);
            operate.setOutBoundDetail(outBoundDetail);
            operate.setOutBoundOrder(outBoundOrder);
            operate.setIsUpload(false);
            operateDBOperator.insertData(operate);
            outboundPresenter.createMultiBarcode(operate);
            outBoundDetail.resetOperates();
        } else {
            operate.setOperateQty(operate.getOperateQty() + addNum);
            operate.setIsUpload(false);
            operate.update();
        }
        outBoundDetail.setQty(outBoundDetail.getQty() + addNum);
        outBoundDetail.setOperateQty(outBoundDetail.getOperateQty() + addNum);
        outBoundDetail.setRefreshTime(new Date(System.currentTimeMillis()));
        outBoundDetail.update();

        UpdateView();
    }

    //验证返回
    @Override
    public void ShowData(List<LinkedTreeMap> linkedTreeMaps) {
        final String skuName = linkedTreeMaps.get(0).get("productName").toString();
        final String barcode = linkedTreeMaps.get(0).get("barcode").toString();
        saveBarcode(skuName, barcode);
    }

    //根据条码与数量生成条码明细
    private void saveBarcode(final String skuName, final String barcode) {
        OutBoundOrder outBoundOrder = orderDBOperator.getItemByID(mActivity.orderId);
        int addNum = Integer.parseInt(mEt_num.getText().toString());
        OutBoundDetail outBoundDetail = null;
        OutBoundOperate outBoundOperate = null;

        outBoundDetail = new OutBoundDetail();
        outBoundDetail.setQty(addNum);
        outBoundDetail.setOperateQty(addNum);
        outBoundDetail.setBarcode(barcode);
        outBoundDetail.setOutBoundOrder(outBoundOrder);
        outBoundDetail.setSkuName(skuName);
        outBoundDetail.setRefreshTime(new Date(System.currentTimeMillis()));
        detailDBOperator.insertData(outBoundDetail);

        outBoundOperate = new OutBoundOperate();
        outBoundOperate.setBarcode(barcode);
        outBoundOperate.setOperateQty(addNum);
        outBoundOperate.setOutBoundDetail(outBoundDetail);
        outBoundOperate.setOutBoundOrder(outBoundOrder);
        outBoundOperate.setIsUpload(false);
        operateDBOperator.insertData(outBoundOperate);
        outboundPresenter.createMultiBarcode(outBoundOperate);

        UpdateView();
    }

    //重新计算出库单对象的操作数
    private void UpdateView() {
        OutBoundOrder outBoundOrder = orderDBOperator.getItemByID(mActivity.orderId);
        outBoundOrder.resetOperates();
        outBoundOrder.resetDetails();
        int orderSum = 0;
        for (OutBoundOperate operate : outBoundOrder.getOperates()) {
            orderSum += operate.getOperateQty();
        }
        outBoundOrder.setQty(orderSum);
        if (orderSum == 0)
            outBoundOrder.setStatus("新单");
        else
            outBoundOrder.setStatus("处理中");
        outBoundOrder.setOperateQty(orderSum);
        outBoundOrder.update();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mEt_num.setText("1");
                mEt_barcode.setText("");
                refreshData();
            }
        });
        KeyboardUtils.hideSoftInput(getActivity());
    }

    @Override
    public void startScan() {
        scanPresenter.startScanBarcode();
    }

}

package com.lubin.chj.view.activity;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.lubin.chj.Listener.OnBroadCaseFinishListener;
import com.lubin.chj.MApplication;
import com.lubin.chj.R;
import com.lubin.chj.service.BarcodeReceiver;
import com.lubin.chj.service.ScanServiceWithUHF;
import com.lubin.chj.utils.SharePreferenceUtil;
import com.lubin.chj.view.activity.Fragment.FragmentBase;
import com.lubin.chj.view.activity.Fragment.InventoryByGwFragment;
import com.lubin.chj.view.activity.Fragment.InventoryByGwFragmentFirst;
import com.lubin.chj.view.activity.Fragment.InventoryMainFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author DaiJiCheng
 * @time 2016/9/27  15:22
 * @desc ${柜位盘点}
 */
public class InventoryActivity extends BaseActivity {
    @BindView(R.id.tb_common)
    Toolbar mTbCommon;
    @BindView(R.id.common_title)
    AppBarLayout mCommonTitle;
    @BindView(R.id.fragments_container)
    RelativeLayout mFragmentsContainer;
    private ArrayList<FragmentBase> mFragments;
    private int currentFragment;
    public ScanServiceWithUHF mService = ScanServiceWithUHF.getInstance();
    private String mFlag = "电子标签";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);
        ButterKnife.bind(this);
        initHeader();
        initFragmet();
    }

    @Override
    protected void onResume() {
        mTbCommon.setTitle("盘点");
        initService();
        super.onResume();
    }

    private SharePreferenceUtil mSpUtil = MApplication.getInstance().getSpUtil();
    String barCode = "0000000000";

    private void initService() {
        if (mSpUtil.getBarCode() != null && !mSpUtil.getBarCode().isEmpty())
            barCode = mSpUtil.getBarCode();
        BarcodeReceiver.setListener(new OnBroadCaseFinishListener() {
            @Override
            public void onBroadCaseFinish(final String data) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!data.equals(barCode)) {
                            mFragments.get(currentFragment).setBarcode(data);
                        }
                    }
                });
            }
        });
    }


    private void initHeader() {
        setSupportActionBar(mTbCommon);
        mTbCommon.setNavigationIcon(R.mipmap.back);
        mTbCommon.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentFragment != 0)
                    mFragments.get(currentFragment).finishByBackIcon();
                else
                    finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_inventory, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getTitle().equals("条码")) {
            mFlag = "电子标签";
            item.setTitle("电子标签");
        } else {
            mFlag = "条码";
            item.setTitle("条码");
        }
        return super.onOptionsItemSelected(item);
    }

    private void initFragmet() {
        currentFragment = 0;
        mFragments = new ArrayList<>();
        mFragments.add(new InventoryMainFragment());
        mFragments.add(new InventoryByGwFragmentFirst());
        mFragments.add(new InventoryByGwFragment());

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragments_container, mFragments.get(0), "0")
                .add(R.id.fragments_container, mFragments.get(1), "1")
                .add(R.id.fragments_container, mFragments.get(2), "2")
                .show(mFragments.get(0))
                .hide(mFragments.get(1))
                .hide(mFragments.get(2))
                .commit();
    }

    public void changeFragment(int index) {
        getSupportFragmentManager()
                .beginTransaction()
                .show(mFragments.get(index))
                .hide(mFragments.get(currentFragment))
                .commit();
        currentFragment = index;
        switch (index) {
            case 0:
                mTbCommon.setTitle("盘点");
                break;
            case 1:
                mTbCommon.setTitle("柜位盘点");
                break;
            case 2:
                mTbCommon.setTitle("柜位盘点");
                break;

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getRepeatCount() == 0) {
            if (275 == keyCode) {
                if (mFlag.equals("电子标签"))
                    mService.inventory();
                else
                    mService.scanBarcode();
            }
        }
        return true;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (event.getRepeatCount() == 0) {
            if (275 == keyCode) {
                if (mFlag.equals("电子标签"))
                    mService.pause();
                else
                    mService.stopScan();
            }
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (currentFragment != 0)
            mFragments.get(currentFragment).finishByBackBtn();
        else
            finish();
    }
}

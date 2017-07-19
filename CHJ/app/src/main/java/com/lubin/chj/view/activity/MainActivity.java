package com.lubin.chj.view.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Process;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.lubin.chj.Listener.OnNetReqFinishListener;
import com.lubin.chj.MApplication;
import com.lubin.chj.R;
import com.lubin.chj.modle.LoginModelImpl;
import com.lubin.chj.presenter.CabinetPresenterImpl;
import com.lubin.chj.service.ScanServiceWithUHF;
import com.lubin.chj.utils.MyReceiver;
import com.lubin.chj.utils.SharePreferenceUtil;
import com.lubin.chj.view.activity.VInterface.ICabinetView;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements ICabinetView<String> {


    @BindView(R.id.tb_common)
    Toolbar tbCommon;
    @BindView(R.id.equipId)
    TextView tvEquipId;
    @BindView(R.id.light_color)
    TextView lightColor;
    private ScanServiceWithUHF mScanServiceWithUHF = ScanServiceWithUHF.getInstance();
    private LoginModelImpl loginModel;
    private CabinetPresenterImpl mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        loginModel = new LoginModelImpl();
        mPresenter = new CabinetPresenterImpl(this);
        //initScanService();
        StartMoniter();
        initHeader();
    }

    private void initHeader() {
        tbCommon.setNavigationIcon(R.mipmap.back);
        tbCommon.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leave();
            }
        });
        tbCommon.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_out:
                        Intent i = getBaseContext().getPackageManager()
                                .getLaunchIntentForPackage(getBaseContext().getPackageName());
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                        break;
                }
                return true;
            }
        });

        if (mSpUtil.getLight() == null || mSpUtil.getLight().isEmpty()) mSpUtil.setLight("是");
        if (mSpUtil.getBarCode() == null || mSpUtil.getBarCode().isEmpty())
            mSpUtil.setBarCode("0123456789");
        mPresenter.setColor();

    }

    @Override
    protected void onResume() {
        tbCommon.setTitle(MApplication.getInstance().getSpUtil().getName());
        tvEquipId.setText(MApplication.getSerialNumber());
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mScanServiceWithUHF.Close();
        StopMoniter();
    }

    private void logout() {
        loginModel.logout(new OnNetReqFinishListener() {
            @Override
            public void OnNetReqFinish(Map<String, Object> hashMap) {
                Object result = hashMap.get("result");
                if (result == null) return;
                try {
                    JSONObject jsonObject = new JSONObject(result.toString());
                    String returnCode = jsonObject.getString("returnCode");
                    String returnMsg = jsonObject.getString("returnMsg");
                    if (returnCode.equals("0000")) {
                        Process.killProcess(Process.myPid());
                        ShowDialog(returnMsg);
                    } else {
                        ShowDialog(returnMsg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // region 待机监控
    protected MyReceiver receiver = null;


    protected void StartMoniter() {
        receiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(MyReceiver.ActionOffScreen);
        filter.addAction(MyReceiver.ActionOnScreen);
        filter.setPriority(Integer.MAX_VALUE);
        registerReceiver(receiver, filter);
    }

    protected void StopMoniter() {
        unregisterReceiver(receiver);
    }


    @OnClick({R.id.btn_store, R.id.btn_take, R.id.btn_adjust, R.id.btn_stockTake, R.id.btn_setting, R.id.btn_out, R.id.btn_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_store:
                startActivity(new Intent(this, StoreActivity.class));
                break;
            case R.id.btn_take:
                startActivity(new Intent(this, PickActivity.class));
                break;
            case R.id.btn_adjust:
                startActivity(new Intent(this, AdjustActivity.class));
                break;
            case R.id.btn_stockTake:
                startActivity(new Intent(this, InventoryActivity.class));
                break;
            case R.id.btn_search:
                startActivity(new Intent(this, SearchActivity.class));
                break;
            case R.id.btn_setting:
                startActivity(new Intent(this, SettingActivity.class));
                break;
            case R.id.btn_out:
                loginModel.logout(new OnNetReqFinishListener() {
                    @Override
                    public void OnNetReqFinish(Map<String, Object> hashMap) {
                        Object result = hashMap.get("result");
                        if (result == null) return;
                        try {
                            JSONObject jsonObject = new JSONObject(result.toString());
                            String returnCode = jsonObject.getString("returnCode");
                            String returnMsg = jsonObject.getString("returnMsg");
                            if (returnCode.equals("0000")) {
                                MApplication.getInstance().getSpUtil().setPassword("");
                                Intent intent = new Intent(MainActivity.this, SplashActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            } else {
                                ShowDialog(returnMsg);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                break;
        }
    }

    @Override
    public void onBackPressed() {
        leave();
    }

    private void leave() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示！");
        builder.setMessage("是否退出系统？");
        builder.setPositiveButton("退出", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                logout();
            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }

    private SharePreferenceUtil mSpUtil = MApplication.getInstance().getSpUtil();

    @Override
    public void ShowCabinets(final List<String> t) {
        if (t != null && t.size() > 0) {
            mSpUtil.setLightColor("#" + t.get(0));
        } else {
            mSpUtil.setLightColor("#000000");
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                lightColor.setBackgroundColor(Color.parseColor("#" + t.get(0)));
            }
        });
    }

    @Override
    public void changeBtnStatus() {

    }
}
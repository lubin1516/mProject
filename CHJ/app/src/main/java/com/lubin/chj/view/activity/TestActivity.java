package com.lubin.chj.view.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lubin.chj.Listener.OnNetReqFinishListener;
import com.lubin.chj.R;
import com.lubin.chj.bean.PcInfo;
import com.lubin.chj.modle.PickModel;
import com.lubin.chj.utils.SoapUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author DaiJiCheng
 * @time 2016/9/20  10:19
 * @desc ${测试}
 */
public class TestActivity extends BaseActivity {
    @BindView(R.id.bt_test)
    Button mBtTest;
    @BindView(R.id.tv_text)
    TextView mTvText;
    @BindView(R.id.bt_test1)
    Button mBtTest1;
    @BindView(R.id.tv_text1)
    TextView mTvText1;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    test(msg);
                    break;
                case 2:
                    test1(msg);
                    break;
            }

        }

        private void test1(Message msg) {
            String datas = msg.obj.toString();
            mTvText1.setText(datas);
            Log.e("PCSTORE", datas);
        }

        private void test(Message msg) {
            String datas = msg.obj.toString();
            mTvText.setText(datas);
            Log.e("kuwei", datas);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.bt_test, R.id.bt_test1,R.id.bt_test2})
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.bt_test:
                queryGw();
                break;
            case R.id.bt_test1:
               // storePc();
              //  pick();
               // fetchPc();
                getPcWZ();
                break;
            case R.id.bt_test2:
                fetchPc();
                ShowToast("bt_test2");
                break;
        }

    }

    private void fetchPc() {

        PickModel model = new PickModel();
        PcInfo info = new PcInfo("4903592331","01010101");
        ArrayList<PcInfo> list = new ArrayList<>();
        list.add(info);
        /*Map<String, Object> hashMapFechPc = model.getHashMapFechPc(list, null);
        model.fetchPC(hashMapFechPc, new OnNetReqFinishListener() {
            @Override
            public void OnNetReqFinish(Map<String, Object> hashMap) {

            }
        });*/

    }

    private void pick() {
        PickModel model = new PickModel();
        Map<String, Object> hashMapForPZH = model.getHashMapForPZH("4903592331");
        model.queryPCByCkpzh(hashMapForPZH, new OnNetReqFinishListener() {
            @Override
            public void OnNetReqFinish(Map<String, Object> hashMap) {

            }
        });


        Map<String, Object> hashMap = model.getHashMap("4903592331,4903592331");
        model.queryPc(hashMap, new OnNetReqFinishListener() {
            @Override
            public void OnNetReqFinish(Map<String, Object> hashMap) {

            }
        });
    }

    private void storePc() {
        List<PcInfo> list = new Vector<>();
        PcInfo pcInfo = new PcInfo("1502614994", "01010101");
        list.add(pcInfo);
        boolean ignore = false;
        HashMap<String, Object> hashMap = new HashMap<>();
        String json = new Gson().toJson(list);
        hashMap.put("jsonPcInfoList", json);
        hashMap.put("rightID", "a268fc80-030f-43af-9e3c-752362122171");
        hashMap.put("ignore", ignore);
        hashMap.put("doMethod", "StoragePC");
        SoapUtil.GetWebServiceData(hashMap, new OnNetReqFinishListener() {
            @Override
            public void OnNetReqFinish(Map<String, Object> hashMap) {
                Object result = hashMap.get("result");
                Log.e("test", "storePc======"+result.toString());
                Message msg = Message.obtain();
                msg.what = 2;
                msg.obj = result;
                mHandler.sendMessage(msg);
                Log.d("test", hashMap.toString());
            }
        });
    }

    private void queryGw() {
        HashMap<String, Object> hashMap = new HashMap<>();
         hashMap.put("cfsl", "20");
         hashMap.put("gwdx", "大");
        hashMap.put("rightID", "665c2c3f-38b6-4486-9d59-f92019b71f19");
        hashMap.put("doMethod", "QueryGWList");
        SoapUtil.GetWebServiceData(hashMap, new OnNetReqFinishListener() {
            @Override
            public void OnNetReqFinish(Map<String, Object> hashMap) {
                Object result = hashMap.get("result");
                Log.d("test", "OnNetReqFinish-------->>>__>>: "+result.toString());
                Message msg = Message.obtain();
                msg.what = 1;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        });
    }


    public void getPcWZ() {
//        HashMap<String, Object> hashMap = new HashMap<>();
//        hashMap.put("pchList", "1500616392");
//        hashMap.put("rightID", "665c2c3f-38b6-4486-9d59-f92019b71f19");
//        hashMap.put("doMethod", "QueryGWList");
        /*MovePCModel model = new MovePCModel();
        Map<String, Object> hashMapForGetPcWZ = model.getHashMapForMovePC("1500616392","01010101","true");
        model.MovePC(hashMapForGetPcWZ, new OnNetReqFinishListener() {
            @Override
            public void OnNetReqFinish(final Map<String, Object> hashMap) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mTvText1.setText(hashMap.get("result").toString());
                    }
                });
                Log.d("test","result;;;;===>"+hashMap.get("result").toString());
            }
        });*/
    }
}



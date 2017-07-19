package com.lubin.chj.modle;

import android.util.Log;

import com.google.gson.Gson;
import com.lubin.chj.Listener.OnNetReqFinishListener;
import com.lubin.chj.bean.PcInfo;
import com.lubin.chj.bean.jsonToBean.SavePDReturn;
import com.lubin.chj.modle.MInterface.IInventoryModel;
import com.lubin.chj.utils.GlobleConfig;
import com.lubin.chj.utils.SoapUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author DaiJiCheng
 * @time 2016/9/27  17:51
 * @desc ${盘点}
 */
public class InventoryModelImpl implements IInventoryModel {
    private SavePDReturn mSavePDReturn;
    @Override
    public void savePD(Map<String, Object> map, final OnNetReqFinishListener listener) {
        SoapUtil.GetWebServiceData(map, new OnNetReqFinishListener() {
            @Override
            public void OnNetReqFinish(Map<String, Object> hashMap) {
                Log.d("test", hashMap.toString());
                Object result = hashMap.get("result");
                if (hashMap.get("result") != null) {
                    Log.d("test",hashMap.toString());
                    mSavePDReturn= new SavePDReturn().objectFromData(hashMap.get("result").toString());
                    hashMap.put("returnCode", mSavePDReturn.getReturnCode());
                    hashMap.put("returnMsg", mSavePDReturn.getReturnMsg());
                } else {
                    hashMap.put("returnCode", "9999");
                    hashMap.put("returnMsg", "网络请求失败");
                }
                listener.OnNetReqFinish(hashMap);
            }
        });
    }

    @Override
    public Map<String, Object> getHashMapForSave(List<PcInfo> list, String gwbh) {
        Map<String, Object> map = new HashMap<>();
        String s = new Gson().toJson(list);
        map.put("jsonPcInfoList", s);
        map.put("pdgwbh", gwbh);
        map.put("rightID", GlobleConfig.rightId);
        map.put("doMethod", "SavePD");
        return map;
    }
}

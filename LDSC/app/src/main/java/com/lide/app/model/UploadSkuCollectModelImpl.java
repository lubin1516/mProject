package com.lide.app.model;

import com.google.gson.Gson;
import com.lide.app.MApplication;
import com.lide.app.bean.BeanToJson.BarCodeQtyBiz;
import com.lide.app.bean.BeanToJson.RequestForAddSku;
import com.lide.app.listener.OnLoadFinishListener;
import com.lide.app.model.MInterface.IUploadSkuCollectModel;
import com.lide.app.service.DbService;
import com.lide.app.persistence.util.NetWorkUtil;
import com.lide.app.persistence.util.Utils;
import com.lubin.bean.TakeStockSkuCollect;
import com.lubin.bean.TakeStockTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lubin on 2016/10/20.
 */

public class UploadSkuCollectModelImpl implements IUploadSkuCollectModel {
    private DbService db;
    private String apiUrl = Utils.apiUrl;
    List<TakeStockSkuCollect> takeStockSkuCollects;


    public UploadSkuCollectModelImpl() {
        db = DbService.getInstance(MApplication.getInstance());
    }

    /**
     * 上传sku明细
     * @return
     */
    @Override
    public Map<String, String> getMapForUploadSkuByTaskCode() {
        Map<String, String> map = new HashMap<>();
        TakeStockTask currentTask = Utils.getCurrentTask();

        String doMethod = "api/takeStockOrder/" + currentTask.getTakeStockOrder().getTakeStockId() +
                "/task/" + currentTask.getCodeId() + "/skuTag/append";
        String postUrl = apiUrl + doMethod;
        String apiKey = Utils.getApiKey();

        RequestForAddSku requestForAddSku = new RequestForAddSku();
        requestForAddSku.barcodes = new ArrayList<>();
        takeStockSkuCollects = db.querySkuCollectByTaskCode(Utils.getCurrentTask().getId());

        for (TakeStockSkuCollect takeStockSkuCollect : takeStockSkuCollects) {
            BarCodeQtyBiz barCodeQtyBiz = new BarCodeQtyBiz();
            barCodeQtyBiz.barcode = takeStockSkuCollect.getBarcode();
            barCodeQtyBiz.qty = takeStockSkuCollect.getNum();
            requestForAddSku.barcodes.add(barCodeQtyBiz);
        }
        String requestJsonData = new Gson().toJson(requestForAddSku);
        map.put("apiKey", apiKey);
        map.put("postUrl", postUrl);
        map.put("requestJsonData", requestJsonData);
        return map;
    }

    @Override
    public void upLoadSkuCollect(Map<String, String> map, final OnLoadFinishListener listener) {
        NetWorkUtil.setListener(new OnLoadFinishListener() {
            @Override
            public void OnLoadFinish(Map<String, String> map) {
                listener.OnLoadFinish(map);
            }
        });
        NetWorkUtil.StartNetWork(map);
    }

    public void changeSkuCollectStatus() {
        for (TakeStockSkuCollect takeStockSkuCollect : takeStockSkuCollects) {
            takeStockSkuCollect.setIsUpload(true);
            Utils.getCurrentTask().setComplete(true);
            Utils.getCurrentTask().update();
        }
    }
}

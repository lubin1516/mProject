package com.lide.app.presenter;

import android.extend.util.service.VersionUtils;
import android.util.Log;

import com.google.gson.internal.LinkedTreeMap;
import com.lide.app.MApplication;
import com.lide.app.bean.JsonToBean.VersionUrl;
import com.lide.app.listener.OnLoadFinishListener;
import com.lide.app.model.VersionModel;
import com.lide.app.persistence.util.FormatUtils;
import com.lide.app.ui.VInterface.IDataFragmentView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by lubin on 2016/12/21.
 */

public class VersionPresenter {

    private final VersionModel versionModel;
    IDataFragmentView view;

    public VersionPresenter(IDataFragmentView view) {
        this.view = view;
        versionModel = new VersionModel();
    }

    public void queryVersionJsonUrl() {
        view.startProgressDialog("检查是否有新版本~");
        try {
            //获取最新版本的路径
            versionModel.queryVersionUrl(new OnLoadFinishListener() {
                @Override
                public void OnLoadFinish(Map<String, String> map) {
                    if (map.get("statusCode").equals("200")) {
                        try {
                            VersionUrl result = FormatUtils.resultToBean(map.get("result"), VersionUrl.class);
                            String jsonUrl = result.androidAppUpdateUrl1.getValue1();
                            if (jsonUrl.equals("")) {
                                view.stopProgressDialog(null);
                                view.ShowToast("最新版本~");
                                return;
                            }
                            //获取最新版本
                            versionModel.queryVersion(jsonUrl, new OnLoadFinishListener() {
                                @Override
                                public void OnLoadFinish(Map<String, String> map) {
                                    if (map.get("statusCode").equals("200")) {
                                        try {
                                            view.stopProgressDialog(null);
                                            JSONObject object = new JSONObject(map.get("result"));
                                            String url = object.getString("url");
                                            String newVersion = object.getString("version");
                                            VersionUtils.Version oleVersion = VersionUtils.getAppVersionName(MApplication.getInstance());
                                            try {
                                                int i = VersionUtils.compareVersion(newVersion, oleVersion.versionName);
                                                if (i > 0) {
                                                    List<LinkedTreeMap> linkedTreeMaps = new ArrayList<>();
                                                    LinkedTreeMap treeMap = new LinkedTreeMap<>();
                                                    treeMap.put("url", url);
                                                    linkedTreeMaps.add(treeMap);
                                                    view.ShowData(linkedTreeMaps);
                                                } else if (i <= 0) {
                                                    view.ShowToast("最新版本~");
                                                }
                                            } catch (Exception e) {
                                                view.showDialog(e.getMessage());
                                            }
                                        } catch (JSONException e) {
                                            view.showDialog(e.getMessage());
                                        }
                                    } else {
                                        view.ShowToast("最新版本~");
                                    }
                                }
                            });
                        } catch (Exception e) {
                            view.showDialog(map.get("result"));
                        }
                    } else {
                        view.showDialog(map.get("result"));
                    }
                }
            });
        } catch (Exception e) {
            Log.d("versionException", e.getMessage());
        }
    }

}

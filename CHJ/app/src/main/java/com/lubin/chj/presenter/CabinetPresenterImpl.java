package com.lubin.chj.presenter;

import com.lubin.chj.Listener.OnNetReqFinishListener;
import com.lubin.chj.bean.Light;
import com.lubin.chj.modle.CabinetMoldelImpl;
import com.lubin.chj.modle.MInterface.ICabinetModel;
import com.lubin.chj.modle.MInterface.ISetLightModel;
import com.lubin.chj.modle.SetLightModelImpl;
import com.lubin.chj.presenter.IPresenter.ICabinetPresenter;
import com.lubin.chj.view.activity.VInterface.ICabinetView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by lubin on 2016/9/22.
 */
public class CabinetPresenterImpl implements ICabinetPresenter {
    private ICabinetView view;
    private ICabinetModel cabinetModel;
    private ISetLightModel setLightModel;

    public CabinetPresenterImpl(ICabinetView view) {
        this.view = view;
        this.cabinetModel = new CabinetMoldelImpl();
        this.setLightModel = new SetLightModelImpl();
    }

    @Override
    public void QueryCabinet(String key) {
        cabinetModel.queryCarbinet(cabinetModel.getHashMap(key), new OnNetReqFinishListener() {
            @Override
            public void OnNetReqFinish(Map<String, Object> hashMap) {
                String returnCode = hashMap.get("returnCode").toString();
                String returnMsg = hashMap.get("returnMsg").toString();
                if (returnCode.equals("0000")) {
                    view.ShowCabinets(cabinetModel.getListBean());
                } else {
                    view.ShowDialog(returnMsg);
                }
            }
        });
    }

    public void SetLight(final List<Light> lights) {
        setLightModel.setLight(setLightModel.getHashMapForLight(lights), new OnNetReqFinishListener() {
            @Override
            public void OnNetReqFinish(Map<String, Object> hashMap) {
                String returnCode = hashMap.get("returnCode").toString();
                String returnMsg = hashMap.get("returnMsg").toString();
                if (returnCode.equals("0000")) {
                    /*List<SetLightReturn.ListBean> lightListBean = setLightModel.getLightListBean();
                    StringBuilder sb = new StringBuilder();
                    for (SetLightReturn.ListBean bean : lightListBean) {
                        if (bean.getRCode().equals("0003")) {
                            sb.append(bean.getGwbh() + ":" + bean.getRMsg());
                        }
                    }
                    if (!TextUtils.isEmpty(sb.toString()))
                        view.ShowDialog(sb.toString());*/
                    if (lights.get(0).isOpen) {
                        view.ShowToast("柜位" + ":" + lights.get(0).gwbh + "亮灯成功！");
                    } else {
                        view.ShowToast("柜位" + ":" + lights.get(0).gwbh + "灭灯成功！");
                    }
                    view.changeBtnStatus();
                } else {
                    view.ShowToast("柜位" + ":" + lights.get(0).gwbh + returnMsg);
                }
            }
        });
    }

    @Override
    public void setColor() {
        setLightModel.setColor(setLightModel.getColor(), new OnNetReqFinishListener() {
            @Override
            public void OnNetReqFinish(Map<String, Object> hashMap) {
                String returnCode = hashMap.get("returnCode").toString();
                String returnMsg = hashMap.get("returnMsg").toString();
                if (returnCode.equals("0000")) {
                    List<String> list = new ArrayList<String>();
                    list.add(returnMsg);
                    view.ShowCabinets(list);
                } else {
                    view.ShowToast(returnMsg);
                }
            }
        });
    }

    @Override
    public void setQueryGWList(int cfsl, String gwdx) {
        cabinetModel.QueryGWList(cfsl, gwdx, new OnNetReqFinishListener() {
            @Override
            public void OnNetReqFinish(Map<String, Object> hashMap) {
                String returnCode = hashMap.get("returnCode").toString();
                String returnMsg = hashMap.get("returnMsg").toString();
                if (returnCode.equals("0000")) {
                    view.ShowCabinets(cabinetModel.getListBean());
                } else {
                    view.ShowDialog(returnMsg);
                }
            }
        });
    }
}

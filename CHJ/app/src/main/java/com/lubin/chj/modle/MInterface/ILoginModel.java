package com.lubin.chj.modle.MInterface;

import com.lubin.chj.Listener.OnLoginFinshListener;
import com.lubin.chj.bean.User;

/**
 * @author DaiJiCheng
 * @time 2016/9/29  9:36
 * @desc ${TODD}
 */
public interface ILoginModel {
    void login(User user, String serialNumber, OnLoginFinshListener listener);
}

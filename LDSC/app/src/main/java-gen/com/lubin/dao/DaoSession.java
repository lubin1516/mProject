package com.lubin.dao;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.lubin.bean.User;
import com.lubin.bean.TakeStockOrder;
import com.lubin.bean.TakeStockTask;
import com.lubin.bean.TakeStockEpcCollect;
import com.lubin.bean.TakeStockSkuCollect;
import com.lubin.bean.InBoundOrder;
import com.lubin.bean.InBoundCase;
import com.lubin.bean.InBoundDetail;
import com.lubin.bean.InBoundOperate;
import com.lubin.bean.OutBoundOrder;
import com.lubin.bean.OutBoundDetail;
import com.lubin.bean.OutBoundOperate;

import com.lubin.dao.UserDao;
import com.lubin.dao.TakeStockOrderDao;
import com.lubin.dao.TakeStockTaskDao;
import com.lubin.dao.TakeStockEpcCollectDao;
import com.lubin.dao.TakeStockSkuCollectDao;
import com.lubin.dao.InBoundOrderDao;
import com.lubin.dao.InBoundCaseDao;
import com.lubin.dao.InBoundDetailDao;
import com.lubin.dao.InBoundOperateDao;
import com.lubin.dao.OutBoundOrderDao;
import com.lubin.dao.OutBoundDetailDao;
import com.lubin.dao.OutBoundOperateDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig userDaoConfig;
    private final DaoConfig takeStockOrderDaoConfig;
    private final DaoConfig takeStockTaskDaoConfig;
    private final DaoConfig takeStockEpcCollectDaoConfig;
    private final DaoConfig takeStockSkuCollectDaoConfig;
    private final DaoConfig inBoundOrderDaoConfig;
    private final DaoConfig inBoundCaseDaoConfig;
    private final DaoConfig inBoundDetailDaoConfig;
    private final DaoConfig inBoundOperateDaoConfig;
    private final DaoConfig outBoundOrderDaoConfig;
    private final DaoConfig outBoundDetailDaoConfig;
    private final DaoConfig outBoundOperateDaoConfig;

    private final UserDao userDao;
    private final TakeStockOrderDao takeStockOrderDao;
    private final TakeStockTaskDao takeStockTaskDao;
    private final TakeStockEpcCollectDao takeStockEpcCollectDao;
    private final TakeStockSkuCollectDao takeStockSkuCollectDao;
    private final InBoundOrderDao inBoundOrderDao;
    private final InBoundCaseDao inBoundCaseDao;
    private final InBoundDetailDao inBoundDetailDao;
    private final InBoundOperateDao inBoundOperateDao;
    private final OutBoundOrderDao outBoundOrderDao;
    private final OutBoundDetailDao outBoundDetailDao;
    private final OutBoundOperateDao outBoundOperateDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        userDaoConfig = daoConfigMap.get(UserDao.class).clone();
        userDaoConfig.initIdentityScope(type);

        takeStockOrderDaoConfig = daoConfigMap.get(TakeStockOrderDao.class).clone();
        takeStockOrderDaoConfig.initIdentityScope(type);

        takeStockTaskDaoConfig = daoConfigMap.get(TakeStockTaskDao.class).clone();
        takeStockTaskDaoConfig.initIdentityScope(type);

        takeStockEpcCollectDaoConfig = daoConfigMap.get(TakeStockEpcCollectDao.class).clone();
        takeStockEpcCollectDaoConfig.initIdentityScope(type);

        takeStockSkuCollectDaoConfig = daoConfigMap.get(TakeStockSkuCollectDao.class).clone();
        takeStockSkuCollectDaoConfig.initIdentityScope(type);

        inBoundOrderDaoConfig = daoConfigMap.get(InBoundOrderDao.class).clone();
        inBoundOrderDaoConfig.initIdentityScope(type);

        inBoundCaseDaoConfig = daoConfigMap.get(InBoundCaseDao.class).clone();
        inBoundCaseDaoConfig.initIdentityScope(type);

        inBoundDetailDaoConfig = daoConfigMap.get(InBoundDetailDao.class).clone();
        inBoundDetailDaoConfig.initIdentityScope(type);

        inBoundOperateDaoConfig = daoConfigMap.get(InBoundOperateDao.class).clone();
        inBoundOperateDaoConfig.initIdentityScope(type);

        outBoundOrderDaoConfig = daoConfigMap.get(OutBoundOrderDao.class).clone();
        outBoundOrderDaoConfig.initIdentityScope(type);

        outBoundDetailDaoConfig = daoConfigMap.get(OutBoundDetailDao.class).clone();
        outBoundDetailDaoConfig.initIdentityScope(type);

        outBoundOperateDaoConfig = daoConfigMap.get(OutBoundOperateDao.class).clone();
        outBoundOperateDaoConfig.initIdentityScope(type);

        userDao = new UserDao(userDaoConfig, this);
        takeStockOrderDao = new TakeStockOrderDao(takeStockOrderDaoConfig, this);
        takeStockTaskDao = new TakeStockTaskDao(takeStockTaskDaoConfig, this);
        takeStockEpcCollectDao = new TakeStockEpcCollectDao(takeStockEpcCollectDaoConfig, this);
        takeStockSkuCollectDao = new TakeStockSkuCollectDao(takeStockSkuCollectDaoConfig, this);
        inBoundOrderDao = new InBoundOrderDao(inBoundOrderDaoConfig, this);
        inBoundCaseDao = new InBoundCaseDao(inBoundCaseDaoConfig, this);
        inBoundDetailDao = new InBoundDetailDao(inBoundDetailDaoConfig, this);
        inBoundOperateDao = new InBoundOperateDao(inBoundOperateDaoConfig, this);
        outBoundOrderDao = new OutBoundOrderDao(outBoundOrderDaoConfig, this);
        outBoundDetailDao = new OutBoundDetailDao(outBoundDetailDaoConfig, this);
        outBoundOperateDao = new OutBoundOperateDao(outBoundOperateDaoConfig, this);

        registerDao(User.class, userDao);
        registerDao(TakeStockOrder.class, takeStockOrderDao);
        registerDao(TakeStockTask.class, takeStockTaskDao);
        registerDao(TakeStockEpcCollect.class, takeStockEpcCollectDao);
        registerDao(TakeStockSkuCollect.class, takeStockSkuCollectDao);
        registerDao(InBoundOrder.class, inBoundOrderDao);
        registerDao(InBoundCase.class, inBoundCaseDao);
        registerDao(InBoundDetail.class, inBoundDetailDao);
        registerDao(InBoundOperate.class, inBoundOperateDao);
        registerDao(OutBoundOrder.class, outBoundOrderDao);
        registerDao(OutBoundDetail.class, outBoundDetailDao);
        registerDao(OutBoundOperate.class, outBoundOperateDao);
    }
    
    public void clear() {
        userDaoConfig.clearIdentityScope();
        takeStockOrderDaoConfig.clearIdentityScope();
        takeStockTaskDaoConfig.clearIdentityScope();
        takeStockEpcCollectDaoConfig.clearIdentityScope();
        takeStockSkuCollectDaoConfig.clearIdentityScope();
        inBoundOrderDaoConfig.clearIdentityScope();
        inBoundCaseDaoConfig.clearIdentityScope();
        inBoundDetailDaoConfig.clearIdentityScope();
        inBoundOperateDaoConfig.clearIdentityScope();
        outBoundOrderDaoConfig.clearIdentityScope();
        outBoundDetailDaoConfig.clearIdentityScope();
        outBoundOperateDaoConfig.clearIdentityScope();
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public TakeStockOrderDao getTakeStockOrderDao() {
        return takeStockOrderDao;
    }

    public TakeStockTaskDao getTakeStockTaskDao() {
        return takeStockTaskDao;
    }

    public TakeStockEpcCollectDao getTakeStockEpcCollectDao() {
        return takeStockEpcCollectDao;
    }

    public TakeStockSkuCollectDao getTakeStockSkuCollectDao() {
        return takeStockSkuCollectDao;
    }

    public InBoundOrderDao getInBoundOrderDao() {
        return inBoundOrderDao;
    }

    public InBoundCaseDao getInBoundCaseDao() {
        return inBoundCaseDao;
    }

    public InBoundDetailDao getInBoundDetailDao() {
        return inBoundDetailDao;
    }

    public InBoundOperateDao getInBoundOperateDao() {
        return inBoundOperateDao;
    }

    public OutBoundOrderDao getOutBoundOrderDao() {
        return outBoundOrderDao;
    }

    public OutBoundDetailDao getOutBoundDetailDao() {
        return outBoundDetailDao;
    }

    public OutBoundOperateDao getOutBoundOperateDao() {
        return outBoundOperateDao;
    }

}
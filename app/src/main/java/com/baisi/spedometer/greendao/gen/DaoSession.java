package com.baisi.spedometer.greendao.gen;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.baisi.spedometer.step.bean.SportRecordData;
import com.baisi.spedometer.step.bean.StepData;
import com.baisi.spedometer.step.bean.StepTodayData;

import com.baisi.spedometer.greendao.gen.SportRecordDataDao;
import com.baisi.spedometer.greendao.gen.StepDataDao;
import com.baisi.spedometer.greendao.gen.StepTodayDataDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig sportRecordDataDaoConfig;
    private final DaoConfig stepDataDaoConfig;
    private final DaoConfig stepTodayDataDaoConfig;

    private final SportRecordDataDao sportRecordDataDao;
    private final StepDataDao stepDataDao;
    private final StepTodayDataDao stepTodayDataDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        sportRecordDataDaoConfig = daoConfigMap.get(SportRecordDataDao.class).clone();
        sportRecordDataDaoConfig.initIdentityScope(type);

        stepDataDaoConfig = daoConfigMap.get(StepDataDao.class).clone();
        stepDataDaoConfig.initIdentityScope(type);

        stepTodayDataDaoConfig = daoConfigMap.get(StepTodayDataDao.class).clone();
        stepTodayDataDaoConfig.initIdentityScope(type);

        sportRecordDataDao = new SportRecordDataDao(sportRecordDataDaoConfig, this);
        stepDataDao = new StepDataDao(stepDataDaoConfig, this);
        stepTodayDataDao = new StepTodayDataDao(stepTodayDataDaoConfig, this);

        registerDao(SportRecordData.class, sportRecordDataDao);
        registerDao(StepData.class, stepDataDao);
        registerDao(StepTodayData.class, stepTodayDataDao);
    }
    
    public void clear() {
        sportRecordDataDaoConfig.clearIdentityScope();
        stepDataDaoConfig.clearIdentityScope();
        stepTodayDataDaoConfig.clearIdentityScope();
    }

    public SportRecordDataDao getSportRecordDataDao() {
        return sportRecordDataDao;
    }

    public StepDataDao getStepDataDao() {
        return stepDataDao;
    }

    public StepTodayDataDao getStepTodayDataDao() {
        return stepTodayDataDao;
    }

}

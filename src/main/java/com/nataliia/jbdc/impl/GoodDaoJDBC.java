package com.nataliia.jbdc.impl;

import com.nataliia.jbdc.DbConnector;
import com.nataliia.jbdc.GoodDao;
import com.nataliia.model.Good;

public class GoodDaoJDBC extends AbstractDao<Good, Long> implements GoodDao {

    public GoodDaoJDBC(Class<Good> goodClass, DbConnector dbConnector) {
        super(goodClass, dbConnector);
    }
}

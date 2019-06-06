package com.nataliia.jbdc.impl;

import com.nataliia.model.Good;



import com.nataliia.jbdc.GoodDao;

import java.sql.Connection;

public class GoodDaoJDBC extends AbstractDao<Good, Long> implements GoodDao {

    public GoodDaoJDBC(Class<Good> goodClass, Connection connection) {
        super(goodClass, connection);
    }
}

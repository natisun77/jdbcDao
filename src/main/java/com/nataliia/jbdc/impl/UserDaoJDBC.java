package com.nataliia.jbdc.impl;


import com.nataliia.jbdc.DbConnector;
import com.nataliia.jbdc.UserDao;
import com.nataliia.model.User;

public class UserDaoJDBC extends AbstractDao<User, Long> implements UserDao {

    public UserDaoJDBC(Class<User> userClass, DbConnector dbConnector) {
        super(userClass, dbConnector);
    }
}

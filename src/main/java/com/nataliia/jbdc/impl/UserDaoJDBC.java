package com.nataliia.jbdc.impl;


import com.nataliia.jbdc.UserDao;
import com.nataliia.model.User;

import java.sql.Connection;

public class UserDaoJDBC extends AbstractDao<User, Long> implements UserDao {

    public UserDaoJDBC(Class<User> userClass, Connection connection) {
        super(userClass, connection);
    }
}

package com.nataliia.Application;

import com.nataliia.jbdc.DbConnector;
import com.nataliia.jbdc.impl.AbstractDao;
import com.nataliia.jbdc.impl.UserDaoJDBC;
import com.nataliia.model.User;

public class Application {


    public static void main(String[] args) {

        AbstractDao<User, Long> usersDao = new UserDaoJDBC(User.class, new DbConnector());
        User userOne = new User(1L, "nata", "@", "1111");
        User userTwo = new User(2L, "alex", "@", "1111");
        User userThree = new User(3L, "alex", "@", "1111");
        usersDao.save(userOne);
        usersDao.save(userTwo);
        usersDao.save(userThree);
        System.out.println(usersDao.get(1L));
        System.out.println(usersDao.getAll());
        User userThree1 = new User(3L, "alex11", "@", "1111");
        usersDao.update(userThree1);
    }
}

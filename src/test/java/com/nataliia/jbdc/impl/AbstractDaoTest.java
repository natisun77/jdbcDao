package com.nataliia.jbdc.impl;

import com.nataliia.jbdc.DbConnector;
import com.nataliia.model.User;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AbstractDaoTest {
    private AbstractDao<User, Long> usersDao = new UserDaoJDBC(User.class, new DbConnector());

    @Test
    public void saveAndGet() {
        User user = new User(15L, "nata", "@", "1111");
        usersDao.save(user);
        assertEquals(user, usersDao.get(15L));
    }

    @Test
    public void update() {
        User user = new User(18L, "alex", "@", "1111");
        usersDao.save(user);
        User user1 = new User(18L, "alex", "@1111", "2222");
        usersDao.update(user1);
        assertEquals(user1, usersDao.get(18L));
    }

    @Test
    public void getAll() {
        User user = new User(20L, "alex", "@", "1111");
        User user1 = new User(21L, "alex", "@", "1111");
        usersDao.save(user);
        usersDao.save(user1);
        List<User> list = usersDao.getAll();
        assertTrue(list.contains(user));
        assertTrue(list.contains(user1));
    }

    @Test
    public void deleteAndGet() {
        usersDao.delete(16L);
        User user = new User(16L, "nata", "@", "1111");
        usersDao.save(user);
        assertEquals(user, usersDao.get(16L));
    }
}
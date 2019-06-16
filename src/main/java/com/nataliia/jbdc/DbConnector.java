package com.nataliia.jbdc;

import com.nataliia.jbdc.impl.AbstractDao;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnector {

    private static final String dbUr1 = "jdbc:mysql://localhost:3306/abstract_dao?serverTimezone=UTC";
    private static final String name = "root";
    private static final String pass = "1111";
    private static final Logger LOGGER = Logger.getLogger(DbConnector.class);

    public static Connection connect() {
        Connection connection;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(dbUr1, name, pass);
            LOGGER.debug("Request for connection: " + dbUr1 + ", " + name);
            return connection;
        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.error("Can't make connection, sql error", e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            LOGGER.error("Can't make connection, class is not found", e);
        }
        return null;
    }

}

package com.nataliia.jbdc.impl;

import com.nataliia.jbdc.DbConnector;
import com.nataliia.jbdc.GenericDao;
import org.apache.log4j.Logger;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDao<T, ID> implements GenericDao<T, ID> {

    private static final Logger LOGGER = Logger.getLogger(AbstractDao.class);
    private final Connection connection;
    private Class<T> tClass;

    protected AbstractDao(Class<T> tClass, DbConnector dbConnector) {
        this.tClass = tClass;
        connection = dbConnector.connect();
    }

    @Override
    public T save(T t) {
        if (t != null) {
            RequestHelper<T> request = new RequestHelper<>(tClass);
            String sql = request.getQueryForSave();
            LOGGER.debug(sql);
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                Object[] params = request.getBindValues(t);
                setStatementParams(preparedStatement, params).executeUpdate();
            } catch (SQLException | IllegalAccessException e) {
                LOGGER.error("Can't add the object", e);
                throw new RuntimeException("Can't add the object", e);
            }
        }
        return t;
    }

    private PreparedStatement setStatementParams(PreparedStatement preparedStatement, Object[] params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            preparedStatement.setObject(i + 1, params[i]);
        }
        return preparedStatement;
    }

    @Override
    public T update(T t) {
        if (t != null) {
            RequestHelper<T> request = new RequestHelper<>(tClass);
            Field[] fields = getFields();
            Object id = null;
            for (int i = 0; i < fields.length; i++) {
                if ("id".equals(fields[i].getName().toLowerCase())) {
                    try {
                        id = fields[i].get(t);
                    } catch (IllegalAccessException e) {
                        LOGGER.error("Can't get access to update the object", e);
                    }
                }
            }
            if (id == null) {
                throw new RuntimeException();
            }
            String sql = request.getQueryForUpdate();
            try {
                LOGGER.debug(sql);
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                Object[] params = request.getBindValues(t);
                Object[] paramsWithId = new Object[params.length + 1];
                System.arraycopy(params, 0, paramsWithId, 0, params.length);
                paramsWithId[params.length] = id;
                setStatementParams(preparedStatement, paramsWithId).executeUpdate();
            } catch (SQLException | IllegalAccessException e) {
                LOGGER.error("Can't update the object with ID=" + id, e);
                throw new RuntimeException("Can't update the object", e);
            }
        }
        return t;
    }

    @Override
    public List<T> getAll() {
        List<T> result = new ArrayList<>();
        try {
            RequestHelper<T> requestHelper = new RequestHelper<>(tClass);
            Statement statement = connection.createStatement();
            String sql = requestHelper.getQueryToGetAll();
            LOGGER.debug(sql);
            ResultSet resultSet = statement.executeQuery(sql);
            Constructor<T> emptyArgsConstructor = tClass.getDeclaredConstructor();
            Field[] declaredFields = getFields();
            while (resultSet.next()) {
                T t = emptyArgsConstructor.newInstance();
                for (int i = 0; i < declaredFields.length; i++) {
                    declaredFields[i].set(t, resultSet.getObject(i + 1));
                }
                result.add(t);
            }
        } catch (SQLException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            LOGGER.error("Can't get result of Objects from DB", e);
        }
        return result;
    }

    @Override
    public T get(ID id) {
        try {
            RequestHelper<T> request = new RequestHelper<>(tClass);
            String sql = request.getQueryToFind();
            LOGGER.debug(sql);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setObject(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Constructor<T> emptyArgsConstructor = tClass.getDeclaredConstructor();
                T t = emptyArgsConstructor.newInstance();
                Field[] declaredFields = getFields();
                for (int i = 0; i < declaredFields.length; i++) {
                    declaredFields[i].set(t, resultSet.getObject(i + 1));
                }
                return t;
            }
        } catch (SQLException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            LOGGER.error("Can't get object with ID " + id + " from DB.", e);

        }
        throw new RuntimeException("Can't find the object");
    }

    @Override
    public void delete(ID id) {
        try {
            RequestHelper<T> request = new RequestHelper<>(tClass);
            String sql = request.getQueryToDelete();
            LOGGER.debug(sql);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setObject(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("Can't delete object with ID " + id + " from DB.", e);
        }
    }

    private Field[] getFields() {
        Field[] fields = tClass.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
        }
        return fields;
    }
}

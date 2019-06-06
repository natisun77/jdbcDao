package com.nataliia.jbdc.impl;

import java.lang.reflect.Field;

public class RequestHelper<T> {
    private Class<T> tClass;

    public RequestHelper(Class<T> tClass) {
        this.tClass = tClass;
    }

    public String getQueryForSave() {
        return "INSERT INTO " + getTableName() + "(" + getColumnsNames(",")
                + ") VALUES ( " + getQuestionMarks() + ")";
    }

    public String getQueryForUpdate() {
        return "UPDATE " + getTableName() + " SET " + getColumnsNames(" = ?, ") + " =? WHERE ID = ?";
    }

    public String getQueryToGetAll() {
        return "SELECT " + getColumnsNames(",") + " FROM " + getTableName();
    }

    public String getQueryToFind() {
        return "SELECT * FROM " + getTableName() + " WHERE ID = ";
    }

    public String getQueryToDelete() {
        return "DELETE FROM " + getTableName() + " WHERE ID = ?";
    }

    private String getTableName() {
        return tClass.getSimpleName().toLowerCase();
    }

    private String getColumnsNames(String prefix) {
        Field[] fields = getFields();

        StringBuilder columns = new StringBuilder();
        for (int i = 0; i < fields.length; i++) {
            columns.append(fields[i].getName().toLowerCase());
            if (i != fields.length - 1) {
                columns.append(prefix);
            }
        }
        return columns.toString();
    }

    private String getQuestionMarks() {
        Field[] fields = getFields();

        StringBuilder questionMarks = new StringBuilder();
        String prefix = ",";
        for (int i = 0; i < fields.length; i++) {
            questionMarks.append("?");
            if (i != fields.length - 1) {
                questionMarks.append(prefix);
            }
        }
        return questionMarks.toString();
    }

    public Object[] getBindValues(T t) throws IllegalAccessException {
        Field[] fields = getFields();
        Object[] values = new Object[fields.length];
        for (int i = 0; i < fields.length; i++) {
            values[i] = fields[i].get(t);
        }
        return values;
    }

    private Field[] getFields() {
        Field[] fields = tClass.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
        }
        return fields;
    }

}

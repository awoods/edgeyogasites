/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 *     http://duracloud.org/license/
 */
package com.edge.media.service.util;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;

/**
 * @author Andrew Woods
 *         Date: 2/4/12
 */
public class DbUtil {

    private static final String TABLE = "useids";
    private static final int MEMID_INDEX = 2;
    private static final int DATETIME_INDEX = 3;

    private Connection connection = null;
    private Statement statement = null;


    public boolean notUnique(int id) throws Exception {
        ResultSet rs = getStatement().executeQuery(
            "SELECT * FROM " + TABLE + " WHERE GUID=" + id);
        return rs.next();
    }

    public void storeId(int id) throws Exception {
        int empty = -1;
        String dateTime = new DateUtil().currentDateTime();
        getStatement().executeUpdate(
            "INSERT INTO " + TABLE + " VALUES (" + id + "," + empty + ",'" +
                dateTime + "')");
    }

    public void verifyNotExpired(int guid, int memid, int daysValid)
        throws Exception {
        boolean expired = true;
        ResultSet resultSet = findById(guid);
        if (resultSet.next()) {
            String dateString = resultSet.getString(DATETIME_INDEX);
            Calendar date = new DateUtil().fromString(dateString);
            date.roll(Calendar.DAY_OF_MONTH, daysValid);

            // update database to set member making call
            resultSet.updateInt(MEMID_INDEX, memid);
            resultSet.updateRow();

            expired = System.currentTimeMillis() > date.getTimeInMillis();

        } else {
            // Error if no stream found in DB
            StringBuilder err = new StringBuilder("No stream found. ");
            err.append("memid=");
            err.append(memid);
            err.append(", ");
            err.append("guid=");
            err.append(guid);
            throw new Exception(err.toString());
        }

        if (expired) {
            // Error if date has expired
            StringBuilder err = new StringBuilder("Stream has expired. ");
            err.append("It is valid for ");
            err.append(daysValid);
            err.append(daysValid == 1 ? " day. " : " days. ");
            err.append("memid=");
            err.append(memid);
            err.append(", ");
            err.append("guid=");
            err.append(guid);
            throw new Exception(err.toString());
        }
    }

    private ResultSet findById(int id) throws Exception {
        return getStatement().executeQuery(
            "SELECT * FROM " + TABLE + " WHERE GUID=" + id);
    }

    private Connection getConnection() throws Exception {
        if (null == connection) {
            DataSource dataSource = null;
            try {
                Context context = new InitialContext();
                Context env = (Context) context.lookup("java:comp/env");
                dataSource = (DataSource) env.lookup("jdbc/edge_db");

            } catch (NamingException e) {
                throw new Exception(
                    "Unable to load JDBC driver: " + e.getMessage());
            }

            if (dataSource != null) {
                try {
                    connection = dataSource.getConnection();

                } catch (SQLException e) {
                    throw new Exception(
                        "Unable to get connection: " + e.getMessage());
                }
            }
        }
        return connection;
    }

    private Statement getStatement() throws Exception {
        if (null == statement) {
            statement =
                getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                ResultSet.CONCUR_UPDATABLE);
        }
        return statement;
    }
}

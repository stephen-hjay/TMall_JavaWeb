package com.hjay.tmall.Utils;


import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * A Utils class for DB connection
 */

public class DBUtils {
    // through properties file load db connection parameters into the system
    static String ip;
    static int port;
    static String database;
    static String encoding_format;
    static String loginName;
    static String password;
    static String driverClassName;

    static {
        // step 1 : load properties Document
        Properties properties = new Properties();

        InputStream in = null;
        try {
            // properties file must be placed in the classes file in output
            in = Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties");
            properties.load(in);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ip = properties.getProperty("ip","127.0.0.1");
        port = Integer.parseInt(properties.getProperty("port","3306"));
        database = properties.getProperty("database");
        encoding_format = properties.getProperty("encoding_format");
        loginName = properties.getProperty("loginName");
        password = properties.getProperty("password");
        driverClassName = properties.getProperty("driverClassName");
    }


    static {
        try {
            // it needs to import the mysql driver via maven pom.xml
            Class.forName(driverClassName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        // it needs to import the mysql driver via maven pom.xml
        // database url: jdbc:mysql://ip:port/database?characterEncoding=UTF-8
        String url = String.format("jdbc:mysql://%s:%d/%s?characterEncoding=%s", ip, port, database, encoding_format);
        // for each connection, we need to init a new connection
        return DriverManager.getConnection(url, loginName, password);
    }


    // for test only
    public static void main(String[] args) throws SQLException {
        System.out.println(getConnection());

    }



}






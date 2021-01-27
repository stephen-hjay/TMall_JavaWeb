package com.hjay.tmall.Utils;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DBConnPoolUtils {
    private static DataSource dataSource;

    static {
        // load properties
        Properties properties = new Properties();
        InputStream in = DBConnPoolUtils.class.getClassLoader().getResourceAsStream("druid.properties");
        try {
            properties.load(in);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // use DruidDataSourceFactory to create dataSource
        // the properties argument needs to be in the standard way:https://www.cnblogs.com/wuyun-blog/p/5679073.html
        try {
            dataSource = DruidDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * get datasource
     */

    public static DataSource getDataSource(){
        return dataSource;
    }

    /**
     * get connection from connection pool
     *
     */
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
    /**
     * for
     */

    public static void free(ResultSet rs, Statement st, Connection conn) {
        try {
            if (rs != null)
                rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (st != null)
                    st.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if (conn != null)
                    try {
                        conn.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            }
        }
    }


        public static void main(String[] args) {
        try {
            System.out.println(DBConnPoolUtils.getConnection());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

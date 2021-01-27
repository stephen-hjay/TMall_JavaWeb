package com.hjay.tmall.DAO.Implement;

import com.hjay.tmall.DAO.DAO;
import com.hjay.tmall.Utils.DBConnPoolUtils;
import com.hjay.tmall.Entity.Bean;
import com.hjay.tmall.Entity.Implement.User;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserDAOImproved implements DAO {
    private JdbcTemplate template = new JdbcTemplate(DBConnPoolUtils.getDataSource());
    public int getTotal(){
        String sql = "select count(*) from user";
        Integer result = (Integer) template.queryForObject(sql,Integer.class);
        return result;
    }

    public boolean add(Bean bean_father) {
        User bean = (User) bean_father;
        String sql = "insert into user values(null ,? ,?)";
        Object args[] = {bean.getName(),bean.getPassword()};
        int temp = template.update(sql, args);
        if (temp > 0) {
            System.out.println("Add Success");
        }else{
            System.out.println("Add Fail");
        }
        return temp>0;
    }

    public boolean update(Bean bean_father) {
        User bean = (User) bean_father;
        String sql = "update user set name= ? , password = ? where id = ? ";
        Object args[] = new Object[]{bean.getName(),bean.getPassword(),bean.getId()};
        int temp = template.update(sql,args);
        if (temp > 0) {
            System.out.println("Update Success");
        }else {
            System.out.println("Update Fail");
        }
        return temp>0;

    }

    public boolean delete(int id) {
        String sql = "delete from user where id = ?";
        Object args[] = new Object[]{id};
        int temp = template.update(sql,args);
        if (temp > 0) {
            System.out.println("Delete Success");
        }else {
            System.out.println("Delete Fail");
        }
        return temp>0;
    }

    public User getById(int id) {
        String sql = "select * from user where id = ?";
        Object args[] = new Object[]{id};
        Object user = template.queryForObject(sql,args,new BeanPropertyRowMapper(User.class));
        return (User) user;
    }

    public List<User> list() throws SQLException {
        return list(0, Short.MAX_VALUE);
    }

    public List<User> list(int start, int count) throws SQLException {
        Connection connection = null;
        connection = DBConnPoolUtils.getConnection();
        String sql = "select * from user order by id desc limit ?,? ";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1,start);
        preparedStatement.setInt(2,count);
        List<User> resultBeans  = template.query(preparedStatement.toString(), new RowMapper() {
            @Override
            public Bean mapRow(ResultSet resultSet, int i) throws SQLException {
                User bean = new User();
                int id = resultSet.getInt(1);
                String name = resultSet.getString("name");
                bean.setName(name);
                String password = resultSet.getString("password");
                bean.setPassword(password);
                bean.setId(id);
                return bean;
            }
        });
        DBConnPoolUtils.free(null,preparedStatement,connection);
        return resultBeans;
    }

    // for user isExist
    public boolean isExist(String name) {
        User user = getByUsrName(name);
        return user!=null;
    }


    public User getByUsrName(String name) {
        User bean = null;
        String sql = "select * from User where name = ?";
        try (Connection c = DBConnPoolUtils.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, name);
            ResultSet rs =ps.executeQuery();
            if (rs.next()) {
                bean = new User();
                int id = rs.getInt("id");
                bean.setName(name);
                String password = rs.getString("password");
                bean.setPassword(password);
                bean.setId(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bean;
    }

    // for login
    public User getByUsrName(String name, String password) {
        User bean = null;

        String sql = "select * from User where name = ? and password=?";
        try (Connection c = DBConnPoolUtils.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, password);
            ResultSet rs =ps.executeQuery();

            if (rs.next()) {
                bean = new User();
                int id = rs.getInt("id");
                bean.setName(name);
                bean.setPassword(password);
                bean.setId(id);
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }
        return bean;
    }

}
package com.hjay.tmall.DAO.Implement;

import com.hjay.tmall.DAO.DAO;
import com.hjay.tmall.Utils.DBConnPoolUtils;
import com.hjay.tmall.Utils.DBUtils;
import com.hjay.tmall.Entity.Bean;
import com.hjay.tmall.Entity.Implement.Category;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;



public class CategoryDAO implements DAO {

    public int getTotal() {
        int total = 0;
//        try (Connection c = DBConnPoolUtils.getConnection(); Statement s = c.createStatement();) {
        try (Connection c = DBConnPoolUtils.getConnection(); Statement s = c.createStatement();) {

            String sql = "select count(*) from category";

            ResultSet rs = s.executeQuery(sql);
            while (rs.next()) {
                total = rs.getInt(1);
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }
        return total;
    }

    public boolean add(Bean bean_father) {
        // 强制转换也只是转换了一个引用的类型
        Category bean = (Category) bean_father;
        String sql = "insert into category values(null,?)";

        boolean result = false;

        // You need to specify Statement.RETURN_GENERATED_KEYS to Statement.executeUpdate(), Statement.executeLargeUpdate() or Connection.prepareStatement().
        try (Connection c = DBConnPoolUtils.getConnection(); PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {
            ps.setString(1, bean.getName());
            result = ps.execute();

            //  通常我们在应用中对mysql执行了insert操作后，
            //  需要获取插入记录的自增主键，这时候通常用getGeneratedKeys()方法获取主键
            // 获取自增主键
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                bean.setId(id);
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }
        return result;
    }

    public boolean update(Bean bean_father) {
        Category bean = (Category) bean_father;
        String sql = "update category set name= ? where id = ?";
        boolean result = false;
        try (Connection c = DBConnPoolUtils.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {

            ps.setString(1, bean.getName());
            ps.setInt(2, bean.getId());

            result = ps.execute();

        } catch (SQLException e) {

            e.printStackTrace();
        }
        return result;
    }

    public boolean delete(int id) {
        boolean result = false;
        try (Connection c = DBConnPoolUtils.getConnection(); Statement s = c.createStatement();) {

            String sql = "delete from category where id = " + id;

            result = s.execute(sql);

        } catch (SQLException e) {

            e.printStackTrace();
        }
        return result;
    }

    public Category getById(int id) {
        Category bean = null;

        try (Connection c = DBConnPoolUtils.getConnection(); Statement s = c.createStatement();) {

            String sql = "select * from category where id = " + id;

            ResultSet rs = s.executeQuery(sql);

            if (rs.next()) {
                bean = new Category();
                String name = rs.getString(2);
                bean.setName(name);
                bean.setId(id);
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }
        return bean;
    }

    public List<Category> list() {
        return list(0, Short.MAX_VALUE);
    }

    public List<Category> list(int start, int count) {
        List<Category> beans = new ArrayList<Category>();

        String sql = "select * from category order by id desc limit ?,? ";


        try (Connection c = DBConnPoolUtils.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {

            ps.setInt(1, start);
            ps.setInt(2, count);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Category bean = new Category();
                int id = rs.getInt(1);
                String name = rs.getString(2);
                bean.setId(id);
                bean.setName(name);
                beans.add(bean);
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }
        return beans;
    }

}


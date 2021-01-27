package com.hjay.tmall.DAO.Implement;

import com.hjay.tmall.DAO.DAO;
import com.hjay.tmall.Utils.DBConnPoolUtils;
import com.hjay.tmall.Utils.DateFormatUtils;
import com.hjay.tmall.Entity.Bean;
import com.hjay.tmall.Entity.Implement.Product;
import com.hjay.tmall.Entity.Implement.Review;
import com.hjay.tmall.Entity.Implement.User;
import java.util.Date;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReviewDAO implements DAO {

    public int getTotal() {
        int total = 0;
        try (Connection c = DBConnPoolUtils.getConnection(); Statement s = c.createStatement();) {

            String sql = "select count(*) from Review";

            ResultSet rs = s.executeQuery(sql);
            while (rs.next()) {
                total = rs.getInt(1);
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }
        return total;
    }
    public int getTotal(int pid) {
        int total = 0;
        try (Connection c = DBConnPoolUtils.getConnection(); Statement s = c.createStatement();) {

            String sql = "select count(*) from Review where pid = " + pid;

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
        Review bean = (Review) bean_father;
        String sql = "insert into Review values(null,?,?,?,?)";
        boolean result = false;
        try (Connection c = DBConnPoolUtils.getConnection(); PreparedStatement ps = c.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);) {

            ps.setString(1, bean.getContent());
            ps.setInt(2, bean.getUser().getId());
            ps.setInt(3, bean.getProduct().getId());
            // it needs the dateformat utils to transform date to timestamp
            ps.setTimestamp(4, DateFormatUtils.date2timestamp(bean.getCreateDate()));
            result = ps.execute();

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
        Review bean = (Review) bean_father;
        String sql = "update Review set content= ?, uid=?, pid=? , createDate = ? where id = ?";
        boolean result = false;
        try (Connection c = DBConnPoolUtils.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {
            ps.setString(1, bean.getContent());
            ps.setInt(2, bean.getUser().getId());
            ps.setInt(3, bean.getProduct().getId());
            ps.setTimestamp(4, DateFormatUtils.date2timestamp( bean.getCreateDate()));
            ps.setInt(5, bean.getId());
            result = ps.execute();

        } catch (SQLException e) {

            e.printStackTrace();
        }
        return result;
    }

    public boolean delete(int id) {
        boolean result = false;
        try (Connection c = DBConnPoolUtils.getConnection(); Statement s = c.createStatement();) {

            String sql = "delete from Review where id = " + id;

            result = s.execute(sql);

        } catch (SQLException e) {

            e.printStackTrace();
        }
        return result;
    }
    // useless
    @Override
    public List<? extends Bean> list() throws SQLException {
        return null;
    }
    // useless
    @Override
    public List<? extends Bean> list(int start, int count) throws SQLException {
        return null;
    }

    public Review getById(int id) {
        Review bean = new Review();

        try (Connection c = DBConnPoolUtils.getConnection(); Statement s = c.createStatement();) {

            String sql = "select * from Review where id = " + id;

            ResultSet rs = s.executeQuery(sql);

            if (rs.next()) {
                int pid = rs.getInt("pid");
                int uid = rs.getInt("uid");

                Date createDate = DateFormatUtils.timestamp2date(rs.getTimestamp("createDate"));

                String content = rs.getString("content");

                Product product = new ProductDAO().getById(pid);
                User user = new UserDAO().getById(uid);

                bean.setContent(content);
                bean.setCreateDate(createDate);
                bean.setProduct(product);
                bean.setUser(user);
                bean.setId(id);
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }
        return bean;
    }

    public List<Review> list(int pid) {
        return list(pid, 0, Short.MAX_VALUE);
    }

    // return the count of reviews of a specific product
    public int getCount(int pid) {
        String sql = "select count(*) from Review where pid = ? ";

        try (Connection c = DBConnPoolUtils.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {

            ps.setInt(1, pid);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }
        return 0;
    }

    public List<Review> list(int pid, int start, int count) {
        List<Review> beans = new ArrayList<Review>();

        String sql = "select * from Review where pid = ? order by id desc limit ?,? ";

        try (Connection c = DBConnPoolUtils.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {

            ps.setInt(1, pid);
            ps.setInt(2, start);
            ps.setInt(3, count);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Review bean = new Review();
                int id = rs.getInt(1);

                int uid = rs.getInt("uid");
                // java.util.Date
                Date createDate = DateFormatUtils.timestamp2date(rs.getTimestamp("createDate"));

                String content = rs.getString("content");

                Product product = new ProductDAO().getById(pid);
                User user = new UserDAO().getById(uid);

                bean.setContent(content);
                bean.setCreateDate(createDate);
                bean.setId(id);
                bean.setProduct(product);
                bean.setUser(user);
                beans.add(bean);
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }
        return beans;
    }

    // search for a content
    public boolean isExist(String content, int pid) {
        String sql = "select * from Review where content = ? and pid = ?";

        try (Connection c = DBConnPoolUtils.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, content);
            ps.setInt(2, pid);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return true;
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return false;
    }

}


package com.hjay.tmall.DAO.Implement;

import com.hjay.tmall.DAO.DAO;
import com.hjay.tmall.Entity.Bean;
import com.hjay.tmall.Entity.Implement.Order;
import com.hjay.tmall.Entity.Implement.User;
import com.hjay.tmall.Utils.DBConnPoolUtils;
import com.hjay.tmall.Utils.DateFormatUtils;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderDAOImproved implements DAO {
    JdbcTemplate template = new JdbcTemplate(DBConnPoolUtils.getDataSource());


    public int getTotal() {
        String sql = "select count(*) from Order_";
        Integer total = (Integer) template.queryForObject(sql,Integer.class);

        return total;
    }


    /*
    @Override
	public int update(String sql, Object... args) throws DataAccessException {
		return update(sql, newArgPreparedStatementSetter(args));
	}
     */
    public boolean add(Bean bean_father) {
        Order bean = (Order) bean_father;
        String sql = "insert into order_ values(null,?,?,?,?,?,?,?,?,?,?,?,?)";
        Object[] args = {bean.getOrderCode(),bean.getAddress(),bean.getPost(),bean.getReceiver(),bean.getMobile(),
                bean.getUserMessage(),bean.getCreateDate(),bean.getDeliveryDate(),bean.getConfirmDate(),bean.getUser().getId(),bean.getStatus()};

        template.update(sql,args);

        boolean result = false;

        try (Connection c = DBConnPoolUtils.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {

            ps.setString(1, bean.getOrderCode());
            ps.setString(2, bean.getAddress());
            ps.setString(3, bean.getPost());
            ps.setString(4, bean.getReceiver());
            ps.setString(5, bean.getMobile());
            ps.setString(6, bean.getUserMessage());

            ps.setTimestamp(7,  DateFormatUtils.date2timestamp(bean.getCreateDate()));
            ps.setTimestamp(8,  DateFormatUtils.date2timestamp(bean.getPayDate()));
            ps.setTimestamp(9,  DateFormatUtils.date2timestamp(bean.getDeliveryDate()));
            ps.setTimestamp(10,  DateFormatUtils.date2timestamp(bean.getConfirmDate()));
            ps.setInt(11, bean.getUser().getId());
            ps.setString(12, bean.getStatus());

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
        Order bean = (Order) bean_father;
        String sql = "update order_ set address= ?, post=?, receiver=?,mobile=?,userMessage=? ,createDate = ? , payDate =? , deliveryDate =?, confirmDate = ? , orderCode =?, uid=?, status=? where id = ?";
        boolean result = false;
        try (Connection c = DBConnPoolUtils.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {

            ps.setString(1, bean.getAddress());
            ps.setString(2, bean.getPost());
            ps.setString(3, bean.getReceiver());
            ps.setString(4, bean.getMobile());
            ps.setString(5, bean.getUserMessage());
            ps.setTimestamp(6, DateFormatUtils.date2timestamp(bean.getCreateDate()));
            ps.setTimestamp(7, DateFormatUtils.date2timestamp(bean.getPayDate()));
            ps.setTimestamp(8, DateFormatUtils.date2timestamp(bean.getDeliveryDate()));
            ps.setTimestamp(9, DateFormatUtils.date2timestamp(bean.getConfirmDate()));
            ps.setString(10, bean.getOrderCode());
            ps.setInt(11, bean.getUser().getId());
            ps.setString(12, bean.getStatus());
            ps.setInt(13, bean.getId());
            result = ps.execute();

        } catch (SQLException e) {

            e.printStackTrace();
        }
        return result;

    }

    public boolean delete(int id) {
        boolean result = false;
        try (Connection c = DBConnPoolUtils.getConnection(); Statement s = c.createStatement();) {

            String sql = "delete from Order_ where id = " + id;

            result = s.execute(sql);

        } catch (SQLException e) {

            e.printStackTrace();
        }
        return result;
    }

    public Order getById(int id) {
        Order bean = new Order();
        try (Connection c = DBConnPoolUtils.getConnection(); Statement s = c.createStatement();) {

            String sql = "select * from Order_ where id = " + id;

            ResultSet rs = s.executeQuery(sql);

            if (rs.next()) {
                String orderCode =rs.getString("orderCode");
                String address = rs.getString("address");
                String post = rs.getString("post");
                String receiver = rs.getString("receiver");
                String mobile = rs.getString("mobile");
                String userMessage = rs.getString("userMessage");
                String status = rs.getString("status");
                int uid =rs.getInt("uid");
                java.util.Date createDate = DateFormatUtils.timestamp2date( rs.getTimestamp("createDate"));
                java.util.Date payDate = DateFormatUtils.timestamp2date( rs.getTimestamp("payDate"));
                java.util.Date deliveryDate = DateFormatUtils.timestamp2date( rs.getTimestamp("deliveryDate"));
                java.util.Date confirmDate = DateFormatUtils.timestamp2date( rs.getTimestamp("confirmDate"));
                bean.setOrderCode(orderCode);
                bean.setAddress(address);
                bean.setPost(post);
                bean.setReceiver(receiver);
                bean.setMobile(mobile);
                bean.setUserMessage(userMessage);
                bean.setCreateDate(createDate);
                bean.setPayDate(payDate);
                bean.setDeliveryDate(deliveryDate);
                bean.setConfirmDate(confirmDate);
                User user = new UserDAO().getById(uid);
                bean.setUser(user);
                bean.setStatus(status);

                bean.setId(id);
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }
        return bean;
    }

    public List<Order> list() {
        return list(0, Short.MAX_VALUE);
    }

    public List<Order> list(int start, int count) {
        List<Order> beans = new ArrayList<Order>();

        String sql = "select * from order_ order by id desc limit ?,? ";

        try (Connection c = DBConnPoolUtils.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {

            ps.setInt(1, start);
            ps.setInt(2, count);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Order bean = new Order();
                String orderCode =rs.getString("orderCode");
                String address = rs.getString("address");
                String post = rs.getString("post");
                String receiver = rs.getString("receiver");
                String mobile = rs.getString("mobile");
                String userMessage = rs.getString("userMessage");
                String status = rs.getString("status");
                java.util.Date createDate = DateFormatUtils.timestamp2date( rs.getTimestamp("createDate"));
                java.util.Date payDate = DateFormatUtils.timestamp2date( rs.getTimestamp("payDate"));
                java.util.Date deliveryDate = DateFormatUtils.timestamp2date( rs.getTimestamp("deliveryDate"));
                java.util.Date confirmDate = DateFormatUtils.timestamp2date( rs.getTimestamp("confirmDate"));
                int uid =rs.getInt("uid");
                int id = rs.getInt("id");
                bean.setId(id);
                bean.setOrderCode(orderCode);
                bean.setAddress(address);
                bean.setPost(post);
                bean.setReceiver(receiver);
                bean.setMobile(mobile);
                bean.setUserMessage(userMessage);
                bean.setCreateDate(createDate);
                bean.setPayDate(payDate);
                bean.setDeliveryDate(deliveryDate);
                bean.setConfirmDate(confirmDate);
                User user = new UserDAO().getById(uid);
                bean.setUser(user);
                bean.setStatus(status);
                beans.add(bean);
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }
        return beans;
    }

    public List<Order> list(int uid,String excludedStatus) {
        return list(uid,excludedStatus,0, Short.MAX_VALUE);
    }

    public List<Order> list(int uid, String excludedStatus, int start, int count) {
        List<Order> beans = new ArrayList<Order>();

        String sql = "select * from Order_ where uid = ? and status != ? order by id desc limit ?,? ";

        try (Connection c = DBConnPoolUtils.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {

            ps.setInt(1, uid);
            ps.setString(2, excludedStatus);
            ps.setInt(3, start);
            ps.setInt(4, count);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Order bean = new Order();
                String orderCode =rs.getString("orderCode");
                String address = rs.getString("address");
                String post = rs.getString("post");
                String receiver = rs.getString("receiver");
                String mobile = rs.getString("mobile");
                String userMessage = rs.getString("userMessage");
                String status = rs.getString("status");
                java.util.Date createDate = DateFormatUtils.timestamp2date( rs.getTimestamp("createDate"));
                java.util.Date payDate = DateFormatUtils.timestamp2date( rs.getTimestamp("payDate"));
                java.util.Date deliveryDate = DateFormatUtils.timestamp2date( rs.getTimestamp("deliveryDate"));
                Date confirmDate = DateFormatUtils.timestamp2date( rs.getTimestamp("confirmDate"));

                int id = rs.getInt("id");
                bean.setId(id);
                bean.setOrderCode(orderCode);
                bean.setAddress(address);
                bean.setPost(post);
                bean.setReceiver(receiver);
                bean.setMobile(mobile);
                bean.setUserMessage(userMessage);
                bean.setCreateDate(createDate);
                bean.setPayDate(payDate);
                bean.setDeliveryDate(deliveryDate);
                bean.setConfirmDate(confirmDate);
                User user = new UserDAO().getById(uid);
                bean.setStatus(status);
                bean.setUser(user);
                beans.add(bean);
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }
        return beans;
    }
}

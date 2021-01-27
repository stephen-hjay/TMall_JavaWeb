package com.hjay.tmall.DAO.Implement;

import com.hjay.tmall.DAO.DAO;
import com.hjay.tmall.Utils.DBConnPoolUtils;
import com.hjay.tmall.Entity.Bean;
import com.hjay.tmall.Entity.Implement.Order;
import com.hjay.tmall.Entity.Implement.OrderItem;
import com.hjay.tmall.Entity.Implement.Product;
import com.hjay.tmall.Entity.Implement.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class OrderItemDAO implements DAO {
    @Override
    public OrderItem getById(int id) {
        OrderItem bean = new OrderItem();

        try (Connection c = DBConnPoolUtils.getConnection(); Statement s = c.createStatement();) {

            String sql = "select * from OrderItem where id = " + id;

            ResultSet rs = s.executeQuery(sql);

            if (rs.next()) {
                int pid = rs.getInt("pid");
                int oid = rs.getInt("oid");
                int uid = rs.getInt("uid");
                int number = rs.getInt("number");
                Product product = new ProductDAO().getById(pid);
                User user = new UserDAO().getById(uid);
                bean.setProduct(product);
                bean.setUser(user);
                bean.setNumber(number);

                if(-1!=oid){
                    Order order= new OrderDAO().getById(oid);
                    bean.setOrder(order);
                }

                bean.setId(id);
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }
        return bean;
    }

    @Override
    public boolean add(Bean bean_father) {
        OrderItem bean = (OrderItem) bean_father;
        String sql = "insert into OrderItem values(null,?,?,?,?)";

        boolean result = false;
        try (Connection c = DBConnPoolUtils.getConnection(); PreparedStatement ps = c.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);) {

            ps.setInt(1, bean.getProduct().getId());

            //订单项在创建的时候，是没有订单信息的
            if(null==bean.getOrder())
                ps.setInt(2, -1);
            else
                ps.setInt(2, bean.getOrder().getId());

            ps.setInt(3, bean.getUser().getId());
            ps.setInt(4, bean.getNumber());
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

    @Override
    public boolean delete(int id) {
        boolean result = false;
        try (Connection c = DBConnPoolUtils.getConnection(); Statement s = c.createStatement();) {

            String sql = "delete from OrderItem where id = " + id;

            result = s.execute(sql);

        } catch (SQLException e) {

            e.printStackTrace();
        }
        return result;
    }

    @Override
    public List<? extends Bean> list() throws SQLException {
        return null;
    }

    @Override
    public List<? extends Bean> list(int start, int count) throws SQLException {
        return null;
    }

    @Override
    public boolean update(Bean bean_father) {
        OrderItem bean = (OrderItem) bean_father;
        String sql = "update OrderItem set pid= ?, oid=?, uid=?,number=?  where id = ?";
        boolean result = false;
        try (Connection c = DBConnPoolUtils.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {
            ps.setInt(1, bean.getProduct().getId());
            if(null==bean.getOrder())
                ps.setInt(2, -1);
            else
                ps.setInt(2, bean.getOrder().getId());
            ps.setInt(3, bean.getUser().getId());
            ps.setInt(4, bean.getNumber());

            ps.setInt(5, bean.getId());
            result = ps.execute();

        } catch (SQLException e) {

            e.printStackTrace();
        }
        return result;
    }

    @Override
    public int getTotal() {
        int total = 0;
        try (Connection c = DBConnPoolUtils.getConnection(); Statement s = c.createStatement();) {

            String sql = "select count(*) from OrderItem";

            ResultSet rs = s.executeQuery(sql);
            while (rs.next()) {
                total = rs.getInt(1);
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }
        return total;
    }

    public List<OrderItem> listByOrder(int oid) {
        return listByOrder(oid, 0, Short.MAX_VALUE);
    }


    // get a list of orderItems of a order
    public List<OrderItem> listByOrder(int oid, int start, int count) {
        List<OrderItem> beans = new ArrayList<OrderItem>();

        String sql = "select * from OrderItem where oid = ? order by id desc limit ?,? ";

        try (Connection c = DBConnPoolUtils.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {

            ps.setInt(1, oid);
            ps.setInt(2, start);
            ps.setInt(3, count);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                OrderItem bean = new OrderItem();
                int id = rs.getInt(1);

                int pid = rs.getInt("pid");
                int uid = rs.getInt("uid");
                int number = rs.getInt("number");

                Product product = new ProductDAO().getById(pid);
                if(-1!=oid){
                    Order order= new OrderDAO().getById(oid);
                    bean.setOrder(order);
                }

                User user = new UserDAO().getById(uid);
                bean.setProduct(product);

                bean.setUser(user);
                bean.setNumber(number);
                bean.setId(id);
                beans.add(bean);
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }
        return beans;
    }

    public void fill(List<Order> os) {
        for (Order o : os) {
            List<OrderItem> ois=listByOrder(o.getId());
            float total = 0;
            int totalNumber = 0;
            for (OrderItem oi : ois) {
                total+=oi.getNumber()*oi.getProduct().getPromotePrice();
                totalNumber+=oi.getNumber();
            }
            o.setTotal(total);
            o.setOrderItems(ois);
            o.setTotalNumber(totalNumber);
        }

    }

    public void fill(Order o) {
        List<OrderItem> ois=listByOrder(o.getId());
        float total = 0;
        for (OrderItem oi : ois) {
            total+=oi.getNumber()*oi.getProduct().getPromotePrice();
        }
        o.setTotal(total);
        o.setOrderItems(ois);
    }

    public List<OrderItem> listByProduct(int pid) {
        return listByProduct(pid, 0, Short.MAX_VALUE);
    }

    public List<OrderItem> listByProduct(int pid, int start, int count) {
        List<OrderItem> beans = new ArrayList<OrderItem>();

        String sql = "select * from OrderItem where pid = ? order by id desc limit ?,? ";

        try (Connection c = DBConnPoolUtils.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {

            ps.setInt(1, pid);
            ps.setInt(2, start);
            ps.setInt(3, count);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                OrderItem bean = new OrderItem();
                int id = rs.getInt(1);

                int uid = rs.getInt("uid");
                int oid = rs.getInt("oid");
                int number = rs.getInt("number");

                Product product = new ProductDAO().getById(pid);
                if(-1!=oid){
                    Order order= new OrderDAO().getById(oid);
                    bean.setOrder(order);
                }

                User user = new UserDAO().getById(uid);
                bean.setProduct(product);

                bean.setUser(user);
                bean.setNumber(number);
                bean.setId(id);
                beans.add(bean);
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }
        return beans;
    }

    public int getSaleCount(int pid) {
        int total = 0;
        try (Connection c = DBConnPoolUtils.getConnection(); Statement s = c.createStatement();) {

            String sql = "select sum(number) from OrderItem where pid = " + pid;

            ResultSet rs = s.executeQuery(sql);
            while (rs.next()) {
                total = rs.getInt(1);
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }
        return total;
    }
    public List<OrderItem> listByUser(int uid) {
        return listByUser(uid, 0, Short.MAX_VALUE);
    }

    public List<OrderItem> listByUser(int uid, int start, int count) {
        List<OrderItem> beans = new ArrayList<OrderItem>();

        String sql = "select * from OrderItem where uid = ? and oid=-1 order by id desc limit ?,? ";

        try (Connection c = DBConnPoolUtils.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {

            ps.setInt(1, uid);
            ps.setInt(2, start);
            ps.setInt(3, count);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                OrderItem bean = new OrderItem();
                int id = rs.getInt(1);

                int pid = rs.getInt("pid");
                int oid = rs.getInt("oid");
                int number = rs.getInt("number");


                Product product = new ProductDAO().getById(pid);
                if(-1!=oid){
                    Order order= new OrderDAO().getById(oid);
                    bean.setOrder(order);
                }

                User user = new UserDAO().getById(uid);
                bean.setProduct(product);

                bean.setUser(user);
                bean.setNumber(number);
                bean.setId(id);
                beans.add(bean);
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }
        return beans;
    }


}


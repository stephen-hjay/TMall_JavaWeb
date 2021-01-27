package com.hjay.tmall.DAO.Implement;

import com.hjay.tmall.DAO.DAO;
import com.hjay.tmall.Utils.DBConnPoolUtils;
import com.hjay.tmall.Entity.Bean;
import com.hjay.tmall.Entity.Implement.Product;
import com.hjay.tmall.Entity.Implement.Property;
import com.hjay.tmall.Entity.Implement.PropertyValue;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


// used for connect Property and Product
public class PropertyValueDAO implements DAO {

    // useless just for DAO
    public int getTotal() {
        int total = 0;
        try (Connection c = DBConnPoolUtils.getConnection(); Statement s = c.createStatement();) {
            String sql = "select count(*) from PropertyValue";
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
        PropertyValue bean = (PropertyValue) bean_father;
        String sql = "insert into PropertyValue values(null,?,?,?)";
        boolean result = false;
        try (Connection c = DBConnPoolUtils.getConnection(); PreparedStatement ps = c.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);) {

            ps.setInt(1, bean.getProduct().getId());
            ps.setInt(2, bean.getProperty().getId());
            ps.setString(3, bean.getValue());
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

    // set PropertyValue based on Product and Property
    public boolean update(Bean bean_father) {
        PropertyValue bean = (PropertyValue) bean_father;
        String sql = "update PropertyValue set pid= ?, ptid=?, value=?  where id = ?";
        boolean result = false;
        try (Connection c = DBConnPoolUtils.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {
            ps.setInt(1, bean.getProduct().getId());
            ps.setInt(2, bean.getProperty().getId());
            ps.setString(3, bean.getValue());
            ps.setInt(4, bean.getId());
            result = ps.execute();

        } catch (SQLException e) {

            e.printStackTrace();
        }
        return result;
    }







    public boolean delete(int id) {
        boolean result = false;
        try (Connection c = DBConnPoolUtils.getConnection(); Statement s = c.createStatement();) {
            String sql = "delete from PropertyValue where id = " + id;
            result = s.execute(sql);
        } catch (SQLException e) {

            e.printStackTrace();
        }
        return result;
    }
    // useless just for DAO
    public PropertyValue getById(int id) {
        PropertyValue bean = new PropertyValue();
        try (Connection c = DBConnPoolUtils.getConnection(); Statement s = c.createStatement();) {
            String sql = "select * from PropertyValue where id = " + id;
            ResultSet rs = s.executeQuery(sql);
            if (rs.next()) {
                int pid = rs.getInt("pid");
                int ptid = rs.getInt("ptid");
                String value = rs.getString("value");
                Product product = new ProductDAO().getById(pid);
                Property property = new PropertyDAO().getById(ptid);
                bean.setProduct(product);
                bean.setProperty(property);
                bean.setValue(value);
                bean.setId(id);
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }
        return bean;
    }

    /**
     * this method could return search result based product and property name
     * @param ptid
     * @param pid
     * @return
     */
    public PropertyValue getByPropertyIDandProductID(int ptid, int pid ) {
        PropertyValue bean = null;
        try (Connection c = DBConnPoolUtils.getConnection(); Statement s = c.createStatement();) {

            String sql = "select * from PropertyValue where ptid = " + ptid + " and pid = " + pid;

            ResultSet rs = s.executeQuery(sql);

            if (rs.next()) {
                bean= new PropertyValue();
                int id = rs.getInt("id");

                String value = rs.getString("value");

                Product product = new ProductDAO().getById(pid);
                Property property = new PropertyDAO().getById(ptid);
                bean.setProduct(product);
                bean.setProperty(property);
                bean.setValue(value);
                bean.setId(id);
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }
        return bean;
    }
    // useless just for DAO
    public List<PropertyValue> list() {
        return list(0, Short.MAX_VALUE);
    }
    // useless just for DAO
    public List<PropertyValue> list(int start, int count) {
        List<PropertyValue> beans = new ArrayList<PropertyValue>();

        String sql = "select * from PropertyValue order by id desc limit ?,? ";

        try (Connection c = DBConnPoolUtils.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {

            ps.setInt(1, start);
            ps.setInt(2, count);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                PropertyValue bean = new PropertyValue();
                int id = rs.getInt(1);

                int pid = rs.getInt("pid");
                int ptid = rs.getInt("ptid");
                String value = rs.getString("value");

                Product product = new ProductDAO().getById(pid);
                Property property = new PropertyDAO().getById(ptid);
                bean.setProduct(product);
                bean.setProperty(property);
                bean.setValue(value);
                bean.setId(id);
                beans.add(bean);
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }
        return beans;
    }

    // init a propertyValue
    // 初始化某个产品对应的属性值，初始化逻辑：
    //1. 根据分类获取所有的属性
    //2. 遍历每一个属性
    //2.1 根据属性和产品，获取属性值
    //2.2 如果属性值不存在，就创建一个属性值对象
    public void init(Product p) {
        // obtain product category and get all the properties belong to that specific category
        List<Property> pts= new PropertyDAO().list(p.getCategory().getId());
        for (Property pt: pts) {
            PropertyValue pv = getByPropertyIDandProductID(pt.getId(),p.getId());
            // if that propertyvalue does not exist, then add it
            if(null==pv){
                pv = new PropertyValue();
                pv.setProduct(p);
                pv.setProperty(pt);
                this.add(pv);
            }
        }
    }

    // 查询某个产品下所有的属性值
    public List<PropertyValue> list(int pid) {
        List<PropertyValue> beans = new ArrayList<PropertyValue>();
        // property id -> pid and ptid -> property id
        String sql = "select * from PropertyValue where pid = ? order by ptid desc";

        try (Connection c = DBConnPoolUtils.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {

            ps.setInt(1, pid);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                PropertyValue bean = new PropertyValue();
                int id = rs.getInt(1);

                int ptid = rs.getInt("ptid");
                String value = rs.getString("value");

                Product product = new ProductDAO().getById(pid);
                Property property = new PropertyDAO().getById(ptid);
                bean.setProduct(product);
                bean.setProperty(property);
                bean.setValue(value);
                bean.setId(id);
                beans.add(bean);
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }
        return beans;
    }

}


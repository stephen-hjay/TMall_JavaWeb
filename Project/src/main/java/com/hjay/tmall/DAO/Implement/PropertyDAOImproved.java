package com.hjay.tmall.DAO.Implement;


import com.hjay.tmall.DAO.DAO;
import com.hjay.tmall.Utils.DBConnPoolUtils;
import com.hjay.tmall.Entity.Bean;
import com.hjay.tmall.Entity.Implement.Category;
import com.hjay.tmall.Entity.Implement.Property;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 还没修改
 */

/**
 * CREATE TABLE property (
 *   id int(11) UNIQUE NOT NULL AUTO_INCREMENT,
 *   cid int(11) DEFAULT NULL,
 *   name varchar(255) DEFAULT NULL,
 *   PRIMARY KEY (id),
 *   CONSTRAINT fk_property_category FOREIGN KEY (cid) REFERENCES category (id)
 * ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
 */


// property needs to referenced by category id (a category has certain types of properties)
public class PropertyDAOImproved implements DAO {
    @Override
    public Property getById(int id) {
        Property bean = new Property();
        try (Connection c = DBConnPoolUtils.getConnection(); Statement s = c.createStatement();) {

            String sql = "select * from property where id = " + id;

            ResultSet rs = s.executeQuery(sql);

            if (rs.next()) {
                String name = rs.getString("name");
                int cid = rs.getInt("cid");
                bean.setName(name);
                Category category = new CategoryDAO().getById(cid);
                bean.setCategory(category);
                bean.setId(id);
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }
        return bean;
    }

    @Override
    public boolean add(Bean bean_father) {
        Property bean = (Property) bean_father;
        String sql = "insert into property values(null,?,?)";
        boolean result = false;
        try (Connection c = DBConnPoolUtils.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {
            ps.setInt(1, bean.getCategory().getId());
            ps.setString(2, bean.getName());
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
            String sql = "delete from property where id = " + id;
            result = s.execute(sql);
        } catch (SQLException e) {

            e.printStackTrace();
        }
        return result;
    }

    @Override
    public List<Property> list() throws SQLException {
        return null;
    }

    @Override
    public List<Property> list(int start, int count) throws SQLException {
        return null;
    }
    // search for all propoerties of a certain category
    public List<Property> list(int cid) {
        return list(cid, 0, Short.MAX_VALUE);
    }

    // search for  propoerties of a certain category for paging
    public List<Property> list(int cid, int start, int count) {
        List<Property> beans = new ArrayList<Property>();

        String sql = "select * from property where cid = ? order by id desc limit ?,? ";

        try (Connection c = DBConnPoolUtils.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {

            ps.setInt(1, cid);
            ps.setInt(2, start);
            ps.setInt(3, count);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Property bean = new Property();
                int id = rs.getInt(1);
                String name = rs.getString("name");
                bean.setName(name);
                Category category = new CategoryDAO().getById(cid);
                bean.setCategory(category);
                bean.setId(id);

                beans.add(bean);
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }
        return beans;
    }


    @Override
    public boolean update(Bean bean_father) {
        Property bean = (Property) bean_father;
        String sql = "update property set cid= ?, name=? where id = ?";
        boolean result = false;
        try (Connection c = DBConnPoolUtils.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {
            ps.setInt(1, bean.getCategory().getId());
            ps.setString(2, bean.getName());
            ps.setInt(3, bean.getId());
            result = ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public int getTotal() {
        return 0;
    }

   // search for total count of propoerties of a certain category
    public int getTotal(int cid) {
        int total = 0;
        try (Connection c = DBConnPoolUtils.getConnection(); Statement s = c.createStatement();) {

            String sql = "select count(*) from property where cid =" + cid;

            ResultSet rs = s.executeQuery(sql);
            while (rs.next()) {
                total = rs.getInt(1);
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }
        return total;
    }
}

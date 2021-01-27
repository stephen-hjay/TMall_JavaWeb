package com.hjay.tmall.DAO.Implement;

import com.hjay.tmall.DAO.DAO;
import com.hjay.tmall.Utils.DBConnPoolUtils;
import com.hjay.tmall.Entity.Bean;
import com.hjay.tmall.Entity.Implement.Product;
import com.hjay.tmall.Entity.Implement.ProductImage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductImageDAO implements DAO {
    public static final String type_single = "type_single";
    public static final String type_detail = "type_detail";

    @Override
    public ProductImage getById(int id) {
        ProductImage bean = new ProductImage();
        try (Connection c = DBConnPoolUtils.getConnection(); Statement s = c.createStatement();) {

            String sql = "select * from ProductImage where id = " + id;
            ResultSet rs = s.executeQuery(sql);

            if (rs.next()) {
                int pid = rs.getInt("pid");
                String type = rs.getString("type");
                Product product = new ProductDAO().getById(pid);
                bean.setProduct(product);
                bean.setType(type);
                bean.setId(id);
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }
        return bean;
    }

    @Override
    public boolean add(Bean bean_father) {
        ProductImage bean = (ProductImage) bean_father;
        String sql = "insert into ProductImage values(null,?,?)";
        boolean result = false;
        try (Connection c = DBConnPoolUtils.getConnection(); PreparedStatement ps = c.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);) {
            ps.setInt(1, bean.getProduct().getId());
            ps.setString(2, bean.getType());
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

            String sql = "delete from ProductImage where id = " + id;

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

    // product images for a product
    public List<ProductImage> list(Product p, String type) {
        return list(p, type,0, Short.MAX_VALUE);
    }

    public List<ProductImage> list(Product p, String type, int start, int count) {
        List<ProductImage> beans = new ArrayList<ProductImage>();

        String sql = "select * from ProductImage where pid =? and type =? order by id desc limit ?,? ";

        try (Connection c = DBConnPoolUtils.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {

            ps.setInt(1, p.getId());
            ps.setString(2, type);

            ps.setInt(3, start);
            ps.setInt(4, count);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                ProductImage bean = new ProductImage();
                int id = rs.getInt(1);

                bean.setProduct(p);
                bean.setType(type);
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
        return true;
    }

    @Override
    public int getTotal() {
        int total = 0;
        try (Connection c = DBConnPoolUtils.getConnection(); Statement s = c.createStatement();) {

            String sql = "select count(*) from ProductImage";

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
